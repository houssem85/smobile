package fr.strada.smobile.ui.pointeuse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.Commentaire
import fr.strada.smobile.ui.pointeuse.getDaysBetweenTwoDates
import kotlinx.android.synthetic.main.item_comment.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentaireAdapter(val context: Context): RecyclerView.Adapter<CommentaireAdapter.ViewHolder> (){

    private val items = ArrayList<Commentaire>()

    fun setData(list : List<Commentaire>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val model = items[position]
       holder.txtComment.text =  model.commentaire
       model.dateCreationCommentaire?.let {
           try {
               val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
               val dateCreationCommentaire = it.take(10)
               val dateNow = sdf.format(Date())
               val diffDays = getDaysBetweenTwoDates(dateNow,dateCreationCommentaire)
               when(diffDays){
                   0L -> {
                       holder.txtTime.text = context.getString(R.string.aujourd_hui)
                   }
                   1L -> {
                       holder.txtTime.text = String.format(context.getString(R.string.ilya_s_jour),diffDays)
                   }
                   else -> {
                       holder.txtTime.text = String.format(context.getString(R.string.ilya_s_jours),diffDays)
                   }
               }
           }catch (ex:Exception){ }
       }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtComment = itemView.txt_comment
        var txtAuthor = itemView.txt_author
        var txtTime = itemView.txt_time
        var view = itemView
    }
}