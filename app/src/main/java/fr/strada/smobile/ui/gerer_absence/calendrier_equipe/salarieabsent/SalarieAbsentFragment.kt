package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salarieabsent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentSalarieAbsentBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_absents.SalariesAbsentsComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.CalendrierEquipeFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_salarie_absent.*
import kotlinx.android.synthetic.main.layout_no_employee_absent.*
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SalarieAbsentFragment : BaseFragment() {

    private lateinit var component: SalariesAbsentsComponent
    private lateinit var viewModel: SalarieAbsentViewModel
    private lateinit var binding: FragmentSalarieAbsentBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var adapter: SalarieAbsentAdapter
    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    private val finishEmployeeAbsentReceiver = FinishEmployeeAbsentReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_salarie_absent, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("OLD_TAG").i("onCreateView SalarieAbsentFragment")
        initViews()
        registerEmployeeAbsentReceiver()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeSalariesAbsents()
        viewModel.getSalarieAbsents()
    }

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.salarieAbsentComponent()
                .setContext(requireContext())
                .build()
        }
        else if (activity is MainTabletteActivity){
            component = (activity as MainTabletteActivity).component.salarieAbsentComponent()
                .setContext(requireContext())
                .build()
        }
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(SalarieAbsentViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

    private fun attachAdapterInRecycleView() {
        recycleView_salarie_absent.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView_salarie_absent.layoutManager = layoutManager
    }


    private fun observeSalariesAbsents() {
        viewModel.salariesAbsents.observe(viewLifecycleOwner, Observer {
            adapter.items = it
        })
    }

    private fun initViews() {
        if (CalendrierEquipeFragment.month == 7
            && CalendrierEquipeFragment.year == 2020
        ) {
            recycleView_salarie_absent?.visibility = View.GONE
            layout_no_employee_absent?.visibility = View.VISIBLE

        } else {
            recycleView_salarie_absent?.visibility = View.VISIBLE
            layout_no_employee_absent?.visibility = View.GONE
        }
    }


    inner class FinishEmployeeAbsentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getStringExtra("employeeAbsent")
            if (action == "isEmployeeAbsentVisible") {
                isListVisible()
            } else if (action == "isEmployeeAbsentGone") {
                isListGone()
            }
        }
    }


    fun isListVisible() {
        recycleView_salarie_absent?.visibility = View.VISIBLE
        layout_no_employee_absent?.visibility = View.GONE
    }

    fun isListGone() {
        recycleView_salarie_absent?.visibility = View.GONE
        layout_no_employee_absent?.visibility = View.VISIBLE
    }

    private fun registerEmployeeAbsentReceiver() {
        val filter = IntentFilter("FinishEmployeeAbsentReceiver")
        activity?.registerReceiver(finishEmployeeAbsentReceiver, filter)
    }


}
