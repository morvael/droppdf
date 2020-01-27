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
package pl.derwinski.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Dominik Derwiński
 */
public class Downloader {

    private static final TrustManager TRUST_ALL = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

    };

    private final File root;
    private final File dataRoot;
    private final DownloadMode mode;

    private static volatile boolean sslInitialized;

    public Downloader(File root, DownloadMode mode) throws Exception {
        this.root = root;
        this.dataRoot = new File(root, "data");
        this.dataRoot.mkdirs();
        this.mode = mode;
        if (sslInitialized == false) {
            synchronized (Downloader.class) {
                if (sslInitialized == false) {
                    try {
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, new TrustManager[]{TRUST_ALL}, null);
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    } finally {
                        sslInitialized = true;
                    }
                }
            }
        }
    }

    public File getFile(String format, Object... args) {
        return getFile(String.format(format, args));
    }

    public File getFile(String name) {
        return new File(root, name);
    }

    public File getDataFile(String format, Object... args) {
        return getDataFile(String.format(format, args));
    }

    public File getDataFile(String name) {
        return new File(dataRoot, name);
    }

    public File download(URL url, String format, Object... args) throws Exception {
        return download(url, String.format(format, args));
    }

    public File download(URL url, String name) throws Exception {
        File file = new File(dataRoot, name);
        if (mode != DownloadMode.NONE) {
            if (file.exists() == false || mode == DownloadMode.ALL || (mode == DownloadMode.JSON && name.toLowerCase().endsWith("json"))) {
                FileUtils.copyURLToFile(url, file, 5000, 5000);
            }
        }
        if (file.exists() == false) {
            throw new FileNotFoundException(name);
        }
        return file;
    }

}
