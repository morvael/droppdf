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

import com.lowagie.text.Image;
import java.io.File;

/**
 *
 * @author Dominik Derwiński
 */
public class ImageBuilder {

    private final PdfMaker pdf;

    private File source;
    private Float fitWidth;
    private Float fitHeight;
    private int hAlign = HAlign.LEFT.getElementAlign();

    ImageBuilder(PdfMaker pdf) {
        this.pdf = pdf;
    }

    public ImageBuilder source(File source) {
        this.source = source;
        return this;
    }

    public ImageBuilder scaleToFit(float fitWidth, float fitHeight) {
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        return this;
    }

    public ImageBuilder hAlign(HAlign hAlign) {
        if (hAlign == null) {
            return this;
        }
        this.hAlign = hAlign.getElementAlign();
        return this;
    }

    public Image build() throws Exception {
        Image image = pdf.getImage(source);
        if (fitWidth != null && fitHeight != null) {
            image.scaleToFit(fitWidth, fitHeight);
        }
        image.setAlignment(hAlign);
        return image;
    }

}
