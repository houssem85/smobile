package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.storage.CredentialsManager
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.ActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.LocalActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.TimeModel
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.data.network.PointeuseDao
import fr.strada.smobile.ui.pointeuse.PointeuseWorker
import fr.strada.smobile.utils.ISO_DATE_PATTERN
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.convertUTCtoLocalTime
import fr.strada.smobile.utils.data_stores.UserPreferences
import fr.strada.smobile.utils.ext.getToken
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class PointeuseRepository @Inject constructor(
    @Named("api_gateway") val api: Api,
    private val pointeuseDao: PointeuseDao,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val credentialsManager: CredentialsManager,
    private val userPreferences: UserPreferences,
    private val context: Application
) {
    suspend fun getListTypeActivitePointeuse(): Resource<List<TypeActivitePointeuseModel>> {
        try {
            if (internetConnectionChecker.check()) {
                val token = credentialsManager.getToken()
                val collaborateurId = userPreferences.collaborateurId.first()
                val res = api.getPointeuseActiviteList("Bearer $token", collaborateurId)
                if (res.code() == 200) {
                    val list = res.body()!!
                    pointeuseDao.deleteAllListTypeActivitePointeuse()
                    pointeuseDao.insertListTypeActivitePointeuse(list)
                } else {
                    return Resource.error(res.code().toString(), null)
                }
            }
            val localList = pointeuseDao.getListTypeActivitePointeuse()
            return Resource.success(localList)
        } catch (ex: java.lang.Exception) {
            return Resource.error(ex.message!!, null)
        }
    }

    suspend fun getDerniereActivitiePointeuse(): Resource<ActivitiePointeuse> {
        try {
            if (internetConnectionChecker.check()) {
                val token = credentialsManager.getToken()
                val collaborateurId = userPreferences.collaborateurId.first()
                val res = api.getDerniereActiviteParCollabId("Bearer $token", collaborateurId)
                if (res.code() == 200) {
                    val data = res.body()!!
                    pointeuseDao.deleteDerniereActivitePointeuse()
                    pointeuseDao.insertDerniereActivitePointeuse(data)
                } else {
                    return Resource.error(res.code().toString(), null)
                }
            }
            val localList = pointeuseDao.getDerniereActivitePointeuse()
            return if (localList.isNotEmpty()) {
                val localData = localList.first()
                Resource.success(localData)
            } else {
                Resource.error("", null)
            }
        } catch (ex: java.lang.Exception) {
            return Resource.error(ex.message!!, null)
        }
    }


    suspend fun startActivitiePointeuse(
        typeActivitePointeuseId: String,
        latitudeDepart: Double,
        longitudeDepart: Double
    ): Resource<PointeuseWorker.FutureStatus> {
        try {
            val typeLieuActiviteId = "168b71c0-a347-4b1a-9e8a-cdfcf4759e0f"
            val isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            val simpleDateFormat = SimpleDateFormat(isoDatePattern, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val now = Date()
            val dateActivite = simpleDateFormat.format(now)
            val collaborateurId = userPreferences.collaborateurId.first()
            if (internetConnectionChecker.check()) {
                val token = credentialsManager.getToken()
                val res = api.createActivitePointeuse(
                    "Bearer $token",
                    typeActivitePointeuseId,
                    collaborateurId,
                    typeLieuActiviteId,
                    dateActivite,
                    latitudeDepart = latitudeDepart,
                    longitudeDepart = longitudeDepart
                )
                if (res.code() == 200) {
                    return Resource.success(PointeuseWorker.FutureStatus.NOT_ENQUEUED)
                } else {
                    return Resource.error(res.code().toString(), null)
                }
            } else {

                val isoSimpleDateFormat = SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault())
                isoSimpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val isoDateActivite = isoSimpleDateFormat.format(now)
                val uniqueId = UUID.randomUUID().toString()
                val localActivitiePointeuse = LocalActivitiePointeuse(
                    uniqueId,
                    typeActivitePointeuseId,
                    collaborateurId,
                    typeLieuActiviteId,
                    dateActivite,
                    null,
                    latitudeDepart,
                    longitudeDepart
                )
                val typeActivitiePointeuse = pointeuseDao.getTypeActivitePointeuseById(typeActivitePointeuseId)
                val activitiePointeuse = ActivitiePointeuse(
                    uniqueId,
                    isoDateActivite,
                    null,
                    null,
                    null,
                    null,
                    typeActivitiePointeuse
                )
                pointeuseDao.insertLocalActivitePointeuse(localActivitiePointeuse)
                pointeuseDao.deleteDerniereActivitePointeuse()
                pointeuseDao.insertDerniereActivitePointeuse(activitiePointeuse)
                return Resource.success(PointeuseWorker.FutureStatus.ENQUEUED)
            }
        } catch (ex: Exception) {
            return Resource.error(ex.message!!, null)
        }
    }

    suspend fun stopActivitiePointeuse(
        id: String,
        latitudeArrivee: Double,
        longitudeArrivee: Double
    ): Resource<PointeuseWorker.FutureStatus> {
        try {
            val isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            val simpleDateFormat = SimpleDateFormat(isoDatePattern, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val now = Date()
            val finActivite = simpleDateFormat.format(now)
            if (internetConnectionChecker.check()) {
                val token = credentialsManager.getToken()
                val res = api.updateActivitePointeuseDuree(
                    "Bearer $token",
                    id,
                    finActivite,
                    latitudeArrivee,
                    longitudeArrivee
                )
                if (res.code() == 200) {
                    return Resource.success(PointeuseWorker.FutureStatus.NOT_ENQUEUED)
                } else {
                    return Resource.error(res.code().toString(), null)
                }
            } else {
                val isoSimpleDateFormat = SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault())
                isoSimpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val isoFinActivite = isoSimpleDateFormat.format(now)
                val isLocalActivitiePointeuseIsExist = pointeuseDao.isLocalActivitiePointeuseIsExist(id)
                if (isLocalActivitiePointeuseIsExist) {
                    pointeuseDao.updateLocalActivitiePointeuse(
                        id,
                        finActivite,
                        latitudeArrivee,
                        longitudeArrivee
                    )
                } else {
                    val list = pointeuseDao.getDerniereActivitePointeuse()
                    if (list.isNotEmpty()) {
                        val derniereActivitePointeuse = list.first()
                        val localActivitiePointeuse = LocalActivitiePointeuse(
                            derniereActivitePointeuse.id,
                            "",
                            "",
                            "",
                            "",
                            finActivite,
                            0.0,
                            0.0,
                            latitudeArrivee,
                            longitudeArrivee,
                            true
                        )
                        pointeuseDao.insertLocalActivitePointeuse(localActivitiePointeuse)
                    }
                }
                pointeuseDao.updateDerniereActivitePointeuse(id, isoFinActivite)
                return Resource.success(PointeuseWorker.FutureStatus.ENQUEUED)
            }
        } catch (ex: Exception) {
            return Resource.error(ex.message!!, null)
        }
    }

    suspend fun syncronisationActivitePointeuse(): Resource<Boolean> {
        try {
            val token = credentialsManager.getToken()
            val localList = pointeuseDao.getListLocalActivitiePointeuse()
            localList.forEach {
                if (it.createdInServer) {
                    val res = api.updateActivitePointeuseDuree(
                        "Bearer $token",
                        it.id,
                        it.finActivite,
                        it.latitudeArrivee,
                        it.longitudeArrivee
                    )
                    pointeuseDao.deleteLocalActivitiePointeuse(it)
                } else {
                    val res = api.createActivitePointeuse(
                        "Bearer $token",
                        it.typeActivitePointeuseId,
                        it.collaborateurId,
                        it.typeLieuActiviteId,
                        it.dateActivite,
                        it.finActivite,
                        it.latitudeDepart,
                        it.longitudeDepart,
                        it.latitudeArrivee,
                        it.longitudeArrivee
                    )
                    pointeuseDao.deleteLocalActivitiePointeuse(it)
                }
            }
            return Resource.success(true)
        } catch (ex: Exception) {
            return Resource.error(ex.message!!, null)
        }
    }

    suspend fun getListActivitePointeuseByDay(
        today: Date?,
        dayBefor: String,
        dayAfter: String
    ): Resource<List<TimeModel>> = try {
        if (internetConnectionChecker.check()) {
            val collaborateurId = userPreferences.collaborateurId.first()
            val token = credentialsManager.getToken()
            val res = api.getListActivitePointeuseParCritere(
                "Bearer $token",
                collaborateurId,
                dayBefor,
                dayAfter
            )
            if (res.code() == 200) {
                Resource.success(res.body())
            } else {
                Timber.e(res.code().toString())
                Resource.error(res.code().toString(), null)
            }
        } else {
            Resource.error(
                context.getString(R.string.Veuillez_vérifier_votre_connexion_Internet),
                null
            )
        }
    } catch (ex: Exception) {
        Timber.e(ex)
        Resource.error(ex.message.toString(), null)
    }

    suspend fun getLastSixDaysActivites(jour: String) = try {
        if (internetConnectionChecker.check()) {
            val collaborateurId = userPreferences.collaborateurId.first()
            val token = credentialsManager.getToken()
            val res =
                api.getListActivitesCommentairesLastSixDays("Bearer $token", collaborateurId, jour)
            if (res.code() == 200) {
                Resource.success(res.body()!!)
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
        Timber.e(ex)
        Resource.error(ex.message!!, null)
    }
}