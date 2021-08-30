package fr.strada.smobile.ui.activities.journalier


import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.data.Event
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.activites.day.Activite
import fr.strada.smobile.databinding.FragmentMyDailyActivitiesBinding
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_MONTH
import fr.strada.smobile.di.mes_activities.MesActivitiesComponent
import fr.strada.smobile.ui.activities.MesActivitiesFragment
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.pointeuse.millisToTime
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.DatePicker
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_my_daily_activities.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class MyDailyActivitiesFragment : BaseFragment(), DialogChangeMonthListner,
    CollapsibleCalendar.CalendarListener {

    lateinit var component: MesActivitiesComponent
    lateinit var viewModel: MyDailyActivitiesViewModel
    lateinit var binding: FragmentMyDailyActivitiesBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Named(DIALOG_CHANGE_MONTH)
    @Inject
    internal lateinit var dialogChangeMonth: Dialog

    private val selectDateReceiver = SelectDateReceiver()

    // UI
    lateinit var calendarView: CollapsibleCalendar

    private lateinit var adapter: TimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_daily_activities,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerSelectDateReceiver()
        Handler(Looper.getMainLooper()).post {
            try {
                inflateCalendar(view)
                initCalendarWithCurrentYearMonth()
                dismissLoading()
                attachListnerToCalendar()
                initRecyclerView()
                showDialogMonthIfIsShown()
                observeActivities()
                observeDailyActivitiesMensuel()
                if (savedInstanceState == null) {
                    val strDate = getStrDate(
                        viewModel.year.value!!,
                        viewModel.month.value!! + 1,
                        viewModel.day.value!!,
                        locale
                    )
                    observeActivities()
                    observeDailyActivitiesMensuel()
                    viewModel.getActivitesJournaliere(strDate)
                    viewModel.getDailyActivitiesMensuel()
                }
            } catch (ex: Exception) {
                dismissLoading()
            }
        }
    }

    private fun registerSelectDateReceiver() {
        val filter = IntentFilter("SelectDateReceiver")
        requireActivity().registerReceiver(selectDateReceiver, filter)
    }

    private fun inflateCalendar(view: View) {
        val stub = view.findViewById(R.id.stub) as ViewStub
        val inflated = stub.inflate()
        calendarView = inflated.findViewById(R.id.calendarView)
    }

    private fun initRecyclerView() {
        recycleView_date.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        adapter = TimeAdapter(context)
        recycleView_date.adapter = adapter
    }

    private fun initCalendarWithCurrentYearMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(viewModel.year.value!!, viewModel.month.value!!, 1)
        val adapter = CalendarAdapter(requireContext(), calendar)
        calendarView.setAdapter(adapter)
        viewModel.selectedDay.value?.let {
            calendarView.select(
                Day(
                    viewModel.selectedYear.value!!,
                    viewModel.selectedMonth.value!!,
                    viewModel.selectedDay.value!!
                )
            )
        }
    }

    private fun attachListnerToCalendar() {
        calendarView.setCalendarListener(this)
    }

    private fun showDialogMonthIfIsShown() {
        if (viewModel.isDialogChangeMonthShown) {
            dialogChangeMonth.show()
        }
    }

    private fun observeActivities() {
        viewModel.activitesJournaliere.observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    val list = ArrayList<Activite>()
                    list.addAll(it.data!!.activitesConduite)
                    list.addAll(it.data.activitesPointeuse)
                    recycleView_date.invalidate()
                    adapter.setData(list)
                    lblService.text = millisToTime(list.map {
                        if (it.duree != null) {
                            it.duree.totalMilliseconds
                        } else {
                            0L
                        }
                    }.sum())
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
                    calendarView.setListIndicatorTag(indicators)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    ///---------------------- Custom Navigation -----------------------///

    override fun initArguments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val isExistArg = arguments != null
            if (isExistArg) {
                viewModel.year.value = requireArguments().getInt("year")
                viewModel.month.value = requireArguments().getInt("month")
                viewModel.day.value = requireArguments().getInt("day")
                viewModel.selectedYear.value = requireArguments().getInt("year")
                viewModel.selectedMonth.value = requireArguments().getInt("month")
                viewModel.selectedDay.value = requireArguments().getInt("day")
            } else {
                val c = Calendar.getInstance()
                viewModel.year.value = c.get(Calendar.YEAR)
                viewModel.month.value = c.get(Calendar.MONTH)
                viewModel.day.value = c.get(Calendar.DAY_OF_MONTH)
                viewModel.selectedYear.value = c.get(Calendar.YEAR)
                viewModel.selectedMonth.value = c.get(Calendar.MONTH)
                viewModel.selectedDay.value = c.get(Calendar.DAY_OF_MONTH)
            }
        }
    }

    override fun initComponent() {
        when (activity) {
            is MainActivity -> {
                component = (activity as MainActivity).component.mesActivitiesComponent()
                    .setContext(requireContext())
                    .setDialogChangeMonthListner(this)
                    .setDialogChangeWeekListner(null)
                    .setDialogChangeYearListner(null)
                    .build()
            }
            is MainTabletteActivity -> {
                component = (activity as MainTabletteActivity).component.mesActivitiesComponent()
                    .setContext(requireContext())
                    .setDialogChangeMonthListner(this)
                    .setDialogChangeWeekListner(null)
                    .setDialogChangeYearListner(null)
                    .build()
            }
            else -> {
                component = SmobileApp.instance!!.appComponent.mainComponent().setContext(requireActivity())
                    .setOnClickListener(null).build()
                    .mesActivitiesComponent()
                    .setContext(requireContext())
                    .setDialogChangeMonthListner(this)
                    .setDialogChangeWeekListner(null)
                    .setDialogChangeYearListner(null)
                    .build()
            }
        }
    }

    override fun injectDependencies() {
        component.injectMyDailyActivitiesFragment(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(MyDailyActivitiesViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        view_month.setOnClickListener {
            dialogChangeMonth.show()
        }
        btn_graph.setOnClickListener {
            val intent = Intent(activity, GraphicalViewActivity::class.java)
            val cal = Calendar.getInstance()
            var year = calendarView.year
            var month = calendarView.month
            var day = 1
            if (calendarView.selectedDay != null) {
                year = viewModel.selectedYear.value!!
                month = viewModel.selectedMonth.value!!
                day = viewModel.selectedDay.value!!
            }
            intent.putExtra("year", year)
            intent.putExtra("month", month)
            intent.putExtra("day", day)
            startActivity(intent)
        }
    }
    //----------------------- dialog change month listenr ------------------------//

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
        viewModel.day.value = 1
        calendarView.prevMonth()
        dialog.dismiss()
    }

    override fun onClickCancelListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onDismissListner() {
        viewModel.isDialogChangeMonthShown = false
    }

    ///--------------------------------- calendar listner----------------------------------------------//

    override fun onDaySelect() {
        val selectedDay = calendarView.selectedDay
        viewModel.selectedYear.value = selectedDay!!.year
        viewModel.selectedMonth.value = selectedDay.month
        viewModel.selectedDay.value = selectedDay.day
        (parentFragment as MesActivitiesFragment).setYearMonthDay(
            viewModel.selectedYear.value!!,
            viewModel.selectedMonth.value!!,
            viewModel.selectedDay.value!!
        )
        val strDate = getStrDate(
            viewModel.selectedYear.value!!,
            viewModel.selectedMonth.value!! + 1,
            viewModel.selectedDay.value!!,
            locale
        )
        viewModel.getActivitesJournaliere(strDate)
    }

    override fun onItemClick(v: View) {

    }

    override fun onDataUpdate() {

    }

    override fun onMonthChange() {
        viewModel.year.value = calendarView.year
        viewModel.month.value = calendarView.month
        viewModel.day.value = if (calendarView.selectedDay != null) {
            if (calendarView.month != calendarView.selectedDay!!.month) {
                1
            } else {
                calendarView.selectedDay!!.day
            }
        } else {
            1
        }
        (parentFragment as MesActivitiesFragment).setYearMonthDay(
            viewModel.year.value!!,
            viewModel.month.value!!,
            viewModel.day.value!!
        )
        viewModel.getDailyActivitiesMensuel()
    }

    override fun onWeekChange(position: Int, isNext: Boolean) {

    }

    override fun onClickListener() {

    }

    override fun onDayChanged() {

    }
    //------------------------------------//

    inner class SelectDateReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val day = p1?.getIntExtra("day", 1)
            val month = p1?.getIntExtra("month", 0)
            val year = p1?.getIntExtra("year", 0)
            selectDay(year!!, month!!, day!!)
        }
    }

    fun selectDay(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        val adapter = CalendarAdapter(requireContext(), calendar)
        calendarView.setAdapter(adapter)
        viewModel.selectedYear.value = year
        viewModel.selectedMonth.value = month
        viewModel.selectedDay.value = day
        viewModel.year.value = year
        viewModel.month.value = month
        viewModel.day.value = day
        calendarView.select(Day(year, month, day))
        (parentFragment as MesActivitiesFragment).setYearMonthDay(
            viewModel.selectedYear.value!!,
            viewModel.selectedMonth.value!!,
            viewModel.selectedDay.value!!
        )
        viewModel.getDailyActivitiesMensuel()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            requireActivity().unregisterReceiver(selectDateReceiver)
        } catch (ex: Exception) {
        }
    }

    //---------------------lodding manager------------------//

    private fun showLoading() {
        (parentFragment as MesActivitiesFragment).showLoading()
    }

    private fun dismissLoading() {
        (parentFragment as MesActivitiesFragment).dismissLoading()
    }
}
