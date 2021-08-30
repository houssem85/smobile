package fr.strada.smobile.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import fr.strada.smobile.R
import kotlinx.android.synthetic.main.collaborateur_item.view.*

class ColaborateursAdapter(var list: List<CollaborateurItem>, val click: (Int) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.collaborateur_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coll = list[position]
        if (coll.isSelected) {
            holder.check.visibility = View.VISIBLE
        } else {
            holder.check.visibility = View.INVISIBLE
        }
        holder.image.load(coll.image) {
            transformations(CircleCropTransformation())
        }
        holder.name.text = coll.name
        holder.itemView.setOnClickListener {
            click(position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image = itemView.collaborateur_image
    val name = itemView.colaborateur_name
    val check = itemView.imageViewCheckC
}

data class CollaborateurItem(val name: String, val image: String, var isSelected: Boolean = false)