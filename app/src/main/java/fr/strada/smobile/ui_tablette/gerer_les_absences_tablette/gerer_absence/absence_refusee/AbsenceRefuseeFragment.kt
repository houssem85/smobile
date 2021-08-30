package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentAbsenceRefuseeBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_a_valider.*
import timber.log.Timber
import javax.inject.Inject


class AbsenceRefuseeFragment : BaseFragment() {

    private lateinit var component: AbsenceRefuseeComponent
    private lateinit var viewModel: AbsenceRefuseeViewModel
    private lateinit var binding: FragmentAbsenceRefuseeBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: AbsenceRefuseeAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_absence_refusee, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("HOUSSEM_TAG").i("onViewCreated AbsenceRefuseeFragment")
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeAbsencesRefusee()
        viewModel.getAbsencesRefusee()
    }

    private fun attachAdapterInRecycleView() {
        recycleView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun observeAbsencesRefusee() {
        viewModel.absencesRefusee.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.absenceRefuseeComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(AbsenceRefuseeViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }


}