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
public enum VAlign {

    TOP(Element.ALIGN_TOP),
    MIDDLE(Element.ALIGN_MIDDLE),
    BOTTOM(Element.ALIGN_BOTTOM);

    private final int elementAlign;

    private VAlign(int elementAlign) {
        this.elementAlign = elementAlign;
    }

    public int getElementAlign() {
        return elementAlign;
    }

}
