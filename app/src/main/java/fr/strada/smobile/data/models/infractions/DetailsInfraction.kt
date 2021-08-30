package fr.strada.smobile.data.models.infractions

data class DetailsInfraction(
    val dateInfraction : String,
    val heureInfraction : Value ,
    val dureeAutorisee : Value ,
    val dureeEffectuee : Value ,
    val reposAutorisee : Value ,
    val reposEffectuee : Value ,
    val infractionTextLoi : String ,
    val justificatifsInfraction : List<String> ,
    val commentaire : String? ,
)