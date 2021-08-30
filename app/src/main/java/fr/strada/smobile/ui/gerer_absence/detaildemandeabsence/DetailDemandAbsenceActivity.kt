package fr.strada.smobile.ui.gerer_absence.detaildemandeabsence

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import eightbitlab.com.blurview.RenderScriptBlur
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.ScheduleStatus
import fr.strada.smobile.databinding.ActivityDetailDemandeAbsenceBinding
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.EventObserver
import kotlinx.android.synthetic.main.activity_detail_demande_absence.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailDemandAbsenceActivity : BaseActivity() {

    private lateinit var viewModel: DetailDemandAbsenceViewModel
    private lateinit var binding: ActivityDetailDemandeAbsenceBinding
    lateinit var dialogLoading: Dialog
    lateinit var dialogSuccess: Dialog
    lateinit var dialogAttente: Dialog
    lateinit var dialogRefused: Dialog
    var scheduleState: ScheduleStatus = ScheduleStatus.STOP


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        initViewModel()
        bindViewModel()
        setupNavigation()
        dialogLoading = provideDialogLoading(this)
        dialogSuccess = provideDialogSuccess(this)
        dialogAttente = provideDialogAttente(this)
        dialogRefused = provideDialogRefused(this)
        manageStartButton()
    }


    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_demande_absence)
        binding.lifecycleOwner = this
    }


    private fun initViewModel() {
        viewModel = DetailDemandAbsenceViewModel()
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
    }

    private fun setupNavigation() {
        viewModel.pressBtnBackEvent.observe(this, EventObserver {
            onBackPressed()
        })

        viewModel.pressBtnValider.observe(this, EventObserver {
            GlobalScope.launch(Dispatchers.Main)
            {
                blurView()
                dialogLoading.show()
                delay(3000)
                dialogLoading.dismiss()
                dialogSuccess.show()
            }
        })

        viewModel.pressBtnEnAttente.observe(this, EventObserver {
            GlobalScope.launch(Dispatchers.Main)
            {
                blurView()
                dialogLoading.show()
                delay(3000)
                dialogLoading.dismiss()
                dialogAttente.show()
            }
        })

        viewModel.pressBtnRefuser.observe(this, EventObserver {
            GlobalScope.launch(Dispatchers.Main)
            {
                blurView()
                dialogLoading.show()
                delay(3000)
                dialogLoading.dismiss()
                dialogRefused.show()
            }
        })
        /*
        viewModel.pressBtnChrono.observe(this, EventObserver {
            when (scheduleState) {
                ScheduleStatus.START -> {
                    (application as SmobileApp).stop()
                }
                ScheduleStatus.STOP -> {
                    (application as SmobileApp).start()
                }
            }
        })
        */
    }

    private fun blurView() {
        val decorView = window.decorView
        val rootView = decorView.findViewById(android.R.id.content) as ViewGroup
        val radius = 1f
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
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
            onBackPressed()
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
            onBackPressed()
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
            onBackPressed()
        }
        return dialog
    }

    private fun manageStartButton() {
        (application as SmobileApp).isChronoStarted.observe(this, Observer {
            if (!it) {
                btn_draggable.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_start
                    )
                )
                scheduleState = ScheduleStatus.STOP
            } else {
                btn_draggable.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_stop
                    )
                )
                scheduleState = ScheduleStatus.START
            }
        })
    }

    override fun onPause() {
        super.onPause()
        dialogRefused.dismiss()
        dialogAttente.dismiss()
        dialogSuccess.dismiss()
    }

}