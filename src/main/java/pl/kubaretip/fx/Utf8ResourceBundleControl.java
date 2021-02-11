package pl.kubaretip.fx;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * https://docs.oracle.com/javase/7/docs/api/java/util/ResourceBundle.Control.html
 */
public class Utf8ResourceBundleControl extends ResourceBundle.Control {

    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {

        if (baseName == null || locale == null || format == null || loader == null)
            throw new NullPointerException();
        ResourceBundle bundle = null;

        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, format);
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    // Disable caches to get fresh data for
                    // reloading.
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                bundle = new PropertyResourceBundle(inputStreamReader);
            } finally {
                stream.close();
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            }

        }

        return bundle;
    }
}
