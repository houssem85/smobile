package fr.strada.smobile.ui.activities.hebdomadaire

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shawnlin.numberpicker.NumberPicker
import com.shrikanthravi.collapsiblecalendarview.data.Event
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentMesActivitiesHebdomadaireBinding
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_WEEK
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_YEAR
import fr.strada.smobile.di.mes_activities.MesActivitiesComponent
import fr.strada.smobile.ui.activities.MesActivitiesFragment
import fr.strada.smobile.ui.activities.Utils.getDate
import fr.strada.smobile.ui.activities.Utils.getDisplayedValuesFromWeeksOfYear
import fr.strada.smobile.ui.activities.Utils.getWeeksOfYear
import fr.strada.smobile.ui.activities.Utils.getWeeksOfYearMonth
import fr.strada.smobile.ui.activities.hebdomadaire.Utils.getDayOfWeek
import fr.strada.smobile.ui.activities.hebdomadaire.Utils.getWeekOfMonth
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.activities.mensuel.Utils.getWeeksNumbersInMonth
import fr.strada.smobile.ui.activities.mensuel.Utils.millisToTime
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.listner.DialogChangeYearListner
import fr.strada.smobile.utils.listner.ItemJourListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_mes_activities_hebdomadaire.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList


class MesActivitiesHebdomadaireFragment : BaseFragment(), CollapsibleCalendar.CalendarListener,
    DialogChangeYearListner, ItemJourListner {

    lateinit var component: MesActivitiesComponent
    lateinit var viewModel: MesActivitiesHebdomadaireViewModel
    lateinit var binding: FragmentMesActivitiesHebdomadaireBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Named(DIALOG_CHANGE_YEAR)
    @Inject
    internal lateinit var dialog: Dialog

    @Named(DIALOG_CHANGE_WEEK)
    @Inject
    internal lateinit var dialogChangeWeek: Dialog

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    private lateinit var adapter: JourActivitiesHebdomadaireAdapter

    // UI
    var calendarView: CollapsibleCalendar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mes_activities_hebdomadaire,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).post {
            try {
                inflateCalendarView(view)
                attachLayoutManagerToRecycleView()
                initAdapter()
                attachAdapterToRecycleView()
                showDialogsIfIsShown()
                setupCalendarWithCurrentWeek()
                observeActivitiesHebdomadaire()
                observeDailyActivitiesMensuel()
                if (savedInstanceState == null) {
                    viewModel.getDailyActivitiesMensuel()
                }
            } catch (ex: Exception) {
                dismissLoading()
            }
        }
    }

    private fun inflateCalendarView(view: View) {
        val stub = view.findViewById(R.id.stub) as ViewStub
        val inflated = stub.inflate()
        calendarView = inflated.findViewById(R.id.calendarView)
    }

    private fun attachLayoutManagerToRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun initAdapter() {
        adapter = JourActivitiesHebdomadaireAdapter(requireContext(), this)
    }

    private fun attachAdapterToRecycleView() {
        recycleView.adapter = adapter
    }

    private fun showDialogsIfIsShown() {
        if (viewModel.isDialogChangeYearShown) {
            dialog.show()
        }
        if (viewModel.isDialogChangeWeekShown) {
            dialogChangeWeek.show()
        }
    }

    // TODO: 4/28/21 add suspend if need
    private fun setupCalendarWithCurrentWeek() {
        val weekOfMonth = getWeekOfMonth(
            viewModel.year.value!!,
            viewModel.month.value!!,
            viewModel.week.value!!,
            locale,
            Calendar.MONDAY
        )
        calendarView?.changeToWeek(
            viewModel.year.value!!,
            viewModel.month.value!!,
            weekOfMonth,
            locale
        )
        setCalendarViewListner()
        dismissLoading()
    }

    private fun setCalendarViewListner() {
        calendarView?.setCalendarListener(this)
    }

    private fun observeActivitiesHebdomadaire() {
        viewModel.activitiesHebdomadaire.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    adapter.setData(it.data!!.days)
                    valueTempsNuit.text = millisToTime(it.data.nuitCumul.totalMilliseconds)
                    valueTempsService.text = millisToTime(it.data.serviceCumul.totalMilliseconds)
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeDailyActivitiesMensuel() {
        viewModel.dailyActivitiesMensuel.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val indicators = ArrayList<Event>()
                    it.data!!.forEach {
                        val event = Utils.convertStrDateToEvent(it)
                        indicators.add(event)
                    }
                    calendarView?.setListIndicatorTag(indicators)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    //------------------------Custom Navigation------------------------------//

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.mesActivitiesComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(null)
                .setDialogChangeWeekListner(dialogChangeWeekListner)
                .setDialogChangeYearListner(this)
                .build()
        } else {
            component = (activity as MainTabletteActivity).component.mesActivitiesComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(null)
                .setDialogChangeWeekListner(dialogChangeWeekListner)
                .setDialogChangeYearListner(this)
                .build()
        }
    }

    override fun injectDependencies() {
        component.injectMesActivitiesHebdomadaireFragment(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            providerFactory
        ).get(MesActivitiesHebdomadaireViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        BtnNextWeek.setOnClickListener {
            calendarView?.nextWeek()
        }
        btnPreviousWeek.setOnClickListener {
            calendarView?.prevWeek()
        }
        calendarHeader.setOnClickListener {
            dialog.show()
        }
    }

    override fun initArguments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.month.value = requireArguments().getInt("month")
            viewModel.year.value = requireArguments().getInt("year")
            viewModel.week.value = requireArguments().getInt("week")
        }
    }

    // ---------------------------- calendar listner ----------------------------- //

    override fun onDaySelect() {
        val selectedDay = calendarView?.selectedDay
        (parentFragment as MesActivitiesFragment).startJournalierFragment(
            selectedDay!!.year,
            selectedDay.month,
            selectedDay.day
        )
    }

    override fun onItemClick(v: View) {

    }

    override fun onDataUpdate() {

    }

    override fun onMonthChange() {
        viewModel.month.value = calendarView?.month
        viewModel.year.value = calendarView?.year
        (parentFragment as MesActivitiesFragment).setYearMonthDay(
            calendarView!!.year,
            calendarView!!.month,
            1
        )
        viewModel.getDailyActivitiesMensuel()
    }

    /**
     *
     * @param position
     * @param isNext
     */

    override fun onWeekChange(position: Int, isNext: Boolean) {
        val sf = SimpleDateFormat("yyyy-MM-dd", locale)
        val year = viewModel.year.value!!
        val month = viewModel.month.value!!
        val weeksOfYearMonth = getWeeksOfYearMonth(year, month, Calendar.MONDAY, locale)
        val startDate = getDate(
            weeksOfYearMonth.startDates[position].year,
            weeksOfYearMonth.startDates[position].month,
            weeksOfYearMonth.startDates[position].day,
            locale
        )
        val endDate = getDate(
            weeksOfYearMonth.endDates[position].year,
            weeksOfYearMonth.endDates[position].month,
            weeksOfYearMonth.endDates[position].day,
            locale
        )
        val strStartDate = sf.format(startDate)
        val strEndDate = sf.format(endDate)
        val weeks = getWeeksNumbersInMonth(
            viewModel.month.value!!,
            viewModel.year.value!!,
            locale,
            Calendar.MONDAY
        )
        viewModel.week.value = weeks[position]
        viewModel.getActivitiesHebdomadaire(
            viewModel.year.value!!,
            viewModel.month.value!! + 1,
            strStartDate,
            strEndDate
        )

    }

    override fun onClickListener() {

    }

    override fun onDayChanged() {

    }

    //--------------------DialogChangeYearListner--------------------//

    override fun onShowListner(dialog: Dialog, yearPicker: NumberPicker) {
        viewModel.isDialogChangeYearShown = true
        yearPicker.value = viewModel.year.value!!
    }

    override fun onClickTerminerListner(dialog: Dialog, yearPicker: NumberPicker) {
        dialog.dismiss()
        viewModel.yearDialogChangeWeek.value = yearPicker.value
        dialogChangeWeek.show()
    }

    override fun onClickAnnulerListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onDismissListner() {
        viewModel.isDialogChangeYearShown = false
    }


    // ------------------------------ItemJourListner------------------------------ //

    override fun onClickListner(jour: Int) {
        val day = getDayOfWeek(
            viewModel.year.value!!,
            viewModel.week.value!!,
            jour,
            Calendar.MONDAY,
            locale
        )
        (parentFragment as MesActivitiesFragment).startJournalierFragment(
            day.year,
            day.month,
            day.day
        )
    }

    //------------------------------DialogChangeWeekListner------------------------------------------//
    private val dialogChangeWeekListner = object : DialogChangeWeekListner {

        override fun onShowListner(dialog: Dialog, weekPicker: NumberPicker) {
            viewModel.isDialogChangeWeekShown = true
            val year = viewModel.yearDialogChangeWeek.value!!
            val month = viewModel.month.value!! + 1
            val weeksOfYear = getWeeksOfYear(year, Calendar.MONDAY, locale)
            weekPicker.value = 0
            weekPicker.minValue = 0
            weekPicker.maxValue = weeksOfYear.weeksCount - 1
            weekPicker.displayedValues =
                getDisplayedValuesFromWeeksOfYear(weeksOfYear, requireContext())
            for (i in 0 until weeksOfYear.weeksCount) {
                if (weeksOfYear.endDates[i].month == month && weeksOfYear.endDates[i].year == viewModel.year.value!!) {
                    weekPicker.value = i
                    break
                }
            }
        }

        override fun onClickTerminerListner(dialog: Dialog, weekPicker: NumberPicker) {
            val weeksOfYear =
                getWeeksOfYear(viewModel.yearDialogChangeWeek.value!!, Calendar.MONDAY, locale)
            viewModel.month.value = weeksOfYear.endDates[weekPicker.value].month - 1
            viewModel.year.value = viewModel.yearDialogChangeWeek.value!!
            viewModel.week.value = weeksOfYear.weeksNumbers[weekPicker.value]
            val weekOfMonth = getWeekOfMonth(
                viewModel.year.value!!,
                viewModel.month.value!!,
                viewModel.week.value!!,
                locale,
                Calendar.MONDAY
            )
            calendarView?.changeToWeek(
                viewModel.year.value!!,
                viewModel.month.value!!,
                weekOfMonth,
                locale
            )
            dialog.dismiss()
        }

        override fun onClickAnnulerListner(dialog: Dialog) {
            dialog.dismiss()
        }

        override fun onDismissListner(dialog: Dialog) {
            viewModel.isDialogChangeWeekShown = false
        }

    }

    //-----------------Loding Manager----------------------//

    private fun dismissLoading() {
        (parentFragment as MesActivitiesFragment).dismissLoading()
    }

    private fun showLoading() {
        (parentFragment as MesActivitiesFragment).showLoading()
    }
}
