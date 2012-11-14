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

package clutter.hypertoolkit.html;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Includes {
    private List<JavascriptInclude> javascript;
    private List<CssInclude> css;

    public Includes() {
        this(Lists.<JavascriptInclude>newArrayList(), Lists.<CssInclude>newArrayList());
    }

    public Includes(List<JavascriptInclude> javascript, List<CssInclude> css) {
        this.javascript = javascript;
        this.css = css;
    }

    public Includes merge(List<JavascriptInclude> javascript, List<CssInclude> css) {
        List<JavascriptInclude> mergedJs = new ArrayList<JavascriptInclude>(this.javascript);
        mergedJs.addAll(javascript);
        List<CssInclude> mergedCss = new ArrayList<CssInclude>(this.css);
        mergedCss.addAll(css);
        return new Includes(mergedJs, mergedCss);
    }

    public Includes merge(Includes includes) {
        return merge(includes.getJavascript(), includes.getCss());
    }

    public List<JavascriptInclude> getJavascript() {
        return javascript;
    }

    public Includes addJs(String... javascriptIncludes) {
        List<JavascriptInclude> list = new ArrayList<JavascriptInclude>();
        for (String javascriptInclude : javascriptIncludes) {
            list.add(new JavascriptInclude(javascriptInclude));
        }
        return merge(list, Lists.<CssInclude>newArrayList());
    }

    public Includes addCss(String... cssIncludes) {
        List<CssInclude> list = new ArrayList<CssInclude>();
        for (String cssInclude : cssIncludes) {
            list.add(new CssInclude(cssInclude));
        }
        return merge(Lists.<JavascriptInclude>newArrayList(), list);
    }

    public List<CssInclude> getCss() {
        return css;
    }

}
