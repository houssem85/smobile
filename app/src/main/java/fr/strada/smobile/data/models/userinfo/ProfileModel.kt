package fr.strada.smobile.data.models.userinfo

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    val id: String,

    @SerializedName("driverId")
    val driverID: String?,

    val lastName: String?,
    val firstName: String?,
    val maidenName: String?,
    val fullName: String?,
    val post: String?,
    val profile: String?,
    val phone: String?,
    val adress: String?,
    val zipCode: String?,
    val city: String?,
    val email: String?,
    val warning: Boolean?,
    val state: Boolean?,

    @SerializedName("groupeActiviteId")
    val groupeActiviteID: String,

    val groupeActiviteLibelle: String,
    val identifiantBorne: String,
    val motDePasseBorne: String,
    val datePremiereEmbauche: String,
    val card: Card?,
    val drivingLicenseCard: DrivingLicenseCard?
)

data class Card (
    val cn: String,
    val validityStart: String,
    val validityEnd: String,
    val unload: Unload
)

data class Unload (
    val lastUnloadDate: String,
    val nextUnloadDate: String
)

data class DrivingLicenseCard (
    val number: String,
    val autority: String,
    val country: String
)