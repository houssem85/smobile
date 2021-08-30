package fr.strada.smobile.utils

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

object Toggle {

    fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: ConstraintLayout): Boolean {
        Animations.toggleArrow(v, isExpanded)
        if (isExpanded) {
            Animations.expand(layoutExpand)
        } else {
            Animations.collapse(layoutExpand)
        }
        return isExpanded
    }
}