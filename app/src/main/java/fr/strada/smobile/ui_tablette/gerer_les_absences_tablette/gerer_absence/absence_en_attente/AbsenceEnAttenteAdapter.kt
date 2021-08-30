package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.*

class AbsenceEnAttenteAdapter(
    val context: Context?
) :
    RecyclerView.Adapter<ViewHolderAbsenceEnAttente>() {

    var items: ArrayList<AbsenceModel> = ArrayList()

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
        holder.txtStartDate?.text = items[position].startDate
        holder.txtType?.text = items[position].type
        holder.txtNbJour?.text = items[position].nbrJour
        holder.txtName?.text = items[position].author
    }
}


class ViewHolderAbsenceEnAttente(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
    var view = itemView
}