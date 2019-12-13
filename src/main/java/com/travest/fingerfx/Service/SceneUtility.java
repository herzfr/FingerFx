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

        Scene scene = new Scene(root);
        Stage homeStage = new Stage();

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
                homeStage.setX(event.getScreenX() - xOffset);
                homeStage.setY(event.getScreenY() - yOffset);
            }
        });

        homeStage.getIcons().add(new Image(SceneUtility.class.getResourceAsStream("/images/fingericon.png")));
        homeStage.initStyle(StageStyle.UNDECORATED);
        homeStage.setScene(scene);
        homeStage.show();
    }

    public void fingerLoginStage(Stage stage) throws IOException {
//        Window stage = loginAnchor.getScene().getWindow();
        stage.hide();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

        Scene scene = new Scene(root);
        Stage homeStage = new Stage();

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
                homeStage.setX(event.getScreenX() - xOffset);
                homeStage.setY(event.getScreenY() - yOffset);
            }
        });

        homeStage.getIcons().add(new Image(SceneUtility.class.getResourceAsStream("/images/fingericon.png")));
        homeStage.initStyle(StageStyle.UNDECORATED);
        homeStage.setScene(scene);
        homeStage.show();
    }


}
