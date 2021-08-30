package fr.strada.smobile.data.models

data class InfractionModel(

    var idInfraction: Int,
    var type: String,
    var subType: String? = null,
    var nbAttachement: Int? = null,
    var icAttachement: Int? = null
)