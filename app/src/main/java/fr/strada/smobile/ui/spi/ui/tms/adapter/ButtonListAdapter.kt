package fr.strada.smobile.ui.spi.ui.tms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import kotlinx.android.synthetic.main.item_btn_info.view.*

class ButtonListAdapter(val click: (Int) -> Unit) :
    RecyclerView.Adapter<ButtonListAdapter.MyViewHolder?>() {

    private var buttons: List<ButtonItem> = listOf(
        ButtonItem("Expiditeur", true),
        ButtonItem("Destinataire", false),
        ButtonItem("Palettes europes", false),
        ButtonItem("Marchandise", false),
        ButtonItem("Tarif", false),
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_btn_info, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val button = buttons[position]
        holder.btn.isSelected = button.isSelected
        holder.btn.text = button.name
        holder.btn.setOnClickListener {
            buttons.forEach { it.isSelected = false }
            buttons[position].isSelected = true
           // holder.btn.setTextColor(Color.WHITE)
            notifyDataSetChanged()
            click(position)
        }
    }



override fun getItemCount(): Int {
    return buttons.size
}

class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    val btn = itemview.btn_info_list
}
}
data class ButtonItem(val name: String, var isSelected: Boolean = false)