package fr.strada.smobile.ui.messagerie.nouveau_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.utils.listner.ItemDestinataireListner
import kotlinx.android.synthetic.main.item_destinataire.view.*

class DestinataireAdapter(val context:Context,val itemDestinataireListner: ItemDestinataireListner?): RecyclerView.Adapter<DestinataireAdapter.DestinataireViewHolder>() {

    val items = listOf("Miguel Albert","Miguel Albert","Miguel Albert")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinataireViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_destinataire,parent,false)
        return DestinataireViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinataireViewHolder, position: Int) {
        val destinataire = items[position]
        holder.lblName.text = destinataire
        holder.item.setOnClickListener {
            itemDestinataireListner?.onItemClickListner(position,destinataire)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DestinataireViewHolder(view: View):RecyclerView.ViewHolder(view){
        val item = view
        val lblName = view.lblName
    }
}