package pl.kubaretip.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import pl.kubaretip.core.SupportedKeys;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WindowController implements Initializable, NativeKeyListener {

    private final Robot robot;
    private SupportedKeys selectedKey;
    private int repeatQuantity;
    private int keyHoldTime;
    private Thread clickingButtonThread = null;
    private boolean forceStopClick = false;

    @FXML
    private Spinner<Integer> repeatQuantitySpinner;

    @FXML
    private Spinner<Integer> keyHoldTimeSpinner;

    @FXML
    private VBox container;

    @FXML
    private ChoiceBox<String> selectKey;

    @FXML
    private Label startLabel;

    public WindowController() throws AWTException {
        GlobalScreen.addNativeKeyListener(this);
        this.robot = new Robot();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        repeatQuantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 100));
        keyHoldTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1000));
        selectKey.setItems(FXCollections.observableArrayList(Arrays.stream(SupportedKeys.values())
                .map(SupportedKeys::getName)
                .collect(Collectors.toList())));
        selectKey.setValue(SupportedKeys.MOUSE_L_BUTTON.getName());
        setupOnlyInputNumberSpinner(keyHoldTimeSpinner);
        setupOnlyInputNumberSpinner(repeatQuantitySpinner);
    }

    private void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    private void unregisterNativeHook() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    public void setupOnlyInputNumberSpinner(Spinner<Integer> spinner) {
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinner.getEditor().setText(oldValue);
            }
        });
    }

    private void simulateKeyPress() {
        robot.keyPress(selectedKey.getEvent());
        robot.delay(keyHoldTime);
        robot.keyRelease(selectedKey.getEvent());
    }

    private void simulateMousePress() {
        robot.mousePress(selectedKey.getEvent());
        robot.delay(keyHoldTime);
        robot.mouseRelease(selectedKey.getEvent());
    }

    public void maximize() {
        var stage = (Stage) container.getScene().getWindow();
        stage.setIconified(true);
        stage.setIconified(false);
    }

    @FXML
    public void onActionSaveSettingsButton() {
        keyHoldTime = keyHoldTimeSpinner.getValue();
        repeatQuantity = repeatQuantitySpinner.getValue();
        Arrays.stream(SupportedKeys.values())
                .filter(supportedKeys -> supportedKeys.getName().equals(this.selectKey.getValue()))
                .findFirst()
                .ifPresent(selectKey -> this.selectedKey = selectKey);
        registerNativeHook();
        startLabel.setTextFill(Color.GREEN);
    }

    public void startClicking() {
        if (selectedKey.getType() == 0) {
            for (int i = 0; i < repeatQuantity; i++) {
                if (forceStopClick) {
                    return;
                }
                simulateKeyPress();
            }
        }
        if (selectedKey.getType() == 1) {
            for (int i = 0; i < repeatQuantity; i++) {
                if (forceStopClick) {
                    return;
                }
                simulateMousePress();
            }
        }
        stopClicking();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_F2) {
            if (clickingButtonThread == null) {
                forceStopClick = false;
                clickingButtonThread = new Thread(this::startClicking);
                clickingButtonThread.start();
            }
        }
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_F3) {
            if (clickingButtonThread != null) {
                stopClicking();
            }
        }
    }

    public void stopClicking() {
        forceStopClick = true;
        clickingButtonThread = null;
        unregisterNativeHook();
        Platform.runLater(this::maximize);
        startLabel.setTextFill(Color.RED);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        // empty
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        // empty
    }
}
