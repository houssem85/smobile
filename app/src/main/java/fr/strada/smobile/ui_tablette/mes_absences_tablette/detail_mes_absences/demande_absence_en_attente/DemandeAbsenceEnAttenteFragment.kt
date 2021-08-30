package fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.demande_absence_en_attente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentDemandeAbsenceEnAttenteBinding
import fr.strada.smobile.di_tablette.detail_mes_absences.demande_absence_en_attente.DemandeAbsenceEnAttenteComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.soldeabsence.SoldeAbsenceAdapter
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_a_valider.*
import javax.inject.Inject


class DemandeAbsenceEnAttenteFragment : BaseFragment() {

    private lateinit var component: DemandeAbsenceEnAttenteComponent
    private lateinit var viewModel: DemandeAbsenceEnAttenteViewModel
    private lateinit var binding: FragmentDemandeAbsenceEnAttenteBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: SoldeAbsenceAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_demande_absence_en_attente, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeAbsencesRefusee()
        viewModel.getAbsencesInProgress()
    }

    override fun initComponent() {

        component = (activity as MainTabletteActivity).component.demandeAbsenceEnAttenteComponent()
            .setContext(requireContext())
            .build()
    }

    private fun attachAdapterInRecycleView() {
        recycleView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun observeAbsencesRefusee() {
        viewModel.absencesEnAttente.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(DemandeAbsenceEnAttenteViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

}