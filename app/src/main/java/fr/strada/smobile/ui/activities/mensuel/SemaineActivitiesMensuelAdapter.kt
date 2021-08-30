package fr.strada.smobile.ui.activities.mensuel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.activites.month.Week
import fr.strada.smobile.ui.activities.mensuel.Utils.millisToTime
import fr.strada.smobile.utils.listner.ItemSemaineListner
import kotlinx.android.synthetic.main.item_semaine_activities_mensuel.view.*

class SemaineActivitiesMensuelAdapter(val context:Context,val itemSemaineListner: ItemSemaineListner):RecyclerView.Adapter<SemaineActivitiesMensuelAdapter.SemaineActivitiesMensuelViewHolder>(){

    private var weeks = ArrayList<Week>()

    fun setData(list : List<Week>){
        weeks.clear()
        weeks.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SemaineActivitiesMensuelViewHolder
    {
        val view= LayoutInflater.from(context).inflate(R.layout.item_semaine_activities_mensuel,parent,false)
        return SemaineActivitiesMensuelViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SemaineActivitiesMensuelViewHolder, position: Int)
    {
        try {
            val week = weeks[position]
            holder.lblSemaine.text = "${context.getString(R.string.semaine)} ${week.weekNumber.replace("S","")}"
            holder.lblNombreJours.text = week.daysCount.toString()
            holder.lblTempsService.text = millisToTime(week.tempsService.totalMilliseconds)
            holder.lblHeureNuit.text = millisToTime(week.heureNuit.totalMilliseconds)
            holder.view.setOnClickListener {
                try {
                    val semaine = week.weekNumber.replace("S","").toInt()
                    itemSemaineListner.onClickListner(semaine)
                }catch (ex:Exception){ }
            }
        }catch (ex:Exception){ }
    }

    override fun getItemCount(): Int
    {
        return weeks.size
    }

    class SemaineActivitiesMensuelViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val lblSemaine= view.lblSemaine
        val lblNombreJours= view.lblNombreJours
        val lblTempsService = view.lblTempsService
        val lblHeureNuit = view.lblHeureNuit
        val view = view
    }
}