package com.denis;

public class Deleted {
    
    // private boolean isCbsIdValueNotExist(byte value, boolean update) {
    // for (short i = 0; i < cbsMessageIdBuffer.length; i++) {
    // if (cbsMessageIdBuffer[i] == value) {
    // return false;
    // }
    // }
    //
    // if (update) {
    // cbsMessageIdBuffer[cbsMessageIdBufferIndex] = value;
    //
    // if (++cbsMessageIdBufferIndex >= cbsMessageIdBuffer.length) {
    // cbsMessageIdBufferIndex = 0;
    // }
    // }
    //
    // return true;
    // }
    
    // private void processCellBroadcastPage() {
    // EnvelopeHandler var1 = EnvelopeHandler.getTheHandler();
    // // short var2 = (short) 0;
    // short var3 = (short) 0;
    // this.shortBuffer[0] = 0;
    // if (!sfield_token255_descoff177_staticref40 && !sfield_token255_descoff632_staticref113 && --sfield_token255_descoff247_staticref50 <= 0) {
    // sfield_token255_descoff254_staticref52 *= sfield_token255_descoff653_staticref117;
    // if (sfield_token255_descoff254_staticref52 < 0) {
    // sfield_token255_descoff254_staticref52 = 32767;
    // }
    //
    // sfield_token255_descoff247_staticref50 = sfield_token255_descoff254_staticref52;
    // this.method_token255_descoff1445();
    // }
    //
    // if (sfield_token255_descoff310_staticref64 == 1) {
    // if (var1.findTLV(TAG_CELL_BROADCAST_PAGE, (byte) 1) != TLV_NOT_FOUND) {
    //
    // this.shortBuffer[1] = 0;
    // this.readEnvelopData(this.byteBuffer, (short) 0, (short) 8);
    //
    // if (this.method_token255_descoff1577(this.byteBuffer, (short) 2)) {
    // short var4 = (short) ((short) (this.byteBuffer[6] & 127));
    // short var5 = (short) this.byteBuffer[7];
    // if ((short) var4 != 1) {
    // if (sfield_token255_descoff135_staticref33) {
    // if (this.isCbsIdValueNotExist((byte) var4, true) && --sfield_token255_descoff142_staticref34 <= 0) {
    // if (sfield_token255_descoff128_staticref32 == 1) {
    // playTone();
    // this.readAndDisplayText(textIndex8);
    // sfield_token255_descoff128_staticref32 = 0;
    // }
    //
    // this.resetCbsMessageIdBuffer();
    // this.resetVars();
    // sfield_token255_descoff135_staticref33 = false;
    // sfield_token255_descoff324_staticref66 = 3;
    // sfield_token255_descoff142_staticref34 = 0;
    // }
    //
    // } else {
    // sfield_token255_descoff324_staticref66 = 3;
    // if ((short) var4 != this.byteBufferPublic[2]) {
    // this.byteBufferPublic[2] = (byte) ((short) var4);
    // if (!this.booleanFlag1) {
    // this.decrease();
    // } else {
    // this.booleanFlag1 = false;
    // }
    // }
    //
    // if (this.byteBufferPublic[0] < 4) {
    // if (!this.isCbsIdValueNotExist((byte) var4, false)) {
    // return;
    // }
    //
    // short var6 = (short) ((short) ((short) ((short) ((short) var5 & -16) >> 4) & 15));
    // short var7 = (short) ((short) ((short) var5 & 15));
    // if (this.method_token255_descoff1133((byte) ((short) var6)) || (short) var7 > 4 || (short) var6 > (short) var7) {
    // return;
    // }
    //
    // var3 = (short) ((short) (this.shortBuffer[2] + (short) (79 * (short) ((short) var6 - 1))));
    // if ((short) var3 > 473) {
    // return;
    // }
    //
    // this.readEnvelopData(Vars.sfield_token255_descoff107_staticref26, var3, (short) 79);
    // this.encrease((byte) ((short) var6));
    // if (this.countSetted() >= (short) var7) {
    // this.byteBufferPublic[1] = (byte) ((short) (this.byteBufferPublic[1] + 1));
    // this.decrease();
    // this.booleanFlag1 = true;
    // this.shortBuffer[2] += (short) (79 * (short) var7);
    // }
    // }
    //
    // if (this.byteBufferPublic[1] >= 1 || this.byteBufferPublic[0] >= 4) {
    // this.method_token255_descoff1217(this.byteBufferPublic[1], (byte) 1, true);
    // this.isCbsIdValueNotExist((byte) var4, true);
    // }
    //
    // }
    // }
    // }
    // }
    // }
    // }
    
//  private void resetCbsMessageIdBuffer() {
//  for (short i = 0; i < cbsMessageIdBuffer.length; i++) {
//      cbsMessageIdBuffer[i] = 1;
//  }
//}
}
