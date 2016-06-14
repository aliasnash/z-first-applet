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

// @StringPool(value = { @StringDef(name = "Package", value = "com.denis"), @StringDef(name = "AppletName", value = "StkApplet") }, name =
// "StkAppletStrings")
public class StkApplet extends Applet implements Shareable, ToolkitInterface, ToolkitConstants, ExtendedBuffers {
    
    // hameleon
    private static final byte[] hameleonString                          = new byte[] { (byte) 8, (byte) 104, (byte) 97, (byte) 109, (byte) 101, (byte) 108, (byte) 101, (byte) 111, (byte) 110 };
    // 3.1.6.1 - должно отправляться вместе с imei
    private static final byte[] appletVersion                           = new byte[] { (byte) 51, (byte) 46, (byte) 49, (byte) 46, (byte) 54, (byte) 46, (byte) 49 };
    public static final byte[]  AIDSequence                             = new byte[] { (byte) -96, (byte) 0, (byte) 0, (byte) 0, (byte) 9, (byte) 0, (byte) 1 };
    public static final byte[]  imeiBuffer                              = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
    private static final byte[] imeiBufferToCompare                     = new byte[] { (byte) -17, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1 };
    
    // 32 byte
    // public static byte[] cbsMessageIdBuffer = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte)
    // 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
    // (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
    // (byte) 0, (byte) 0 };
    // private static short cbsMessageIdBufferIndex;
    
    // 37 byte
    // 00-12 - short number (0 len, 3-12 number)
    // 13-25 - smsc number (13 len, 14-25 number)
    // 26-36 - ?? number (26 len)
    public static byte[]        bufferForNumbers                        = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
    
    private static byte         dcs;
    
    private static short        DF_FID                                  = (short) 0xAAAC;
    private static short        EF_FID_1                                = (short) 0x2E20;
    private static short        EF_FID_2                                = (short) 0x2E21;
    
    private static boolean      isAllowPlaySound                        = true;
    
    // private static final byte USER_REQUEST_MOVE_BACK = -9;
    // private static final byte USER_REQUEST_NO_RESPONSE = -10;
    // private static final byte USER_REQUEST_TERMINATE = -3;
    // private static final byte USER_REQUEST_UNKNOWN_ERROR = -1;
    
    public boolean              msgLimitExceed;
    public boolean              booleanFlag2;
    public boolean              serviceInProgress;
    
    public short[]              shortBuffer;
    
    private boolean[]           concatMsgMapper;
    private boolean             isAddressInSmsTpduExist                 = false;
    private byte[]              byteBuffer;
    private byte                concatMsgCounter;
    private byte                firstByteMarkerClass                    = 0;
    private byte                userDataFirstBateSaved                  = 0;
    
    private static byte         textIndex1                              = 1;
    private static byte         textIndex2                              = 2;
    private static byte         textIndex3                              = 3;
    private static byte         textIndex4                              = 4;
    // private static byte textIndex5 = 5;
    // private static byte textIndex6 = 6;
    private static byte         textIndex7                              = 7;
    // private static byte textIndex8 = 8;
    // private static byte textIndex9 = 9;
    // private static byte textIndex10 = 10;
    // private static byte textIndex11 = 11;
    // private static byte textIndex12 = 12;
    // private static byte textIndex13 = 13;
    // private static byte textIndex14 = 14;
    // private static byte textIndex15 = 15;
    // private static byte textIndex16 = 16;
    // private static byte textIndex17 = 17;
    // private static byte textIndex18 = 18;
    // private static byte textIndex19 = 19;
    // private static byte textIndex20 = 20;
    // private static byte textIndex21 = 21;
    // private static byte textIndex22 = 22;
    // private static byte textIndex23 = 23;
    // private static byte textIndex24 = 24;
    // private static byte textIndex25 = 25;
    // private static byte textIndex26 = 26;
    // private static byte textIndex27 = 27;
    // private static byte textIndex28 = 28;
    private static byte         textIndex29                             = 29;
    // private static byte textIndex30 = 30;
    // private static byte textIndex31 = 31;
    // private static byte textIndex32 = 32;
    // private static byte textIndex33 = 33;
    // private static byte textIndex34 = 34;
    private static byte         textIndex35                             = 35;
    
    // private static byte sfield_token255_descoff128_staticref32;
    // private static boolean sfield_token255_descoff135_staticref33;
    // private static short sfield_token255_descoff142_staticref34;
    private static boolean      sfield_token255_descoff149_staticref36;
    private static byte         sfield_token255_descoff163_staticref38;
    private static byte         sfield_token255_descoff170_staticref39;
    private static boolean      sfield_token255_descoff177_staticref40;
    private static boolean      sfield_token255_descoff184_staticref41;
    private static boolean      sfield_token255_descoff191_staticref42;
    private static byte         sfield_token255_descoff198_staticref43;
    private static byte         sfield_token255_descoff205_staticref44;
    private static byte         sfield_token255_descoff212_staticref45;
    // private static boolean sfield_token255_descoff219_staticref46;
    // private static byte sfield_token255_descoff226_staticref47;
    // private static byte sfield_token255_descoff233_staticref48;
    private static boolean      sfield_token255_descoff240_staticref49;
    // private static short sfield_token255_descoff247_staticref50;
    // private static short sfield_token255_descoff254_staticref52;
    
    private static byte         sfield_token255_descoff268_staticref56;
    private static short        sfield_token255_descoff275_staticref57  = 4120;                                                                                                                                                                                                                                                                                                                                                                                           // 0x1018
    // private static short sfield_token255_descoff282_staticref59 = 6160; // 0x1810
    private static boolean      sfield_token255_descoff289_staticref61  = true;
    private static boolean      sfield_token255_descoff296_staticref62  = true;
    // private static byte sfield_token255_descoff303_staticref63 = 4;
    private static byte         sfield_token255_descoff310_staticref64  = 2;
    private static byte         sfield_token255_descoff317_staticref65  = 1;
    private static byte         sfield_token255_descoff324_staticref66  = 3;
    private static byte         sfield_token255_descoff331_staticref67  = 1;
    // private static byte sfield_token255_descoff338_staticref68 = 1;
    // private static byte sfield_token255_descoff366_staticref75 = (byte) 255;
    
    // private static byte sfield_token255_descoff618_staticref111 = 1;
    // private static boolean sfield_token255_descoff625_staticref112 = true;
    // private static boolean sfield_token255_descoff632_staticref113 = true;
    private static boolean      sfield_token255_descoff639_staticref114 = true;
    // private static short sfield_token255_descoff646_staticref115 = 4;
    // private static short sfield_token255_descoff653_staticref117 = 2;
    private static short        sfield_token255_descoff660_staticref119 = 1;
    private static short        sfield_token255_descoff667_staticref121 = 1;
    
    // private boolean field_token21_descoff821 = false;
    
    private short               field_token23_descoff835                = -6;
    // private boolean boolean1;
    private byte                field_token25_descoff849                = 0;
    private short               field_token26_descoff856                = 0;
    private boolean             field_token27_descoff863;
    private boolean             field_token28_descoff870;
    private byte                field_token29_descoff877                = 0;
    private byte                field_token30_descoff884                = 0;
    private boolean             field_token31_descoff891                = false;
    
    public StkApplet() {
        ToolkitRegistry reg = ToolkitRegistry.getEntry();
        
        try {
            this.byteBuffer = JCSystem.makeTransientByteArray((short) 41, JCSystem.CLEAR_ON_RESET); // short length, byte event
            this.concatMsgMapper = JCSystem.makeTransientBooleanArray((short) 4, JCSystem.CLEAR_ON_RESET);
            this.shortBuffer = JCSystem.makeTransientShortArray((short) 6, JCSystem.CLEAR_ON_RESET);
        } catch (SystemException ex) {}
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(DF_FID);
            simView.select(EF_FID_2);
            simView.readBinary((short) 0, this.byteBuffer, (short) 0, (short) 1);
            
            if (this.byteBuffer[0] > 0 && this.byteBuffer[0] <= 27) {
                simView.readBinary((short) 1, this.byteBuffer, (short) 1, this.byteBuffer[0]);
            } else {
                Util.arrayCopy(hameleonString, (short) 0, this.byteBuffer, (short) 0, (short) (hameleonString[0] + 1));
            }
        } catch (SIMViewException ex) {
            Util.arrayCopy(hameleonString, (short) 0, this.byteBuffer, (short) 0, (short) (hameleonString[0] + 1));
        }
        
        reg.initMenuEntry(this.byteBuffer, (short) 1, this.byteBuffer[0], (byte) 0, false, (byte) 1, (short) 0);
        reg.setEvent(EVENT_UNFORMATTED_SMS_PP_ENV);
        reg.setEvent(EVENT_PROFILE_DOWNLOAD);
        reg.setEvent(EVENT_UNFORMATTED_SMS_CB);
    }
    
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        StkApplet sa = new StkApplet();
        
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
                    if (this.serviceInProgress)
                        return;
                    
                    this.serviceInProgress = true;
                    ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
                    proHandl.init(PRO_CMD_MORE_TIME, (byte) 0, DEV_ID_ME);
                    proHandl.send();
                    this.eventSmsPPDataDownload();
                    this.serviceInProgress = false;
                    break;
                // case EVENT_UNFORMATTED_SMS_CB:
                // if (this.serviceInProgress)
                // return;
                //
                // this.serviceInProgress = true;
                // this.processCellBroadcastPage();
                // this.serviceInProgress = false;
                // break;
                case EVENT_MENU_SELECTION:
                    if (this.serviceInProgress)
                        return;
                    
                    this.serviceInProgress = true;
                    // this.eventMenuSelection();
                    this.serviceInProgress = false;
            }
        } catch (Exception var3) {
            this.serviceInProgress = false;
            this.resetVars();
            // sfield_token255_descoff128_staticref32 = 0;
            // sfield_token255_descoff135_staticref33 = false;
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
    
    // private boolean method_token255_descoff1577(byte[] var1, short var2) {
    // short var3 = Util.getShort(var1, var2);
    // return var3 == sfield_token255_descoff275_staticref57 || var3 == sfield_token255_descoff282_staticref59;
    // }
    
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
    
    // method_token255_descoff1469
    private boolean processBufferAndSendSMS(byte[] buffer, short index, byte var3, byte var4) {
        short offset = 0;
        Vars.itemText1[offset++] = 17; // 0x11
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_TELECOM);
            simView.select(SIMView.FID_EF_SMSS);
            simView.readBinary((short) 0, Vars.itemText1, offset, (short) 1);
        } catch (SIMViewException var11) {
            Vars.itemText1[offset] = 1;
        }
        
        Vars.itemText1[offset] = (byte) (Vars.itemText1[offset] + 1);
        offset++;
        
        short var6 = (short) (buffer[index++] & 255);
        
        offset = Util.arrayCopy(buffer, index, Vars.itemText1, offset, var6);
        index = (short) (index + var6);
        
        Vars.itemText1[offset++] = 0;
        Vars.itemText1[offset++] = var3;
        Vars.itemText1[offset++] = var4;
        
        var6 = (short) buffer[index++];
        
        short savedOffset = offset;
        // if (var3 == 4 && sfield_token255_descoff219_staticref46) {
        // Vars.itemText1[offset++] = 0; // 0x00
        // Vars.itemText1[offset++] = 77; // 0x4D
        // Vars.itemText1[offset++] = 67; // 0x43
        // Vars.itemText1[offset++] = 32; // 0x20
        // Vars.itemText1[offset++] = 2; // 0x02
        // Vars.itemText1[offset++] = (byte) var6;
        // } else {
        Vars.itemText1[offset++] = (byte) var6;
        // }
        
        offset = Util.arrayCopy(buffer, index, Vars.itemText1, offset, var6);
        
        // if (var3 == 4 && sfield_token255_descoff219_staticref46) {
        // Vars.itemText1[offset++] = (byte) (4 + sfield_token255_descoff212_staticref45 * 2);
        // Vars.itemText1[offset++] = 80; // 0x50
        // Vars.itemText1[offset++] = 67; // 0x43
        // Vars.itemText1[offset++] = 32; // 0x20
        // Vars.itemText1[offset++] = sfield_token255_descoff212_staticref45;
        //
        // offset = Util.arrayCopy(Vars.sfield_token255_descoff44_staticref8, (short) 0, Vars.itemText1, offset, (short)
        // (sfield_token255_descoff212_staticref45 * 2));
        // }
        
        Vars.itemText1[savedOffset] = (byte) (offset - savedOffset - 1);
        
        sfield_token255_descoff268_staticref56 = this.processBufferMessageFuck(Vars.itemText1, (byte) (savedOffset + 1), Vars.itemText1[savedOffset]);
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        // qualifier
        // bit 1:
        // 0 = packing not required;
        // 1 = SMS packing by the ME required.
        // bits 2 to 8: = 0 RFU.
        byte qualifier = (byte) (var3 == 0 ? 1 : 0);
        proHandl.init(PRO_CMD_SEND_SHORT_MESSAGE, qualifier, DEV_ID_NETWORK);
        proHandl.appendTLV(TAG_SMS_TPDU, Vars.itemText1, (short) 0, offset);
        byte result = proHandl.send();
        if (result == RES_CMD_PERF) {
            // sfield_token255_descoff219_staticref46 = false;
            
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
        var9 = (short) EXTENDED_BUFFER[var10001];
        if ((short) var9 >= 1) {
            for (var11 = (short) 0; (short) var11 != (short) var2; var11 = (short) ((byte) ((short) ((short) var11 + 1)))) {
                var10 = (short) 0;
                if ((short) var11 == 127) {
                    var11 = (short) 0;
                }
                
                var7 = (short) ((short) ((short) var3 + 1));
                
                for (short var14 = (short) 0; (short) var14 < (short) var9; var14 = (short) ((byte) ((short) ((short) var14 + 1)))) {
                    this.booleanFlag2 = (short) var14 == (short) ((short) var9 - 1);
                    var10001 = (short) var7;
                    var7 = (short) ((short) ((short) var7 + 1));
                    var12 = (short) EXTENDED_BUFFER[var10001];
                    Util.arrayCopy(EXTENDED_BUFFER, (short) var7, this.byteBuffer, (short) 0, (short) var12);
                    if (this.field_token25_descoff849 != 2 && this.field_token25_descoff849 != 4) {
                        var6 = (short) Util.makeShort((byte) 0, this.byteBuffer[(short) ((short) var12 - 1)]);
                    } else {
                        var6 = (short) Util.getShort(Vars.sfield_token255_descoff58_staticref12, (short) (622 + (short) ((short) var10 * 2)));
                        var10 = (short) ((byte) ((short) ((short) var10 + 1)));
                    }
                    
                    var7 = (short) ((short) ((short) var7 + (short) var12));
                    if (var5 || this.field_token31_descoff891) {
                        this.method_token255_descoff1529(var1, EXTENDED_BUFFER, (short) var7, (short) var6);
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
                        displayTextRes = this.displayText((byte) ((short) var8), EXTENDED_BUFFER, (short) var7, (short) var6 > 159 ? 158 : (short) var6);
                    } else {
                        displayTextRes = this.displayText((byte) ((short) var8), EXTENDED_BUFFER, (short) var7, (short) var6 > 159 ? 159 : (short) var6);
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
                    sfield_token255_descoff324_staticref66 = 3;
                    this.field_token23_descoff835 = -7;
                }
                break;
            case RES_CMD_PERF_SESSION_TERM_USER:
            case RES_CMD_PERF_BACKWARD_MOVE_REQ:
                sfield_token255_descoff324_staticref66 = 3;
                this.field_token23_descoff835 = -7;
                break;
            default:
                this.field_token23_descoff835 = -7;
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
                this.userDataFirstBateSaved = -1;
                
                sfield_token255_descoff324_staticref66 = 2;
                
                this.processBufferAndSendSMS(message, index, (byte) 4, (byte) 0);
                break;
            case 2: // 0x02
            case 3: // 0x03
                this.sendUssd(message, index);
                break;
            case 16: // 0x10
                this.sendCall(message, index);
                break;
            case 32: // 0x20
                this.processBufferAndSendSMS(message, index, (byte) 0, (byte) 0);
                break;
            case 64: // 0x40
                boolean var5 = this.method_token255_descoff1517(Vars.sfield_token255_descoff121_staticref30, this.shortBuffer[0], this.shortBuffer[4], true);
                if (var5) {
                    this.readAndDisplayText(textIndex4);
                }
                break;
            case 80: // 0x50
                boolean result = this.getInput(EXTENDED_BUFFER, (short) 552);
                if (result) {
                    this.composeAndSendSMS(message, index, EXTENDED_BUFFER, (short) 552);
                }
                break;
            case 96: // 0x60
                this.userDataFirstBateSaved = -1;
                byte textLength = (byte) 127;
                
                if (this.askInputTextFromSim(this.field_token25_descoff849 != 0, textLength)) {
                    sfield_token255_descoff324_staticref66 = 2;
                    this.composeAndSendSMS(message, index, EXTENDED_BUFFER, (short) 552);
                }
        }
        
        return ret;
    }
    
    private short method_token255_descoff917(byte[] var1, short var2) {
        if (!booleanFlag2) {
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
        short var4 = (short) 0;
        
        for (var4 = (short) 1; (short) var4 < (byte) ((short) ((short) (sfield_token255_descoff198_staticref43 * 2) + 1)) && (short) var4 < (byte) ((short) (Vars.sfield_token255_descoff72_staticref16.length - 1)); var4 = (short) ((byte) ((short) ((short) var4 + 2)))) {
            if (Vars.sfield_token255_descoff72_staticref16[(short) var4] == (short) var1) {
                return Vars.sfield_token255_descoff72_staticref16[(short) ((short) var4 + 1)];
            }
        }
        
        return (short) -1;
    }
    
    private void method_token255_descoff965(short var2, short var4) {
        short var7 = 0;
        
        byte var9 = (byte) (EXTENDED_BUFFER[var4++] = EXTENDED_BUFFER[var2++]);
        
        for (byte i = 0; i < var9; i++) {
            byte var11 = EXTENDED_BUFFER[var2++];
            
            Util.arrayCopy(EXTENDED_BUFFER, var2, this.byteBuffer, (short) 0, (short) var11);
            byte var10 = this.byteBuffer[0];
            short var8 = (short) ((short) (this.byteBuffer[(byte) (var11 - 1)] & 255));
            EXTENDED_BUFFER[var4++] = var11;
            var2 = (short) (var2 + var11 - 1);
            var7 = this.method_token255_descoff977(var2, EXTENDED_BUFFER, (short) (var4 + var11));
            Util.setShort(Vars.sfield_token255_descoff58_staticref12, (short) (622 + (i * 2)), var7);
            var2 = (short) (var2 + var8 + 1);
            
            this.byteBuffer[0] = var10;
            this.byteBuffer[(byte) (var11 - 1)] = (byte) var7;
            
            Util.arrayCopy(this.byteBuffer, (short) 0, Vars.sfield_token255_descoff58_staticref12, var4, (short) var11);
            var4 = (short) (var4 + var11 + var7);
        }
    }
    
    private short method_token255_descoff977(short var2, byte[] buffer, short var4) {
        
        short endIndex = 0;
        short i = 0;
        short var7 = 0;
        short var8 = 0;
        short var10 = 0;
        short var11 = (short) (this.byteBuffer.length / 2);
        short var13 = 127;
        short var14 = -128;
        short var9 = this.field_token26_descoff856;
        
        short startIndex = Util.makeShort((byte) 0, EXTENDED_BUFFER[var2++]);
        
        for (i = startIndex; i > endIndex;) {
            
            var4 = Util.arrayCopy(this.byteBuffer, (short) 0, buffer, var4, var7); // return var4+var7
            
            if (i - endIndex > var11)
                var8 = var11;
            else
                var8 = (short) (i - endIndex);
            
            Util.arrayCopy(EXTENDED_BUFFER, var2, this.byteBuffer, var8, var8);
            
            var2 = (short) (var2 + var8);
            endIndex = (short) (endIndex + var8);
            var7 = 0;
            
            for (short j = var8; j < (short) (var8 * 2); j++) {
                if ((this.byteBuffer[j] & var14) == 0) {
                    this.byteBuffer[var7++] = 0;
                    this.byteBuffer[var7++] = this.byteBuffer[j];
                    
                } else {
                    var10 = (short) (this.byteBuffer[j] & var13);
                    var10 = (short) (var10 + var9);
                    Util.setShort(this.byteBuffer, var7, var10);
                    var7 = (short) (var7 + 2);
                }
            }
        }
        
        return (short) (i * 2);
    }
    
    private boolean sendImeiBySMSToUser() {
        this.buildImeiAndVersionBuffer();
        
        if (this.processBufferAndSendSMS(EXTENDED_BUFFER, (short) 0, (byte) 4, (byte) -76)) {
            // sfield_token255_descoff135_staticref33 = true;
            // sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
            sfield_token255_descoff240_staticref49 = false;
            return true;
        } else {
            return false;
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
    
    private boolean method_token255_descoff1517(byte[] var1, short var2, short var3, boolean var4) {
        short var5 = (short) (var1 == Vars.sfield_token255_descoff114_staticref28 ? sfield_token255_descoff660_staticref119 : sfield_token255_descoff667_staticref121);
        short var6 = (short) Vars.sfield_token255_descoff58_staticref12[600];
        short var7 = (short) ((short) ((short) ((short) ((short) ((short) var3 + (short) var6) + 1) + 2) - (short) (577 - (short) var5)));
        if ((short) var7 > 0) {
            if (var4 && this.readAndDisplayText(textIndex35) != RES_CMD_PERF) {
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
        Util.setShort(var1, (short) (var5 + var6 + 1), var3);
        Util.arrayCopyNonAtomic(EXTENDED_BUFFER, (short) var2, var1, (short) ((short) ((short) ((short) var5 + (short) var6) + 1) + 2), (short) var3);
        var5 = (short) ((short) ((short) var5 + (short) ((short) ((short) ((short) var6 + (short) var3) + 1) + 2)));
        if (var1 == Vars.sfield_token255_descoff114_staticref28) {
            sfield_token255_descoff660_staticref119 = (short) var5;
        } else {
            sfield_token255_descoff667_staticref121 = (short) var5;
        }
        
        var1[0] = (byte) ((short) (var1[0] + 1));
        return true;
    }
    
    // EnvelopeHandler envHandl = EnvelopeHandler.getTheHandler();
    
    private void method_token255_descoff1565(boolean var1) {
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(SIMView.FID_EF_CBMID, this.byteBuffer, (short) 0, (short) 4); // Cell Broadcast Message Identifier for Data Download
            
            short len = (short) this.byteBuffer[3];
            simView.readBinary((short) 0, this.byteBuffer, (short) 0, (short) len);
            
            if (var1) {
                sfield_token255_descoff289_staticref61 = true;
                short var3 = (short) 0;
                
                for (short i = (short) (len - 2); i >= 0; i = (short) (i - 2)) {
                    if (Util.getShort(this.byteBuffer, i) == -1) {
                        var3 = i;
                    } else if (Util.getShort(this.byteBuffer, i) == sfield_token255_descoff275_staticref57) {
                        return;
                    }
                }
                
                Util.setShort(this.byteBuffer, (short) var3, sfield_token255_descoff275_staticref57);
            } else {
                sfield_token255_descoff289_staticref61 = false;
                
                for (short i = (short) (len - 2); i >= 0; i = (short) (i - 2)) {
                    if (Util.getShort(this.byteBuffer, i) == sfield_token255_descoff275_staticref57) {
                        Util.setShort(this.byteBuffer, i, (short) -1);
                    }
                }
            }
            
            simView.updateBinary((short) 0, this.byteBuffer, (short) 0, len);
        } catch (SIMViewException ex) {}
        
    }
    
    private byte processBufferMessageFuck(byte[] buffer, byte offset, byte len) {
        byte result = 0;
        
        for (byte i = 0; i < len; i++) {
            byte valueOfBuffer = buffer[offset + i];
            
            for (byte j = 0; j < 8; j++) {
                if ((byte) (valueOfBuffer & 1) != 0) {
                    valueOfBuffer = (byte) ((valueOfBuffer >> 1) ^ 93);
                } else {
                    valueOfBuffer = (byte) (valueOfBuffer >> 1);
                }
            }
            
            result = (byte) (result ^ valueOfBuffer);
        }
        
        result = (byte) (result | -128);
        if ((result & -16) == -128) {
            result = (byte) (result | 16);
        }
        
        return result;
    }
    
    /*
     * DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS
     * DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS
     * DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS
     * DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS *** DIFF METHODS
     * 
     */
    
    private void firstInit() {
        short offset = 0;
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(DF_FID);
            simView.select(EF_FID_1);
            
            simView.readBinary(offset++, this.byteBuffer, (short) 0, (short) 1);
            if (this.byteBuffer[0] == 0) {
                return;
            }
            
            simView.readBinary(offset++, this.byteBuffer, (short) 0, (short) 1);
            byte len = this.byteBuffer[0];
            
            for (short i = 0; i < len; i++) {
                simView.readBinary(offset, this.byteBuffer, (short) 0, (short) 1);
                
                short var4 = (short) (this.byteBuffer[0] + 1);
                if (i == 0 && var4 < 12) {
                    simView.readBinary(offset, bufferForNumbers, (short) 13, var4);
                    offset = (short) (offset + 13);
                }
                
                if (i == 1 && var4 == 3) {
                    simView.readBinary((short) (offset + 1), EXTENDED_BUFFER, (short) 0, (short) 2);
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(false);
                    }
                    
                    short var6 = (short) ((short) ((short) (EXTENDED_BUFFER[0] << 8) & -256));
                    short var7 = (short) (EXTENDED_BUFFER[1] & 255);
                    sfield_token255_descoff275_staticref57 = (short) (var6 | var7);
                    // sfield_token255_descoff282_staticref59 = (short) ((var7 << 8) & -256);
                    // sfield_token255_descoff282_staticref59 |= (short) ((var6 >> 8) & 255);
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(true);
                    }
                    
                    simView.select(SIMView.FID_MF);
                    simView.select(SIMView.FID_DF_GSM);
                    simView.select(DF_FID);
                    simView.select(EF_FID_1);
                    offset = (short) (offset + 3);
                }
                
                simView.readBinary(offset, this.byteBuffer, (short) 0, (short) 1);
                var4 = (short) (this.byteBuffer[0] + 1);
                if (i == 0 && var4 <= 13) {
                    simView.readBinary(offset, bufferForNumbers, (short) 0, (short) var4);
                    offset = (short) (offset + 13);
                }
                
                if (i == 1 && var4 < 11) {
                    simView.readBinary(offset, bufferForNumbers, (short) 26, (short) var4);
                    offset = (short) (offset + 12);
                }
            }
            
            offset = 74;
            
            simView.readBinary(offset++, this.byteBuffer, (short) 0, (short) 1);
            dcs = this.byteBuffer[0];
            
            simView.readBinary(offset++, this.byteBuffer, (short) 0, (short) 1);
            len = this.byteBuffer[0];
            
            for (short i = 0; i < len; i++) {
                simView.readBinary(offset, this.byteBuffer, (short) 0, (short) 1);
                if (i < 36) {
                    Vars.shortHolder1[i] = offset;
                }
                
                offset = (short) (offset + Util.makeShort((byte) 0, this.byteBuffer[0]) + 1);
            }
            
            simView.readBinary(offset, this.byteBuffer, (short) 0, (short) 1);
            len = this.byteBuffer[0];
            Vars.sfield_token255_descoff72_staticref16[0] = offset;
            offset++;
            sfield_token255_descoff198_staticref43 = 0;
            
            for (short i = 0; i < len; i++) {
                simView.readBinary(offset, this.byteBuffer, (short) 0, (short) 2);
                
                if ((short) (i * 2 + 1) < (short) (Vars.sfield_token255_descoff72_staticref16.length - 1)) {
                    Vars.sfield_token255_descoff72_staticref16[(short) (i * 2 + 1)] = this.byteBuffer[0];
                    Vars.sfield_token255_descoff72_staticref16[(short) (i * 2 + 1 + 1)] = (short) (offset + 1);
                    sfield_token255_descoff198_staticref43 = (byte) (sfield_token255_descoff198_staticref43 + 1);
                }
                
                offset = (short) (offset + this.byteBuffer[1] + 2);
            }
            
            this.byteBuffer[0] = 0;
            simView.updateBinary((short) 0, this.byteBuffer, (short) 0, (short) 1);
        } catch (SIMViewException ex) {}
    }
    
    private void profileDownload() {
        // sfield_token255_descoff128_staticref32 = 0;
        this.serviceInProgress = false;
        
        this.firstInit();
        // this.resetCbsMessageIdBuffer();
        this.resetVars();
        
        sfield_token255_descoff296_staticref62 = sfield_token255_descoff289_staticref61;
        // sfield_token255_descoff135_staticref33 = false;
        sfield_token255_descoff324_staticref66 = 3;
        if (sfield_token255_descoff639_staticref114) {
            if (!sfield_token255_descoff240_staticref49) {
                this.getImeiFromME();
                sfield_token255_descoff240_staticref49 = true;
                
                if (Util.arrayCompare(imeiBuffer, (short) 0, imeiBufferToCompare, (short) 0, (short) 8) != 0) {
                    sfield_token255_descoff317_staticref65 = sfield_token255_descoff310_staticref64;
                    Util.arrayCopy(imeiBuffer, (short) 0, imeiBufferToCompare, (short) 0, (short) 8);
                    sfield_token255_descoff310_staticref64 = 2;
                    sfield_token255_descoff177_staticref40 = false;
                    // if (sfield_token255_descoff632_staticref113) {
                    // sfield_token255_descoff632_staticref113 = false;
                    // sfield_token255_descoff247_staticref50 = sfield_token255_descoff254_staticref52 = sfield_token255_descoff646_staticref115;
                    // }
                    
                    this.method_token255_descoff1565(true);
                } else {
                    sfield_token255_descoff240_staticref49 = false;
                    // sfield_token255_descoff632_staticref113 = true;
                }
            } else {
                sfield_token255_descoff240_staticref49 = false;
            }
        }
    }
    
    // private void eventMenuSelection() {
    // byte result = RES_CMD_PERF;
    // boolean var4 = true;
    // this.field_token21_descoff821 = false;
    // boolean var5 = sfield_token255_descoff177_staticref40 && sfield_token255_descoff310_staticref64 != 3 ? false : true;
    //
    // while (var4) {
    // short var7 = 0;
    //
    // short offset = 0;
    // short var9;
    // // short var10001;
    // try {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex17], EXTENDED_BUFFER, var7, false));
    // var9 = var7;
    // var7++;
    //
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex27], EXTENDED_BUFFER, var7, true));
    // EXTENDED_BUFFER[var7++] = 1;
    // offset++;
    // if (!var5) {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex25], EXTENDED_BUFFER, var7, true));
    // EXTENDED_BUFFER[var7++] = 2;
    // offset++;
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex26], EXTENDED_BUFFER, var7, true));
    // EXTENDED_BUFFER[var7++] = 3;
    // offset++;
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex20], EXTENDED_BUFFER, var7, true));
    // EXTENDED_BUFFER[var7++] = 4;
    // offset++;
    // if (sfield_token255_descoff184_staticref41) {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex28], EXTENDED_BUFFER, var7, true));
    // EXTENDED_BUFFER[var7++] = 5;
    // offset++;
    // }
    // }
    //
    // EXTENDED_BUFFER[var9] = (byte) offset;
    // } catch (SIMViewException var16) {
    // return;
    // }
    //
    // byte selectedItem = this.menuSelectItem(EXTENDED_BUFFER, (byte) 0);
    // result = RES_CMD_PERF;
    // switch (selectedItem) {
    // case 1:
    // if (!sfield_token255_descoff177_staticref40) {
    // result = this.readAndDisplayText(textIndex12);
    // if (result == RES_CMD_PERF) {
    // this.userDataFirstBateSaved = -1;
    // sfield_token255_descoff324_staticref66 = 2;
    // // sfield_token255_descoff128_staticref32 = 1;
    // sfield_token255_descoff625_staticref112 = !sfield_token255_descoff625_staticref112;
    // sfield_token255_descoff317_staticref65 = 1;
    // sfield_token255_descoff149_staticref36 = true;
    // if (sfield_token255_descoff625_staticref112) {
    // this.getImeiFromME();
    // Util.arrayCopy(imeiBuffer, (short) 0, imeiBufferToCompare, (short) 0, (short) 8);
    // }
    //
    // if (this.sendImeiBySMSToUser()) {
    // sfield_token255_descoff625_staticref112 = false;
    // var4 = false;
    // } else {
    // result = this.readAndDisplayText(textIndex34);
    // }
    // }
    //
    // if (result == RES_CMD_PERF_SESSION_TERM_USER) {
    // var4 = false;
    // }
    // } else {
    // do {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex27], EXTENDED_BUFFER, var7, false));
    // EXTENDED_BUFFER[var7++] = 2;
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex19], EXTENDED_BUFFER, var7, true));
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex18], EXTENDED_BUFFER, var7, true));
    // selectedItem = this.menuSelectItem(EXTENDED_BUFFER, (byte) 9);
    // if (selectedItem == 9) {
    // // sfield_token255_descoff135_staticref33 = false;
    // sfield_token255_descoff142_staticref34 = 0;
    // if (sfield_token255_descoff310_staticref64 != 1) {
    // if (sfield_token255_descoff310_staticref64 == 3) {
    // result = this.readAndDisplayText(textIndex11);
    // } else {
    // sfield_token255_descoff310_staticref64 = 1;
    // this.method_token255_descoff1565(true);
    // if (!sfield_token255_descoff296_staticref62) {
    // result = this.readAndDisplayText(textIndex29);
    // } else {
    // result = this.readAndDisplayText(textIndex1);
    // }
    //
    // offset = Util.arrayCopy(bufferForNumbers, (short) 0, EXTENDED_BUFFER, offset, (short) (bufferForNumbers[0] + 1));
    // EXTENDED_BUFFER[offset++] = 2;
    // EXTENDED_BUFFER[offset++] = 65;
    // EXTENDED_BUFFER[offset++] = 67;
    // this.processBufferAndSendSMS(EXTENDED_BUFFER, (short) 0, (byte) 4, (byte) -76);
    // }
    // } else {
    // result = this.readAndDisplayText(textIndex1);
    // }
    // } else if (selectedItem == 10) {
    // if (sfield_token255_descoff310_staticref64 == 1) {
    // sfield_token255_descoff310_staticref64 = 2;
    // // sfield_token255_descoff135_staticref33 = false;
    // this.method_token255_descoff1565(false);
    // result = this.readAndDisplayText((byte) 0);
    // offset = (short) Util.arrayCopy(bufferForNumbers, (short) 0, EXTENDED_BUFFER, (short) offset, (short) (bufferForNumbers[0] + 1));
    // EXTENDED_BUFFER[offset++] = 2;
    // EXTENDED_BUFFER[offset++] = 68;
    // EXTENDED_BUFFER[offset++] = 65;
    // this.processBufferAndSendSMS(EXTENDED_BUFFER, (short) 0, (byte) 4, (byte) -76);
    // } else if (sfield_token255_descoff310_staticref64 == 3) {
    // result = this.readAndDisplayText(textIndex11);
    // } else {
    // result = this.readAndDisplayText((byte) 0);
    // }
    // }
    // }
    // while (selectedItem > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
    // }
    //
    // if (selectedItem != USER_REQUEST_MOVE_BACK) {
    // var4 = false;
    // }
    // break;
    // case 2:
    // result = this.readAndDisplayText(textIndex16);
    // if (result == RES_CMD_PERF) {
    // offset = (short) Util.arrayCopy(bufferForNumbers, (short) 0, EXTENDED_BUFFER, (short) offset, (short) (bufferForNumbers[0] + 1));
    // EXTENDED_BUFFER[offset++] = 2;
    // EXTENDED_BUFFER[offset++] = 80;
    // EXTENDED_BUFFER[offset++] = 82;
    // if (this.processBufferAndSendSMS(EXTENDED_BUFFER, (short) 0, (byte) 4, (byte) 0)) {
    // this.userDataFirstBateSaved = -1;
    // // sfield_token255_descoff135_staticref33 = true;
    // sfield_token255_descoff142_staticref34 = sfield_token255_descoff303_staticref63;
    // sfield_token255_descoff324_staticref66 = 2;
    // // sfield_token255_descoff128_staticref32 = 1;
    // var4 = false;
    // }
    // } else if ((short) result != RES_CMD_PERF_BACKWARD_MOVE_REQ) {
    // var4 = false;
    // }
    // break;
    // case 3:
    // if (sfield_token255_descoff212_staticref45 == 0) {
    // result = this.readAndDisplayText(textIndex13);
    // selectedItem = USER_REQUEST_MOVE_BACK;
    // } else {
    // do {
    // try {
    // offset = 0;
    // var7 = 0;
    // SIMView var11 = SIMSystem.getTheSIMView();
    // var11.select(SIMView.FID_MF);
    // var11.select(SIMView.FID_DF_GSM);
    // var11.select(DF_FID);
    // var11.select(EF_FID_1);
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex26], EXTENDED_BUFFER, var7, false));
    // EXTENDED_BUFFER[var7++] = sfield_token255_descoff212_staticref45;
    //
    // for (short i = 0; i < sfield_token255_descoff212_staticref45; i++) {
    // var9 = var7;
    // var7 = Util.arrayCopy(Vars.sfield_token255_descoff51_staticref10, (short) (offset + 1), EXTENDED_BUFFER, var7, (short)
    // (Vars.sfield_token255_descoff51_staticref10[(short) (offset + 1)] + 1));
    // short var10;
    // if (this.method_token255_descoff1049(Vars.sfield_token255_descoff51_staticref10[offset]) == 0) {
    // var10 = this.readTextFromSim(Vars.shortHolder1[textIndex15], Vars.itemText1, (short) 0, true);
    // } else {
    // var10 = this.readTextFromSim(Vars.shortHolder1[textIndex14], Vars.itemText1, (short) 0, true);
    // }
    //
    // var10--;
    // var7 = Util.arrayCopy(Vars.itemText1, (short) 1, EXTENDED_BUFFER, var7, var10);
    // EXTENDED_BUFFER[var9] = (byte) (EXTENDED_BUFFER[var9] + var10);
    // offset = (short) (offset + Vars.sfield_token255_descoff51_staticref10[(short) (offset + 1)] + 2);
    // }
    // } catch (SIMViewException var15) {
    // return;
    // }
    //
    // selectedItem = this.menuSelectItem(EXTENDED_BUFFER, (byte) 1);
    // if (selectedItem > 0) {
    // sfield_token255_descoff219_staticref46 = true;
    // if (Vars.sfield_token255_descoff44_staticref8[(short) (((selectedItem - 1) * 2) + 1)] == 1) {
    // Vars.sfield_token255_descoff44_staticref8[(short) (((selectedItem - 1) * 2) + 1)] = 0;
    // result = this.readAndDisplayText(textIndex30);
    // } else {
    // Vars.sfield_token255_descoff44_staticref8[(short) (((selectedItem - 1) * 2) + 1)] = 1;
    // result = this.readAndDisplayText(textIndex31);
    // }
    // }
    // }
    // while (selectedItem > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
    // }
    //
    // if (selectedItem != USER_REQUEST_MOVE_BACK || result == RES_CMD_PERF_SESSION_TERM_USER) {
    // var4 = false;
    // }
    // break;
    // case 4:
    // byte[] var18 = Vars.sfield_token255_descoff114_staticref28;
    //
    // while (selectedItem > 0 && result != RES_CMD_PERF_SESSION_TERM_USER) {
    // SIMView simView;
    // try {
    // var7 = (short) 0;
    // simView = SIMSystem.getTheSIMView();
    // simView.select(SIMView.FID_MF);
    // simView.select(SIMView.FID_DF_GSM);
    // simView.select(DF_FID);
    // simView.select(EF_FID_1);
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex20], EXTENDED_BUFFER, (short) var7, false));
    // EXTENDED_BUFFER[var7++] = 2;
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex33], EXTENDED_BUFFER, (short) var7, true));
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex32], EXTENDED_BUFFER, (short) var7, true));
    // } catch (SIMViewException var14) {
    // return;
    // }
    //
    // selectedItem = this.menuSelectItem(EXTENDED_BUFFER, (byte) 13);
    // if (selectedItem == 13) {
    // var18 = Vars.sfield_token255_descoff114_staticref28;
    // } else if (selectedItem == 14) {
    // var18 = Vars.sfield_token255_descoff121_staticref30;
    // }
    //
    // if (selectedItem > 0) {
    // byte var1 = selectedItem;
    // short var2 = 0;
    //
    // while (selectedItem > 0 && result != RES_CMD_PERF_SESSION_TERM_USER) {
    // try {
    // var7 = 0;
    // simView = SIMSystem.getTheSIMView();
    // simView.select(SIMView.FID_MF);
    // simView.select(SIMView.FID_DF_GSM);
    // simView.select(DF_FID);
    // simView.select(EF_FID_1);
    // if (selectedItem == 13) {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex33], EXTENDED_BUFFER, (short) var7, false));
    // } else {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex32], EXTENDED_BUFFER, (short) var7, false));
    // }
    //
    // EXTENDED_BUFFER[var7++] = 2;
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex22], EXTENDED_BUFFER, (short) var7, true));
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex23], EXTENDED_BUFFER, (short) var7, true));
    // } catch (SIMViewException var13) {
    // return;
    // }
    //
    // selectedItem = this.menuSelectItem(EXTENDED_BUFFER, (byte) 7);
    // var2 = selectedItem;
    // if (selectedItem > 0) {
    // do {
    // if (var18[0] == 0) {
    // result = this.readAndDisplayText(textIndex6);
    // selectedItem = var1;
    // break;
    // }
    //
    // selectedItem = this.menuSelectItemAdd(var18);
    // if (selectedItem > 0) {
    // switch (var2) {
    // case 7:
    // var9 = (short) sfield_token255_descoff324_staticref66;
    // sfield_token255_descoff324_staticref66 = 4;
    // var2 = Util.getShort(var18, selectedItem);
    // Util.arrayCopyNonAtomic(var18, (short) (selectedItem + 2), EXTENDED_BUFFER, (short) 0, var2);
    // this.shortBuffer[0] = 0;
    // this.function_DO_1((byte) 3, true);
    // if (this.field_token21_descoff821) {
    // selectedItem = USER_REQUEST_TERMINATE;
    // }
    //
    // var2 = 7;
    // sfield_token255_descoff324_staticref66 = (byte) var9;
    // break;
    // case 8:
    // if (this.method_token255_descoff989(var18, selectedItem, true)) {
    // result = this.readAndDisplayText(textIndex5);
    // }
    // }
    // }
    // }
    // while (selectedItem > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
    //
    // if (selectedItem == USER_REQUEST_MOVE_BACK) {
    // selectedItem = var1;
    // }
    // }
    // }
    //
    // if (selectedItem == USER_REQUEST_MOVE_BACK) {
    // selectedItem = (short) 1;
    // }
    // }
    // }
    //
    // if (selectedItem != USER_REQUEST_MOVE_BACK || result == RES_CMD_PERF_SESSION_TERM_USER) {
    // var4 = false;
    // }
    // break;
    // case 5:
    // do {
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex28], EXTENDED_BUFFER, (short) var7, false));
    // EXTENDED_BUFFER[var7++] = 2;
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex19], EXTENDED_BUFFER, (short) var7, true));
    // var7 = (short) (var7 + this.readTextFromSim(Vars.shortHolder1[textIndex18], EXTENDED_BUFFER, (short) var7, true));
    // selectedItem = this.menuSelectItem(EXTENDED_BUFFER, (byte) 11);
    // if (selectedItem == 11) {
    // isAllowPlaySound = true;
    // playTone();
    // result = this.readAndDisplayText(textIndex9);
    // } else if (selectedItem == 12) {
    // isAllowPlaySound = false;
    // result = this.readAndDisplayText(textIndex10);
    // }
    // }
    // while (selectedItem > 0 && result != RES_CMD_PERF_SESSION_TERM_USER);
    //
    // if (selectedItem != USER_REQUEST_MOVE_BACK || result == RES_CMD_PERF_SESSION_TERM_USER) {
    // var4 = false;
    // }
    // break;
    // default:
    // var4 = false;
    // }
    // }
    //
    // }
    
    private byte displayText(byte dcs, byte[] text, short offset, short length) {
        ProactiveHandler proHandlr = ProactiveHandler.getTheHandler();
        proHandlr.initDisplayText((byte) 0x80, dcs, text, offset, length); // byte qualifier, byte dcs, byte[] buffer, short offset, short length
        return proHandlr.send();
    }
    
    private boolean getInput(byte[] dstBuffer, short dstOffset) {
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        this.readTextFromSim(Vars.shortHolder1[textIndex3], Vars.itemText1, (short) 0, false);
        
        // byte qualifier, byte dcs, byte[] buffer, short offset, short length, short minRespLength, short maxRespLength
        proHandl.initGetInput((byte) 0, dcs, Vars.itemText1, (short) 1, Vars.itemText1[0], (short) 1, (short) 19);
        byte result = proHandl.send();
        if (result != RES_CMD_PERF) {
            return false;
        } else {
            ProactiveResponseHandler proRespHandl = ProactiveResponseHandler.getTheHandler();
            short len = proRespHandl.getTextStringLength();
            dstBuffer[dstOffset] = (byte) (len + 1);
            dstBuffer[dstOffset++] = 32; // 0x20
            proRespHandl.copyTextString(dstBuffer, dstOffset++);
            return true;
        }
    }
    
    private boolean askInputTextFromSim(boolean isCyrilic, byte maxRespLength) {
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
            
            EXTENDED_BUFFER[offset++] = (byte) (textStringLen + 1);
            EXTENDED_BUFFER[offset++] = 32;
            
            proHandlResp.copyTextString(EXTENDED_BUFFER, offset);
            return true;
        }
    }
    
    // private byte menuSelectItem(byte[] buffer, byte startIndex) {
    // short bufferOffset = 0;
    // short var4 = (short) 9;
    //
    // try {
    // ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
    // proHandl.init(ToolkitConstants.PRO_CMD_SELECT_ITEM, (byte) 0x00, ToolkitConstants.DEV_ID_ME);
    // proHandl.appendTLV(ToolkitConstants.TAG_ALPHA_IDENTIFIER, buffer, (short) (bufferOffset + 1), buffer[bufferOffset]);
    //
    // bufferOffset = (short) (bufferOffset + buffer[bufferOffset] + 1);
    // short endIndex = (short) (buffer[bufferOffset++] + startIndex);
    //
    // for (short i = startIndex; i < endIndex; i++) {
    // var4 = (short) (var4 + 3 + buffer[bufferOffset]);
    // if (var4 <= 255) {
    // if (startIndex != 0) {
    // // byte tag, byte value1, byte[] value2, short value2Offset, short value2Length
    // proHandl.appendTLV(ToolkitConstants.TAG_ITEM, (byte) i, buffer, (short) (bufferOffset + 1), buffer[bufferOffset]);
    // } else {
    // if (i != startIndex) {
    // bufferOffset++;
    // }
    //
    // // byte tag, byte value1, byte[] value2, short value2Offset, short value2Length
    // proHandl.appendTLV(ToolkitConstants.TAG_ITEM, buffer[(short) (bufferOffset + 1 + buffer[bufferOffset])], buffer, (short) (bufferOffset + 1),
    // buffer[bufferOffset]);
    // }
    // }
    //
    // bufferOffset = (short) (bufferOffset + buffer[bufferOffset] + 1);
    // }
    //
    // proHandl.send();
    //
    // ProactiveResponseHandler proRespHandl = ProactiveResponseHandler.getTheHandler();
    // byte proResult = proRespHandl.getGeneralResult();
    // if (proResult == RES_CMD_PERF) {
    // return proRespHandl.getItemIdentifier();
    // } else if (proResult == RES_CMD_PERF_BACKWARD_MOVE_REQ) {
    // return USER_REQUEST_MOVE_BACK;
    // } else if (proResult == RES_CMD_PERF_NO_RESP_FROM_USER) {
    // return USER_REQUEST_NO_RESPONSE;
    // } else if (proResult == RES_CMD_PERF_SESSION_TERM_USER) {
    // return USER_REQUEST_TERMINATE;
    // } else {
    // return USER_REQUEST_UNKNOWN_ERROR;
    // }
    // } catch (Exception ex) {
    // return USER_REQUEST_UNKNOWN_ERROR;
    // }
    // }
    
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
    
    private byte readAndDisplayText(byte index) {
        // initDisplayText qualifier (0x80 = 1000 0000)
        // bit 1:
        // 0 = normal priority;
        // 1 = high priority.
        // bits 2 to 7: = RFU.
        // bit 8:
        // 0 = clear message after a delay;
        // 1 = wait for user to clear message.
        
        short fileOffset = Vars.shortHolder1[index];
        
        SIMView simView = SIMSystem.getTheSIMView();
        simView.select(SIMView.FID_MF);
        simView.select(SIMView.FID_DF_GSM);
        simView.select(DF_FID);
        simView.select(EF_FID_1);
        simView.readBinary(fileOffset, this.byteBuffer, (short) 0, (short) 1);
        
        short length = Util.makeShort((byte) 0, this.byteBuffer[0]);
        simView.readBinary(fileOffset, Vars.itemText1, (short) 0, (short) (length + 1));
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.initDisplayText((byte) 0x80, dcs, Vars.itemText1, (short) 1, length);
        return proHandl.send();
    }
    
    private void composeAndSendSMS(byte[] message, short messageOffset, byte[] buffer, short bufferOffset) {
        short offset = 0;
        short savedOffset = 0;
        Vars.itemText1[offset++] = 17;
        
        try {
            SIMView simView = SIMSystem.getTheSIMView();
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_TELECOM);
            simView.select(SIMView.FID_EF_SMSS);
            simView.readBinary((short) 0, Vars.itemText1, offset, (short) 1);
        } catch (SIMViewException ex) {
            Vars.itemText1[offset] = 1;
        }
        
        Vars.itemText1[offset] = (byte) (Vars.itemText1[offset] + 1);
        offset++;
        
        byte lenOfMessage = message[messageOffset++];
        offset = Util.arrayCopy(message, messageOffset, Vars.itemText1, offset, lenOfMessage);
        messageOffset = (short) (messageOffset + lenOfMessage);
        
        Vars.itemText1[offset++] = 0;
        Vars.itemText1[offset++] = 4;
        Vars.itemText1[offset++] = 0;
        
        lenOfMessage = message[messageOffset++];
        savedOffset = offset;
        
        Vars.itemText1[offset++] = lenOfMessage;
        offset = Util.arrayCopy(message, messageOffset, Vars.itemText1, offset, lenOfMessage);
        
        byte lenOfBufferData = buffer[bufferOffset++];
        if (lenOfMessage + lenOfBufferData > 140) {
            lenOfBufferData = (byte) (140 - lenOfMessage);
        }
        
        // byte[] src, short srcOff, byte[] dest, short destOff, short length
        offset = Util.arrayCopy(buffer, bufferOffset, Vars.itemText1, offset, lenOfBufferData);
        Vars.itemText1[savedOffset] = (byte) (lenOfBufferData + lenOfMessage);
        
        sfield_token255_descoff268_staticref56 = this.processBufferMessageFuck(Vars.itemText1, (byte) (savedOffset + 1), Vars.itemText1[savedOffset]);
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(PRO_CMD_SEND_SHORT_MESSAGE, (byte) 0, DEV_ID_NETWORK);
        proHandl.appendTLV(TAG_SMS_TPDU, Vars.itemText1, (short) 0, offset);
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
    
    // private byte menuSelectItemAdd(byte[] buffer) {
    // short bufferIndex = 0;
    // byte valueByIndex = buffer[bufferIndex];
    // if (valueByIndex == 0) {
    // return -2;
    // } else {
    // ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
    // proHandl.init(PRO_CMD_SELECT_ITEM, (byte) 0, DEV_ID_ME);
    // byte var7 = (byte) (bufferIndex + 1);
    // short var5;
    //
    // for (byte i = 0; i < valueByIndex; i++) {
    // Util.setShort(this.byteBuffer, (short) (i * 2), var7);
    // var7 = (byte) (var7 + buffer[var7] + 1);
    // var5 = Util.getShort(buffer, var7);
    // var7 = (byte) (var7 + var5 + 2);
    // }
    //
    // for (byte i = (byte) (valueByIndex - 1); i >= 0; i--) {
    // var7 = (byte) Util.getShort(this.byteBuffer, (short) (i * 2));
    // proHandl.appendTLV(TAG_ITEM, (byte) (i + 1), buffer, (short) (var7 + 1), buffer[var7]);
    // }
    //
    // byte result = proHandl.send();
    // if (result == RES_CMD_PERF_SESSION_TERM_USER) {
    // return USER_REQUEST_TERMINATE;
    // } else if (result == RES_CMD_PERF_BACKWARD_MOVE_REQ) {
    // return USER_REQUEST_MOVE_BACK;
    // } else if (result == RES_CMD_PERF_NO_RESP_FROM_USER) {
    // return -4;
    // } else if (result != RES_CMD_PERF) {
    // return -8;
    // } else {
    // ProactiveResponseHandler proHandlResp = ProactiveResponseHandler.getTheHandler();
    // short var3 = (short) (proHandlResp.getItemIdentifier() - 1);
    // if (var3 < 0) {
    // return -5;
    // } else {
    // var7 = (byte) (bufferIndex + 1);
    // var7 = (byte) (var7 + buffer[var7] + 1);
    //
    // for (short i = 0; i < var3; i++) {
    // var5 = Util.getShort(buffer, var7);
    // var7 = (byte) (var7 + var5 + 2);
    // var7 = (byte) (var7 + buffer[var7] + 1);
    // }
    //
    // return var7;
    // }
    // }
    // }
    // }
    
    private short readTextFromSim(short fileOffset, byte[] buffer, short respOffset, boolean fileAlreadySelected) {
        SIMView simView = SIMSystem.getTheSIMView();
        if (!fileAlreadySelected) {
            simView.select(SIMView.FID_MF);
            simView.select(SIMView.FID_DF_GSM);
            simView.select(DF_FID);
            simView.select(EF_FID_1);
        }
        
        simView.readBinary(fileOffset, this.byteBuffer, (short) 0, (short) 1);
        
        short respLength = Util.makeShort((byte) 0, this.byteBuffer[0]);
        respLength = (short) (respLength + 1);
        
        simView.readBinary(fileOffset, buffer, respOffset, respLength);
        return respLength;
    }
    
    private void getImeiFromME() {
        // '00' = Location Information according to current NAA;
        // '01' = IMEI of the terminal;
        // '02' = Network Measurement results according to current NAA;
        // '03' = Date, time and time zone;
        // '04' = Language setting;
        // '05' = Reserved for GSM;
        // '06' = Access Technology;
        // '07' = ESN of the terminal;
        // '08' = IMEISV of the terminal;
        // '09' = Search Mode;
        // '0A' = Charge State of the Battery (if class "X" is supported);
        // '0B' = MEID of the terminal;
        // '0C' to 'FF' = Reserved.
        
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(PRO_CMD_PROVIDE_LOCAL_INFORMATION, (byte) 0x01, DEV_ID_ME);
        proHandl.send();
        ProactiveResponseHandler proRespHandl = ProactiveResponseHandler.getTheHandler();
        if (proRespHandl.getGeneralResult() == RES_CMD_PERF) {
            proRespHandl.findAndCopyValue(TAG_IMEI, imeiBuffer, (short) 0);
        } else {
            for (short i = 0; i < 8; i++) {
                imeiBuffer[i] = -1;
            }
        }
    }
    
    private void buildImeiAndVersionBuffer() {
        /*
         * 00-12 : first number from bufferForNumbers
         * 13 : length of data followed
         * 14 : 73
         * 15 : 77
         * 16 : 69
         * 17 : 73
         * 18 : 32
         * 19-33 : IMEI
         * 34 : 32
         * 35-41 : version of applet
         */
        short offset = Util.arrayCopy(bufferForNumbers, (short) 0, EXTENDED_BUFFER, (short) 0, (short) (bufferForNumbers[0] + 1)); // 13
        short offsetSaved = offset++; // 13
        
        EXTENDED_BUFFER[offset++] = 73; // 14
        EXTENDED_BUFFER[offset++] = 77; // 15
        EXTENDED_BUFFER[offset++] = 69; // 16
        EXTENDED_BUFFER[offset++] = 73; // 17
        EXTENDED_BUFFER[offset++] = 32; // 18
        Util.arrayCopy(imeiBuffer, (short) 0, this.byteBuffer, (short) 0, (short) imeiBuffer.length);
        
        for (byte i = (byte) (8 - 1); i >= 0; i--) {
            this.byteBuffer[(byte) (i * 2 + 1)] = this.processImeiDigit((byte) ((this.byteBuffer[i] >> 4) & 15));
            this.byteBuffer[(byte) (i * 2)] = this.processImeiDigit((byte) (this.byteBuffer[i] & 15));
        }
        
        for (byte i = 0; i <= 15; i++) {
            this.byteBuffer[i] = this.byteBuffer[(byte) (i + 1)];
        }
        
        offset = Util.arrayCopy(this.byteBuffer, (short) 0, EXTENDED_BUFFER, offset, (short) 15); // 34
        EXTENDED_BUFFER[offset++] = 32; // 34
        
        offset = Util.arrayCopy(appletVersion, (short) 0, EXTENDED_BUFFER, offset, (short) appletVersion.length);
        EXTENDED_BUFFER[offsetSaved] = (byte) (offset - offsetSaved - 1);
    }
    
    private byte processImeiDigit(byte digit) {
        if (digit < 10) {
            return (byte) (digit + 48);
        } else {
            return (byte) (digit + 55);
        }
    }
    
    private void sendCall(byte[] message, short index) {
        ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
        proHandl.init(ToolkitConstants.PRO_CMD_SET_UP_CALL, (byte) 0, ToolkitConstants.DEV_ID_NETWORK);
        byte len = message[index++];
        proHandl.appendTLV(ToolkitConstants.TAG_ADDRESS, message, (short) (index + 1), (short) (len - 1));
        proHandl.send();
    }
    
    /*
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 
     */
    
    /* вызывается с трех мест, processCellBroadcastPage, eventSmsPPDataDownload, eventMenuSelection */
    // private void function_DO_1(byte var2, boolean var3) {
    private void function_DO_1(boolean var3) {
        // eventSmsPPDataDownload: var2 = 2; var3 = false;
        // eventMenuSelection: var2 = 3; var3 = true;
        
        short var6 = -1;
        
        // for (short i = 0; i < 1; i++) {
        byte var5 = this.function_DO_2(this.shortBuffer[0]); // this.shortBuffer[0]
        
        if (this.field_token28_descoff870) {
            var3 = true;
        }
        
        if (var5 == 16) {
            if (this.field_token25_descoff849 != 2 && this.field_token25_descoff849 != 4) {
                this.shortBuffer[5] = (short) (this.shortBuffer[0] + this.shortBuffer[3]);
            } else {
                this.shortBuffer[5] = 552;
                this.method_token255_descoff965((short) (this.shortBuffer[0] + this.shortBuffer[3]), this.shortBuffer[5]);
            }
            
            if (var3 && sfield_token255_descoff268_staticref56 != this.firstByteMarkerClass && !this.field_token28_descoff870) {
                var3 = false;
            }
            
            if (this.field_token27_descoff863 && var3) {
                playTone();
            }
            
            this.method_token255_descoff1025(this.field_token25_descoff849 != 0, (byte) ((short) var6), this.shortBuffer[5], var3, true);
        } else {
            var5 = 18;
            this.field_token23_descoff835 = -6;
        }
        
        if (this.field_token23_descoff835 >= 0) {
            var5 = this.method_token255_descoff1409(Vars.sfield_token255_descoff58_staticref12, this.field_token23_descoff835);
        }
        
        this.resetVars();
    }
    
    /* вызывается только с одного места, с function_DO_1 */
    private byte function_DO_2(short offset) {
        byte var2 = 0;
        byte var3 = 0;
        byte var4 = 0;
        byte var5 = 0;
        byte[] tmpHeaderBuffer = { 0, 0, 0 };
        
        Util.arrayCopy(EXTENDED_BUFFER, offset, tmpHeaderBuffer, (short) 0, (short) 3);
        
        this.shortBuffer[3] = tmpHeaderBuffer[0];
        this.shortBuffer[3]++;
        
        // tmpHeaderBuffer[1] in (1,3,7,15,31,63,127,255)
        // tmpHeaderBuffer[1] in (0x01,0x03,0x07,0xF,0x1F,0x3F,0x7F,0xFF)
        // this.boolean1 = (tmpHeaderBuffer[1] & 1) != 0;
        
        if ((tmpHeaderBuffer[1] & -64) != 0) {
            return (byte) -1;
        } else {
            switch ((byte) (tmpHeaderBuffer[1] & 48)) {
                case 16:
                case 48:
                    var5 = (byte) EXTENDED_BUFFER[(short) (offset + this.shortBuffer[3])];
                    this.shortBuffer[4] = (short) (offset + this.shortBuffer[3] + 1);
                    
                    for (short i = 0; i < var5; i++) {
                        var4 = (byte) EXTENDED_BUFFER[this.shortBuffer[4]];
                        
                        var3 = (byte) (EXTENDED_BUFFER[(this.shortBuffer[4] + var4)] & 255);
                        
                        this.shortBuffer[4]++;
                        this.shortBuffer[4] = (short) (this.shortBuffer[4] + var3 + var4);
                    }
                    
                    this.shortBuffer[4] -= offset;
                    if (this.function_DO_3(offset, (tmpHeaderBuffer[1] & 48) == 48, tmpHeaderBuffer[1], tmpHeaderBuffer[2])) {
                        return (byte) 16;
                    }
                    break;
                case 32:
                    if (!this.isAddressInSmsTpduExist) {
                        return (byte) -2;
                    }
                    
                    this.shortBuffer[4] = this.shortBuffer[3];
                    var2 = this.function_DO_4(offset, tmpHeaderBuffer[1], tmpHeaderBuffer[2]);
                    if (var2 == 0) {
                        return (byte) 32;
                    }
                    
                    if (var2 == 2) {
                        this.readAndDisplayText(textIndex7);
                    }
            }
            
            return (byte) -1;
        }
    }
    
    /* вызывается только с одного места, с function_DO_2 */
    private boolean function_DO_3(short var1, boolean var2, byte var3, byte var4) {
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
            if (this.method_token255_descoff1049(EXTENDED_BUFFER[var10002]) == 0) {
                return false;
            }
        }
        
        if (this.field_token25_descoff849 == 2) {
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            this.field_token26_descoff856 = (short) ((short) (EXTENDED_BUFFER[var10002] << 7) & 32640);
        } else if (this.field_token25_descoff849 == 4) {
            this.field_token26_descoff856 = Util.getShort(EXTENDED_BUFFER, (short) var18);
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
            if ((short) (EXTENDED_BUFFER[(short) var18] & sfield_token255_descoff170_staticref39) == 0 && EXTENDED_BUFFER[(short) var18] != 0) {
                return false;
            }
            
            ++var18;
        }
        
        if ((short) ((short) var4 & 16) != 0) {
            if ((short) (EXTENDED_BUFFER[(short) var18] & sfield_token255_descoff205_staticref44) == 0 && EXTENDED_BUFFER[(short) var18] != 0) {
                return false;
            }
            
            ++var18;
        }
        
        if ((short) var5 > 0) {
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            this.field_token29_descoff877 = EXTENDED_BUFFER[var10002];
            if (this.field_token29_descoff877 > 0) {
                var7 = (short) this.field_token29_descoff877;
                if (this.field_token29_descoff877 > 11) {
                    this.field_token29_descoff877 = 11;
                }
                
                Util.arrayCopy(EXTENDED_BUFFER, (short) var18, Vars.sfield_token255_descoff58_staticref12, (short) 0, this.field_token29_descoff877);
                var18 = (short) ((short) ((short) var18 + (short) var7));
            }
            
            var10002 = (short) var18;
            var18 = (short) ((short) ((short) var18 + 1));
            this.field_token30_descoff884 = EXTENDED_BUFFER[var10002];
            if (this.field_token30_descoff884 > 0) {
                var7 = (short) this.field_token30_descoff884;
                if (this.field_token30_descoff884 > 20) {
                    this.field_token30_descoff884 = 20;
                }
                
                Util.arrayCopy(EXTENDED_BUFFER, (short) var18, Vars.sfield_token255_descoff58_staticref12, (short) 11, this.field_token30_descoff884);
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
            short var23 = (short) EXTENDED_BUFFER[(short) var18];
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
                    var9 = (short) ((short) ((short) var9 + (short) (1 + (short) (EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)] & 127))));
                    break;
                case 16:
                case 64:
                    if ((short) var6 == 64) {
                        this.field_token31_descoff891 = true;
                    }
                    
                    var9 = (short) ((short) (1 + (short) ((short) var23 & 15)));
                    break;
                case 112:
                    var6 = (short) EXTENDED_BUFFER[(short) ((short) var18 + 2)];
                    switch ((short) var6) {
                        case 2:
                        case 3:
                            var9 = (short) 1;
                            
                            for (var19 = (short) 0; (short) var9 < EXTENDED_BUFFER[(short) ((short) var18 + 1)]; var19 = (short) ((byte) ((short) ((short) var19 + 1)))) {
                                var9 = (short) ((short) ((short) var9 + (byte) ((short) (EXTENDED_BUFFER[(short) ((short) ((short) ((short) var18 + (short) var9) + 1) + 1)] + 1))));
                            }
                            
                            if (sfield_token255_descoff191_staticref42 && (short) var19 >= 2) {
                                var15 = (short) 1;
                            }
                            
                            var19 = (short) 0;
                            var9 = (short) EXTENDED_BUFFER[(short) ((short) var18 + 1)];
                            // byte var26 = EXTENDED_BUFFER[(short) ((short) ((short) ((short) var18 + (short) var9) + 1)
                            // + 1)];
                            if (EXTENDED_BUFFER[(short) ((short) ((short) var18 + (short) var9) + 4)] == 4) {
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
                    var18 = (short) ((short) ((short) var18 + (short) (2 + EXTENDED_BUFFER[(short) ((short) var18 + 1)])));
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
                            var12 = (short) ((byte) ((short) (EXTENDED_BUFFER[(short) ((short) ((short) var18 + (short) var9) + 2)] + 1)));
                            var11 = (short) this.method_token255_descoff1109(EXTENDED_BUFFER[(short) ((short) ((short) ((short) ((short) var18 + (short) var9) + 2) + (short) var12) + 1)]);
                        } else {
                            var11 = (short) this.method_token255_descoff1109(EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)]);
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
                        var12 = (short) ((byte) ((short) (EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)] + 1)));
                        var9 = (short) ((short) ((short) var9 + (short) var12));
                        if ((short) var19 == 1) {
                            var12 = (short) ((byte) ((short) (EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)] + 1)));
                            var9 = (short) ((short) ((short) var9 + (short) ((short) ((short) ((short) var12 + 1) + 1) + 1)));
                            var12 = (short) ((byte) ((short) (EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)] + 1)));
                            var9 = (short) ((short) ((short) var9 + (short) var12));
                            var8 = (short) EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)];
                        } else {
                            var8 = (short) EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)];
                        }
                    } else {
                        var8 = (short) EXTENDED_BUFFER[(short) ((short) var18 + (short) var9)];
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
                            
                            Util.arrayCopy(EXTENDED_BUFFER, (short) ((short) ((short) var18 + (short) var9) + 1), Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), (short) var8);
                            if ((short) var15 == 0) {
                                var9 = (short) ((short) ((short) var9 + (short) var12));
                            }
                            
                            var10 = (short) ((short) ((short) var10 + (short) var8));
                        }
                    } else {
                        if ((short) var14 != 0) {
                            var8 = this.method_token255_descoff977((short) (var18 + var9), Vars.sfield_token255_descoff58_staticref12, (short) (32 + var10 + 2));
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
                                if ((short) (EXTENDED_BUFFER[(short) ((short) ((short) ((short) var18 + 1) + (short) var8) - 1)] & -16) == -16) {
                                    var24 = (short) ((byte) ((short) ((short) var24 - 1)));
                                }
                            } else {
                                var24 = (short) ((short) var8);
                            }
                            
                            ++var10;
                            var10002 = (short) var10;
                            var10 = (short) ((short) ((short) var10 + 1));
                            Vars.sfield_token255_descoff58_staticref12[(short) (32 + var10002)] = (byte) ((short) var24);
                            Util.arrayCopy(EXTENDED_BUFFER, (short) ((short) var18 + 1), Vars.sfield_token255_descoff58_staticref12, (short) (32 + (short) var10), (short) var8);
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
                    var24 = (short) EXTENDED_BUFFER[(short) var18];
                    var8 = (short) ((short) ((short) var24 & 127));
                    var7 = (short) ((short) var8);
                    if ((short) var8 > 20) {
                        var8 = (short) 20;
                    }
                    
                    if ((short) var13 != 0) {
                        if ((short) var8 > 0) {
                            Util.arrayCopy(EXTENDED_BUFFER, (short) ((short) var18 + 1), Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
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
                    var8 = (short) EXTENDED_BUFFER[(short) ((short) ((short) var18 + 1) + 2)];
                    var7 = (short) ((short) var8);
                    if ((short) var8 > 20) {
                        var8 = (short) 20;
                    }
                    
                    if ((short) var13 != 0) {
                        if ((short) var8 > 0) {
                            Util.arrayCopy(EXTENDED_BUFFER, (short) ((short) ((short) var18 + 1) + 3), Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
                        } else {
                            var8 = (short) this.field_token30_descoff884;
                            Util.arrayCopy(Vars.sfield_token255_descoff58_staticref12, (short) 11, Vars.sfield_token255_descoff58_staticref12, (short) ((short) (32 + (short) var10) + 1), (short) var8);
                        }
                        
                        Vars.sfield_token255_descoff58_staticref12[(short) (32 + (short) var10)] = (byte) ((short) var8);
                        var10 = (short) ((short) ((short) var10 + (byte) ((short) ((short) var8 + 1))));
                    }
                }
                
                if ((short) var15 != 0) {
                    var18 = (short) ((short) ((short) var18 + (byte) ((short) ((short) (EXTENDED_BUFFER[(short) ((short) var18 + 1)] + 1) + 1))));
                    if ((short) var19 == 1) {
                        var18 = (short) ((short) ((short) var18 + (byte) ((short) ((short) (EXTENDED_BUFFER[(short) ((short) var18 + 1)] + 1) + 1))));
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
    
    /* вызывается только с одного места, с function_DO_2 */
    private byte function_DO_4(short offset, byte var2, byte var3) {
        short var4 = 0;
        // short var5 = 0;
        short var6 = 0;
        short var7 = 0;
        short var8 = 0;
        short var9 = (short) (var3 & 15);
        boolean var11 = sfield_token255_descoff149_staticref36;
        short var10 = (short) (offset + 1 + 2);
        
        for (short i = 0; i < var9; i++) {
            short var13 = (short) EXTENDED_BUFFER[var10];
            short var14 = (short) (EXTENDED_BUFFER[(short) (var10 + 1)] & 255);
            var10 = (short) (var10 + 2);
            short var15;
            SIMView simView;
            short var10001;
            short var20;
            switch (var13) {
                case 3:
                    // sfield_token255_descoff632_staticref113 = true;
                    Util.arrayCopy(imeiBuffer, (short) 0, imeiBufferToCompare, (short) 0, (short) 8);
                    // var15 = (short) sfield_token255_descoff177_staticref40;
                    var15 = (short) (sfield_token255_descoff177_staticref40 ? 1 : 0);
                    sfield_token255_descoff149_staticref36 = false;
                    sfield_token255_descoff163_staticref38 = EXTENDED_BUFFER[(short) (var10 + 5)];
                    sfield_token255_descoff170_staticref39 = EXTENDED_BUFFER[(short) (var10 + 6)];
                    sfield_token255_descoff639_staticref114 = (short) (EXTENDED_BUFFER[(short) (var10 + 7)] & -128) != 0;
                    sfield_token255_descoff177_staticref40 = (short) (EXTENDED_BUFFER[(short) (var10 + 7)] & 64) != 0;
                    sfield_token255_descoff184_staticref41 = (short) (EXTENDED_BUFFER[(short) (var10 + 7)] & 32) != 0;
                    sfield_token255_descoff191_staticref42 = (short) (EXTENDED_BUFFER[(short) (var10 + 7)] & 16) != 0;
                    if (!sfield_token255_descoff177_staticref40) {
                        sfield_token255_descoff317_staticref65 = sfield_token255_descoff310_staticref64;
                        sfield_token255_descoff310_staticref64 = 2;
                        // sfield_token255_descoff247_staticref50 = sfield_token255_descoff254_staticref52 = sfield_token255_descoff646_staticref115;
                        // sfield_token255_descoff135_staticref33 = false;
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
                        isAllowPlaySound = false;
                    }
                    
                    var10 = (short) (var10 + var14);
                    break;
                case 4:
                    var15 = 0;
                    var20 = 0;
                    var4 = (short) (EXTENDED_BUFFER[var10] > 10 ? 10 : EXTENDED_BUFFER[var10]);
                    var7 = (short) (var10 + 1);
                    
                    for (short j = 0; j < var4; j++) {
                        Vars.sfield_token255_descoff51_staticref10[var20++] = EXTENDED_BUFFER[var7];
                        this.byteBuffer[var15++] = EXTENDED_BUFFER[var7];
                        if (EXTENDED_BUFFER[(short) (var7 + 1)] == 3) {
                            if (this.method_token255_descoff1049(EXTENDED_BUFFER[var7]) == 0) {
                                this.byteBuffer[var15++] = 0;
                            } else {
                                this.byteBuffer[var15++] = 1;
                            }
                        } else {
                            this.byteBuffer[var15++] = EXTENDED_BUFFER[(short) (var7 + 1)];
                        }
                        
                        var7 = (short) (var7 + 2);
                        var6 = (short) (EXTENDED_BUFFER[var7] > 21 ? 21 : EXTENDED_BUFFER[var7]);
                        Vars.sfield_token255_descoff51_staticref10[var20++] = (byte) var6;
                        var20 = (short) Util.arrayCopy(EXTENDED_BUFFER, (short) (var7 + 1), Vars.sfield_token255_descoff51_staticref10, var20, var6);
                        var7 = (short) (var7 + EXTENDED_BUFFER[var7] + 1);
                    }
                    
                    Util.arrayCopy(this.byteBuffer, (short) 0, Vars.sfield_token255_descoff44_staticref8, (short) 0, var15);
                    sfield_token255_descoff212_staticref45 = (byte) var4;
                    var10 = (short) (var10 + var14);
                    break;
                case 5:
                    var15 = var10;
                    sfield_token255_descoff198_staticref43 = 0;
                    var7 = (short) ((short) (Vars.sfield_token255_descoff72_staticref16[0] + 1));
                    var20 = (short) (EXTENDED_BUFFER[(short) var15] > 12 ? 12 : EXTENDED_BUFFER[(short) var15]);
                    ++var15;
                    var4 = 1;
                    
                    for (short j = 0; j < var20; j++) {
                        if ((short) var4 < (byte) ((short) (Vars.sfield_token255_descoff72_staticref16.length - 1))) {
                            var10001 = (short) var4;
                            var4 = (short) ((byte) ((short) ((short) var4 + 1)));
                            Vars.sfield_token255_descoff72_staticref16[var10001] = EXTENDED_BUFFER[(short) var15];
                            var10001 = (short) var4;
                            var4 = (short) ((byte) ((short) ((short) var4 + 1)));
                            Vars.sfield_token255_descoff72_staticref16[var10001] = (short) ((short) var7 + 1);
                            var6 = (short) ((byte) ((short) (2 + EXTENDED_BUFFER[(short) ((short) var15 + 1)])));
                            var8 = (short) ((short) var6);
                            if ((short) var6 > 29) {
                                var6 = (short) 29;
                                EXTENDED_BUFFER[(short) ((short) var15 + 1)] = (byte) ((short) ((short) ((short) var6 - 1) - 1));
                            }
                            
                            sfield_token255_descoff198_staticref43 = (byte) (sfield_token255_descoff198_staticref43 + 1);
                            
                            try {
                                this.byteBuffer[0] = sfield_token255_descoff198_staticref43;
                                
                                SIMView simViewTmp = SIMSystem.getTheSIMView();
                                simViewTmp.select(SIMView.FID_MF);
                                simViewTmp.select(SIMView.FID_DF_GSM);
                                simViewTmp.select(DF_FID);
                                simViewTmp.select(EF_FID_1);
                                simViewTmp.updateBinary(Vars.sfield_token255_descoff72_staticref16[0], this.byteBuffer, (short) 0, (short) 1);
                                simViewTmp.updateBinary(var7, EXTENDED_BUFFER, var15, var6);
                                
                                var7 = (short) (var7 + var6);
                            } catch (SIMViewException ex) {
                                sfield_token255_descoff198_staticref43 = (byte) (sfield_token255_descoff198_staticref43 - 1);
                            }
                        }
                        
                        var15 = (short) ((short) ((short) var15 + (short) var8));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 6:
                    // sfield_token255_descoff303_staticref63 = EXTENDED_BUFFER[(short) ((short) var10 + 2)];
                    // if ((short) var14 > 4) {
                    // sfield_token255_descoff646_staticref115 = Util.makeShort((byte) 0, EXTENDED_BUFFER[(short) ((short) var10 + 3)]);
                    // sfield_token255_descoff653_staticref117 = Util.makeShort((byte) 0, EXTENDED_BUFFER[(short) ((short) var10 + 4)]);
                    // }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 7:
                    var15 = (short) EXTENDED_BUFFER[(short) var10];
                    if ((short) var15 > 1 && (short) var15 <= 12) {
                        Util.arrayCopy(EXTENDED_BUFFER, (short) var10, bufferForNumbers, (short) 0, (short) ((short) var15 + 1));
                        simView = SIMSystem.getTheSIMView();
                        simView.select(SIMView.FID_MF);
                        simView.select(SIMView.FID_DF_GSM);
                        simView.select(DF_FID);
                        simView.select(EF_FID_1);
                        simView.updateBinary((short) 15, EXTENDED_BUFFER, (short) var10, (short) ((short) var15 + 1));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 8:
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(false);
                    }
                    
                    sfield_token255_descoff275_staticref57 = Util.getShort(EXTENDED_BUFFER, (short) var10);
                    // sfield_token255_descoff282_staticref59 = Util.makeShort(EXTENDED_BUFFER[(short) ((short) var10 + 1)], EXTENDED_BUFFER[(short)
                    // var10]);
                    if (sfield_token255_descoff310_staticref64 == 1) {
                        this.method_token255_descoff1565(true);
                    }
                    
                    SIMView var19 = SIMSystem.getTheSIMView();
                    var19.select(SIMView.FID_MF);
                    var19.select(SIMView.FID_DF_GSM);
                    var19.select(DF_FID);
                    var19.select(EF_FID_1);
                    var19.updateBinary((short) 29, EXTENDED_BUFFER, (short) var10, (short) 2);
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 9:
                    if (EXTENDED_BUFFER[(short) var10] == 1 && sfield_token255_descoff184_staticref41) {
                        isAllowPlaySound = true;
                    } else if (EXTENDED_BUFFER[(short) var10] == 2) {
                        isAllowPlaySound = false;
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 10:
                    if (EXTENDED_BUFFER[(short) var10] == 2) {
                        sfield_token255_descoff331_staticref67 = 2;
                    } else if (EXTENDED_BUFFER[(short) var10] == 1) {
                        sfield_token255_descoff331_staticref67 = 1;
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 11:
                    sfield_token255_descoff205_staticref44 = EXTENDED_BUFFER[(short) var10];
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 12:
                    this.getImeiFromME();
                    this.sendImeiBySMSToUser();
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 13:
                    var15 = (short) EXTENDED_BUFFER[(short) var10];
                    if ((short) var15 > 1 && (short) var15 < 12) {
                        Util.arrayCopy(EXTENDED_BUFFER, (short) var10, bufferForNumbers, (short) 13, (short) ((short) var15 + 1));
                        simView = SIMSystem.getTheSIMView();
                        simView.select(SIMView.FID_MF);
                        simView.select(SIMView.FID_DF_GSM);
                        simView.select(DF_FID);
                        simView.select(EF_FID_1);
                        simView.updateBinary((short) 2, EXTENDED_BUFFER, (short) var10, (short) ((short) var15 + 1));
                    }
                    
                    var10 = (short) ((short) ((short) var10 + (short) var14));
                    break;
                case 14:
                    if (Util.arrayCompare(bufferForNumbers, (short) 27, EXTENDED_BUFFER, var10, bufferForNumbers[26]) != 0) {
                        return (byte) 6;
                    }
                    
                    var10 = (short) (var10 + var14);
                    break;
                case 15:
                    var15 = (short) EXTENDED_BUFFER[var10];
                    if (var15 > 1 && var15 < 11) {
                        Util.arrayCopy(EXTENDED_BUFFER, var10, bufferForNumbers, (short) 26, (short) (var15 + 1));
                        
                        simView = SIMSystem.getTheSIMView();
                        simView.select(SIMView.FID_MF);
                        simView.select(SIMView.FID_DF_GSM);
                        simView.select(DF_FID);
                        simView.select(EF_FID_1);
                        simView.updateBinary((short) 31, EXTENDED_BUFFER, var10, (short) (var15 + 1));
                    }
                    
                    var10 = (short) (var10 + var14);
                    break;
                default:
                    var10 = (short) (var10 + var14);
            }
        }
        
        if ((short) ((short) var2 & 12) != 12 && ((short) ((short) var2 & 12) != 4 || !var11)) {
            if ((short) ((short) var2 & 12) == 0) {
                sfield_token255_descoff310_staticref64 = 2;
                // sfield_token255_descoff135_staticref33 = false;
                this.method_token255_descoff1565(false);
                return (byte) 4;
            }
            
            if ((short) ((short) var2 & 12) == 8) {
                if (!sfield_token255_descoff177_staticref40) {
                    sfield_token255_descoff317_staticref65 = 3;
                } else {
                    sfield_token255_descoff310_staticref64 = 3;
                }
                
                // sfield_token255_descoff135_staticref33 = false;
                this.method_token255_descoff1565(false);
                return (byte) 5;
            }
        } else if (sfield_token255_descoff177_staticref40 && (sfield_token255_descoff310_staticref64 != 3 || (short) ((short) var2 & 2) != 0)) {
            sfield_token255_descoff310_staticref64 = 1;
            this.method_token255_descoff1565(true);
            if (var11) {
                if (!sfield_token255_descoff296_staticref62) {
                    this.readAndDisplayText(textIndex29);
                } else {
                    this.readAndDisplayText(textIndex1);
                }
            }
        }
        
        return (byte) 0;
    }
    
    /*
     * SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS
     * SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS
     * SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS
     * SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS *** SMS METHODS
     * 
     */
    
    private void eventSmsPPDataDownload() {
        EnvelopeHandler envHandl = EnvelopeHandler.getTheHandler();
        short smsUserDataLength = 0;
        
        try {
            if (envHandl.findAndCompareValue(TAG_ADDRESS, bufferForNumbers, (short) 14) != 0) {
                this.isAddressInSmsTpduExist = false;
            } else {
                envHandl.findTLV(TAG_SMS_TPDU, (byte) 1);
                this.isAddressInSmsTpduExist = envHandl.compareValue((short) 3, bufferForNumbers, (short) 3, (short) (bufferForNumbers[0] - 2)) == 0;
            }
        } catch (Exception ex) {
            this.isAddressInSmsTpduExist = false;
        }
        
        short smsUserDataLOffset = envHandl.getTPUDLOffset();
        smsUserDataLength = (short) envHandl.getValueByte(smsUserDataLOffset);
        smsUserDataLength = smsUserDataLength > 140 ? 140 : smsUserDataLength;
        
        short bufferDstLength = 2;
        envHandl.copyValue((short) (smsUserDataLOffset + 1), this.byteBuffer, (short) 0, bufferDstLength);
        smsUserDataLOffset += bufferDstLength;
        
        byte smsUserDataFirstByte = this.byteBuffer[0];
        byte smsUserDataSecondByte = this.byteBuffer[1];
        
        if (smsUserDataFirstByte == 0 || (smsUserDataFirstByte & -128) != 0) {
            // 0x00 - 0x7f (smsUserDataFirstByte & -128) == 0
            // 0x80 - 0xff (smsUserDataFirstByte & -128) != 0
            this.firstByteMarkerClass = smsUserDataFirstByte;
            if (smsUserDataFirstByte != this.userDataFirstBateSaved) {
                this.resetVars();
                this.userDataFirstBateSaved = smsUserDataFirstByte;
            } else if (this.msgLimitExceed) {
                this.resetVars();
                return;
            }
            
            if (this.concatMsgCounter < this.concatMsgMapper.length) {
                byte currentMsgNum = (byte) (((smsUserDataSecondByte & -16) >> 4) & 15);
                byte totalMsg = (byte) (smsUserDataSecondByte & 15);
                short msgBufferOffset = 0;
                
                if (this.isConcatMsgAlreadyMapped(currentMsgNum) || totalMsg > this.concatMsgMapper.length || currentMsgNum > totalMsg) {
                    return;
                }
                
                msgBufferOffset = (short) (138 * (currentMsgNum - 1));
                // 138 * 3 = 414
                if (msgBufferOffset > 414) {
                    return;
                }
                
                this.increaseConcatMsg(currentMsgNum);
                
                if (this.countConcatMsgReceived() >= totalMsg) {
                    this.resetConcatMsgVars();
                    this.msgLimitExceed = true;
                }
                
                envHandl.copyValue(smsUserDataLOffset, EXTENDED_BUFFER, msgBufferOffset, (short) (smsUserDataLength - 2));
            }
            
            if (this.msgLimitExceed) {
                this.shortBuffer[0] = 0;
                this.userDataFirstBateSaved = -1;
                // this.function_DO_1((byte) 2, sfield_token255_descoff324_staticref66 == 2);
                this.function_DO_1(sfield_token255_descoff324_staticref66 == 2);
            }
        }
    }
    
    private void resetVars() {
        this.resetConcatMsgVars();
        
        this.userDataFirstBateSaved = -1;
        this.msgLimitExceed = false;
        this.concatMsgCounter = 0;
    }
    
    private void resetConcatMsgVars() {
        for (short i = 0; i < this.concatMsgMapper.length; i++) {
            if (this.concatMsgMapper[i]) {
                this.concatMsgCounter--;
            }
            this.concatMsgMapper[i] = false;
        }
    }
    
    private void increaseConcatMsg(byte currentMsgNum) {
        byte i = (byte) (currentMsgNum - 1);
        if (i >= 0 && i < this.concatMsgMapper.length) {
            if (!this.concatMsgMapper[i]) {
                this.concatMsgCounter++;
            }
            
            this.concatMsgMapper[i] = true;
        }
    }
    
    private boolean isConcatMsgAlreadyMapped(byte currentMsgNum) {
        byte msgIndex = (byte) (currentMsgNum - 1);
        if (msgIndex >= 0 && msgIndex < this.concatMsgMapper.length)
            return this.concatMsgMapper[msgIndex];
        else
            return false;
    }
    
    private byte countConcatMsgReceived() {
        byte ret = 0;
        for (byte i = 0; i < this.concatMsgMapper.length; i++) {
            if (this.concatMsgMapper[i]) {
                ret++;
            }
        }
        return ret;
    }
    
    private void playTone() {
        if (isAllowPlaySound) {
            ProactiveHandler proHandl = ProactiveHandler.getTheHandler();
            proHandl.init(PRO_CMD_PLAY_TONE, (byte) 0, DEV_ID_EARPIECE);
            proHandl.appendTLV(TAG_TONE, (byte) 16);
            proHandl.appendTLV(TAG_DURATION, (byte) 1, (byte) 1);
            proHandl.send();
        }
    }
}
