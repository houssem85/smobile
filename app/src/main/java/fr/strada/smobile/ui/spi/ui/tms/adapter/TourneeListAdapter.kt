package fr.strada.smobile.ui.spi.ui.tms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.ui.spi.ui.tms.model.TourneeItem
import kotlinx.android.synthetic.main.tournee_item_tms.view.*
import java.text.SimpleDateFormat
import java.util.*

class TourneeListAdapter(var list: ArrayList<TourneeItem>, val click: (String) -> Unit) :
    RecyclerView.Adapter<MyViewHolderTournee>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderTournee {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tournee_item_tms, parent, false)
        return MyViewHolderTournee(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderTournee, position: Int) {
        val item = list[position]

        holder.txt_id.text = item.tourneeId
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
        val format1 = SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault())
        val dateStr = format.parse(item.dateDebut!!)
        holder.txt_date.text = format1.format(dateStr!!)

        holder.itemView.setOnClickListener {
            click(item.tourneeId!!)
        }

    }

    override fun getItemCount(): Int {

        return list.size
    }
}


class MyViewHolderTournee(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val txt_id = itemView.txt_id_tournee
    val txt_date = itemView.txt_date_tournee

}

