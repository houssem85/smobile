package fr.strada.smobile.ui.pointeuse.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.Activitie
import kotlinx.android.synthetic.main.item_date.view.*

class ActivitieAdapter(val context: Context): RecyclerView.Adapter<ActivitieAdapter.ViewHolder>(){

    val items = ArrayList<Activitie>()

    fun setData(data : List<Activitie>)
    {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_date, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        model.heureDebut?.let {
            it.hours.let { hours ->
                it.minutes.let { minutes ->
                    holder.txtStartDate.text =
                        String.format("%02d:%02d", hours.toInt(), minutes.toInt())
                }
            }
        }
        model.heureFin?.let {
            it.hours.let { hours ->
                it.minutes.let { minutes ->
                    holder.txtEndDate.text =
                        String.format("%02d:%02d", hours.toInt(), minutes.toInt())
                }
            }
        }
        model.dureeActivite?.let {
            it.hours.let { hours ->
                it.minutes.let { minutes ->
                    holder.txtDuration.text =
                        String.format("%02d:%02d", hours.toInt(), minutes.toInt())
                }
            }
        }
        model.typeActivitePointeuse?.let { typeActivitePointeuseModel ->
            typeActivitePointeuseModel.codeCouleur?.let {
                try {
/*                    holder.roundCornerLayout1.backgroundColor = Color.parseColor(it.take(7))
                    holder.roundCornerLayout2.backgroundColor = Color.parseColor(it.take(7))
                    holder.roundCornerLayout3.backgroundColor = Color.parseColor(it.take(7))
                    holder.roundCornerLayout4.backgroundColor = Color.parseColor(it.take(7))*/
                    holder.imageView.setBackgroundColor(Color.parseColor(it.take(7)))

                } catch (ex: Exception) {
                }
            }
            typeActivitePointeuseModel.icone?.let {
                Glide.with(context).load(BuildConfig.URL_BASE_TIME + it).into(holder.imageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtStartDate = itemView.txt_start_date
        var txtEndDate = itemView.txt_end_date
        var txtDuration = itemView.txt_duration
        var roundCornerLayout1 = itemView.roundCornerLayout1
        var roundCornerLayout2= itemView.roundCornerLayout2
        var roundCornerLayout3= itemView.roundCornerLayout3
        var roundCornerLayout4= itemView.roundCornerLayout4
        var view = itemView
        var imageView= itemView.imageView
    }
}