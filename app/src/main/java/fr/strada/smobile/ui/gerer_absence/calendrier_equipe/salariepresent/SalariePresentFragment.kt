package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.salariepresent


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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentSalariePresentBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.salaries_presents.SalariesPresentsComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.CalendrierEquipeFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_salarie_present.*
import kotlinx.android.synthetic.main.layout_no_employee_present.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SalariePresentFragment : BaseFragment() {

    private lateinit var component: SalariesPresentsComponent
    private lateinit var viewModel: SalariePresentViewModel
    private lateinit var binding: FragmentSalariePresentBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var adapter: SalariePresentAdapter
    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    private val finishEmployeePresentReceiver = FinishEmployeePresentReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_salarie_present, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        registerEmployeePresentReceiver()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeSalariesAbsents()
        viewModel.getSalariePresents()
    }

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.salariePresentComponent()
                .setContext(requireContext())
                .build()
        }
        else if (activity is MainTabletteActivity){
            component = (activity as MainTabletteActivity).component.salariePresentComponent()
                .setContext(requireContext())
                .build()
        }
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(SalariePresentViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {

    }


    private fun initViews(){
        if (CalendrierEquipeFragment.month == 7
            && CalendrierEquipeFragment.year == 2020
        ) {
            recycleView_salarie_present?.visibility = View.GONE
            layout_no_employee_present?.visibility = View.VISIBLE
        } else {
            recycleView_salarie_present?.visibility = View.VISIBLE
            layout_no_employee_present?.visibility = View.GONE
        }
    }

    private fun attachAdapterInRecycleView() {
        recycleView_salarie_present.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView_salarie_present.layoutManager = layoutManager

    }

    private fun observeSalariesAbsents() {
        viewModel.salariesPresents.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }


    inner class FinishEmployeePresentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getStringExtra("employeePresent")
            if (action == "isEmployeePresentVisible") {
                isListVisible()
            } else if (action == "isEmployeePresentGone") {
                isListGone()
            }
        }
    }


    fun isListVisible() {
        recycleView_salarie_present?.visibility = View.VISIBLE
        layout_no_employee_present?.visibility = View.GONE
    }

    fun isListGone() {
        recycleView_salarie_present?.visibility = View.GONE
        layout_no_employee_present?.visibility = View.VISIBLE
    }

    private fun registerEmployeePresentReceiver() {
        val filter = IntentFilter("FinishEmployeePresentReceiver")
        activity?.registerReceiver(finishEmployeePresentReceiver, filter)
    }

}
