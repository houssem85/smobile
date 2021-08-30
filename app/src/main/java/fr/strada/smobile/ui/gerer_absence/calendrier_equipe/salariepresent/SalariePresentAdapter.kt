package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.SalarieModel
import kotlinx.android.synthetic.main.item_employee_present.view.*

class SalariePresentAdapter (val context: Context?) :
    RecyclerView.Adapter<ViewHolderSalariePresent>() {

    var items: ArrayList<SalarieModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSalariePresent {

        return ViewHolderSalariePresent(
            LayoutInflater.from(context).inflate(
                R.layout.item_employee_present,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderSalariePresent, position: Int) {
        holder.txtName?.text = items.get(position).name
        holder.txtPhoneNumber?.text = items.get(position).telephoneNumber
    }
}


class ViewHolderSalariePresent(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtName = itemView.txt_name_employee
    var txtPhoneNumber = itemView.txt_phone_number
    var view = itemView
}