package fr.strada.smobile.ui.demande_absence


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shawnlin.numberpicker.NumberPicker
import eightbitlab.com.blurview.RenderScriptBlur
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentAbsenceRequestBinding
import fr.strada.smobile.di.absence_request.AbsenceRequestComponent
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.Toggle
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AbsenceRequestFragment : BaseFragment() {

    private lateinit var component: AbsenceRequestComponent
    private lateinit var viewModel: AbsenceRequestViewModel
    private lateinit var binding: FragmentAbsenceRequestBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var dialogBuilder: AlertDialog.Builder
    lateinit var loaderDialogBuilder: AlertDialog.Builder
    lateinit var dialogView: View
    lateinit var btnTerminerStart: AppCompatButton
    lateinit var btnAnnulerStart: AppCompatButton
    lateinit var btnTerminerEnd: AppCompatButton
    lateinit var btnAnnulerEnd: AppCompatButton
    var datePickerStart: MaterialCalendarView? = null
    lateinit var datePickerEnd: MaterialCalendarView
    lateinit var hoursPickerStart: NumberPicker
    lateinit var minutesPickerStart: NumberPicker
    lateinit var hoursPickerEnd: NumberPicker
    lateinit var minutesPickerEnd: NumberPicker

    val dataHours = arrayOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08",
        "09", "10", "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "20", "21", "22", "23"
    )
    val dataMinutes = arrayOf("00", "15", "30", "45")
    lateinit var currentDate: CalendarDay
    lateinit var dialogStart: AlertDialog
    lateinit var dialogEnd: AlertDialog
    lateinit var successDialog: AlertDialog
    lateinit var loadingDialog: AlertDialog
    lateinit var btnValidate: AppCompatButton
    lateinit var decorView: View
    lateinit var rootView: ViewGroup
    var isExpanded = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_absence_request, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initComponent() {
        component = if (activity is MainActivity) {
            (activity as MainActivity).component
                .absenceRequestComponent()
                .setContext(requireContext())
                .build()
        } else {
            (activity as MainTabletteActivity).component
                .absenceRequestComponent()
                .setContext(requireContext())
                .build()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AbsenceRequestFragment()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(AbsenceRequestViewModel::class.java)
        // reset viewModel
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnOpenMenuEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (activity is MainActivity) {
                    (activity as MainActivity).openDrawer()
                }
            }
        })

        viewModel.pressBtnSelectTypeAbsence.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    reset()
                    Toggle.toggleLayout(
                        menu_myadmin_expand.visibility == GONE,
                        img_expand,
                        menu_myadmin_expand
                    )

                }
            })
        viewModel.selectCongeEvent.observe(viewLifecycleOwner, {
            it?.let {
                edit_type.setText(txt_conge.text)
                Toggle.toggleLayout(
                    menu_myadmin_expand.visibility == GONE,
                    img_expand,
                    menu_myadmin_expand
                )
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.colorPrimary) }
                    ?.let { it2 ->
                        edit_type.setTextColor(
                            it2
                        )
                    }
                decorateValidateButton()
            }
        })
        viewModel.selectRecuperationEvent.observe(viewLifecycleOwner, {
            it?.let {
                edit_type.setText(txt_recuperation.text)
                Toggle.toggleLayout(
                    menu_myadmin_expand.visibility == GONE,
                    img_expand,
                    menu_myadmin_expand
                )
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.colorPrimary) }
                    ?.let { it2 ->
                        edit_type.setTextColor(
                            it2
                        )
                    }
                decorateValidateButton()
            }
        })
        viewModel.selectReposCompensateurEvent.observe(viewLifecycleOwner, {
            it?.let {
                edit_type.setText(txt_repos.text)
                Toggle.toggleLayout(
                    menu_myadmin_expand.visibility == GONE,
                    img_expand,
                    menu_myadmin_expand
                )
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.colorPrimary) }
                    ?.let { it2 ->
                        edit_type.setTextColor(
                            it2
                        )
                    }
                decorateValidateButton()
            }
        })
        viewModel.selectReposCompensateurRemplacementEvent.observe(viewLifecycleOwner, {
            it?.let {
                edit_type.setText(txt_repos_replacement.text)
                Toggle.toggleLayout(
                    menu_myadmin_expand.visibility == GONE,
                    img_expand,
                    menu_myadmin_expand
                )
                activity?.let { it1 -> ContextCompat.getColor(it1, R.color.colorPrimary) }
                    ?.let { it2 ->
                        edit_type.setTextColor(
                            it2
                        )
                    }
                decorateValidateButton()
            }
        })

        viewModel.pressBtnSelectDateDebutEvent.observe(viewLifecycleOwner, {
            it?.let {
                //  view_edit_date_deb.setBackgroundResource(R.drawable.bg_sub_menu_edit)
                reset()
                if (edit_type.text.toString().isNotEmpty()) {
                    dialogStart.show()
                    viewModel.dialogStartIsShown.value = true
                    viewModel.layoutErrorTypeAbsence.value = GONE
                    viewModel.layoutErrorDateDeb.value = GONE
                    view_edit_type.setBackgroundResource(R.drawable.bg_sub_menu_edit)
                } else {
                    viewModel.layoutErrorDateDeb.value = GONE
                    viewModel.layoutErrorTypeAbsence.value = VISIBLE
                    view_edit_type.setBackgroundResource(R.drawable.bg_error_red)
                }
            }
        })

        viewModel.pressBtnSelectDateFinEvent.observe(viewLifecycleOwner, {
            it?.let {
                reset()
                if (edit_date_deb.text.toString().isNotEmpty()) {
                    dialogEnd.show()
                    viewModel.dialogEndIsShow.value = true
                    viewModel.layoutErrorTypeAbsence.value = GONE
                    viewModel.layoutErrorDateDeb.value = GONE
                    view_edit_date_deb.setBackgroundResource(R.drawable.bg_sub_menu_edit)
                    view_edit_type.setBackgroundResource(R.drawable.bg_sub_menu_edit)
                } else {
                    viewModel.layoutErrorDateDeb.value = VISIBLE
                    viewModel.layoutErrorTypeAbsence.value = GONE
                    view_edit_date_deb.setBackgroundResource(R.drawable.bg_error_red)
                }
            }
        })

        viewModel.pressBtnValiderEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (edit_date_deb.text.toString().isNotEmpty() && edit_type.text.toString()
                        .isNotEmpty() &&
                    edit_date_fin.text.toString().isNotEmpty()
                ) {
                    GlobalScope.launch(Dispatchers.Main)
                    {
                        try {
                            blureView()
                         //   showLoadingDialog()
                            loadingDialog.show()
                            viewModel.loadingDialogIsShown.value = true
                            delay(3000)
                            loadingDialog.dismiss()
                            viewModel.loadingDialogIsShown.value = false
                            successDialog.show()
                            blurView.setupWith(rootView)
                                .setBlurEnabled(false)
                        } catch (ex: Exception) {
                        }
                    }
                }
            }
        })
    }

    private fun reset() {
        viewModel.layoutErrorTypeAbsence.value = GONE
        viewModel.layoutErrorDateDeb.value = GONE
        view_edit_date_deb.setBackgroundResource(R.drawable.bg_sub_menu_edit)
        view_edit_date_fin.setBackgroundResource(R.drawable.bg_sub_menu_edit)
        view_edit_type.setBackgroundResource(R.drawable.bg_sub_menu_edit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        currentDate = CalendarDay.today()
        initAlertDialogStart()
        initAlertDialogEnd()
        initLoadingDialog()
        initSuccessAlertDialog()
        startAnimation(view)
        if (activity is MainTabletteActivity) {
            setupContainer()
        }
        retrieveInformation()
        decorateValidateButton()
    }

    private fun retrieveInformation() {
        if (viewModel.dateS.isNotEmpty()) {
            val year = viewModel.dateS.substring(6, 10).toInt()
            val mon = viewModel.dateS.substring(3, 5).toInt()
            val day = viewModel.dateS.substring(0, 2).toInt()
            datePickerStart!!.selectedDate = CalendarDay.from(year, mon, day)
            datePickerStart!!.currentDate = CalendarDay.from(year, mon, day)
            datePickerEnd.addDecorators(
                CurrentDayDecorator(
                    activity,
                    datePickerStart!!.selectedDate!!
                )
            )
            datePickerEnd.state().edit().setMinimumDate(datePickerStart!!.selectedDate!!).commit()
            ///
            val hours = viewModel.dateS.substring(13, 15)
            hoursPickerStart.value = dataHours.indexOf(hours)
            //
            val minutes = viewModel.dateS.substring(16, 18)
            minutesPickerStart.value = dataMinutes.indexOf(minutes)
        }
        if (viewModel.dateE.isNotEmpty()) {
            val year = viewModel.dateE.substring(6, 10).toInt()
            val mon = viewModel.dateE.substring(3, 5).toInt()
            val day = viewModel.dateE.substring(0, 2).toInt()
            datePickerEnd.selectedDate = CalendarDay.from(year, mon, day)
            datePickerEnd.currentDate = CalendarDay.from(year, mon, day)
            datePickerStart!!.state().edit().setMaximumDate(datePickerEnd.selectedDate!!).commit()
            //
            val hours = viewModel.dateE.substring(13, 15)
            hoursPickerEnd.value = dataHours.indexOf(hours)
            //
            val minutes = viewModel.dateE.substring(16, 18)
            minutesPickerEnd.value = dataMinutes.indexOf(minutes)
            //
        }
    }


    private fun init(view: View) {
        btnValidate = view.findViewById(R.id.btn_validate)
        decorView = activity?.window?.decorView ?: decorView
        rootView = decorView.findViewById(android.R.id.content) as ViewGroup
    }


    private fun startAnimation(view: View) {
        val toTop: Animation = AnimationUtils.loadAnimation(activity, R.anim.bottom_to_top)
        val content = view.findViewById<ConstraintLayout>(R.id.contentAbsence)
        content.startAnimation(toTop)
    }


    private fun displayStartDate() {
        try {
            val year = datePickerStart!!.selectedDate!!.year
            val mon = datePickerStart!!.selectedDate!!.month
            val strMon = datePickerStart!!.selectedDate!!.month.let {
                String.format("%02d", it)
            }

            val month = getMonthName(requireContext(), mon - 1)
            val day = datePickerStart!!.selectedDate!!.day.let {
                String.format("%02d", it)
            }

            val date = "$day $month $year"
            val hours = dataHours[hoursPickerStart.value]
            val minutes = dataMinutes[minutesPickerStart.value]
            edit_date_deb.setText("$date - $hours:$minutes")
            viewModel.dateS = "$day $strMon $year - $hours:$minutes"
            decorateValidateButton()
        } catch (ex: Exception) {

        }
    }

    private fun displayEndDate() {
        try {
            val year = datePickerEnd.selectedDate!!.year
            val mon = datePickerEnd.selectedDate!!.month
            val strMonth = datePickerEnd.selectedDate!!.month.let {
                String.format("%02d", it)
            }
            val month = getMonthName(requireContext(), mon - 1)
            val day = datePickerEnd.selectedDate!!.day.let {
                String.format("%02d", it)
            }
            val date = "$day $month $year"
            val hours = dataHours[hoursPickerEnd.value]
            val minutes = dataMinutes[minutesPickerEnd.value]
            edit_date_fin.setText("$date - $hours:$minutes")
            viewModel.dateE = "$day $strMonth $year - $hours:$minutes"
            decorateValidateButton()
        } catch (ex: Exception) {
        }

    }


    private fun initAlertDialogStart() {
        dialogBuilder = AlertDialog.Builder(context)
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date_time, null)
        btnTerminerStart = dialogView.findViewById<AppCompatButton>(R.id.btnTerminer)
        btnAnnulerStart = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        datePickerStart = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        hoursPickerStart = dialogView.findViewById<NumberPicker>(R.id.hoursPicker)
        minutesPickerStart = dialogView.findViewById<NumberPicker>(R.id.minutesPicker)
        hoursPickerStart.minValue = 0
        hoursPickerStart.maxValue = dataHours.size - 1
        hoursPickerStart.displayedValues = dataHours
        hoursPickerStart.value = 17
        minutesPickerStart.minValue = 0
        minutesPickerStart.maxValue = dataMinutes.size - 1
        minutesPickerStart.displayedValues = dataMinutes
        minutesPickerStart.value = 0
        dialogBuilder.setView(dialogView)
        datePickerStart = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        datePickerStart?.selectedDate = currentDate
        dialogStart = dialogBuilder.create()
        dialogStart.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogStart.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        datePickerStart?.addDecorators(
            CurrentDayDecoratorStartCalendar(
                activity,
                datePickerStart!!.selectedDate!!
            )
        )
        datePickerStart?.state()?.edit()?.setMinimumDate(datePickerStart!!.selectedDate)
            ?.setCalendarDisplayMode(CalendarMode.MONTHS)
            ?.commit()

        //// show alertDialogStart when changing orientation
        if (viewModel.dialogStartIsShown.value == true) {
            dialogStart.show()
        } else {
            dialogStart.dismiss()
        }

        btnTerminerStart.setOnClickListener {
            if (datePickerStart!!.selectedDate != null) {

                if ((datePickerStart?.selectedDate == datePickerEnd.selectedDate) && ((hoursPickerEnd.value < hoursPickerStart.value))) {
                    Toast.makeText(activity, "erreur, date invalide", Toast.LENGTH_SHORT).show()
                } else if ((datePickerStart?.selectedDate == datePickerEnd.selectedDate) && (hoursPickerEnd.value == hoursPickerStart.value) && (minutesPickerEnd.value <= minutesPickerStart.value)) {
                    Toast.makeText(activity, "erreur, date invalide", Toast.LENGTH_SHORT).show()
                } else {

                    dialogStart.dismiss()
                    viewModel.dialogStartIsShown.value = false

                    displayStartDate()
                    datePickerEnd.removeDecorators()
                    datePickerEnd.addDecorators(
                        CurrentDayDecorator(
                            activity,
                            datePickerStart!!.selectedDate!!
                        )
                    )
                    datePickerEnd.state().edit().setMinimumDate(datePickerStart!!.selectedDate)
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit()

                    if (viewModel.dateE != "") {
                        displayDuration()
                    }
                }
            }
        }

        btnAnnulerStart.setOnClickListener {
            parseStrDateInterventionInDatePicker(edit_date_deb.text.toString(), datePickerStart!!)
            parseHourInHourPicker(edit_date_deb.text.toString(), hoursPickerStart, dataHours)
            parseMinutesInMinutesPicker(
                edit_date_deb.text.toString(),
                minutesPickerStart,
                dataMinutes
            )
            dialogStart.dismiss()
            viewModel.dialogStartIsShown.value = false

        }

    }


    private fun initAlertDialogEnd() {
        dialogBuilder = AlertDialog.Builder(context)
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date_time, null)
        btnTerminerEnd = dialogView.findViewById<AppCompatButton>(R.id.btnTerminer)
        btnAnnulerEnd = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        datePickerEnd = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        hoursPickerEnd = dialogView.findViewById<NumberPicker>(R.id.hoursPicker)
        minutesPickerEnd = dialogView.findViewById<NumberPicker>(R.id.minutesPicker)
        hoursPickerEnd.minValue = 0
        hoursPickerEnd.maxValue = dataHours.size - 1
        hoursPickerEnd.displayedValues = dataHours
        hoursPickerEnd.value = 17
        minutesPickerEnd.minValue = 0
        minutesPickerEnd.maxValue = dataMinutes.size - 1
        minutesPickerEnd.displayedValues = dataMinutes
        minutesPickerEnd.value = 0
        dialogBuilder.setView(dialogView)
        datePickerEnd = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        datePickerEnd.selectionColor = ContextCompat.getColor(requireContext(), R.color.bluelight)
        dialogEnd = dialogBuilder.create()
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogEnd.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //// show alertDialogEnd when changing orientation
        if (viewModel.dialogEndIsShow.value == true) {
            dialogEnd.show()
        } else {
            dialogEnd.dismiss()
        }

        btnTerminerEnd.setOnClickListener {
            if (datePickerEnd.selectedDate != null) {
                if ((datePickerStart?.selectedDate == datePickerEnd.selectedDate) && ((hoursPickerEnd.value < hoursPickerStart.value))) {
                    Toast.makeText(activity, "erreur, date invalide", Toast.LENGTH_SHORT).show()
                } else if ((datePickerStart?.selectedDate == datePickerEnd.selectedDate) && (hoursPickerEnd.value == hoursPickerStart.value) && (minutesPickerEnd.value <= minutesPickerStart.value)) {
                    Toast.makeText(activity, "erreur, date invalide", Toast.LENGTH_SHORT).show()
                } else {

                    dialogEnd.dismiss()
                    viewModel.dialogEndIsShow.value = false
                    displayEndDate()
                    datePickerStart!!.state().edit().setMaximumDate(datePickerEnd.selectedDate)
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit()
                    displayDuration()
                }
            }

        }

        btnAnnulerEnd.setOnClickListener()
        {
            parseStrDateInterventionInDatePicker(edit_date_fin.text.toString(), datePickerEnd)
            parseHourInHourPicker(edit_date_fin.text.toString(), hoursPickerEnd, dataHours)
            parseMinutesInMinutesPicker(
                edit_date_fin.text.toString(),
                minutesPickerEnd,
                dataMinutes
            )
            dialogEnd.dismiss()
            viewModel.dialogEndIsShow.value = false
        }
    }

    private fun displayDuration() {
        val sdf = SimpleDateFormat("dd M yyyy - hh:mm")
        val d1: Date = sdf.parse(viewModel.dateS)
        val d2: Date = sdf.parse(viewModel.dateE)
        val period = calculateDuration(d1, d2)
        viewModel.duration.value = period
    }

    private fun calculateDuration(startDate: Date, endDate: Date): String {
        var different = endDate.time - startDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli

        return "$elapsedDays jours,  $elapsedHours heures et $elapsedMinutes minutes"
    }


    private fun parseStrDateInterventionInDatePicker(
        strDate: String,
        datePicker: MaterialCalendarView
    ) {
        try {
            datePicker.clearSelection()
            val date = strDate.substring(0, 10) // 20/04/2020
            val items = date.split(" ")
            val selectedDate =
                CalendarDay.from(items[2].toInt(), items[1].toInt(), items[0].toInt())
            datePicker.selectedDate = selectedDate
        } catch (ex: Exception) {

        }
    }

    private fun parseHourInHourPicker(
        strDate: String,
        hoursPicker: NumberPicker,
        valuesHours: Array<String>
    ) {
        try {
            val hours = strDate.substring(15, 17)
            hoursPicker.value = valuesHours.indexOf(hours)
        } catch (ex: Exception) {
        }

    }

    private fun parseMinutesInMinutesPicker(
        strDate: String,
        minutesPicker: NumberPicker,
        valuesMinutes: Array<String>
    ) {
        try {
            val minutes = strDate.substring(18, 20) // 15
            minutesPicker.value = valuesMinutes.indexOf(minutes)
        } catch (ex: Exception) {
        }
    }

    private fun initLoadingDialog() {
        loaderDialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null)
        loaderDialogBuilder.setView(dialogView)
        loadingDialog = loaderDialogBuilder.create()
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setCancelable(false)

        if (viewModel.loadingDialogIsShown.value == true) {
            GlobalScope.launch(Dispatchers.Main)
            {
                try {
                    blureView()
                    loadingDialog.show()
                    viewModel.loadingDialogIsShown.value= true
                    delay(3000)
                    loadingDialog.dismiss()
                    viewModel.loadingDialogIsShown.value= false
                    successDialog.show()
                    viewModel.successDialogIsShown.value= true
                    blurView.setupWith(rootView)
                        .setBlurEnabled(false)
                } catch (ex: Exception) { }
            }
        } else {
            loadingDialog.dismiss()
        }

    }


    private fun initSuccessAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_success_validate_absence, null)
        val btnOk = dialogView.findViewById<AppCompatButton>(R.id.btnOk)
        dialogBuilder.setView(dialogView)
        successDialog = dialogBuilder.create()
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        successDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnOk.setOnClickListener {
            successDialog.dismiss()
            if (activity is MainActivity) {
                // (activity as MainActivity).changeFragment(HomeFragment())
            } else {
                // (activity as MainTabletteActivity).changeFragment(AccueilTabletteFragment())
                // (activity as MainTabletteActivity).setMenuItemActive(0)
            }
        }

        //// show alertDialogStart when changing orientation
        if (viewModel.successDialogIsShown.value == true) {
            successDialog.show()
        } else {
            successDialog.dismiss()
        }
    }

    private fun blureView() {
        val radius = 2f
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(context))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(false)
    }


    private fun decorateValidateButton() {
        val isFormCompleted = viewModel.typeAbsence.value!!.isNotEmpty()
                && viewModel.dateS.isNotEmpty() &&
                viewModel.dateE.isNotEmpty()
        if (isFormCompleted) {
            btnValidate.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_widget_blue)
        }
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame_demande_absence.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame_demande_absence.layoutParams = layoutParams
    }


}
