package fr.strada.sidemenuview.ui

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.strada.sidemenuview.R
import fr.strada.sidemenuview.adapter.SideMenuAdapter
import fr.strada.sidemenuview.data.MenuItem
import fr.strada.sidemenuview.data.StateMenu
import fr.strada.sidemenuview.utils.MenuItemListner
import fr.strada.sidemenuview.utils.SubMenuItemListner

class SideMenuView : LinearLayout {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    protected var mInflater: LayoutInflater

    // UI
    protected var mLayoutRoot: LinearLayoutCompat
    protected var btnOpenCloseMenu: AppCompatImageView
    protected var itemsMenuRecyclerView: RecyclerView
    //
    private var adapter : SideMenuAdapter
    private var layoutManager : LinearLayoutManager
    //
    private var state = StateMenu.CLOSED
    //
    private var initialWidth:Int = 0

    init {
        // inflate Views
        mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.widget_side_menu_view, this, true)
        mLayoutRoot = view.findViewById(R.id.mLayoutRoot)
        btnOpenCloseMenu = view.findViewById(R.id.btnOpenCloseMenu)
        itemsMenuRecyclerView = view.findViewById(R.id.itemsMenuRecyclerView)
        // setup RecycleView
        adapter = SideMenuAdapter(context, ArrayList(),null,null)
        adapter.state = state
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        itemsMenuRecyclerView.recycledViewPool.setMaxRecycledViews(1, 0)
        itemsMenuRecyclerView.layoutManager = layoutManager
        itemsMenuRecyclerView.adapter = adapter
        //Attributes
        initialWidth = fromDpToPixels(330)
        //Listners
        btnOpenCloseMenu.setOnClickListener {
           if(state == StateMenu.OPNED) // close drawer
           {
               closeDrawer()
           }else  // open drawer
           {
               openDrawer()
           }
        }
    }

    fun setMenuItems(menuItems:ArrayList<MenuItem>)
    {
        adapter.menuItems = menuItems
        adapter.notifyDataSetChanged()
    }

    fun openDrawer(){
        if(state == StateMenu.CLOSED)
        {
            val anim = ValueAnimator.ofInt(fromDpToPixels(70), initialWidth)
            anim.addUpdateListener { valueAnimator ->
                val `val` = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams = mLayoutRoot.layoutParams
                layoutParams.width = `val`
                mLayoutRoot.layoutParams = layoutParams
                if(valueAnimator.animatedFraction in 0.0..0.25)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_menu)
                    btnOpenCloseMenu.isEnabled = false

                }else if(valueAnimator.animatedFraction > 0.25 && valueAnimator.animatedFraction <= 0.5)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_menu_step_1)
                    btnOpenCloseMenu.isEnabled = false

                }else if(valueAnimator.animatedFraction > 0.5 && valueAnimator.animatedFraction <= 0.75)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_menu_step_2)
                    btnOpenCloseMenu.isEnabled = false
                }else if(valueAnimator.animatedFraction > 0.75 && valueAnimator.animatedFraction <= 1.0)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_back)
                    btnOpenCloseMenu.isEnabled = false
                }
                if(valueAnimator.animatedFraction >= 0.9 && valueAnimator.animatedFraction <= 1.0){
                    btnOpenCloseMenu.isEnabled = true
                }
            }
            anim.duration = 1000
            anim.start()
            state = StateMenu.OPNED
            adapter.state = StateMenu.OPNED
        }
    }

    fun closeDrawer()
    {   if(state == StateMenu.OPNED)
        {
            closeSubMenus()
            val anim = ValueAnimator.ofInt(initialWidth, fromDpToPixels(70))
            anim.addUpdateListener { valueAnimator ->
                val valeur = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams = mLayoutRoot.layoutParams
                layoutParams.width = valeur
                mLayoutRoot.layoutParams = layoutParams
                if(valueAnimator.animatedFraction in 0.0..0.25)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_back)
                    btnOpenCloseMenu.isEnabled = false

                }else if(valueAnimator.animatedFraction > 0.25 && valueAnimator.animatedFraction <= 0.5)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_menu_step_2)
                    btnOpenCloseMenu.isEnabled = false
                }else if(valueAnimator.animatedFraction > 0.5 && valueAnimator.animatedFraction <= 0.75)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_menu_step_1)
                    btnOpenCloseMenu.isEnabled = false
                }else if(valueAnimator.animatedFraction > 0.75 && valueAnimator.animatedFraction <= 1.0)
                {
                    btnOpenCloseMenu.setImageResource(R.drawable.ic_menu)
                    btnOpenCloseMenu.isEnabled = false
                }
                if(valueAnimator.animatedFraction >= 0.9 && valueAnimator.animatedFraction <= 1.0){
                    btnOpenCloseMenu.isEnabled = true
                }
            }
            anim.duration = 1000
            anim.start()
            state = StateMenu.CLOSED
            adapter.state = StateMenu.CLOSED
        }
    }

    fun fromDpToPixels(dpValue:Int):Int
    {
        val metrics = this.resources.displayMetrics
        val dp = dpValue
        val fpixels = metrics.density * dp
        val pixels = (fpixels + 0.5f).toInt()
        return pixels
    }

    fun setMenuItemListner(menuItemListner: MenuItemListner)
    {
        adapter.menuItemListner = menuItemListner
    }

    fun setSubMenuItemListner(subMenuItemListner: SubMenuItemListner)
    {
        adapter.subMenuItemListner = subMenuItemListner
    }

    fun closeSubMenus()
    {
        val items= adapter.menuItems
        for(menuItem in items){
            menuItem.isSubMenuOpned = false
        }
        adapter.notifyDataSetChanged()
    }

    fun setMenuItemActive(position:Int)
    {
        adapter.menuItems.forEach {
            it.selected = false
        }
        adapter.menuItems[position].selected = true
        adapter.notifyDataSetChanged()
    }
}