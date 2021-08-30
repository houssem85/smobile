package fr.strada.smobile.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.utils.listner.OnClickListener
import kotlinx.android.synthetic.main.item_menu_pointeuse.view.*

class TypeActivitiePointeuseAdapter(val context : Context,val onClickListener : OnClickListener?) : RecyclerView.Adapter<TypeActivitiePointeuseAdapter.TypeActivitiePointeuseViewHolder>() {

    var items = listOf<TypeActivitePointeuseModel>()

    override fun onBindViewHolder(holder: TypeActivitiePointeuseViewHolder, position: Int) {
        val model = items[position]
        holder.lblLabel.text = model.libelle
        holder.imageView.load(BuildConfig.URL_BASE_TIME + model.icone)
        holder.item.setOnClickListener {
            onClickListener?.onClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeActivitiePointeuseViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_menu_pointeuse,parent,false)
        return TypeActivitiePointeuseViewHolder(view)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    class TypeActivitiePointeuseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val item = view
        val lblLabel = view.lblLabel
        val imageView: ImageView = view.imageView
    }
}