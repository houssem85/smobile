package fr.strada.smobile.utils;



public class Holder {
    private Object value = null;

    public void Put(String str) {
        this.value = str;
    }

    public void Put(byte b) {
        this.value = Byte.valueOf(b);
    }
    public void Put(long j) {
        this.value = Long.valueOf(j);
    }
    public void Put(byte[] bArr) {
        this.value = bArr;
    }

    public void Put(int i) {
        this.value = Integer.valueOf(i);
    }

    public void Put(int[] iArr) {
        this.value = iArr;
    }

    public String GetString() {
        return (String) this.value;
    }

    public byte GetByte() {
        return ((Byte) this.value).byteValue();
    }

    public byte[] GetByteArray() {
        return (byte[]) this.value;
    }

    public int GetInt() {
        return ((Integer) this.value).intValue();
    }

    public int[] GetIntArray() {
        return (int[]) this.value;
    }




}
