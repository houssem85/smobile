package fr.strada.smobile.data.models.access_functionalities

enum class Functionality(val code : String) {
    MES_ACTIVITES("mesActivites"),
    LECTURE_CARTE_CONDUCTEUR("lectureCarteConducteur"),
    POINTEUSE("pointeuseSmboile"),
    MES_ACTIVITES_MODE_BORNE("mesActivitesModeBorne"),
    LECTURE_CARTE_CONDUCTEUR_MODE_BORNE("lectureCarteConducteurModeBorne"),
    POINTEUSE_MODE_BORNE("pointeuseSmboileModeBorne")
}

/**
 * sorted functionalities tablette menu
 */
val sortedFunctionalities = listOf(Functionality.POINTEUSE,Functionality.MES_ACTIVITES,Functionality.LECTURE_CARTE_CONDUCTEUR)