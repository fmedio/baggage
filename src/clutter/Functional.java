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

package clutter;

import java.util.LinkedList;
import java.util.List;

public class Functional {

    public static <In, Out> Out[] transform(In[] ins, GenericFunction<In, Out> function) {
        Out[] outs = function.array(ins.length);
        for (int i = 0; i < ins.length; i++) {
            In in = ins[i];
            outs[i] = function.apply(in);
        }
        return outs;
    }

    public static <In, Out> List<Out> transform(List<In> ins, GenericFunction<In, Out> function) {
        List<Out> outs = new LinkedList<Out>();

        for (In in : ins) {
            outs.add(function.apply(in));
        }
        return outs;
    }

    public static class ToString<T> implements GenericFunction<T, String> {
        @Override
        public String apply(T t) {
            return t.toString();
        }

        @Override
        public String[] array(int size) {
            return new String[size];
        }
    }

}
