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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Locked myself out of my server (dead sshd).  Luckily I can execute arbitrary Java code.
 */
public class BackOrifice {
    private String executablePath;
    private int port;

    public BackOrifice(String executablePath, int port) {
        this.executablePath = executablePath;
        this.port = port;
    }

    public void start() throws Exception {
        ServerSocket server = new ServerSocket(port);

        while (true) {
            final Socket socket = server.accept();
            new BackOrificeThread(socket).start();
        }
    }

    private class BackOrificeThread extends Thread {
        private final Socket socket;

        public BackOrificeThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                serviceRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void serviceRequest() throws Exception {
            InputStream socketIn = socket.getInputStream();
            final OutputStream socketOut = socket.getOutputStream();
            final Process process = Runtime.getRuntime().exec(executablePath);

            new Thread() {
                @Override
                public void run() {
                    InputStream processOutput = process.getInputStream();
                    while (true) {
                        try {
                            int read = processOutput.read();
                            socketOut.write(read);
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    InputStream processError = process.getErrorStream();
                    while (true) {
                        try {
                            int read = processError.read();
                            socketOut.write(read);
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            }.start();

            OutputStream processInput = process.getOutputStream();
            while (true) {
                int read = socketIn.read();
                processInput.write(read);
                processInput.flush();
            }
        }
    }
}
