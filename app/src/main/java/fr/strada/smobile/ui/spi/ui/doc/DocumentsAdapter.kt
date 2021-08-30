package fr.strada.smobile.ui.spi.ui.doc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.DocumentModel

class DocumentsAdapter (val context: Context): RecyclerView.Adapter<DocumentsAdapter.ViewHolder> (){

    private val items = ArrayList<DocumentModel>()

    fun setData(list : List<DocumentModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.doc_item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
       // holder.txtComment.text =  model.commentaire

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //    var txtComment = itemView.txt_comment
      //  var txtAuthor = itemView.txt_author
       // var txtTime = itemView.txt_time
        var view = itemView
    }


}