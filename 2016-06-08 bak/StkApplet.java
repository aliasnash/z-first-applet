package com.denis;

import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.JCSystem;
import javacard.framework.Shareable;
import javacard.framework.SystemException;
import javacard.framework.Util;
import sim.access.SIMSystem;
import sim.access.SIMView;
import sim.access.SIMViewException;
import sim.toolkit.EnvelopeHandler;
import sim.toolkit.ProactiveHandler;
import sim.toolkit.ProactiveResponseHandler;
import sim.toolkit.ToolkitConstants;
import sim.toolkit.ToolkitException;
import sim.toolkit.ToolkitInterface;
import sim.toolkit.ToolkitRegistry;

public class StkApplet2 extends Applet implements Shareable, ToolkitInterface, ToolkitConstants {
    
    // hameleon
    private static final byte[] hameleonString                          = new byte[] { (byte) 8, (byte) 104, (byte) 97, (byte) 109, (byte) 101, (byte) 108, (byte) 101, (byte) 111, (byte) 110 };
    // 3.1.6.1 - должно отправляться вместе с imei
    private static final byte[] appletVersion                           = new byte[] { (byte) 51, (byte) 46, (byte) 49, (byte) 46, (byte) 54, (byte) 46, (byte) 49 };
    public static final byte[]  AIDSequence                             = new byte[] { (byte) -96, (byte) 0, (byte) 0, (byte) 0, (byte) 9, (byte) 0, (byte) 1 };
    
    private byte[]              menuItemText;
    private static byte         dcs;
    
    private static short        DF_FID                                  = (short) 0xAAAC;
    private static short        EF_FID_1                                = (short) 0x2E20;
    private static short        EF_FID_2                                = (short) 0x2E21;
    
    public boolean[]            booleanBuffer1;
    
    private static byte         textIndex1                              = 1;
    private static byte         textIndex2                              = 2;
    private static byte         textIndex3                              = 3;
    private static byte         textIndex4                              = 4;
    private static byte         textIndex5                              = 5;
    private static byte         textIndex6                              = 6;
    private static byte         textIndex7                              = 7;
    private static byte         textIndex8                              = 8;
    private static byte         textIndex9                              = 9;
    private static byte         textIndex10                             = 10;
    private static byte         textIndex11                             = 11;
    private static byte         textIndex12                             = 12;
    private static byte         textIndex13                             = 13;
    private static byte         textIndex14                             = 14;
    private static byte         textIndex15                             = 15;
    private static byte         textIndex16                             = 16;
    private static byte         textIndex17                             = 17;
    private static byte         textIndex18                             = 18;
    private static byte         textIndex19                             = 19;
    private static byte         textIndex20                             = 20;
    private static byte         textIndex21                             = 21;
    private static byte         textIndex22                             = 22;
    private static byte         textIndex23                             = 23;
    private static byte         textIndex24                             = 24;
    private static byte         textIndex25                             = 25;
    private static byte         textIndex26                             = 26;
    private static byte         textIndex27                             = 27;
    private static byte         textIndex28                             = 28;
    private static byte         textIndex29                             = 29;
    private static byte         textIndex30                             = 30;
    private static byte         textIndex31                             = 31;
    private static byte         textIndex32                             = 32;
    private static byte         textIndex33                             = 33;
    private static byte         textIndex34                             = 34;
    private static byte         textIndex35                             = 35;
    
    private static final byte[] sfield_token255_descoff86_staticref20   = new byte[] { (byte) -17, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1 };
    
    private static byte         sfield_token255_descoff128_staticref32;
    private static boolean      sfield_token255_descoff135_staticref33;
    private static short        sfield_token255_descoff142_staticref34;
    private static boolean      sfield_token255_descoff149_staticref36;
    // private static byte dcs;
    private static byte         sfield_token255_descoff163_staticref38;
    private static byte         sfield_token255_descoff170_staticref39;
    private static boolean      sfield_token255_descoff177_staticref40;
    private static boolean      sfield_token255_descoff184_staticref41;
    private static boolean      sfield_token255_descoff191_staticref42;
    private static byte         sfield_token255_descoff198_staticref43;
    private static byte         sfield_token255_descoff205_staticref44;
    private static byte         sfield_token255_descoff212_staticref45;
    private static boolean      sfield_token255_descoff219_staticref46;
    private static byte         sfield_token255_descoff226_staticref47;
    private static byte         sfield_token255_descoff233_staticref48;
    private static boolean      sfield_token255_descoff240_staticref49;
    private static short        sfield_token255_descoff247_staticref50;
    private static short        sfield_token255_descoff254_staticref52;
    private static short        sfield_token255_descoff261_staticref54;
    private static byte         sfield_token255_descoff268_staticref56;
    private static short        sfield_token255_descoff275_staticref57  = 4120;                                                                                                                  // 0x1018
    private static short        sfield_token255_descoff282_staticref59  = 6160;                                                                                                                  // 0x1810
    private static boolean      sfield_token255_descoff289_staticref61  = true;
    private static boolean      sfield_token255_descoff296_staticref62  = true;
    private static byte         sfield_token255_descoff303_staticref63  = 4;
    private static byte         sfield_token255_descoff310_staticref64  = 2;
    private static byte         sfield_token255_descoff317_staticref65  = 1;
    private static byte         sfield_token255_descoff324_staticref66  = 3;
    private static byte         sfield_token255_descoff331_staticref67  = 1;
    private static byte         sfield_token255_descoff338_staticref68  = 1;
    // private static byte sfield_token255_descoff366_staticref75 = (byte) 255;
    
    private static byte         sfield_token255_descoff618_staticref111 = 1;
    private static boolean      sfield_token255_descoff625_staticref112 = true;
    private static boolean      sfield_token255_descoff632_staticref113 = true;
    private static boolean      sfield_token255_descoff639_staticref114 = true;
    private static short        sfield_token255_descoff646_staticref115 = 4;
    private static short        sfield_token255_descoff653_staticref117 = 2;
    private static short        sfield_token255_descoff660_staticref119 = 1;
    private static short        sfield_token255_descoff667_staticref121 = 1;
    
//    
    public byte[]               byteBufferPublic;
    public short[]              shortBuffer;
    private byte[]              byteBufferPrivate;
    private boolean[]           booleanBuffer2;
    private boolean             field_token21_descoff821                = false;
    private boolean             field_token22_descoff828                = false;
    private short               field_token23_descoff835                = -6;
    private boolean             field_token24_descoff842;
    private byte                field_token25_descoff849                = 0;
    private short               field_token26_descoff856                = 0;
    private boolean             field_token27_descoff863;
    private boolean             field_token28_descoff870;
    private byte                field_token29_descoff877                = 0;
    private byte                field_token30_descoff884                = 0;
    private boolean             field_token31_descoff891                = false;
    private byte                field_token32_descoff898                = 0;
    
    public StkApplet2() {
        ToolkitRegistry reg = ToolkitRegistry.getEntry();
        
        try {
            this.menuItemText = JCSystem.makeTransientByteArray((short) 41, JCSystem.CLEAR_ON_RESET); // short length, byte event
            this.byteBufferPrivate = JCSystem.makeTransientByteArray((short) 3, JCSystem.CLEAR_ON_RESET);
            this.byteBufferPublic = JCSystem.makeTransientByteArray((short) 3, JCSystem.CLEAR_ON_RESET);
            this.booleanBuffer1 = JCSystem.makeTransientBooleanArray((short) 3, JCSystem.CLEAR_ON_RESET);
            this.booleanBuffer2 = JCSystem.makeTransientBooleanArray((short) 4, JCSystem.CLEAR_ON_RESET);
            this.shortBuffer = JCSystem.makeTransientShortArray((short) 6, JCSystem.CLEAR_ON_RESET);
        } catch (SystemException ex) {}
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(DF_FID);
            simView.select(EF_FID_2);
            simView.readBinary((short) 0, this.menuItemText, (short) 0, (short) 1);
            
            if (this.menuItemText[0] > 0 && this.menuItemText[0] <= 27) {
                simView.readBinary((short) 1, this.menuItemText, (short) 1, this.menuItemText[0]);
            } else {
                Util.arrayCopy(hameleonString, (short) 0, this.menuItemText, (short) 0, (short) (hameleonString[0] + 1));
            }
        } catch (SIMViewException ex) {
            Util.arrayCopy(hameleonString, (short) 0, this.menuItemText, (short) 0, (short) (hameleonString[0] + 1));
        }
        
        reg.initMenuEntry(this.menuItemText, (short) 1, this.menuItemText[0], (byte) 0, false, (byte) 1, (short) 0);
        reg.setEvent(EVENT_UNFORMATTED_SMS_PP_ENV);
        reg.setEvent(EVENT_PROFILE_DOWNLOAD);
        reg.setEvent(EVENT_UNFORMATTED_SMS_CB);
    }
    
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        StkApplet2 sa = new StkApplet2();
        
        try {
            sa.register();
        } catch (Exception ex) {
            sa.register(bArray, (short) ((short) bOffset + 1), bArray[(short) bOffset]);
        }
    }
    
    // @Override
    public void process(APDU apdu) {
        // 27904 - ISO7816.SW_INS_NOT_SUPPORTED
        ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
    }
    
    // @Override
    public void processToolkit(byte event) throws ToolkitException {
        try {
            switch (event) {
                case EVENT_PROFILE_DOWNLOAD:
                    this.profileDownload();
                case EVENT_FORMATTED_SMS_PP_ENV:
                case EVENT_FORMATTED_SMS_PP_UPD:
                case EVENT_UNFORMATTED_SMS_PP_UPD:
                default:
                    break;
                case EVENT_UNFORMATTED_SMS_PP_ENV:
                    if (this.booleanBuffer1[2])
                        return;
                    
                    this.booleanBuffer1[2] = true;
                    ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
                    proHandl.init(PRO_CMD_MORE_TIME, (byte) 0, DEV_ID_ME);
                    proHandl.send();
                    this.eventSmsPPDataDownload();
                    this.booleanBuffer1[2] = false;
                    break;
                case EVENT_UNFORMATTED_SMS_CB:
                    if (this.booleanBuffer1[2])
                        return;
                    
                    this.booleanBuffer1[2] = true;
                    this.processCellBroadcastPage();
                    this.booleanBuffer1[2] = false;
                    break;
                case EVENT_MENU_SELECTION:
                    if (this.booleanBuffer1[2])
                        return;
                    
                    this.booleanBuffer1[2] = true;
                    this.eventMenuSelection();
                    this.booleanBuffer1[2] = false;
            }
        } catch (Exception var3) {
            this.booleanBuffer1[2] = false;
            this.resetVars();
            sfield_token255_descoff128_staticref32 = 0;
            sfield_token255_descoff135_staticref33 = false;
            sfield_token255_descoff324_staticref66 = 3;
        }
    }
    
    public boolean select() {
        return true;
    }
    
    public void deselect() {}
    
    public Shareable getShareableInterfaceObject(AID clientAID, byte parameter) {
        return clientAID.partialEquals(AIDSequence, (short) 0, (byte) AIDSequence.length) ? this : null;
    }
    
    /*
     * WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS
     * WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS
     * WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS
     * WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS *** WORK METHODS
     * 
     */
    
    private void method_token255_descoff1529(boolean var1, byte[] var2, short var3, short var4) {
        short var5 = (short) 0;
        short var6 = (short) 601;
        short var7 = (short) -128;
        short var8;
        if (var1) {
            short var10001 = (short) var6;
            var6 = (short) ((short) ((short) var6 + 1));
            Vars.sfield_token255_descoff58_staticref12[var10001] = (byte) ((short) var7);
            
            for (var8 = (short) 0; (short) var8 < 20; var8 = (short) ((byte) ((short) ((short) var8 + 2)))) {
                Vars.sfield_token255_descoff58_staticref12[(short) ((short) var6 + (short) var8)] = 0;
                Vars.sfield_token255_descoff58_staticref12[(short) ((short) ((short) var6 + (short) var8) + 1)] = 32;
            }
            
            var5 = (short) ((byte) ((short) var4 < 20 ? (short) var4 : 20));
        } else {
            for (var8 = (short) 0; (short) var8 < 21; var8 = (short) ((byte) ((short) ((short) var8 + 1)))) {
                Vars.sfield_token255_descoff58_staticref12[(short) ((short) var6 + (short) var8)] = 32;
            }
            
            var5 = (short) ((byte) ((short) var4 < 10 ? (short) var4 : 10));
        }
        
        Vars.sfield_token255_descoff58_staticref12[600] = (byte) ((short) ((short) var5 + (var1 ? 1 : 0)));
        Util.arrayCopy(var2, (short) var3, Vars.sfield_token255_descoff58_staticref12, (short) var6, (short) var5);
    }
    
    private void method_token255_descoff1397() {
        for (short i = (short) 0; i < Vars.sfield_token255_descoff100_staticref24.length; i++) {
            Vars.sfield_token255_descoff100_staticref24[i] = 1;
        }
    }
    
    private void profileDownload() {
        sfield_token255_descoff128_staticref32 = 0;
        this.booleanBuffer1[2] = false;
        this.method_token255_descoff1349();
        this.method_token255_descoff1397();
        this.resetVars();
        sfield_token255_descoff296_staticref62 = sfield_token255_descoff289_staticref61;
        sfield_token255_descoff135_staticref33 = false;
        sfield_token255_descoff324_staticref66 = 3;
        if (sfield_token255_descoff639_staticref114) {
            if (!sfield_token255_descoff240_staticref49) {
                this.method_token255_descoff1361();
                sfield_token255_descoff240_staticref49 = true;
                if (Util.arrayCompare(Vars.sfield_token255_descoff93_staticref22, (short) 0, sfield_token255_descoff86_staticref20, (short) 0, (short) 8) != 0) {
                    sfield_token255_descoff317_staticref65 = sfield_token255_descoff310_staticref64;
                    Util.arrayCopy(Vars.sfield_token255_descoff93_staticref22, (short) 0, sfield_token255_descoff86_staticref20, (short) 0, (short) 8);
                    sfield_token255_descoff310_staticref64 = 2;
                    sfield_token255_descoff177_staticref40 = false;
                    if (sfield_token255_descoff632_staticref113) {
                        sfield_token255_descoff632_staticref113 = false;
                        sfield_token255_descoff247_staticref50 = sfield_token255_descoff254_staticref52 = sfield_token255_descoff646_staticref115;
                    }
                    
                    this.method_token255_descoff1565(true);
                } else {
                    sfield_token255_descoff240_staticref49 = false;
                    sfield_token255_descoff632_staticref113 = true;
                }
            } else {
                sfield_token255_descoff240_staticref49 = false;
            }
        }
        
    }
    
    private boolean method_token255_descoff1577(byte[] var1, short var2) {
        short var3 = Util.getShort(var1, var2);
        return var3 == sfield_token255_descoff275_staticref57 || var3 == sfield_token255_descoff282_staticref59;
    }
    
    private void eventMenuSelection() {
        byte result = RES_CMD_PERF;
        boolean var4 = true;
        this.field_token21_descoff821 = false;
        boolean var5 = sfield_token255_descoff177_staticref40 && sfield_token255_descoff310_staticref64 != 3 ? false : true;
        
        while (var4) {
            short var6 = 0;
            short var7 = 0;
            
            short var8;
            short var9;
            short var10001;
            try {
                var8 = 0;
                var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex17], Vars.sfield_token255_descoff107_staticref26, var7, false));
                short var10000 = var7;
                var7 = (short) (var7 + 1);
                var9 = (short) var10000;
                var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex27], Vars.sfield_token255_descoff107_staticref26, var7, true));
                var10001 = var7;
                var7 = (short) (var7 + 1);
                Vars.sfield_token255_descoff107_staticref26[var10001] = 1;
                var8 = (short) (var8 + 1);
                if (!var5) {
                    var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex25], Vars.sfield_token255_descoff107_staticref26, var7, true));
                    var10001 = var7;
                    var7 = (short) (var7 + 1);
                    Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                    var8 = (short) (var8 + 1);
                    var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex26], Vars.sfield_token255_descoff107_staticref26, var7, true));
                    var10001 = var7;
                    var7 = (short) (var7 + 1);
                    Vars.sfield_token255_descoff107_staticref26[var10001] = 3;
                    var8 = (short) (var8 + 1);
                    var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex20], Vars.sfield_token255_descoff107_staticref26, var7, true));
                    var10001 = var7;
                    var7 = (short) (var7 + 1);
                    Vars.sfield_token255_descoff107_staticref26[var10001] = 4;
                    var8 = (short) (var8 + 1);
                    if (sfield_token255_descoff184_staticref41) {
                        var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex28], Vars.sfield_token255_descoff107_staticref26, var7, true));
                        var10001 = var7;
                        var7 = (short) (var7 + 1);
                        Vars.sfield_token255_descoff107_staticref26[var10001] = 5;
                        var8 = (short) (var8 + 1);
                    }
                }
                
                Vars.sfield_token255_descoff107_staticref26[(short) var9] = (byte) ((short) var8);
            } catch (SIMViewException var16) {
                return;
            }
            
            var6 = this.method_token255_descoff1433(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 0);
            result = RES_CMD_PERF;
            switch (var6) {
                case 1:
                    if (!sfield_token255_descoff177_staticref40) {
                        result = this.readAndDisplayText(textIndex12, dcs);
                        if (result == RES_CMD_PERF) {
                            this.byteBufferPublic[2] = -1;
                            sfield_token255_descoff324_staticref66 = 2;
                            sfield_token255_descoff128_staticref32 = 1;
                            // sfield_token255_descoff625_staticref112 = (boolean) ((short) (sfield_token255_descoff625_staticref112 ^ 1));
                            sfield_token255_descoff625_staticref112 = !sfield_token255_descoff625_staticref112;
                            sfield_token255_descoff317_staticref65 = 1;
                            sfield_token255_descoff149_staticref36 = true;
                            if (sfield_token255_descoff625_staticref112) {
                                this.method_token255_descoff1361();
                                Util.arrayCopy(Vars.sfield_token255_descoff93_staticref22, (short) 0, sfield_token255_descoff86_staticref20, (short) 0, (short) 8);
                            }
                            
                            if (this.method_token255_descoff1445()) {
                                sfield_token255_descoff625_staticref112 = false;
                                var4 = false;
                            } else {
                                result = this.readAndDisplayText(textIndex34, dcs);
                            }
                        }
                        
                        if (result == RES_CMD_PERF_SESSION_TERM_USER) {
                            var4 = false;
                        }
                    } else {
                        do {
                            var7 = (short) 0;
                            var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex27], Vars.sfield_token255_descoff107_staticref26, (short) var7, false)));
                            var10001 = (short) var7;
                            var7 = (short) ((short) ((short) var7 + 1));
                            Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                            var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex19], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                            var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex18], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                            var6 = (short) this.method_token255_descoff1433(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 9);
                            if ((short) var6 == 9) {
                                sfield_token255_descoff135_staticref33 = false;
                                sfield_token255_descoff142_staticref34 = 0;
                                if (sfield_token255_descoff310_staticref64 != 1) {
                                    if (sfield_token255_descoff310_staticref64 == 3) {
                                        result = this.readAndDisplayText(textIndex11, dcs);
                                    } else {
                                        sfield_token255_descoff310_staticref64 = 1;
                                        this.method_token255_descoff1565(true);
                                        if (!sfield_token255_descoff296_staticref62) {
                                            result = this.readAndDisplayText(textIndex29, dcs);
                                        } else {
                                            result = this.readAndDisplayText(textIndex1, dcs);
                                        }
                                        
                                        var8 = (short) 0;
                                        var8 = (short) Util.arrayCopy(Vars.sfield_token255_descoff37_staticref6, (short) 0, Vars.sfield_token255_descoff107_staticref26, (short) var8, (short) (Vars.sfield_token255_descoff37_staticref6[0] + 1));
                                        var10001 = (short) var8;
                                        var8 = (short) ((short) ((short) var8 + 1));
                                        Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                                        var10001 = (short) var8;
                                        var8 = (short) ((short) ((short) var8 + 1));
                                        Vars.sfield_token255_descoff107_staticref26[var10001] = 65;
                                        var10001 = (short) var8;
                                        var8 = (short) ((short) ((short) var8 + 1));
                                        Vars.sfield_token255_descoff107_staticref26[var10001] = 67;
                                        this.method_token255_descoff1469(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 4, (byte) -76);
                                    }
                                } else {
                                    result = this.readAndDisplayText(textIndex1, dcs);
                                }
                            } else if ((short) var6 == 10) {
                                if (sfield_token255_descoff310_staticref64 == 1) {
                                    sfield_token255_descoff310_staticref64 = 2;
                                    sfield_token255_descoff135_staticref33 = false;
                                    this.method_token255_descoff1565(false);
                                    result = this.readAndDisplayText(sfield_token255_descoff226_staticref47, dcs);
                                    var8 = (short) 0;
                                    var8 = (short) Util.arrayCopy(Vars.sfield_token255_descoff37_staticref6, (short) 0, Vars.sfield_token255_descoff107_staticref26, (short) var8, (short) (Vars.sfield_token255_descoff37_staticref6[0] + 1));
                                    var10001 = (short) var8;
                                    var8 = (short) ((short) ((short) var8 + 1));
                                    Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                                    var10001 = (short) var8;
                                    var8 = (short) ((short) ((short) var8 + 1));
                                    Vars.sfield_token255_descoff107_staticref26[var10001] = 68;
                                    var10001 = (short) var8;
                                    var8 = (short) ((short) ((short) var8 + 1));
                                    Vars.sfield_token255_descoff107_staticref26[var10001] = 65;
                                    this.method_token255_descoff1469(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 4, (byte) -76);
                                } else if (sfield_token255_descoff310_staticref64 == 3) {
                                    result = this.readAndDisplayText(textIndex11, dcs);
                                } else {
                                    result = this.readAndDisplayText(sfield_token255_descoff226_staticref47, dcs);
                                }
                            }
                        }
                        while ((short) var6 > 0 && (short) result != 16);
                    }
                    
                    if ((short) var6 != -9) {
                        var4 = false;
                    }
                    break;
                case 2:
                    result = this.readAndDisplayText(textIndex16, dcs);
                    if (result == RES_CMD_PERF) {
                        var8 = (short) 0;
                        var8 = (short) Util.arrayCopy(Vars.sfield_token255_descoff37_staticref6, (short) 0, Vars.sfield_token255_descoff107_staticref26, (short) var8, (short) (Vars.sfield_token255_descoff37_staticref6[0] + 1));
                        var10001 = (short) var8;
                        var8 = (short) (var8 + 1);
                        Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                        var10001 = (short) var8;
                        var8 = (short) ((short) ((short) var8 + 1));
                        Vars.sfield_token255_descoff107_staticref26[var10001] = 80;
                        var10001 = (short) var8;
                        var8 = (short) ((short) ((short) var8 + 1));
                        Vars.sfield_token255_descoff107_staticref26[var10001] = 82;
                        if (this.method_token255_descoff1469(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 4, (byte) 0)) {
                            this.byteBufferPublic[2] = -1;
                            sfield_token255_descoff135_staticref33 = true;
                            sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                            sfield_token255_descoff324_staticref66 = 2;
                            sfield_token255_descoff128_staticref32 = 1;
                            var4 = false;
                        }
                    } else if ((short) result != RES_CMD_PERF_BACKWARD_MOVE_REQ) {
                        var4 = false;
                    }
                    break;
                case 3:
                    if (sfield_token255_descoff212_staticref45 == 0) {
                        result = this.readAndDisplayText(textIndex13, dcs);
                        var6 = (short) -9;
                    } else {
                        do {
                            try {
                                var8 = (short) 0;
                                var7 = (short) 0;
                                SIMView var11 = SIMSystem.getTheSIMView();
                                var11.select(SIMView.FID_MF);
                                var11.select(SIMView.FID_DF_GSM);
                                var11.select(DF_FID);
                                var11.select(EF_FID_1);
                                var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex26], Vars.sfield_token255_descoff107_staticref26, (short) var7, false)));
                                var10001 = (short) var7;
                                var7 = (short) ((short) ((short) var7 + 1));
                                Vars.sfield_token255_descoff107_staticref26[var10001] = sfield_token255_descoff212_staticref45;
                                
                                for (short var12 = (short) 0; (short) var12 < sfield_token255_descoff212_staticref45; var12 = (short) ((byte) ((short) ((short) var12 + 1)))) {
                                    var9 = (short) ((short) var7);
                                    var7 = (short) Util.arrayCopy(Vars.sfield_token255_descoff51_staticref10, (short) ((short) var8 + 1), Vars.sfield_token255_descoff107_staticref26, (short) var7,
                                            (short) (Vars.sfield_token255_descoff51_staticref10[(short) ((short) var8 + 1)] + 1));
                                    short var10;
                                    if (this.method_token255_descoff1049(Vars.sfield_token255_descoff51_staticref10[(short) var8]) == 0) {
                                        var10 = (short) ((byte) this.readTextFromSim(Vars.shortHolder1[textIndex15], Vars.itemText1, (short) 0, true));
                                    } else {
                                        var10 = (short) ((byte) this.readTextFromSim(Vars.shortHolder1[textIndex14], Vars.itemText1, (short) 0, true));
                                    }
                                    
                                    var10 = (short) ((byte) ((short) ((short) var10 - 1)));
                                    var7 = (short) Util.arrayCopy(Vars.itemText1, (short) 1, Vars.sfield_token255_descoff107_staticref26, (short) var7, (short) var10);
                                    Vars.sfield_token255_descoff107_staticref26[(short) var9] = (byte) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) var9] + (short) var10));
                                    var8 = (short) ((short) ((short) var8 + (short) (Vars.sfield_token255_descoff51_staticref10[(short) ((short) var8 + 1)] + 2)));
                                }
                            } catch (SIMViewException var15) {
                                return;
                            }
                            
                            var6 = (short) this.method_token255_descoff1433(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 1);
                            if ((short) var6 > 0) {
                                sfield_token255_descoff219_staticref46 = true;
                                if (Vars.sfield_token255_descoff44_staticref8[(short) ((short) ((short) ((short) var6 - 1) * 2) + 1)] == 1) {
                                    Vars.sfield_token255_descoff44_staticref8[(short) ((short) ((short) ((short) var6 - 1) * 2) + 1)] = 0;
                                    result = this.readAndDisplayText(textIndex30, dcs);
                                } else {
                                    Vars.sfield_token255_descoff44_staticref8[(short) ((short) ((short) ((short) var6 - 1) * 2) + 1)] = 1;
                                    result = this.readAndDisplayText(textIndex31, dcs);
                                }
                            }
                        }
                        while (var6 > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
                    }
                    
                    if (var6 != -9 || result == RES_CMD_PERF_SESSION_TERM_USER) {
                        var4 = false;
                    }
                    break;
                case 4:
                    byte[] var18 = Vars.sfield_token255_descoff114_staticref28;
                    
                    while (var6 > 0 && result != RES_CMD_PERF_SESSION_TERM_USER) {
                        SIMView var17;
                        try {
                            var7 = (short) 0;
                            var17 = SIMSystem.getTheSIMView();
                            var17.select(SIMView.FID_MF);
                            var17.select(SIMView.FID_DF_GSM);
                            var17.select(DF_FID);
                            var17.select(EF_FID_1);
                            var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex20], Vars.sfield_token255_descoff107_staticref26, (short) var7, false)));
                            var10001 = (short) var7;
                            var7 = (short) ((short) ((short) var7 + 1));
                            Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                            var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex33], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                            var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex32], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                        } catch (SIMViewException var14) {
                            return;
                        }
                        
                        var6 = (short) this.method_token255_descoff1433(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 13);
                        if (var6 == 13) {
                            var18 = Vars.sfield_token255_descoff114_staticref28;
                        } else if (var6 == 14) {
                            var18 = Vars.sfield_token255_descoff121_staticref30;
                        }
                        
                        if ((short) var6 > 0) {
                            short var1 = var6;
                            short var2 = (short) 0;
                            
                            while (var6 > 0 && result != RES_CMD_PERF_SESSION_TERM_USER) {
                                try {
                                    var7 = (short) 0;
                                    var17 = SIMSystem.getTheSIMView();
                                    var17.select(SIMView.FID_MF);
                                    var17.select(SIMView.FID_DF_GSM);
                                    var17.select(DF_FID);
                                    var17.select(EF_FID_1);
                                    if ((short) var6 == 13) {
                                        var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex33], Vars.sfield_token255_descoff107_staticref26, (short) var7, false)));
                                    } else {
                                        var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex32], Vars.sfield_token255_descoff107_staticref26, (short) var7, false)));
                                    }
                                    
                                    var10001 = (short) var7;
                                    var7 = (short) ((short) ((short) var7 + 1));
                                    Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                                    var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex22], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                                    var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex23], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                                } catch (SIMViewException var13) {
                                    return;
                                }
                                
                                var6 = (short) this.method_token255_descoff1433(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 7);
                                var2 = (short) ((short) var6);
                                if ((short) var6 > 0) {
                                    do {
                                        if (var18[0] == 0) {
                                            result = this.readAndDisplayText(textIndex6, dcs);
                                            var6 = (short) ((short) var1);
                                            break;
                                        }
                                        
                                        var6 = (short) this.method_token255_descoff929(var18, (short) 0);
                                        if ((short) var6 > 0) {
                                            switch ((short) var2) {
                                                case 7:
                                                    var9 = (short) sfield_token255_descoff324_staticref66;
                                                    sfield_token255_descoff324_staticref66 = 4;
                                                    var2 = (short) Util.getShort(var18, (short) var6);
                                                    Util.arrayCopyNonAtomic(var18, (short) ((short) var6 + 2), Vars.sfield_token255_descoff107_staticref26, (short) 0, (short) var2);
                                                    this.shortBuffer[0] = 0;
                                                    this.method_token255_descoff1217((byte) 1, (byte) 3, true);
                                                    if (this.field_token21_descoff821) {
                                                        var6 = (short) -3;
                                                    }
                                                    
                                                    var2 = (short) 7;
                                                    sfield_token255_descoff324_staticref66 = (byte) ((short) var9);
                                                    break;
                                                case 8:
                                                    if (this.method_token255_descoff989(var18, (short) var6, true)) {
                                                        result = this.readAndDisplayText(textIndex5, dcs);
                                                    }
                                            }
                                        }
                                    }
                                    while (var6 > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
                                    
                                    if (var6 == -9) {
                                        var6 = var1;
                                    }
                                }
                            }
                            
                            if (var6 == -9) {
                                var6 = (short) 1;
                            }
                        }
                    }
                    
                    if (var6 != -9 || result == RES_CMD_PERF_SESSION_TERM_USER) {
                        var4 = false;
                    }
                    break;
                case 5:
                    do {
                        var7 = (short) 0;
                        var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex28], Vars.sfield_token255_descoff107_staticref26, (short) var7, false)));
                        var10001 = (short) var7;
                        var7 = (short) ((short) ((short) var7 + 1));
                        Vars.sfield_token255_descoff107_staticref26[var10001] = 2;
                        var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex19], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                        var7 = (short) ((short) ((short) var7 + this.readTextFromSim(Vars.shortHolder1[textIndex18], Vars.sfield_token255_descoff107_staticref26, (short) var7, true)));
                        var6 = (short) this.method_token255_descoff1433(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 11);
                        if ((short) var6 == 11) {
                            sfield_token255_descoff338_staticref68 = 1;
                            playTone();
                            result = this.readAndDisplayText(textIndex9, dcs);
                        } else if ((short) var6 == 12) {
                            sfield_token255_descoff338_staticref68 = 2;
                            result = this.readAndDisplayText(textIndex10, dcs);
                        }
                    }
                    while (var6 > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
                    
                    if (var6 != -9 || result == RES_CMD_PERF_SESSION_TERM_USER) {
                        var4 = false;
                    }
                    break;
                default:
                    var4 = false;
            }
        }
        
    }
    
    private void method_token255_descoff1061() {
        short var2 = (short) 0;
        var2 = Util.arrayCopy(Vars.sfield_token255_descoff37_staticref6, (short) 0, Vars.sfield_token255_descoff107_staticref26, var2, (short) (Vars.sfield_token255_descoff37_staticref6[0] + 1));
        short var10000 = var2;
        var2 = (short) (var2 + 1);
        short var3 = var10000;
        short var10001 = var2;
        var2 = (short) (var2 + 1);
        Vars.sfield_token255_descoff107_staticref26[var10001] = 73;
        var10001 = var2;
        var2 = (short) (var2 + 1);
        Vars.sfield_token255_descoff107_staticref26[var10001] = 77;
        var10001 = var2;
        var2 = (short) (var2 + 1);
        Vars.sfield_token255_descoff107_staticref26[var10001] = 69;
        var10001 = var2;
        var2 = (short) (var2 + 1);
        Vars.sfield_token255_descoff107_staticref26[var10001] = 73;
        var10001 = var2;
        var2 = (short) (var2 + 1);
        Vars.sfield_token255_descoff107_staticref26[var10001] = 32;
        Util.arrayCopy(Vars.sfield_token255_descoff93_staticref22, (short) 0, this.menuItemText, (short) 0, (short) Vars.sfield_token255_descoff93_staticref22.length);
        this.method_token255_descoff1553(this.menuItemText, (byte) 8);
        
        for (short i = 0; i <= 15; i = (short) (i + 1)) {
            this.menuItemText[(short) i] = this.menuItemText[(short) (i + 1)];
        }
        
        // byte[] src, short srcOff, byte[] dest, short destOff, short length
        var2 = Util.arrayCopy(this.menuItemText, (short) 0, Vars.sfield_token255_descoff107_staticref26, (short) var2, (short) 15);
        var10001 = var2;
        var2 = (short) (var2 + 1);
        Vars.sfield_token255_descoff107_staticref26[var10001] = 32;
        // byte[] src, short srcOff, byte[] dest, short destOff, short length
        var2 = Util.arrayCopy(appletVersion, (short) 0, Vars.sfield_token255_descoff107_staticref26, var2, (short) appletVersion.length);
        Vars.sfield_token255_descoff107_staticref26[var3] = (byte) ((short) (var2 - var3 - 1));
    }
    
    private boolean method_token255_descoff989(byte[] var1, short var2, boolean var3) {
        short var4 = (short) (var1 == Vars.sfield_token255_descoff114_staticref28 ? sfield_token255_descoff660_staticref119 : sfield_token255_descoff667_staticref121);
        short var5 = (short) 1;
        short var6 = (short) 0;
        short var7 = (short) 0;
        short var8 = (short) 0;
        
        short var9;
        for (var9 = (short) var1[0]; (short) var8 < (short) var9; ++var8) {
            var6 = (short) var1[(short) var5];
            var7 = (short) Util.getShort(var1, (short) ((short) ((short) var5 + 1) + (short) var6));
            if (var3 && (short) ((short) ((short) var5 + (short) var6) + 1) == (short) var2) {
                break;
            }
            
            var5 = (short) ((short) ((short) var5 + (short) ((short) ((short) ((short) var6 + (short) var7) + 1) + 2)));
            if (!var3 && (short) ((short) var5 - 1) >= (short) var2) {
                break;
            }
        }
        
        if ((short) var8 == (short) var9) {
            return false;
        } else {
            if (var3) {
                var9 = (short) 1;
                var8 = (short) ((short) ((short) ((short) var2 + (short) var7) + 2));
            } else {
                var9 = (short) ((short) ((short) var8 + 1));
                var8 = (short) ((short) var5);
                var5 = (short) 1;
            }
            
            if ((short) var9 < var1[0]) {
                Util.arrayCopyNonAtomic(var1, (short) var8, var1, (short) var5, (short) ((short) var4 - (short) var8));
            }
            
            var4 = (short) ((short) ((short) var4 - (short) ((short) var8 - (short) var5)));
            if (var1 == Vars.sfield_token255_descoff114_staticref28) {
                sfield_token255_descoff660_staticref119 = (short) var4;
            } else {
                sfield_token255_descoff667_staticref121 = (short) var4;
            }
            
            var1[0] = (byte) ((short) (var1[0] - (byte) ((short) var9)));
            return true;
        }
    }
    
    private boolean method_token255_descoff1469(byte[] var1, short var2, byte var3, byte var4) {
        short var5 = (short) 0;
        short var10001 = (short) var5;
        var5 = (short) ((short) ((short) var5 + 1));
        Vars.itemText1[var10001] = 17;
        
        try {
            SIMView var8 = SIMSystem.getTheSIMView();
            var8.select(SIMView.FID_MF);
            var8.select((short) 32528);
            var8.select((short) 28483);
            var8.readBinary((short) 0, Vars.itemText1, (short) var5, (short) 1);
        } catch (SIMViewException var11) {
            Vars.itemText1[(short) var5] = 1;
        }
        
        var10001 = (short) var5;
        var5 = (short) ((short) ((short) var5 + 1));
        Vars.itemText1[var10001] = (byte) ((short) (Vars.itemText1[var10001] + 1));
        var10001 = (short) var2;
        var2 = (short) ((short) ((short) var2 + 1));
        short var6 = (short) ((short) (var1[var10001] & 255));
        var5 = (short) Util.arrayCopy(var1, (short) var2, Vars.itemText1, (short) var5, (short) var6);
        var2 = (short) ((short) ((short) var2 + (short) var6));
        var10001 = (short) var5;
        var5 = (short) ((short) ((short) var5 + 1));
        Vars.itemText1[var10001] = 0;
        var10001 = (short) var5;
        var5 = (short) ((short) ((short) var5 + 1));
        Vars.itemText1[var10001] = (byte) ((short) var3);
        var10001 = (short) var5;
        var5 = (short) ((short) ((short) var5 + 1));
        Vars.itemText1[var10001] = (byte) ((short) var4);
        var10001 = (short) var2;
        var2 = (short) ((short) ((short) var2 + 1));
        var6 = (short) var1[var10001];
        short var7 = (short) ((byte) ((short) var5));
        if ((short) var3 == 4 && sfield_token255_descoff219_staticref46) {
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 0;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 77;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 67;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 32;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 2;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = (byte) ((short) var6);
        } else {
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = (byte) ((short) var6);
        }
        
        var5 = (short) Util.arrayCopy(var1, (short) var2, Vars.itemText1, (short) var5, (short) var6);
        if ((short) var3 == 4 && sfield_token255_descoff219_staticref46) {
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = (byte) ((short) (4 + (short) (sfield_token255_descoff212_staticref45 * 2)));
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 80;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 67;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = 32;
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            Vars.itemText1[var10001] = sfield_token255_descoff212_staticref45;
            var5 = (short) Util.arrayCopy(Vars.sfield_token255_descoff44_staticref8, (short) 0, Vars.itemText1, (short) var5, (short) (sfield_token255_descoff212_staticref45 * 2));
        }
        
        Vars.itemText1[(short) var7] = (byte) ((short) ((short) ((short) var5 - (short) var7) - 1));
        sfield_token255_descoff268_staticref56 = this.method_token255_descoff1037(Vars.itemText1, (byte) ((short) ((short) var7 + 1)), Vars.itemText1[(short) var7]);
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(PRO_CMD_SEND_SHORT_MESSAGE, (byte) ((short) var3 == 0 ? 1 : 0), DEV_ID_NETWORK);
        proHandl.appendTLV(TAG_SMS_TPDU, Vars.itemText1, (short) 0, (short) var5);
        byte result = proHandl.send();
        if (result == RES_CMD_PERF) {
            sfield_token255_descoff219_staticref46 = false;
            
            try {
                SIMView simView = SIMSystem.getTheSIMView();
                simView.select(SIMView.FID_MF);
                simView.select(SIMView.FID_DF_TELECOM);
                simView.select(SIMView.FID_EF_SMSS);
                simView.updateBinary((short) 0, Vars.itemText1, (short) 1, (short) 1);
            } catch (SIMViewException ex) {}
            
            return true;
        } else {
            return false;
        }
    }
    
    private boolean method_token255_descoff1241(short var1, boolean var2, byte var3, byte var4) {
        short var6 = (short) 0;
        short var7 = (short) 0;
        short var8 = (short) 0;
        short var9 = (short) 0;
        short var10 = (short) 0;
        short var11 = (short) 0;
        short var12 = (short) 0;
        short var13 = (short) 1;
        short var14 = (short) 1;
        short var15 = (short) 0;
        short var16 = (short) 1;
        short var17 = (short) 0;
        short var19 = (short) 0;
        this.field_token31_descoff891 = false;
        short var20 = (short) 5;
        this.field_token25_descoff849 = (byte) ((short) ((short) var3 & 6));
        short var5 = (short) ((short) ((short) var4 & 15));
        Vars.sfield_token255_descoff58_staticref12[31] = 0;
        short var18 = (short) ((short) ((short) ((short) var1 + 1) + 2));
        short var10002;
        if (var2) {
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            if (this.method_token255_descoff1049(Vars.sfield_token255_descoff107_staticref26[var10002]) == 0) {
                return false;
            }
        }
        
        if (this.field_token25_descoff849 == 2) {
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            this.field_token26_descoff856 = (short) ((short) (Vars.sfield_token255_descoff107_staticref26[var10002] << 7) & 32640);
        } else if (this.field_token25_descoff849 == 4) {
            this.field_token26_descoff856 = Util.getShort(Vars.sfield_token255_descoff107_staticref26, (short) var18);
            var18 = (short) (var18 + 2);
        }
        
        if ((short) ((short) var4 & -128) != 0) {
            this.field_token27_descoff863 = true;
        } else {
            this.field_token27_descoff863 = false;
        }
        
        if ((short) ((short) var4 & 64) != 0) {
            this.field_token28_descoff870 = true;
        } else {
            this.field_token28_descoff870 = false;
        }
        
        if ((short) ((short) var4 & 32) != 0) {
            if ((short) (Vars.sfield_token255_descoff107_staticref26[(short) var18] & sfield_token255_descoff170_staticref39) == 0 && Vars.sfield_token255_descoff107_staticref26[(short) var18] != 0) {
                return false;
            }
            
            ++var18;
        }
        
        if ((short) ((short) var4 & 16) != 0) {
            if ((short) (Vars.sfield_token255_descoff107_staticref26[(short) var18] & sfield_token255_descoff205_staticref44) == 0 && Vars.sfield_token255_descoff107_staticref26[(short) var18] != 0) {
                return false;
            }
            
            ++var18;
        }
        
        if ((short) var5 > 0) {
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            this.field_token29_descoff877 = Vars.sfield_token255_descoff107_staticref26[var10002];
            if (this.field_token29_descoff877 > 0) {
                var7 = (short) this.field_token29_descoff877;
                if (this.field_token29_descoff877 > 11) {
                    this.field_token29_descoff877 = 11;
                }
                
                Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var18, Vars.sfield_token255_descoff58_staticref12, (short) 0, this.field_token29_descoff877);
                var18 = (short) ((short) ((short) var18 + (short) var7));
            }
            
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            this.field_token30_descoff884 = Vars.sfield_token255_descoff107_staticref26[var10002];
            if (this.field_token30_descoff884 > 0) {
                var7 = (short) this.field_token30_descoff884;
                if (this.field_token30_descoff884 > 20) {
                    this.field_token30_descoff884 = 20;
                }
                
                Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var18, Vars.sfield_token255_descoff58_staticref12, (short) 11, this.field_token30_descoff884);
                var18 = (short) ((short) ((short) var18 + (short) var7));
            }
        }
        
        SIMView var21 = SIMSystem.getTheSIMView();
        var21.select(SIMView.FID_MF);
        var21.select(SIMView.FID_DF_GSM);
        var21.select(DF_FID);
        var21.select(EF_FID_1);
        short var10000;
        short var22;
        if (sfield_token255_descoff331_staticref67 != 2 || sfield_token255_descoff324_staticref66 != 3) {
            var9 = (short) this.method_token255_descoff1109((byte) 100);
            if ((short) var9 != -1) {
                var10 = (short) ((short) ((short) var10 + this.readTextFromSim((short) var9, Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), true)));
                var10000 = (short) var10;
                var10 = (short) ((short) ((short) var10 + 1));
                var11 = (short) var10000;
                var22 = (short) -128;
                var10002 = (short) var10;
                var10 = (short) ((short) ((short) var10 + 1));
                Vars.sfield_token255_descoff58_staticref12[(short) (32 + var10002)] = (byte) ((short) var22);
                Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var11)] = (byte) ((short) ((short) ((short) var10 - (short) var11) - 1));
                Vars.sfield_token255_descoff58_staticref12[31] = (byte) ((short) (Vars.sfield_token255_descoff58_staticref12[31] + 1));
            }
        }
        
        for (var22 = (short) 0; (short) var22 < (short) var5 && (short) var22 < (short) var20; var22 = (short) ((byte) ((short) ((short) var22 + 1)))) {
            var9 = (short) 0;
            short var23 = (short) Vars.sfield_token255_descoff107_staticref26[(short) var18];
            var6 = (short) ((short) ((short) var23 & 112));
            var16 = (short) 1;
            var14 = (short) 1;
            var15 = (short) 0;
            switch ((short) var6) {
                case 0:
                case 32:
                case 48:
                case 80:
                case 96:
                    if ((short) var6 == 0 && sfield_token255_descoff163_staticref38 != 2 && sfield_token255_descoff163_staticref38 != 3) {
                        var14 = (short) 0;
                    }
                    
                    var9 = (short) ((short) (1 + (short) ((short) var23 & 15)));
                    var9 = (short) ((short) ((short) var9 + (short) (1 + (short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)] & 127))));
                    break;
                case 16:
                case 64:
                    if ((short) var6 == 64) {
                        this.field_token31_descoff891 = true;
                    }
                    
                    var9 = (short) ((short) (1 + (short) ((short) var23 & 15)));
                    break;
                case 112:
                    var6 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + 2)];
                    switch ((short) var6) {
                        case 2:
                        case 3:
                            var9 = (short) 1;
                            
                            for (var19 = (short) 0; (short) var9 < Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + 1)]; var19 = (short) ((byte) ((short) ((short) var19 + 1)))) {
                                var9 = (short) ((short) ((short) var9 + (byte) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) ((short) var18 + (short) var9) + 1) + 1)] + 1))));
                            }
                            
                            if (sfield_token255_descoff191_staticref42 && (short) var19 >= 2) {
                                var15 = (short) 1;
                            }
                            
                            var19 = (short) 0;
                            var9 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + 1)];
                            // byte var26 = Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) ((short) var18 + (short) var9) + 1)
                            // + 1)];
                            if (Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) var18 + (short) var9) + 4)] == 4) {
                                var19 = (short) 1;
                            }
                            
                            var9 = (short) 1;
                    }
                    
                    if ((short) var15 != 0) {
                        break;
                    }
                default:
                    var20 = (short) ((byte) ((short) ((short) var20 + 1)));
                    var16 = (short) 0;
                    var18 = (short) ((short) ((short) var18 + (short) (2 + Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + 1)])));
            }
            
            if ((short) var16 != 0) {
                if ((short) var14 != 0) {
                    Vars.sfield_token255_descoff58_staticref12[31] = (byte) ((short) (Vars.sfield_token255_descoff58_staticref12[31] + 1));
                }
                
                var17 = (short) ((short) ((short) var23 & -128) == 0 ? 0 : 1);
                var13 = (short) 1;
                short var24;
                if ((short) var17 == 0 && ((short) var15 == 0 || (short) var6 != 2 && (short) var6 != 3 || (short) var19 != 1)) {
                    if ((short) var14 != 0) {
                        if ((short) var15 != 0 && ((short) var6 == 2 || (short) var6 == 3)) {
                            var12 = (short) ((byte) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) var18 + (short) var9) + 2)] + 1)));
                            var11 = (short) this.method_token255_descoff1109(Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) ((short) ((short) var18 + (short) var9) + 2) + (short) var12) + 1)]);
                        } else {
                            var11 = (short) this.method_token255_descoff1109(Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)]);
                        }
                        
                        if ((short) var11 != -1) {
                            var10 = (short) ((short) ((short) var10 + this.readTextFromSim((short) var11, Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), true)));
                        } else {
                            var13 = (short) 0;
                            Vars.sfield_token255_descoff58_staticref12[31] = (byte) ((short) (Vars.sfield_token255_descoff58_staticref12[31] - 1));
                        }
                    }
                } else {
                    var24 = (short) -128;
                    short var25 = (short) (this.field_token25_descoff849 == 0 ? 0 : 1);
                    if ((short) var15 != 0 && ((short) var6 == 2 || (short) var6 == 3)) {
                        var9 = (short) (var9 + 2);
                        var12 = (short) ((byte) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)] + 1)));
                        var9 = (short) ((short) ((short) var9 + (short) var12));
                        if ((short) var19 == 1) {
                            var12 = (short) ((byte) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)] + 1)));
                            var9 = (short) ((short) ((short) var9 + (short) ((short) ((short) ((short) var12 + 1) + 1) + 1)));
                            var12 = (short) ((byte) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)] + 1)));
                            var9 = (short) ((short) ((short) var9 + (short) var12));
                            var8 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)];
                        } else {
                            var8 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)];
                        }
                    } else {
                        var8 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + (short) var9)];
                        var12 = (short) ((byte) ((short) ((short) var8 + 1)));
                    }
                    
                    if ((short) var8 > 40) {
                        var8 = (short) 40;
                    }
                    
                    if (this.field_token25_descoff849 != 2 && this.field_token25_descoff849 != 4) {
                        if ((short) var14 != 0) {
                            if ((short) var25 != 0) {
                                Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) ((short) var8 + 1));
                                Vars.sfield_token255_descoff58_staticref12[(short) ((short) (32 + (short) var10) + 1)] = (byte) ((short) var24);
                                var10 = (short) (var10 + 2);
                            } else {
                                var10002 = (short) var10;
                                var10 = (short) ((short) ((short) var10 + 1));
                                Vars.sfield_token255_descoff58_staticref12[(short) (32 + var10002)] = (byte) ((short) var8);
                            }
                            
                            Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) ((short) ((short) var18 + (short) var9) + 1), Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), (short) var8);
                            if ((short) var15 == 0) {
                                var9 = (short) ((short) ((short) var9 + (short) var12));
                            }
                            
                            var10 = (short) ((short) ((short) var10 + (short) var8));
                        }
                    } else {
                        if ((short) var14 != 0) {
                            var8 = (short) ((byte) this.method_token255_descoff977(Vars.sfield_token255_descoff107_staticref26, (short) ((short) var18 + (short) var9), Vars.sfield_token255_descoff58_staticref12,
                                    (short) (32 + (short) ((short) var10 + 2))));
                            Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) (((short) var8 > 40 ? 40 : (short) var8) + 1));
                            Vars.sfield_token255_descoff58_staticref12[(short) ((short) (32 + (short) var10) + 1)] = (byte) ((short) var24);
                            var10 = (short) ((short) ((short) var10 + (short) (Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] + 1)));
                        }
                        
                        if ((short) var15 == 0) {
                            var9 = (short) ((short) ((short) var9 + (short) var12));
                        }
                    }
                }
                
                if ((short) var14 == 0) {
                    var13 = (short) 0;
                }
                
                if ((short) var13 != 0) {
                    var10000 = (short) var10;
                    var10 = (short) ((short) ((short) var10 + 1));
                    var11 = (short) var10000;
                    Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) var6);
                    ++var10;
                }
                
                var8 = (short) ((short) ((short) var23 & 15));
                var7 = (short) ((short) var8);
                if ((short) var8 > 11) {
                    var8 = (short) 11;
                }
                
                if ((short) var6 == 16 || (short) var6 == 0 || (short) var6 == 48 || (short) var6 == 32 || (short) var6 == 96 || (short) var6 == 80) {
                    if ((short) var13 != 0) {
                        if ((short) var8 > 0) {
                            Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) ((short) var8 + 1));
                            if ((short) var6 != 16) {
                                var24 = (short) ((byte) ((short) ((short) ((short) var8 - 1) * 2)));
                                if ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) ((short) var18 + 1) + (short) var8) - 1)] & -16) == -16) {
                                    var24 = (short) ((byte) ((short) ((short) var24 - 1)));
                                }
                            } else {
                                var24 = (short) ((short) var8);
                            }
                            
                            ++var10;
                            var10002 = (short) var10;
                            var10 = (short) ((short) ((short) var10 + 1));
                            Vars.sfield_token255_descoff58_staticref12[(short) (32 + var10002)] = (byte) ((short) var24);
                            Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) ((short) var18 + 1), Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), (short) var8);
                            var10 = (short) ((short) ((short) var10 + (short) var8));
                        } else {
                            Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) (this.field_token29_descoff877 + 1));
                            if ((short) var6 != 16) {
                                var24 = (short) ((byte) ((short) ((short) (this.field_token29_descoff877 - 1) * 2)));
                                if ((short) (Vars.sfield_token255_descoff58_staticref12[(short) (this.field_token29_descoff877 - 1)] & -16) == -16) {
                                    var24 = (short) ((byte) ((short) ((short) var24 - 1)));
                                }
                            } else {
                                var24 = (short) this.field_token29_descoff877;
                            }
                            
                            ++var10;
                            var10002 = (short) var10;
                            var10 = (short) ((short) ((short) var10 + 1));
                            Vars.sfield_token255_descoff58_staticref12[(short) (32 + var10002)] = (byte) ((short) var24);
                            Util.arrayCopy(Vars.sfield_token255_descoff58_staticref12, (short) 0, Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), this.field_token29_descoff877);
                            var10 = (short) ((short) ((short) var10 + this.field_token29_descoff877));
                        }
                    }
                    
                    var18 = (short) ((short) ((short) var18 + (short) (1 + (short) var7)));
                }
                
                if ((short) var6 == 0 || (short) var6 == 48 || (short) var6 == 32 || (short) var6 == 96 || (short) var6 == 80) {
                    var24 = (short) Vars.sfield_token255_descoff107_staticref26[(short) var18];
                    var8 = (short) ((short) ((short) var24 & 127));
                    var7 = (short) ((short) var8);
                    if ((short) var8 > 20) {
                        var8 = (short) 20;
                    }
                    
                    if ((short) var13 != 0) {
                        if ((short) var8 > 0) {
                            Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) ((short) var18 + 1), Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
                        } else {
                            var8 = (short) this.field_token30_descoff884;
                            Util.arrayCopy(Vars.sfield_token255_descoff58_staticref12, (short) 11, Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
                        }
                        
                        if ((short) ((short) var24 & -128) != 0) {
                            Vars.sfield_token255_descoff58_staticref12[(short) ((short) ((short) (32 + (short) var10) + (short) var8) + 1)] = 32;
                            Vars.sfield_token255_descoff58_staticref12[(short) ((short) ((short) (32 + (short) var10) + (short) var8) + 2)] = (byte) ((short) ((short) ((short) var22 + 1) + 48));
                            var8 = (short) ((byte) ((short) ((short) var8 + 2)));
                        }
                        
                        if ((short) var6 == 0) {
                            Vars.sfield_token255_descoff58_staticref12[(short) ((short) ((short) (32 + (short) var10) + (short) var8) + 1)] = 32;
                            Vars.sfield_token255_descoff58_staticref12[(short) ((short) ((short) (32 + (short) var10) + (short) var8) + 2)] = (byte) ((short) (sfield_token255_descoff163_staticref38 + 48));
                            var8 = (short) ((byte) ((short) ((short) var8 + 2)));
                        }
                        
                        Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) var8);
                        var10 = (short) ((short) ((short) var10 + (byte) ((short) ((short) var8 + 1))));
                    }
                    
                    var18 = (short) ((short) ((short) var18 + (short) ((short) var7 + 1)));
                }
                
                if ((short) var6 == 2 || (short) var6 == 3) {
                    var8 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) ((short) var18 + 1) + 2)];
                    var7 = (short) ((short) var8);
                    if ((short) var8 > 20) {
                        var8 = (short) 20;
                    }
                    
                    if ((short) var13 != 0) {
                        if ((short) var8 > 0) {
                            Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) ((short) ((short) var18 + 1) + 3), Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
                        } else {
                            var8 = (short) this.field_token30_descoff884;
                            Util.arrayCopy(Vars.sfield_token255_descoff58_staticref12, (short) 11, Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
                        }
                        
                        Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) var8);
                        var10 = (short) ((short) ((short) var10 + (byte) ((short) ((short) var8 + 1))));
                    }
                }
                
                if ((short) var15 != 0) {
                    var18 = (short) ((short) ((short) var18 + (byte) ((short) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + 1)] + 1) + 1))));
                    if ((short) var19 == 1) {
                        var18 = (short) ((short) ((short) var18 + (byte) ((short) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var18 + 1)] + 1) + 1))));
                        var20 = (short) ((byte) ((short) ((short) var20 + 1)));
                        var22 = (short) ((byte) ((short) ((short) var22 + 1)));
                    }
                } else {
                    if ((short) var6 == 64) {
                        ++var18;
                    }
                    
                    if ((short) var17 != 0) {
                        var18 = (short) ((short) ((short) var18 + (short) var12));
                    } else {
                        ++var18;
                    }
                }
                
                if ((short) var13 != 0) {
                    Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var11)] = (byte) ((short) ((short) ((short) var10 - (short) var11) - 1));
                }
            }
        }
        
        if (sfield_token255_descoff324_staticref66 != 3 || this.field_token28_descoff870) {
            var9 = (short) this.method_token255_descoff1109((byte) 101);
            if ((short) var9 != -1) {
                var10 = (short) ((short) ((short) var10 + this.readTextFromSim((short) var9, Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), true)));
                var10000 = (short) var10;
                var10 = (short) ((short) ((short) var10 + 1));
                var11 = (short) var10000;
                var10002 = (short) var10;
                var10 = (short) ((short) ((short) var10 + 1));
                Vars.sfield_token255_descoff58_staticref12[(short) (32 + var10002)] = -112;
                Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var11)] = (byte) ((short) ((short) ((short) var10 - (short) var11) - 1));
                Vars.sfield_token255_descoff58_staticref12[31] = (byte) ((short) (Vars.sfield_token255_descoff58_staticref12[31] + 1));
            }
        }
        
        return true;
    }
    
    private boolean method_token255_descoff941(byte var1) {
        switch ((short) var1) {
            case -128:
            case 0:
            case 16:
            case 32:
            case 48:
            case 64:
            case 80:
            case 96:
            case 112:
                return false;
            case -112:
                return true;
            default:
                return false;
        }
    }
    
    private boolean method_token255_descoff1133(byte var1) {
        byte var2 = (byte) (var1 - 1);
        return var2 >= 0 && var2 <= this.booleanBuffer2.length ? this.booleanBuffer2[var2] : false;
    }
    
    private void method_token255_descoff1025(boolean var1, byte var2, short var3, boolean var4, boolean var5) {
        short var6 = (short) 0;
        short var7 = var3;
        short var9 = (short) 0;
        short var10 = (short) 0;
        short var11 = (short) 0;
        short var12 = (short) 2;
        byte displayTextRes = 0;
        this.field_token23_descoff835 = -6;
        short var8;
        if (var1) {
            var8 = (short) 8;
        } else {
            var8 = (short) 4;
        }
        
        short var10001 = (short) var7;
        var7 = (short) ((short) ((short) var7 + 1));
        var9 = (short) Vars.sfield_token255_descoff107_staticref26[var10001];
        if ((short) var9 >= 1) {
            for (var11 = (short) 0; (short) var11 != (short) var2; var11 = (short) ((byte) ((short) ((short) var11 + 1)))) {
                var10 = (short) 0;
                if ((short) var11 == 127) {
                    var11 = (short) 0;
                }
                
                var7 = (short) ((short) ((short) var3 + 1));
                
                for (short var14 = (short) 0; (short) var14 < (short) var9; var14 = (short) ((byte) ((short) ((short) var14 + 1)))) {
                    this.booleanBuffer1[1] = (short) var14 == (short) ((short) var9 - 1);
                    var10001 = (short) var7;
                    var7 = (short) ((short) ((short) var7 + 1));
                    var12 = (short) Vars.sfield_token255_descoff107_staticref26[var10001];
                    Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var7, this.menuItemText, (short) 0, (short) var12);
                    if (this.field_token25_descoff849 != 2 && this.field_token25_descoff849 != 4) {
                        var6 = (short) Util.makeShort((byte) 0, this.menuItemText[(short) ((short) var12 - 1)]);
                    } else {
                        var6 = (short) Util.getShort(Vars.sfield_token255_descoff58_staticref12, (short) (622 + (short) ((short) var10 * 2)));
                        var10 = (short) ((byte) ((short) ((short) var10 + 1)));
                    }
                    
                    var7 = (short) ((short) ((short) var7 + (short) var12));
                    if (var5 || this.field_token31_descoff891) {
                        this.method_token255_descoff1529(var1, Vars.sfield_token255_descoff107_staticref26, (short) var7, (short) var6);
                        this.field_token31_descoff891 = false;
                        if (var5) {
                            this.method_token255_descoff1517(Vars.sfield_token255_descoff114_staticref28, this.shortBuffer[0], this.shortBuffer[4], false);
                        }
                    }
                    
                    if (!var4) {
                        return;
                    }
                    
                    var5 = false;
                    if (var1) {
                        displayTextRes = this.displayText((byte) ((short) var8), Vars.sfield_token255_descoff107_staticref26, (short) var7, (short) var6 > 159 ? 158 : (short) var6);
                    } else {
                        displayTextRes = this.displayText((byte) ((short) var8), Vars.sfield_token255_descoff107_staticref26, (short) var7, (short) var6 > 159 ? 159 : (short) var6);
                    }
                    
                    if (displayTextRes != RES_CMD_PERF_NO_RESP_FROM_USER) {
                        this.processResultOfDisplayText(displayTextRes);
                        if (this.field_token23_descoff835 != -6) {
                            return;
                        }
                    } else {
                        var14 = (short) ((short) var9);
                        this.field_token23_descoff835 = -4;
                    }
                    
                    var7 = (short) ((short) ((short) var7 + (short) var6));
                }
            }
            
        }
    }
    
    private byte displayText(byte dcs, byte[] text, short offset, short length) {
        ProactiveHandler proHandlr = ProactiveHandler.getTheHandler();
        proHandlr.initDisplayText((byte) 0x80, dcs, text, offset, length); // byte qualifier, byte dcs, byte[] buffer, short offset, short length
        return proHandlr.send();
    }
    
    // method_token255_descoff1181
    private void processResultOfDisplayText(byte displayTextRes) {
        switch (displayTextRes) {
            case RES_CMD_PERF:
                this.resetVars();
                this.field_token23_descoff835 = this.method_token255_descoff917(Vars.sfield_token255_descoff58_staticref12, (short) 31);
                
                if (this.field_token23_descoff835 != -9 && this.field_token23_descoff835 != -3 && this.field_token23_descoff835 != -8 && this.field_token23_descoff835 != -5) {
                    if (this.field_token23_descoff835 != -6 && this.field_token23_descoff835 < 0) {
                        this.field_token23_descoff835 = -4;
                    }
                } else {
                    sfield_token255_descoff135_staticref33 = true;
                    sfield_token255_descoff324_staticref66 = 3;
                    sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                    this.field_token23_descoff835 = -7;
                }
                break;
            case RES_CMD_PERF_SESSION_TERM_USER:
                this.field_token21_descoff821 = true;
            case RES_CMD_PERF_BACKWARD_MOVE_REQ:
                sfield_token255_descoff135_staticref33 = true;
                sfield_token255_descoff324_staticref66 = 3;
                sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                this.field_token23_descoff835 = -7;
                break;
            default:
                sfield_token255_descoff135_staticref33 = true;
                sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                this.field_token23_descoff835 = -7;
        }
        
    }
    
    private boolean method_token255_descoff1205(byte var1, boolean var2) {
        for (short var3 = (short) 0; (short) var3 < Vars.sfield_token255_descoff100_staticref24.length; var3 = (short) ((byte) ((short) ((short) var3 + 1)))) {
            if (Vars.sfield_token255_descoff100_staticref24[(short) var3] == (short) var1) {
                return false;
            }
        }
        
        if (var2) {
            Vars.sfield_token255_descoff100_staticref24[sfield_token255_descoff261_staticref54] = (byte) ((short) var1);
            if (++sfield_token255_descoff261_staticref54 >= Vars.sfield_token255_descoff100_staticref24.length) {
                sfield_token255_descoff261_staticref54 = 0;
            }
        }
        
        return true;
    }
    
    private void method_token255_descoff1361() {
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(ToolkitConstants.PRO_CMD_PROVIDE_LOCAL_INFORMATION, (byte) 1, ToolkitConstants.DEV_ID_ME);
        proHandl.send();
        ProactiveResponseHandler proRespHandl = ProactiveResponseHandler.getTheHandler();
        if (proRespHandl.getGeneralResult() == 0) {
            proRespHandl.findAndCopyValue((byte) 20, Vars.sfield_token255_descoff93_staticref22, (short) 0);
        } else {
            for (short var3 = (short) 0; (short) var3 < 8; var3 = (short) ((byte) ((short) ((short) var3 + 1)))) {
                Vars.sfield_token255_descoff93_staticref22[(short) var3] = -1;
            }
        }
        
    }
    
    private boolean getInput(byte[] dstBuffer, short dstOffset) {
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        this.readTextFromSim(Vars.shortHolder1[textIndex3], Vars.itemText1, (short) 0, false);
        
        // byte qualifier, byte dcs, byte[] buffer, short offset, short length, short minRespLength, short maxRespLength
        proHandl.initGetInput((byte) 0, dcs, Vars.itemText1, (short) 1, Vars.itemText1[0], (short) 1, (short) 19);
        byte result = proHandl.send();
        if (result != ToolkitConstants.RES_CMD_PERF) {
            return false;
        } else {
            ProactiveResponseHandler proRespHandl = ProactiveResponseHandler.getTheHandler();
            short len = proRespHandl.getTextStringLength();
            dstBuffer[dstOffset] = (byte) (len + 1);
            dstOffset = (short) (dstOffset + 1);
            dstBuffer[dstOffset] = 32;
            dstOffset = (short) (dstOffset + 1);
            
            proRespHandl.copyTextString(dstBuffer, dstOffset);
            return true;
        }
    }
    
    private byte method_token255_descoff1541(byte var1) {
        short var2 = (short) ((short) var1 & 15);
        if (var2 < 10) {
            return (byte) (var2 + 48);
        } else {
            return (byte) (var2 + 55);
        }
    }
    
    private byte method_token255_descoff1253(short var1) {
        short var2 = (short) 0;
        short var3 = (short) 0;
        short var4 = (short) 0;
        short var5 = (short) 0;
        
        Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var1, this.byteBufferPrivate, (short) 0, (short) 3);
        
        this.shortBuffer[3] = (short) (this.byteBufferPrivate[0] & 255);
        ++this.shortBuffer[3];
        this.field_token24_descoff842 = (this.byteBufferPrivate[1] & 1) != 0;
        if ((short) (this.byteBufferPrivate[1] & -64) != 0) {
            return (byte) -1;
        } else {
            switch ((short) (this.byteBufferPrivate[1] & 48)) {
                case 16:
                case 48:
                    var5 = (short) Vars.sfield_token255_descoff107_staticref26[(short) ((short) var1 + this.shortBuffer[3])];
                    this.shortBuffer[4] = (short) ((short) ((short) var1 + this.shortBuffer[3]) + 1);
                    
                    for (short var6 = (short) 0; (short) var6 < (short) var5; var6 = (short) ((byte) ((short) ((short) var6 + 1)))) {
                        var4 = (short) Vars.sfield_token255_descoff107_staticref26[this.shortBuffer[4]];
                        short var10004 = this.shortBuffer[4];
                        short var10001 = this.shortBuffer[4];
                        this.shortBuffer[4] = (short) (var10004 + 1);
                        var3 = (short) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) (var10001 + (short) var4)] & 255));
                        this.shortBuffer[4] += (short) ((short) var3 + (short) var4);
                    }
                    
                    this.shortBuffer[4] -= (short) var1;
                    if (this.method_token255_descoff1241((short) var1, (short) (this.byteBufferPrivate[1] & 48) == 48, this.byteBufferPrivate[1], this.byteBufferPrivate[2])) {
                        return (byte) 16;
                    }
                    break;
                case 32:
                    if (!this.field_token22_descoff828) {
                        return (byte) -2;
                    }
                    
                    this.shortBuffer[4] = this.shortBuffer[3];
                    var2 = (short) this.method_token255_descoff1229((short) var1, this.byteBufferPrivate[1], this.byteBufferPrivate[2]);
                    if ((short) var2 == 0) {
                        return (byte) 32;
                    }
                    
                    if ((short) var2 == 2) {
                        this.readAndDisplayText(textIndex7, dcs);
                    }
            }
            
            return (byte) -1;
        }
    }
    
    private void method_token255_descoff953(byte[] var1, short var2) {
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(ToolkitConstants.PRO_CMD_SET_UP_CALL, (byte) 0, ToolkitConstants.DEV_ID_NETWORK);
        short var3 = var1[var2];
        var3 = (short) (var3 - 1);
        ++var2;
        proHandl.appendTLV(ToolkitConstants.TAG_ADDRESS, var1, (short) (var2 + 1), var3);
        proHandl.send();
    }
    
    private byte method_token255_descoff1433(byte[] var1, short var2, byte var3) {
        short var4 = (short) 9;
        
        try {
            ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
            // byte type, byte qualifier, byte dstDevice
            
            proHandl.init(ToolkitConstants.PRO_CMD_SELECT_ITEM, (byte) 0, ToolkitConstants.DEV_ID_ME);
            
            // byte tag, byte[] value, short valueOffset, short valueLength
            proHandl.appendTLV(ToolkitConstants.TAG_ALPHA_IDENTIFIER, var1, (short) (var2 + 1), var1[var2]);
            
            var2 = (short) (var2 + (var1[var2] + 1));
            short var10001 = var2;
            var2 = (short) (var2 + 1);
            short var8 = (short) (var1[var10001] + var3);
            short var5 = var2;
            
            for (short var6 = var3; var6 < var8; var6 = (short) (var6 + 1)) {
                var4 = (short) (var4 + 3 + var1[var5]);
                if (var4 <= 255) {
                    if (var3 != 0) {
                        // byte tag, byte value1, byte[] value2, short value2Offset, short value2Length
                        proHandl.appendTLV(ToolkitConstants.TAG_ITEM, (byte) ((short) var6), var1, (short) (var5 + 1), var1[var5]);
                    } else {
                        if (var6 != (short) var3) {
                            ++var5;
                        }
                        
                        // byte tag, byte value1, byte[] value2, short value2Offset, short value2Length
                        proHandl.appendTLV(ToolkitConstants.TAG_ITEM, var1[(short) (var5 + 1 + var1[var5])], var1, (short) (var5 + 1), var1[var5]);
                    }
                }
                
                var5 = (short) (var5 + (short) (var1[var5] + 1));
            }
            
            proHandl.send();
            
            ProactiveResponseHandler proRespHandl = ProactiveResponseHandler.getTheHandler();
            byte proResult = proRespHandl.getGeneralResult();
            if (proResult == 0) {
                return proRespHandl.getItemIdentifier();
            } else if (proResult == 17) {
                return (byte) -9;
            } else if (proResult == 18) {
                return (byte) -10;
            } else if (proResult == 16) {
                return (byte) -3;
            } else {
                return (byte) -1;
            }
        } catch (Exception ex) {
            return (byte) -1;
        }
    }
    
    private void processCellBroadcastPage() {
        EnvelopeHandler var1 = EnvelopeHandler.getTheHandler();
        // short var2 = (short) 0;
        short var3 = (short) 0;
        this.shortBuffer[0] = 0;
        if (!sfield_token255_descoff177_staticref40 && !sfield_token255_descoff632_staticref113 && --sfield_token255_descoff247_staticref50 <= 0) {
            sfield_token255_descoff254_staticref52 *= sfield_token255_descoff653_staticref117;
            if (sfield_token255_descoff254_staticref52 < 0) {
                sfield_token255_descoff254_staticref52 = 32767;
            }
            
            sfield_token255_descoff247_staticref50 = sfield_token255_descoff254_staticref52;
            this.method_token255_descoff1445();
        }
        
        if (sfield_token255_descoff310_staticref64 == 1) {
            if (var1.findTLV(TAG_CELL_BROADCAST_PAGE, (byte) 1) != 0) {
                this.shortBuffer[1] = 0;
                this.method_token255_descoff1097((short) 8, this.menuItemText, (short) 0);
                if (this.method_token255_descoff1577(this.menuItemText, (short) 2)) {
                    short var4 = (short) ((short) (this.menuItemText[6] & 127));
                    short var5 = (short) this.menuItemText[7];
                    if ((short) var4 != 1) {
                        if (sfield_token255_descoff135_staticref33) {
                            if (this.method_token255_descoff1205((byte) ((short) var4), true) && --sfield_token255_descoff142_staticref34 <= 0) {
                                if (sfield_token255_descoff128_staticref32 == 1) {
                                    playTone();
                                    this.readAndDisplayText(textIndex8, dcs);
                                    sfield_token255_descoff128_staticref32 = 0;
                                }
                                
                                this.method_token255_descoff1397();
                                this.resetVars();
                                sfield_token255_descoff135_staticref33 = false;
                                sfield_token255_descoff324_staticref66 = 3;
                                sfield_token255_descoff142_staticref34 = 0;
                            }
                            
                        } else {
                            sfield_token255_descoff324_staticref66 = 3;
                            if ((short) var4 != this.byteBufferPublic[2]) {
                                this.byteBufferPublic[2] = (byte) ((short) var4);
                                if (!this.booleanBuffer1[0]) {
                                    this.method_token255_descoff1385();
                                } else {
                                    this.booleanBuffer1[0] = false;
                                }
                            }
                            
                            if (this.byteBufferPublic[0] < 4) {
                                if (!this.method_token255_descoff1205((byte) ((short) var4), false)) {
                                    return;
                                }
                                
                                short var6 = (short) ((short) ((short) ((short) ((short) var5 & -16) >> 4) & 15));
                                short var7 = (short) ((short) ((short) var5 & 15));
                                if (this.method_token255_descoff1133((byte) ((short) var6)) || (short) var7 > 4 || (short) var6 > (short) var7) {
                                    return;
                                }
                                
                                var3 = (short) ((short) (this.shortBuffer[2] + (short) (79 * (short) ((short) var6 - 1))));
                                if ((short) var3 > 473) {
                                    return;
                                }
                                
                                this.method_token255_descoff1097((short) 79, Vars.sfield_token255_descoff107_staticref26, (short) var3);
                                this.method_token255_descoff1493((byte) ((short) var6));
                                if (this.countSetted() >= (short) var7) {
                                    this.byteBufferPublic[1] = (byte) ((short) (this.byteBufferPublic[1] + 1));
                                    this.method_token255_descoff1385();
                                    this.booleanBuffer1[0] = true;
                                    this.shortBuffer[2] += (short) (79 * (short) var7);
                                }
                            }
                            
                            if (this.byteBufferPublic[1] >= 1 || this.byteBufferPublic[0] >= 4) {
                                this.method_token255_descoff1217(this.byteBufferPublic[1], (byte) 1, true);
                                this.method_token255_descoff1205((byte) ((short) var4), true);
                            }
                            
                        }
                    }
                }
            }
        }
    }
    
    private byte method_token255_descoff1037(byte[] var1, short var2, byte var3) {
        short result = (short) 0;
        
        for (short i = 0; i < var3; i++) {
            short var5 = (short) var1[(short) (var2 + i)];
            
            for (short j = 0; j < 8; j++) {
                if ((short) ((short) var5 & 1) != 0) {
                    var5 = (short) ((short) ((short) ((short) var5 >> 1) ^ 93));
                } else {
                    var5 = (short) ((short) ((short) var5 >> 1));
                }
            }
            
            result = (short) ((short) ((short) result ^ (short) var5));
        }
        
        result = (short) (result | -128);
        if ((short) (result & -16) == -128) {
            result = (short) ((short) ((short) result | 16));
        }
        
        return (byte) result;
    }
    
    private void sendUssd(byte[] array_ussd, short valueOffset) {
        // Please note that the result of Send_USSD command will go to applet but not a screen.
        // The result must be received by ProactiveResponseHandler and then shown by DisplayText
        
        // byte[] array_ussd = { (byte) 0x0F, (byte) 0xAA, (byte) 0x18, (byte) 0x0C, (byte) 0x36, (byte) 0x02 };
        // 0F - Data encoding scheme
        // AA180C3602 - packed *100# string
        
        short len = (short) array_ussd[valueOffset];
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(PRO_CMD_SEND_USSD, (byte) 0x00, DEV_ID_NETWORK);
        proHandl.appendTLV(TAG_USSD_STRING, array_ussd, (short) (valueOffset + 1), len);
        proHandl.send();
    }
    
    private byte readAndDisplayText(byte index, byte dcs/* , byte qualifier */) {
        short fileOffset = Vars.shortHolder1[index];
        
        SIMView simView = SIMSystem.getTheSIMView();
        simView.select(SIMView.FID_MF);
        simView.select(SIMView.FID_DF_GSM);
        simView.select(DF_FID);
        simView.select(EF_FID_1);
        simView.readBinary(fileOffset, this.menuItemText, (short) 0, (short) 1);
        
        short length = Util.makeShort((byte) 0, this.menuItemText[0]);
        // short fileOffset, byte[] resp, short respOffset, short respLength
        simView.readBinary(fileOffset, Vars.itemText1, (short) 0, (short) (length + 1));
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        // byte qualifier, byte dcs, byte[] buffer, short offset, short length
        proHandl.initDisplayText((byte) 0x80, dcs, Vars.itemText1, (short) 1, length);
        return proHandl.send();
    }
    
    private void method_token255_descoff1217(byte var1, byte var2, boolean var3) {
        short var6 = (short) 1;
        short var7 = (short) 0;
        
        for (short var4 = (short) 0; (short) var4 < (short) var1; var4 = (short) ((byte) ((short) ((short) var4 + 1)))) {
            short var5 = (short) this.method_token255_descoff1253(this.shortBuffer[0]);
            if (this.field_token28_descoff870) {
                var3 = true;
            }
            
            if (this.field_token24_descoff842) {
                var1 = (byte) (var1 + 1);
            }
            
            if ((short) var2 != 2 && (short) var2 != 3) {
                var6 = (short) 1;
            } else {
                var6 = (short) -1;
            }
            
            if ((short) var5 == 16) {
                if (this.field_token25_descoff849 != 2 && this.field_token25_descoff849 != 4) {
                    this.shortBuffer[5] = (short) (this.shortBuffer[0] + this.shortBuffer[3]);
                } else {
                    this.shortBuffer[5] = 552;
                    this.method_token255_descoff965(Vars.sfield_token255_descoff107_staticref26, (short) (this.shortBuffer[0] + this.shortBuffer[3]), Vars.sfield_token255_descoff107_staticref26,
                            this.shortBuffer[5]);
                }
                
                if ((short) var2 == 2 && var3 && sfield_token255_descoff268_staticref56 != this.field_token32_descoff898 && !this.field_token28_descoff870) {
                    var3 = false;
                    sfield_token255_descoff135_staticref33 = true;
                }
                
                if (this.field_token27_descoff863 && var3 && (short) var2 != 3) {
                    playTone();
                }
                
                this.method_token255_descoff1025(this.field_token25_descoff849 != 0, (byte) ((short) var6), this.shortBuffer[5], var3, (short) var2 == 2);
            } else {
                var5 = (short) 18;
                this.field_token23_descoff835 = -6;
            }
            
            if (this.field_token23_descoff835 >= 0) {
                var5 = (short) this.method_token255_descoff1409(Vars.sfield_token255_descoff58_staticref12, this.field_token23_descoff835);
            }
            
            if (this.field_token24_descoff842) {
                this.shortBuffer[0] += this.shortBuffer[4];
            } else {
                this.shortBuffer[0] += this.shortBuffer[4];
                if ((short) (this.shortBuffer[0] % 79) != 0) {
                    this.shortBuffer[0] += (short) (79 - (short) (this.shortBuffer[0] % 79));
                }
            }
            
            if (this.field_token23_descoff835 == -7) {
                if (!this.field_token24_descoff842) {
                    this.resetVars();
                    return;
                }
                
                var7 = (short) 1;
            }
            
            if ((short) var7 != 0 && !this.field_token24_descoff842) {
                this.resetVars();
                return;
            }
        }
        
        this.resetVars();
    }
    
    // resetVars
    private void resetVars() {
        this.shortBuffer[2] = 0;
        this.byteBufferPublic[0] = 0;
        this.method_token255_descoff1385();
        this.byteBufferPublic[1] = 0;
        this.booleanBuffer1[0] = false;
        this.byteBufferPublic[2] = -1;
    }
    
    private short method_token255_descoff917(byte[] var1, short var2) {
        if (!this.booleanBuffer1[1]) {
            return (short) -6;
        } else {
            short var4 = var1[var2];
            if (var4 <= 0) {
                return (short) -2;
            } else {
                ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
                proHandl.init(PRO_CMD_SELECT_ITEM, (byte) 0x00, DEV_ID_ME);
                short var6 = (short) (var2 + 1);
                if (sfield_token255_descoff331_staticref67 == 2 && sfield_token255_descoff324_staticref66 == 3 && (short) var4 == 1) {
                    var6 = (short) ((short) ((short) var2 + 1));
                    var6 = (short) ((short) ((short) var6 + (short) (var1[(short) var6] + 2)));
                    return (short) var6;
                } else {
                    for (short var7 = (short) 0; (short) var7 < (short) var4; var7 = (short) ((byte) ((short) ((short) var7 + 1)))) {
                        if (sfield_token255_descoff324_staticref66 != 4 || this.method_token255_descoff941(var1[(short) ((short) ((short) var6 + var1[(short) var6]) + 2)])) {
                            proHandl.appendTLV(ToolkitConstants.TAG_ITEM, (byte) ((short) ((short) var7 + 1)), var1, (short) ((short) var6 + 1), var1[(short) var6]);
                        }
                        
                        var6 = (short) ((short) ((short) var6 + (short) (var1[(short) var6] + 1)));
                        var6 = (short) ((short) ((short) var6 + (short) (var1[(short) var6] + 1)));
                    }
                    
                    byte result = proHandl.send();
                    if (result == RES_CMD_PERF_SESSION_TERM_USER) {
                        return (short) -3;
                    } else if (result == RES_CMD_PERF_BACKWARD_MOVE_REQ) {
                        return (short) -9;
                    } else if (result == RES_CMD_PERF_NO_RESP_FROM_USER) {
                        return (short) -4;
                    } else if (result != RES_CMD_PERF) {
                        return (short) -8;
                    } else {
                        ProactiveResponseHandler var8 = ProactiveResponseHandler.getTheHandler();
                        short var3 = (short) ((byte) ((short) (var8.getItemIdentifier() - 1)));
                        if ((short) var3 < 0) {
                            return (short) -5;
                        } else {
                            var6 = (short) ((short) ((short) var2 + 1));
                            var6 = (short) ((short) ((short) var6 + (short) (var1[(short) var6] + 1)));
                            
                            for (short var9 = (short) 0; (short) var9 < (short) var3; var9 = (short) ((byte) ((short) ((short) var9 + 1)))) {
                                var6 = (short) ((short) ((short) var6 + (short) (var1[(short) var6] + 1)));
                                var6 = (short) ((short) ((short) var6 + (short) (var1[(short) var6] + 1)));
                            }
                            
                            if (var1[(short) ((short) var6 + 1)] == -112) {
                                return (short) -6;
                            } else {
                                return (short) ((short) var6 + 1);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private short method_token255_descoff1109(byte var1) {
        // short var2 = (short) 0;
        // short var3 = (short) 0;
        short var4 = (short) 0;
        
        for (var4 = (short) sfield_token255_descoff618_staticref111; (short) var4 < (byte) ((short) ((short) (sfield_token255_descoff198_staticref43 * 2) + sfield_token255_descoff618_staticref111))
                && (short) var4 < (byte) ((short) (Vars.sfield_token255_descoff72_staticref16.length - 1)); var4 = (short) ((byte) ((short) ((short) var4 + 2)))) {
            if (Vars.sfield_token255_descoff72_staticref16[(short) var4] == (short) var1) {
                return Vars.sfield_token255_descoff72_staticref16[(short) ((short) var4 + 1)];
            }
        }
        
        return (short) -1;
    }
    
    private void method_token255_descoff1553(byte[] text, byte count) {
        for (short i = (short) (count - 1); i >= 0; i = (short) (i - 1)) {
            text[(short) (i * 2 + 1)] = this.method_token255_descoff1541((byte) ((text[i] >> 4) & 15));
            text[(short) (i * 2)] = this.method_token255_descoff1541((byte) (text[i] & 15));
        }
    }
    
    // method_token255_descoff1265
    private void playTone() {
        if (sfield_token255_descoff338_staticref68 != 2) {
            ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
            proHandl.init(ToolkitConstants.PRO_CMD_PLAY_TONE, (byte) 0, ToolkitConstants.DEV_ID_EARPIECE);
            proHandl.appendTLV(ToolkitConstants.TAG_TONE, (byte) 16);
            proHandl.appendTLV(ToolkitConstants.TAG_DURATION, (byte) 1, (byte) 1);
            proHandl.send();
        }
    }
    
    private void method_token255_descoff965(byte[] var1, short var2, byte[] var3, short var4) {
        short var5 = (short) ((short) var2);
        short var6 = (short) ((short) var4);
        short var7 = (short) 0;
        short var10001 = (short) var6;
        var6 = (short) ((short) ((short) var6 + 1));
        short var10003 = (short) var5;
        var5 = (short) ((short) ((short) var5 + 1));
        short var9 = (short) (var3[var10001] = var1[var10003]);
        
        for (short var12 = (short) 0; (short) var12 < (short) var9; var12 = (short) ((byte) ((short) ((short) var12 + 1)))) {
            var10001 = (short) var5;
            var5 = (short) ((short) ((short) var5 + 1));
            short var11 = (short) var1[var10001];
            Util.arrayCopy(var1, (short) var5, this.menuItemText, (short) 0, (short) var11);
            short var10 = (short) this.menuItemText[0];
            short var8 = (short) ((short) (this.menuItemText[(short) ((short) var11 - 1)] & 255));
            var10001 = (short) var6;
            var6 = (short) ((short) ((short) var6 + 1));
            var3[var10001] = (byte) ((short) var11);
            var5 = (short) ((short) ((short) var5 + (short) ((short) var11 - 1)));
            var7 = (short) this.method_token255_descoff977(var1, (short) var5, var3, (short) ((short) var6 + (short) var11));
            Util.setShort(Vars.sfield_token255_descoff58_staticref12, (short) (622 + (short) ((short) var12 * 2)), (short) var7);
            var5 = (short) ((short) ((short) var5 + (short) ((short) var8 + 1)));
            this.menuItemText[0] = (byte) ((short) var10);
            this.menuItemText[(short) ((short) var11 - 1)] = (byte) ((short) var7);
            Util.arrayCopy(this.menuItemText, (short) 0, var3, (short) var6, (short) var11);
            var6 = (short) ((short) ((short) var6 + (short) ((short) var11 + (short) var7)));
        }
        
    }
    
    private boolean method_token255_descoff1445() {
        this.method_token255_descoff1061();
        if (this.method_token255_descoff1469(Vars.sfield_token255_descoff107_staticref26, (short) 0, (byte) 4, (byte) -76)) {
            sfield_token255_descoff135_staticref33 = true;
            sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
            sfield_token255_descoff240_staticref49 = false;
            return true;
        } else {
            return false;
        }
    }
    
    private void eventSmsPPDataDownload() {
        EnvelopeHandler envHandl = EnvelopeHandler.getTheHandler();
        short var2 = (short) 0;
        short var3 = (short) 0;
        
        try {
            if (envHandl.findAndCompareValue(TAG_ADDRESS, Vars.sfield_token255_descoff37_staticref6, (short) 14) != 0) {
                this.field_token22_descoff828 = false;
            } else {
                envHandl.findTLV(TAG_SMS_TPDU, (byte) 1);
                this.field_token22_descoff828 = envHandl.compareValue((short) 3, Vars.sfield_token255_descoff37_staticref6, (short) 3, (short) (Vars.sfield_token255_descoff37_staticref6[0] - 2)) == 0;
            }
        } catch (Exception ex) {
            this.field_token22_descoff828 = false;
        }
        
        this.shortBuffer[1] = envHandl.getTPUDLOffset();
        
        short var10004 = this.shortBuffer[1];
        short var10001 = this.shortBuffer[1];
        this.shortBuffer[1] = (short) (var10004 + 1);
        var2 = (short) ((short) (envHandl.getValueByte(var10001) & 255));
        var2 = (short) ((short) var2 > 140 ? 140 : (short) var2);
        this.method_token255_descoff1097((short) 2, this.menuItemText, (short) 0);
        short var4 = (short) this.menuItemText[0];
        short var5 = (short) this.menuItemText[1];
        if ((short) var4 == 0 || (short) ((short) var4 & -128) != 0) {
            this.field_token32_descoff898 = (byte) ((short) var4);
            if ((short) var4 != this.byteBufferPublic[2]) {
                this.resetVars();
                this.byteBufferPublic[2] = (byte) ((short) var4);
            } else if (this.booleanBuffer1[0]) {
                this.resetVars();
                return;
            }
            
            if (this.byteBufferPublic[0] < 4) {
                short var6 = (short) ((short) ((short) ((short) ((short) var5 & -16) >> 4) & 15));
                short var7 = (short) ((short) ((short) var5 & 15));
                if (this.method_token255_descoff1133((byte) ((short) var6)) || (short) var7 > 4 || (short) var6 > (short) var7) {
                    return;
                }
                
                var3 = (short) ((short) (138 * (short) ((short) var6 - 1)));
                if ((short) var3 > 414) {
                    return;
                }
                
                this.method_token255_descoff1493((byte) ((short) var6));
                if (this.countSetted() >= (short) var7) {
                    this.byteBufferPublic[1] = (byte) ((short) (this.byteBufferPublic[1] + 1));
                    this.method_token255_descoff1385();
                    this.booleanBuffer1[0] = true;
                }
                
                this.method_token255_descoff1097((short) ((short) var2 - 2), Vars.sfield_token255_descoff107_staticref26, (short) var3);
            }
            
            if (this.booleanBuffer1[0]) {
                this.shortBuffer[0] = 0;
                this.byteBufferPublic[2] = -1;
                sfield_token255_descoff128_staticref32 = 0;
                sfield_token255_descoff135_staticref33 = false;
                this.method_token255_descoff1217((byte) 1, (byte) 2, sfield_token255_descoff324_staticref66 == 2);
            }
            
        }
    }
    
    private byte method_token255_descoff1049(byte var1) {
        short var2 = (short) 0;
        short var3 = (short) sfield_token255_descoff212_staticref45;
        if ((short) var1 == 0) {
            return (byte) 1;
        } else {
            for (var2 = (short) 0; (short) var2 < (short) var3; var2 = (short) ((byte) ((short) ((short) var2 + 1)))) {
                if (Vars.sfield_token255_descoff44_staticref8[(short) ((short) var2 * 2)] == (short) var1) {
                    if (Vars.sfield_token255_descoff44_staticref8[(byte) ((short) ((short) ((short) var2 * 2) + 1))] == 1) {
                        return (byte) 1;
                    }
                    
                    return (byte) 0;
                }
            }
            
            return (byte) 2;
        }
    }
    
    private void method_token255_descoff1493(byte var1) {
        short var2 = (short) ((byte) ((short) ((short) var1 - 1)));
        if (var2 >= 0 && var2 <= this.booleanBuffer2.length) {
            if (!this.booleanBuffer2[var2]) {
                this.byteBufferPublic[0] = (byte) ((short) (this.byteBufferPublic[0] + 1));
            }
            
            this.booleanBuffer2[var2] = true;
        }
    }
    
    private boolean method_token255_descoff1517(byte[] var1, short var2, short var3, boolean var4) {
        short var5 = (short) (var1 == Vars.sfield_token255_descoff114_staticref28 ? sfield_token255_descoff660_staticref119 : sfield_token255_descoff667_staticref121);
        short var6 = (short) Vars.sfield_token255_descoff58_staticref12[600];
        short var7 = (short) ((short) ((short) ((short) ((short) ((short) var3 + (short) var6) + 1) + 2) - (short) (577 - (short) var5)));
        if ((short) var7 > 0) {
            if (var4 && this.readAndDisplayText(textIndex35, dcs) != RES_CMD_PERF) {
                return false;
            }
            
            if (!this.method_token255_descoff989(var1, (short) var7, false)) {
                return false;
            }
            
            var5 = (short) (var1 == Vars.sfield_token255_descoff114_staticref28 ? sfield_token255_descoff660_staticref119 : sfield_token255_descoff667_staticref121);
        }
        
        while ((short) (9 + (short) (24 * (short) (var1[0] + 1))) > 255) {
            if (!this.method_token255_descoff989(var1, (short) 2, false)) {
                return false;
            }
            
            var5 = (short) (var1 == Vars.sfield_token255_descoff114_staticref28 ? sfield_token255_descoff660_staticref119 : sfield_token255_descoff667_staticref121);
        }
        
        Util.arrayCopy(Vars.sfield_token255_descoff58_staticref12, (short) 600, var1, (short) var5, (short) ((short) var6 + 1));
        Util.setShort(var1, (short) ((short) ((short) var5 + (short) var6) + 1), (short) var3);
        Util.arrayCopyNonAtomic(Vars.sfield_token255_descoff107_staticref26, (short) var2, var1, (short) ((short) ((short) ((short) var5 + (short) var6) + 1) + 2), (short) var3);
        var5 = (short) ((short) ((short) var5 + (short) ((short) ((short) ((short) var6 + (short) var3) + 1) + 2)));
        if (var1 == Vars.sfield_token255_descoff114_staticref28) {
            sfield_token255_descoff660_staticref119 = (short) var5;
        } else {
            sfield_token255_descoff667_staticref121 = (short) var5;
        }
        
        var1[0] = (byte) ((short) (var1[0] + 1));
        return true;
    }
    
    private byte method_token255_descoff1229(short var1, byte var2, byte var3) {
        short var4 = (short) 0;
        short var5 = (short) 0;
        short var6 = (short) 0;
        short var7 = (short) 0;
        short var8 = (short) 0;
        short var9 = (short) ((short) ((short) var3 & 15));
        boolean var11 = sfield_token255_descoff149_staticref36;
        short var10 = (short) ((short) ((short) ((short) var1 + 1) + 2));
        
        for (short var12 = (short) 0; var12 < var9; var12 = (short) (var12 + 1)) {
            short var13 = (short) Vars.sfield_token255_descoff107_staticref26[var10];
            short var14 = (short) ((short) (Vars.sfield_token255_descoff107_staticref26[(short) (var10 + 1)] & 255));
            var10 = (short) (var10 + 2);
            short var15;
            SIMView var16;
            short var10001;
            short var20;
            switch (var13) {
                case 3:
                    sfield_token255_descoff632_staticref113 = true;
                    Util.arrayCopy(Vars.sfield_token255_descoff93_staticref22, (short) 0, sfield_token255_descoff86_staticref20, (short) 0, (short) 8);
                    // var15 = (short) sfield_token255_descoff177_staticref40;
                    var15 = (short) (sfield_token255_descoff177_staticref40 ? 1 : 0);
                    sfield_token255_descoff149_staticref36 = false;
                    sfield_token255_descoff163_staticref38 = Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 5)];
                    sfield_token255_descoff170_staticref39 = Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 6)];
                    sfield_token255_descoff639_staticref114 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 7)] & -128) != 0;
                    sfield_token255_descoff177_staticref40 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 7)] & 64) != 0;
                    sfield_token255_descoff184_staticref41 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 7)] & 32) != 0;
                    sfield_token255_descoff191_staticref42 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 7)] & 16) != 0;
                    if (!sfield_token255_descoff177_staticref40) {
                        sfield_token255_descoff317_staticref65 = sfield_token255_descoff310_staticref64;
                        sfield_token255_descoff310_staticref64 = 2;
                        sfield_token255_descoff247_staticref50 = sfield_token255_descoff254_staticref52 = sfield_token255_descoff646_staticref115;
                        sfield_token255_descoff135_staticref33 = false;
                        this.method_token255_descoff1565(true);
                        return (byte) 2;
                    }
                    
                    if (var15 == 0) {
                        sfield_token255_descoff310_staticref64 = sfield_token255_descoff317_staticref65;
                        if (sfield_token255_descoff317_staticref65 == 1) {
                            this.method_token255_descoff1565(true);
                        }
                    }
                    
                    if (!sfield_token255_descoff184_staticref41) {
                        sfield_token255_descoff338_staticref68 = 2;
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 4:
                    var15 = (short) 0;
                    var20 = (short) 0;
                    var4 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) var10] > 10 ? 10 : Vars.sfield_token255_descoff107_staticref26[(short) var10]);
                    var7 = (short) ((short) ((short) var10 + 1));
                    
                    for (var5 = (short) 0; (short) var5 < (short) var4; var5 = (short) ((byte) ((short) ((short) var5 + 1)))) {
                        var10001 = (short) var20;
                        var20 = (short) ((short) ((short) var20 + 1));
                        Vars.sfield_token255_descoff51_staticref10[var10001] = Vars.sfield_token255_descoff107_staticref26[(short) var7];
                        var10001 = (short) var15;
                        var15 = (short) ((short) ((short) var15 + 1));
                        this.menuItemText[var10001] = Vars.sfield_token255_descoff107_staticref26[(short) var7];
                        if (Vars.sfield_token255_descoff107_staticref26[(short) ((short) var7 + 1)] == 3) {
                            if (this.method_token255_descoff1049(Vars.sfield_token255_descoff107_staticref26[(short) var7]) == 0) {
                                var10001 = (short) var15;
                                var15 = (short) ((short) ((short) var15 + 1));
                                this.menuItemText[var10001] = 0;
                            } else {
                                var10001 = (short) var15;
                                var15 = (short) ((short) ((short) var15 + 1));
                                this.menuItemText[var10001] = 1;
                            }
                        } else {
                            var10001 = (short) var15;
                            var15 = (short) ((short) ((short) var15 + 1));
                            this.menuItemText[var10001] = Vars.sfield_token255_descoff107_staticref26[(short) ((short) var7 + 1)];
                        }
                        
                        var7 = (short) (var7 + 2);
                        var6 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) var7] > 21 ? 21 : Vars.sfield_token255_descoff107_staticref26[(short) var7]);
                        var10001 = (short) var20;
                        var20 = (short) ((short) ((short) var20 + 1));
                        Vars.sfield_token255_descoff51_staticref10[var10001] = (byte) ((short) var6);
                        var20 = (short) Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) ((short) var7 + 1), Vars.sfield_token255_descoff51_staticref10, (short) var20, (short) var6);
                        var7 = (short) ((short) ((short) var7 + (short) (Vars.sfield_token255_descoff107_staticref26[(short) var7] + 1)));
                    }
                    
                    Util.arrayCopy(this.menuItemText, (short) 0, Vars.sfield_token255_descoff44_staticref8, (short) 0, (short) var15);
                    sfield_token255_descoff212_staticref45 = (byte) ((short) var4);
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 5:
                    var15 = (short) ((short) var10);
                    sfield_token255_descoff198_staticref43 = 0;
                    var7 = (short) ((short) (Vars.sfield_token255_descoff72_staticref16[sfield_token255_descoff233_staticref48] + 1));
                    var20 = (short) (Vars.sfield_token255_descoff107_staticref26[(short) var15] > 12 ? 12 : Vars.sfield_token255_descoff107_staticref26[(short) var15]);
                    ++var15;
                    var4 = (short) sfield_token255_descoff618_staticref111;
                    
                    for (var5 = (short) 0; (short) var5 < (short) var20; var5 = (short) ((byte) ((short) ((short) var5 + 1)))) {
                        if ((short) var4 < (byte) ((short) (Vars.sfield_token255_descoff72_staticref16.length - 1))) {
                            var10001 = (short) var4;
                            var4 = (short) ((byte) ((short) ((short) var4 + 1)));
                            Vars.sfield_token255_descoff72_staticref16[var10001] = Vars.sfield_token255_descoff107_staticref26[(short) var15];
                            var10001 = (short) var4;
                            var4 = (short) ((byte) ((short) ((short) var4 + 1)));
                            Vars.sfield_token255_descoff72_staticref16[var10001] = (short) ((short) var7 + 1);
                            var6 = (short) ((byte) ((short) (2 + Vars.sfield_token255_descoff107_staticref26[(short) ((short) var15 + 1)])));
                            var8 = (short) ((short) var6);
                            if ((short) var6 > 29) {
                                var6 = (short) 29;
                                Vars.sfield_token255_descoff107_staticref26[(short) ((short) var15 + 1)] = (byte) ((short) ((short) ((short) var6 - 1) - 1));
                            }
                            
                            sfield_token255_descoff198_staticref43 = (byte) ((short) (sfield_token255_descoff198_staticref43 + 1));
                            
                            try {
                                SIMView var17 = SIMSystem.getTheSIMView();
                                var17.select(SIMView.FID_MF);
                                var17.select(SIMView.FID_DF_GSM);
                                var17.select(DF_FID);
                                var17.select(EF_FID_1);
                                this.menuItemText[0] = sfield_token255_descoff198_staticref43;
                                var17.updateBinary(Vars.sfield_token255_descoff72_staticref16[sfield_token255_descoff233_staticref48], this.menuItemText, (short) 0, (short) 1);
                                var17.updateBinary((short) var7, Vars.sfield_token255_descoff107_staticref26, (short) var15, (short) var6);
                                var7 = (short) ((short) ((short) var7 + (short) var6));
                            } catch (SIMViewException var18) {
                                sfield_token255_descoff198_staticref43 = (byte) ((short) (sfield_token255_descoff198_staticref43 - 1));
                            }
                        }
                        
                        var15 = (short) ((short) ((short) var15 + (short) var8));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 6:
                    sfield_token255_descoff303_staticref63 = Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 2)];
                    if ((short) var14 > 4) {
                        sfield_token255_descoff646_staticref115 = Util.makeShort((byte) 0, Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 3)]);
                        sfield_token255_descoff653_staticref117 = Util.makeShort((byte) 0, Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 4)]);
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 7:
                    var15 = (short) Vars.sfield_token255_descoff107_staticref26[(short) var10];
                    if ((short) var15 > 1 && (short) var15 <= 12) {
                        Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var10, Vars.sfield_token255_descoff37_staticref6, (short) 0, (short) ((short) var15 + 1));
                        var16 = SIMSystem.getTheSIMView();
                        var16.select(SIMView.FID_MF);
                        var16.select(SIMView.FID_DF_GSM);
                        var16.select(DF_FID);
                        var16.select(EF_FID_1);
                        var16.updateBinary((short) 15, Vars.sfield_token255_descoff107_staticref26, (short) var10, (short) ((short) var15 + 1));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 8:
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(false);
                    }
                    
                    sfield_token255_descoff275_staticref57 = Util.getShort(Vars.sfield_token255_descoff107_staticref26, (short) var10);
                    sfield_token255_descoff282_staticref59 = Util.makeShort(Vars.sfield_token255_descoff107_staticref26[(short) ((short) var10 + 1)], Vars.sfield_token255_descoff107_staticref26[(short) var10]);
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(true);
                    }
                    
                    SIMView var19 = SIMSystem.getTheSIMView();
                    var19.select(SIMView.FID_MF);
                    var19.select(SIMView.FID_DF_GSM);
                    var19.select(DF_FID);
                    var19.select(EF_FID_1);
                    var19.updateBinary((short) 29, Vars.sfield_token255_descoff107_staticref26, (short) var10, (short) 2);
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 9:
                    if (Vars.sfield_token255_descoff107_staticref26[(short) var10] == 1 && sfield_token255_descoff184_staticref41) {
                        sfield_token255_descoff338_staticref68 = 1;
                    } else if (Vars.sfield_token255_descoff107_staticref26[(short) var10] == 2) {
                        sfield_token255_descoff338_staticref68 = 2;
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 10:
                    if (Vars.sfield_token255_descoff107_staticref26[(short) var10] == 2) {
                        sfield_token255_descoff331_staticref67 = 2;
                    } else if (Vars.sfield_token255_descoff107_staticref26[(short) var10] == 1) {
                        sfield_token255_descoff331_staticref67 = 1;
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 11:
                    sfield_token255_descoff205_staticref44 = Vars.sfield_token255_descoff107_staticref26[(short) var10];
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 12:
                    this.method_token255_descoff1361();
                    this.method_token255_descoff1445();
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 13:
                    var15 = (short) Vars.sfield_token255_descoff107_staticref26[(short) var10];
                    if ((short) var15 > 1 && (short) var15 < 12) {
                        Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var10, Vars.sfield_token255_descoff37_staticref6, (short) 13, (short) ((short) var15 + 1));
                        var16 = SIMSystem.getTheSIMView();
                        var16.select(SIMView.FID_MF);
                        var16.select(SIMView.FID_DF_GSM);
                        var16.select(DF_FID);
                        var16.select(EF_FID_1);
                        var16.updateBinary((short) 2, Vars.sfield_token255_descoff107_staticref26, (short) var10, (short) ((short) var15 + 1));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 14:
                    if (Util.arrayCompare(Vars.sfield_token255_descoff37_staticref6, (short) 27, Vars.sfield_token255_descoff107_staticref26, (short) var10, Vars.sfield_token255_descoff37_staticref6[26]) != 0) {
                        return (byte) 6;
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 15:
                    var15 = (short) Vars.sfield_token255_descoff107_staticref26[(short) var10];
                    if ((short) var15 > 1 && (short) var15 < 11) {
                        Util.arrayCopy(Vars.sfield_token255_descoff107_staticref26, (short) var10, Vars.sfield_token255_descoff37_staticref6, (short) 26, (short) ((short) var15 + 1));
                        var16 = SIMSystem.getTheSIMView();
                        var16.select(SIMView.FID_MF);
                        var16.select(SIMView.FID_DF_GSM);
                        var16.select(DF_FID);
                        var16.select(EF_FID_1);
                        var16.updateBinary((short) 31, Vars.sfield_token255_descoff107_staticref26, (short) var10, (short) ((short) var15 + 1));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                default:
                    var10 = (short) ((short) ((short) var10 + (short) var14));
            }
        }
        
        if ((short) ((short) var2 & 12) != 12 && ((short) ((short) var2 & 12) != 4 || !var11)) {
            if ((short) ((short) var2 & 12) == 0) {
                sfield_token255_descoff310_staticref64 = 2;
                sfield_token255_descoff135_staticref33 = false;
                this.method_token255_descoff1565(false);
                return (byte) 4;
            }
            
            if ((short) ((short) var2 & 12) == 8) {
                if (!sfield_token255_descoff177_staticref40) {
                    sfield_token255_descoff317_staticref65 = 3;
                } else {
                    sfield_token255_descoff310_staticref64 = 3;
                }
                
                sfield_token255_descoff135_staticref33 = false;
                this.method_token255_descoff1565(false);
                return (byte) 5;
            }
        } else if (sfield_token255_descoff177_staticref40 && (sfield_token255_descoff310_staticref64 != 3 || (short) ((short) var2 & 2) != 0)) {
            sfield_token255_descoff310_staticref64 = 1;
            this.method_token255_descoff1565(true);
            if (var11) {
                if (!sfield_token255_descoff296_staticref62) {
                    this.readAndDisplayText(textIndex29, dcs);
                } else {
                    this.readAndDisplayText(textIndex1, dcs);
                }
            }
        }
        
        return (byte) 0;
    }
    
    private void method_token255_descoff1385() {
        for (short i = 0; i < this.booleanBuffer2.length; i++) {
            if (this.booleanBuffer2[i]) {
                this.byteBufferPublic[0] = (byte) (this.byteBufferPublic[0] - 1);
            }
            
            this.booleanBuffer2[i] = false;
        }
        
    }
    
    private void method_token255_descoff1097(short var1, byte[] var2, short var3) {
        EnvelopeHandler var4 = EnvelopeHandler.getTheHandler();
        var4.copyValue(this.shortBuffer[1], var2, (short) var3, (short) var1);
        this.shortBuffer[1] += (short) var1;
    }
    
    private short method_token255_descoff929(byte[] var1, short var2) {
        short var4 = (short) var1[(short) var2];
        if ((short) var4 == 0) {
            return (short) -2;
        } else {
            ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
            proHandl.init(ToolkitConstants.PRO_CMD_SELECT_ITEM, (byte) 0, ToolkitConstants.DEV_ID_ME);
            short var7 = (short) ((short) ((short) var2 + 1));
            
            short var5;
            for (short var8 = (short) 0; (short) var8 < (short) var4; var8 = (short) ((byte) ((short) ((short) var8 + 1)))) {
                Util.setShort(this.menuItemText, (short) ((short) var8 * 2), (short) var7);
                var7 = (short) ((short) ((short) var7 + (short) (var1[(short) var7] + 1)));
                var5 = (short) Util.getShort(var1, (short) var7);
                var7 = (short) ((short) ((short) var7 + (short) ((short) var5 + 2)));
            }
            
            for (short var9 = (short) ((byte) ((short) ((short) var4 - 1))); (short) var9 >= 0; var9 = (short) ((byte) ((short) ((short) var9 - 1)))) {
                var7 = (short) Util.getShort(this.menuItemText, (short) ((short) var9 * 2));
                proHandl.appendTLV(ToolkitConstants.TAG_ITEM, (byte) ((short) ((short) var9 + 1)), var1, (short) ((short) var7 + 1), var1[(short) var7]);
            }
            
            byte result = proHandl.send();
            if (result == ToolkitConstants.RES_CMD_PERF_SESSION_TERM_USER) {
                return (short) -3;
            } else if (result == ToolkitConstants.RES_CMD_PERF_BACKWARD_MOVE_REQ) {
                return (short) -9;
            } else if (result == ToolkitConstants.RES_CMD_PERF_NO_RESP_FROM_USER) {
                return (short) -4;
            } else if (result != ToolkitConstants.RES_CMD_PERF) {
                return (short) -8;
            } else {
                ProactiveResponseHandler var10 = ProactiveResponseHandler.getTheHandler();
                short var3 = (short) ((byte) ((short) (var10.getItemIdentifier() - 1)));
                if ((short) var3 < 0) {
                    return (short) -5;
                } else {
                    var7 = (short) ((short) ((short) var2 + 1));
                    var7 = (short) ((short) ((short) var7 + (short) (var1[(short) var7] + 1)));
                    
                    for (short var11 = (short) 0; (short) var11 < (short) var3; var11 = (short) ((byte) ((short) ((short) var11 + 1)))) {
                        var5 = (short) Util.getShort(var1, (short) var7);
                        var7 = (short) ((short) ((short) var7 + (short) ((short) var5 + 2)));
                        var7 = (short) ((short) ((short) var7 + (short) (var1[(short) var7] + 1)));
                    }
                    
                    return (short) var7;
                }
            }
        }
    }
    
    private byte method_token255_descoff1409(byte[] message, short index) {
        sfield_token255_descoff324_staticref66 = 3;
        
        byte ret = 0;
        short var4 = message[index];
        index++;
        
        switch (var4) {
            case -128: // 0x80
                ret = 1;
                break;
            case 0: // 0x00
            case 48: // 0x30
                this.byteBufferPublic[2] = -1;
                if (var4 == 0 && sfield_token255_descoff163_staticref38 != 1 && sfield_token255_descoff163_staticref38 != 4) {
                    sfield_token255_descoff128_staticref32 = 3;
                } else {
                    sfield_token255_descoff128_staticref32 = 1;
                }
                
                sfield_token255_descoff324_staticref66 = 2;
                sfield_token255_descoff135_staticref33 = true;
                sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                this.method_token255_descoff1469(message, index, (byte) 4, (byte) 0);
                break;
            case 2: // 0x02
            case 3: // 0x03
                sendUssd(message, index);
                break;
            case 16: // 0x10
                this.method_token255_descoff953(message, index);
                break;
            case 32: // 0x20
                sfield_token255_descoff135_staticref33 = true;
                sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                sfield_token255_descoff128_staticref32 = 3;
                this.method_token255_descoff1469(message, index, (byte) 0, (byte) 0);
                break;
            case 64: // 0x40
                boolean var5 = this.method_token255_descoff1517(Vars.sfield_token255_descoff121_staticref30, this.shortBuffer[0], this.shortBuffer[4], true);
                if (var5) {
                    this.readAndDisplayText(textIndex4, dcs);
                }
                break;
            case 80: // 0x50
                boolean result = getInput(Vars.sfield_token255_descoff107_staticref26, (short) 552);
                if (result) {
                    this.readPrepareAndSendSMS(message, index, Vars.sfield_token255_descoff107_staticref26, (short) 552);
                }
                break;
            case 96: // 0x60
                this.byteBufferPublic[2] = -1;
                sfield_token255_descoff128_staticref32 = 0;
                byte textLength = (byte) 127;
                if (this.askInputTextFromSim(this.field_token25_descoff849 != 0, textLength)) {
                    sfield_token255_descoff128_staticref32 = 1;
                    sfield_token255_descoff324_staticref66 = 2;
                    sfield_token255_descoff135_staticref33 = true;
                    sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
                    this.readPrepareAndSendSMS(message, index, Vars.sfield_token255_descoff107_staticref26, (short) 552);
                }
        }
        
        return ret;
    }
    
    // initGetInput - qualifier
    // bit 1:
    // 0 = digits (0 to 9, *, #, and +) only;
    // 1 = alphabet set.
    // bit 2:
    // 0 = SMS default alphabet;
    // 1 = UCS2 alphabet.
    // bit 3:
    // 0 = terminal may echo user input on the display;
    // 1 = user input shall not be revealed in any way (see note).
    // bit 4:
    // 0 = user input to be in unpacked format;
    // 1 = user input to be in SMS packed format.
    // bits 5 to 7: = RFU.
    // bit 8:
    // 0 = no help information available;
    // 1 = help information available.
    private boolean askInputTextFromSim(boolean isCyrilic, byte maxRespLength) {
        short textStringLen = (short) 0;
        short offset = (short) 552; // 0x0228
        byte qUcsAlpha = 0x03;
        byte qSMSDefaultAlpha = 0x01;
        byte qualifier = isCyrilic ? qUcsAlpha : qSMSDefaultAlpha;
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        this.readTextFromSim(Vars.shortHolder1[textIndex2], Vars.itemText1, (short) 0, false);
        proHandl.initGetInput(qualifier, dcs, Vars.itemText1, (short) 1, Vars.itemText1[0], (short) 1, (short) (maxRespLength / (isCyrilic ? 2 : 1)));
        byte result = proHandl.send();
        
        if (result != RES_CMD_PERF) {
            return false;
        } else {
            ProactiveResponseHandler proHandlResp = ProactiveResponseHandler.getTheHandler();
            textStringLen = proHandlResp.getTextStringLength();
            
            Vars.sfield_token255_descoff107_staticref26[offset++] = (byte) (textStringLen + 1);
            Vars.sfield_token255_descoff107_staticref26[offset++] = 32;
            
            proHandlResp.copyTextString(Vars.sfield_token255_descoff107_staticref26, offset);
            return true;
        }
    }
    
    private void method_token255_descoff1565(boolean var1) {
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(SIMView.FID_EF_CBMID, this.menuItemText, (short) 0, (short) 4); // Cell Broadcast Message Identifier for Data Download
            
            short len = (short) this.menuItemText[3];
            simView.readBinary((short) 0, this.menuItemText, (short) 0, (short) len);
            
            if (var1) {
                sfield_token255_descoff289_staticref61 = true;
                short var3 = (short) 0;
                
                for (short i = (short) (len - 2); i >= 0; i = (short) (i - 2)) {
                    if (Util.getShort(this.menuItemText, i) == -1) {
                        var3 = (short) (i);
                    } else if (Util.getShort(this.menuItemText, i) == sfield_token255_descoff275_staticref57) {
                        return;
                    }
                }
                
                Util.setShort(this.menuItemText, (short) var3, sfield_token255_descoff275_staticref57);
            } else {
                sfield_token255_descoff289_staticref61 = false;
                
                for (short i = (short) (len - 2); i >= 0; i = (short) (i - 2)) {
                    if (Util.getShort(this.menuItemText, i) == sfield_token255_descoff275_staticref57) {
                        Util.setShort(this.menuItemText, i, (short) -1);
                    }
                }
            }
            
            simView.updateBinary((short) 0, this.menuItemText, (short) 0, len);
        } catch (SIMViewException ex) {}
        
    }
    
    private short method_token255_descoff977(byte[] var1, short var2, byte[] var3, short var4) {
        short var5 = (short) 0;
        short var6 = (short) 0;
        short var7 = (short) 0;
        short var8 = (short) 0;
        short var9 = (short) 0;
        short var10 = (short) 0;
        short var11 = (short) ((byte) ((short) (this.menuItemText.length / 2)));
        short var12 = (short) 0;
        short var13 = (short) 127;
        short var14 = (short) -128;
        var9 = (short) this.field_token26_descoff856;
        short var10002 = (short) var2;
        var2 = (short) ((short) ((short) var2 + 1));
        
        for (var6 = (short) Util.makeShort((byte) 0, var1[var10002]); (short) var5 < (short) var6; var4 = (short) Util.arrayCopy(this.menuItemText, (short) 0, var3, (short) var4, (short) var7)) {
            if ((short) ((short) var6 - (short) var5) > (short) var11) {
                var8 = (short) ((short) var11);
            } else {
                var8 = (short) ((byte) ((short) ((short) var6 - (short) var5)));
            }
            
            Util.arrayCopy(var1, (short) var2, this.menuItemText, (short) var8, (short) var8);
            var2 = (short) ((short) ((short) var2 + (short) var8));
            var5 = (short) ((short) ((short) var5 + (short) var8));
            var7 = (short) 0;
            
            for (var12 = (short) ((short) var8); (short) var12 < (short) ((short) var8 * 2); var12 = (short) ((byte) ((short) ((short) var12 + 1)))) {
                if ((short) (this.menuItemText[(short) var12] & (short) var14) == 0) {
                    short var10001 = (short) var7;
                    var7 = (short) ((byte) ((short) ((short) var7 + 1)));
                    this.menuItemText[var10001] = 0;
                    var10001 = (short) var7;
                    var7 = (short) ((byte) ((short) ((short) var7 + 1)));
                    this.menuItemText[var10001] = this.menuItemText[(short) var12];
                } else {
                    var10 = (short) ((short) (this.menuItemText[(short) var12] & (short) var13));
                    var10 = (short) ((short) ((short) var10 + (short) var9));
                    Util.setShort(this.menuItemText, (short) var7, (short) var10);
                    var7 = (short) ((byte) ((short) ((short) var7 + 2)));
                }
            }
        }
        
        return (short) ((short) var6 * 2);
    }
    
    // method_token255_descoff1457
    private void readPrepareAndSendSMS(byte[] message, short index, byte[] buffer, short var4) {
        short var5 = (short) 0;
        short var6 = (short) 0;
        short var10001 = var5;
        var5 = (short) (var5 + 1);
        Vars.itemText1[var10001] = 17;
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_TELECOM);
            simView.select(SIMView.FID_EF_SMSS);
            simView.readBinary((short) 0, Vars.itemText1, var5, (short) 1);
        } catch (SIMViewException ex) {
            Vars.itemText1[var5] = 1;
        }
        
        var10001 = var5;
        var5++;
        Vars.itemText1[var10001] = (byte) (Vars.itemText1[var10001] + 1);
        var10001 = index;
        index++;
        short var7 = (short) (message[var10001] & 255);
        var5 = Util.arrayCopy(message, index, Vars.itemText1, var5, var7);
        index = (short) (index + var7);
        var10001 = var5;
        var5++;
        Vars.itemText1[var10001] = 0;
        var10001 = var5;
        var5++;
        Vars.itemText1[var10001] = 4;
        var10001 = var5;
        var5++;
        Vars.itemText1[var10001] = 0;
        var10001 = index;
        index++;
        var7 = (short) (message[var10001] & 255);
        var6 = var5;
        var10001 = var5;
        var5++;
        Vars.itemText1[var10001] = (byte) var7;
        var5 = Util.arrayCopy(message, index, Vars.itemText1, var5, var7);
        short var8 = (short) (buffer[var4] & 255);
        if (var7 + var8 > 140) {
            var8 = (short) (140 - var7);
        }
        
        var5 = Util.arrayCopy(buffer, (short) (var4 + 1), Vars.itemText1, var5, var8);
        Vars.itemText1[var6] = (byte) (var8 + var7);
        sfield_token255_descoff268_staticref56 = this.method_token255_descoff1037(Vars.itemText1, (byte) (var6 + 1), Vars.itemText1[var6]);
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(PRO_CMD_SEND_SHORT_MESSAGE, (byte) 0, DEV_ID_NETWORK);
        proHandl.appendTLV(TAG_SMS_TPDU, Vars.itemText1, (short) 0, var5);
        byte result = proHandl.send();
        if (result == RES_CMD_PERF) {
            try {
                SIMView simView = SIMSystem.getTheSIMView();
                simView.select(SIMView.FID_MF);
                simView.select(SIMView.FID_DF_TELECOM);
                simView.select(SIMView.FID_EF_SMSS);
                simView.updateBinary((short) 0, Vars.itemText1, (short) 1, (short) 1);
            } catch (SIMViewException ex) {}
        }
        
    }
    
    // method_token255_descoff1169
    private short readTextFromSim(short fileOffset, byte[] buffer, short respOffset, boolean fileAlreadySelected) {
        SIMView simView = SIMSystem.getTheSIMView();
        if (!fileAlreadySelected) {
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(DF_FID);
            simView.select(EF_FID_1);
        }
        
        simView.readBinary(fileOffset, this.menuItemText, (short) 0, (short) 1);
        
        short respLength = Util.makeShort((byte) 0, this.menuItemText[0]);
        respLength = (short) (respLength + 1);
        
        simView.readBinary(fileOffset, buffer, respOffset, respLength);
        return respLength;
    }
    
    private void method_token255_descoff1349() {
        short var1 = 0;
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(DF_FID);
            simView.select(EF_FID_1);
            short var10001 = (short) var1;
            var1 = (short) ((short) ((short) var1 + 1));
            simView.readBinary(var10001, this.menuItemText, (short) 0, (short) 1);
            if (this.menuItemText[0] == 0) {
                return;
            }
            
            var10001 = (short) var1;
            var1 = (short) ((short) ((short) var1 + 1));
            simView.readBinary(var10001, this.menuItemText, (short) 0, (short) 1);
            short var2 = (short) this.menuItemText[0];
            
            short var3;
            for (var3 = (short) 0; (short) var3 < (short) var2; var3 = (short) ((byte) ((short) ((short) var3 + 1)))) {
                simView.readBinary((short) var1, this.menuItemText, (short) 0, (short) 1);
                short var4 = (short) ((byte) ((short) (this.menuItemText[0] + 1)));
                if ((short) var3 == 0 && (short) var4 < 12) {
                    simView.readBinary((short) var1, Vars.sfield_token255_descoff37_staticref6, (short) 13, (short) var4);
                    var1 = (short) (var1 + 13);
                }
                
                if ((short) var3 == 1 && (short) var4 == 3) {
                    simView.readBinary((short) ((short) var1 + 1), Vars.sfield_token255_descoff107_staticref26, (short) 0, (short) 2);
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(false);
                    }
                    
                    short var6 = (short) ((short) ((short) (Vars.sfield_token255_descoff107_staticref26[0] << 8) & -256));
                    short var7 = (short) ((short) (Vars.sfield_token255_descoff107_staticref26[1] & 255));
                    sfield_token255_descoff275_staticref57 = (short) (var6 | var7);
                    sfield_token255_descoff282_staticref59 = (short) ((var7 << 8) & -256);
                    sfield_token255_descoff282_staticref59 |= (short) ((var6 >> 8) & 255);
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(true);
                    }
                    
                    simView.select(SIMView.FID_MF);
                    simView.select(SIMView.FID_DF_GSM);
                    simView.select(DF_FID);
                    simView.select(EF_FID_1);
                    var1 = (short) (var1 + 3);
                }
                
                simView.readBinary((short) var1, this.menuItemText, (short) 0, (short) 1);
                var4 = (short) ((byte) ((short) (this.menuItemText[0] + 1)));
                if ((short) var3 == 0 && (short) var4 <= 13) {
                    simView.readBinary((short) var1, Vars.sfield_token255_descoff37_staticref6, (short) 0, (short) var4);
                    var1 = (short) (var1 + 13);
                }
                
                if ((short) var3 == 1 && (short) var4 < 11) {
                    simView.readBinary((short) var1, Vars.sfield_token255_descoff37_staticref6, (short) 26, (short) var4);
                    var1 = (short) (var1 + 12);
                }
            }
            
            var1 = (short) 74;
            var10001 = (short) var1;
            var1 = (short) ((short) ((short) var1 + 1));
            simView.readBinary(var10001, this.menuItemText, (short) 0, (short) 1);
            dcs = this.menuItemText[0];
            var10001 = (short) var1;
            var1 = (short) ((short) ((short) var1 + 1));
            simView.readBinary(var10001, this.menuItemText, (short) 0, (short) 1);
            var2 = (short) this.menuItemText[0];
            
            for (var3 = (short) 0; (short) var3 < (short) var2; var3 = (short) ((byte) ((short) ((short) var3 + 1)))) {
                simView.readBinary((short) var1, this.menuItemText, (short) 0, (short) 1);
                if ((short) var3 < 36) {
                    Vars.shortHolder1[(short) var3] = (short) var1;
                }
                
                var1 = (short) ((short) ((short) var1 + (short) (Util.makeShort((byte) 0, this.menuItemText[0]) + 1)));
            }
            
            simView.readBinary((short) var1, this.menuItemText, (short) 0, (short) 1);
            var2 = (short) this.menuItemText[0];
            Vars.sfield_token255_descoff72_staticref16[sfield_token255_descoff233_staticref48] = (short) var1;
            ++var1;
            sfield_token255_descoff198_staticref43 = 0;
            
            for (var3 = (short) 0; (short) var3 < (short) var2; var3 = (short) ((byte) ((short) ((short) var3 + 1)))) {
                simView.readBinary((short) var1, this.menuItemText, (short) 0, (short) 2);
                if ((byte) ((short) ((short) ((short) var3 * 2) + sfield_token255_descoff618_staticref111)) < (byte) ((short) (Vars.sfield_token255_descoff72_staticref16.length - 1))) {
                    Vars.sfield_token255_descoff72_staticref16[(short) ((short) ((short) var3 * 2) + sfield_token255_descoff618_staticref111)] = this.menuItemText[0];
                    Vars.sfield_token255_descoff72_staticref16[(short) ((short) ((short) ((short) var3 * 2) + 1) + sfield_token255_descoff618_staticref111)] = (short) ((short) var1 + 1);
                    sfield_token255_descoff198_staticref43 = (byte) ((short) (sfield_token255_descoff198_staticref43 + 1));
                }
                
                var1 = (short) ((short) ((short) var1 + (short) (this.menuItemText[1] + 2)));
            }
            
            this.menuItemText[0] = 0;
            simView.updateBinary((short) 0, this.menuItemText, (short) 0, (short) 1);
        } catch (SIMViewException ex) {}
        
    }
    
    // method_token255_descoff1121
    private byte countSetted() {
        byte ret = 0;
        for (short i = 0; i < this.booleanBuffer2.length; i++) {
            if (this.booleanBuffer2[i]) {
                ret++;
            }
        }
        return ret;
    }
    
}
