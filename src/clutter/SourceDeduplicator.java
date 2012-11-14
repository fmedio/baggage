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

public class SourceDeduplicator<T extends Comparable<T>> implements Source<T> {
    private T lastReturned;
    private Source<T> source;
    private boolean isFirst;

    public SourceDeduplicator(Source<T> source) {
        this.source = source;
        lastReturned = source.maybeNext();
        isFirst = true;
    }

    @Override
    public T maybeNext() {
        if (isFirst) {
            isFirst = false;
            return lastReturned;
        }

        T candidate = source.maybeNext();
        if (candidate == null) {
            return null;
        }

        for (; candidate.compareTo(lastReturned) == 0; candidate = source.maybeNext()) {
        }

        lastReturned = candidate;
        return candidate;
    }
}
