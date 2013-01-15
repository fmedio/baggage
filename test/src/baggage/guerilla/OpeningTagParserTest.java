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

package baggage.guerilla;

import baggage.Bag;
import baggage.BaseTestCase;

public class OpeningTagParserTest extends BaseTestCase {
    public void testParse() throws Exception {
        OpeningTagParser parser = new OpeningTagParser();
        OpeningTag openingTag = parser.parse("body one=\"two\" three_three = 'four' five=six seven=\"eight \\\"eight\\\"\" /");
        assertEquals("body", openingTag.getStringValue());
        Bag<String, String> attributes = openingTag.getAttributes();
        assertEquals(4, attributes.size());
        assertEquals("two", attributes.get("one"));
        assertEquals("four", attributes.get("three_three"));
        assertEquals("six", attributes.get("five"));
        assertEquals("eight \\\"eight\\\"", attributes.get("seven"));
    }

    public void testParseJunk() throws Exception {
        OpeningTagParser parser = new OpeningTagParser();
        OpeningTag openingTag = parser.parse("... this is junk");

    }
}
