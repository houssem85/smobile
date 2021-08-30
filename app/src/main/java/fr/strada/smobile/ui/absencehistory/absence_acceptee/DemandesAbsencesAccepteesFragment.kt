package fr.strada.smobile.ui.absencehistory.absence_acceptee


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.ui.base.BaseNFragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DemandesAbsencesAccepteesFragment : BaseNFragment() {

    private val monthYearReceiver = MonthYearReceiver()
    var recyclerView: RecyclerView? = null
    var layoutNoAbsences: ConstraintLayout? = null
    private val viewModel: DemandesAbsencesAccepteesViewModel by lazy { DemandesAbsencesAccepteesViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_demandes_absences_acceptees, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initViews(view)
        registerMonthYearReceiver()
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.recycleView)!!
        recyclerView.apply {
            this!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = DemandesAbsencesAccepteesAdapter(
                context,
                viewModel.getAbsenceAccepted()
            )
        }
    }

    private fun initViews(view: View) {
        layoutNoAbsences = view.findViewById(R.id.layout_no_absences)!!
        recyclerView?.visibility = View.VISIBLE
        layoutNoAbsences?.visibility = View.GONE
    }

    inner class MonthYearReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val month = intent.getIntExtra("month",0)
            val year = intent.getIntExtra("year",2020)
            val cal= Calendar.getInstance()
            val currentMonth = cal.get(Calendar.MONTH)
            val currentYear = cal.get(Calendar.YEAR)
            if (month == currentMonth && currentYear==year)
            {
                isListVisible()
            } else
            {
                isListGone()
            }
        }
    }

    fun isListVisible() {
        recyclerView?.visibility = View.VISIBLE
        layoutNoAbsences?.visibility = View.GONE
    }

    fun isListGone() {
        recyclerView?.visibility = View.GONE
        layoutNoAbsences?.visibility = View.VISIBLE
    }

    private fun registerMonthYearReceiver(){
        val filter = IntentFilter("MonthYearReceiver")
        activity?.registerReceiver(monthYearReceiver, filter)
    }

}


