package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentAbsenceValideeBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_a_valider.*
import timber.log.Timber
import javax.inject.Inject

class AbsenceValideeFragment : BaseFragment() {

    private lateinit var component: AbsenceValideeComponent
    private lateinit var viewModel: AbsenceValideeViewModel
    private lateinit var binding: FragmentAbsenceValideeBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: AbsenceValideeAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_absence_validee, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("HOUSSEM_TAG").i("onViewCreated AbsenceValideeFragment")
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeAbsencesValidee()
        viewModel.getAbsencesValidee()
    }

    private fun attachAdapterInRecycleView() {
        recycleView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun observeAbsencesValidee() {
        viewModel.absencesValidee.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.absenceValideeComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(AbsenceValideeViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }


}