package fr.strada.sidemenuview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.strada.sidemenuview.R
import fr.strada.sidemenuview.data.MenuItem
import fr.strada.sidemenuview.data.StateMenu
import fr.strada.sidemenuview.utils.MenuItemListner
import fr.strada.sidemenuview.utils.SubMenuItemListner
import kotlinx.android.synthetic.main.item_menu.view.*
import java.util.*

class SideMenuAdapter(var context: Context,var menuItems:ArrayList<MenuItem>,var menuItemListner: MenuItemListner?,var subMenuItemListner: SubMenuItemListner?) : RecyclerView.Adapter<SideMenuAdapter.ItemMenuViewHolder>() {

    var state = StateMenu.OPNED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMenuViewHolder
    {
        val view= LayoutInflater.from(context).inflate(R.layout.item_menu,parent,false)
        return ItemMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemMenuViewHolder, position: Int)
    {
        val menuItem = menuItems[position]
        // Second image
        if(menuItem.secondImage != null)
        {
            holder.icSecondImage.setImageDrawable(menuItem.secondImage)
            holder.icSecondImage.visibility = VISIBLE
        }else{
            holder.icSecondImage.visibility = GONE
        }
        // Sub Menu
        if(menuItem.subMenuItems != null)
        {   holder.icArrowImage.visibility = VISIBLE
            val adapter = SideSubMenuAdapter(context,menuItem.subMenuItems!!,position,subMenuItemListner!!)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.itemsSubMenuRecyclerView.layoutManager = layoutManager
            holder.itemsSubMenuRecyclerView.adapter = adapter
            if(menuItem.isSubMenuOpned)
            {
                holder.itemsSubMenuRecyclerView.visibility = VISIBLE
                holder.icArrowImage.setImageResource(R.drawable.ic_arrow_top)
            }else
            {
                holder.itemsSubMenuRecyclerView.visibility = GONE
                holder.icArrowImage.setImageResource(R.drawable.ic_arrow_bottom)
            }

        }else
        {
            holder.icArrowImage.visibility = GONE
        }
        holder.lblTitle.text = menuItem.title
        holder.icFirstImage.setImageDrawable(menuItem.firstImage)
        // Listners
        holder.item.setOnClickListener {
            if(menuItem.subMenuItems == null)
            {
                menuItemListner?.onClickMenuItem(position,menuItem.fragment,menuItem.activity)
            }else{
                menuItemListner?.onClickMenuItemWithSubMenu()
            }
            if(menuItem.subMenuItems == null)
            {
                unSelectAll()
                menuItem.selected = true
            }
            if(menuItem.subMenuItems != null){
                if(holder.itemsSubMenuRecyclerView.visibility == GONE)
                {
                    holder.itemsSubMenuRecyclerView.visibility = VISIBLE
                    menuItem.isSubMenuOpned = true

                }else if(holder.itemsSubMenuRecyclerView.visibility == VISIBLE)
                {
                    holder.itemsSubMenuRecyclerView.visibility = GONE
                    menuItem.isSubMenuOpned = false
                }
            }
            notifyDataSetChanged()
        }
        // Alpha
        if(menuItem.selected)
        {
           holder.lblTitle.alpha = 1.0f
           holder.icSecondImage.alpha = 1.0f
           holder.icFirstImage.alpha = 1.0f
           holder.icArrowImage.alpha = 1.0f
        }else
        {
            holder.lblTitle.alpha = 0.5f
            holder.icSecondImage.alpha = 0.5f
            holder.icFirstImage.alpha = 0.5f
            holder.icArrowImage.alpha = 0.5f
        }
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    class ItemMenuViewHolder(view: View):RecyclerView.ViewHolder(view){
        var item = view
        var icFirstImage = view.icFirstImage
        var icSecondImage = view.icSecondImage
        var icArrowImage = view.icArrowImage
        var lblTitle = view.lblTitle
        var itemsSubMenuRecyclerView = view.itemsSubMenuRecyclerView
    }

    private fun unSelectAll()
    {
        menuItems.forEach {
            it.selected = false
        }
    }
}