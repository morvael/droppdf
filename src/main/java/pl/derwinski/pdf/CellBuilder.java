/*
 * Copyright (C) 2020 Dominik Derwiński
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

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;

/**
 *
 * @author Dominik Derwiński
 */
public class CellBuilder {

    private final PdfMaker pdf;

    private Element element;
    private int hAlign = HAlign.LEFT.getElementAlign();
    private int vAlign = VAlign.TOP.getElementAlign();
    private float paddingLeft = 0f;
    private float paddingRight = 0f;
    private float paddingTop = 0f;
    private float paddingBottom = 0f;
    private int border = Border.NO_BORDER.getBorder();
    private int colspan = 1;
    private int rowspan = 1;
    private Color backgroundColor;
    private Float minimumHeight;

    CellBuilder(PdfMaker pdf) {
        this.pdf = pdf;
    }

    public CellBuilder content(String txt, Font font) {
        if (txt == null) {
            return this;
        }
        element = new Phrase(txt, font);
        return this;
    }

    public CellBuilder content(Font font, String format, Object... args) {
        if (format == null) {
            return this;
        }
        element = new Phrase(String.format(format, args), font);
        return this;
    }

    public CellBuilder content(Image img) {
        if (img == null) {
            return this;
        }
        element = img;
        return this;
    }

    public CellBuilder content(Paragraph par) {
        if (par == null) {
            return this;
        }
        element = par;
        return this;
    }

    public CellBuilder content(PdfPTable tab) {
        if (tab == null) {
            return this;
        }
        element = tab;
        return this;
    }

    public CellBuilder hAlign(HAlign hAlign) {
        if (hAlign == null) {
            return this;
        }
        this.hAlign = hAlign.getElementAlign();
        return this;
    }

    public CellBuilder vAlign(VAlign vAlign) {
        if (vAlign == null) {
            return this;
        }
        this.vAlign = vAlign.getElementAlign();
        return this;
    }

    public CellBuilder padding(float padding) {
        this.paddingLeft = padding;
        this.paddingRight = padding;
        this.paddingTop = padding;
        this.paddingBottom = padding;
        return this;
    }

    public CellBuilder paddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public CellBuilder paddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public CellBuilder paddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public CellBuilder paddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    public CellBuilder border(Border border) {
        if (border == null) {
            return this;
        }
        this.border = border.getBorder();
        return this;
    }

    public CellBuilder colspan(int colspan) {
        this.colspan = colspan;
        return this;
    }

    public CellBuilder rowspan(int rowspan) {
        this.rowspan = rowspan;
        return this;
    }

    public CellBuilder backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public CellBuilder minimumHeight(float minimumHeight) {
        this.minimumHeight = minimumHeight;
        return this;
    }

    public PdfPCell build() {
        if (element == null) {
            return null;
        }
        PdfPCell cell;
        if (element instanceof Phrase) {
            cell = new PdfPCell((Phrase) element);
        } else if (element instanceof Image) {
            cell = new PdfPCell((Image) element);
        } else if (element instanceof PdfPTable) {
            cell = new PdfPCell((PdfPTable) element);
        } else {
            throw new IllegalArgumentException("Unknown content");
        }
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(hAlign);
        cell.setVerticalAlignment(vAlign);
        cell.setPaddingLeft(paddingLeft);
        cell.setPaddingRight(paddingRight);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingBottom(paddingBottom);
        cell.setBorder(border);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (minimumHeight != null) {
            cell.setMinimumHeight(minimumHeight);
        }
        return cell;
    }

}
