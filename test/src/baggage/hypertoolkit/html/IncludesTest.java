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

package baggage.hypertoolkit.html;

import baggage.BaseTestCase;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class IncludesTest extends BaseTestCase {
    public void testMerge() throws Exception {
        Includes includes = new Includes(
                Lists.newArrayList(new JavascriptInclude("mainJs")),
                Lists.newArrayList(new CssInclude("mainCss"))
        );

        Includes result = includes.merge(
                Lists.newArrayList(new JavascriptInclude("subJs")),
                Lists.newArrayList(new CssInclude("subCss"))
        );

        assertEquals("mainJs subJs", Joiner.on(" ").join(result.getJavascript()));
        assertEquals("mainCss subCss", Joiner.on(" ").join(result.getCss()));
    }
}
