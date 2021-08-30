package fr.strada.smobile.ui.messagerie.nouveau_message

import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R

@BindingAdapter(value =["destinataire","object","message"])
fun bindBackground(btn: AppCompatButton,destinataire: MutableLiveData<String>,object_: MutableLiveData<String>,message: MutableLiveData<String>) {
    destinataire.value?.let { destinataire ->
        object_.value?.let { object_ ->
            message.value?.let { message->
                val destinataireIsValid = destinataire.trim().isNotEmpty()
                val objectIsValid = object_.trim().isNotEmpty()
                val messageIsValid = message.trim().isNotEmpty()
                if(destinataireIsValid && objectIsValid && messageIsValid){
                    btn.setBackgroundResource(R.drawable.bg_rectangle_blue_radius)
                    btn.isEnabled = true
                }else{
                    btn.setBackgroundResource(R.drawable.bg_dark_gray)
                    btn.isEnabled = false
                }
            }
        }
    }
}