package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentAbsenceEnAttenteBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_a_valider.*
import timber.log.Timber
import javax.inject.Inject


class AbsenceEnAttenteFragment : BaseFragment() {

    private lateinit var component: AbsenceEnAttenteComponent
    private lateinit var viewModel: AbsenceEnAttenteViewModel
    private lateinit var binding: FragmentAbsenceEnAttenteBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: AbsenceEnAttenteAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_absence_en_attente, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("HOUSSEM_TAG").i("onViewCreated AbsenceEnAttenteFragment")
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeAbsencesEnAttente()
        viewModel.getAbsencesEnAttente()
    }

    private fun attachAdapterInRecycleView() {
        recycleView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun observeAbsencesEnAttente() {
        viewModel.absencesEnAttente.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.absenceEnAttenteComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(AbsenceEnAttenteViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }


}