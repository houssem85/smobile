package fr.strada.smobile.ui.absencehistory


import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import fr.strada.smobile.R
import fr.strada.smobile.ui.absencehistory.absence_acceptee.DemandesAbsencesAccepteesFragment
import fr.strada.smobile.ui.absencehistory.absence_refusee.DemandesAbsencesRefuseesFragment
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.base.BaseNFragment
import fr.strada.smobile.ui.gerer_absence.TabFragmentAdapter
import fr.strada.smobile.ui.main.MainActivity
import java.util.*

class AbsenceHistoryFragment : BaseNFragment() ,ViewPager.OnPageChangeListener {

    lateinit var tabFragmentAdapter: TabFragmentAdapter
    var cal = Calendar.getInstance()

    lateinit var txtYear: TextView
    var txtMonth: TextView? = null
    lateinit var datePickerView: Dialog
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var viewMonth: ConstraintLayout
    var btnBack: AppCompatImageView? = null
    lateinit var calendarView : CollapsibleCalendar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_absence_history, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setupViewPager()
        initCalendar()
        configCalendar(calendarView, txtMonth!!, txtYear)
        changeDate()
        setupNavigation()
    }

    private fun setupNavigation()
    {
        btnBack?.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    private fun init(view: View) {
        datePickerView = activity?.let { Dialog(it) }!!
        txtMonth = view.findViewById(R.id.txt_month)
        txtYear = view.findViewById(R.id.txt_year)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewMonth = view.findViewById(R.id.view_month)
        btnBack = view.findViewById(R.id.btnBack)
        calendarView = view.findViewById(R.id.calendarView)
    }



    private fun setupViewPager()
    {
        viewPager.offscreenPageLimit  = 2
        tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        tabFragmentAdapter.addFragment(DemandesAbsencesAccepteesFragment(), resources.getString(R.string.demandes_accept_es))
        tabFragmentAdapter.addFragment(DemandesAbsencesRefuseesFragment(), resources.getString(R.string.demandes_refus_es))
        viewPager.adapter = tabFragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(this)
    }

    //------------------View Pager Listner-----------------//

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int)
    {
        val month = calendarView.month
        val year = calendarView.year
        val currentMonth = cal.get(Calendar.MONTH)
        val currentYear = cal.get(Calendar.YEAR)
        if(currentMonth==month && currentYear==year){
            if (position == 0){
                makeEventTagAccepted()
            }
            else {
                makeEventTagRefused()
            }
        }else
        {
            clearEvent()
        }
    }

    //-------------------------------------------------//

    private fun initCalendar() {
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        txtMonth?.text = context?.let { getMonthName(it, month) }
        txtYear.text = year.toString()
        Handler().post {
            try
            {
                if (tabLayout.selectedTabPosition ==0)
                {
                    makeEventTagAccepted()
                } else
                {
                    makeEventTagRefused()
                }
            }catch (ex:Exception){

            }
        }
    }

    private fun changeDate()
    {
        viewMonth.setOnClickListener {
            var year: Int = calendarView.year
            var month: Int = calendarView.month
            val datePickerView = Dialog(requireContext())
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
                datePickerView.dismiss()
                calendarView.year = picker.year
                calendarView.month = picker.month
                calendarView.prevMonth()
                date.text = context?.let { it1 -> getMonthName(it1, month - 1) }
                txtYear.text = year.toString()
            }
            btnCancel.setOnClickListener {
                datePickerView.dismiss()
            }
            datePickerView.show()
        }


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
                val currentMonth = cal.get(Calendar.MONTH)
                val currentYear = cal.get(Calendar.YEAR)
                if(currentMonth==month && currentYear==year){
                    if (tabLayout.selectedTabPosition ==0){
                        makeEventTagAccepted()
                    }
                    else {
                        makeEventTagRefused()
                    }
                }else{
                    clearEvent()
                }
                sendMonthYearReceiver(requireContext(),month,year)

            }

            override fun onWeekChange(position: Int, isNext: Boolean) {
            }

            override fun onClickListener() {
            }

            override fun onDayChanged() {
            }
        })

    }


     fun sendMonthYearReceiver(context: Context,month:Int,year:Int) {
         val intent = Intent("MonthYearReceiver")
         intent.putExtra("month", month)
         intent.putExtra("year", year)
         context.sendBroadcast(intent)
     }

    fun makeEventTagAccepted() {
        clearEvent()
        calendarView.addEventTag(2020, 7, 1, R.drawable.bg_first_day_interval_absence_accepted)
        for (i in 2 until 13) {
            calendarView.addEventTag(2020, 7, i, R.drawable.bg_interval_absence_accepted)
        }
        calendarView.addEventTag(2020, 7, 13, R.drawable.bg_last_day_interval_absence_accepted)

    }

    fun makeEventTagRefused() {
        clearEvent()
        calendarView.addEventTag(
            2020,
            7,
            16,
            R.drawable.bg_first_day_interval_absence_refused
        )
        calendarView.addEventTag(
            2020,
            7,
            17,
            R.drawable.bg_last_day_interval_absence_refused
        )

    }

    fun clearEvent(){
        calendarView.clearListEventTag()
    }
}
