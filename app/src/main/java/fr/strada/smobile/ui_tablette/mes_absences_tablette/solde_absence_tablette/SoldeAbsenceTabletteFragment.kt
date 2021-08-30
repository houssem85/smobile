package fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.data.Interval
import com.shrikanthravi.collapsiblecalendarview.data.TypeConge
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentSoldeAbsenceTabletteBinding
import fr.strada.smobile.di_tablette.solde_absence_tablette.DIALOG_CHANGE_MONTH
import fr.strada.smobile.di_tablette.solde_absence_tablette.SoldeAbsenceTabletteComponent
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.DatePicker
import fr.strada.smobile.utils.Toggle.toggleLayout
import fr.strada.smobile.utils.listner.DialogChangeMonthListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_solde_absence_tablette.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList


class SoldeAbsenceTabletteFragment : BaseFragment(), DialogChangeMonthListner,
    CollapsibleCalendar.CalendarListener {

    private lateinit var component: SoldeAbsenceTabletteComponent
    private lateinit var viewModel: SoldeAbsenceTabletteViewModel
    private lateinit var binding: FragmentSoldeAbsenceTabletteBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Named(DIALOG_CHANGE_MONTH)
    @Inject
    internal lateinit var dialog: Dialog

    // UI
    lateinit var calendarView: CollapsibleCalendar
    var cal = Calendar.getInstance()
    var year = cal.get(Calendar.YEAR)
    var month = cal.get(Calendar.MONTH)
    var isExpanded = false

    companion object {
        @JvmStatic
        fun newInstance() = SoldeAbsenceTabletteFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_solde_absence_tablette,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("11-2020","SoldeAbsenceTabletteFragment onCreateView")
        Handler().post {
            try {
                val stub = view.findViewById(R.id.stub) as ViewStub
                val inflated = stub.inflate()
                calendarView = inflated.findViewById(R.id.calendarView)
                setupCalendarViewListner()
                makeEventTag()
                (activity as MainTabletteActivity).dialogLoading.dismiss()

            } catch (ex: Exception) {
            }
        }
    }


    private fun setupCalendarViewListner() {
        calendarView.setCalendarListener(this)
    }

    private fun makeEventTag() {
        val intervals = ArrayList<Interval>()
        intervals.add(Interval(Day(2020,month,1), Day(2020,month,10), TypeConge.PRIS))
        intervals.add(Interval(Day(2020,month,18), Day(2020,month,20), TypeConge.DEMMANDE))
        calendarView.addIntervals(intervals)
    }


    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.soldeAbsenceTabletteComponent()
            .setContext(requireContext())
            .setDialogChangeMonthListner(this)
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            providerFactory
        ).get(SoldeAbsenceTabletteViewModel::class.java)
        //
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnChangeMonthEvent.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    dialog.show()
                }
            })
        viewModel.pressBtnSoldeAbsenceEvent.observe(viewLifecycleOwner,
            {
                it?.let {
                    val isExpanded = menu_solde.visibility == View.GONE
                    toggleLayout(isExpanded, img_expand, menu_solde)
                }
            })
    }

    //----------------------------------------dialog Listner-----------------------------------//

    override fun onShowListner(dialog: Dialog, txtDate: TextView, datePicker: DatePicker) {
        val month = viewModel.month.value
        val year = viewModel.year.value
        val monthName = Utils.getMonthName(requireContext(), month!!)
        txtDate.text = "$monthName $year"
        datePicker.setDate(year!!, month)
    }

    override fun onClickValidateListner(dialog: Dialog, datePicker: DatePicker) {
        calendarView.year = datePicker.year
        calendarView.month = datePicker.month
        Timber.tag("fsk").i(" " + datePicker.month)
        viewModel.year.value = datePicker.year
        viewModel.month.value = datePicker.month - 1
        calendarView.prevMonth()
        dialog.dismiss()
    }

    override fun onClickCancelListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onDismissListner() {

    }


    //----------------------------------------calendar Listner-----------------------------------//

    override fun onDaySelect() {
    }

    override fun onItemClick(v: View) {
    }

    override fun onDataUpdate() {
        viewModel.month.value = calendarView.month
        viewModel.year.value = calendarView.year
    }

    override fun onMonthChange() {
        viewModel.month.value = calendarView.month
        viewModel.year.value = calendarView.year
    }

    override fun onWeekChange(position: Int, isNext: Boolean) {
    }

    override fun onClickListener() {
    }

    override fun onDayChanged() {}


}