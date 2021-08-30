package fr.strada.smobile.data.models.pointeuse

import fr.strada.smobile.data.models.activites.Value

data class TimeModel (
    var id: String? = null,
    var heureDebutActivite: Value? = null,
    var heureFinActivite: Value? = null,
    var duree: Value? = null,
    var typeActivite: TypeActivitePointeuseModel? = null,
    var dateActivite: String? = null,
    var finActivite: String? = null
)