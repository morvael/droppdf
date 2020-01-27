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
public class Faction {

    public String _id;
    public String _rev;
    public String Name;
    public String Lore;
    public String GamePlay;
    public String FleetGamePlay;
    public String Imageurl;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faction{_id=").append(_id);
        sb.append(", _rev=").append(_rev);
        sb.append(", Name=").append(Name);
        sb.append(", Lore=").append(Lore);
        sb.append(", GamePlay=").append(GamePlay);
        sb.append(", FleetGamePlay=").append(FleetGamePlay);
        sb.append(", Imageurl=").append(Imageurl);
        sb.append('}');
        return sb.toString();
    }

}
