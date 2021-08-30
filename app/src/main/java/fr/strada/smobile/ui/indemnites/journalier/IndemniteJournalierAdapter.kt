package fr.strada.smobile.ui.indemnites.journalier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.journalier.FraisRow
import kotlinx.android.synthetic.main.item_indemnite_jour.view.*

class IndemniteJournalierAdapter(val list: List<FraisRow>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_indemnite_jour, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val frais = list[position]
        holder.label.text = frais.typeFraisLibelle
        holder.montant.text = frais.montantFrais.toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val label = itemView.txt_labelindimnite
    val montant = itemView.txt_montantindemnite

}
