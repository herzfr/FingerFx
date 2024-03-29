package com.travest.fingerfx;

import com.travest.fingerfx.Entity.Record;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
//        loader.setResources(ResourceBundle.getBundle("fa.fontawesome"));
        loader.setLocation(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();

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
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Secugen Finger App");
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/fingericon.png")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
