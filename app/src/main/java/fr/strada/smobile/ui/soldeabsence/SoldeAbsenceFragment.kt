package fr.strada.smobile.ui.soldeabsence


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.data.Interval
import com.shrikanthravi.collapsiblecalendarview.data.TypeConge
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentSoldeAbsenceBinding
import fr.strada.smobile.di.solde_absence.SoldeAbsenceComponent
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.Toggle.toggleLayout
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_solde_absence.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */


class SoldeAbsenceFragment : BaseFragment() {

    private lateinit var component:SoldeAbsenceComponent
    private lateinit var binding:FragmentSoldeAbsenceBinding
    private lateinit var viewModel:SoldeAbsenceViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var calendarView: CollapsibleCalendar
    var cal = Calendar.getInstance()
    var year = cal.get(Calendar.YEAR)
    var month = cal.get(Calendar.MONTH)
    lateinit var adapter : SoldeAbsenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solde_absence, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    private fun init(view: View) {

        val stub = view.findViewById(R.id.stub) as ViewStub
        val inflated = stub.inflate()
        calendarView = inflated.findViewById(R.id.calendarView)
    }

    private fun displayExpandedMenu() {
        solde.setOnClickListener {
            val isExpanded = menu_solde.visibility == View.GONE
            toggleLayout(isExpanded, img_expand, menu_solde)
        }
    }

    private fun initRecyclerView()
    {   adapter = SoldeAbsenceAdapter(context)
        adapter.items = viewModel.getSoldeAbsence()
        recycler_solde.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recycler_solde.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().post {
            try {
                init(view)
                initCalendar()
                initRecyclerView()
                configCalendar(calendarView, txt_month, txt_year)
                changeDate()
                displayExpandedMenu()
                makeEventTag()
                img_down.visibility = VISIBLE

                if (activity is MainActivity) {
                    (activity as MainActivity).dialogLoading.dismiss()
                }
                else if (activity is MainTabletteActivity){
                    (activity as MainTabletteActivity).dialogLoading.dismiss()
                }
            }catch (ex:Exception){
            }
        }
    }

    override fun initComponent() {
        component = if(activity is MainTabletteActivity) {
            (activity as MainTabletteActivity).component.soldeAbsenceComponent().setContext(requireContext()).build()
        }else {
            (activity as MainActivity).component.soldeAbsenceComponent().setContext(requireContext()).build()
        }
    }

    override fun injectDependencies()
    {
        component.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(SoldeAbsenceViewModel::class.java)
        //
        viewModel.resetViewModel()
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        btnOpenMenu?.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }
        btnAddAbsence?.setOnClickListener {
            // to change later
           //  (activity as MainActivity).changeFragment(AbsenceRequestFragment.newInstance())
        }
        view_historique?.setOnClickListener {
            if(activity is MainActivity)
            {
                // (activity as MainActivity).addFragment(AbsenceHistoryFragment())
            }else
            {
                sendStartFragmentHistoriqueAbsenceReceiver()
            }
        }
    }

    private fun initCalendar() {
        txt_month.text = context?.let { getMonthName(it, month) }
        txt_year.text = year.toString()
        calendarView.month = month
        calendarView.year = year
        cal.set(year, month, 1)
        calendarView.setAdapter(CalendarAdapter(requireContext(), cal))
        calendarView.todayItemBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_date_jour)
        calendarView.todayItemTextColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
    }


    fun configCalendar(
        calendarView: CollapsibleCalendar,
        textMonth: TextView,
        textYear: TextView
    ) {
        calendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDaySelect() {
            }

            override fun onItemClick(v: View) {
            }

            override fun onDataUpdate() {
                val month = calendarView.month
                val year = calendarView.year
                textMonth.text = context?.let { getMonthName(it, month) }
                textYear.text = year.toString()
            }

            override fun onMonthChange() {
                val month = calendarView.month
                val year = calendarView.year
                textMonth.text = context?.let { getMonthName(it, month) }
                textYear.text = year.toString()
            }

            override fun onWeekChange(position: Int, isNext: Boolean) {
            }

            override fun onClickListener() {
            }

            override fun onDayChanged() {
            }
        })

    }

    private fun makeEventTag() {
        val intervals = ArrayList<Interval>()
        intervals.add(Interval(Day(2020,month,1),Day(2020,month,10),TypeConge.PRIS))
        intervals.add(Interval(Day(2020,month,18),Day(2020,month,20),TypeConge.DEMMANDE))
        calendarView.addIntervals(intervals)
    }


    fun changeDate() {
        view_month.setOnClickListener {

            var year: Int = calendarView.year
            var month: Int = calendarView.month

            val datePickerView = Dialog(requireActivity())
            datePickerView.requestWindowFeature(Window.FEATURE_NO_TITLE)
            datePickerView.setCancelable(false)
            datePickerView.setContentView(R.layout.dialog_change_month)
            datePickerView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val date = datePickerView.findViewById<TextView>(R.id.txt_date)
            date.text = context?.let { it1 -> getMonthName(it1, month) } + " " + year

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
                date.text = context?.let { it1 -> getMonthName(it1, month - 1) }
                txt_year.text = year.toString()
                datePickerView.dismiss()

            }
            btnCancel.setOnClickListener {
                datePickerView.dismiss()
            }
            datePickerView.show()
        }
    }


    fun sendStartFragmentHistoriqueAbsenceReceiver()
    {
        val intent = Intent("StartFragmentHistoriqueAbsenceReceiver")
        context?.sendBroadcast(intent)
    }

}
