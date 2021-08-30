package fr.strada.smobile.ui.indemnites.mensuel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.mensuel.SummaryFrai
import kotlinx.android.synthetic.main.itemfraishedbdo.view.*

class GridIndemniteMensuellAdapter(val list: List<SummaryFrai>) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.itemfraishedbdo, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.txtnbr.text = item.cumulNombreFrais.toString()
        holder.txtname.text = item.typeFraisLibelle
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtnbr = itemView.txt_nbr_frais
    val txtname = itemView.txt_name_frais


}
