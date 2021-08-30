package fr.strada.smobile.ui.mes_frais.detail_demande_envoyee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.strada.smobile.R
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.ui.activities.Utils
import kotlinx.android.synthetic.main.item_depense.view.imgType
import kotlinx.android.synthetic.main.item_depense.view.lblMontant
import kotlinx.android.synthetic.main.item_depense.view.lblType
import kotlinx.android.synthetic.main.item_depense_disabled.view.*
import java.text.SimpleDateFormat

class DepenseDisabledAdapter (val context: Context,val onClick :(Depense) -> Unit ): RecyclerView.Adapter<DepenseDisabledAdapter.DepenseDisabledViewHolder>() {

    private val items = ArrayList<Depense>()

    fun setData(list : List<Depense>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepenseDisabledViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_depense_disabled,parent,false)
        return DepenseDisabledViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepenseDisabledViewHolder, position: Int) {
        val locale= Utils.getCurrentLocal(context)
        val depenseModel = items[position]
        holder.lblType.text = depenseModel.typeDepenseLibelle
        holder.lblMontant.text = "${String.format("%.2f",depenseModel.montant)}€"
        val sfApp = SimpleDateFormat("EEEE d LLLL",locale)
        val sfApi = SimpleDateFormat("yyyy-MM-dd",locale)
        try {
            holder.lblDate.text = sfApp.format(sfApi.parse(depenseModel.dateDepense.take(10))!!)
        }catch (ex:Exception){ }
        depenseModel.typeDepenseIcone?.let {
            holder.imgType.load(it)
        }
        holder.item.setOnClickListener {
            onClick(depenseModel)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DepenseDisabledViewHolder(view: View): RecyclerView.ViewHolder(view){
        val item=  view
        val lblType=  view.lblType
        val imgType=  view.imgType
        val lblMontant = view.lblMontant
        val lblDate = view.lblDate
    }
}