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

import baggage.hypertoolkit.ActionId;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class Html {
    public static Tag a() {
        return new Tag("a");
    }

    public static Tag b() {
        return new Tag("b");
    }

    public static Tag b(Renderable... renderables) {
        return new Tag("b", renderables);
    }

    public static Tag b(String text) {
        return new Tag("b", text);
    }

    public static Tag body(Renderable... children) {
        return new Tag("body").add(children);
    }

    public static Tag br() {
        return new Tag("br");
    }

    public static Renderable concat(Renderable... renderables) {
        return new Renderables(renderables);
    }

    public static Tag div(Renderable child, Renderable... children) {
        return new Tag("div").add("").add(child).add(children);
    }

    public static Tag div(String id) {
        return div().attr("id", id);
    }

    public static Tag div() {
        return new Tag("div");
    }
    
    public static Tag div(CssClass first, CssClass... cssClasses) {
        return div().css(first).css(cssClasses);
    }

    public static Tag fieldset() {
        return new Tag("fieldset");
    }

    public static Tag form(String method, ActionId action) {
        return form(attr("method", method), attr("action", action.getName()));
    }

    public static Tag form(Attribute... attributes) {
        Tag tag = new Tag("form");
        tag.attr(attributes);
        tag.attr("accept-charset", "UTF-8");
        return tag;
    }

    public static Tag h1() {
        return new Tag("h1");
    }

    public static Tag h1(String text) {
        return new Tag("h1", text);
    }

    public static Tag h1(Renderable... renderables) {
        return new Tag("h1", renderables);
    }

    public static Tag h2() {
        return new Tag("h2");
    }

    public static Tag h2(Renderable... renderables) {
        return new Tag("h2", renderables);
    }

    public static Tag h2(String text) {
        return new Tag("h2", text);
    }

    public static Tag h3() {
        return new Tag("h3");
    }

    public static Tag h3(String text) {
        return new Tag("h3", text);
    }

    public static Tag head(Renderable... children) {
        return new Tag("head").add(children);
    }

    public static Tag html(Renderable... children) {
        return new Tag("html").add(children);
    }

    public static Tag hr() {
        return new Tag("hr");
    }

    public static Tag i(String text) {
        return new Tag("i", text);
    }

    public static Tag img(String src) {
        return new Tag("img").attr("src", src);
    }

    public static Tag input() {
        return new Tag("input");
    }

    public static Tag input(String type, String name, String value) {
        return input(type, name, value, true);
    }

    public static Tag input(String type, String name, String value, boolean allowAutocomplete) {
        Tag input = input();
        input.attr("type", type);
        input.attr("name", name);
        input.attr("value", value);
        if (!allowAutocomplete) {
            input.attr("autocomplete", "off");
        }
        return input;
    }

    public static Tag input(Attribute... attributes) {
        Tag tag = new Tag("input");
        tag.attr(attributes);
        return tag;

    }

    public static Tag label() {
        return new Tag("label");
    }

    public static Tag label(String text, Renderable input) {
        Tag label = label();
        Text t = text(text);
        label.add(t);
        label.add(input);
        return label;
    }

    public static Tag legend() {
        return new Tag("legend");
    }

    public static Tag li() {
        return new Tag("li");
    }

    public static Tag li(String text) {
        return new Tag("li", text);
    }

    public static Tag li(Renderable... renderables) {
        return new Tag("li", renderables);
    }

    public static Renderable nbsp() {
        return new Renderable() {
            public void render(PrintWriter printWriter) throws IOException {
                printWriter.print("&nbsp;");
            }
        };
    }

    public static Tag ol() {
        return new Tag("ol");
    }

    public static Tag option() {
        return new Tag("option");
    }

    public static Tag option(String value, Renderable child) {
        return option().attr("value", value).add(child);
    }

    public static Tag option(Attribute... attributes) {
        return option().attr(attributes);
    }

    public static Tag p(Renderable r) {
        Tag p = p();
        p.add(r);
        return p;
    }

    public static Tag p(String text) {
        return p(text(text));
    }

    public static Tag p() {
        Tag p = new Tag("p");
        return p;
    }

    public static Tag pre(String text) {
        return new Tag("pre", text);
    }

    public static Tag passwordInput(String name) {
        Tag tag = input();
        tag.attr("type", "password");
        tag.attr("name", StringEscapeUtils.escapeHtml4(name));
        return tag;
    }

    public static Tag select(String name) {
        return new Tag("select").attr("name", name);
    }

    public static Tag span() {
        return new Tag("span");
    }

    public static Tag span(String text) {
        return new Tag("span", text);
    }

    public static Tag span(Renderable... renderables) {
        Tag tag = new Tag("span");
        tag.add(renderables);
        return tag;
    }

    public static Tag strong() {
        return new Tag("strong");
    }

    public static Tag strong(String text) {
        return new Tag("strong", text);
    }

    public static Tag strong(Renderable renderable) {
        return new Tag("strong", renderable);
    }

    public static Tag submit(String value) {
        Tag input = input();
        input.attr("type", "submit");
        input.attr("value", StringEscapeUtils.escapeHtml4(value));
        return input;
    }

    public static Tag table(CssClass... cssClasses) {
        return new Tag("table").css(cssClasses);
    }


    public static Tag td(Renderable... renderables) {
        return new Tag("td", renderables);
    }

    public static Tag td(String text) {
        return new Tag("td", text);
    }

    public static Tag title(String text) {
        return new Tag("title", text);
    }

    public static Tag td(Renderable renderable, int colspan) {
        return new Tag("td", renderable).attr("colspan", Integer.toString(colspan));
    }

    public static Tag tr(Renderable... renderables) {
        return new Tag("tr", renderables);
    }

    public static Tag textarea() {
        return new Tag("textarea");
    }

    public static Tag textInput(String name) {
        Tag tag = input();
        tag.attr("type", "text");
        tag.attr("name", StringEscapeUtils.escapeHtml4(name));
        return tag;
    }

    public static Tag th(Renderable... renderable) {
        return new Tag("th", renderable);
    }

    public static Tag th(String text) {
        return new Tag("th", text);
    }

    public static Tag th(String text, int colspan) {
        return new Tag("th", text).attr("colspan", Integer.toString(colspan));
    }

    public static Tag title() {
        return new Tag("title");
    }

    public static Tag tr() {
        return new Tag("tr");
    }

    public static Tag ul() {
        return new Tag("ul");
    }

    public static Tag ul(Collection<Renderable> renderables) {
        return new Tag("ul").add(renderables);
    }

    public static Attribute attr(String name, String value) {
        return new Attribute(name, value);
    }

    public static Text text(String text) {
        return new Text(text);
    }

    public static Text text(String text, boolean escapeIt) {
        return new Text(text, escapeIt);
    }

    public static Tag wbr() {
        return new Tag("wbr");
    }
}
