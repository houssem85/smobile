package fr.strada.smobile.ui.activities

import android.view.View.GONE
import com.facebook.shimmer.ShimmerFrameLayout

fun ShimmerFrameLayout.stopAnimation()
{
    this.stopShimmer()
    this.visibility = GONE
}