package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.infractions.DetailsInfraction
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.data.models.infractions.Value
import fr.strada.smobile.utils.Resource

class FakeInfractionRepository : InfractionRepository {

    override suspend fun getInfractionsCategories(): Resource<List<InfractionsCategorie>> {
        val categorie1 = InfractionsCategorie("146a1263-3fd3-4975-b1b2-6b5f8f85ad12","CODE1","Code1")
        val categorie2 = InfractionsCategorie("146a1263-3fd3-4975-b1b2-6b5f8f85ad12","CODE2","Code2")
        val categorie3 = InfractionsCategorie("146a1263-3fd3-4975-b1b2-6b5f8f85ad12","CODE3","Code3")
        return Resource.success(listOf(categorie1,categorie2,categorie3))
    }

    override suspend fun getInfractions(
        dateDebut: String?,
        dateFin: String?,
        infractionClass: Int?
    ): Resource<List<Infraction>> {
       return Resource.success(listOf())
    }

    override suspend fun getDetailsInfraction(infractionId: String): Resource<DetailsInfraction> {
        val value = Value(3600000L)
        val detailsInfraction = DetailsInfraction("2021-10-11",value,value,value,value,value,"textLoi", listOf(),"commentaire")
        return Resource.success(detailsInfraction)
    }

    override suspend fun updateInfraction(
        infractionId: String,
        commentaire: String
    ): Resource<Boolean> {
        return Resource.success(true)
    }
}