package fr.strada.smobile.ui_tablette.messagerie.nouveau_message_predefinie

import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R

@BindingAdapter(value =["object","message"])
fun bindBackground(btn: AppCompatButton,object_: MutableLiveData<String>, message: MutableLiveData<String>) {
        object_.value?.let { object_ ->
            message.value?.let { message->
                val objectIsValid = object_.trim().isNotEmpty()
                val messageIsValid = message.trim().isNotEmpty()
                if(objectIsValid && messageIsValid){
                    btn.setBackgroundResource(R.drawable.bg_rectangle_blue_radius)
                    btn.isEnabled = true
                }else{
                    btn.setBackgroundResource(R.drawable.bg_dark_gray)
                    btn.isEnabled = false
                }
            }
    }
}