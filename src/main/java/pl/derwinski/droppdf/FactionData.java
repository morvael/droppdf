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
package pl.derwinski.droppdf;

import java.awt.Color;

/**
 *
 * @author Dominik Derwiński
 */
public enum FactionData {

    UCM(new Color(56, 146, 53), Color.WHITE) {
        @Override
        public String getImage(char letter) {
            switch (letter) {
                case 'a':
                    return "VehicleOrange.png";
                case 'A':
                    return "VehicleTransportOrange.png";
                case 'b':
                    return "ArtilleryYellow.png";
                case 'B':
                    return "ArtilleryTransportYellow.png";
                case 'c':
                    return "InfantryGreen.png";
                case 'C':
                    return "InfantryTransportGreen.png";
                default:
                    throw new RuntimeException(String.format("Missing UCM image %s", Character.toString(letter)));
            }
        }
    },
    Scourge(new Color(107, 63, 145), Color.WHITE) {
        @Override
        public String getImage(char letter) {
            switch (letter) {
                case 'a':
                    return "VehicleOrange.png";
                case 'A':
                    return "VehicleTransportOrange.png";
                case 'b':
                    return "ArtilleryYellow.png";
                case 'B':
                    return "ArtilleryTransportYellow.png";
                case 'c':
                    return "InfantryGreen.png";
                case 'C':
                    return "InfantryTransportGreen.png";
                case 'd':
                    return "CrabPurple.png";
                case 'D':
                    return "CrabTransportPurple.png";
                case 's':
                    return "ScreamerBlue.png";
                case 'S':
                    return "ScreamerTransportBlue.png";
                case 'v':
                    return "VampireYellow.png";
                case 'V':
                    return "VampireTransportYellow.png";
                default:
                    throw new RuntimeException(String.format("Missing Scourge image %s", Character.toString(letter)));
            }
        }
    },
    PHR(new Color(234, 214, 66), Color.BLACK) {
        @Override
        public String getImage(char letter) {
            switch (letter) {
                case 'a':
                    return "WalkerOrange.png";
                case 'A':
                    return "WalkerTransportOrange.png";
                case 'b':
                    return "FanBlue.png";
                case 'B':
                    return "FanTransportBlue.png";
                case 'c':
                    return "InfantryGreen.png";
                case 'C':
                    return "InfantryTransportGreen.png";
                case 'm':
                    return "MedusaYellow.png";
                case 'M':
                    return "MedusaTransportYellow.png";
                default:
                    throw new RuntimeException(String.format("Missing PHR image %s", Character.toString(letter)));
            }
        }
    },
    Shaltari(new Color(231, 87, 15), Color.WHITE) {
        @Override
        public String getImage(char letter) {
            switch (letter) {
                case 'a':
                    return "VehicleOrange.png";
                case 'A':
                    return "VehicleTransportOrange.png";
                case 'b':
                    return "InfantryGreen.png";
                case 'B':
                    return "InfantryTransportGreen.png";
                default:
                    throw new RuntimeException(String.format("Missing Shaltari image %s", Character.toString(letter)));
            }
        }
    },
    Resistance(new Color(64, 138, 201), Color.WHITE) {
        @Override
        public String getImage(char letter) {
            switch (letter) {
                case 'a':
                    return "VehicleOrange.png";
                case 'A':
                    return "VehicleTransportOrange.png";
                case 'b':
                    return "WheelsBlue.png";
                case 'B':
                    return "WheelsTransportBlue.png";
                case 'c':
                    return "InfantryGreen.png";
                case 'C':
                    return "InfantryTransportGreen.png";
                case 'd':
                    return "BusBlue.png";
                case 'D':
                    return "BusTransportBlue.png";
                case 's':
                    return "DrillBlue.png";
                case 'S':
                    return "DrillTransportBlue.png";
                default:
                    throw new RuntimeException(String.format("Missing Resistance image %s", Character.toString(letter)));
            }
        }
    };

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

    public abstract String getImage(char letter);

}
