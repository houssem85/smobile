package fr.strada.smobile.ui.activities.journalier

import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.data.models.activites.day.Activite
import fr.strada.smobile.data.models.activites.day.listOfTypesActiviteConduite
import fr.strada.smobile.ui.activities.Utils.getDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun getNextDay(day: Day): Day {
    val calendar = Calendar.getInstance()
    calendar.clear()
    calendar.set(Calendar.YEAR, day.year)
    calendar.set(Calendar.MONTH, day.month)
    calendar.set(Calendar.DAY_OF_MONTH, day.day)
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return Day(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

fun getPreviusDay(day: Day): Day {
    val calendar = Calendar.getInstance()
    calendar.clear()
    calendar.set(Calendar.YEAR, day.year)
    calendar.set(Calendar.MONTH, day.month)
    calendar.set(Calendar.DAY_OF_MONTH, day.day)
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return Day(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

fun getStrDate(year: Int, month: Int, day: Int, locale: Locale):String{
    val sf = SimpleDateFormat("yyyy-MM-dd", locale)
    val date= getDate(year, month, day, locale)
    return sf.format(date)
}

fun convertActivitesPointeuseConduiteToBareData(activites: List<Activite>) : BarData{
    val barData = BarData()
    barData.barWidth = (1/60F)
    activites.forEach {
        it.duree?.let { duree ->
            val isActivitePointeuse = it.typeActivitePointeuse != null
            val bareEntrys = ArrayList<BarEntry>()
            var index = it.heureDebut.totalHours
            val end = it.heureFin.totalHours
            while (index < end) {
                var y = 5F
                if(!isActivitePointeuse){
                    listOfTypesActiviteConduite.forEach { type ->
                        if(it.typeActiviteConduite!!.libelle == type.libelle) {
                            y =  type.height
                        }
                    }
                }
                val bareEntry = BarEntry(index, y)
                bareEntrys.add(bareEntry)
                index += (1/60F)
            }
            val barDataSet = BarDataSet(bareEntrys, "")
            if(isActivitePointeuse){
                try {
                    barDataSet.color = Color.parseColor(it.typeActivitePointeuse!!.codeCouleur!!.take(7))
                }catch (ex: Exception){ }
            }else{
                listOfTypesActiviteConduite.forEach { type ->
                    try{
                        if(it.typeActiviteConduite!!.libelle == type.libelle) {
                            barDataSet.color = Color.parseColor(type.couleur)
                        }
                    }catch (ex: Exception){ }
                }
            }
            barDataSet.setDrawValues(false)
            barData.addDataSet(barDataSet)
        }
    }
    return barData
}
