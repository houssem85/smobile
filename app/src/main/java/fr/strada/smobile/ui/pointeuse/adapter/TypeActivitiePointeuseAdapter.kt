package fr.strada.smobile.ui.pointeuse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.utils.listner.OnClickListener
import kotlinx.android.synthetic.main.item_pointeuse_menu.view.*

class TypeActivitiePointeuseAdapter(val context : Context, val onClickListener : OnClickListener) : RecyclerView.Adapter<TypeActivitiePointeuseAdapter.TypeActivitiePointeuseViewHolder>() {

    var items = listOf<TypeActivitePointeuseModel>()

    override fun onBindViewHolder(holder: TypeActivitiePointeuseViewHolder, position: Int) {
        val model = items[position]
        holder.lblLabel.text = model.libelle
        model.icone?.let {
            Glide.with(context).load( BuildConfig.URL_BASE_TIME + it).into(holder.imageView)
        }
        holder.item.setOnClickListener {
            onClickListener.onClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeActivitiePointeuseViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_pointeuse_menu,parent,false)
        return TypeActivitiePointeuseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TypeActivitiePointeuseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val item = view
        val lblLabel = view.lblLabel
        val imageView= view.imageView
    }
}