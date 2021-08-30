package fr.strada.smobile.ui.spi.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.ui.spi.ui.home.HomeSpiFragment
import kotlinx.android.synthetic.main.item_spi_app.view.*

class MenuItemAdapter(
    var list: List<HomeSpiFragment.AppItem>,
    val click: (String) -> Unit
) :
    RecyclerView.Adapter<MyView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_spi_app, parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val app = list[position]
        holder.image.setImageResource(app.image)
        holder.name.text = app.name
        holder.fonction.visibility = View.GONE
        holder.itemView.setOnClickListener {
            click(app.name)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image = itemView.imageView_app
    val name = itemView.textView_name
    val fonction = itemView.textView_fonction
}
