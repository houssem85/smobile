package fr.strada.smobile.ui.reglage

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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import fr.strada.smobile.R
import fr.strada.smobile.data.repositories.ReglageRepository
import fr.strada.smobile.databinding.FragmentReglageBinding
import fr.strada.smobile.di.reglage.ReglageComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.listner.DialogSynchroListener
import fr.strada.smobile.utils.listner.DialogTempsNotificationsPointeuseListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_reglage.*
import javax.inject.Inject

class ReglageFragment : BaseFragment(), DialogSynchroListener,
    DialogTempsNotificationsPointeuseListner {

    private lateinit var component: ReglageComponent


    lateinit var viewModel: ReglageViewModel
    private lateinit var binding: FragmentReglageBinding
    private lateinit var switchsync: SwitchMaterial
    private lateinit var regRepository: ReglageRepository

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var dialog: Dialog

    lateinit var dialogSynchronize: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reglage, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //   Toast.makeText(requireContext(),"true", Toast.LENGTH_SHORT).show()
        initComponent()
        initViewModel()
        bindViewModel()
        regRepository = ReglageRepository(requireContext())
        initSwitch(view)
        dialogSynchronize = initDialogSyncro(requireContext(), this)
        if (viewModel.isDialogTempsDeTravailShown) {
            dialog.show()
        }
        if (activity is MainTabletteActivity) {
            setupContainer()
        }
    }

    private fun initDialogSyncro(
        context: Context,
        dialogsynchroListener: DialogSynchroListener
    ): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_synchronise_firebase, null)
        val btnActiver = dialogView.findViewById<Button>(R.id.btnActiver)
        val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
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
        switchsync = view.findViewById(R.id.switch_synchro) as SwitchMaterial
        viewModel.isLogged.observe(viewLifecycleOwner, { connected ->
            switchsync.isChecked = connected
        })

        switchsync.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogSynchronize.show()
            } else {
                viewModel.changeValueSynchronized(false)
            }
        }
    }

    override fun initComponent() {
        component = if (activity is MainActivity) {
            (activity as MainActivity).component.reglageComponentComponent()
                .setContext(requireContext())
                .setDialogTempsNotificationsPointeuseListner(this)
                .setdialogSynchroListener(this)
                .build()
        } else {
            (activity as MainTabletteActivity).component.reglageComponentComponent()
                .setContext(requireContext())
                .setDialogTempsNotificationsPointeuseListner(this)
                .setdialogSynchroListener(this)
                .build()
        }
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(ReglageViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        btnOpenMenu?.setOnClickListener {
            if (activity is MainActivity) {
                (activity as MainActivity).openDrawer()
            }
        }
        lblTempsDeTravail?.setOnClickListener {
            dialog.show()
        }
        txtTempsDeTravail?.setOnClickListener {
            dialog.show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReglageFragment()
    }

    override fun onClickBtnTerminer(
        dialog: Dialog,
        hoursPicker: com.shawnlin.numberpicker.NumberPicker,
        minutesPicker: com.shawnlin.numberpicker.NumberPicker,
        dataHours: Array<String>,
        dataMinutes: Array<String>
    ) {
        dialog.dismiss()
        val minutes = dataMinutes[minutesPicker.value]
        val hours = dataHours[hoursPicker.value]
        viewModel.hours.value = hours
        viewModel.minutes.value = minutes
        viewModel.isPointeuseActive.value = true
    }

    override fun onClickBtnAnnuler(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onClickBtnAnnulerSynchro(dialog: Dialog) {
        dialogSynchronize.dismiss()
    }

    override fun onClickBtnConfirmer(dialog: Dialog) {
        viewModel.changeValueSynchronized( true)
        dialogSynchronize.dismiss()
    }

    override fun onDismissDialogListner(dialog: Dialog) {
        viewModel.isDialogTempsDeTravailShown = false
    }

    override fun onShowDialogListner(
        dialog: Dialog,
        hoursPicker: com.shawnlin.numberpicker.NumberPicker,
        minutesPicker: com.shawnlin.numberpicker.NumberPicker,
        dataHours: Array<String>,
        dataMinutes: Array<String>
    ) {
        viewModel.isDialogTempsDeTravailShown = true
        hoursPicker.value = dataHours.indexOf(viewModel.hours.value)
        minutesPicker.value = dataMinutes.indexOf(viewModel.minutes.value)
    }


    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frameRegelage.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frameRegelage.layoutParams = layoutParams
    }
}


