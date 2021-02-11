package pl.kubaretip;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import pl.kubaretip.fx.LanguageConstants;
import pl.kubaretip.fx.ResourceLoader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Start extends Application {

    private final static int MIN_WINDOW_WIDTH = 320;
    private final static int MIN_WINDOW_HEIGHT = 255;
    private final static String WINDOW_TITLE = "Auto-hold clicker";
    private static final String FXML_PATH = "window.fxml";

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var scene = loadStage(FXML_PATH, new ResourceLoader(LanguageConstants.EN));
        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
        stage.getIcons().add(new Image(Start.class.getResourceAsStream("/images/icon.png")));
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.resizableProperty().setValue(false);
        stage.setScene(scene);
        stage.show();
    }

    Scene loadStage(String fxmlPath, ResourceLoader loader) throws IOException {
        FXMLLoader fxmlLoader = loader.fxmlLoader(fxmlPath);
        Parent root = fxmlLoader.load();
        return new Scene(root, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
    }

}
