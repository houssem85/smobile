package fr.strada.smobile.data.models

data class AbsenceModel(

    val nbrJour: String,
    val statusValidation: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val color: String? = null,
    val image: Int? = null,
    val duration: Int? = null,
    val type: String? = null,
    val author: String? = null

)
