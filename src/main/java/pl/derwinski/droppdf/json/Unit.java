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
public class Unit {

    public String _id;
    public String _rev;
    public String Id;
    public String Name;
    public int Armour;
    public int Move;
    public String[] CounterMeasures;
    public int DamagePoints;
    public int Points;
    public String Type;
    public String Category;
    public String[] TransportOptions;
    public String[] ExtraRules;
    public String[] Special;
    public Weapon[] Weapons;
    public String Faction;
    public int MinSquadSize;
    public int MaxSquadSize;
    public int Move2;
    public boolean AlternateMove;
    public BehemothStats BehemothStats;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Unit{_id=").append(_id);
        sb.append(", _rev=").append(_rev);
        sb.append(", Id=").append(Id);
        sb.append(", Name=").append(Name);
        sb.append(", Armour=").append(Armour);
        sb.append(", Move=").append(Move);
        sb.append(", CounterMeasures=").append(Arrays.toString(CounterMeasures));
        sb.append(", DamagePoints=").append(DamagePoints);
        sb.append(", Points=").append(Points);
        sb.append(", Type=").append(Type);
        sb.append(", Category=").append(Category);
        sb.append(", TransportOptions=").append(Arrays.toString(TransportOptions));
        sb.append(", ExtraRules=").append(Arrays.toString(ExtraRules));
        sb.append(", Special=").append(Arrays.toString(Special));
        sb.append(", Weapons=").append(Arrays.toString(Weapons));
        sb.append(", Faction=").append(Faction);
        sb.append(", MinSquadSize=").append(MinSquadSize);
        sb.append(", MaxSquadSize=").append(MaxSquadSize);
        sb.append(", Move2=").append(Move2);
        sb.append(", AlternateMove=").append(AlternateMove);
        sb.append(", BehemothStats=").append(BehemothStats);
        sb.append('}');
        return sb.toString();
    }

    public String getMove() {
        if (Move2 > 0) {
            return String.format("%d\"-%d\"", Move, Move2);
        } else {
            return String.format("%d\"", Move);
        }
    }

    public String getCounterMeasures() {
        if (CounterMeasures == null || CounterMeasures.length == 0) {
            return "-";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String cm : CounterMeasures) {
                sb.append(cm);
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
    }

    public String getArmour() {
        return String.format("%d", Armour);
    }

    public String getDamagePoints() {
        return String.format("%d", DamagePoints);
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

    public String getSquadSize() {
        if (MaxSquadSize > MinSquadSize) {
            return String.format("Squad Size: %d - %d", MinSquadSize, MaxSquadSize);
        } else {
            return String.format("Squad Size: %d", MinSquadSize);
        }
    }

}
