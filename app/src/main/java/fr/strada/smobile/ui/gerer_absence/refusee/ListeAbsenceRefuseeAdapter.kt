package fr.strada.smobile.ui.gerer_absence.refusee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.*
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_name
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_nb_jour
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.txt_type
import kotlinx.android.synthetic.main.item_liste_absence_refusee.view.*

class ListeAbsenceRefuseeAdapter (val items: List<AbsenceModel>,
                                  val context: Context?) :
    RecyclerView.Adapter<ViewHolderAbsenceRefuser>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsenceRefuser {

        return ViewHolderAbsenceRefuser(
            LayoutInflater.from(context).inflate(
                R.layout.item_liste_absence_refusee,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderAbsenceRefuser, position: Int) {
        holder.txtStartDate?.text = items.get(position).startDate
        holder.txtEndDate?.text = items.get(position).endDate
        holder.txtType?.text = items.get(position).type
        holder.txtNbJour?.text = items.get(position).nbrJour + " "+ context!!.getString(R.string.jours)
        holder.txtName?.text = items.get(position).author
    }
}


class ViewHolderAbsenceRefuser(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_date
    var txtEndDate = itemView.txt_end_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
    var view = itemView


}