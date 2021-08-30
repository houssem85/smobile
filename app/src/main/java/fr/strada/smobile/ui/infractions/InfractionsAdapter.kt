package fr.strada.smobile.ui.infractions

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.ui.pointeuse.millisToTime
import kotlinx.android.synthetic.main.item_infraction.view.*

class InfractionsAdapter(private val context: Context,private val onClick: (Int) -> Unit): RecyclerView.Adapter<InfractionsAdapter.InfractionsViewHolder>() {

    private var items : ArrayList<Infraction> = ArrayList()
    
    fun setData(data : List<Infraction>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun getItem(position:Int) = items.get(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfractionsViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_infraction,parent,false)
        return InfractionsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InfractionsViewHolder, position: Int) {
        val model = items[position]
        holder.lblTypeInfraction.text = model.infractionLibelle
        holder.lblTypeConduite.text = model.infractionCategorieLibelle
        holder.lbl_heure.text  = millisToTime(model.infractionHeure.totalMilliseconds)
        val strDate = "${model.infractionDate.substring(8,10)}/${model.infractionDate.substring(5,7)}/${model.infractionDate.substring(0,4)}"
        holder.lbl_date.text = strDate
        holder.view.setOnClickListener {
            onClick.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class InfractionsViewHolder(view: View):RecyclerView.ViewHolder(view)
    {   val view = view
        val lblTypeInfraction= view.lblTypeInfraction
        val lblTypeConduite= view.lblTypeConduite
        val lblNbAttachment = view.lblNbAttachment
        val icAttachment = view.icAttachment
        val lbl_date = view.lbl_date
        val lbl_heure = view.lbl_heure
    }
}