package fr.strada.smobile.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TopSpacingItemDecoration(private val paddingTop: Int, private val paddingRight: Int,
                               private val paddingLeft:Int, private val paddingBottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = paddingTop
        outRect.right = paddingRight
        outRect.left = paddingLeft
        outRect.bottom = paddingBottom
    }
}