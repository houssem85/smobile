package fr.strada.smobile.ui.mes_frais.nouvelle_depense.dialog_ajouter_autre_depense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import fr.strada.smobile.R
import kotlinx.android.synthetic.main.dialog_votre_depense_a_ete_ajouter_avec_succes.*

class DialogAjouterAutreDepense : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_votre_depense_a_ete_ajouter_avec_succes, container, false);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation()
    {
        btnAjouterUneAutreDepense.setOnClickListener {
            dismiss()
            onPressBtnAjouterUneAutreDepense?.invoke()
        }
        btnTerminer.setOnClickListener {
            dismiss()
            onPressBtnTerminer?.invoke()
        }
    }

    private var onPressBtnAjouterUneAutreDepense : (() -> Unit)? = null
    private var onPressBtnTerminer : (() -> Unit)? = null

    fun setOnPressBtnAjouterUneAutreDepenseListner(onPressBtnAjouterUneAutreDepense : (() -> Unit))
    {
        this.onPressBtnAjouterUneAutreDepense = onPressBtnAjouterUneAutreDepense
    }

    fun setOnPressBtnTerminerListner(onPressBtnTerminer : (() -> Unit))
    {
        this.onPressBtnTerminer = onPressBtnTerminer
    }

    companion object {
        const val  TAG = "DialogAjouterAutreDepense"
    }

    override fun onStart() {
        super.onStart()
        val isTabletteSize = resources.getBoolean(R.bool.isTablet)
        if(!isTabletteSize){
            val window = dialog!!.window!!
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params
        }
    }
}