package fr.strada.smobile.utils.listner

import androidx.databinding.ObservableList
import java.io.File

interface GroupImagesListner {

    fun onDataChanged(images:ObservableList<File>)

    fun onImageClickListner(image:File)
}