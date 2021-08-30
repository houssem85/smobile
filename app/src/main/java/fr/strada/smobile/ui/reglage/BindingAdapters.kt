package fr.strada.smobile.ui.reglage

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R

@BindingAdapter("isItemNotificationsActive")
fun bindIsItemNotificationsActive(imageView: ImageView, isItemNotificationsActive: MutableLiveData<Boolean>) {
    isItemNotificationsActive.value?.let {
       if(it)
       {
           imageView.setImageResource(R.drawable.ic_arrow_top)
       }else
       {
           imageView.setImageResource(R.drawable.ic_arrow_bottom)
       }
    }
}