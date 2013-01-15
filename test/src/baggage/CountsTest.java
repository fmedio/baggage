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


import com.google.common.collect.Lists;

import java.util.List;

public class CountsTest extends BaseTestCase {
    public void testCounts() throws Exception {
        Counts<String> counts = new Counts<String>();
        assertEquals(0l, counts.get("poop"));
        assertEquals(1l, counts.increment("poop"));
        assertEquals(1l, counts.get("poop"));
        assertEquals(3l, counts.increment("poop", 2l));
        assertEquals(3l, counts.get("poop"));
        assertEquals(1l, counts.size());
        assertEquals(5l, counts.increment("poop", 2l));
        assertEquals(5l, counts.sum());
    }

    public void testFromIterator() throws Exception {
        List<String> list = Lists.newArrayList("foo", "foo", "bar");
        Counts<String> counts = new Counts<String>(list.iterator());
        assertEquals(3, counts.sum());
        assertEquals(2, counts.get("foo"));
        assertEquals(1, counts.get("bar"));
    }

    public void testTally() throws Exception {
        Counts<String> counts = new Counts<String>(Lists.newArrayList("foo", "foo", "bar").iterator());
        counts.tally(new Counts<String>(Lists.newArrayList("foo", "foo", "bar").iterator()));
        assertEquals(5, counts.sum());
        assertEquals(3, counts.get("foo"));
        assertEquals(2, counts.get("bar"));
    }

    public void testAdd() throws Exception {
        Counts<String> counts = new Counts<String>(Lists.newArrayList("foo", "foo", "bar").iterator());
        counts.add(new Counts<String>(Lists.newArrayList("foo", "foo", "bar").iterator()));
        assertEquals(6, counts.sum());
        assertEquals(4, counts.get("foo"));
        assertEquals(2, counts.get("bar"));
    }
}
