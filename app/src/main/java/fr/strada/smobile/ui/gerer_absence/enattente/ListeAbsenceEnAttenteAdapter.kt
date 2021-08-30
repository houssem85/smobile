package fr.strada.smobile.ui.gerer_absence.enattente

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.*

class ListeAbsenceEnAttenteAdapter (val items: List<AbsenceModel>,
                                    val context: Context?) :
    RecyclerView.Adapter<ViewHolderAbsenceEnAttente>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsenceEnAttente {

        return ViewHolderAbsenceEnAttente(
            LayoutInflater.from(context).inflate(
                R.layout.item_liste_absence_en_attente,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderAbsenceEnAttente, position: Int) {
        holder.txtStartDate?.text = items.get(position).startDate
        holder.txtType?.text = items.get(position).type
        holder.txtNbJour?.text = items.get(position).nbrJour
        holder.txtName?.text = items.get(position).author

    }

}


class ViewHolderAbsenceEnAttente(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
    var view = itemView


}