package fr.strada.smobile.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_absence.view.*

class AbsenceAdapter (
    val items: ArrayList<AbsenceModel>,
    val context: Context?) :
    RecyclerView.Adapter<ViewHolderAbsence>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsence {

        return ViewHolderAbsence(
            LayoutInflater.from(context).inflate(
                R.layout.item_absence,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderAbsence, position: Int) {
        holder.txtNbrJour?.text = items[position].nbrJour + context!!.getString(R.string.jours)
        holder.bg?.setBackgroundColor(Color.parseColor(items[position].color))
        holder.imgAbsence?.setBackgroundResource(items[position].image!!)
        holder.txtStatus?.text = items[position].statusValidation
    }
}

class ViewHolderAbsence(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtNbrJour = itemView.txt_nbr_jour
    var imgAbsence = itemView.img_absence
    var bg = itemView.bg_nbr
    var txtStatus = itemView.txt_status_absence
    var view = itemView
}