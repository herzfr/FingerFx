package com.travest.fingerfx.utility;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Dialog {

    public static void errorMessage(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
        buttonBar.setDisable(true);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
        // now we can retrive alert bounds:
        double yIni = -alert.getHeight();
        double yEnd = alert.getY();
        // and move alert to the top of the screen
        alert.setY(yIni);
        buttonBar.getButtons().stream().filter(b -> ((Button) b).isDefaultButton()).findFirst()
                .ifPresent(b -> ((Button) b).addEventFilter(EventType.ROOT,
                        e -> {
                            if (e.getEventType().equals(ActionEvent.ACTION)) {
                                e.consume();
                                final DoubleProperty yPropertyOut = new SimpleDoubleProperty(yEnd);
                                yPropertyOut.addListener((ov, n, n1) -> alert.setY(n1.doubleValue()));
                                Timeline timeOut = new Timeline();
                                timeOut.getKeyFrames().add(new KeyFrame(Duration.millis(300), t -> alert.close(),
                                        new KeyValue(yPropertyOut, yIni, Interpolator.EASE_BOTH)));
                                timeOut.play();
                            }
                        }));
        final DoubleProperty yProperty = new SimpleDoubleProperty();
        yProperty.addListener((ob, n, n1) -> alert.setY(n1.doubleValue()));
        Timeline timeIn = new Timeline();
        timeIn.getKeyFrames().add(new KeyFrame(Duration.millis(300), e -> {
            buttonBar.setDisable(false);
        }, new KeyValue(yProperty, yEnd, Interpolator.EASE_BOTH)));
        timeIn.play();
    }

}
