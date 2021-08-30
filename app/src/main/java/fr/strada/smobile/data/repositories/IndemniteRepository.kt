package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.R
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class IndemniteRepository @Inject constructor(
    @Named("api_gateway") val api: Api,
    private val credentialsManager: CredentialsManager,
    private val userPreferences: UserPreferences,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val context: Application
) {

    suspend fun getIndemnitejournalier(jour: String) = try {
        if (internetConnectionChecker.check()) {
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getIndemniteJournalier("Bearer $token", collaborateurId, jour)
            if (res.code() == 200) {
                Resource.success(res.body())
            } else {
                Resource.error(res.code().toString(), null)
            }
        } else {
            Resource.error(
                context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),
                null
            )
        }
    } catch (ex: Exception) {
        Resource.error(ex.message.toString(), null)
    }

    suspend fun getIndemnitehebdomadair(dateDebut: String, dateFin: String) = try {
        if (internetConnectionChecker.check()) {
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getIndemnitehebdo("Bearer $token", collaborateurId, dateDebut, dateFin)
            if (res.code() == 200) {
                Resource.success(res.body())
            } else {
                Resource.error(res.code().toString(), null)
            }
        } else {
            Resource.error(
                context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),
                null
            )
        }
    } catch (ex: Exception) {
        Resource.error(ex.message.toString(), null)
    }

    suspend fun getIndemnitemensuell(dateDebut: String, dateFin: String) = try {
        if (internetConnectionChecker.check()) {
            val token = credentialsManager.getToken()
            val collaborateurId = userPreferences.collaborateurId.first()
            val res = api.getIndemniteMensuell("Bearer $token", collaborateurId, dateDebut, dateFin)
            if (res.code() == 200) {
                Resource.success(res.body())
            } else {
                Resource.error(res.code().toString(), null)
            }
        } else {
            Resource.error(
                context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),
                null
            )
        }
    } catch (ex: Exception) {
        Resource.error(ex.message.toString(), null)
    }
}