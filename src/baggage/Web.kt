import baggage.Bag
import baggage.ListBag
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.text.WordUtils
import org.codehaus.jackson.map.ObjectMapper
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

public class WebApp {
    fun start(httpPort: Int, verbs: Map<String, () -> Verb<Any>>) {
        val server = Server()
        val connector = SelectChannelConnector()
        connector.setPort(httpPort)
        connector.setHost("0.0.0.0")
        server.setConnectors(array<Connector>(connector))
        val contextHandler = ServletContextHandler()
        contextHandler.setContextPath("/")
        server.setHandler(contextHandler)
        var dispatcher = Dispatcher(verbs)
        contextHandler.addServlet(ServletHolder(dispatcher), "/")
        server.setHandler(contextHandler)
        server.start()
        server.join()
    }
}

class JsonResource(val o: Any, override val statusCode: Int = 200): Resource {
    override val contentType: String = "application/json; charset=UTF-8"
    override val headers: Map<String, String> = mapOf()
    override val cookies: Array<Cookie> = array()
    override fun renderTo(stream: OutputStream) {
        var objectMapper = ObjectMapper()
        objectMapper.writeValue(stream, o)
        stream.flush();
    }
}

class HttpRedirect(val target: String) : Resource {
    override val contentType: String = "text/plain; charset=UTF-8"
    override val statusCode: Int = HttpServletResponse.SC_MOVED_TEMPORARILY
    override val headers: Map<String, String> = mapOf("Location" to target)
    override val cookies: Array<Cookie> = array()
    override fun renderTo(stream: OutputStream) {
    }

}

class Crypto(private val spec: SecretKey) {
    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance("AES")!!
        var cipherBytes = Base64.decodeBase64(cipherText)
        cipher.init(Cipher.DECRYPT_MODE, spec)
        var decryptedBytes = cipher.doFinal(cipherBytes)!!
        return String(decryptedBytes)
    }

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance("AES")!!
        cipher.init(Cipher.ENCRYPT_MODE, spec)
        var encrypted = cipher.doFinal(plainText.getBytes())
        return Base64.encodeBase64URLSafeString(encrypted)!!
    }
}

public class TextResource(val text: String, val httpStatusCode: Int, override val cookies: Array<Cookie>): Resource {
    override val contentType: String = "text/plain"
    override val statusCode: Int = httpStatusCode
    override val headers: Map<String, String> = mapOf()
    override fun renderTo(stream: OutputStream) {
        val writer = BufferedWriter(OutputStreamWriter(stream))
        writer.write(text)
        writer.flush()
    }
}

public class Dispatcher(private val verbs: Map<String, () -> Verb<Any>>): HttpServlet() {
    protected override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        service(req, resp)
    }

    protected override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        service(req, resp)
    }

    protected override fun service(req: HttpServletRequest?, resp: HttpServletResponse?) {
        var name: String? = req?.getRequestURI()?.replaceAll("^" + req?.getServletPath() + "/", "")?.replaceAll("^/", "")
        var resource = TextResource("Four, oh, four.", 404, array()) as Resource

        if (name!!.toLowerCase().equals("")) {
            resource = StaticResource("index.html")
        } else  if (name!!.contains(".")) {
            val staticResource = StaticResource(name!!)
            if (staticResource.exists)
                resource = staticResource
        } else if (verbs.containsKey(name)) {
            val factory = verbs.get(name)
            val verb = factory!!()
            val arg = ArgSerializer().deserialize(verb.argType, ParameterFactory().getParameters(req!!))
            val cookies = req.getCookies()!!.map { c -> c!! as Cookie }
            try {
                resource = verb.execute(arg, cookies)
            } catch (e: Exception) {
                resource = ErrorPage(e)
            }
        }

        resp!!.setContentType(resource.contentType);
        resp.setStatus(resource.statusCode);

        val out = GZIPOutputStream(BufferedOutputStream(resp.getOutputStream()!!, 1024 * 1024))
        resp.addHeader("Content-Encoding", "gzip")
        resp.addHeader("Server", "Web.kt")
        resource.cookies.forEach { c -> resp.addCookie(c) }
        resource.headers.keySet().forEach { k -> resp.addHeader(k, resource.headers.get(k)) }
        resource.renderTo(out)
        out.flush()
        out.close()
    }
}

class ErrorPage(val t: Throwable) : Resource {
    override val contentType: String = "text/plain; charset=UTF-8"
    override val statusCode: Int = 500
    override val headers: Map<String, String> = mapOf()
    override val cookies: Array<Cookie> = array()
    override fun renderTo(stream: OutputStream) {
        var baos = ByteArrayOutputStream()
        var pw = PrintWriter(baos)
        t.printStackTrace(pw)
        pw.flush()
        val stackTrace = String(baos.toByteArray())

        val writer = OutputStreamWriter(stream)
        writer.write("Oops! ")
        //writer.write(t.getMessage()!!)
        //writer.write("\n")
        writer.write(stackTrace)
        writer.write("\n")
        writer.flush();
    }
}

internal val contentTypes = mapOf(
        ".js" to "text/javascript; charset=UTF-8",
        ".txt" to "text/plain; charset=UTF-8",
        ".css" to "text/css; charset=UTF-8",
        ".html" to "text/html; charset=UTF-8",
        ".png" to "image/png",
        ".ico" to "image/bitmap"
)

public class StaticResource(val name: String): Resource {
    private val extension = when {
        name.contains(".") -> name.subSequence(name.lastIndexOf('.'), name.length).toString().toLowerCase()
        else -> ""
    }

    private val filePath = "web/" + name
    private val file = File(filePath)

    val exists = file.exists()

    override val contentType: String = contentTypes.getOrElse(extension, {() -> "application/octet-stream" })
    override val statusCode: Int = 200
    override val headers: Map<String, String> = mapOf()
    override val cookies: Array<Cookie> = array()
    override fun renderTo(output: OutputStream) {
        val input = FileInputStream(file)
        IOUtils.copy(input, output);
    }
}

public class ParameterFactory() {
    public fun getParameters(servletRequest: HttpServletRequest): Bag<String, String> {
        var parameters = ListBag<String, String>()
        var names = servletRequest.getParameterNames()!!
        while (names.hasMoreElements()) {
            var name = (names.nextElement() as String)
            for (value in servletRequest.getParameterValues(name)!!) {
                parameters.put(name, value)
            }
        }
        return parameters
    }

    public fun getHeaders(servletRequest: HttpServletRequest): Bag<String, String> {
        var result = ListBag<String, String>()
        var headers = servletRequest.getHeaderNames()!!
        while (headers.hasMoreElements()) {
            var headerName = headers.nextElement()!!
            val headerValues = servletRequest.getHeaders(headerName)!!
            while (headerValues.hasMoreElements()) {
                var headerValue = headerValues.nextElement()
                result.put(headerName, headerValue)
            }
        }
        return result
    }
}

public trait Resource {
    val contentType: String
    val statusCode: Int
    val headers: Map<String, String>
    val cookies: Array<Cookie>
    fun renderTo(stream: OutputStream)
}

public trait Verb<TArg> {
    val argType: Class<TArg>
    fun execute(arg: TArg, cookies: Collection<Cookie>): Resource
}

public class ArgSerializer {
    fun deserialize<T>(klass: Class<T>, arg: Bag<String, String>): T {
        if (javaClass<Int>().equals(klass)) {
            if (arg.keySet()!!.size == 0) {
                throw RuntimeException("No values provided")
            }
            val key = arg.keySet()!!.first()
            val stringVal = arg.get(key)!!
            return java.lang.Integer.parseInt(stringVal) as T
        }

        if (javaClass<String>().equals(klass)) {
            if (arg.keySet()!!.size == 0) {
                throw RuntimeException("No values provided")
            }
            val key = arg.keySet()!!.first()
            return arg.get(key)!! as T
        }

        val o = klass.newInstance()
        arg.keySet()?.forEach { k ->
            val getterName = "get" + WordUtils.capitalizeFully(k)!!
            val hasGetter = o.javaClass.getMethods().any { method -> method.getName() == getterName }
            if (hasGetter) {
                val getter = o.javaClass.getMethod(getterName)
                val returnType = getter.getReturnType().toString()
                val setter = o.javaClass.getMethod("set" + WordUtils.capitalizeFully(k)!!, getter.getReturnType()!!)
                val stringValue = arg.get(k)!!
                when (returnType) {
                    "long" -> setter.invoke(o, java.lang.Long.parseLong(stringValue)!!)
                    "int" -> setter.invoke(o, java.lang.Integer.parseInt(stringValue)!!)
                    "class java.lang.String" -> setter.invoke(o, stringValue)
                    else -> throw RuntimeException("Unsupported type '" + returnType + "'")
                }
            }
        }
        return o
    }
}

