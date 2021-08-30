package fr.strada.smobile.ui.activities.mensuel


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter
import com.shrikanthravi.collapsiblecalendarview.data.Event
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentMesActivitiesMensuelBinding
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_MONTH
import fr.strada.smobile.di.mes_activities.MesActivitiesComponent
import fr.strada.smobile.ui.activities.MesActivitiesFragment
import fr.strada.smobile.ui.activities.mensuel.Utils.convertStrDateToEvent
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.activities.mensuel.Utils.getWeeksNumbersInMonth
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.pointeuse.millisToTime
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.DatePicker
import fr.strada.smobile.utils.KEY_MONTH
import fr.strada.smobile.utils.KEY_YEAR
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.listner.ItemSemaineListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_mes_activities_mensuel.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList


class MesActivitiesMensuelFragment : BaseFragment(), CollapsibleCalendar.CalendarListener,
    DialogChangeMonthListner, ItemSemaineListner {

    lateinit var component: MesActivitiesComponent
    lateinit var viewModel: MesActivitiesMensuelViewModel
    lateinit var binding: FragmentMesActivitiesMensuelBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    @Named(DIALOG_CHANGE_MONTH)
    @Inject
    internal lateinit var dialog: Dialog

    private lateinit var adapter: SemaineActivitiesMensuelAdapter

    // UI
    lateinit var calendarView: CollapsibleCalendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mes_activities_mensuel,
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
                initCalendarView()
                steupCalendarViewListner()
                dismissLoading()
                attachLayoutManagerToRecycleView()
                initAdapter()
                attachAdapterToRecycleView()
                observeActivitiesMensuel()
                observeDailyActivitiesMensuel()
                showDialogChangeMonthIfIsRecentlyVisible()
                if (savedInstanceState == null) {
                    viewModel.getActivitiesMensuel()
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

    private fun initCalendarView() {
        val calendar = Calendar.getInstance()
        calendar.set(viewModel.year.value!!, viewModel.month.value!!, 1)
        val adapter = CalendarAdapter(requireContext(), calendar)
        calendarView.setAdapter(adapter)
    }

    private fun steupCalendarViewListner() {
        calendarView.setCalendarListener(this)
    }

    private fun attachLayoutManagerToRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun initAdapter() {
        adapter = SemaineActivitiesMensuelAdapter(requireContext(), this)
    }

    private fun attachAdapterToRecycleView() {
        recycleView.adapter = adapter
    }

    private fun showDialogChangeMonthIfIsRecentlyVisible() {
        if (viewModel.isDialogChangeMonthShown) {
            dialog.show()
        }
    }

    private fun observeActivitiesMensuel() {
        viewModel.activitiesMensuel.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    val weeksNumbersInMonth = getWeeksNumbersInMonth(
                        viewModel.month.value!!,
                        viewModel.year.value!!,
                        locale,
                        Calendar.MONDAY
                    )
                    if (it.data!!.weeks.size <= weeksNumbersInMonth.size) {
                        it.data.weeks.forEachIndexed { index, week ->
                            week.weekNumber = "S${weeksNumbersInMonth[index]}"
                        }
                        adapter.setData(it.data.weeks)
                    }
                    lblTotalService.text = millisToTime(it.data.serviceCumul.totalMilliseconds)
                    lblTotalNuit.text = millisToTime(it.data.nuitCumul.totalMilliseconds)
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
                        val event = convertStrDateToEvent(it)
                        indicators.add(event)
                    }
                    calendarView.setListIndicatorTag(indicators)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    //--------------------------------- custom Navigation ------------------------------------//

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.mesActivitiesComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(this)
                .setDialogChangeWeekListner(null)
                .setDialogChangeYearListner(null)
                .build()
        } else {
            component = (activity as MainTabletteActivity).component.mesActivitiesComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(this)
                .setDialogChangeWeekListner(null)
                .setDialogChangeYearListner(null)
                .build()
        }
    }

    override fun injectDependencies() {
        component.injectMesActivitiesMensuelFragment(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(MesActivitiesMensuelViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    /**
     * init year , month in viewModel
     * month begin with 0 ex: January = 0
     * @param savedInstanceState
     */
    override fun initArguments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val isMonthYearExist = arguments != null
            val calendar = Calendar.getInstance()
            if (isMonthYearExist) {
                viewModel.year.value = requireArguments().getInt(KEY_YEAR)
                viewModel.month.value = requireArguments().getInt(KEY_MONTH)
            } else {
                viewModel.year.value = calendar.get(Calendar.YEAR)
                viewModel.month.value = calendar.get(Calendar.MONTH)
            }
        }
    }

    override fun setupNavigation() {
        calendarHeader.setOnClickListener {
            dialog.show()
        }
    }
    //---------------------------------------- calendar Listner-----------------------------------//

    override fun onDaySelect() {
        val day = calendarView.selectedDay
        (parentFragment as MesActivitiesFragment).startJournalierFragment(
            day!!.year,
            day.month,
            day.day
        )
    }

    override fun onItemClick(v: View) {

    }

    override fun onDataUpdate() {

    }

    override fun onMonthChange() {
        (parentFragment as MesActivitiesFragment).setYearMonthDay(
            calendarView.year,
            calendarView.month,
            1
        )
        viewModel.month.value = calendarView.month
        viewModel.year.value = calendarView.year
        viewModel.getActivitiesMensuel()
        viewModel.getDailyActivitiesMensuel()
    }

    override fun onWeekChange(position: Int, isNext: Boolean) {

    }

    override fun onClickListener() {

    }

    override fun onDayChanged() {

    }

    //----------------------------------------dialog Listner-----------------------------------//

    override fun onShowListner(dialog: Dialog, txtDate: TextView, datePicker: DatePicker) {
        viewModel.isDialogChangeMonthShown = true
        val month = viewModel.month.value
        val year = viewModel.year.value
        val monthName = getMonthName(requireContext(), month!!)
        txtDate.text = "$monthName $year"
        datePicker.setDate(year!!, month)
    }

    override fun onClickValidateListner(dialog: Dialog, datePicker: DatePicker) {
        calendarView.year = datePicker.year
        calendarView.month = datePicker.month
        viewModel.year.value = datePicker.year
        viewModel.month.value = datePicker.month
        calendarView.prevMonth()
        dialog.dismiss()
    }

    override fun onClickCancelListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onDismissListner() {
        viewModel.isDialogChangeMonthShown = false
    }

    //----------------- ItemSemaineListner ----------------//

    override fun onClickListner(semaine: Int) {
        (parentFragment as MesActivitiesFragment).startHebdomadaireFragment(
            viewModel.year.value!!,
            viewModel.month.value!!,
            semaine
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    //-----------------Loding Manager----------------------//

    private fun dismissLoading() {
        (parentFragment as MesActivitiesFragment).dismissLoading()
    }

    private fun showLoading() {
        (parentFragment as MesActivitiesFragment).dismissLoading()
    }
}
