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

import clutter.Bag;
import clutter.ListBag;
import clutter.hypertoolkit.ActionId;

import java.util.UUID;

public class Link extends Tag {
    private String target;

    public Link(ActionId actionId) {
        this(actionId.getName(), new ListBag<String, String>(), false);
    }

    public Link(ActionId id, Bag<String, String> bag) {
        this(id.getName(), bag, false);
    }

    public Link(String baseUrl, Bag<String, String> parameters) {
        this(baseUrl, parameters, false);
    }

    public Link(ActionId actionId, Bag<String, String> parameters, boolean includeCacheBuster) {
        this(actionId.getName(), parameters, includeCacheBuster);
    }

    public Link(String targetAction, Bag<String, String> parameters, boolean includeCacheBuster) {
        super("a");
        if (includeCacheBuster) {
            parameters.put("cb", UUID.randomUUID().toString());
        }
        target = targetAction + new UrlParamEncoder().asUrl(parameters);
        attr("href", target);
    }

    public String getTarget() {
        return target;
    }
}
