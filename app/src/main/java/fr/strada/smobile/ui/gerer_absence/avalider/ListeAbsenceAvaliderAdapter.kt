package fr.strada.smobile.ui.gerer_absence.avalider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.*

class ListeAbsenceAvaliderAdapter (val items: List<AbsenceModel>,
                                   val context: Context?) :
    RecyclerView.Adapter<ViewHolderAbsenceAValider>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsenceAValider {

        return ViewHolderAbsenceAValider(
            LayoutInflater.from(context).inflate(
                R.layout.item_liste_absence_avalider,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderAbsenceAValider, position: Int) {
        holder.txtStartDate?.text = items.get(position).startDate
        holder.txtType?.text = items.get(position).type
        holder.txtNbJour?.text = items.get(position).nbrJour
        holder.txtName?.text = items.get(position).author

    }

}


class ViewHolderAbsenceAValider(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
    var view = itemView
}