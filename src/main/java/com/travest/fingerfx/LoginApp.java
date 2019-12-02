package com.travest.fingerfx;

import com.sun.security.ntlm.Server;
import com.travest.fingerfx.Entity.Record;
import com.travest.fingerfx.Service.LoginService;
import com.travest.fingerfx.Service.ServerRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



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
    private Button btnLogin;

    @FXML
    void loginTo(ActionEvent event) {
        usr = userName.getText();
        pass = passWord.getText();

        LoginService login = new LoginService();
        login.loginRequest(usr, pass);

    }

}
