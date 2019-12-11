package com.travest.fingerfx;

import SecuGen.FDxSDKPro.jni.*;
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
    private long ret;
    private long deviceName;
    private long devicePort;

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
    private FontAwesomeIconView conLog;
    @FXML
    private FontAwesomeIconView keyLog;
    @FXML
    private FontAwesomeIconView regLog;
    @FXML
    private FontAwesomeIconView clsLog;
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
        conLog.setStyle("-fx-fill: #2bc344;");
        keyLog.setStyle("-fx-fill: #fff;");
        regLog.setStyle("-fx-fill: #fff;");
        clsLog.setStyle("-fx-fill: #fff;");
    }

    @FXML
    void onVerify(ActionEvent event) {
        pnlVerify.toFront();
        conLog.setStyle("-fx-fill: #fff;");
        keyLog.setStyle("-fx-fill: #2bc344;");
        regLog.setStyle("-fx-fill: #fff;");
        clsLog.setStyle("-fx-fill: #fff;");
    }

    @FXML
    void onRegister(ActionEvent event) {
        pnlReg.toFront();
        conLog.setStyle("-fx-fill: #fff;");
        keyLog.setStyle("-fx-fill: #fff;");
        regLog.setStyle("-fx-fill: #2bc344;");
        clsLog.setStyle("-fx-fill: #fff;");
    }

    @FXML
    void onDisconnect(ActionEvent event) {
        conLog.setStyle("-fx-fill: #fff;");
        keyLog.setStyle("-fx-fill: #fff;");
        regLog.setStyle("-fx-fill: #fff;");
        clsLog.setStyle("-fx-fill: #2bc344;");

        if (jsgfpLib != null ) {
            jsgfpLib.CloseDevice();
            jsgfpLib.Close();
            jsgfpLib = null;
        }
        if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            displayCon.setText("CloseDevice() Success [" + ret + "]");
            lblInfo.setText("OFF");
            incubLight.setStyle(" -fx-fill: #e12272;");
        } else {
            displayCon.setText( "CloseDevice() Error : " + ret);
        }

    }

    @FXML
    void onInit(ActionEvent event) {
        initialize();
    }

    @FXML
    void onLedOff(ActionEvent event) {

        displayCon.appendText("");
        displayCon.appendText(" \n");
        displayCon.appendText(jsgfpLib + "\n");

        if (jsgfpLib != null) {
            led = false;
            ret = jsgfpLib.SetLedOn(led);
            if (led == false) {
                displayCon.appendText("Led OFF\n");
            } else {
                displayCon.appendText("Led ON\n");
            }
        } else {
            displayCon.appendText("Filed to On, Device not Found");
        }

    }

    @FXML
    void onLedOn(ActionEvent event) {

        displayCon.appendText("");
        displayCon.appendText(" \n");
        displayCon.appendText(jsgfpLib + "\n");

        if (jsgfpLib != null) {
            led = true;
            ret = jsgfpLib.SetLedOn(led);
            if (led == true) {
                displayCon.appendText("Led ON\n");
            } else {
                displayCon.appendText("Led OFF\n");
            }
        } else {
            displayCon.appendText("Filed to On, Device not Found");
        }



    }


    private void initialize(){
        displayCon.setText("");
        displayCon.appendText("Initialize Device... \n");

        if (jsgfpLib != null) {
            jsgfpLib.CloseDevice();
            jsgfpLib.Close();
            jsgfpLib = null;
        }

        jsgfpLib = new JSGFPLib();
        ret = jsgfpLib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        deviceName = SGFDxDeviceName.SG_DEV_FDU04;

        SGDeviceList[] devList = new SGDeviceList[10];
        for (int i = 0; i < 10; ++i) {
            devList[i] = new SGDeviceList();
        }
        int[] ndevs = new int[1];

        ret = jsgfpLib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        System.out.println(ret);
        if (ret == 6 ) {
            displayCon.appendText("Device not detected \n");
        } else {
            displayCon.appendText("Device detected \n");
        }

        ret = jsgfpLib.EnumerateDevice(ndevs, devList);
        displayCon.appendText("Enumerate Device Called\n");

        for (int i = 0 ; i < ndevs[0]; i++) {
            ret = jsgfpLib.Init(devList[i].devName);
            displayCon.appendText("Device Init : " + ret + "\n");

            ret = jsgfpLib.OpenDevice(devList[i].devID);
            displayCon.appendText("Device Open : " + ret + "\n");

            System.out.println(ret);
            System.out.println(SGFDxErrorCode.SGFDX_ERROR_NONE);

            if (ret != SGFDxErrorCode.SGFDX_ERROR_NONE) {
                displayCon.appendText("Filed Open Device \n");
            } else {
                displayCon.appendText("Open Device Success \n");

                ret = jsgfpLib.GetDeviceInfo(dvcInfo);
                if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {

                    fieldDSN.setText(new String(dvcInfo.deviceSN()) + "\n");
                    fieldBRGS.setText(dvcInfo.brightness + "\n");
                    fieldPORT.setText(dvcInfo.comPort + "\n");
                    fieldSPED.setText(dvcInfo.comSpeed + "\n");
                    fieldCONT.setText(dvcInfo.contrast + "\n");
                    fieldDVID.setText(dvcInfo.deviceID + "\n");
                    fieldFWVS.setText(dvcInfo.FWVersion + "\n");
                    fieldGAIN.setText(dvcInfo.gain + "\n");
                    fieldDPI.setText(dvcInfo.imageDPI + "\n");
                    fieldHEIGHT.setText(dvcInfo.imageHeight + "\n");
                    fieldWIDTH.setText(+dvcInfo.imageWidth + "\n");


                    devicePort = dvcInfo.comPort;
                    lblInfo.setText("ON");
                    incubLight.setStyle(" -fx-fill: #2bc344;");



                } else {
                    displayCon.appendText("Get Device Info Filed : " + ret + "\n");
                }
            }

        }

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

        fieldDSN.setDisable(true);
        fieldBRGS.setDisable(true);
        fieldPORT.setDisable(true);
        fieldSPED.setDisable(true);
        fieldCONT.setDisable(true);
        fieldDVID.setDisable(true);
        fieldFWVS.setDisable(true);
        fieldGAIN.setDisable(true);
        fieldDPI.setDisable(true);
        fieldHEIGHT.setDisable(true);
        fieldWIDTH.setDisable(true);
        displayCon.setEditable(false);
        conLog.setStyle("-fx-fill: #2bc344;");

    }


}
