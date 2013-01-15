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

package hello;

import baggage.Bag;
import baggage.ListBag;
import baggage.hypertoolkit.request.InvalidRequestException;
import baggage.hypertoolkit.request.RequestParser;

public class PeriodRequestParser extends RequestParser<Period> {
    @Override
    public Period parse(Bag<String, String> parameters) throws InvalidRequestException {
        int start = requiredInt("start", parameters);
        int end = requiredInt("end", parameters);
        return new Period(start, end);
    }

    @Override
    public Bag<String, String> toParameters(Period period) {
        Bag<String, String> bag = new ListBag<String, String>();
        bag.put("start", Integer.toString(period.getStartYear()));
        bag.put("end", Integer.toString(period.getEndYear()));
        return bag;
    }
}
