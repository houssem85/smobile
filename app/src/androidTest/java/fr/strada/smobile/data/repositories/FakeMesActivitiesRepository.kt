package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.activites.Value
import fr.strada.smobile.data.models.activites.day.Activite
import fr.strada.smobile.data.models.activites.day.ActivitesJournaliere
import fr.strada.smobile.data.models.activites.month.ActivitesMensuel
import fr.strada.smobile.data.models.activites.month.Week
import fr.strada.smobile.data.models.activites.week.ActivitesHebdomadaire
import fr.strada.smobile.data.models.activites.week.Day
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.utils.Resource

class FakeMesActivitiesRepository : MesActivitesRepository {

    override suspend fun getActivitesMensuel(year: Int, month: Int): Resource<ActivitesMensuel> {
        val endOfTheMonth = "2021-09-31"
        val month = 9
        val serviceCumul  = Value(7200000,2F,2F,0F)
        val nuitCumul  = Value(0,0F,0F,0F)
        val startOfTheMonth = "2021-09-01"
        val year = 2021
        val week1 = Week(2,Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),"S12")
        val week2 = Week(2,Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),"S13")
        val week3 = Week(2,Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),"S14")
        return Resource.success(ActivitesMensuel(endOfTheMonth,month,serviceCumul,nuitCumul,startOfTheMonth, listOf(week1,week2,week3),year))
    }

    override suspend fun getDailyActivitesMensuel(year: Int, month: Int): Resource<List<String>> {
        val data = listOf("2021-09-01","2021-09-12","2021-09-20","2021-09-30")
        return Resource.success(data)
    }

    override suspend fun getActivitesHebdomadaire(
        year: Int,
        month: Int,
        dateStart: String,
        dateEnd: String
    ): Resource<ActivitesHebdomadaire> {
        val endOfTheWeek = "2021-09-22"
        val serviceCumul  = Value(7200000,2F,2F,0F)
        val nuitCumul  = Value(0,0F,0F,0F)
        val startOfTheWeek = "2021-09-16"
        val week = 20
        val year = 2021
        val day1 = Day("Li",Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F))
        val day2 = Day("Mar",Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F))
        val day3 = Day("Mer",Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F))
        val day4 = Day("Jeu",Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F))
        val day5 = Day("Ven",Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F),Value(7200000,2F,2F,0F))
        return Resource.success(ActivitesHebdomadaire(listOf(day1,day2,day3,day4,day5),endOfTheWeek,serviceCumul,nuitCumul,startOfTheWeek,week,year))
    }

    override suspend fun getActivitesJournaliere(jour: String): Resource<ActivitesJournaliere> {
        val typeActivitie = TypeActivitePointeuseModel("146a1263-3fd3-4975-b1b2-6b5f8f85ad12","Repos","Repos","","")
        val value  = Value(7200000,2F,2F,0F)
        val activite1 = Activite(typeActivitie,value,value,value,null)
        val activite2 = Activite(typeActivitie,value,value,value,null)
        val activite3 = Activite(typeActivitie,value,value,value,null)
        val activite4 = Activite(typeActivitie,value,value,value,null)
        val activite5 = Activite(typeActivitie,value,value,value,null)
        val data = ActivitesJournaliere(listOf(activite1,activite2,activite3,activite4,activite5), listOf())
        return Resource.success(data)
    }
}