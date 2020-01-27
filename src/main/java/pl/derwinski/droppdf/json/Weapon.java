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
package pl.derwinski.droppdf.json;

import java.util.Arrays;

/**
 *
 * @author Dominik Derwiński
 */
public class Weapon {

    public String Name;
    public int Energy;
    public int Shots;
    public int Accuracy;
    public String RangeFull;
    public String RangeCountered;
    public String MoveFire;
    public String Arc;
    public boolean Optional;
    public int OptionalCost;
    public String[] Special;
    public String FullString;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Weapon{Name=").append(Name);
        sb.append(", Energy=").append(Energy);
        sb.append(", Shots=").append(Shots);
        sb.append(", Accuracy=").append(Accuracy);
        sb.append(", RangeFull=").append(RangeFull);
        sb.append(", RangeCountered=").append(RangeCountered);
        sb.append(", MoveFire=").append(MoveFire);
        sb.append(", Arc=").append(Arc);
        sb.append(", Optional=").append(Optional);
        sb.append(", OptionalCost=").append(OptionalCost);
        sb.append(", Special=").append(Arrays.toString(Special));
        sb.append(", FullString=").append(FullString);
        sb.append('}');
        return sb.toString();
    }

    public String getShots() {
        return String.format("%d", Shots);
    }

    public String getAccuracy() {
        if (Accuracy > 0) {
            return String.format("%d+", Accuracy);
        } else {
            return "-";
        }
    }

    public String getEnergy() {
        return String.format("%d", Energy);
    }

    public String getSpecial() {
        if (Special == null || Special.length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String sp : Special) {
                sb.append(sp);
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
            return sb.toString();
        }
    }

}
