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

import org.apache.lucene.util.OpenBitSet;

import java.nio.ByteBuffer;

public class StoredBitSet extends OpenBitSet {
    private int id;

    private byte[] contentsForHibernate;


    public StoredBitSet(OpenBitSet openBitSet) {
        this.bits = openBitSet.getBits();
        this.wlen = openBitSet.getNumWords();
    }


    public StoredBitSet() {
    }

    public int getWordCount() {
        return wlen;
    }

    public void setWordCount(int wordCount) {
        this.wlen = wordCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public byte[] getContentsForHibernate() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bits.length * 8);
        for (long segment : bits) {
            byteBuffer.putLong(segment);
        }
        return byteBuffer.array();
    }

    public void setContentsForHibernate(byte[] contentsForHibernate) {
        ByteBuffer buffer = ByteBuffer.wrap(contentsForHibernate);
        this.bits = new long[contentsForHibernate.length / 8];

        for (int i = 0; i < bits.length; i++) {
            bits[i] = buffer.getLong();
        }
    }
}
