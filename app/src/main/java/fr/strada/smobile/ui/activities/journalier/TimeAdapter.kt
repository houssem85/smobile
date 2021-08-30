package fr.strada.smobile.ui.activities.journalier

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.data.models.activites.day.Activite
import fr.strada.smobile.data.models.activites.day.listOfTypesActiviteConduite
import fr.strada.smobile.ui.pointeuse.millisToTime
import kotlinx.android.synthetic.main.item_date.view.*

class TimeAdapter (val context: Context?) : RecyclerView.Adapter<ViewHolderTime>() {

    var items = ArrayList<Activite>()

    fun setData(list:List<Activite>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTime {

        return ViewHolderTime(
            LayoutInflater.from(context).inflate(
                R.layout.item_date ,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return items.size

    }
    override fun onBindViewHolder(holder: ViewHolderTime, position: Int) {
        val activite = items[position]
        holder.txtStartDate.text = millisToTime(activite.heureDebut.totalMilliseconds)
        holder.txtEndDate?.text = millisToTime(activite.heureFin.totalMilliseconds)
        activite.duree?.let {
            holder.txtDuration?.text = millisToTime(it.totalMilliseconds)
        }
        activite.typeActiviteConduite?.let {
            if(it.codeCouleur==null){
                listOfTypesActiviteConduite.forEach { type->
                    if(type.libelle == it.libelle){
                        try{
                            Glide.with(holder.imageView.context).load("").skipMemoryCache(true).into( holder.imageView)

                            holder.imageView.setBackgroundColor(Color.parseColor(type.couleur))
                        }catch (ex:Exception){ }
                    }
                }
            }
        }
        activite.typeActivitePointeuse?.let { it ->
            it.codeCouleur?.let {
                try {

                    holder.imageView.setBackgroundColor(Color.parseColor(it.take(7)))
                }catch (ex:Exception){ }
            }
            it.icone?.let {
               Glide.with(holder.imageView.context).load(BuildConfig.URL_BASE_TIME + it).skipMemoryCache(true).into( holder.imageView)
            }
        }
    }
}
class ViewHolderTime(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtStartDate = itemView.txt_start_date
    var txtEndDate = itemView.txt_end_date
    var txtDuration = itemView.txt_duration
    var view = itemView
    var imageView= itemView.imageView
}