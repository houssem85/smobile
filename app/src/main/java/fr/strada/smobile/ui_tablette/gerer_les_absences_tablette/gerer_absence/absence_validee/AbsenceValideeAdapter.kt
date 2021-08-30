package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_name
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_nb_jour
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_type
import kotlinx.android.synthetic.main.item_liste_absence_validee.view.*

class AbsenceValideeAdapter(
    val context: Context?
) :
    RecyclerView.Adapter<ViewHolderAbsenceValidee>() {

    var items: ArrayList<AbsenceModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsenceValidee {

        return ViewHolderAbsenceValidee(
            LayoutInflater.from(context).inflate(
                R.layout.item_liste_absence_validee,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderAbsenceValidee, position: Int) {
        holder.txtStartDate?.text = items[position].startDate
        holder.txtType?.text = items[position].type
        holder.txtNbJour?.text = items[position].nbrJour  + " "+ context!!.getString(R.string.jours)
        holder.txtName?.text = items[position].author
    }
}



class ViewHolderAbsenceValidee(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_start_date
    var txtEndDate = itemView.txt_end_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
    var view = itemView
}