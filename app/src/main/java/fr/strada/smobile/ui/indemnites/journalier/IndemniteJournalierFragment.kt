package fr.strada.smobile.ui.indemnites.journalier

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.journalier.FraisRow
import fr.strada.smobile.databinding.FragmentIndemniteJournalierBinding
import fr.strada.smobile.di.indemnites.IndemnityComponent
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_MONTH
import fr.strada.smobile.ui.activities.journalier.getStrDate
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.indemnites.IndemnitesFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.DatePicker
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_indemnite_journalier.*
import kotlinx.android.synthetic.main.fragment_my_daily_activities.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named


/**
 * A simple [Fragment] subclass.
 */
class IndemniteJournalierFragment : BaseFragment(), DialogChangeMonthListner,
    CollapsibleCalendar.CalendarListener {

    lateinit var component: IndemnityComponent
    lateinit var viewModel: IndemniteJournalierViewModel
    lateinit var binding: FragmentIndemniteJournalierBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Named(DIALOG_CHANGE_MONTH)
    @Inject
    internal lateinit var dialogChangeMonth: Dialog

    private val selectDateReceiver = SelectDateReceiver()

    // UI
    lateinit var calendarView: CollapsibleCalendar

    private lateinit var adapterIndemnite: IndemniteJournalierAdapter

    private var list: ArrayList<FraisRow> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_indemnite_journalier,
            container,
            false
        )
        binding.lifecycleOwner = this
        adapterIndemnite = IndemniteJournalierAdapter(list)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerSelectDateReceiver()
        Handler(Looper.getMainLooper()).post {
            try {
                inflateCalendar(view)
                initRecyclerView()
                initCalendarWithCurrentYearMonth()
                dismissLoading()
                attachListnerToCalendar()
                showDialogMonthIfIsShown()
                observesIndemnite()
                if (savedInstanceState == null) {
                    val strDate = getStrDate(
                        viewModel.year.value!!,
                        viewModel.month.value!! + 1,
                        viewModel.day.value!!,
                        locale
                    )
                    viewModel.getIndemniterJournalier(strDate)
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
        recycler_indemnite_jour.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterIndemnite
        }
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

    private fun observesIndemnite() {
        viewModel.indemniteJournalier.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.e("test")
                    dismissLoading()
                    list.clear()
                    list.addAll(it.data!!.fraisRows)
                    txt_cumule_journalier.text = it.data.cumulJournalierFrais.toString()
                    adapterIndemnite.notifyDataSetChanged()

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
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(this)
                .setDialogChangeWeekListner(null)
                .setDialogChangeYearListner(null)
                .build()
        } else {
            component = (activity as MainTabletteActivity).component.indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(this)
                .setDialogChangeWeekListner(null)
                .setDialogChangeYearListner(null)
                .build()
        }
    }

    override fun injectDependencies() {
        component.injectIndemnitesJournalier(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(IndemniteJournalierViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        view_month_indemnite.setOnClickListener {
            dialogChangeMonth.show()
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
        (parentFragment as IndemnitesFragment).setYearMonthDay(
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
        viewModel.getIndemniterJournalier(strDate)
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
        (parentFragment as IndemnitesFragment).setYearMonthDay(
            viewModel.year.value!!,
            viewModel.month.value!!,
            viewModel.day.value!!
        )

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
        (parentFragment as IndemnitesFragment).setYearMonthDay(
            viewModel.selectedYear.value!!,
            viewModel.selectedMonth.value!!,
            viewModel.selectedDay.value!!
        )

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
        (parentFragment as IndemnitesFragment).showLoading()
    }

    private fun dismissLoading() {
        (parentFragment as IndemnitesFragment).dismissLoading()
    }
}
