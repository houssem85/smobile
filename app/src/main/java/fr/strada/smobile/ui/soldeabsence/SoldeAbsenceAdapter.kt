package fr.strada.smobile.ui.soldeabsence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.fragment_absence_request.view.txt_type
import kotlinx.android.synthetic.main.item_absence.view.txt_nbr_jour
import kotlinx.android.synthetic.main.item_absence_inprogress.view.*

class SoldeAbsenceAdapter (
    val context: Context?) :
    RecyclerView.Adapter<ViewHolderSoldeAbsence>() {

    var items: ArrayList<AbsenceModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSoldeAbsence {

        return ViewHolderSoldeAbsence(
            LayoutInflater.from(context).inflate(
                R.layout.item_absence_inprogress,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderSoldeAbsence, position: Int) {

        holder.txtNbrJour?.text = items.get(position).nbrJour.toString() + " "+ context!!.getString(R.string.jours)
        holder.txtType?.text = items.get(position).type

        if (items.get(position).type == "Congé payé") {
            holder.txtStartDate?.text =
                context.getString(R.string.du) + " " + items.get(position).startDate
            holder.txtEndDate?.text =
                context.getString(R.string.au) + " " + items.get(position).endDate
        }
        else
        {
            holder.txtEndDate?.text = items.get(position).endDate
        }

    }

}


class ViewHolderSoldeAbsence(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtNbrJour = itemView.txt_nbr_jour
    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_start_date
    var txtEndDate = itemView.txt_end_date

    var view = itemView


}