package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.activites.day.ActivitesJournaliere
import fr.strada.smobile.data.models.activites.month.ActivitesMensuel
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.utils.Resource

interface MesActivitesRepository {

    suspend fun getActivitesMensuel(year:Int, month:Int): Resource<ActivitesMensuel>

    suspend fun getDailyActivitesMensuel(year:Int, month:Int): Resource<List<String>>

    suspend fun getActivitesHebdomadaire(year:Int,month:Int,dateStart : String ,dateEnd : String): Resource<ActivitesHebdomadaire>

    suspend fun getActivitesJournaliere(jour:String): Resource<ActivitesJournaliere>
}