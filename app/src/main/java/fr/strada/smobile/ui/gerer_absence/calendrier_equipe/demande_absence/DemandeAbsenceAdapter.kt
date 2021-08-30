package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.AbsenceModel
import fr.strada.smobile.utils.listner.ItemDemandeAbsenceListner
import kotlinx.android.synthetic.main.item_liste_absence_avalider.view.*

class DemandeAbsenceAdapter (val context: Context?,
                             val itemDemandeAbsenceListner: ItemDemandeAbsenceListner
) :
    RecyclerView.Adapter<ViewHolderAbsencesRequest>() {

    var items: ArrayList<AbsenceModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAbsencesRequest {
        return ViewHolderAbsencesRequest(
            LayoutInflater.from(context).inflate(
                R.layout.item_absences_request,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderAbsencesRequest, position: Int) {
        holder.txtStartDate?.text = items.get(position).startDate
        holder.txtType?.text = items.get(position).type
        holder.txtNbJour?.text = items.get(position).nbrJour
        holder.txtName?.text = items.get(position).author
        holder.view.setOnClickListener {
            itemDemandeAbsenceListner.onClickListner(position)
        }
    }
}


class ViewHolderAbsencesRequest(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtType = itemView.txt_type
    var txtStartDate = itemView.txt_date
    var txtNbJour = itemView.txt_nb_jour
    var txtName = itemView.txt_name
    var view = itemView
}