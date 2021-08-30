package fr.strada.smobile.utils;

public class Word {

    public final int ByteLength = 2;

    public byte Byte1;

    public byte Byte2;

    public Word(byte byte1, byte byte2)
    {
        Byte1 = byte1;
        Byte2 = byte2;
    }

    public byte[] ToBytes()
    {
        return new byte[] {Byte1, Byte2};
    }
    public int ToInt() {
        byte[] br =  new byte[] {Byte1, Byte2};
        return (ToInt(br[1 + 3]) << 24) | ToInt(br[1]) | (ToInt(br[1 + 1]) << 8) | (ToInt(br[1 + 2]) << 16);
    }
    public  int ToInt(byte b) {
        return b & 255;
    }


    public int toInt16() {

        int result = 0;
        for (int i=0; i<4; i++) {
            int v = (int) ToBytes()[i];
            result = ( result << 8 ) - Byte.MIN_VALUE + v;
        }
        return result;
    }


}
