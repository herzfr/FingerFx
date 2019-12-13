package com.travest.fingerfx;

import SecuGen.FDxSDKPro.jni.*;
import com.travest.fingerfx.Service.ServerRequest;
import com.travest.fingerfx.utility.AppData;
import com.travest.fingerfx.utility.Dialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
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

    // Byte Image ============
    byte[] imageBufferA1;
    byte[] imageBufferB1;
    byte[] imageBufferA2;
    byte[] imageBufferB2;

    // Image ============
    private BufferedImage imgRegA1;
    private BufferedImage imgRegB1;
    private BufferedImage imgRegA2;
    private BufferedImage imgRegB2;

    // Templete ============
    private byte[] regMinA1 = new byte[400];
    private byte[] regMinB1 = new byte[400];
    private byte[] regMinA2 = new byte[400];
    private byte[] regMinB2 = new byte[400];

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

    //application data
    AppData appData;

    //server request service
    ServerRequest serverRequest = new ServerRequest();

    @FXML
    private Button btnConn;
    @FXML
    private Button btnVrfy;
    @FXML
    private Button btnReg;
    @FXML
    private Button btnUpdt;
    @FXML
    private FontAwesomeIconView conLog;
    @FXML
    private FontAwesomeIconView keyLog;
    @FXML
    private FontAwesomeIconView regLog;
    @FXML
    private FontAwesomeIconView clsLog;
    @FXML
    private Label lblAccount;
    @FXML
    private ImageView imgView;
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
    private Pane pnlVerify;
    @FXML
    private TextArea infoCaptA1;
    @FXML
    private Button btnVerify;
    @FXML
    private ImageView imageVerifA;
    @FXML
    private ImageView imageVerifB;
    @FXML
    private Pane pnlReg;
    @FXML
    private AnchorPane frameUpdtA;
    @FXML
    private ImageView iregistrasiA1;
    @FXML
    private TextArea infoReg;
    @FXML
    private AnchorPane frameUpdtB;
    @FXML
    private ImageView iregistrasiB1;
    @FXML
    private Button regBtnOk;
    @FXML
    private Button regBtnB1;
    @FXML
    private Label infoFinger2;
    @FXML
    private Button regBtnA1;
    @FXML
    private AnchorPane frameUpdtA1;
    @FXML
    private ImageView iregistrasiB2;
    @FXML
    private AnchorPane frameUpdtA2;
    @FXML
    private ImageView iregistrasiA2;
    @FXML
    private Label infoFinger1;
    @FXML
    private Button regBtnA2;
    @FXML
    private Button regBtnB2;
    @FXML
    private Pane pnlUpd;
    @FXML
    private ImageView iupdateA1;
    @FXML
    private TextArea infoUpd;
    @FXML
    private AnchorPane frameUpdtB1;
    @FXML
    private ImageView iupdateB1;
    @FXML
    private Button updBtnOk;
    @FXML
    private Button updBtnB1;
    @FXML
    private Label infoLabelB;
    @FXML
    private Button updBtnA1;
    @FXML
    private AnchorPane frameUpdtB2;
    @FXML
    private ImageView iupdateB2;
    @FXML
    private ImageView iupdateA2;
    @FXML
    private Label infoLabelA;
    @FXML
    private Button updBtnA2;
    @FXML
    private Button updBtnB2;


    // ===================================================================================================================
    // CUNSTRUCTION OR TRIGGERRR
    // ===================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            ByteArrayInputStream rocketInputStream = new ByteArrayInputStream(base64Decoder.decodeBuffer(appData.getRecord().getAvatar()));
            imgView.setImage(new Image(rocketInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conLog.setStyle("-fx-fill: #2bc344;");
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

        // Disable Capt Button Reg
        regBtnB1.setDisable(true);
        regBtnA2.setDisable(true);
        regBtnB2.setDisable(true);
        regBtnOk.setDisable(true);

        updBtnB1.setDisable(true);
        updBtnA2.setDisable(true);
        updBtnB2.setDisable(true);
        updBtnOk.setDisable(true);
        infoCaptA1.setDisable(true);

        System.out.println("is registered"+appData.getIsFingerRegistered());

        if(appData.getIsFingerRegistered()){
            initVerify();
            btnReg.setDisable(true);
        }else{
            btnVrfy.setDisable(true);
            btnUpdt.setDisable(true);
        }

        initialize();
    }


    private static BufferedImage resize(BufferedImage image, int height, int width) {
        java.awt.Image tmp = image.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }


    // ===================================================================================================================
    // MAIN BUTTON CONNECT / VERIFY / REGISTRATION / UPDATE
    // ===================================================================================================================

    @FXML
    void onConnection(ActionEvent event) {
        pnlConn.toFront();
        conLog.setStyle("-fx-fill: #2bc344;");
        keyLog.setStyle("-fx-fill: #fff;");
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
    void onVerify(ActionEvent event) {
        pnlVerify.toFront();
        conLog.setStyle("-fx-fill: #fff;");
        keyLog.setStyle("-fx-fill: #2bc344;");
        regLog.setStyle("-fx-fill: #fff;");
        clsLog.setStyle("-fx-fill: #fff;");

        Boolean status = serverRequest.getFinger(appData.getRecord().getUsername(), appData.getToken());
        System.out.println(appData.getFinger().getTemplate());
        System.out.println("get Finger ");

        if(status){
            initVerify();
        }else{
            Dialog.errorMessage("Error", "Get updated finger failed");
        }

    }

    @FXML
    void onUpdate(ActionEvent event) {
        pnlUpd.toFront();
        conLog.setStyle("-fx-fill: #fff;");
        keyLog.setStyle("-fx-fill: #fff;");
        regLog.setStyle("-fx-fill: #fff;");
        clsLog.setStyle("-fx-fill: #2bc344;");
    }

    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }


    // ===================================================================================================================
    // CONNECT
    // ===================================================================================================================

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

    // ===================================================================================================================
    // REGISTRATION
    // ===================================================================================================================
    @FXML
    void regCaptA1(ActionEvent event) {
        infoReg.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegA1 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferA1 = ((java.awt.image.DataBufferByte) imgRegA1.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferA1, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA1, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoReg.appendText("Captured A1\n");

                BufferedImage resized = resize(imgRegA1, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iregistrasiA1.setImage(i);

                if (quality[0] < 50) {
                    infoReg.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoReg.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferA1, regMinA1);
                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA1, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferA1, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinA1, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoReg.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnB1.setDisable(false);
                        } else {
                            infoReg.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnB1.setDisable(true);
                            regBtnA2.setDisable(true);
                            regBtnB2.setDisable(true);
                            regBtnOk.setDisable(true);

                            iregistrasiB1.setImage(null);
                            iregistrasiA2.setImage(null);
                            iregistrasiB2.setImage(null);
                        }
                    }
                }

            } else {
                infoReg.appendText("Captured A is Failed\n");
            }

        } else {
            infoReg.setText("Please Initialize Device First in Connection Form");
        }

    }

    @FXML
    void regCaptB1(ActionEvent event) {
        infoReg.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegB1 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferB1 = ((java.awt.image.DataBufferByte) imgRegB1.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferB1, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB1, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoReg.appendText("Captured B1\n");

                BufferedImage resized = resize(imgRegB1, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iregistrasiB1.setImage(i);

                if (quality[0] < 50) {
                    infoReg.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoReg.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferB1, regMinB1);
                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB1, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferB1, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinB1, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoReg.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnA2.setDisable(false);
                            registrastionFinger1();
                        } else {
                            infoReg.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnA2.setDisable(true);
                            regBtnB2.setDisable(true);
                            regBtnOk.setDisable(true);

                            iregistrasiA2.setImage(null);
                            iregistrasiB2.setImage(null);

                        }
                    }
                }

            } else {
                infoReg.appendText("Captured A is Failed\n");
            }

        } else {
            infoReg.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void regCaptA2(ActionEvent event) {
        infoReg.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegA2 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferA2 = ((java.awt.image.DataBufferByte) imgRegA2.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferA2, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA2, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoReg.appendText("Captured A2\n");

                BufferedImage resized = resize(imgRegA2, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iregistrasiA2.setImage(i);

                if (quality[0] < 50) {
                    infoReg.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoReg.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferA2, regMinA2);
                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA2, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferA2, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinA2, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoReg.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnB2.setDisable(false);


                        } else {
                            infoReg.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnB2.setDisable(true);
                            regBtnOk.setDisable(true);

                            iregistrasiB2.setImage(null);
                        }
                    }
                }

            } else {
                infoReg.appendText("Captured A is Failed\n");
            }

        } else {
            infoReg.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void regCaptB2(ActionEvent event) {
        infoReg.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegB2 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferB2 = ((java.awt.image.DataBufferByte) imgRegB2.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferB2, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB2, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoReg.appendText("Captured B2\n");

                BufferedImage resized = resize(imgRegB2, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iregistrasiB2.setImage(i);

                if (quality[0] < 50) {
                    infoReg.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoReg.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferB2, regMinB2);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB2, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferB2, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinB2, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoReg.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnOk.setDisable(false);
                            registrastionFinger2();
                        } else {
                            infoReg.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            regBtnOk.setDisable(true);
                        }
                    }
                }

            } else {
                infoReg.appendText("Captured A is Failed\n");
            }

        } else {
            infoReg.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void registrationOk(ActionEvent event) {
        boolean status = false;

        try {
            status = serverRequest.registerFinger(appData.getRecord().getUsername(), regMinA1, regMinA2, imgRegA2, imgRegA1, appData.getToken());
        } catch (IOException e) {
            e.printStackTrace();
            Dialog.errorMessage("Error", "Finger registration failed");
        }

        if (status) {
            infoReg.setText("Register New Finger Print Success");
            btnVrfy.setDisable(false);
            btnReg.setDisable(true);
            btnUpdt.setDisable(false);
        } else {
            infoReg.setText("Register New Finger Print Failed");

        }

    }


    // ===================================================================================================================
    // VERIFY
    // ===================================================================================================================
    @FXML
    void verifyFinger(ActionEvent event) {

        infoCaptA1.setText("Scaning your finger....\n");

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
                    infoCaptA1.appendText("Get Image Success, \nbut image quality is to low \n");
                    infoCaptA1.appendText("Please Try Again!\n");
                } else {
                    infoCaptA1.appendText("Get Image Success\n");
                    infoCaptA1.appendText("Thank You\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferVerify1, regVerify3);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferVerify1, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferVerify1, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regVerify3, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoCaptA1.appendText("Reg. Capture 2 PASS QC. \nQual[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            int[] matchScore = new int[1];
                            boolean[] matched = new boolean[1];
                            long secuLevel = SGFDxSecurityLevel.SL_NORMAL;
                            matched[0] = false;

                            ret = jsgfpLib.MatchTemplate(regVerify3, regVerify1, secuLevel, matched);

                            if (matched[0]) {
                                infoCaptA1.setText("");
                                infoCaptA1.appendText("Verification Success");
                            } else {
                                infoCaptA1.setText("");
                                infoCaptA1.appendText("1st Verification Failed\n");
                                verifyFinger2();
                            }
                        } else {
                            infoCaptA1.setText("");
                            infoCaptA1.appendText("Finger Capture 2 FAIL QC \n");
                            infoCaptA1.appendText("Try Again");
                        }
                    }
                }
            } else {
                infoCaptA1.appendText("Captured A is Failed\n");
            }
        } else {
            infoCaptA1.setText("Please Initialize Device First in Connection Form");
        }
    }


    void verifyFinger2() {

        System.out.println("retry");

        infoCaptA1.appendText("Retry....\nMatching 2nd Finger\n");

        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgVerif2 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferVerify2 = ((java.awt.image.DataBufferByte) imgVerif2.getRaster().getDataBuffer()).getData();

        int[] matchScore = new int[1];
        boolean[] matched = new boolean[1];
        long secuLevel = SGFDxSecurityLevel.SL_NORMAL;
        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(regVerify3, regVerify2, secuLevel, matched);

        if (matched[0]) {
            infoCaptA1.appendText("Verification Success");
        } else {
            infoCaptA1.appendText("Verification Failed");
        }

    }


    void initVerify() {

        System.out.println("finger 1 : " + appData.getFinger().getFinger());
        System.out.println("finger 2 : " + appData.getFinger().getFinger2());
        System.out.println("template 1 : " + appData.getFinger().getTemplate());
        System.out.println("template 2 : " + appData.getFinger().getTemplate2());

        regVerify1 = Base64.getDecoder().decode(appData.getFinger().getTemplate().getBytes());
        regVerify2 = Base64.getDecoder().decode(appData.getFinger().getTemplate2().getBytes());

        fingerVerif1 = appData.getFinger().getFinger();
        imgByteVerif = Base64.getDecoder().decode(fingerVerif1);

        fingerVerif2 = appData.getFinger().getFinger2();
        imgByteVerif2 = Base64.getDecoder().decode(fingerVerif2);

        try {
            imgVerif = ImageIO.read(new ByteArrayInputStream(imgByteVerif));
            imgVerif2 = ImageIO.read(new ByteArrayInputStream(imgByteVerif2));
            System.out.println("image verify : " + imgVerif);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage resized = resize(imgVerif, 200, 154);
        Image image1 = SwingFXUtils.toFXImage(resized, null);
        imageVerifA.setImage(image1);

        BufferedImage resized2 = resize(imgVerif2, 200, 154);
        Image image2 = SwingFXUtils.toFXImage(resized2, null);
        imageVerifB.setImage(image2);

    }

    // ===================================================================================================================
    // UPDATE
    // ===================================================================================================================
    @FXML
    void updCaptA1(ActionEvent event) {
        infoReg.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegA1 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferA1 = ((java.awt.image.DataBufferByte) imgRegA1.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferA1, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA1, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoUpd.appendText("Captured A1\n");

                BufferedImage resized = resize(imgRegA1, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iupdateA1.setImage(i);

                if (quality[0] < 50) {
                    infoUpd.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoUpd.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferA1, regMinA1);
                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA1, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferA1, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinA1, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoUpd.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnB1.setDisable(false);
                        } else {
                            infoReg.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnB1.setDisable(true);
                            updBtnA2.setDisable(true);
                            updBtnB2.setDisable(true);
                            updBtnOk.setDisable(true);

                            iupdateB1.setImage(null);
                            iupdateA2.setImage(null);
                            iupdateB2.setImage(null);
                        }
                    }
                }

            } else {
                infoUpd.appendText("Captured A is Failed\n");
            }

        } else {
            infoUpd.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void updCaptB1(ActionEvent event) {
        infoUpd.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegB1 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferB1 = ((java.awt.image.DataBufferByte) imgRegB1.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferB1, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB1, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoUpd.appendText("Captured B1\n");

                BufferedImage resized = resize(imgRegB1, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iupdateB1.setImage(i);

                if (quality[0] < 50) {
                    infoUpd.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoUpd.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferB1, regMinB1);
                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB1, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferB1, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinB1, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoUpd.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnA2.setDisable(false);
                            updateFinger1();
                        } else {
                            infoUpd.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnA2.setDisable(true);
                            updBtnB2.setDisable(true);
                            updBtnOk.setDisable(true);

                            iupdateA2.setImage(null);
                            iupdateB2.setImage(null);

                        }
                    }
                }

            } else {
                infoUpd.appendText("Captured A is Failed\n");
            }

        } else {
            infoUpd.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void updCaptA2(ActionEvent event) {
        infoUpd.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegA2 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Global Byte Image
        imageBufferA2 = ((java.awt.image.DataBufferByte) imgRegA2.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferA2, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA2, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoUpd.appendText("Captured A2\n");

                BufferedImage resized = resize(imgRegA2, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iupdateA2.setImage(i);

                if (quality[0] < 50) {
                    infoUpd.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoUpd.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferA2, regMinA2);
                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA2, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferA2, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinA2, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoUpd.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnB2.setDisable(false);

                        } else {
                            infoUpd.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnB2.setDisable(true);
                            updBtnOk.setDisable(true);
                            iupdateB2.setImage(null);
                        }
                    }
                }

            } else {
                infoUpd.appendText("Captured A is Failed\n");
            }

        } else {
            infoUpd.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void updCaptB2(ActionEvent event) {
        infoUpd.setText("");
        int[] quality = new int[1];
        long nfiqvalue;
        int[] numOfMinutes = new int[1];

        // Global BufferedImage
        imgRegB2 = new BufferedImage(dvcInfo.imageWidth, dvcInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        // Global Byte Image
        imageBufferB2 = ((java.awt.image.DataBufferByte) imgRegB2.getRaster().getDataBuffer()).getData();

        if (jsgfpLib != null) {
            ret = jsgfpLib.GetImageEx(imageBufferB2, 10 * 1000, 0, 80);
            jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB2, quality);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                infoUpd.appendText("Captured B2\n");

                BufferedImage resized = resize(imgRegB2, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iupdateB2.setImage(i);

                if (quality[0] < 50) {
                    infoUpd.appendText("Get Image Success, but image quality is [" + quality[0] + "]. Please Try Again!\n");
                } else {
                    infoUpd.appendText("Get Image Success, your image quality is [" + quality[0] + "]. Thank You.\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferB2, regMinB2);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB2, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferB2, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinB2, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoUpd.appendText("Reg. Capture 2 PASS. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnOk.setDisable(false);
                            updateFinger2();
                        } else {
                            infoUpd.appendText("Reg. Capture 2 FAIL. Quality [" + quality[0] + "] NFIQ [" + nfiqvalue + "] Minutiae [" + numOfMinutes[0] + "]\n");
                            updBtnOk.setDisable(true);
                        }
                    }
                }

            } else {
                infoUpd.appendText("Captured A is Failed\n");
            }

        } else {
            infoUpd.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void updateOk(ActionEvent event) {

        boolean status = false;

        try {
            status = serverRequest.updateFinger(appData.getRecord().getUsername(), regMinA1, regMinA2, imgRegA2, imgRegA1, appData.getToken());
        } catch (IOException e) {
            e.printStackTrace();
            Dialog.errorMessage("Error", "Finger update failed");
        }

        if (status) {
            infoUpd.setText("Update Finger Print Success");
        } else {
            infoUpd.setText("Update Finger Print Failed");

        }

    }


    // ===================================================================================================================
    // FUNCTION OR SERVICE
    // ===================================================================================================================

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
//        System.out.println(ret);
        if (ret == 6) {
            Dialog.errorMessage("Login Error", "Finger Scan Device Not Detected");
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

//            System.out.println(ret);
//            System.out.println(SGFDxErrorCode.SGFDX_ERROR_NONE);

            if (ret != SGFDxErrorCode.SGFDX_ERROR_NONE) {
                displayCon.appendText("Failed Open Device \n");
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
                    displayCon.appendText("Get Device Info Failed : " + ret + "\n");
                }
            }

        }

    }

    public void registrastionFinger1() {
        int[] matchScore = new int[1];
        boolean[] matched = new boolean[1];
        long secuLevel = (long) (8);
        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(regMinA1, regMinB1, secuLevel, matched);
//        System.out.println(ret);

        if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            matchScore[0] = 0;
            ret = jsgfpLib.GetMatchingScore(regMinA1, regMinB1, matchScore);

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {

                if (matched[0]) {
                    infoFinger1.setText("Success: " + matchScore[0]);
                } else {
                    infoFinger1.setText("Fail: " + matchScore[0]);
                }

            } else {
                infoReg.appendText("Registration Fail, GetMatchingScore() Error : " + ret + "\n");
            }
        } else {
            infoReg.appendText("Registration Fail, MatchTemplate() Error : " + ret + "\n");
        }
    }

    public void registrastionFinger2() {
        int[] matchScore = new int[1];
        boolean[] matched = new boolean[1];
        long secuLevel = (long) (8);
        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(regMinA2, regMinB2, secuLevel, matched);
//        System.out.println(ret);

        if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            matchScore[0] = 0;
            ret = jsgfpLib.GetMatchingScore(regMinA2, regMinB2, matchScore);

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {

                if (matched[0]) {
                    infoFinger2.setText("Success: " + matchScore[0]);
                } else {
                    infoFinger2.setText("Fail: " + matchScore[0]);
                }

            } else {
                infoReg.appendText("Registration Fail, GetMatchingScore() Error : " + ret + "\n");
            }
        } else {
            infoReg.appendText("Registration Fail, MatchTemplate() Error : " + ret + "\n");
        }
    }

    public void updateFinger1() {
        int[] matchScore = new int[1];
        boolean[] matched = new boolean[1];
        long secuLevel = (long) (8);
        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(regMinA1, regMinB1, secuLevel, matched);
//        System.out.println(ret);

        if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            matchScore[0] = 0;
            ret = jsgfpLib.GetMatchingScore(regMinA1, regMinB1, matchScore);
            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                if (matched[0]) {
                    infoLabelA.setText("Success: " + matchScore[0]);
                } else {
                    infoLabelA.setText("Fail: " + matchScore[0]);
                }
            } else {
                infoUpd.appendText("Registration Fail, GetMatchingScore() Error : " + ret + "\n");
            }
        } else {
            infoUpd.appendText("Registration Fail, MatchTemplate() Error : " + ret + "\n");
        }
    }

    public void updateFinger2() {
        int[] matchScore = new int[1];
        boolean[] matched = new boolean[1];
        long secuLevel = (long) (8);
        matched[0] = false;

        ret = jsgfpLib.MatchTemplate(regMinA2, regMinB2, secuLevel, matched);
//        System.out.println(ret);

        if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            matchScore[0] = 0;
            ret = jsgfpLib.GetMatchingScore(regMinA2, regMinB2, matchScore);

            if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {

                if (matched[0]) {
                    infoLabelB.setText("Success: " + matchScore[0]);
                } else {
                    infoLabelB.setText("Fail: " + matchScore[0]);
                }

            } else {
                infoUpd.appendText("Registration Fail, GetMatchingScore() Error : " + ret + "\n");
            }
        } else {
            infoUpd.appendText("Registration Fail, MatchTemplate() Error : " + ret + "\n");
        }
    }

}
