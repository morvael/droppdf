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

/**
 *
 * @author Dominik Derwiński
 */
public enum HAlign {

    LEFT(Element.ALIGN_LEFT),
    CENTER(Element.ALIGN_CENTER),
    RIGHT(Element.ALIGN_RIGHT),
    JUSTIFIED(Element.ALIGN_JUSTIFIED);

    private final int elementAlign;

    private HAlign(int elementAlign) {
        this.elementAlign = elementAlign;
    }

    public int getElementAlign() {
        return elementAlign;
    }

}
