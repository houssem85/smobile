package fr.strada.smobile.utils.cardlib;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ByteConverter {

    public static int ToUInt32(byte[] bytes) {
        byte[] newArray = new byte[0];
        int result = 0;
        try {
            newArray = ToInt32Array(bytes);
            result  = BitConverter.toInt32(newArray, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int ToInt32_2(byte[] bytes, int index)
    {
        int a = (int)((int)(0xff & bytes[index]) << 32 | (int)(0xff & bytes[index + 1]) << 40 | (int)(0xff & bytes[index + 2]) << 48 | (int)(0xff & bytes[index + 3]) << 56);
        // int a = (int)((int)(0xff & bytes[index]) << 56 | (int)(0xff & bytes[index + 1]) << 48 | (int)(0xff & bytes[index + 2]) << 40 | (int)(0xff & bytes[index + 3]) << 32);
        //Array.Resize;
        return a;
    }

    private static byte[] ToInt32Array(byte[] bytes) throws Exception {
        if (bytes.length > 4)
            throw new Exception("bytes array too long");

        byte[] newArray = new byte[4];

        int startAt = newArray.length - bytes.length;
        System.arraycopy(bytes, 0, newArray, startAt, bytes.length);


        return newArray;
    }
    public static Date ToDateTime(byte[] bytes)
    {
        int timeStamp = ToUInt32(bytes);

        Date dateTime = UnixTimeStampToDate(timeStamp);
        return dateTime;
    }

    private static Date UnixTimeStampToDate(int timeStamp) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(1970, 1, 1, 0, 0, 0);
        calendar.setTimeInMillis(timeStamp * 1000L);

      //  calendar.add(Calendar.SECOND,timeStamp);
        return calendar.getTime();
    }


    public class UInt32 extends Number implements Comparable<UInt32> {
        /** Maximum allowed value */
        public static final long MAX_VALUE = 4294967295L;
        /** Minimum allowed value */
        public static final long MIN_VALUE = 0;
        private long             value;

        /**
         * Create a UInt32 from a long.
         *
         * @param value
         *          Must be a valid integer within MIN_VALUE&ndash;MAX_VALUE
         * @throws NumberFormatException
         *           if value is not between MIN_VALUE and MAX_VALUE
         */
        public UInt32(long value) {
            this.value = value;
        }

        /**
         * Create a UInt32 from a String.
         *
         * @param value
         *          Must parse to a valid integer within MIN_VALUE&ndash;MAX_VALUE
         * @throws NumberFormatException
         *           if value is not an integer between MIN_VALUE and MAX_VALUE
         */
        public UInt32(String value) {
            this(Long.parseLong(value));
        }

        /** The value of this as a byte. */
        @Override
        public byte byteValue() {
            return (byte) value;
        }

        /**
         * Compare two UInt32s.
         *
         * @return 0 if equal, -ve or +ve if they are different.
         */
        public int compareTo(UInt32 other) {
            return (int) (value - other.value);
        }

        /** The value of this as a double. */
        @Override
        public double doubleValue() {
            return value;
        }

        /** Test two UInt32s for equality. */
        @Override
        public boolean equals(Object o) {
            return (o instanceof UInt32) && (((UInt32) o).value == value);
        }

        /** The value of this as a float. */
        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return (int) value;
        }

        /** The value of this as a int. */
        @Override
        public int intValue() {
            return (int) value;
        }

        /** The value of this as a long. */
        @Override
        public long longValue() {
            return /* (long) */value;
        }

        /** The value of this as a short. */
        @Override
        public short shortValue() {
            return (short) value;
        }

        /** The value of this as a string */
        @Override
        public String toString() {
            return "" + value;
        }
    }


}
