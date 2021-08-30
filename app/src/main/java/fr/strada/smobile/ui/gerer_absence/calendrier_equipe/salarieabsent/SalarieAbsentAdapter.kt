package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.SalarieModel
import kotlinx.android.synthetic.main.item_employee_absent.view.*
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_name

class SalarieAbsentAdapter (val context: Context?) :
    RecyclerView.Adapter<ViewHolderSalarieAbsent>() {

    var items: ArrayList<SalarieModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSalarieAbsent {

        return ViewHolderSalarieAbsent(
            LayoutInflater.from(context).inflate(
                R.layout.item_employee_absent,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderSalarieAbsent, position: Int) {
        holder.txtStartDate?.text = items.get(position).startDateAbsence
        holder.txtName?.text = items.get(position).name
        holder.txtEnddate?.text = items.get(position).endDateAbsence
    }
}


class ViewHolderSalarieAbsent(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtStartDate = itemView.txt_start_date
    var txtEnddate = itemView.txt_end_date
    var txtName = itemView.txt_name
    var view = itemView
}