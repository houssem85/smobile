package fr.strada.smobile.ui.absencehistory.absence_refusee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_absence_accepted.view.txt_end_date
import kotlinx.android.synthetic.main.item_absence_accepted.view.txt_start_date
import kotlinx.android.synthetic.main.item_absence_accepted.view.txt_type_absence
import kotlinx.android.synthetic.main.item_absence_refused.view.*

class DemandesAbsencesRefuseesAdapter (
    val context: Context?,
    val items: List<AbsenceModel>

) :
    RecyclerView.Adapter<ViewHolderAbsenceRefused>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsenceRefused {

        return ViewHolderAbsenceRefused(
            LayoutInflater.from(context).inflate(
                R.layout.item_absence_refused,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: ViewHolderAbsenceRefused, position: Int) {
        holder.txtNBOfDays?.text = items.get(position).nbrJour
        holder.txtStartDate?.text = context!!.getString(R.string.du)+ " "+items.get(position).startDate
        holder.txtEndDate?.text = context!!.getString(R.string.au) + " "+items.get(position).endDate
        holder.txtTypeAbsence?.text = items.get(position).type
    }
}


class ViewHolderAbsenceRefused(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtNBOfDays = itemView.txt_nbr_jour
    var txtStartDate = itemView.txt_start_date
    var txtEndDate = itemView.txt_end_date
    var txtTypeAbsence = itemView.txt_type_absence
    var view = itemView
}