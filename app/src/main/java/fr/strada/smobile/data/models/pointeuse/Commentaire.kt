package fr.strada.smobile.data.models.pointeuse

import android.os.Parcelable
import fr.strada.smobile.ui.activities.mensuel.Utils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Commentaire(
    var userId : String? = null,
    var userName : String? = null,
    var jourActivite : String? = null,
    var commentaireId : String? = null,
    var commentaire : String? = null,
    var dateCreationCommentaire : String? = null,
):Parcelable ,Comparable<Commentaire> {
    override fun compareTo(other: Commentaire): Int {
        try {
            val myDate = Utils.fromIsoFormatToDate(dateCreationCommentaire!!)
            val otherDate = Utils.fromIsoFormatToDate(other.dateCreationCommentaire!!)
            return myDate.compareTo(otherDate)
        }catch (ex:Exception){
           return -1
        }
    }
}