package com.travest.fingerfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;

public class FingerMainApp {

    @FXML
    private Button btnConn;

    @FXML
    private Button btnVrfy;

    @FXML
    private Button btnReg;

    @FXML
    private Button btnDisc;

    @FXML
    void onConnection(ActionEvent event) {
        System.out.println("this connect");
    }

    @FXML
    void onDisconnect(ActionEvent event) {
        System.out.println("this Disconnect");
    }

    @FXML
    void onRegistration(ActionEvent event) {
        System.out.println("this Registration");
    }

    @FXML
    void onVerify(ActionEvent event) {
        System.out.println("this Verification");
    }
}
