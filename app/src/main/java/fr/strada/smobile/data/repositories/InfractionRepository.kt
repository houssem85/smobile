package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.infractions.DetailsInfraction
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.utils.Resource

interface InfractionRepository {

    suspend fun getInfractionsCategories(): Resource<List<InfractionsCategorie>>

    suspend fun getInfractions(dateDebut : String?,dateFin : String?,infractionClass: Int?): Resource<List<Infraction>>

    suspend fun getDetailsInfraction(infractionId:String): Resource<DetailsInfraction>

    suspend fun updateInfraction(infractionId:String,commentaire : String): Resource<Boolean>
}