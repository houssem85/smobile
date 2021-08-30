package fr.strada.smobile.ui.indemnites.mensuel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.mensuel.SemaineFrai
import fr.strada.smobile.utils.listner.ItemSemaineListner
import kotlinx.android.synthetic.main.item_semaine_indemnite_mensuelle.view.*

class SemaineIndemniteMensuelleAdapter(
    val context: Context,
    val list: List<SemaineFrai>,
    val itemSemaineListner: ItemSemaineListner
) :
    RecyclerView.Adapter<SemaineIndemniteMensuelleAdapter.SemaineIndemniteMensuelleViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SemaineIndemniteMensuelleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_semaine_indemnite_mensuelle, parent, false)
        return SemaineIndemniteMensuelleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SemaineIndemniteMensuelleViewHolder, position: Int) {

        val s = list[position]
        holder.lblSemaine.text = s.jour
        holder.lblFrais.text = s.jourNombreFrais.toString()
        holder.lblMontant.text = "${s.jourMontantFrais}€"
        holder.view.setOnClickListener {
            val str = s.jour.replace("S", "")
            itemSemaineListner.onClickListner(str.toInt())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SemaineIndemniteMensuelleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblSemaine = itemView.lblSemaine
        val lblFrais = itemView.lblFrais
        val lblMontant = itemView.lblMontant
        val view = itemView
    }

}