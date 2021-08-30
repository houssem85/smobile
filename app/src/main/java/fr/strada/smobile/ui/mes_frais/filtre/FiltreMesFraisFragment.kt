package fr.strada.smobile.ui.mes_frais.filtre

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.DialogFilterMesFraisBinding
import fr.strada.smobile.ui.demande_absence.TodayDecorator
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.dialog_filter_mes_frais.*
import javax.inject.Inject

class FiltreMesFraisFragment : DialogFragment() {

    private lateinit var binding : DialogFilterMesFraisBinding
    lateinit var viewModel : FiltreMesFraisViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter_mes_frais, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
        observeDateDebut()
        observeDateFin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    private fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(requireActivity(), providerFactory).get(FiltreMesFraisViewModel::class.java)
    }

    private fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {
        txtDateDebut.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date, null)
            val datePickerDebut = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
            val btnTerminer = dialogView.findViewById<Button>(R.id.btnTerminer)
            val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
            val currentDate = CalendarDay.today()
            datePickerDebut.addDecorators(TodayDecorator(activity, currentDate))
            dialogBuilder.setView(dialogView)
            val dialogDateDebut = dialogBuilder.create()
            dialogDateDebut.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogDateDebut.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (viewModel.dateDebut.value != "") {
                val year = viewModel.dateDebut.value?.substring(6, 10)?.toInt()
                val month = viewModel.dateDebut.value?.substring(3, 5)?.toInt()
                val day = viewModel.dateDebut.value?.substring(0, 2)?.toInt()
                datePickerDebut.selectedDate = CalendarDay.from(year!!, month!!, day!!)
            }
            if (viewModel.dateFin.value != "") {
                val year = viewModel.dateFin.value?.substring(6, 10)?.toInt()
                val month = viewModel.dateFin.value?.substring(3, 5)?.toInt()
                val day = viewModel.dateFin.value?.substring(0, 2)?.toInt()
                datePickerDebut.state().edit().setMaximumDate(CalendarDay.from(year!!, month!!, day!!)).commit()
            }
            btnTerminer.setOnClickListener {
                if (datePickerDebut.selectedDate != null) {
                    val strDate = "${String.format(
                        "%02d",
                        datePickerDebut.selectedDate!!.day
                    )}/${String.format(
                        "%02d",
                        datePickerDebut.selectedDate!!.month
                    )}/${datePickerDebut.selectedDate!!.year}"
                    txtDateDebut.setText(strDate)
                    dialogDateDebut.dismiss()
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.veuillez_choisir_une_date),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            btnAnnuler.setOnClickListener {
                dialogDateDebut.dismiss()
            }
            dialogDateDebut.show()
        }
        txtDateFin.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date, null)
            val datePickerFin = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
            val btnTerminer = dialogView.findViewById<Button>(R.id.btnTerminer)
            val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
            val currentDate = CalendarDay.today()
            datePickerFin.addDecorators(TodayDecorator(activity, currentDate))
            dialogBuilder.setView(dialogView)
            val dialogDatefin = dialogBuilder.create()
            dialogDatefin.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogDatefin.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (viewModel.dateFin.value != "") {
                val year = viewModel.dateFin.value?.substring(6, 10)?.toInt()
                val month = viewModel.dateFin.value?.substring(3, 5)?.toInt()
                val day = viewModel.dateFin.value?.substring(0, 2)?.toInt()
                datePickerFin.selectedDate = CalendarDay.from(year!!, month!!, day!!)
            }
            if (viewModel.dateDebut.value != "") {
                val year = viewModel.dateDebut.value?.substring(6, 10)?.toInt()
                val month = viewModel.dateDebut.value?.substring(3, 5)?.toInt()
                val day = viewModel.dateDebut.value?.substring(0, 2)?.toInt()
                datePickerFin.state().edit().setMinimumDate(CalendarDay.from(year!!, month!!, day!!)).commit()
            }
            btnTerminer.setOnClickListener {
                if (datePickerFin.selectedDate != null) {
                    val strDate = "${String.format(
                        "%02d",
                        datePickerFin.selectedDate!!.day
                    )}/${String.format(
                        "%02d",
                        datePickerFin.selectedDate!!.month
                    )}/${datePickerFin.selectedDate!!.year}"
                    txtDateFin.setText(strDate)
                    dialogDatefin.dismiss()
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.veuillez_choisir_une_date),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            btnAnnuler.setOnClickListener {
                dialogDatefin.dismiss()
            }
            dialogDatefin.show()
        }
        btnAppliquer?.setOnClickListener {
            val isPeriodeFilterActive = switchPeriode.isChecked
            val dateFin = txtDateFin.text.toString()
            val dateDebut = txtDateDebut.text.toString()
            val isDemandeAccepteeActive = viewModel.isDemandeAccepteeActive.value!!
            val isDemandeEnCourActive = viewModel.isDemandeEnCourActive.value!!
            onSubmit?.invoke(isPeriodeFilterActive,dateDebut,dateFin,isDemandeAccepteeActive,isDemandeEnCourActive)
            dialog?.dismiss()
        }
    }

    private fun observeDateDebut(){
        viewModel.dateDebut.observe(viewLifecycleOwner,{
            switchPeriode.isEnabled = !(viewModel.dateDebut.value!!.isEmpty() || viewModel.dateFin.value!!.isEmpty())
        })
    }

    private fun observeDateFin(){
        viewModel.dateFin.observe(viewLifecycleOwner,{
            switchPeriode.isEnabled = !(viewModel.dateDebut.value!!.isEmpty() || viewModel.dateFin.value!!.isEmpty())
        })
    }

    override fun onStart() {
        super.onStart()
        try {
            val isTableteSize = resources.getBoolean(R.bool.isTablet)
            if(!isTableteSize)
            {
                val window = dialog!!.window
                window!!.setGravity(Gravity.BOTTOM)
                val params = window.attributes
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = params
            }
        }catch (ex:Exception){

        }
    }

    companion object {
        const val TAG = "FiltreMesFraisFragment"
    }

    private var onSubmit : ((Boolean,String,String,Boolean,Boolean) -> Unit)? = null

    fun setOnSbmitListner(onSubmit:(Boolean,String,String,Boolean,Boolean) -> Unit)
    {
        this.onSubmit = onSubmit
    }
}