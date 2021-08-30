package fr.strada.smobile.ui.indemnites.mensuel


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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.data.models.indemnite.mensuel.SemaineFrai
import fr.strada.smobile.databinding.FragmentIndemniteMensuelleBinding
import fr.strada.smobile.di.indemnites.IndemnityComponent
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_MONTH
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.indemnites.IndemnitesFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.DatePicker
import fr.strada.smobile.utils.KEY_MONTH
import fr.strada.smobile.utils.KEY_YEAR
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.listner.ItemSemaineListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_indemnite_mensuelle.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * A simple [Fragment] subclass.
 */
class IndemniteMensuelleFragment : BaseFragment(), CollapsibleCalendar.CalendarListener,
    DialogChangeMonthListner, ItemSemaineListner {

    lateinit var component: IndemnityComponent
    lateinit var viewModel: IndemniteMensuelleViewModel
    lateinit var binding: FragmentIndemniteMensuelleBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    @Named(DIALOG_CHANGE_MONTH)
    @Inject
    internal lateinit var dialog: Dialog

    private lateinit var semaineIndemniteMensuelleAdapter: SemaineIndemniteMensuelleAdapter
    private lateinit var summaryFraiAdapter: GridIndemniteMensuellAdapter
    private var listSemaineFrai: ArrayList<SemaineFrai> = arrayListOf()
    private var listSummaryFrai: ArrayList<fr.strada.smobile.data.models.indemnite.mensuel.SummaryFrai> =
        arrayListOf()

    // UI
    lateinit var calendarView: CollapsibleCalendar
    var isOriontationChanged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_indemnite_mensuelle,
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
                initAdapterGrid()
                attachAdapterToRecycleView()
                attachAdapterGridToRecycleView()
                observeActivitiesMensuel()
                showDialogChangeMonthIfIsRecentlyVisible()
                if (savedInstanceState == null) {
                    viewModel.month.value = calendarView.month
                    viewModel.year.value = calendarView.year
                    Timber.e("tttettettettetette")
                    val s: String =
                        Utils.getLastDayMonth(calendarView.year, calendarView.month, locale)
                    Timber.tag("datttttt").e(s)
                    viewModel.getIndemniteMensuell(
                        Utils.getFirstDayMonth(
                            calendarView.year,
                            calendarView.month,
                            locale
                        ), Utils.getLastDayMonth(calendarView.year, calendarView.month, locale)
                    )

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
        semaineIndemniteMensuelleAdapter =
            SemaineIndemniteMensuelleAdapter(requireContext(), listSemaineFrai, this)
    }

    private fun attachAdapterToRecycleView() {
        recycleView.adapter = semaineIndemniteMensuelleAdapter
    }

    private fun initAdapterGrid() {
        summaryFraiAdapter = GridIndemniteMensuellAdapter(listSummaryFrai)
    }

    private fun attachAdapterGridToRecycleView() {
        gridrecyclermonsuelle.adapter = summaryFraiAdapter
    }

    private fun showDialogChangeMonthIfIsRecentlyVisible() {
        if (viewModel.isDialogChangeMonthShown) {
            dialog.show()
        }
    }

    private fun observeActivitiesMensuel() {
        Timber.e("eeererreerr")
        viewModel.indemnitmensuell.observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    listSemaineFrai.clear()
                    listSemaineFrai.clear()
                    val weeksNumbersInMonth = Utils.getWeeksNumbersInMonth(
                        viewModel.month.value!!,
                        viewModel.year.value!!,
                        locale,
                        Calendar.MONDAY
                    )
                    var somme = 0

                    if (!it.data!!.semaineFrais.isNullOrEmpty()) {
                        if (it.data.semaineFrais.size <= weeksNumbersInMonth.size) {
                            it.data.semaineFrais.forEachIndexed { index, week ->
                                week.jour = "S${weeksNumbersInMonth[index]}"
                            }
                            listSemaineFrai.addAll(it.data.semaineFrais)
                            listSemaineFrai.forEach {
                                somme += it.jourMontantFrais
                            }
                            viewModel.totalCumulMensuel.value = somme
                            semaineIndemniteMensuelleAdapter.notifyDataSetChanged()
                        }

                    }
                    if (!it.data.summaryFrais.isNullOrEmpty()) {
                        listSummaryFrai.addAll(it.data.summaryFrais)
                        summaryFraiAdapter.notifyDataSetChanged()
                    }

                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.NO_CONTENT -> Timber.e("NO_CONTENT")
            }
        })
    }


    //--------------------------------- custom Navigation ------------------------------------//

    override fun initComponent() {
        component = if (activity is MainActivity) {
            (activity as MainActivity).component.indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(this)
                .setDialogChangeWeekListner(null)
                .setDialogChangeYearListner(null)
                .build()
        } else {
            (activity as MainTabletteActivity).component.indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(this)
                .setDialogChangeWeekListner(null)
                .setDialogChangeYearListner(null)
                .build()
        }
    }

    override fun injectDependencies() {
        component.injectIndemnitesMensuelFragment(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(IndemniteMensuelleViewModel::class.java)
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
        (parentFragment as IndemnitesFragment).startJournalierFragment(
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
        (parentFragment as IndemnitesFragment).setYearMonthDay(
            calendarView.year,
            calendarView.month,
            1
        )
        viewModel.month.value = calendarView.month
        viewModel.year.value = calendarView.year
        viewModel.getIndemniteMensuell(
            Utils.getFirstDayMonth(
                calendarView.year,
                calendarView.month,
                locale
            ),
            Utils.getLastDayMonth(calendarView.year, calendarView.month, locale)
        )
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
        (parentFragment as IndemnitesFragment).startHebdomadaireFragment(
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
        (parentFragment as IndemnitesFragment).dismissLoading()
    }

    private fun showLoading() {
        (parentFragment as IndemnitesFragment).dismissLoading()
    }
}
