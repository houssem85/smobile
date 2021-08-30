package fr.strada.smobile.ui_tablette.messagerie.conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageConversationModel
import fr.strada.smobile.ui_tablette.messagerie.boite_reception.TypeMessage
import kotlinx.android.synthetic.main.item_message.view.*

class ConversationAdapter(val context:Context): RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    var items = ArrayList<MessageConversationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int)
    {
        val message = items[position]
        if(message.typeMessage == TypeMessage.ENVOYEE)
        {
            holder.messageEmetteur.visibility = VISIBLE
            holder.messageDestinataire.visibility = GONE
            holder.txtMessageEmetteur.text = message.message
        }else
        {
            holder.messageEmetteur.visibility = GONE
            holder.messageDestinataire.visibility = VISIBLE
            holder.txtMessageDestinataire.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ConversationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var messageDestinataire = itemView.messageDestinataire
        var messageEmetteur = itemView.messageEmetteur
        var txtMessageDestinataire = itemView.txtMessageDestinataire
        var txtMessageEmetteur = itemView.txtMessageEmetteur
    }
}