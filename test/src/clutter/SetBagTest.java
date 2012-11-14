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

public class SetBagTest extends BaseTestCase {
    public void testBasic() throws Exception {
        SetBag<String, Integer> bag = new SetBag<String, Integer>();
        bag.put("foo", 1);
        bag.put("foo", 2);
        bag.put("foo", 3);
        bag.put("foo", 4);
        bag.put("bar", 1);
        bag.put("baz", 1);
        bag.put("baz", 2);

        assertEquals((int) bag.get("foo"), 1);
        assertEquals(bag.getValues("foo").size(), 4);
        assertEquals("foo, bar, baz", Joiner.on(", ").join(bag.keySet()));
        assertEquals("1, 2, 3, 4", Joiner.on(", ").join(bag.getValues("foo")));
        assertEquals("1", Joiner.on(", ").join(bag.getValues("bar")));
        assertEquals("1, 2", Joiner.on(", ").join(bag.getValues("baz")));
        assertEquals(0, bag.getValues("blah").size());
    }

    public void testValuesSize() throws Exception {
        SetBag<String, Integer> bag = new SetBag<String, Integer>();
        bag.put("foo", 1);
        bag.put("foo", 2);
        bag.put("foo", 3);
        assertEquals(1, bag.size());
    }

    public void testRemove() throws Exception {
        SetBag<String, String> bag = new SetBag<String, String>();
        bag.put("hello", "hello");
        bag.remove("hello");
        assertTrue(bag.get("hello") == null);
    }

    public void testNullValue() throws Exception {
        final SetBag<String, String> bag = new SetBag<String, String>();
        bag.put("hello", null);
        assertEquals(0, bag.keySet().size());
    }
}
