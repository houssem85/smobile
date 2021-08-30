package fr.strada.smobile.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.NotificationModel
import kotlinx.android.synthetic.main.item_notification.view.*
import kotlinx.android.synthetic.main.item_notification.view.txt_notification
import kotlinx.android.synthetic.main.layout_my_notification.view.*

class NotificationAdapter (
    val items: ArrayList<NotificationModel>,
    val context: Context?) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_notification,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNotification?.text = items.get(position).title
        holder.txtDescription?.text = items.get(position).description
        holder.txtTime?.text = items.get(position).time
        holder.txtNbr?.text = 3.toString()
        //holder.txtDescription1?.text = context!!.getString(R.string.votre_demande, items.get(position).description)

    }

}


class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtNotification = itemView.txt_notification
    var txtDescription = itemView.txt_description
    var txtTime = itemView.txt_time
    var txtNbr = itemView.txt_nbr_notification
    var view = itemView

}