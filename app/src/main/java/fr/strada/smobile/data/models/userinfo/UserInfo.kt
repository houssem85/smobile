package fr.strada.smobile.data.models.userinfo


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("accessStartDate")
    val accessStartDate: String?,
    @SerializedName("accountValidityStartDate")
    val accountValidityStartDate: String?,
    @SerializedName("active")
    val active: Boolean?,
    @SerializedName("address1")
    val address1: String?,
    @SerializedName("address2")
    val address2: String?,
    @SerializedName("auth0Id")
    val auth0Id: String?,
    @SerializedName("authenticationMethod")
    val authenticationMethod: Int,
    @SerializedName("authentificationStatus")
    val authentificationStatus: String?,
    @SerializedName("canConnect")
    val canConnect: Boolean?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("companies")
    val companies: List<Company>,
    @SerializedName("companyIds")
    val companyIds: List<String>?,
    @SerializedName("companyNames")
    val companyNames: List<String>?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("createdDate")
    val createdDate: String?,
    @SerializedName("driver")
    val driver: Driver?,
    @SerializedName("driverId")
    val driverId: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("endDateValidityAccount")
    val endDateValidityAccount: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("languageId")
    val languageId: String?,
    @SerializedName("languageLabel")
    val languageLabel: String?,
    @SerializedName("lastConnectionDate")
    val lastConnectionDate: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("limitationPacketsPerMinutes")
    val limitationPacketsPerMinutes: Int,
    @SerializedName("listFunctionalitiesByRolesAndUser")
    val listFunctionalitiesByRolesAndUser: List<FunctionalitiesByRolesAndUser>?,
    @SerializedName("numberOfCallsPerMinute")
    val numberOfCallsPerMinute: Int?,
    @SerializedName("numberOfSimultaneousSessions")
    val numberOfSimultaneousSessions: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("photoId")
    val photoId: String?,
    @SerializedName("roleCodes")
    val roleCodes: List<String>?,
    @SerializedName("roleIds")
    val roleIds: List<String>?,
    @SerializedName("roleLabels")
    val roleLabels: List<String>?,
    @SerializedName("statusId")
    val statusId: String?,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userPhoto")
    val userPhoto: UserPhoto?,
    @SerializedName("userTypeId")
    val userTypeId: String?,
    @SerializedName("userTypeLabel")
    val userTypeLabel: String?,
    @SerializedName("zipCode")
    val zipCode: String?
)

fun UserInfo.hasTonnents() : Boolean {
    if(this.companies.isNotEmpty()){
        this.companies.forEach {
            if(it.tenants.isNotEmpty()){
                return true
            }
        }
    }
    return false
}