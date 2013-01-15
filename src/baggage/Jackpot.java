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

package baggage;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Jackpot<T extends Comparable<T>> implements Source<T> {
    private List<Iterator<T>> iterators;
    private List<T> payline;

    public Jackpot(Iterator<T>... iterators) {
        this(Lists.newArrayList(iterators));
    }

    public Jackpot(List<Iterator<T>> iterators) {
        this.iterators = iterators;
        payline = new ArrayList<T>(iterators.size());

        for (int i = 0; i < iterators.size(); i++) {
            Iterator<T> iterator = iterators.get(i);
            if (iterator.hasNext()) {
                payline.add(iterator.next());
            } else {
                payline.add(null);
            }
        }
    }

    @Override
    public T maybeNext() {
        for (int i = 0; i < payline.size(); i++) {
            if (payline.get(i) != null) {
                return fetchSmallest();
            }
        }

        return null;
    }


    private int smallestKey() {
        int smallestKey = -1;

        for (int i = 0; i < payline.size(); i++) {
            if (payline.get(i) == null) {

            } else if (smallestKey == -1) {
                smallestKey = i;
            } else {
                int comparison = payline.get(i).compareTo(payline.get(smallestKey));
                if (comparison < 0) {
                    smallestKey = i;
                }
            }
        }
        return smallestKey;
    }

    private T fetchSmallest() {
        int smallestKey = smallestKey();

        T result = payline.get(smallestKey);

        if (iterators.get(smallestKey).hasNext()) {
            payline.set(smallestKey, iterators.get(smallestKey).next());
        } else {
            payline.set(smallestKey, null);
        }

        return (T) result;
    }

    public List<T> payline() {
        return payline;
    }
}
