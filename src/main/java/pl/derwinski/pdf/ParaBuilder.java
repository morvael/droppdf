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

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.HyphenationAuto;
import java.util.ArrayList;

/**
 *
 * @author Dominik Derwiński
 */
public class ParaBuilder {

    private final PdfMaker pdf;
    private final ArrayList<Element> elements = new ArrayList<>();

    private boolean hyphenation = false;
    private int hAlign = HAlign.LEFT.getElementAlign();
    private float spacingAfter = 0f;

    ParaBuilder(PdfMaker pdf) {
        this.pdf = pdf;
    }

    public ParaBuilder add(String text, Font font) {
        if (text == null) {
            return this;
        }
        elements.add(new Chunk(text, font));
        return this;
    }

    public ParaBuilder add(Font font, String format, Object... args) {
        if (format == null) {
            return this;
        }
        elements.add(new Chunk(String.format(format, args), font));
        return this;
    }

    public ParaBuilder add(float leading, String text, Font font) {
        if (text == null) {
            return this;
        }
        elements.add(new Phrase(leading, text, font));
        return this;
    }

    public ParaBuilder add(Image img) {
        if (img == null) {
            return this;
        }
        elements.add(new Chunk(img, 0f, 0f));
        return this;
    }

    public ParaBuilder add(Image img, float offsetX, float offsetY, boolean changeLeading) {
        if (img == null) {
            return this;
        }
        elements.add(new Chunk(img, offsetX, offsetY, changeLeading));
        return this;
    }

    public ParaBuilder newLine() {
        elements.add(Chunk.NEWLINE);
        return this;
    }

    public ParaBuilder hyphenation(boolean hyphenation) {
        this.hyphenation = hyphenation;

        return this;
    }

    public ParaBuilder hAlign(HAlign hAlign) {
        if (hAlign == null) {
            return this;
        }
        this.hAlign = hAlign.getElementAlign();
        return this;
    }

    public ParaBuilder spacingAfter(float spacingAfter) {
        this.spacingAfter = spacingAfter;
        return this;
    }

    public Paragraph build() {
        if (elements.isEmpty()) {
            return null;
        }
        Paragraph para = new Paragraph();
        for (Element element : elements) {
            para.add(element);
        }
        if (hyphenation) {
            para.setHyphenation(new HyphenationAuto("en", "GB", 2, 2));
        }
        para.setAlignment(hAlign);
        para.setSpacingAfter(spacingAfter);
        return para;
    }

}
