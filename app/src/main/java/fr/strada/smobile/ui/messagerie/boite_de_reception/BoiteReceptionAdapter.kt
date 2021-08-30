package fr.strada.smobile.ui.messagerie.boite_de_reception

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.utils.listner.ItemBoiteReceptionListener
import kotlinx.android.synthetic.main.item_boite_reception.view.*

class BoiteReceptionAdapter (
    val context: Context? , val itemBoiteReceptionListener: ItemBoiteReceptionListener) :
    RecyclerView.Adapter<ViewHolderBoiteReception>() {

    var items: ArrayList<MessageModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBoiteReception {

        return ViewHolderBoiteReception(
            LayoutInflater.from(context).inflate(
                R.layout.item_boite_reception,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolderBoiteReception, position: Int) {
        holder.txtName.text = items[position].nameSender
        holder.txtMessageContent.text = items[position].messageContent
        holder.txtHeureEnvoi.text = items[position].heureEnvoi
        holder.imgProfile.setBackgroundResource(items[position].imageProfile!!)
        holder.imgTypeMessage.setBackgroundResource(items[position].imageTypeOfMessage!!)

        if(items[position].typeOfMessage == "reply"){
            holder.txtMessageContent.alpha = 0.5F
        }
        holder.btnDelete.setOnClickListener {
            itemBoiteReceptionListener.OnPressBtnDeleteListener(position)
        }
        holder.card_view.setOnClickListener {
            itemBoiteReceptionListener.onClickListner(position)
        }
    }
}

class ViewHolderBoiteReception(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var card_view= itemView.card_view
    var txtName = itemView.lbl_name
    var txtMessageContent = itemView.lbl_message
    var txtHeureEnvoi = itemView.lbl_heure_d_envoi
    var imgProfile = itemView.img_profile
    var imgTypeMessage = itemView.img_type_message
    var btnDelete = itemView.btn_delete
}