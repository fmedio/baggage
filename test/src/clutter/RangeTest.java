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

public class RangeTest extends BaseTestCase {
    public void testSize() throws Exception {
        Range<Integer> range = new Range<Integer>(0, 4);
        assertEquals(5, Math.abs(range.getEnd() - range.getStart()) + 1);
    }

    public void testPosition() throws Exception {
        Range<Double> range = new Range<Double>(0d, 4d);
        assertEquals(Range.Position.before, range.position(-1d));
        assertEquals(Range.Position.inside, range.position(0d));
        assertEquals(Range.Position.inside, range.position(2d));
        assertEquals(Range.Position.inside, range.position(4d));
        assertEquals(Range.Position.after, range.position(5d));
    }
}
