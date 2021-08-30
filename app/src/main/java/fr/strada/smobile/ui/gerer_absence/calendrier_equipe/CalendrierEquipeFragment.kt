package fr.strada.smobile.ui.gerer_absence.calendrier_equipe

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentCalendrierEquipeBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier.CalendrierEquipeComponent
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.TabFragmentAdapter
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence.DemandeAbsenceFragment
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent.SalarieAbsentFragment
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent.SalariePresentFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_calendrier_equipe.*
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class CalendrierEquipeFragment : BaseFragment() {

    private lateinit var component: CalendrierEquipeComponent
    private lateinit var viewModel: CalendrierEquipeViewModel
    private lateinit var binding: FragmentCalendrierEquipeBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var tabFragmentAdapter: TabFragmentAdapter

    companion object {
        var cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_calendrier_equipe, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initComponent() {
        component = if (activity is MainActivity) {
            (activity as MainActivity).component
                .calendrierEquipeComponent()
                .setContext(requireContext())
                .build()
        } else {
            (activity as MainTabletteActivity).component
                .calendrierEquipeComponent()
                .setContext(requireContext())
                .build()
        }
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(CalendrierEquipeViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnRetourEvent.observe(viewLifecycleOwner, {
            (activity as MainActivity).onBackPressed()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalendar()
        configCalendar(calendarView, txt_month_calendar, txt_year_calendar, savedInstanceState)
        changeDate(savedInstanceState)
        initTabFragmentAdapter(savedInstanceState)
        setupViewPager()
        setAdapterInViewPager()
        associateViewPagerWithTabLayout()
        setupNavigation()
        Handler().postDelayed({
            makeTag()
            makeEventTag()
        }, 10)
    }

    private fun initTabFragmentAdapter(savedInstanceState: Bundle?) {
        tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        tabFragmentAdapter.addFragment(
            DemandeAbsenceFragment(),
            resources.getString(R.string.demandes_d_absence) + " " + "(0)"
        )
        tabFragmentAdapter.addFragment(
            SalariePresentFragment(),
            resources.getString(R.string.salari_s_pr_sents) + " " + "(0)"
        )
        tabFragmentAdapter.addFragment(
            SalarieAbsentFragment(),
            resources.getString(R.string.salari_s_absents) + " " + "(0)"
        )
    }

    private fun associateViewPagerWithTabLayout() {
        tabLayoutCalendar.setupWithViewPager(viewPagerCalendar)
        //
        val orientation = resources.configuration.orientation
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
           tabFragmentAdapter.fragmentList.removeAt(0)
           tabFragmentAdapter.fragmentTitleList.removeAt(0)
           tabFragmentAdapter.notifyChangeInPosition(1)
           tabFragmentAdapter.notifyDataSetChanged()
        }
    }

    private fun setAdapterInViewPager() {
        viewPagerCalendar.adapter = tabFragmentAdapter
    }

    private fun setupViewPager() {
        viewPagerCalendar.offscreenPageLimit = 3
    }

    private fun initCalendar() {
        txt_month_calendar.text = getMonthName(requireContext(), month)
        txt_year_calendar.text = year.toString()
        calendarView.month = month
        calendarView.year = year
        val metrics = requireContext().resources.displayMetrics
        val dp = 50f
        val fpixels = metrics.density * dp
        val pixels = (fpixels + 0.5f).toInt()
        calendarView.cellHeight = pixels
        calendarView.cellWidth = 100
        cal.set(year, month, 1)
        calendarView.setAdapter(CalendarAdapter(requireContext(), cal))
    }


    private fun makeTag() {
        calendarView?.addEmployeePresenceTag(2020, month, 11, 0, 4, 0)
        calendarView?.addEmployeePresenceTag(2020, month, 24, 2, 4, 2)
        calendarView?.addEmployeePresenceTag(2020, month, 8, 0, 4, 2)
    }

    private fun makeEventTag() {
        calendarView?.addEventTag(2020, month, 11, R.drawable.bg_tag_calendar)
        calendarView?.addEventTag(2020, month, 24, R.drawable.bg_tag_calendar)
        calendarView?.addEventTag(2020, month, 8, R.drawable.bg_tag_calendar)
    }


    private fun configCalendar(
        calendarView: CollapsibleCalendar,
        textMonth: TextView,
        textYear: TextView,
        savedInstanceState: Bundle?
    ) {
        calendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDaySelect() {
            }

            override fun onItemClick(v: View) {
                sendInformationToFragmentInTabLayout(savedInstanceState)
            }

            override fun onDataUpdate() {
                val month = calendarView.month
                val year = calendarView.year
                textMonth.text = getMonthName(requireContext(), month)
                textYear.text = year.toString()
            }

            override fun onMonthChange() {
                val month = calendarView.month
                val year = calendarView.year
                textMonth.text = getMonthName(requireContext(), month)
                textYear.text = year.toString()
                sendInformationToFragmentInTabLayout(savedInstanceState)
            }

            override fun onWeekChange(position: Int, isNext: Boolean) {}
            override fun onClickListener() {}
            override fun onDayChanged() {}
        })
    }

    private fun changeDate(savedInstanceState: Bundle?) {
        view_month_calendar.setOnClickListener {

            var year: Int = calendarView.year
            var month: Int = calendarView.month

            val datePickerView = Dialog(requireActivity())
            datePickerView.requestWindowFeature(Window.FEATURE_NO_TITLE)
            datePickerView.setCancelable(false)
            datePickerView.setContentView(R.layout.dialog_change_month)
            datePickerView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val date = datePickerView.findViewById<TextView>(R.id.txt_date)
            date.text = getMonthName(requireContext(), month) + " " + year

            val picker =
                datePickerView.findViewById(R.id.date_picker) as fr.strada.smobile.utils.DatePicker
            picker.setDate(year, month)
            picker.setOnDateClickedListener { _, i, i2, i3 ->
                year = i
                month = i2
            }
            val btnValidate = datePickerView.findViewById<AppCompatButton>(R.id.btnDone)
            val btnCancel = datePickerView.findViewById<AppCompatButton>(R.id.btnCancel)

            btnValidate.setOnClickListener {
                calendarView.year = picker.year
                calendarView.month = picker.month
                calendarView.prevMonth()
                date.text = getMonthName(requireContext(), month - 1)
                txt_year_calendar.text = year.toString()
                datePickerView.dismiss()

                sendInformationToFragmentInTabLayout(savedInstanceState)
            }
            btnCancel.setOnClickListener {
                datePickerView.dismiss()
            }
            datePickerView.show()
        }
    }


    private fun sendFinishAbsenceRequestReceiver(context: Context) {
        val intent = Intent("FinishAbsenceRequestReceiver")
        intent.putExtra("requestAbsence", "isRequestVisible")
        context.sendBroadcast(intent)
    }

    private fun sendStopAbsenceRequestReceiver(context: Context) {
        val intent = Intent("FinishAbsenceRequestReceiver")
        intent.putExtra("requestAbsence", "isRequestGone")
        context.sendBroadcast(intent)
    }

    private fun sendFinishEmployeeAbsentReceiver(context: Context) {
        val intent = Intent("FinishEmployeeAbsentReceiver")
        intent.putExtra("employeeAbsent", "isEmployeeAbsentVisible")
        context.sendBroadcast(intent)
    }

    private fun sendStopEmployeeAbsentReceiver(context: Context) {
        val intent = Intent("FinishEmployeeAbsentReceiver")
        intent.putExtra("employeeAbsent", "isEmployeeAbsentGone")
        context.sendBroadcast(intent)
    }

    private fun sendFinishEmployeePresentReceiver(context: Context) {
        val intent = Intent("FinishEmployeePresentReceiver")
        intent.putExtra("employeePresent", "isEmployeePresentVisible")
        context.sendBroadcast(intent)
    }

    private fun sendStopEmployeePresentReceiver(context: Context) {
        val intent = Intent("FinishEmployeePresentReceiver")
        intent.putExtra("employeePresent", "isEmployeePresentGone")
        context.sendBroadcast(intent)
    }

    private fun sendInformationToFragmentInTabLayout(savedInstanceState: Bundle?) {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (txt_month_calendar?.text == requireContext().resources.getString(R.string.août) && txt_year_calendar.text == "2020" && (calendarView.selectedItem?.day == 8
                        || calendarView.selectedItem?.day == 11 || calendarView.selectedItem?.day == 24)
            ) {
                context?.let {
                    sendFinishEmployeePresentReceiver(it)
                    tabLayoutCalendar.getTabAt(1)?.text =
                        "${requireContext().resources.getString(R.string.salari_s_pr_sents)} (4)"
                    if (txt_month_calendar?.text == requireContext().resources.getString(R.string.août) && txt_year_calendar.text == "2020" && (calendarView.selectedItem?.day == 8
                                || calendarView.selectedItem?.day == 24)
                    ) {
                        sendFinishAbsenceRequestReceiver(requireContext())
                        tabLayoutCalendar.getTabAt(0)?.text =
                            "${requireContext().resources.getString(R.string.demande_d_absence)} (2)"
                        if (txt_month_calendar?.text == requireContext().resources.getString(R.string.août) && txt_year_calendar.text == "2020" && calendarView.selectedItem?.day == 24) {
                            sendFinishEmployeeAbsentReceiver(requireContext())
                            tabLayoutCalendar.getTabAt(2)?.text =
                                "${requireContext().resources.getString(R.string.salari_s_absents)} (4)"
                        } else {
                            sendFinishEmployeeAbsentReceiver(requireContext())
                            tabLayoutCalendar.getTabAt(2)?.text =
                                "${requireContext().resources.getString(R.string.salari_s_absents)} (4)"
                            sendStopAbsenceRequestReceiver(requireContext())
                            tabLayoutCalendar.getTabAt(0)?.text =
                                "${requireContext().resources.getString(R.string.demande_d_absence)} (0)"
                        }
                    } else {
                        sendStopAbsenceRequestReceiver(requireContext())
                        tabLayoutCalendar.getTabAt(0)?.text =
                            "${requireContext().resources.getString(R.string.demande_d_absence)} (0)"
                        sendStopEmployeeAbsentReceiver(requireContext())
                        tabLayoutCalendar.getTabAt(2)?.text =
                            "${requireContext().resources.getString(R.string.salari_s_absents)} (0)"
                    }
                }
            } else {
                context?.let {
                    sendStopEmployeePresentReceiver(it)
                    tabLayoutCalendar.getTabAt(1)?.text =
                        "${requireContext().resources.getString(R.string.salari_s_pr_sents)} (0)"
                    sendStopAbsenceRequestReceiver(it)
                    tabLayoutCalendar.getTabAt(0)?.text =
                        "${requireContext().resources.getString(R.string.demande_d_absence)} (0)"
                    sendStopEmployeeAbsentReceiver(it)
                    tabLayoutCalendar.getTabAt(2)?.text =
                        "${requireContext().resources.getString(R.string.salari_s_absents)} (0)"
                }
            }

        } else {
            if (txt_month_calendar?.text == requireContext().resources.getString(R.string.août) && txt_year_calendar.text == "2020" && (calendarView.selectedItem?.day == 8
                        || calendarView.selectedItem?.day == 11 || calendarView.selectedItem?.day == 24)
            ) {
                context?.let {
                    sendFinishEmployeePresentReceiver(it)
                    tabLayoutCalendar.getTabAt(0)?.text =
                        "${requireContext().resources.getString(R.string.salari_s_pr_sents)} (4)"
                    if (txt_month_calendar?.text == requireContext().resources.getString(R.string.août) && txt_year_calendar.text == "2020" && (calendarView.selectedItem?.day == 8
                                || calendarView.selectedItem?.day == 24)
                    ) {

                        if (txt_month_calendar?.text == requireContext().resources.getString(R.string.août) && txt_year_calendar.text == "2020" && calendarView.selectedItem?.day == 24) {
                            sendFinishEmployeeAbsentReceiver(requireContext())
                            tabLayoutCalendar.getTabAt(1)?.text =
                                "${requireContext().resources.getString(R.string.salari_s_absents)} (4)"
                        } else {
                            sendFinishEmployeeAbsentReceiver(requireContext())
                            tabLayoutCalendar.getTabAt(1)?.text =
                                "${requireContext().resources.getString(R.string.salari_s_absents)} (4)"
                        }
                    } else {
                        sendStopEmployeeAbsentReceiver(requireContext())
                        tabLayoutCalendar.getTabAt(1)?.text =
                            "${requireContext().resources.getString(R.string.salari_s_absents)} (0)"
                    }
                }
            } else {
                context?.let {
                    sendStopEmployeePresentReceiver(it)
                    tabLayoutCalendar.getTabAt(0)?.text =
                        "${requireContext().resources.getString(R.string.salari_s_pr_sents)} (0)"
                    sendStopEmployeeAbsentReceiver(it)
                    tabLayoutCalendar.getTabAt(1)?.text =
                        "${requireContext().resources.getString(R.string.salari_s_absents)} (0)"
                }
            }
        }
    }
}
