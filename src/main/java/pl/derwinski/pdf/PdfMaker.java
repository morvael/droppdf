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

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author Dominik Derwiński
 */
public class PdfMaker implements AutoCloseable {

    private static final float MM_MULT = 2.83465f;

    public static float mmToPt(float mm) {
        return mm * MM_MULT;
    }

    private final File file;
    private final Rectangle pageSize;
    private final float pageWidth;
    private final float pageHeight;
    private final FileOutputStream fos;
    private final Document document;
    private final PdfWriter writer;
    private final HashMap<String, BaseFont> baseFonts = new HashMap<>();
    private final HashMap<String, Font> fonts = new HashMap<>();
    private final HashMap<String, Image> images = new HashMap<>();

    public PdfMaker(File file, Rectangle pageSize) throws Exception {
        this.file = file;
        this.pageSize = pageSize;
        this.pageWidth = pageSize.getWidth();
        this.pageHeight = pageSize.getHeight();
        this.fos = new FileOutputStream(file, false);
        this.document = new Document(pageSize);
        this.writer = PdfWriter.getInstance(document, fos);
        this.document.open();
    }

    public void setMargins(float left, float right, float top, float bottom, boolean mirroring) {
        document.setMargins(mmToPt(left), mmToPt(right), mmToPt(top), mmToPt(bottom));
        document.setMarginMirroring(mirroring);
    }

    public BaseFont getBaseFont(String name) throws Exception {
        BaseFont existingBaseFont = baseFonts.get(name);
        if (existingBaseFont == null) {
            BaseFont newBaseFont = BaseFont.createFont(name, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            baseFonts.put(name, newBaseFont);
            return newBaseFont;
        } else {
            return existingBaseFont;
        }
    }

    public Font getFont(BaseFont baseFont, float size, Color color) {
        String key = String.format("%s_%f_%s", baseFont.toString(), size, color.toString());
        Font existingFont = fonts.get(key);
        if (existingFont == null) {
            Font newFont = new Font(baseFont, size, Font.NORMAL, color);
            fonts.put(key, newFont);
            return newFont;
        } else {
            return existingFont;
        }
    }

    public Image getImage(File image) throws Exception {
        String key = image.getAbsolutePath();
        Image existingImage = images.get(key);
        if (existingImage == null) {
            BufferedImage bi = ImageIO.read(image);
            Image newImage = Image.getInstance(bi, null);
            images.put(key, newImage);
            return newImage;
        } else {
            return Image.getInstance(existingImage);
        }
    }

    float getPageWidth() {
        return pageWidth;
    }

    float getPageHeight() {
        return pageHeight;
    }

    float getLeftMargin() {
        return document.leftMargin();
    }

    float getRightMargin() {
        return document.rightMargin();
    }

    float getTopMargin() {
        return document.topMargin();
    }

    float getBottomMargin() {
        return document.bottomMargin();
    }

    float getContentWidth() {
        return pageWidth - document.leftMargin() - document.rightMargin();
    }

    float getContentHeight() {
        return pageHeight - document.topMargin() - document.bottomMargin();
    }

    public Document getDocument() {
        return document;
    }

    PdfWriter getWriter() {
        return writer;
    }

    public BlockBuilder newBlock() {
        return new BlockBuilder(this);
    }

    public TableBuilder newTable() {
        return new TableBuilder(this);
    }

    public CellBuilder newCell() {
        return new CellBuilder(this);
    }

    public ParaBuilder newPara() {
        return new ParaBuilder(this);
    }

    public ImageBuilder newImage() {
        return new ImageBuilder(this);
    }

    @Override
    public void close() throws Exception {
        if (document != null) {
            try {
                document.close();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }
}
