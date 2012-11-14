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

import clutter.Bag;
import clutter.SetBag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class OpeningTagParser {
    private State state;

    public OpeningTag parse(String s) {
        state = State.whiteSpace;
        BufferedReader reader = new BufferedReader(new StringReader(s));
        String tagName = nextToken(reader);
        List<String> attributes = new ArrayList<String>();
        for (String token = nextToken(reader); token != null; token = nextToken(reader)) {
            attributes.add(token);
        }

        if (attributes.size() % 2 == 1 && "/".equals(attributes.get(attributes.size() - 1))) {
            attributes.remove(attributes.size() - 1);
        }

        Bag<String, String> bag = new SetBag<String, String>();

        try {
            for (int i = 0; i < attributes.size(); i += 2) {
                bag.put(attributes.get(i), attributes.get(i + 1));
            }
        } catch (Exception e) {
            // don't care
        }

        return new OpeningTag(tagName, bag);
    }

    private static enum State {
        whiteSpace,
        token,
        doubleQuotedSentence,
        singleQuotedSentence
    }

    private String nextToken(Reader reader) {
        try {
            return next(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String next(Reader reader) throws IOException {
        StringBuilder currentToken = new StringBuilder();

        for (int c = reader.read(); c != -1; c = reader.read()) {
            if (state == State.whiteSpace) {
                if (c == '"') {
                    state = State.doubleQuotedSentence;
                } else if (c == '\'') {
                    state = State.singleQuotedSentence;
                } else if (Character.isWhitespace(c) || c == '=') {
                    state = State.whiteSpace;
                } else {
                    state = State.token;
                    currentToken = new StringBuilder();
                    currentToken.append((char) c);
                    if (c == '\\') {
                        currentToken.append((char) reader.read());
                    }
                }
            } else if (state == State.token) {
                if (c == '"') {
                    String result = currentToken.toString();
                    state = State.doubleQuotedSentence;
                    currentToken = new StringBuilder();
                    return result;
                } else if (c == '\'') {
                    String result = currentToken.toString();
                    state = State.singleQuotedSentence;
                    currentToken = new StringBuilder();
                    return result;
                } else if (Character.isWhitespace(c) || c == '=') {
                    String result = currentToken.toString();
                    state = State.whiteSpace;
                    currentToken = new StringBuilder();
                    return result;
                } else {
                    currentToken.append((char) c);
                    if (c == '\\') {
                        currentToken.append((char) reader.read());
                    }
                }
            } else if (state == State.doubleQuotedSentence) {
                if (c == '"') {
                    String result = currentToken.toString();
                    state = State.whiteSpace;
                    currentToken = new StringBuilder();
                    return result;
                } else {
                    currentToken.append((char) c);
                    if (c == '\\') {
                        currentToken.append((char) reader.read());
                    }
                }
            } else if (state == State.singleQuotedSentence) {
                if (c == '\'') {
                    String result = currentToken.toString();
                    state = State.whiteSpace;
                    currentToken = new StringBuilder();
                    return result;
                } else {
                    currentToken.append((char) c);
                    if (c == '\\') {
                        currentToken.append((char) reader.read());
                    }
                }
            }
        }

        if (currentToken.length() != 0) {
            return currentToken.toString();
        } else {
            return null;
        }
    }
}
