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

import org.apache.log4j.Logger;

public class Log {
    public static void error(Object source, String message, Throwable throwable) {
        getLogger(source).error(message, throwable);
    }

    public static void error(Object source, String message) {
        getLogger(source).error(message);
    }

    public static void info(Object source, String message, Throwable throwable) {
        getLogger(source).info(message, throwable);
    }

    public static void info(Object source, String message) {
        getLogger(source).info(message);
    }

    public static void debug(Object source, String message) {
        getLogger(source).debug(message);
    }

    public static void warn(Object source, String message) {
        getLogger(source).warn(message);
    }

    private static Logger getLogger(Object source) {
        if ("true".equals(System.getProperty("ashcroft"))) {
            return new Logger("dumb") {
                @Override
                public void info(Object message) {
                }

                @Override
                public void info(Object message, Throwable t) {
                }

                @Override
                public void debug(Object message) {
                }

                @Override
                public void error(Object message, Throwable t) {
                }

                @Override
                public void error(Object message) {
                }

                @Override
                public void fatal(Object message, Throwable t) {
                }
            };
        }

        /**
         * For those absurd statics
         */
        if (source instanceof Class) {
            return Logger.getLogger((Class) source);
        }

        return Logger.getLogger(source.getClass());
    }

    public static void fatal(Object source, String s, Exception e) {
        getLogger(source).fatal(s, e);
    }

    public static void warn(Object source, String message, Exception e) {
        getLogger(source).warn(message, e);
    }
}
