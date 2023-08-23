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
public class Unit {

    public String _id;
    public String _rev;
    public Transport[] transport;
    public String[] special;
    public String[] uniqueRules;
    public Weapon[] weapons;
    public Weapon[] optionalWeapons;
    public boolean commandCentre;
    public boolean behemoth;
    public BehemothStats behemothStats;
    public boolean hasExtraUnit;
    public boolean released;
    public ExtraPhoto[] extraPhotos;
    public String category;
    public String name;
    public String faction;
    public int points;
    public String move;
    public String countermeasures;
    public int armour;
    public int damage;
    public String type;
    public int minSquadSize;
    public int maxSquadSize;
    public int commandCentreCost;
    public String extraUnit;

    public String getMove() {
        return move;
    }

    public String getArmour() {
        return String.format("%d", armour);
    }

    public String getDamagePoints() {
        return String.format("%d", damage);
    }

    public String getSquadSize() {
        if (maxSquadSize > minSquadSize) {
            return String.format("Squad Size: %d - %d", minSquadSize, maxSquadSize);
        } else {
            return String.format("Squad Size: %d", minSquadSize);
        }
    }

}
