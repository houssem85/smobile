package fr.strada.smobile.utils.ext

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

fun View.hide(){
    this.visibility = GONE
}

fun View.show(){
    this.visibility = VISIBLE
}