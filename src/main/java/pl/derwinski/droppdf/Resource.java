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
package pl.derwinski.droppdf;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Dominik Derwiński
 */
public enum Resource {

    FACTIONS("https://dropzonecommander.com:3001/factions/"),
    FACTIONSV2("https://dropzonecommander.com:3001/factionsv2/"),
    UNITS("https://dropzonecommander.com:3001/unitsv22/%s"),
    UNIT_PHOTOS("https://www.dropzonecommander.com/assets/images/unitphotos/%s/%s.png"),
    TRANSPORT_ICONS("https://www.dropzonecommander.com/assets/images/transporticons/%s/%s"),
    SHIPS("https://dropzonecommander.com:3001/ships/%s"),
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

    public File fixJSON(File file) throws Exception {
        if (this == SHIPS) {
            String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            s = s.replace("\"Weapons\":[\"-\"]", "\"Weapons\":[]");
            s = s.replace("\"LaunchAssets\":[\"-\"]", "\"LaunchAssets\":[]");
            FileUtils.writeStringToFile(file, s, StandardCharsets.UTF_8);
        }
        return file;
    }

}
