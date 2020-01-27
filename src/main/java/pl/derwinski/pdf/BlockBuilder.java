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

import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.util.ArrayList;

/**
 *
 * @author Dominik Derwiński
 */
public class BlockBuilder {

    private final PdfMaker pdf;
    private final ArrayList<Element> blocks = new ArrayList<>();
    private float paddingLeft = 0f;
    private float paddingRight = 0f;
    private float paddingTop = 0f;
    private float paddingBottom = 0f;
    private VAlign blockAlign = null;
    private boolean keepTogether = false;
    private float spacingAfter = 0f;
    private boolean endPage = false;

    BlockBuilder(PdfMaker pdf) {
        this.pdf = pdf;
    }

    public BlockBuilder add(Image img) {
        if (img == null) {
            return this;
        }
        blocks.add(img);
        return this;
    }

    public BlockBuilder add(Paragraph par) {
        if (par == null) {
            return this;
        }
        blocks.add(par);
        return this;
    }

    public BlockBuilder add(PdfPTable tab) {
        if (tab == null) {
            return this;
        }
        blocks.add(tab);
        return this;
    }

    public BlockBuilder padding(float padding) {
        this.paddingLeft = padding;
        this.paddingRight = padding;
        this.paddingTop = padding;
        this.paddingBottom = padding;
        return this;
    }

    public BlockBuilder paddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public BlockBuilder paddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public BlockBuilder paddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public BlockBuilder paddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    public BlockBuilder blockAlign(VAlign blockAlign) {
        this.blockAlign = blockAlign;
        return this;
    }

    public BlockBuilder keepTogether(boolean keepTogether) {
        this.keepTogether = keepTogether;
        return this;
    }

    public BlockBuilder spacingAfter(float spacingAfter) {
        this.spacingAfter = spacingAfter;
        return this;
    }

    public BlockBuilder endPage(boolean endPage) {
        this.endPage = endPage;
        return this;
    }

    public void buildAndAdd() {
        if (blocks.isEmpty()) {
            return;
        }
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(pdf.getContentWidth());
        table.setLockedWidth(true);
        for (Element block : blocks) {
            PdfPCell cell = null;
            if (block instanceof Image) {
                Image img = (Image) block;
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(img.getAlignment());
            } else if (block instanceof Paragraph) {
                Paragraph par = (Paragraph) block;
                cell = new PdfPCell(par);
                cell.setHorizontalAlignment(par.getAlignment());
            } else if (block instanceof PdfPTable) {
                PdfPTable tab = (PdfPTable) block;
                cell = new PdfPCell(tab);
                cell.setHorizontalAlignment(tab.getHorizontalAlignment());
            } else {
                throw new IllegalArgumentException("Unknown block");
            }
            cell.setBorder(0);
            cell.setPaddingLeft(paddingLeft);
            cell.setPaddingRight(paddingRight);
            cell.setPaddingTop(paddingTop);
            cell.setPaddingBottom(paddingBottom);
            table.addCell(cell);
        }
        if (blockAlign == null) {
            table.setKeepTogether(keepTogether);
            table.setSpacingAfter(spacingAfter);
            pdf.getDocument().add(table);
        } else {
            float tableHeight = table.calculateHeights(true);
            float x = pdf.getLeftMargin();
            float y;
            switch (blockAlign) {
                case TOP:
                    y = pdf.getTopMargin();
                    break;
                case MIDDLE:
                    y = pdf.getTopMargin() + pdf.getContentHeight() / 2f - tableHeight / 2f;
                    break;
                case BOTTOM:
                    y = pdf.getPageHeight() - pdf.getBottomMargin() - tableHeight;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown align");
            }
            table.writeSelectedRows(0, -1, x, pdf.getPageHeight() - y, pdf.getWriter().getDirectContent());
        }
        if (endPage) {
            pdf.getDocument().newPage();
        }
    }

}
