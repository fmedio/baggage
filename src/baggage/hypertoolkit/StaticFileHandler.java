/*
 * The MIT License
 *
 * Copyright (c) 2010 Fabrice Medio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package baggage.hypertoolkit;

import baggage.hypertoolkit.views.FourOhFourPage;
import baggage.hypertoolkit.views.InputStreamResource;
import baggage.hypertoolkit.views.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class StaticFileHandler implements RequestHandler {

    public static final Map<String, String> MIME_TYPES;
    private String webDir;

    static {
        String[][] EXTENSIONS = new String[][]{
                {".gif", "image/gif"},
                {".pdf", "application/pdf"},
                {".doc", "application/vnd.ms-word"},
                {".jpg", "image/jpg"},
                {".jpeg", "image/jpeg"},
                {".png", "image/png"},
                {".ico", "image/bmp"},
                {".css", "text/css"},
                {".js", "text/javascript"},
                {".txt", "text/plain"},
                {".html", "text/html"},
                {".gz", "application/gzip"}
        };

        MIME_TYPES = new HashMap<>();
        for (String[] mapping : EXTENSIONS) {
            MIME_TYPES.put(mapping[0], mapping[1]);
        }
    }


    public StaticFileHandler(String webDir) {
        this.webDir = webDir;
    }

    @Override
    public boolean log() {
        return false;
    }

    @Override
    public Resource handle(HttpServletRequest request) throws Exception {
        String resource = request.getRequestURI();
        String mimeType = MIME_TYPES.get(getExtension(resource));

        File file = new File(webDir, resource);
        if (!file.exists()) {
            return new FourOhFourPage();
        }

        return new InputStreamResource(mimeType, new FileInputStream(file));
    }

    private String getExtension(String resource) {
        int offset = resource.lastIndexOf('.');
        return resource.substring(offset);
    }
}
