package sim.access;
public interface SIMView extends javacard.framework.Shareable {
short status(byte[] param1, short param2, short param3);
short increase(byte[] param1, short param2, byte[] param3, short param4);
void invalidate();
void rehabilitate();
short readRecord(short param1, byte param2, short param3, byte[] param4, short param5, short param6);
void updateRecord(short param1, byte param2, short param3, byte[] param4, short param5, short param6);
short select(short param1, byte[] param2, short param3, short param4);
void select(short param1);
short seek(byte param1, byte[] param2, short param3, short param4);
short readBinary(short param1, byte[] param2, short param3, short param4);
void updateBinary(short param1, byte[] param2, short param3, short param4);
}
