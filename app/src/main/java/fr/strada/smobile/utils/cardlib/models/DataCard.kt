package fr.strada.smobile.utils.cardlib.models

class DataCard {

    var FileId: String = ""
    var DataLength: String = ""
    var SignatureDataLength: String = ""

    var resultData: String = ""
    var resultSignatureData: String = ""


    constructor(resultData: String) {
        this.resultData = resultData
    }

    constructor(
        FileId: String,
        DataLength: String,
        resultData: String,
        resultSignatureData: String,
        cardType: String
    ) {
        this.FileId = FileId
        this.DataLength = DataLength
        if (cardType == "Tacho")
            this.SignatureDataLength = convertHex(128)
        else
            this.SignatureDataLength = convertHex(64)
        this.resultData = resultData
        this.resultSignatureData = resultSignatureData
    }

    fun toStringData(lengh: Int): String {
        if (resultData.isEmpty()) return ""
        return if (lengh > 255) {
            FileId + "00" + DataLength + resultData
        } else
            FileId + "00" + DataLength + resultData.substring(20, resultData.length - 4)
    }

    fun toStringDataG2(lengh: Int): String {
        if (resultData.isEmpty()) return ""
        return if (lengh > 255) {
            FileId + "02" + DataLength + resultData
        } else
            FileId + "02" + DataLength + resultData.substring(20, resultData.length - 4)
    }

    fun toStringData(lengh: Int, Appendix: String): String {
        if (resultData.isEmpty()) return ""

        return if (lengh > 255) {
            FileId + "0" + Appendix + DataLength + resultData
        } else
            FileId + "0" + Appendix + DataLength + resultData.substring(20, resultData.length - 4)
    }

    fun toStringSignatureData(): String {
        if (resultData.isEmpty()) return ""
        return FileId + "01" + SignatureDataLength + resultSignatureData
    }

    fun toStringSignatureData(Appendix: String): String {
        if (resultData.isEmpty()) return ""
        return FileId + "0" + Appendix + SignatureDataLength + resultSignatureData
    }

    fun toString(lengh: Int): String {
        if (resultData.isEmpty()) return ""
        return toStringData(lengh) + toStringSignatureData()
    }

    fun toString(lengh: Int, Appendix: Int): String {
        if (resultData.isEmpty()) return ""
        return toStringData(
            lengh,
            (Appendix - 1).toString()
        ) + toStringSignatureData(Appendix.toString())
    }

    fun convertHex(n: Int): String {
        return String.format("%04X", n)
    }

}