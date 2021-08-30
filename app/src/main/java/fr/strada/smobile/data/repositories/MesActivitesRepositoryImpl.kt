package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.R
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class MesActivitesRepositoryImpl @Inject constructor(@Named("api_gateway")val api: Api,
                                                 private val credentialsManager: CredentialsManager,
                                                 private val userPreferences : UserPreferences,
                                                 private val internetConnectionChecker: InternetConnectionChecker,
                                                 private val context : Application
) : MesActivitesRepository{

    /**
     * @param year ex: 2021
     * @param month ex: january = 1
     */
    override suspend fun getActivitesMensuel(year:Int, month:Int)  = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getActivitesMensuel("Bearer $token",collaborateurId,year,month)
            if(res.code() == 200){
                Resource.success(res.body())
            }else{
                Resource.error(res.code().toString(),null)
            }
        }else{
            Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }

    /**
     * @param year ex: 2021
     * @param month ex: january = 1
     */
    override suspend fun getDailyActivitesMensuel(year:Int, month:Int)  = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getDailyActivitesMensuel("Bearer $token",collaborateurId,year,month)
            if(res.code() == 200){
                Resource.success(res.body())
            }else{
                Resource.error(res.code().toString(),null)
            }
        }else{
            Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }


    override suspend fun getActivitesHebdomadaire(year:Int, month:Int, dateStart : String, dateEnd : String):Resource<ActivitesHebdomadaire>{
        try {
            if(internetConnectionChecker.check()){
                val token = credentialsManager.getToken()
                val collaborateurId = userPreferences.collaborateurId.first()
                val res = api.getActivitesMensuel("Bearer $token",collaborateurId,year,month)
                if(res.code() == 200)
                {   val activitesMensuel = res.body()!!
                    activitesMensuel.weeks.forEach {
                        val week = it.weekNumber.replace("S","").toInt()
                        val res = api.getActivitesHebdomadaire("Bearer $token",collaborateurId,year,week)
                        if(res.code() == 200){
                            val activitesHebdomadaire = res.body()!!
                            if(activitesHebdomadaire.startOfTheWeek.contains(dateStart) && activitesHebdomadaire.endOfTheWeek.contains(dateEnd)){
                                return Resource.success(activitesHebdomadaire)
                            }
                        }
                    }
                    return getActivitesHebdomadaire(year,month-1,dateStart,dateEnd)
                }else{
                    return Resource.error(res.code().toString(),null)
                }
            }else{
                return Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
            }
        }catch (ex:Exception){
            return Resource.error(ex.message.toString(),null)
        }
    }

    override suspend fun getActivitesJournaliere(jour:String)  = try {
        if(internetConnectionChecker.check()){
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getActivitesJournaliere("Bearer $token",collaborateurId,jour)
            if(res.code() == 200){
                Resource.success(res.body())
            }else{
                Resource.error(res.code().toString(),null)
            }
        }else{
            Resource.error(context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),null)
        }
    }catch (ex:Exception){
        Resource.error(ex.message.toString(),null)
    }
}