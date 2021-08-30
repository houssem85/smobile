package fr.strada.smobile.ui.pointeuse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.ui.pointeuse.getAbreviationMonthFromDate
import fr.strada.smobile.utils.ext.hide
import fr.strada.smobile.utils.ext.show
import kotlinx.android.synthetic.main.item_jour_activite.view.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class JourActiviteAdapter(val context:Context,val onClick :(JourActivite) -> Unit) : RecyclerView.Adapter<JourActiviteAdapter.ViewHolder>(){

    val items = ArrayList<JourActivite>()

    fun setData(data:List<JourActivite>)
    {
        if(data.isNotEmpty())
        {
            if(items.isEmpty()){
                items.addAll(data)
                notifyDataSetChanged()
            }else
            {
                val isNotNewData = items.any { it.jourActivite == data.first().jourActivite }
                if(isNotNewData){
                    items.clear()
                }
                items.addAll(data)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_jour_activite,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        model.jourActivite?.let {
            holder.txt_day.text = it.substring(8, 10)
            holder.txt_month.text =
                getAbreviationMonthFromDate(context, it).toUpperCase(Locale.ROOT)
        }


        model.activites?.let { list ->
/*            var cumul = 0L
            list.forEach { activitie ->
                activitie.dureeActivite?.let { value ->
                    value.totalMilliseconds.let {
                        cumul += it
                    }
                }
            }*/
            /*  LocalTime.MIN.plus(
                  Duration.ofMinutes( 260L )
              ).toString()*/

            var totalMinutes = 0
            list.forEach { activitie ->
                totalMinutes += (activitie.dureeActivite!!.hours * 60 + activitie.dureeActivite!!.minutes).toInt()
            }
            val hour = totalMinutes / 60
            val min = totalMinutes % 60

            holder.txt_duration.text = LocalTime.MIN.plus(
                Duration.ofMinutes(totalMinutes.toLong())
            ).toString()
        }
        model.commentaires?.let {
            if(it.isNotEmpty()){
                holder.img_comment.show()
            }else
            {
                holder.img_comment.hide()
            }
        }

        holder.item_view.setOnClickListener {
            onClick.invoke(model)
        }
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txt_day = view.txt_day
        val txt_month = view.txt_month
        val txt_duration = view.txt_duration
        val img_comment = view.img_comment
        val item_view = view
    }
}