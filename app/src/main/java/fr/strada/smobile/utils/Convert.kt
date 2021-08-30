package fr.strada.smobile.utils

object Convert {

    fun HexStringToByteArray(str: String): ByteArray {

        val data = str.toCharArray()
        val len = data.size
        val out = ByteArray(len shr 1)
        run {
            var i = 0
            var j = 0
            while (j < len) {
                var f = Character.digit(data[j], 16) shl 4
                j++
                f = f or Character.digit(data[j], 16)
                j++
                out[i] = (f and 0xFF).toByte()
                i++
            }
        }
        return out
    }

    @JvmOverloads
     fun toHexString(bArr: ByteArray, i: Int = 0, i2: Int = bArr.size): String {
        val sb = StringBuilder()
        for (i3 in 0 until i2) {
            sb.append(toHexString(bArr[i + i3]))
            sb.append("")
        }
        return sb.toString().trim { it <= ' ' }
    }

     fun toHexString(b: Byte): String {
        return String.format("%02X", *arrayOf<Any>(java.lang.Byte.valueOf(b)))
    }

    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        run {
            var i = 0
            while (i < len) {
                data[i / 2] =
                    ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
                i += 2
            }
        }
        return data
    }

    fun ComputeDigitalSignature(b: Byte) : ByteArray{
        return byteArrayOf(0, 42, -98, -102, b)
    }

    fun PerformHashOfFileByte(b : Byte) : ByteArray {
        return byteArrayOf(Byte.MIN_VALUE, 42, -112, b, 0)
    }

    internal fun ToByte(i: Int): Byte {
        return i.toByte()
    }

}