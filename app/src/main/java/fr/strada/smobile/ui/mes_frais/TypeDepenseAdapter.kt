package fr.strada.smobile.ui.mes_frais

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.mes_frais.TypesDepense
import kotlinx.android.synthetic.main.item_type_depense.view.*

class TypeDepenseAdapter(private val context: Context,private val onClick : (TypesDepense) -> Unit) : RecyclerView.Adapter<TypeDepenseAdapter.TypeDepenseViewHolder>(){

    val items = ArrayList<TypesDepense>()

    fun setData(list : List<TypesDepense>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeDepenseViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_type_depense,parent,false)
        return TypeDepenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeDepenseViewHolder, position: Int) {
        val model = items[position]
        holder.lbl_libelle.text = model.libelle
        holder.item.setOnClickListener {
            onClick.invoke(model)
        }
    }

    override fun getItemCount() = items.size

    class TypeDepenseViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val item = view
        val lbl_libelle = view.lbl_libelle
    }
}