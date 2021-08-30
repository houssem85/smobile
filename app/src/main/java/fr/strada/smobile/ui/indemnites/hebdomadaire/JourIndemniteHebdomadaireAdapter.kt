package fr.strada.smobile.ui.indemnites.hebdomadaire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.hebdo.JourFrai
import fr.strada.smobile.utils.listner.ItemJourListner
import kotlinx.android.synthetic.main.item_jour_indemnite_hebdomadaire.view.*

class JourIndemniteHebdomadaireAdapter(
    val context: Context,
    private val listFrai: List<JourFrai>,
    val itemJourListner: ItemJourListner
) :
    RecyclerView.Adapter<JourIndemniteHebdomadaireAdapter.JourIndemniteHebdomadaireViewHolder>() {

    val jours = listOf(
        context.resources.getString(R.string.Lu),
        context.resources.getString(R.string.Ma),
        context.resources.getString(R.string.Me),
        context.resources.getString(R.string.Je),
        context.resources.getString(R.string.Ve),
        context.resources.getString(R.string.Sa),
        context.resources.getString(R.string.Di)
    )
    var frais = ArrayList<Int>()
    var montant = ArrayList<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JourIndemniteHebdomadaireViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_jour_indemnite_hebdomadaire, parent, false)
        return JourIndemniteHebdomadaireViewHolder(view)
    }

    override fun onBindViewHolder(holder: JourIndemniteHebdomadaireViewHolder, position: Int) {
        if (listFrai.isNotEmpty()) {
            val jour = jours[position]
            val jourFrai = listFrai[position]
            holder.lblJour.text = jour
            holder.lblFrais.text = jourFrai.jourNombreFrais.toString()
            holder.lblMontant.text = "${jourFrai.jourMontantFrais} €"
            holder.view.setOnClickListener {
                itemJourListner.onClickListner(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return jours.size
    }

    class JourIndemniteHebdomadaireViewHolder(view: View):RecyclerView.ViewHolder(view){
        val lblJour= view.lblJour
        val lblFrais = itemView.lblFrais
        val lblMontant = itemView.lblMontant
        val view = view
    }
}