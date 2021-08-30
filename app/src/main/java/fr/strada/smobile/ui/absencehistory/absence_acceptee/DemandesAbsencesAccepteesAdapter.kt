package fr.strada.smobile.ui.absencehistory.absence_acceptee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_absence_accepted.view.*
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_nb_jour

class DemandesAbsencesAccepteesAdapter(
    val context: Context?,
    val items: List<AbsenceModel>

) :
    RecyclerView.Adapter<ViewHolderAbsenceAccepted>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsenceAccepted {

        return ViewHolderAbsenceAccepted(
            LayoutInflater.from(context).inflate(
                R.layout.item_absence_accepted,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: ViewHolderAbsenceAccepted, position: Int) {
        holder.txtNBOfDays?.text = items.get(position).nbrJour
        holder.txtStartDate?.text = context!!.getString(R.string.du)+ " "+items.get(position).startDate
        holder.txtEndDate?.text = context!!.getString(R.string.au) + " "+items.get(position).endDate
        holder.txtTypeAbsence?.text = items.get(position).type
    }
}


class ViewHolderAbsenceAccepted(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtNBOfDays = itemView.txt_nb_jour
    var txtStartDate = itemView.txt_start_date
    var txtEndDate = itemView.txt_end_date
    var txtTypeAbsence = itemView.txt_type_absence
    var view = itemView
}