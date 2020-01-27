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

/**
 *
 * @author Dominik Derwiński
 */
public class Ship {

    public String _id;
    public String _rev;
    public String Name;
    public String Faction;
    public String Designation;
    public String Scan;
    public String Signal;
    public String Thrust;
    public String Hull;
    public String Armour;
    public String PointDefence;
    public int GroupMin;
    public int GroupMax;
    public String Tonnage;
    public int TonnageClass;
    public int Points;
    public int HardPoints;
    public String[] Special;
    public ShipWeapon[] Weapons;
    public LaunchAsset[] LaunchAssets;
    public String[] SpecRules;
    public int MinHardPoints;
    public int MaxBroadSides;
    public String[] icons;

    public String getGroup() {
        if (GroupMax > GroupMin) {
            return String.format("%d-%d", GroupMin, GroupMax);
        } else {
            return String.format("%d", GroupMin);
        }
    }
}
