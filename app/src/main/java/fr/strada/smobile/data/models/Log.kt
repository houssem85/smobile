package fr.strada.smobile.data.models

data class Log (
    var udid : String = "",
    var email: String = "guest",
    var deviceName: String = "",
    var lecteurName: String = "",
    var serialNumber: String = "",
    var versionName: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var messages: ArrayList<String> = arrayListOf<String>(),
    var readingCardSteps: ArrayList<String> = arrayListOf<String>(),
    var responseCodeTFD: String = "",
    var fileName: String = "",
    var sendMessage: ArrayList<String> = arrayListOf<String>()
)