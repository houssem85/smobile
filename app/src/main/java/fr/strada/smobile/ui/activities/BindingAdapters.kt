package fr.strada.smobile.ui.activities

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import fr.strada.smobile.R
import fr.strada.smobile.ui.activities.hebdomadaire.MesActivitiesHebdomadaireFragment
import fr.strada.smobile.ui.activities.mensuel.MesActivitiesMensuelFragment
import fr.strada.smobile.ui.indemnites.hebdomadaire.IndemniteHebdomadaireFragment
import fr.strada.smobile.ui.indemnites.mensuel.IndemniteMensuelleFragment

@BindingAdapter("currentFragment")
fun bindCurrentFragment(textView: TextView, currentFragment: MutableLiveData<Fragment>) {
    currentFragment.value?.let {
        if (textView.id == R.id.txtMensuel) {
            if (it is MesActivitiesMensuelFragment || it is IndemniteMensuelleFragment) {
                textView.setTextColor(Color.parseColor("#354360"))
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD)
            } else {
                textView.setTextColor(Color.parseColor("#9A9A9A"))
                textView.setTypeface(textView.getTypeface(), Typeface.NORMAL)
            }
        } else if (textView.id == R.id.txtHebdomadaire) {
            if (it is MesActivitiesHebdomadaireFragment || it is IndemniteHebdomadaireFragment) {
                textView.setTextColor(Color.parseColor("#354360"))
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD)
            } else {
                textView.setTextColor(Color.parseColor("#9A9A9A"))
                textView.setTypeface(textView.getTypeface(), Typeface.NORMAL)
            }
        }
    }
}

@BindingAdapter("currentFragment")
fun bindCurrentFragment(view: View, currentFragment: MutableLiveData<Fragment>) {
    currentFragment.value?.let {
        if (view.id == R.id.indicatorMensuel) {
            if (it is MesActivitiesMensuelFragment || it is IndemniteMensuelleFragment) {
                view.visibility = VISIBLE
            } else {
                view.visibility = INVISIBLE
            }
        } else if (view.id == R.id.indicatorHebdomadaire) {
            if (it is MesActivitiesHebdomadaireFragment || it is IndemniteHebdomadaireFragment) {
                view.visibility = VISIBLE
            } else {
                view.visibility = INVISIBLE
            }

        }
    }
}