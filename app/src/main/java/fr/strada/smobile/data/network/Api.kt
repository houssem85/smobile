package fr.strada.smobile.data.network


import com.google.gson.JsonObject
import fr.strada.smobile.data.models.activites.day.ActivitesJournaliere
import fr.strada.smobile.data.models.activites.month.ActivitesMensuel
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.data.models.indemnite.hebdo.IndemniteHebdo
import fr.strada.smobile.data.models.indemnite.journalier.IndemniteJournalier
import fr.strada.smobile.data.models.indemnite.mensuel.IndemniteMensuel
import fr.strada.smobile.data.models.infractions.DetailsInfraction
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.models.mes_frais.TypesDepense
import fr.strada.smobile.data.models.pointeuse.*
import fr.strada.smobile.data.models.userinfo.ProfileModel
import fr.strada.smobile.data.models.userinfo.UserInfo
import fr.strada.smobile.ui.spi.ui.tms.model.EnlevementLivraison
import fr.strada.smobile.ui.spi.ui.tms.model.Tournee
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*

/**
 * Api
 *
 * @constructor Create empty Api
 */
interface Api {

    @GET("User/GetByEmail")
    suspend fun getUserInfo(
        @Header("authorization") token: String,
        @Query("email") email: String
    ): Response<UserInfo>

    @GET("Employee/Details")
    suspend fun getUserProfile(
        @Header("authorization") token: String,
        @Query("collaborateurId") collaborateurId : String
    ): Response<ProfileModel>

    @GET("ActivitePointeuse/ActivitesPointeusesMensuel")
    suspend fun getActivitesMensuel(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") conducteurId: String,
        @Query("Year") year: Int,
        @Query("MonthNumber") month: Int
    ):Response<ActivitesMensuel>

    @GET("ActivitePointeuse/DatesActivites")
    suspend fun getDailyActivitesMensuel(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") conducteurId: String,
        @Query("Year") year: Int,
        @Query("Month") month: Int
    ):Response<List<String>>

    @GET("ActivitePointeuse/ActivitesPointeusesHebdomadaire")
    suspend fun getActivitesHebdomadaire(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") conducteurId: String,
        @Query("Year") year: Int,
        @Query("WeekNumber") week: Int
    ):Response<ActivitesHebdomadaire>

    @GET("ActivitePointeuse/ActivitesJournaliere")
    suspend fun getActivitesJournaliere(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") conducteurId: String,
        @Query("Jour") jour: String,
    ):Response<ActivitesJournaliere>

    // pointeuse

    @GET("TypeActivite/ListTypeActiviteParCollaborateurId")
    suspend fun getPointeuseActiviteList(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") conducteurId: String
    ): Response<List<TypeActivitePointeuseModel>>

    @GET("ActivitePointeuse/GetDerniereActiviteParCollabId")
    suspend fun getDerniereActiviteParCollabId(
        @Header("authorization") auth: String,
        @Query("collaborateurId") collaborateurId: String
    ): Response<ActivitiePointeuse>


    @POST("ActivitePointeuse/CreateActivitePointeuse")
    suspend fun createActivitePointeuse(
        @Header("authorization") auth: String,
        @Query("TypeActivitePointeuseId") typeActivitePointeuseId: String,
        @Query("CollaborateurId") CollaborateurId: String,
        @Query("TypeLieuActiviteId") TypeLieuActiviteId: String,
        @Query("DateActivite") DateActivite: String,
        @Query("FinActivite") FinActivite: String? = null,
        @Query("LatitudeDepart") latitudeDepart: Double?,
        @Query("LongitudeDepart") longitudeDepart: Double?,
        @Query("LatitudeArrivee") latitudeArrivee: Double? = null,
        @Query("LongitudeArrivee") longitudeArrivee: Double? = null,
    ): Response<ResponseBody>

    @PUT("ActivitePointeuse/UpdateActivitePointeuseFinActivite")
    suspend fun updateActivitePointeuseDuree(
        @Header("authorization") auth: String,
        @Query("ActivitePointeuseId") ActivitePointeuseId: String,
        @Query("FinActivite") finActivite: String?,
        @Query("LatitudeArrivee") latitudeArrivee: Double?,
        @Query("LongitudeArrivee") longitudeArrivee: Double?,
    ): Response<ResponseBody>

    @GET("ActivitePointeuse/GetListActivitePointeuseParCritere")
    suspend fun getListActivitePointeuseParCritere(
        @Header("authorization") auth: String,
        @Query("collaborateurId") collaborateurId: String,
        @Query("dateActiviteDebut") dateActiviteDebut: String,
        @Query("dateActiviteFin") dateActiviteFin: String
    ): Response<List<TimeModel>>

    @GET("ActivitePointeuse/ActivitesCommentairesLastSixDays")
    suspend fun getListActivitesCommentairesLastSixDays(
        @Header("authorization") auth: String,
        @Query("collaborateurId") collaborateurId: String,
        @Query("JourActivite") JourActivite: String
    ): Response<List<JourActivite>>

    @POST("ActivitePointeuse/CreateCommentairesByJourActivitePointeuse")
    suspend fun createCommentairesByJourActivitePointeuse(
        @Header("authorization") auth: String,
        @Query("JourActivite") jourActivite: String,
        @Body commentaire: JsonObject
    ): Response<Boolean>

    @GET("ActivitePointeuse/ListCommentairesByJourActivite")
    suspend fun getListCommentairesByJourActivite(
        @Header("authorization") auth: String,
        @Query("JourActivite") jourActivite: String
    ): Response<List<Commentaire>>

    @GET("InfractionFrais/InfractionsCategories")
    suspend fun getInfractionsCategories(
        @Header("authorization") auth: String,
    ): Response<List<InfractionsCategorie>>

    @GET("InfractionFrais/InfractionsList")
    suspend fun getInfractionsByFiltre(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") collaborateurId: String,
        @Query("DateDebut") dateDebut: String? = null,
        @Query("DateFin") dateFin: String?= null,
        @Query("InfractionClass") infractionClass: Int? = null,
    ): Response<List<Infraction>>


    @GET("InfractionFrais/DetailsInfraction")
    suspend fun getDetailsInfraction(
        @Header("authorization") auth: String,
        @Query("InfractionId") infractionId: String
    ): Response <DetailsInfraction>

    @PUT("InfractionFrais/UpdateInfraction")
    suspend fun updateInfraction(
        @Header("authorization") auth: String,
        @Query("InfractionId") infractionId: String,
        @Body raw : JsonObject
    ): Response <Boolean>

    @GET("InfractionFrais/IndemniteJournalier")
    suspend fun getIndemniteJournalier(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") collaborateurId: String,
        @Query("Jour") jour: String,
    ): Response<IndemniteJournalier>

    @GET("InfractionFrais/IndemniteHebdo")
    suspend fun getIndemnitehebdo(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") collaborateurId: String,
        @Query("DateDebut") dateDebut: String,
        @Query("DateFin") dateFin: String
    ): Response<IndemniteHebdo>

    @GET("InfractionFrais/IndemniteMensuel")
    suspend fun getIndemniteMensuell(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") collaborateurId: String,
        @Query("DateDebut") dateDebut: String,
        @Query("DateFin") dateFin: String
    ): Response<IndemniteMensuel>

    @GET("InfractionFrais/ListNoteFrais")
    suspend fun getListNoteFrais(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") collaborateurId: String,
        @Query("DateDebut") dateDebut: String?,
        @Query("DateFin") dateFin: String?,
        @Query("EnCours") enCours: Boolean?
    ): Response<List<NoteFrais>>

    @GET("InfractionFrais/TypesDepense")
    suspend fun getListTypesDepense(
        @Header("authorization") auth: String,
    ): Response<List<TypesDepense>>

    @POST("InfractionFrais/CreateNoteFrais")
    suspend fun createNoteFrais(
        @Header("authorization") auth: String,
        @Body noteFrais: NoteFrais
    ): Response<ResponseBody>

    @DELETE("InfractionFrais/DeleteNoteFrais")
    suspend fun deleteNoteFrais(
        @Header("authorization") auth: String,
        @Query("NoteFraisId") noteFraisId: String,
    ): Response<ResponseBody>

    @PUT("/InfractionFrais/UpdateNoteFrais")
    suspend fun updateNoteFrais(
        @Header("authorization") auth: String,
        @Query("NoteFraisId") NoteFraisId: String,
        @Body NoteFrais: NoteFrais
    ): Response<ResponseBody>


    @Multipart
    @POST ("/File/TachoFileUpload")
    suspend fun uploadTachofile(
        @Header("authorization") auth: String,
        @Part  file : MultipartBody.Part
    ):Response<ResponseBody>

    @Multipart
    @POST ("/File/PostMetadata")
    suspend fun uploadDocument(
        @Header("authorization") auth: String,
        @Query("CustomerId") CustomerId: String,
        @Query("FileType") FileType : Int,
        @Part IFormFile  : MultipartBody.Part,
    ):Response<ResponseBody>



    @GET("/api/tms/Planning/GetListTourneesDansPlanningSPI")
    suspend fun getListTournee(
        @Header("authorization") auth: String,
        @Query("CollaborateurId") collaborateurId: String,
        @Query ("PageIndex") indexpage : Int ,
        @Query ("PageSize") pageSize : Int
    ):Response<Tournee>
    @GET("/api/tms/Planning/GetListCommandByIdTourneeSPI")
    suspend fun getDetailCommande(
        @Header("authorization") auth: String,
        @Query("tourneID") tourneeId: String,
    ):Response<ArrayList<EnlevementLivraison>>

}