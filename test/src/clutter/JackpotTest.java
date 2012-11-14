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

public class JackpotTest extends TestCase {
    public void testMultiIterator() throws Exception {
        Iterator<String> left = Iterators.iterator("a", "c", "e");
        Iterator<String> right = Iterators.iterator("b", "d", "f");
        Jackpot source = new Jackpot<String>(left, right);
        assertEquals("a b c d e f", Joiner.on(" ").join(new CachingIterator<String>(source)));
    }

    public void testEmptyIterator() throws Exception {
        Iterator<String> left = Iterators.iterator("a", "c", "e");
        Iterator<String> right = Iterators.NULL;
        Jackpot source = new Jackpot<String>(left, right);
        assertEquals("a c e", Joiner.on(" ").join(new CachingIterator<String>(source)));
    }

    public void testDifferentSizes() throws Exception {
        Iterator<String> left = Iterators.iterator("a", "c", "e");
        Iterator<String> right = Iterators.iterator("b");
        Jackpot source = new Jackpot<String>(left, right);
        assertEquals("a b c e", Joiner.on(" ").join(new CachingIterator<String>(source)));
    }
}
