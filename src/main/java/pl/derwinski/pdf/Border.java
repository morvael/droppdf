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

import com.lowagie.text.Rectangle;

/**
 *
 * @author Dominik Derwiński
 */
public enum Border {

    NO_BORDER(Rectangle.NO_BORDER),
    BOX(Rectangle.BOX);

    private final int border;

    private Border(int border) {
        this.border = border;
    }

    public int getBorder() {
        return border;
    }

}
