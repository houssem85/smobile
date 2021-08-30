package fr.strada.smobile.ui.messagerie.message_predefini

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.utils.listner.ItemMessagePredefiniListener
import kotlinx.android.synthetic.main.item_message_predefini.view.*

class MessagePredefiniAdapter(
    val context: Context?, val itemMessagePredefiniListener: ItemMessagePredefiniListener
) :
    RecyclerView.Adapter<ViewHolderMessagePredefini>() {

    var items: ArrayList<MessageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMessagePredefini {

        return ViewHolderMessagePredefini(
            LayoutInflater.from(context).inflate(
                R.layout.item_message_predefini,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderMessagePredefini, position: Int) {
        holder.txtTitle.text = items[position].titleMessagePredefini
        holder.txtMessageContent.text = items[position].messageContent
        holder.txtHeureEnvoi.text = items[position].heureEnvoi
        holder.txtDateEnvoi.text = items[position].dateEnvoi

        holder.view.setOnClickListener {
            itemMessagePredefiniListener.onClickListner(position)
        }

        holder.btnDelete.setOnClickListener {
            itemMessagePredefiniListener.OnPressBtnDeleteListener(position)
        }
    }


}



class ViewHolderMessagePredefini(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtTitle = itemView.txt_object
    val txtMessageContent = itemView.txt_content_message
    val txtDateEnvoi = itemView.txt_date_send_message
    val txtHeureEnvoi = itemView.txt_heure_send_message
    val view = itemView.drag_item
    val btnDelete = itemView.btn_delete
}