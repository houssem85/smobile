package fr.strada.smobile.ui.mes_frais.nouvelle_depense

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.ui.activities.Utils.getCurrentLocal
import fr.strada.smobile.utils.listner.ItemDepenseListner
import kotlinx.android.synthetic.main.item_depense.view.*
import java.text.SimpleDateFormat

class DepenseAdapter(val context: Context,val itemDepenseListner: ItemDepenseListner): RecyclerView.Adapter<DepenseAdapter.DepenseViewHolder>() {

    private val items = ArrayList<Depense>()

    fun setData(list : List<Depense>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepenseViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_depense,parent,false)
        return DepenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepenseViewHolder, position: Int) {
        val locale= getCurrentLocal(context)
        val depenseModel = items[position]
        holder.lblType.text = depenseModel.typeDepenseLibelle
        holder.lblMontant.text = "${String.format("%.2f",depenseModel.montant)}€"
        val sfApp = SimpleDateFormat("EEEE d LLLL",locale)
        val sfApi = SimpleDateFormat("yyyy-MM-dd",locale)
        try {
            holder.lblDayDepense.text = sfApp.format(sfApi.parse(depenseModel.dateDepense.take(10))!!)
        }catch (ex:Exception){ }
/*        depenseModel.typeDepenseIcone?.let {
            holder.imgType.load(it)
        }*/
        if(depenseModel.commentaire.isNullOrEmpty())
        {
            holder.imgComment.visibility = GONE
        }else{
            holder.imgComment.visibility = VISIBLE
        }
        holder.btnDelete.setOnClickListener {
            itemDepenseListner.onClickDeleteListner(position)
        }
        holder.item.setOnClickListener {
            itemDepenseListner.onClickDetailDepenseListener(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getDepenseId(position: Int) = items[position].depenseId

    fun getDepense(position: Int) = items[position]
    class DepenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item = view.drag_item
        val lblType = view.lblType
        val imgType = view.imgType
        val lblMontant = view.lblMontant
        val btnDelete = view.btnDelete
        val imgFile = view.imgFileJoined
        val imgComment = view.imgComment
        val lblDayDepense = view.lblDayDepense
    }
}