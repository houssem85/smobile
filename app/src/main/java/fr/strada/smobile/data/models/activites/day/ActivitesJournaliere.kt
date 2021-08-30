package fr.strada.smobile.data.models.activites.day

data class ActivitesJournaliere(
    val activitesPointeuse: List<Activite> ,
    val activitesConduite: List<Activite>
)