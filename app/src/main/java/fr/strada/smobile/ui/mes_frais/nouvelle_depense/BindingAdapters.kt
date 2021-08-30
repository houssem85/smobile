package fr.strada.smobile.ui.mes_frais.nouvelle_depense

import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.Utils.isDecimalNumber

@BindingAdapter(value =["dateDepense","typeDepense","montant"])
fun bindEnable(btn: AppCompatButton, dateDepense: MutableLiveData<String>, typeDepense: MutableLiveData<String>,montant: MutableLiveData<String>)
{
    dateDepense.value?.let { dateDepense ->
        typeDepense.value?.let { typeDepense ->
            montant.value?.let { montant ->
                val isFormatMontantCorrect = isDecimalNumber(montant)
                val isDateDepenseNotEmpty = dateDepense.trim().isNotEmpty()
                val isTypeDepenseNotEmpty = typeDepense.trim().isNotEmpty()
                if(isFormatMontantCorrect && isDateDepenseNotEmpty && isTypeDepenseNotEmpty){
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

@BindingAdapter(value =["montant"])
fun bindEnable(btn: AppCompatButton,montant: MutableLiveData<String>)
{
    montant.value?.let { montant ->
        val isFormatMontantCorrect = isDecimalNumber(montant)
        if(isFormatMontantCorrect){
            btn.setBackgroundResource(R.drawable.bg_rectangle_blue_radius)
            btn.isEnabled = true
        }else{
            btn.setBackgroundResource(R.drawable.bg_dark_gray)
            btn.isEnabled = false
        }
    }
}