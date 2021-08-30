package fr.strada.smobile.ui.spi.ui.reglage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.shawnlin.numberpicker.NumberPicker
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentSettingsBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.utils.listner.DialogSynchroListener
import fr.strada.smobile.utils.listner.DialogTempsNotificationsPointeuseListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import javax.inject.Inject


class SettingsFragment : BaseFragment(), DialogSynchroListener,
    DialogTempsNotificationsPointeuseListner {

    lateinit var viewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var switchsync: SwitchMaterial

    lateinit var dialogSynchronize: Dialog

    lateinit var msgInfoViePrive: TextView
    lateinit var btnActiver: Button

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchsync = view.findViewById(R.id.switch_vie_prive) as SwitchMaterial
        dialogSynchronize = initDialogSyncro(requireContext(), this)
        initSwitch(view)
        if (activity is MainActivitySpi)
        {
            (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.INVISIBLE
            (activity as MainActivitySpi).image_back_main.visibility = View.VISIBLE
        }
    }

    override fun initComponent() {

    }

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(SettingsViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel

    }

    override fun setupNavigation() {

    }

    override fun onClickBtnAnnulerSynchro(dialog: Dialog) {
        viewModel.isPrivate.value = !viewModel.isPrivate.value!!
        dialogSynchronize.dismiss()
    }

    override fun onClickBtnConfirmer(dialog: Dialog) {
        viewModel.changeValuePrivate( true)
        dialogSynchronize.dismiss()
    }

    override fun onClickBtnTerminer(
        dialog: Dialog,
        hoursPicker: NumberPicker,
        minutesPicker: NumberPicker,
        dataHours: Array<String>,
        dataMinutes: Array<String>
    ) {
        dialog.dismiss()
    }

    override fun onClickBtnAnnuler(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onDismissDialogListner(dialog: Dialog) {
        viewModel.isDialogViePriveShown = false
    }

    override fun onShowDialogListner(
        dialog: Dialog,
        hoursPicker: NumberPicker,
        minutesPicker: NumberPicker,
        dataHours: Array<String>,
        dataMinutes: Array<String>
    ) {
        viewModel.isDialogViePriveShown = true
    }

    private fun initDialogSyncro(
        context: Context,
        dialogsynchroListener: DialogSynchroListener
    ): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_vie_prive, null)
        btnActiver = dialogView.findViewById(R.id.btnActiver)
        val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
        msgInfoViePrive = dialogView.findViewById(R.id.msgInfo)
        dialogBuilder.setView(dialogView)

        val dialogSynchronize = dialogBuilder.create()
        dialogSynchronize.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSynchronize.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnActiver.setOnClickListener {
            dialogsynchroListener.onClickBtnConfirmer(dialogSynchronize)
        }
        btnAnnuler.setOnClickListener {
            dialogsynchroListener.onClickBtnAnnulerSynchro(dialogSynchronize)
        }
        return dialogSynchronize
    }


    private fun initSwitch(view: View) {

        viewModel.isPrivate.observe(viewLifecycleOwner, { activated ->
            switchsync.isChecked = activated
        })

        switchsync.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                msgInfoViePrive.text = getText(R.string.Etes_vous_sur_de_vouloir_activer_vie_prive)
                btnActiver.text = getText(R.string.Activer)
                dialogSynchronize.show()
            } else {
                viewModel.changeValuePrivate(false)
                msgInfoViePrive.text = getText(R.string.Etes_vous_sur_de_vouloir_desactiver_vie_prive)
                btnActiver.text = getText(R.string.Desactiver)
                dialogSynchronize.show()
            }
        }
    }

}