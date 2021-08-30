package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.utils.listner.ItemAbsenceAValiderLisnter
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.*

class AbsenceAValiderAdapter(
    val context: Context?,val itemAbsenceAValiderLisnter: ItemAbsenceAValiderLisnter
) :
    RecyclerView.Adapter<ViewHolderAbsenceAValider>() {

    var items: ArrayList<AbsenceModel> = ArrayList()

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
        holder.txtType?.text = items[position].type
        holder.txtStartDate?.text = items[position].startDate
        holder.txtNbJour?.text = items[position].nbrJour
        holder.txtName?.text = items[position].author

        holder.item.setOnClickListener {
            itemAbsenceAValiderLisnter.onItemClick(position)
        }
    }

}



class ViewHolderAbsenceAValider(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var item = itemView.drag_item
    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
}