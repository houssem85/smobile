package fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentDetailMesAbsencesBinding
import fr.strada.smobile.di_tablette.detail_mes_absences.DetailMesAbsencesComponent
import fr.strada.smobile.ui.absencehistory.absence_acceptee.DemandesAbsencesAccepteesFragment
import fr.strada.smobile.ui.absencehistory.absence_refusee.DemandesAbsencesRefuseesFragment
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.TabFragmentAdapter
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.demande_absence_en_attente.DemandeAbsenceEnAttenteFragment
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_detail_mes_absences.*
import javax.inject.Inject


class DetailMesAbsencesFragment : BaseFragment() {

    private lateinit var component: DetailMesAbsencesComponent
    private lateinit var viewModel: DetailMesAbsencesViewModel
    private lateinit var binding: FragmentDetailMesAbsencesBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var tabFragmentAdapter: TabFragmentAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_mes_absences, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabFragmentAdapter()
        setupViewPager()
        setAdapterInViewPager()
        associateViewPagerWithTabLayout()
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.detailMesAbsencesComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(DetailMesAbsencesViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

    private fun initTabFragmentAdapter() {
        tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        tabFragmentAdapter.addFragment(
            DemandeAbsenceEnAttenteFragment(),
            resources.getString(R.string.demandes_en_attente)
        )
        tabFragmentAdapter.addFragment(
            DemandesAbsencesAccepteesFragment(),
            resources.getString(R.string.demandes_accept_es)
        )
        tabFragmentAdapter.addFragment(
            DemandesAbsencesRefuseesFragment(),
            resources.getString(R.string.demandes_refus_es)
        )

    }

    private fun associateViewPagerWithTabLayout() {
        tabLayout_detail_mes_absences.setupWithViewPager(viewPager_detail_absences)
    }

    private fun setAdapterInViewPager() {
        viewPager_detail_absences.adapter = tabFragmentAdapter
    }

    private fun setupViewPager() {
        viewPager_detail_absences.offscreenPageLimit = 3
    }


}