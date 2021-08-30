package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentGererAbsenceBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.GererAbsenceComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.TabFragmentAdapter
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider.AbsenceAValiderFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_gerer_absence.*
import kotlinx.android.synthetic.main.fragment_messagerie.viewPager
import javax.inject.Inject


class GererAbsenceFragment : BaseFragment() {

    private lateinit var component: GererAbsenceComponent
    private lateinit var viewModel: GererAbsenceViewModel
    private lateinit var binding: FragmentGererAbsenceBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var tabFragmentAdapter: TabFragmentAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gerer_absence, container, false)
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
        component = (activity as MainTabletteActivity).component.gererAbsenceComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(GererAbsenceViewModel::class.java)
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnCalendrierEquipeEvent.observe(viewLifecycleOwner, {
            it?.let {
                sendStartFragmentCalendrierEquipeReceiver()
            }
        })
    }

    fun sendStartFragmentCalendrierEquipeReceiver()
    {
        val intent = Intent("StartFragmentCalendrierEquipeReceiver")
       requireContext().sendBroadcast(intent)
    }


    private fun initTabFragmentAdapter() {
        tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        tabFragmentAdapter.addFragment(
            AbsenceAValiderFragment(),
            resources.getString(R.string.a_valider)
        )
        tabFragmentAdapter.addFragment(
            AbsenceEnAttenteFragment(),
            resources.getString(R.string.en_attente)
        )
        tabFragmentAdapter.addFragment(
            AbsenceValideeFragment(),
            resources.getString(R.string.valid_es)
        )
        tabFragmentAdapter.addFragment(
            AbsenceRefuseeFragment(),
            resources.getString(R.string.refus_es)
        )
    }

    private fun associateViewPagerWithTabLayout() {
        tabLayout_gerer_absence.setupWithViewPager(viewPager)
    }

    private fun setAdapterInViewPager() {
        viewPager.adapter = tabFragmentAdapter
    }

    private fun setupViewPager() {
        viewPager.offscreenPageLimit = 4
    }

}