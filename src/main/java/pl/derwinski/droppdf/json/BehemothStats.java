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
public class BehemothStats {

    public int HullPoints;
    public int HullDamagePoints;
    public String HullEffect;
    public int LegsPoints;
    public int LegsDamagePoints;
    public String LegsEffect;
    public int WeaponsPoints;
    public int WeaponsDamagePoints;
    public String WeaponsEffect;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BehemothStats{HullPoints=").append(HullPoints);
        sb.append(", HullDamagePoints=").append(HullDamagePoints);
        sb.append(", HullEffect=").append(HullEffect);
        sb.append(", LegsPoints=").append(LegsPoints);
        sb.append(", LegsDamagePoints=").append(LegsDamagePoints);
        sb.append(", LegsEffect=").append(LegsEffect);
        sb.append(", WeaponsPoints=").append(WeaponsPoints);
        sb.append(", WeaponsDamagePoints=").append(WeaponsDamagePoints);
        sb.append(", WeaponsEffect=").append(WeaponsEffect);
        sb.append('}');
        return sb.toString();
    }

}
