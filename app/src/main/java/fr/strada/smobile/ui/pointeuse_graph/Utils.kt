package fr.strada.smobile.ui.pointeuse_graph

import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import fr.strada.smobile.data.models.pointeuse.TimeModel

fun convertActivitesPointeuseToBareData(activites: List<TimeModel>) : BarData {
    val barData = BarData()
    barData.barWidth = (1/60F)
    activites.forEach {
        it.duree?.let { duree ->
            try {
                val bareEntrys = ArrayList<BarEntry>()
                var index = it.heureDebutActivite!!.totalHours
                val end = it.heureFinActivite!!.totalHours
                while (index < end) {
                    val y = 5F
                    val bareEntry = BarEntry(index, y)
                    bareEntrys.add(bareEntry)
                    index += (1/60F)
                }
                val barDataSet = BarDataSet(bareEntrys, "")
                barDataSet.color = Color.parseColor(it.typeActivite!!.codeCouleur!!.take(7))
                barDataSet.setDrawValues(false)
                barData.addDataSet(barDataSet)
            }catch (ex: Exception){ }
        }
    }
    return barData
}