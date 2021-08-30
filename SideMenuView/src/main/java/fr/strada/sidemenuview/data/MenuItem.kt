package fr.strada.sidemenuview.data

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment

data class MenuItem(var firstImage: Drawable, var title :String, var fragment: Fragment?, val activity:Activity?, var secondImage: Drawable?, var subMenuItems:List<SubMenuItem>?, var isSubMenuOpned:Boolean,var selected : Boolean = false){

    constructor(firstImage: Drawable, title :String,secondImage: Drawable?,subMenuItems:List<SubMenuItem>?): this(firstImage, title,null,null,secondImage,subMenuItems,false,false)
    constructor(firstImage: Drawable, title :String,fragment: Fragment?,activity: Activity?): this(firstImage, title,fragment,activity,null,null,false,false)
    constructor(firstImage: Drawable, title :String,fragment: Fragment?,activity: Activity?,secondImage: Drawable?): this(firstImage, title,fragment,activity,secondImage,null,false,false)
}