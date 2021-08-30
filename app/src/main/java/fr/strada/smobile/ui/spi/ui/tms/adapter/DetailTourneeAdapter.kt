package fr.strada.smobile.ui.spi.ui.tms.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.ui.spi.ui.tms.model.EnlevementLivraison
import kotlinx.android.synthetic.main.item_expiditionlivraison.view.*
import java.text.SimpleDateFormat
import java.util.*

class DetailTourneeAdapter(
    var list: ArrayList<EnlevementLivraison>,
    val click: (EnlevementLivraison) -> Unit
) :
    RecyclerView.Adapter<MyViewDetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewDetailHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_expiditionlivraison, parent, false)
        return MyViewDetailHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewDetailHolder, position: Int) {
        val item = list[position]
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
        val format1 = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val format2 = SimpleDateFormat("hh:mm", Locale.getDefault())
        val dateStr = format.parse(item.dateSouhaite)
        if (!item.isExpedition!!)
            holder.colorView.setBackgroundColor(Color.GREEN)
        else
            holder.colorView.setBackgroundColor(Color.parseColor("#659CD2"))
        holder.txtName.text = item.nom + "  " + item.ville
        holder.txt_date_tournee.text = format1.format(dateStr)
        holder.txt_heur.text = "Avant " + format2.format(dateStr)
       /* var strCarosserie = ""
        if (!item.typeCarosserieRows.isNullOrEmpty())
        item.typeCarosserieRows!!.forEach {
            if (!it.code.isNullOrEmpty())
            strCarosserie = strCarosserie.plus(it.libelle).plus(" ")
        }
        var strMateriell = ""
        if (!item.typeMaterielRows.isNullOrEmpty())
        item.typeMaterielRows!!.forEach {
            if (!it.code.isNullOrEmpty())
            strMateriell = strMateriell.plus(it.libelle).plus(" ")
        }
        holder.txt_typemateriel_typecarosseri.text = strMateriell.plus("/").plus(strCarosserie)*/
        holder.txt_detail.text = "Poids:".plus(item.poids.toString()).plus(" /mlp:"+item.mlp).plus(" /volume:"+item.volume)
        holder.itemView.setOnClickListener {
            click(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyViewDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var colorView = itemView.view_color
    var txt_date_tournee = itemView.txt_date_tournee
    var txt_heur = itemView.txt_heur
    var txt_typemateriel_typecarosseri = itemView.txt_typemateriel_typecarosseri
    var txt_detail = itemView.txt_detail
    var txt_numero = itemView.txt_numero
    var txtName = itemView.txt_id_tournee

}
