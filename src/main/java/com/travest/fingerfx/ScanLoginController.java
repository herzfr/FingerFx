package com.travest.fingerfx;

import SecuGen.FDxSDKPro.jni.*;
import com.sun.org.apache.xpath.internal.operations.String;
import com.travest.fingerfx.Entity.Finger;
import com.travest.fingerfx.Service.ServerRequest;
import com.travest.fingerfx.utility.AuthenticateData;
import com.travest.fingerfx.utility.Dialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class ScanLoginController implements Initializable {

    //server request utility
    ServerRequest serverRequest = new ServerRequest();

    //athenticate data
    AuthenticateData authenticateData;

    // SECUGEN
    private JSGFPLib jsgfpLib = null;
    SGDeviceInfoParam dvcInfo = new SGDeviceInfoParam();
    private boolean led = false;
    private long ret;
    private long deviceName;
    private long devicePort;

    // Verification ============
    byte[] imageBufferVerify1;
    byte[] imageBufferVerify2;

    private byte[] regMinC = new byte[400];
    private byte[] regMinD = new byte[400];
    private byte[] regMinE = new byte[400];

    private byte[] regVerify1 = new byte[400];
    private byte[] regVerify2 = new byte[400];
    private byte[] regVerify3 = new byte[400];

    private BufferedImage imgVerif;
    private BufferedImage imgVerif2;

    public String fingerVerif1 = null;
    public String fingerVerif2 = null;
    byte[] imgByteVerif;
    byte[] imgByteVerif2;

    //verification tab properties


    @FXML
    private ImageView imageVerifA;
    @FXML
    private TextArea textStatus;
    @FXML
    private Button btnScan;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initialize();
        Boolean status = serverRequest.getAuthFinger();
        if (status) {
//            System.out.println("running oke");
        } else {
//            System.out.println("running false");
        }

    }


    private void initialize() {
        textStatus.setText("");
        textStatus.appendText("Initialize Device... \n");

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
//        System.out.println(ret);
        if (ret == 6) {
            Dialog.errorMessage("Error", "Finger Scan Device Not Detected");
            textStatus.appendText("Device not detected \n");
        } else {
            textStatus.appendText("Device detected \n");
        }

        ret = jsgfpLib.EnumerateDevice(ndevs, devList);
        textStatus.appendText("Enumerate Device Called\n");

        for (int i = 0; i < ndevs[0]; i++) {
            ret = jsgfpLib.Init(devList[i].devName);
            textStatus.appendText("Device Init : " + ret + "\n");

            ret = jsgfpLib.OpenDevice(devList[i].devID);
            textStatus.appendText("Device Open : " + ret + "\n");

            if (ret != SGFDxErrorCode.SGFDX_ERROR_NONE) {
                textStatus.appendText("Failed Open Device \n");
            } else {
                textStatus.setText("Open Device Success \n");
                textStatus.appendText("Waiting scan your finger \n");

                ret = jsgfpLib.GetDeviceInfo(dvcInfo);
                if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                } else {
                    textStatus.setText("Get Device Info Failed : " + ret + "\n");
                }
            }
        }
    }


    @FXML
    void scanFinger(ActionEvent event) {
        System.out.println("scanning yout finger ");


        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgVerif = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        // Global Byte Image
        imageBufferVerify1 = ((java.awt.image.DataBufferByte) imgVerif.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferVerify1, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferVerify1, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {

                if (quality[0] < 50) {
                    textStatus.appendText("Get Image Success, \nbut image quality is to low \n");
                    textStatus.appendText("Please Try Again!\n");
                } else {
                    textStatus.appendText("Get Image Success\n");
                    textStatus.appendText("Thank You\n");
                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferVerify1, regVerify3);
                    compareTemplate(regVerify3);

                }
            } else {
                textStatus.appendText("Captured A is Failed\n");
            }
        } else {
            textStatus.setText("Please Initialize Device First in Connection Form");
        }


//
    }


    void compareTemplate(byte[] scannedTemplate) {


        List<Finger> data;

        data = authenticateData.getFingerList();

//        int i = 0;
        for (Finger temp : data) {
            regVerify1 = Base64.getDecoder().decode(temp.getTemplate().getBytes());
            regVerify2 = Base64.getDecoder().decode(temp.getTemplate2().getBytes());

            int[] matchScore = new int[1];
            boolean[] matched = new boolean[1];
            long secuLevel = SGFDxSecurityLevel.SL_NORMAL;
            matched[0] = false;

            ret = jsgfpLib.MatchTemplate(regVerify1, regVerify3, secuLevel, matched);

            if (matched[0]) {
                textStatus.setText("");
                textStatus.appendText("Username Anda =>" + temp.getUsername());
                textStatus.appendText("\nOn Finger 1");
//                System.out.println("index =>" + i + " Success");
//                System.out.println("username anda =>" + temp.getUsername());
                break;

            } else {
                textStatus.setText("");
                textStatus.appendText("1st Verification Failed\n");
                boolean status = compare2ndFinger(temp, scannedTemplate);
                if (status) {
                    break;
                }
            }
//            i++;
        }
    }


    boolean compare2ndFinger(Finger finger, byte[] scannedTemplate) {

        int[] matchScore = new int[1];
        boolean[] matched = new boolean[1];
        long secuLevel = SGFDxSecurityLevel.SL_NORMAL;
        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(scannedTemplate, regVerify2, secuLevel, matched);

        if (matched[0]) {
            textStatus.setText("");
            textStatus.appendText("Username Anda => " + finger.getUsername());
            textStatus.appendText("\nOn Finger 2");
//            textStatus.appendText("index : " + i);
//            System.out.println("index =>" + i + " Success");
//            System.out.println("username anda =>" + finger.getUsername());
            return true;
        } else {
            textStatus.setText("Verification Failed\n");
            return false;
        }
    }
}
