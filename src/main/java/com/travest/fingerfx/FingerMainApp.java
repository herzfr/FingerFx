package com.travest.fingerfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


public class FingerMainApp {

    @FXML
    private Pane pnlConn;

    @FXML
    private Pane pnlVerify;

    @FXML
    private Pane pnlReg;

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
        System.out.println("Connection");
        pnlConn.toFront();

    }

    @FXML
    void onDisconnect(ActionEvent event) {
        System.out.println("Disconnection");
    }

    @FXML
    void onRegistration(ActionEvent event) {
        System.out.println("Registration");
        pnlReg.toFront();
    }

    @FXML
    void onVerify(ActionEvent event) {
        System.out.println("Verify");
        pnlVerify.toFront();
    }
}
