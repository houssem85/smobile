package fr.strada.sidemenuview.utils

import android.app.Activity
import androidx.fragment.app.Fragment

interface MenuItemListner {
    fun onClickMenuItem(index:Int,fragment: Fragment?,activity: Activity?)

    fun onClickMenuItemWithSubMenu()
}