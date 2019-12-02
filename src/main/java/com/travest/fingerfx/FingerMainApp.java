package com.travest.fingerfx;

import com.travest.fingerfx.utility.AppData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;


public class FingerMainApp implements Initializable {

   AppData singleton;

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("from home main");
        System.out.println(singleton.getRecord().getUsername());
        System.out.println(singleton.getToken());
    }
}
