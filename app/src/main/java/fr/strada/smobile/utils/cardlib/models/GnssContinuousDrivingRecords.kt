package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

/**
 * Informations enregistrées sur une carte de conducteur ou d'atelier,
 * relatives à la position GNSS du véhicule lorsque le temps de conduite continue
 * du conducteur atteint un multiple de trois heures (exigences 305 et 353, Annexe 1C).
 */
class GnssContinuousDrivingRecords() {
   // Désigne la date et l'heure lorsque le temps de conduite continue du détenteur de la carte atteint un multiple de trois heures.
    @SerializedName("timestamp")
    var timestamp: String = ""
    //contient les informations relatives à la position du véhicule.
    @SerializedName("gnssPlaceRecord")
    var gnssPlaceRecord: GnssPlaceRecord = GnssPlaceRecord()
}