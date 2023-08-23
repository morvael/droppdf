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
package pl.derwinski.droppdf;

import java.awt.Color;

/**
 *
 * @author Dominik Derwiński
 */
public enum FactionData {

    UCM(new Color(54, 146, 53), Color.WHITE),
    Scourge(new Color(107, 62, 145), Color.WHITE),
    PHR(new Color(234, 214, 68), Color.BLACK),
    Shaltari(new Color(232, 87, 16), Color.WHITE),
    Resistance(new Color(64, 138, 201), Color.WHITE);

    private final Color color;
    private final Color textColor;

    private FactionData(Color color, Color textColor) {
        this.color = color;
        this.textColor = textColor;
    }

    public Color getColor() {
        return color;
    }

    public Color getTextColor() {
        return textColor;
    }

}
