package com.travest.fingerfx;

import SecuGen.FDxSDKPro.jni.*;
import com.travest.fingerfx.Service.ServerRequest;
import com.travest.fingerfx.utility.AppData;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingFXUtils;
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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class FingerMainApp implements Initializable {

    // SECUGEN
    private JSGFPLib jsgfpLib = null;
    SGDeviceInfoParam dvcInfo = new SGDeviceInfoParam();
    private boolean led = false;
    private long ret;
    private long deviceName;
    private long devicePort;

    byte[] imageBufferA;
    byte[] imageBufferB;

    private BufferedImage imgRegA;
    private BufferedImage imgRegB;

    private byte[] regMinA = new byte[400];
    private byte[] regMinB = new byte[400];

    AppData appData;

    BufferedImage imgReg2;

    byte[] imageBuffer2;
    byte[] regMin2;

    ServerRequest serverRequest = new ServerRequest();

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
    private Button testCapture;
    @FXML
    private Label lblAccount;
    @FXML
    private ImageView imgView;
    @FXML
    private TextArea textArea;
    @FXML
    private ImageView imageFinger1;
    @FXML
    private Button capRegBtnA;
    @FXML
    private Button capRegBtnB;
    @FXML
    private Button regBtnOk;
    @FXML
    private TextArea infoCaptA;
    @FXML
    private TextArea infoCaptB;
    @FXML
    private TextField infoResultReg;
    @FXML
    private ImageView imageA;
    @FXML
    private ImageView imageB;


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

        if (jsgfpLib != null) {
            jsgfpLib.CloseDevice();
            jsgfpLib.Close();
            jsgfpLib = null;
        }
        if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            displayCon.setText("CloseDevice() Success [" + ret + "]");
            lblInfo.setText("OFF");
            incubLight.setStyle(" -fx-fill: #e12272;");
        } else {
            displayCon.setText("CloseDevice() Error : " + ret);
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


    private void initialize() {
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
        if (ret == 6) {
            displayCon.appendText("Device not detected \n");
        } else {
            displayCon.appendText("Device detected \n");
        }

        ret = jsgfpLib.EnumerateDevice(ndevs, devList);
        displayCon.appendText("Enumerate Device Called\n");

        for (int i = 0; i < ndevs[0]; i++) {
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
        
        initialize();

    }


    @FXML
    void testFingerCapture(ActionEvent event) {


        System.out.println("halo test capture");

        int[] quality = new int[1];
        int[] numOfMinutiae = new int[1];
        long nfiqvalue;
        imgReg2 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        imageBuffer2 = ((java.awt.image.DataBufferByte) imgReg2.getRaster().getDataBuffer()).getData();

        Image image = SwingFXUtils.toFXImage(imgReg2, null);

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBuffer2, 10 * 1000, 0, 80);
            System.out.println(new String(imageBuffer2));

            jsgfpLib.GetImageQuality(dvcInfo.imageHeight, dvcInfo.imageWidth, imageBuffer2, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                textArea.appendText("Tahap 2 Second Capture\n");
                System.out.println("tahap 2 second capture ");
                imageFinger1.setImage(image);
//                JlabelImage2.setIcon(new ImageIcon(imgReg2.getScaledInstance(130, 150, java.awt.Image.SCALE_DEFAULT)));

                if (quality[0] < 45) {
                    textArea.appendText("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0] + "]. Please try again\n");
                    System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0] + "]. Please try again\n");
                } else {
                    textArea.appendText("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0] + "]. Thank u\n");
                    System.out.println("GetImageEx() Success [" + ret + "] but image quality is [" + quality[0] + "]. Thank u\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBuffer2, regMin2);
                    System.out.println("create template " + new String(regMin2));
                    textArea.appendText("\ncreate template " + new String(regMin2) + "\n");

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageHeight, dvcInfo.imageWidth, imageBuffer2, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBuffer2, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMin2, numOfMinutiae);

                        System.out.println(quality[0]);
                        System.out.println(nfiqvalue);
                        System.out.println(numOfMinutiae);

                        if ((quality[0] >= 45) && (nfiqvalue <= 2) && (numOfMinutiae[0] >= 20)) {
                            textArea.appendText("Verification Capture PASS QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue + "] Minutiae[" + numOfMinutiae[0] + "]\n");
                            System.out.println("Verification Capture PASS QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue + "] Minutiae[" + numOfMinutiae[0] + "]\n");
//                            r2Captured = true;
                            System.out.println("ret value : " + ret);
                        } else {
                            textArea.appendText("Verification Capture FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue + "] Minutiae[" + numOfMinutiae[0] + "]\n");
                            System.out.println("Verification Capture FAIL QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue + "] Minutiae[" + numOfMinutiae[0] + "]\n");
                        }

                    } else {
                        System.out.println("CreateTemplate() Error : " + ret + "\n");
                        textArea.appendText("CreateTemplate() Error : " + ret + "\n");
                    }
                }

            } else {
                textArea.appendText("Failed #1");
                System.out.println("failed");
            }

        } else {
            textArea.appendText("Errorr Guysss jsqfpLib nya \n");
            System.out.println("Error jsgfplibnya ");
        }


    }

    @FXML
    void regCaptA(ActionEvent event) {
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegA = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferA = ((java.awt.image.DataBufferByte) imgRegA.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferA, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoCaptA.appendText("Captured\n");

                BufferedImage resized = resize(imgRegA, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                imageA.setImage(i);

//                System.out.println(quality[0]);
                if (quality[0] < 50) {
                    infoCaptA.appendText("Get Image Success, \nbut image quality is => " + quality[0] + "\n");
                    infoCaptA.appendText("Please Try Again!\n");
                } else {
                    infoCaptA.appendText("Get Image Success, \nyour image quality is => " + quality[0] + "\n");
                    infoCaptA.appendText("Thank You\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferA, regMinA);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferA, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinA, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoCaptA.appendText("Reg. Capture 2 PASS QC. \nQual[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            infoCaptA.appendText("template : " + new String(regMinA) + "\n");
//
                        } else {
                            infoCaptA.appendText("Reg. Capture 2 FAIL QC. \nQuality[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                        }
                    }
                }

            } else {
                infoCaptA.appendText("Captured A is Failed\n");
            }

        } else {
            infoCaptA.setText("Please Initialize Device First in Connection Form");
        }


    }

    private static BufferedImage resize(BufferedImage image, int height, int width) {
        java.awt.Image tmp = image.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    @FXML
    void regCaptB(ActionEvent event) {
        infoCaptB.setText("testtttttt capt B");

        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegB = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferB = ((java.awt.image.DataBufferByte) imgRegB.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferB, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoCaptB.appendText("Captured\n");

                BufferedImage resized = resize(imgRegB, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                imageB.setImage(i);

//                System.out.println(quality[0]);
                if (quality[0] < 50) {
                    infoCaptB.appendText("Get Image Success, \nbut image quality is => " + quality[0] + "\n");
                    infoCaptB.appendText("Please Try Again!\n");
                } else {
                    infoCaptB.appendText("Get Image Success, \nyour image quality is => " + quality[0] + "\n");
                    infoCaptB.appendText("Thank You\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferB, regMinB);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferB, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinB, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoCaptB.appendText("Reg. Capture 2 PASS QC. \nQual[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            infoCaptB.appendText("template : " + new String(regMinB) + "\n");
//
                        } else {
                            infoCaptB.appendText("Reg. Capture 2 FAIL QC. \nQuality[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                        }
                    }
                }

            } else {
                infoCaptB.appendText("Captured A is Failed\n");
            }

        } else {
            infoCaptB.setText("Please Initialize Device First in Connection Form");
        }


    }

    @FXML
    void registrationOk(ActionEvent event) throws IOException {

        infoResultReg.setText("Ok\n");

        long secuLevel = SGFDxSecurityLevel.SL_NORMAL;
        boolean[] matched = new boolean[1];
//        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(regMinA, regMinB, secuLevel, matched);

        String str = new String(regMinA);
        byte[] b = str.getBytes();

        System.out.println("hasil : " + ret);
        infoResultReg.appendText(String.valueOf(matched[0]) + "\n");
//        infoResultReg.appendText(new String(regMinA)+ "\n");
        infoCaptB.setText("");
//        infoCaptB.appendText("\noriginal :" + regMinA + "\n");
//        infoCaptB.appendText("\nkedalam string :" + str + "\n");
//        infoCaptB.appendText("\nkedalam byte :" + new String(b) + "\n");
//        infoCaptB.appendText("\nappdata :" + appData.getRecord().getUsername() + "\n");

        String encoded = Base64.getEncoder().encodeToString(regMinA);
        String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));

        infoCaptB.appendText("original : " + new String(regMinA) +"\n");
        infoCaptB.appendText("encode decode  : " + decoded + "\n");

        serverRequest.registerNewFinger(appData.getRecord().getUsername(), regMinA, appData.getToken());


    }


}
