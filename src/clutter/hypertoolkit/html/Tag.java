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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (C) 2006 Fabrice Medio
 */
public class Tag implements Renderable {
    private String name;
    private List<Attribute> attributes;
    private List<Renderable> children;
    private String id;
    private List<CssClass> cssClasses;

    public Tag() {
        this.attributes = new ArrayList<Attribute>();
        this.children = new ArrayList<Renderable>();
        this.cssClasses = new ArrayList<CssClass>();
    }


    public Tag(String name, String textValue) {
        this(name, Html.text(textValue));
    }

    public Tag(String name, Attribute attribute, Attribute... attributes) {
        this();
        this.name = name;
        this.attr(attribute);
        this.attr(attributes);
    }

    public Tag(String name, Renderable... renderables) {
        this();
        this.name = name;
        for (Renderable renderable : renderables) {
            this.add(renderable);
        }
    }


    public String getId() {
        return id;
    }

    public Tag id(String id) {
        this.id = id;
        return this;
    }

    public Tag css(CssClass... cssClasses) {
        for (CssClass cssClass : cssClasses) {
            this.cssClasses.add(cssClass);
        }
        return this;
    }

    // @deprecated

    public void onClick(String onClick) {
        this.attr("onClick", onClick);
    }

    public Tag attr(Attribute... attributes) {
        for (Attribute attribute : attributes) {
            this.attributes.add(attribute);
        }
        return this;
    }

    public Tag attr(String name, String value) {
        Attribute attribute = Html.attr(name, value);
        return this.attr(attribute);
    }

    public Tag add(Renderable... renderables) {
        for (Renderable renderable : renderables) {
            this.children.add(renderable);
        }
        return this;
    }

    public Tag add(Collection<Renderable> renderables) {
        return add(renderables.toArray(new Renderable[0]));
    }

    public Tag add(String text) {
        return this.add(Html.text(text));
    }

    public void render(PrintWriter printWriter) throws IOException {
        if (cssClasses.size() > 0) {
            Collection<String> names = Collections2.transform(cssClasses, new Function<CssClass, String>() {
                @Override
                public String apply(CssClass cssClass) {
                    return cssClass.name();
                }
            });

            String display = Joiner.on(" ").join(names);
            attr(Html.attr("class", display));
        }

        if (id != null) {
            attr(Html.attr("id", id));
        }

        printWriter.print("<" + name);
        for (Attribute attribute : attributes) {
            printWriter.print(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
        }

        if (children.size() > 0) {
            printWriter.print(">");
            for (Renderable child : children) {
                child.render(printWriter);
            }
            printWriter.print("</" + name + ">");
        } else {
            printWriter.print(" />");
        }
    }

    /**
     * @deprecated - test-only
     */
    public List<Renderable> getChildren() {
        return children;
    }

    /**
     * @deprecated - test-only
     */
    public String getName() {
        return name;
    }

    /**
     * @deprecated - test-only
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
}
