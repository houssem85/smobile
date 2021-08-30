package fr.strada.smobile.data.models.activites.day

import fr.strada.smobile.data.models.activites.Value
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel

data class Activite(
    val typeActiviteConduite: TypeActivitePointeuseModel?,
    val heureDebut : Value,
    val heureFin : Value,
    val duree: Value? = null,
    val typeActivitePointeuse: TypeActivitePointeuseModel?,
)