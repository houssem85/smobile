package fr.strada.smobile.ui.mes_frais.nouvelle_demande.dialog_enregister_demande

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import fr.strada.smobile.R
import kotlinx.android.synthetic.main.dialog_vous_voulez_enregistrer_votre_nouvelle_depense_avant_de_quitter.*

class DialogEnregisterDemande : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_vous_voulez_enregistrer_votre_nouvelle_depense_avant_de_quitter, container, false);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation()
    {
        btnNon.setOnClickListener {
            dismiss()
            onPressBtnNon?.invoke()
        }
        btnOui.setOnClickListener {
            dismiss()
            onPressBtnOui?.invoke()
        }
    }

    private var onPressBtnOui : (() -> Unit)? = null
    private var onPressBtnNon : (() -> Unit)? = null

    fun setOnPressBtnOuiListner(onPressBtnOui : (() -> Unit))
    {
        this.onPressBtnOui = onPressBtnOui
    }

    fun setOnPressBtnNonListner(onPressBtnNon : (() -> Unit))
    {
        this.onPressBtnNon = onPressBtnNon
    }

    companion object {
        const val  TAG = "DialogEnregisterDemande"
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window!!
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
    }
}