/*
 * Copyright (C) 2020 domin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.derwinski.pdf;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.util.ArrayList;

/**
 *
 * @author Dominik Derwiński
 */
public class TableBuilder {

    private final PdfMaker pdf;

    private final ArrayList<PdfPCell> cells = new ArrayList<>();
    private int numColumns = 1;
    private Float widthPercentage = 100f;
    private Float totalWidth = null;
    private boolean keepTogether = false;
    private int hAlign = HAlign.LEFT.getElementAlign();
    private float[] relativeWidths;
    private float spacingAfter = 0f;

    TableBuilder(PdfMaker pdf) {
        this.pdf = pdf;
    }

    public TableBuilder add(PdfPCell cell) {
        if (cell == null) {
            return this;
        }
        cells.add(cell);
        return this;
    }

    public TableBuilder numColumns(int numColumns) {
        this.numColumns = numColumns;
        return this;
    }

    public TableBuilder widthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
        this.totalWidth = null;
        return this;
    }

    public TableBuilder totalWidth(float totalWidth) {
        this.widthPercentage = null;
        this.totalWidth = totalWidth;
        return this;
    }

    public TableBuilder keepTogether(boolean keepTogether) {
        this.keepTogether = keepTogether;
        return this;
    }

    public TableBuilder hAlign(HAlign hAlign) {
        if (hAlign == null) {
            return this;
        }
        this.hAlign = hAlign.getElementAlign();
        return this;
    }

    public TableBuilder relativeWidths(float... relativeWidths) {
        this.relativeWidths = relativeWidths;
        return this;
    }

    public TableBuilder spacingAfter(float spacingAfter) {
        this.spacingAfter = spacingAfter;
        return this;
    }

    public PdfPTable build() {
        if (cells.isEmpty()) {
            return null;
        }
        PdfPTable table = new PdfPTable(numColumns);
        for (PdfPCell cell : cells) {
            table.addCell(cell);
        }
        if (widthPercentage != null) {
            table.setWidthPercentage(widthPercentage);
        } else {
            table.setLockedWidth(true);
            table.setTotalWidth(totalWidth);
        }
        table.setKeepTogether(keepTogether);
        table.setHorizontalAlignment(hAlign);
        float[] rw = new float[numColumns];
        for (int i = 0; i < numColumns; i++) {
            if (relativeWidths == null || relativeWidths.length == 0) {
                rw[i] = 1f;
            } else {
                rw[i] = relativeWidths[i % relativeWidths.length];
            }
        }
        table.setWidths(rw);
        table.setSpacingAfter(spacingAfter);
        return table;
    }

    public void buildAndAdd() {
        pdf.getDocument().add(build());
    }

}
