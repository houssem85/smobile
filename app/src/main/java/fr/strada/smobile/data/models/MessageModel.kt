package fr.strada.smobile.data.models

data class MessageModel(

    val messageContent: String? = null,
    val objet: String? = null,
    val heureEnvoi: String? = null,
    val dateEnvoi: String? = null,
    val imageProfile: Int? = null,
    val imageTypeOfMessage: Int? = null,
    val typeOfMessage: String? = null,
    val titleMessagePredefini: String? = null,
    val nameSender: String? = null

)