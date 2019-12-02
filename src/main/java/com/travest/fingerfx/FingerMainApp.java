package com.travest.fingerfx;

import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import com.travest.fingerfx.utility.AppData;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class FingerMainApp implements Initializable {

    // SECUGEN
    private JSGFPLib jsgfpLib = null;
    SGDeviceInfoParam dvcInfo = new SGDeviceInfoParam();
    private boolean led = false;

    AppData appData ;



    @FXML
    private Pane pnlVerify;
    @FXML
    private Pane pnlReg;
    @FXML
    private Pane pnlConn;
    @FXML
    private TextField fieldDSN;
    @FXML
    private TextField fieldBRGS;
    @FXML
    private TextField fieldPORT;
    @FXML
    private TextField fieldSPED;
    @FXML
    private TextField fieldCONT;
    @FXML
    private TextField fieldDVID;
    @FXML
    private TextField fieldFWVS;
    @FXML
    private TextField fieldGAIN;
    @FXML
    private TextField fieldDPI;
    @FXML
    private TextField fieldHEIGHT;
    @FXML
    private TextField fieldWIDTH;
    @FXML
    private Button btnInit;
    @FXML
    private Button btnOn;
    @FXML
    private Button btnOff;
    @FXML
    private TextArea displayCon;
    @FXML
    private FontAwesomeIconView incubLight;
    @FXML
    private Label lblInfo;
    @FXML
    private Button btnConn;
    @FXML
    private Button btnVrfy;
    @FXML
    private Button btnReg;
    @FXML
    private Button btnDisc;
    @FXML
    private Label lblAccount;
    @FXML
    private ImageView imgView;

    @FXML
    void onConnection(ActionEvent event) {
        pnlConn.toFront();
    }

    @FXML
    void onVerify(ActionEvent event) {
        pnlVerify.toFront();
    }

    @FXML
    void onRegister(ActionEvent event) {
        pnlReg.toFront();
    }

    @FXML
    void onDisconnect(ActionEvent event) {

    }

    @FXML
    void onInit(ActionEvent event) {
        initialize();
    }

    @FXML
    void onLedOff(ActionEvent event) {

    }

    @FXML
    void onLedOn(ActionEvent event) {

    }


    private void initialize(){
        displayCon.setText("");
        displayCon.appendText("Initialize Device... \n");


    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            ByteArrayInputStream rocketInputStream = new ByteArrayInputStream(base64Decoder.decodeBuffer(appData.getRecord().getAvatar()));
            imgView.setImage(new Image(rocketInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        lblAccount.setText(appData.getRecord().getUsername());

    }


}
