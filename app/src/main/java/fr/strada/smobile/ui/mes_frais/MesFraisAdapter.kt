package fr.strada.smobile.ui.mes_frais

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.utils.listner.ItemDemandeListener
import kotlinx.android.synthetic.main.item_mes_frais.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MesFraisAdapter(
    private val context: Context,
    private val itemDemandeListener: ItemDemandeListener,
    locale: Locale
) : RecyclerView.Adapter<ViewHolderMesFrais>() {

    private var items: ArrayList<NoteFrais> = ArrayList()
    private var sdfMMMyyyy = SimpleDateFormat("d MMM yyyy", locale)
    private var sdfddMMyyyy = SimpleDateFormat("dd/MM/yyyy", locale)
    private var sdfApi = SimpleDateFormat("yyyy-MM-dd", locale)

    fun setData(list: List<NoteFrais>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMesFrais {

        return ViewHolderMesFrais(
            LayoutInflater.from(context).inflate(
                R.layout.item_mes_frais,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderMesFrais, position: Int) {
        val model  = items[position]
        try {
            holder.txtNumDemande.text = model.titre
            val numberDepenses = model.nombreDepense.toString()
            val strDateDebutDepense = sdfMMMyyyy.format(sdfApi.parse(model.datePremierDepense.take(10))!!)
            val strDateLastDepense = sdfMMMyyyy.format(sdfApi.parse(model.dateDernierDepense.take(10))!!)
            if(model.nombreDepense > 1){
                holder.lbl_date_debut_fin_depense.text = String.format(context.getString(R.string.s_dépenses_de_s_au_s),numberDepenses,strDateDebutDepense,strDateLastDepense)
            }else{
                holder.lbl_date_debut_fin_depense.text = String.format(context.getString(R.string.s_dépense_de_s_au_s),numberDepenses,strDateDebutDepense)
            }
            holder.txtDateEnvoieDemande.text = sdfddMMyyyy.format(sdfApi.parse(model.dateCreationNote.take(10))!!)
            val totaleMontant = model.listDepenses.map { it.montant }.sum()
            holder.txtMontant.text = "${String.format("%.2f", totaleMontant)}€"
        }catch (ex:Exception){ }

        if(model.approuvee){
            holder.imgImageDemandeEnvoyee.visibility = VISIBLE
            holder.btnPartager.visibility = VISIBLE
            holder.btnEnvoyer.visibility = GONE
            holder.btnDelete.visibility = GONE
            holder.view.setOnClickListener {
                itemDemandeListener.onClickDemandeAccepteeListner(position)
            }
        }else {
            holder.imgImageDemandeEnvoyee.visibility = INVISIBLE
            holder.btnPartager.visibility = GONE
            holder.btnEnvoyer.visibility = GONE
            holder.btnDelete.visibility = VISIBLE
            holder.view.setOnClickListener {
                itemDemandeListener.onClickDemandeNonAccepteeListener(position)
            }
            holder.btnDelete.setOnClickListener {
                itemDemandeListener.onPressBtnDeleteListener(position)
            }
        }
        if(model.commentaire.isNullOrEmpty()){
            holder.img_commentaire.visibility = INVISIBLE
        }else{
            holder.img_commentaire.visibility = VISIBLE
        }
    }

    fun getNoteFraisId(position:Int) = items[position].noteFraisId

    fun getNoteFrais(position:Int) = items[position]

}


class ViewHolderMesFrais(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtNumDemande = itemView.num_demande
    val txtDateEnvoieDemande = itemView.txtDateEnvoi
    val txtMontant = itemView.txt_montant
    val imgImageDemandeEnvoyee = itemView.img_demande_envoyee
    val view = itemView.drag_item
    val btnEnvoyer = itemView.btn_envoyer
    val btnPartager = itemView.btn_partager
    val btnDelete = itemView.btn_delete
    val lbl_date_debut_fin_depense = itemView.lbl_date_debut_fin_depense
    val img_commentaire = itemView.img_commentaire
}