package fr.strada.smobile.ui.gerer_absence.calendrier_equipe.demande_absence


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
import fr.strada.smobile.databinding.FragmentDemandeAbsenceBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.calendrier_equipe.demande_absence.DemandesAbsencesComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.detaildemandeabsence.DetailDemandAbsenceActivity
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.listner.ItemDemandeAbsenceListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_demande_absence.*
import kotlinx.android.synthetic.main.layout_no_absence_request.*
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class DemandeAbsenceFragment : BaseFragment(), ItemDemandeAbsenceListner {

    private lateinit var component: DemandesAbsencesComponent
    private lateinit var viewModel: DemandeAbsenceViewModel
    private lateinit var binding: FragmentDemandeAbsenceBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var adapter: DemandeAbsenceAdapter
    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    private val finishAbsenceRequestReceiver = FinishAbsenceRequestReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_demande_absence, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.tag("OLD_TAG").i("onCreateView DemandeAbsenceFragment")
        super.onViewCreated(view, savedInstanceState)
        registerAbsenceRequestReceiver()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeDemandeAbsence()
        viewModel.getDemandesAbsences()
    }

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component.demandeAbsenceComponent()
                .setContext(requireContext())
                .setDetailDemandeAbsenceListner(this)
                .build()
        }
        else if (activity is MainTabletteActivity){
            component = (activity as MainTabletteActivity).component.demandeAbsenceComponent()
                .setContext(requireContext())
                .setDetailDemandeAbsenceListner(this)
                .build()
        }
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(DemandeAbsenceViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

    private fun attachAdapterInRecycleView() {
        recycleView_demande_absence.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView_demande_absence.layoutManager = layoutManager
    }

    private fun observeDemandeAbsence() {
        viewModel.demandesAbsence.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }


    inner class FinishAbsenceRequestReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getStringExtra("requestAbsence")
            if (action == "isRequestVisible") {
                isListVisible()
            } else if (action == "isRequestGone") {
                isListGone()
            }
        }
    }


    fun isListVisible() {
        recycleView_demande_absence?.visibility = View.VISIBLE
        layout_no_absence_request?.visibility = View.GONE
    }

    fun isListGone() {
        recycleView_demande_absence?.visibility = View.GONE
        layout_no_absence_request?.visibility = View.VISIBLE
    }

    private fun registerAbsenceRequestReceiver() {
        val filter = IntentFilter("FinishAbsenceRequestReceiver")
        activity?.registerReceiver(finishAbsenceRequestReceiver, filter)
    }

    override fun onClickListner(position: Int) {

        if (activity is MainActivity) {
            startActivity(Intent(context, DetailDemandAbsenceActivity::class.java))
        }
        else if (activity is MainTabletteActivity){

        }
    }


}
