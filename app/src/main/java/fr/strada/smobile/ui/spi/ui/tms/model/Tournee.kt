package fr.strada.smobile.ui.spi.ui.tms.model


import com.google.gson.annotations.SerializedName

data class Tournee (

    @SerializedName("hasNextPage")
    val hasNextPage: Boolean?,
    @SerializedName("hasPreviousPage")
    val hasPreviousPage: Boolean?,
    @SerializedName("items")
    val items:ArrayList<TourneeItem>,
    @SerializedName("itemsCount")
    val itemsCount: Int?,
    @SerializedName("pageCount")
    val pageCount: Int?,
    @SerializedName("pageIndex")
    val pageIndex: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?

)