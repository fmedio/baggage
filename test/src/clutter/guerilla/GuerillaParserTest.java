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

import clutter.BaseTestCase;
import org.apache.commons.io.IOUtils;

public class GuerillaParserTest extends BaseTestCase {
    public void testParse() throws Exception {
        String snippet = "junk <!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n" +
                "<html>\n" +
                "<body foo=\"bar\" hello='world'>\n" +
                "See, I am able to parseFromJSON\n" +
                "</html>\n";

        GuerillaParser parser = new GuerillaParser(IOUtils.toInputStream(snippet));
        TextItem textItem = (TextItem) parser.next(false);
        assertEquals("junk ", textItem.getStringValue());
        DoctypeDeclaration docTypeDeclaration = (DoctypeDeclaration) parser.next(false);
        assertEquals("DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"", docTypeDeclaration.getStringValue());
        assertEquals("\n", parser.next(false).getStringValue());
        OpeningTag html = (OpeningTag) parser.next(false);
        assertEquals("html", html.getStringValue());
        assertEquals("\n", parser.next(false).getStringValue());
        OpeningTag body = (OpeningTag) parser.next(false);
        assertEquals("body", body.getStringValue());
        assertEquals(2, body.getAttributes().size());
        assertEquals("\nSee, I am able to parseFromJSON\n", parser.next(false).getStringValue());
    }


    public void testDiscardText() throws Exception {
        String snippet = "Text here and <there is=\"a\" nice=\"tag\" /> and now for more text";
        GuerillaParser parser = new GuerillaParser(IOUtils.toInputStream(snippet));
        OpeningTag tag = (OpeningTag) parser.next(true);
        assertEquals("there", tag.getStringValue());
        assertEquals(2, tag.getAttributes().size());
        assertNull(parser.next(false));
    }

    public void testClosingTag() throws Exception {
        String snippet = "<b>foo</b>";
        GuerillaParser parser = new GuerillaParser(IOUtils.toInputStream(snippet));
        OpeningTag openingTag = (OpeningTag) parser.next(true);
        TextItem textItem = (TextItem) parser.next(false);
        assertEquals("foo", textItem.getStringValue());
        ClosingTag closingTag = (ClosingTag) parser.next(false);
        assertEquals("b", closingTag.getStringValue());
    }
}
