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

package clutter.guerilla;

import java.io.*;

public class GuerillaParser {
    private Reader reader;
    private State state;

    public GuerillaParser(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        state = State.text;
    }

    private static enum State {
        text,
        docType,
        openingTag,
        closingTag
    }

    public GuerillaType next(boolean ignoreText) {
        try {
            return nextToken(ignoreText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GuerillaType nextToken(boolean ignoreText) throws IOException {
        StringBuilder currentToken = new StringBuilder();

        for (int c = markAndRead(); c != -1; c = markAndRead()) {
            if (state == State.text) {
                reader.mark(2);
                int[] readAhead = new int[2];
                readAhead[0] = reader.read();
                readAhead[1] = reader.read();
                if (readAhead[0] == '<' && readAhead[1] == '!') {
                    state = State.docType;
                    currentToken.append((char) c);
                    TextItem returnValue = new TextItem(currentToken.toString());
                    currentToken = new StringBuilder();
                    if (!ignoreText) {
                        return returnValue;
                    }
                } else if (readAhead[0] == '<' && readAhead[1] == '/') {
                    state = State.closingTag;
                    currentToken.append((char) c);
                    TextItem returnValue = new TextItem(currentToken.toString());
                    currentToken = new StringBuilder();
                    if (!ignoreText) {
                        return returnValue;
                    }
                } else {
                    reader.reset();
                    if (c == '<') {
                        state = State.openingTag;
                        TextItem textItem = new TextItem(currentToken.toString());
                        currentToken = new StringBuilder();
                        if (!ignoreText) {
                            return textItem;
                        }
                    } else {
                        currentToken.append((char) c);
                    }
                }
            } else if (state == State.docType) {
                if (c == '>') {
                    state = State.text;
                    DoctypeDeclaration declaration = new DoctypeDeclaration(currentToken.toString());
                    currentToken = new StringBuilder();
                    return declaration;
                } else {
                    currentToken.append((char) c);
                }
            } else if (state == State.openingTag) {
                if (c == '>') {
                    state = State.text;
                    OpeningTag returnValue = new OpeningTagParser().parse(currentToken.toString());
                    currentToken = new StringBuilder();
                    return returnValue;
                } else {
                    currentToken.append((char) c);
                }
            } else if (state == State.closingTag) {
                if (c == '>') {
                    state = State.text;
                    ClosingTag returnValue = new ClosingTag(currentToken.toString());
                    currentToken = new StringBuilder();
                    return returnValue;
                } else {
                    currentToken.append((char) c);
                }
            }
        }

        return null;
    }

    private int markAndRead() throws IOException {
        reader.mark(2);
        return reader.read();
    }
}
