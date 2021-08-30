package fr.strada.smobile.ui.mes_frais.nouvelle_demande.dialog_envoie_demande

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import fr.strada.smobile.R
import fr.strada.smobile.utils.KeyBoardUtils
import kotlinx.android.synthetic.main.dialog_envoie_demande.*

class DialogEnvoieDemande : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_envoie_demande, container, false);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation()
    {
        btnEnvoyer.setOnClickListener {
            KeyBoardUtils.hideKeyboard(requireActivity())
            val email = txtEmail.text.toString()
            onPressBtnEnvoyerListner?.invoke(dialog!!,email)
        }
        btnAnnuler.setOnClickListener {
            KeyBoardUtils.hideKeyboard(requireActivity())
            dismiss()
        }
    }

    var onPressBtnEnvoyerListner : ((Dialog,String)-> Unit)? = null

    fun setBtnEnvoyerListner(onPressBtnEnvoyerListner : (Dialog,String) -> Unit){
        this.onPressBtnEnvoyerListner = onPressBtnEnvoyerListner
    }

    companion object {
        const val TAG = "DialogEnvoieDemande"
    }
}