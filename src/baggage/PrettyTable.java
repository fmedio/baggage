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

import java.util.ArrayList;
import java.util.List;

public class PrettyTable {
    public static class Row {
        private List<String> cells;

        public Row(String... cells) {
            this.cells = new ArrayList<String>();
            for (String cell : cells) {
                this.addCell(cell);
            }
        }

        public void addCell(String text) {
            if (text == null) {
                this.cells.add("[null]");
            }
            this.cells.add(text);
        }
    }

    private List<Row> rows;
    private Row header;

    public PrettyTable(Row header) {
        this.rows = new ArrayList<Row>();
        this.header = header;
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }

    public String toString() {
        if (rows.size() == 0) {
            return "";
        }

        if (header == null) {
            throw new RuntimeException("Must have a header row!");
        }

        int cellCount = header.cells.size();
        int[] maxWidths = new int[cellCount];
        for (int i = 0; i < header.cells.size(); i++) {
            maxWidths[i] = Math.max(maxWidths[i], header.cells.get(i).length());
        }

        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            if (row.cells.size() != cellCount) {
                throw new RuntimeException("Rows cannot have different sizes!");
            }

            for (int j = 0; j < cellCount; j++) {
                maxWidths[j] = Math.max(maxWidths[j], row.cells.get(j).length());
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append(drawLine(maxWidths));
        sb.append(paintRow(header, maxWidths));
        sb.append(drawLine(maxWidths));
        for (Row row : rows) {
            sb.append(paintRow(row, maxWidths));
        }
        sb.append(drawLine(maxWidths));
        return sb.toString();
    }

    private StringBuffer paintRow(Row row, int[] maxWidths) {
        StringBuffer sb = new StringBuffer();
        sb.append("|");
        for (int i = 0; i < maxWidths.length; i++) {
            int maxWidth = maxWidths[i];
            String text = pad(row.cells.get(i), maxWidth);
            sb.append(" ");
            sb.append(text);
            sb.append(" |");
        }
        sb.append("\n");
        return sb;
    }

    private StringBuffer drawLine(int[] maxWidths) {
        StringBuffer sb = new StringBuffer();
        int width = 1 + 3 * maxWidths.length;
        for (int maxWidth : maxWidths) {
            width += maxWidth;
        }

        for (int i = 0; i < width; i++) {
            sb.append("-");
        }
        sb.append("\n");
        return sb;
    }

    private String pad(String s, int width) {
        StringBuffer sb = new StringBuffer();
        sb.append(s);

        for (int i = 0; i < width - s.length(); i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
