package fr.strada.sidemenuview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.sidemenuview.R
import fr.strada.sidemenuview.data.SubMenuItem
import fr.strada.sidemenuview.utils.SubMenuItemListner
import kotlinx.android.synthetic.main.item_menu.view.*

class SideSubMenuAdapter(var context: Context,var subMenuItems:List<SubMenuItem>,var indexItemMenu:Int,val subMenuItemListner: SubMenuItemListner) : RecyclerView.Adapter<SideSubMenuAdapter.ItemSubMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSubMenuViewHolder
    {
        val view= LayoutInflater.from(context).inflate(R.layout.item_sub_menu,parent,false)
        return ItemSubMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemSubMenuViewHolder, position: Int)
    {
        val subMenuItem = subMenuItems[position]
        holder.lblTitle.text = subMenuItem.title
        holder.lblTitle.setOnClickListener {
            subMenuItemListner.onClickSubMenuItem(indexItemMenu,subMenuItem.fragment)
        }
    }

    override fun getItemCount(): Int {
        return subMenuItems.size
    }

    class ItemSubMenuViewHolder(view: View): RecyclerView.ViewHolder(view){
        var lblTitle = view.lblTitle
    }
}