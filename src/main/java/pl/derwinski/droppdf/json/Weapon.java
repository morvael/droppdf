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
package pl.derwinski.droppdf.json;

/**
 *
 * @author Dominik Derwiński
 */
public class Weapon {

    public String[] special;
    public boolean optional;
    public String[] replaces;
    public boolean replaced;
    public boolean optionalUsed;
    public int amount;
    public String name;
    public String mf;
    public String arc;
    public String rangeFull;
    public String rangeCountered;
    public String shots;
    public String acc;
    public String energy;
    public Integer cost;

    public String getShots() {
        return shots;
    }

    public String getAccuracy() {
        return acc;
    }

    public String getEnergy() {
        return energy;
    }

}
