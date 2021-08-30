package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.detail_demande_absence

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import eightbitlab.com.blurview.RenderScriptBlur
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentDetailDemandeAbsenceBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.detail_demande_absence.DetailDemandeAbsenceComponent
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.EventObserver
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_detail_demande_absence.blurView
import kotlinx.android.synthetic.main.fragment_detail_demande_absence.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class DetailDemandeAbsenceFragment : BaseFragment() {

    private lateinit var component: DetailDemandeAbsenceComponent
    private lateinit var viewModel: DetailDemandeAbsenceViewModel
    private lateinit var binding: FragmentDetailDemandeAbsenceBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var dialogLoading: Dialog
    lateinit var dialogSuccess: Dialog
    lateinit var dialogAttente: Dialog
    lateinit var dialogRefused: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_demande_absence, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimation()

        dialogLoading = provideDialogLoading(requireActivity())
        dialogSuccess = provideDialogSuccess(requireActivity())
        dialogAttente = provideDialogAttente(requireActivity())
        dialogRefused = provideDialogRefused(requireActivity())
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.detailDemandeAbsenceComponent().setContext(requireContext()).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(DetailDemandeAbsenceViewModel::class.java)
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnValider.observe(this, EventObserver {
            it.let {
                GlobalScope.launch(Dispatchers.Main)
                {
                    //  blurView()
                    try {
                        dialogLoading.show()
                        delay(3000)
                        dialogLoading.dismiss()
                        dialogSuccess.show()
                    } catch (ex: Exception) {

                    }
                }
            }
        })

        viewModel.pressBtnEnAttente.observe(this, EventObserver {
            it.let {

                GlobalScope.launch(Dispatchers.Main)
                {
                    //  blurView()
                    try {
                        dialogLoading.show()
                        delay(3000)
                        dialogLoading.dismiss()
                        dialogAttente.show()
                    } catch (ex: Exception) {

                    }
                }
            }
        })

        viewModel.pressBtnRefuser.observe(this, EventObserver {
            it.let {

                GlobalScope.launch(Dispatchers.Main)
                {
                    //  blurView()
                    try {
                        dialogLoading.show()
                        delay(3000)
                        dialogLoading.dismiss()
                        dialogRefused.show()
                    } catch (ex: Exception) {

                    }
                }
            }
        })
    }

    fun startAnimation() {
        val fromBottomAnimation = Utils.inFromBottomAnimation()
        layout_detail_demande_absence.startAnimation(fromBottomAnimation)
    }

    private fun blurView() {
        val decorView = activity?.window?.decorView
        val rootView = decorView?.findViewById(android.R.id.content) as ViewGroup
        val radius = 1f
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(requireContext()))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(false)
    }

    private fun provideDialogLoading(context: Activity): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        return dialog
    }

    private fun provideDialogSuccess(context: Activity): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_success_validate, null)
        val btnOk = dialogView.findViewById<AppCompatButton>(R.id.btnOk)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        btnOk.setOnClickListener {
            sendFinishFragmentDetailDemandeAbsenceReceiver()
            dialog.dismiss()
        }
        return dialog
    }

    private fun provideDialogRefused(context: Activity): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_absence_refused, null)
        val btnOk = dialogView.findViewById<AppCompatButton>(R.id.btnOk)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        btnOk.setOnClickListener {
            sendFinishFragmentDetailDemandeAbsenceReceiver()
            dialog.dismiss()
        }
        return dialog
    }

    private fun provideDialogAttente(context: Activity): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_absence_enattente, null)
        val btnOk = dialogView.findViewById<AppCompatButton>(R.id.btnOk)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        btnOk.setOnClickListener {
            sendFinishFragmentDetailDemandeAbsenceReceiver()
            dialog.dismiss()
        }
        return dialog
    }

    fun sendFinishFragmentDetailDemandeAbsenceReceiver()
    {
        val intent = Intent("FinishFragmentDetailDemandeAbsenceReceiver")
        requireActivity().sendBroadcast(intent)
    }

    override fun onPause() {
        super.onPause()

    }

}