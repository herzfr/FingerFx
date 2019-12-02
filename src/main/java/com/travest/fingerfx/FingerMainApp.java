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
        pnlConn.toFront();
    }

    @FXML
    void onDisconnect(ActionEvent event) {

    }

    @FXML
    void onRegister(ActionEvent event) {
        pnlReg.toFront();
    }

    @FXML
    void onVerify(ActionEvent event) {
        pnlVerify.toFront();
    }
}
