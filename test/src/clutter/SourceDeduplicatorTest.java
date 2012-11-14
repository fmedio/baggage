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

import com.google.common.base.Joiner;
import junit.framework.TestCase;

import java.util.Iterator;

public class SourceDeduplicatorTest extends TestCase {
    public void testDeduplicate() throws Exception {
        Source<String> source = source("a", "a", "b", "b", "c");
        SourceDeduplicator<String> deduplicator = new SourceDeduplicator<String>(source);
        assertEquals("a b c", Joiner.on(" ").join(new CachingIterator<String>(deduplicator)));
    }

    public void testNoDupes() throws Exception {
        Source<String> source = source("a", "b", "c");
        SourceDeduplicator<String> deduplicator = new SourceDeduplicator<String>(source);
        assertEquals("a b c", Joiner.on(" ").join(new CachingIterator<String>(deduplicator)));
    }

    private Source<String> source(String... strings) {
        final Iterator<String> iterator = Iterators.iterator(strings);
        return new Source<String>() {
            @Override
            public String maybeNext() {
                if (!iterator.hasNext()) {
                    return null;
                } else {
                    return iterator.next();
                }
            }
        };
    }
}
