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

import java.net.URL;
import org.apache.commons.httpclient.util.URIUtil;

/**
 *
 * @author Dominik Derwiński
 */
public enum Resource {

    FACTIONS("https://thetrolltrader.com:3001/factions/"),
    UNITS("https://thetrolltrader.com:3001/units/%s"),
    UNIT_PHOTOS("https://www.dropzonecommander.com/builder/assets/images/unitphotos/%s/%s.jpg"),
    TRANSPORT_ICONS("https://www.dropzonecommander.com/builder/assets/images/transporticons/%s"),
    SHIPS("https://thetrolltrader.com:3001/ships/%s"),
    SHIP_PHOTOS("https://dropfleetcommander.com/assets/images/shipphotos/%s/%s.png"),
    SHIP_ICONS("https://dropfleetcommander.com/assets/images/ShipIcons/%s.png");

    private final String url;

    private Resource(String url) {
        this.url = url;
    }

    public URL getURL() throws Exception {
        return getAnyURL(url);
    }

    public URL getURL(String... args) throws Exception {
        Object[] args2 = new Object[args.length];
        int i = 0;
        for (String a : args) {
            args2[i++] = URIUtil.encodePath(a);
        }
        return getAnyURL(String.format(url, args2));
    }

    public static URL getAnyURL(String url) throws Exception {
        return new URL(url);
    }

    public static String getExtension(String path) {
        int index = path.lastIndexOf(".");
        if (index != -1) {
            return path.substring(index + 1);
        } else {
            return "";
        }
    }

}
