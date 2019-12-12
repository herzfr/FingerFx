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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
//    byte[] imageBufferC;

    private BufferedImage imgRegA;
    private BufferedImage imgRegB;

    private byte[] regMinA = new byte[400];
    private byte[] regMinB = new byte[400];

    //verification tab properties

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

//    verification tab properties


    AppData appData;

    //server request service
    ServerRequest serverRequest = new ServerRequest();

    @FXML
    private Pane pnlVerify;
    @FXML
    private Pane pnlReg;
    @FXML
    private Pane pnlConn;
    @FXML
    private Pane pnlUpd;
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
    private Button btnVerify;
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
    private Button btnUpdt;
    @FXML
    private Label lblAccount;
    @FXML
    private ImageView imgView;
    @FXML
    private ImageView imageVerifA;
    @FXML
    private ImageView imageVerifB;
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
    private TextArea infoUpdA;
    @FXML
    private AnchorPane frameUpdtA;
    @FXML
    private ImageView iupdateA;
    @FXML
    private TextArea infoUpdB;
    @FXML
    private TextArea infoCaptA1;
    @FXML
    private AnchorPane frameUpdtB;
    @FXML
    private ImageView iupdateB;
    @FXML
    private TextField infoResultUpd;
    @FXML
    private Button updBtnOk;
    @FXML
    private Button capUpdBtnA;
    @FXML
    private Button capUpdBtnB;
    @FXML
    private Label infoLabelA;
    @FXML
    private Label infoLabelB;


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

        Boolean status = serverRequest.getFinger(appData.getRecord().getUsername(), appData.getToken());
        System.out.println(appData.getFinger().getTemplate());

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
    void onUpdate(ActionEvent event) {
        conLog.setStyle("-fx-fill: #fff;");
        keyLog.setStyle("-fx-fill: #fff;");
        regLog.setStyle("-fx-fill: #fff;");
        clsLog.setStyle("-fx-fill: #2bc344;");

        pnlUpd.toFront();

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
        infoCaptA.setEditable(false);
        infoCaptB.setEditable(false);
        infoResultReg.setEditable(false);
        infoUpdA.setEditable(false);
        infoUpdB.setEditable(false);


        initialize();
        initVerify();

    }


    public void initVerify() {


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



    @FXML
    void regCaptA(ActionEvent event) {
        infoCaptA.setText("");
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
//                            infoCaptA.appendText("template : " + new String(regMinA) + "\n");
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
//                            infoCaptB.appendText("template : " + new String(regMinB) + "\n");
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

        long secuLevel = SGFDxSecurityLevel.SL_NORMAL;
        boolean[] matched = new boolean[1];
        ret = jsgfpLib.MatchTemplate(regMinA, regMinB, secuLevel, matched);

//        Boolean status = serverRequest.updateFinger(appData.getRecord().getUsername(), regMinA, appData.getToken(), imgRegA);
//        if (status) {
//            infoResultReg.setText("Operation Done\n");
//        } else {
//            infoResultReg.setText("Operation Failed\n");
//        }

    }


    @FXML
    void updCaptA(ActionEvent event) {
        infoUpdA.setText("");
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
                infoUpdA.appendText("Captured\n");

                BufferedImage resized = resize(imgRegA, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iupdateA.setImage(i);

//                System.out.println(quality[0]);
                if (quality[0] < 50) {
                    infoUpdA.appendText("Get Image Success, \nbut image quality is => " + quality[0] + "\n");
                    infoUpdA.appendText("Please Try Again!\n");
                } else {
                    infoUpdA.appendText("Get Image Success, \nyour image quality is => " + quality[0] + "\n");
                    infoUpdA.appendText("Thank You\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferA, regMinA);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferA, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferA, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinA, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoUpdA.appendText("Reg. Capture 2 PASS QC. \nQual[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            infoLabelA.setText("SUCCESS");
                            infoLabelA.setStyle("-fx-text-fill: #23cf87");
                        } else {
                            infoUpdA.appendText("Reg. Capture 2 FAIL QC. \nQuality[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            infoLabelA.setText("FAILED");
                            infoLabelA.setStyle("-fx-text-fill: #b51967");
                        }
                    }
                }

            } else {
                infoUpdA.appendText("Captured A is Failed\n");
            }

        } else {
            infoUpdA.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void updCaptB(ActionEvent event) {
        infoUpdB.setText("");
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
                infoUpdB.appendText("Captured\n");

                BufferedImage resized = resize(imgRegB, 200, 154);
                Image i = SwingFXUtils.toFXImage(resized, null);
                iupdateB.setImage(i);

//                System.out.println(quality[0]);
                if (quality[0] < 50) {
                    infoUpdB.appendText("Get Image Success, \nbut image quality is => " + quality[0] + "\n");
                    infoUpdB.appendText("Please Try Again!\n");
                } else {
                    infoUpdB.appendText("Get Image Success, \nyour image quality is => " + quality[0] + "\n");
                    infoUpdB.appendText("Thank You\n");

                    ret = jsgfpLib.CreateTemplate(fingerInfo, imageBufferB, regMinB);

                    if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        long ret2 = jsgfpLib.GetImageQuality(dvcInfo.imageWidth, dvcInfo.imageHeight, imageBufferB, quality);
                        nfiqvalue = jsgfpLib.ComputeNFIQ(imageBufferB, dvcInfo.imageWidth, dvcInfo.imageHeight);
                        ret2 = jsgfpLib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, regMinB, numOfMinutes);
                        if ((quality[0] >= 50) && (nfiqvalue <= 2) && (numOfMinutes[0] >= 20)) {
                            infoUpdB.appendText("Reg. Capture 2 PASS QC. \nQual[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            infoLabelB.setText("SUCCESS");
                            infoLabelB.setStyle("-fx-text-fill: #23cf87");
                        } else {
                            infoUpdB.appendText("Reg. Capture 2 FAIL QC. \nQuality[" + quality[0] + "] \nNFIQ[" + nfiqvalue + "] \nMinutiae[" + numOfMinutes[0] + "]\n");
                            infoLabelA.setText("FAILED");
                            infoLabelA.setStyle("-fx-text-fill: #b51967");
                        }
                    }
                }

            } else {
                infoUpdB.appendText("Captured A is Failed\n");
            }

        } else {
            infoUpdB.setText("Please Initialize Device First in Connection Form");
        }
    }

    @FXML
    void updateOk(ActionEvent event) {
//        infoUpdB.setText("upd ok");

        String imageA = null;
        String imageB = null;

        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        ByteArrayOutputStream bAOS2 = new ByteArrayOutputStream();
        try {
            ImageIO.write(imgRegA, "png", bAOS);
            ImageIO.write(imgRegB, "png", bAOS2);
            byte[] imageByteA = bAOS.toByteArray();
            byte[] imageByteB = bAOS2.toByteArray();
            imageA = Base64.getEncoder().encodeToString(imageByteA);
            imageB = Base64.getEncoder().encodeToString(imageByteB);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
