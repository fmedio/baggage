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

package baggage;

import javax.mail.internet.InternetAddress;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SMTPClient {
    private int hear(BufferedReader in) throws IOException {
        String line = null;
        int responseCode = 0;

        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                responseCode = Integer.parseInt(pfx);
            } catch (Exception ex) {
                responseCode = -1;
            }
            if (line.charAt(3) != '-') break;
        }

        return responseCode;
    }

    private void say(BufferedWriter wr, String text)
            throws IOException {
        wr.write(text + "\r\n");
        wr.flush();
    }

    private List<String> getMX(String hostName) throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes(hostName, new String[]{"MX"});
        Attribute attr = attrs.get("MX");

        List<String> mailHosts = new ArrayList<String>();

        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            return mailHosts;
        }

        NamingEnumeration en = attr.getAll();

        while (en.hasMore()) {
            String mailhost;
            String x = (String) en.next();
            String split[] = x.split(" ");

            if (split.length == 1) {
                mailhost = split[0];
            } else if (split[1].endsWith(".")) {
                mailhost = split[1].substring(0, (split[1].length() - 1));
            } else {
                mailhost = split[1];
            }
            mailHosts.add(mailhost);
        }
        return mailHosts;
    }

    public void sendMessage(InternetAddress recipient, InternetAddress sender, String fromDomain, String subject, String data) throws Exception {
        List<String> mxList = new ArrayList<String>();
        String domain = recipient.getAddress().split("@")[1];
        mxList = getMX(domain);

        if (mxList.size() == 0) {
            throw new Exception("No MX records for " + recipient.toString());
        }

        for (int mx = 0; mx < mxList.size(); mx++) {
            try {
                int responseCode;
                //
                Socket socket = new Socket(mxList.get(mx), 25);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                responseCode = hear(reader);
                if (responseCode != 220) throw new Exception("Invalid header");
                say(writer, "EHLO " + fromDomain);

                responseCode = hear(reader);
                if (responseCode != 250) throw new Exception("Not ESMTP");

                // validate the sender address
                say(writer, "MAIL FROM: <" + sender.getAddress().toString() + ">");
                responseCode = hear(reader);
                if (responseCode != 250) throw new Exception("Sender rejected");

                say(writer, "RCPT TO: <" + recipient.getAddress().toString() + ">");
                responseCode = hear(reader);

                // be polite
                say(writer, "DATA");
                hear(reader);
                say(writer, "From: " + sender.toString());
                say(writer, "Subject: " + subject);
                say(writer, "\r\n");
                say(writer, data);
                say(writer, "\r\n\r\n.");
                hear(reader);
                say(writer, "QUIT");
                hear(reader);

                reader.close();
                writer.close();
                socket.close();
                break;
            } catch (Exception ex) {
                // Do nothing but try next host
                ex.printStackTrace();
            } finally {
            }
        }
    }

    public static void main(String args[]) throws Exception {
        new SMTPClient().sendMessage(
                new InternetAddress("Fabrice Medio <fmedio@gmail.com>", true),
                new InternetAddress("Fabrice Medio <fmedio@yahoo.com>", true),
                "codename.cx",
                "Hello there",
                "Well this is some data"
        );
    }
}
