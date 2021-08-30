package fr.strada.smobile.ui.mes_frais.nouvelle_demande

import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.data.models.mes_frais.NoteFrais

@BindingAdapter(value =["titre","depenses"])
fun bindEnable(btn: AppCompatButton, titre: MutableLiveData<String>, depenses: MutableLiveData<ArrayList<Depense>>) {
    titre.value?.let { titre ->
        depenses.value?.let { depenses ->
            if (depenses.size > 0 && titre.trim().isNotEmpty()) {
                btn.setBackgroundResource(R.drawable.bg_rectangle_blue_radius)
                btn.isEnabled = true
            } else {
                btn.setBackgroundResource(R.drawable.bg_dark_gray)
                btn.isEnabled = false
            }
        }
    }
}

@BindingAdapter(value =["note_frais"])
fun bindEnable(btn: AppCompatButton, noteFrais: MutableLiveData<NoteFrais>) {
    noteFrais.value?.let { noteFrais ->
        if (noteFrais.listDepenses.size > 0) {
            btn.setBackgroundResource(R.drawable.bg_rectangle_blue_radius)
            btn.isEnabled = true
        } else {
            btn.setBackgroundResource(R.drawable.bg_dark_gray)
            btn.isEnabled = false
        }
    }
}
