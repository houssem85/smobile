package fr.strada.smobile.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.NotificationModel
import fr.strada.smobile.utils.listner.ItemNotificationListener
import kotlinx.android.synthetic.main.item_notif_swipe.view.*

class NotificationsAdapter(
    val context: Context?, val itemNotificationListener: ItemNotificationListener
) :
    RecyclerView.Adapter<ViewHolderNotifications>() {

    var items: ArrayList<NotificationModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotifications {

        return ViewHolderNotifications(
            LayoutInflater.from(context).inflate(
                R.layout.item_notif_swipe,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderNotifications, position: Int) {

        holder.txtNotification?.text = items[position].title
        holder.txtDescription?.text = items[position].description
        holder.txtTime?.text = items[position].time

        holder.btnDelete.setOnClickListener {
            itemNotificationListener.OnPressBtnDeleteListener(position)
        }
        holder.card_view.setOnClickListener {
            itemNotificationListener.onClickListner(position)
        }    }

}


class ViewHolderNotifications(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtNotification = itemView.txt_notification
    var txtDescription = itemView.txt_description
    var txtTime = itemView.txt_time
    val btnDelete = itemView.btn_delete
    var card_view= itemView.card_view

}