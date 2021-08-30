package fr.strada.smobile.ui_tablette.accueil.statistique

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentStatistiqueBinding
import fr.strada.smobile.di_tablette.accueil_tablette.statistique.StatistiqueComponent
import fr.strada.smobile.ui.activities.journalier.getStrDate
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.pointeuse.millisToTime
import fr.strada.smobile.ui_tablette.accueil.Utils.getFirstDayInCurrentWeek
import fr.strada.smobile.ui_tablette.accueil.Utils.getLastDayInCurrentWeek
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.TopSpacingItemDecoration
import fr.strada.smobile.utils.listner.StatistiqueFragmentListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_statistique.*
import java.util.*
import javax.inject.Inject


class StatistiqueFragment : BaseFragment() {

    private lateinit var binding: FragmentStatistiqueBinding
    private lateinit var component: StatistiqueComponent
    private lateinit var viewModel: StatistiqueViewModel

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: ListeAbsenceAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    @Inject
    internal lateinit var topSpacingItemDecoration: TopSpacingItemDecoration

    var statistiqueFragmentListner: StatistiqueFragmentListner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistique, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSpaceToItemsInRecycleView()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeAbsences()
        viewModel.getAbsences()
        observeVoirTousVisibility()
        observeActivitiesHebdomadaire()
        if(savedInstanceState == null) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val strDate = getStrDate(year,month+1,day,locale)
            val firstDayOfWeek = getFirstDayInCurrentWeek(Calendar.MONDAY,locale)
            val lastDayOfWeek = getLastDayInCurrentWeek(Calendar.MONDAY,locale)
            viewModel.getActivitiesHebdomadaire(year,month+1,firstDayOfWeek,lastDayOfWeek)
        }
    }


    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.statistiqueComponent().setContext(requireContext()).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(StatistiqueViewModel::class.java)
        // init ViewModel
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnVoirTousEvent.observe(viewLifecycleOwner, {
            it?.let {
                statistiqueFragmentListner?.onClickVoirTousListner()
            }
        })
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun attachAdapterInRecycleView() {
        recycleView.adapter = adapter
    }

    private fun addSpaceToItemsInRecycleView() {
        recycleView.addItemDecoration(topSpacingItemDecoration)
    }

    private fun observeAbsences() {
        viewModel.absences.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    private fun observeVoirTousVisibility() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewModel.voirTous.value = View.GONE
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewModel.voirTous.value = View.VISIBLE
        }
    }

    private fun observeActivitiesHebdomadaire(){
        viewModel.activitiesHebdomadaire.observe(viewLifecycleOwner,{
            if(it.status == Status.SUCCESS){
                lblServiceHebdo.text = millisToTime(it.data!!.serviceCumul.totalMilliseconds)
                lblNuitHebdo.text = millisToTime(it.data.nuitCumul.totalMilliseconds)
            }
        })
    }

    companion object {
        fun newInstance(statistiqueFragmentListner: StatistiqueFragmentListner): StatistiqueFragment {
            val fragment = StatistiqueFragment()
            fragment.statistiqueFragmentListner = statistiqueFragmentListner
            return fragment
        }
    }
}