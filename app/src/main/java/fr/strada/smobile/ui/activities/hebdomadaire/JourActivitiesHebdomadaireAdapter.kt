package fr.strada.smobile.ui.activities.hebdomadaire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.activites.week.Day
import fr.strada.smobile.ui.activities.mensuel.Utils.millisToTime
import fr.strada.smobile.utils.listner.ItemJourListner
import kotlinx.android.synthetic.main.item_jour_activities_hebdomadaire.view.*

class JourActivitiesHebdomadaireAdapter(val context:Context,val itemJourListner: ItemJourListner):RecyclerView.Adapter<JourActivitiesHebdomadaireAdapter.JourActivitiesHebdomadaireViewHolder>(){

    val daysNames = ArrayList<String>()
    val days = ArrayList<Day>()

    fun setData(list:List<Day>){
        days.clear()
        days.addAll(list)
        daysNames.clear()
        daysNames.addAll(listOf(context.resources.getString(R.string.Lu),context.resources.getString(R.string.Ma),context.resources.getString(R.string.Me),context.resources.getString(R.string.Je),context.resources.getString(R.string.Ve),context.resources.getString(R.string.Sa),context.resources.getString(R.string.Di)))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourActivitiesHebdomadaireViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_jour_activities_hebdomadaire,parent,false)
        return JourActivitiesHebdomadaireViewHolder(view)
    }

    override fun onBindViewHolder(holder: JourActivitiesHebdomadaireViewHolder, position: Int) {
        val day = days[position]
        holder.lblJour.text = daysNames[position]
        holder.lblTempsService.text = millisToTime(day.tempsService.totalMilliseconds)
        holder.lblHeureNuit.text = millisToTime(day.heureNuit.totalMilliseconds)
        holder.lblHeureDebut.text = millisToTime(day.heureDebut.totalMilliseconds)
        holder.lblHeureFin.text = millisToTime(day.heureFin.totalMilliseconds)
        holder.view.setOnClickListener {
            itemJourListner.onClickListner(position)
        }
    }

    override fun getItemCount(): Int {
       return days.size
    }

    class JourActivitiesHebdomadaireViewHolder(view: View):RecyclerView.ViewHolder(view){
       val lblJour= view.lblJour
       val view = view
       val lblTempsService = view.lblTempsService
       val lblHeureDebut= view.lblHeureDebut
       val lblHeureFin = view.lblHeureFin
       val lblHeureNuit = view.lblHeureNuit
    }
}