package fr.strada.smobile.ui.indemnites.hebdomadaire


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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shawnlin.numberpicker.NumberPicker
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.hebdo.JourFrai
import fr.strada.smobile.data.models.indemnite.hebdo.SummaryFrai
import fr.strada.smobile.databinding.FragmentIndemniteHebdomadaireBinding
import fr.strada.smobile.di.indemnites.IndemnityComponent
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_WEEK
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_YEAR
import fr.strada.smobile.ui.activities.Utils.getDisplayedValuesFromWeeksOfYear
import fr.strada.smobile.ui.activities.Utils.getWeeksOfYear
import fr.strada.smobile.ui.activities.hebdomadaire.Utils.getDayOfWeek
import fr.strada.smobile.ui.activities.hebdomadaire.Utils.getWeekOfMonth
import fr.strada.smobile.ui.activities.mensuel.Utils.getWeeksNumbersInMonth
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.indemnites.IndemnitesFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.listner.DialogChangeYearListner
import fr.strada.smobile.utils.listner.ItemJourListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_indemnite_hebdomadaire.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * A simple [Fragment] subclass.
 */
class IndemniteHebdomadaireFragment : BaseFragment(), CollapsibleCalendar.CalendarListener,
    DialogChangeYearListner, ItemJourListner {

    lateinit var component: IndemnityComponent
    lateinit var viewModel: IndemniteHebdomadaireViewModel
    lateinit var binding: FragmentIndemniteHebdomadaireBinding

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
    private lateinit var jourIndemniteHebdomadaireAdapter: JourIndemniteHebdomadaireAdapter
    private lateinit var summaryFraiAdapter: GridIndemniteHebdoAdapter

    // UI
    lateinit var calendarView: CollapsibleCalendar
    private var listJourFrai: ArrayList<JourFrai> = arrayListOf()
    private var listSummaryFrai: ArrayList<SummaryFrai> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_indemnite_hebdomadaire,
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
                initAdapterGrid()
                attachAdapterToRecycleView()
                attachAdapterGridToRecycleView()
                showDialogsIfIsShown()
                setupCalendarWithCurrentWeek()
                observeActivitiesHebdomadaire()
                if (savedInstanceState == null) {
                    setupCalendarWithCurrentWeek()
                    observeActivitiesHebdomadaire()
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
        jourIndemniteHebdomadaireAdapter =
            JourIndemniteHebdomadaireAdapter(requireContext(), listJourFrai, this)
    }

    private fun attachAdapterToRecycleView() {
        recycleView.adapter = jourIndemniteHebdomadaireAdapter
    }

    private fun initAdapterGrid() {
        summaryFraiAdapter = GridIndemniteHebdoAdapter(listSummaryFrai)
    }

    private fun attachAdapterGridToRecycleView() {
        gridrecyclerhebdo.adapter = summaryFraiAdapter
    }

    private fun showDialogsIfIsShown() {
        if (viewModel.isDialogChangeYearShown) {
            dialog.show()
        }
        if (viewModel.isDialogChangeWeekShown) {
            dialogChangeWeek.show()
        }
    }

    private fun setupCalendarWithCurrentWeek() {
        val weekOfMonth = getWeekOfMonth(
            viewModel.year.value!!,
            viewModel.month.value!!,
            viewModel.week.value!!,
            locale,
            Calendar.MONDAY
        )
        calendarView.changeToWeek(
            viewModel.year.value!!,
            viewModel.month.value!!,
            weekOfMonth,
            locale
        )
        setCalendarViewListner()
        dismissLoading()
    }

    private fun setCalendarViewListner() {
        calendarView.setCalendarListener(this)
    }

    private fun observeActivitiesHebdomadaire() {
        viewModel.indemnitehebdo.observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    listJourFrai.clear()
                    listSummaryFrai.clear()
                    // viewModel.totalCumulHebdomadaire.value = it.data!!.cumulHebdoFrais
                    var somme: Int = 0
                    if (!it.data!!.jourFrais.isNullOrEmpty()) {
                        listJourFrai.addAll(it.data.jourFrais)
                        listJourFrai.forEach {
                            somme += it.jourMontantFrais
                        }
                        viewModel.totalCumulHebdomadaire.value = somme
                        jourIndemniteHebdomadaireAdapter.notifyDataSetChanged()
                    }
                    if (!it.data.summaryFrais.isNullOrEmpty()) {
                        listSummaryFrai.addAll(it.data.summaryFrais)
                        summaryFraiAdapter.notifyDataSetChanged()
                    }
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //------------------------Custom Navigation------------------------------//

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(null)
                .setDialogChangeWeekListner(dialogChangeWeekListner)
                .setDialogChangeYearListner(this)
                .build()
        } else {
            component = (activity as MainTabletteActivity).component.indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(null)
                .setDialogChangeWeekListner(dialogChangeWeekListner)
                .setDialogChangeYearListner(this)
                .build()
        }
    }

    override fun injectDependencies() {
        component.injectIndemnitesHebdomadaireFragment(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(IndemniteHebdomadaireViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        nextweek_btn.setOnClickListener {
            calendarView.nextWeek()
        }
        prevweek_btn.setOnClickListener {
            calendarView.prevWeek()
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
        val selectedDay = calendarView.selectedDay
        (parentFragment as IndemnitesFragment).startJournalierFragment(
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
        viewModel.month.value = calendarView.month
        viewModel.year.value = calendarView.year
        (parentFragment as IndemnitesFragment).setYearMonthDay(
            calendarView.year,
            calendarView.month, 1
        )
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
        val weeksOfYearMonth = fr.strada.smobile.ui.activities.Utils.getWeeksOfYearMonth(
            year,
            month,
            Calendar.MONDAY,
            locale
        )
        val startDate = fr.strada.smobile.ui.activities.Utils.getDate(
            weeksOfYearMonth.startDates[position].year,
            weeksOfYearMonth.startDates[position].month,
            weeksOfYearMonth.startDates[position].day,
            locale
        )
        val endDate = fr.strada.smobile.ui.activities.Utils.getDate(
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
        viewModel.getIndemniteHebdo(strStartDate, strEndDate)

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
        (parentFragment as IndemnitesFragment).startJournalierFragment(
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
            calendarView.changeToWeek(
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
        (parentFragment as IndemnitesFragment).dismissLoading()
    }

    private fun showLoading() {
        (parentFragment as IndemnitesFragment).showLoading()
    }
}
