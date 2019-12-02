package com.travest.fingerfx;

import com.travest.fingerfx.utility.Dialog;
import com.travest.fingerfx.Service.LoginService;
import com.travest.fingerfx.Service.SceneUtility;
import com.travest.fingerfx.Service.ServerRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;


public class LoginApp {



    String usr;
    String pass;
    String getToken;
    String getUsr;
    boolean getSuccess;

    LoginService loginService;
    ServerRequest serverRequest;

    @FXML
    private TextField userName;
    @FXML
    private PasswordField passWord;
    @FXML
    public Button btnLogin;
    @FXML
    private Button btnCancel;
    @FXML
    private AnchorPane loginAnchor;


    @FXML
    void loginTo(ActionEvent event) throws IOException {
        usr = userName.getText().trim();
        pass = passWord.getText().trim();

        if (isNullOrEmpty(usr) || isNullOrEmpty(pass)) {
            Dialog.errorMessage("Field Empty", "Please fill all field");
        } else {
            LoginService login = new LoginService();
            Boolean status = null;
            status = login.loginRequest(usr, pass);
            if(status){
                Window stage = loginAnchor.getScene().getWindow();
               SceneUtility sceneUtility  = new SceneUtility();
               sceneUtility.homeScene((Stage) loginAnchor.getScene().getWindow());
            }
        }
    }

    @FXML
    void onPassKeyEntered(KeyEvent event) throws IOException {
        usr = userName.getText().trim();
        pass = passWord.getText().trim();

        if (event.getCode() == KeyCode.ENTER) {
            if (isNullOrEmpty(usr) || isNullOrEmpty(pass)) {
                Dialog.errorMessage("Field Empty", "Please fill all field");
            } else {
                LoginService login = new LoginService();
                Boolean status = login.loginRequest(usr, pass);

                if(status){
                    Window stage = loginAnchor.getScene().getWindow();
                    SceneUtility sceneUtility  = new SceneUtility();
                    sceneUtility.homeScene((Stage) loginAnchor.getScene().getWindow());
                }

            }
        }
    }

    @FXML
    void onBtnCancel(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

//    public void homeScene() throws IOException {
//        Window stage = loginAnchor.getScene().getWindow();
//        stage.hide();
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
//
//        root.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                xOffset = event.getSceneX();
//                yOffset = event.getSceneY();
//            }
//        });
//
//        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                xOffset = event.getSceneX();
//                yOffset = event.getSceneY();
//            }
//        });
//
//
//        Scene scene = new Scene(root);
//        Stage homeStage = new Stage();
////        homeStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icon.png")));
//        homeStage.setScene(scene);
//        homeStage.initStyle(StageStyle.UNDECORATED);
//        homeStage.show();
//    }


}
