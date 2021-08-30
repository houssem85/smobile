package fr.strada.smobile.data.models

import com.shrikanthravi.collapsiblecalendarview.data.Day

data class DepenseModel(
    var type:String,
    var date:Day? = null,
    var montant:Double,
    var fileIsJoined:Boolean? = null,
    var commentIsSet: Boolean? = null

)