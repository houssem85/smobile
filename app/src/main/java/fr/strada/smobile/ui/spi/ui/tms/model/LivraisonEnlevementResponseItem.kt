package fr.strada.smobile.ui.spi.ui.tms.model


import com.google.gson.annotations.SerializedName

data class LivraisonEnlevementResponseItem(
    @SerializedName("enlevementLivraison")
    val enlevementLivraison: ArrayList<EnlevementLivraison>?
)