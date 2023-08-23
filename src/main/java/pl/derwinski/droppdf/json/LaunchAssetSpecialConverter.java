/*
 * Copyright (C) 2023 morvael
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

import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.ArrayList;

/**
 *
 * @author Dominik Derwiński
 */
public class LaunchAssetSpecialConverter extends StdConverter<Object, String[]> {

    private static final String[] EMPTY = new String[0];

    @Override
    public String[] convert(Object value) {
        if (value instanceof ArrayList<?>) {
            ArrayList<?> list = (ArrayList<?>) value;
            String[] result = new String[list.size()];
            int i = 0;
            for (Object o : list) {
                result[i++] = String.valueOf(o);
            }
            return result;
        } else {
            System.out.println(String.format("Wrong launch asset special %s", String.valueOf(value)));
            return EMPTY;
        }
    }

}
