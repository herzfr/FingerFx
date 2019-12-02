package com.travest.fingerfx.Service;

import com.travest.fingerfx.Entity.Record;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SceneUtility {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public void homeScene(Stage stage) throws IOException {
//        Window stage = loginAnchor.getScene().getWindow();
        stage.hide();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(root);
        Stage homeStage = new Stage();
        homeStage.getIcons().add(new Image(SceneUtility.class.getResourceAsStream("/images/icon.png")));
        homeStage.initStyle(StageStyle.UTILITY);
        homeStage.setScene(scene);
        homeStage.show();
    }


}
