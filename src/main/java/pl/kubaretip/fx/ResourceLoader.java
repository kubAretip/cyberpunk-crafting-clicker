package pl.kubaretip.fx;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceLoader {

    private static Locale locale;

    public ResourceLoader(LanguageConstants language) {
        if (language == LanguageConstants.EN) {
            locale = new Locale("en", "EN");
        }

    }

    private URL getFxmlUrl(String fxmlPath) {

        URL url = null;

        try {
            url = getClass().getClassLoader().getResource(fxmlPath);
            System.out.println("FXML URL : " + url);
            if (url == null) {
                throw new NullPointerException("** File with " + fxmlPath + " path doesn't exist **");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    public FXMLLoader fxmlLoader(String fxmlPath) {

        FXMLLoader fxmlLoader = new FXMLLoader();
        ResourceBundle resourceBundle = getLanguageBundleResources(locale);

        fxmlLoader.setLocation(getFxmlUrl(fxmlPath));
        fxmlLoader.setResources(resourceBundle);

        return fxmlLoader;
    }

    private ResourceBundle getLanguageBundleResources(Locale locale) {

        Utf8ResourceBundleControl resourceBundleControl = new Utf8ResourceBundleControl();
        ResourceBundle resourceBundle = null;

        try {
            resourceBundle = resourceBundleControl.newBundle("bundles/language", locale, "properties", getClass().getClassLoader(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourceBundle;
    }

}
