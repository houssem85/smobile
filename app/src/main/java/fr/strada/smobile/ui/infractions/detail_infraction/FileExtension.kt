package fr.strada.smobile.ui.infractions.detail_infraction

import java.io.File
import java.util.*

fun File.isHttpOrHttps() :Boolean{
    return this.path.toLowerCase(Locale.ROOT).contains("http")
            || this.path.toLowerCase(Locale.ROOT).contains("https")

}