package fr.strada.smobile.ui_tablette.mes_absences_tablette

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentMesAbsencesTabletteBinding
import fr.strada.smobile.di_tablette.mes_absences_tablette.MesAbsencesTabletteComponent
import fr.strada.smobile.ui.absencehistory.AbsenceHistoryFragment
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.soldeabsence.SoldeAbsenceFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.DetailMesAbsencesFragment
import fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette.SoldeAbsenceTabletteFragment
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_mes_absences_tablette.*
import timber.log.Timber
import javax.inject.Inject

class MesAbsencesTabletteFragment : BaseFragment() {

    private lateinit var component : MesAbsencesTabletteComponent
    private lateinit var binding : FragmentMesAbsencesTabletteBinding
    private lateinit var viewModel: MesAbsencesTabletteViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    private val startFragmentHistoriqueAbsenceReceiver = StartFragmentHistoriqueAbsenceReceiver()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mes_absences_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("11-2020","MesAbsencesTabletteFragment")
        registerStartFragmentHistoriqueAbsenceReceiver()
        val orientation= resources.configuration.orientation
        if(savedInstanceState == null)
        {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) // portrai first time
            {
                setFragmentSoldeAbsence()
            }else
            {
                setFragmentSoldeAbsenceTablette()
                setFragmentDetailMesAbsences()
            }
        }else
        {
            if(orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                if(!viewModel.isFragmentSoldeAbsenceLoaded)
                {
                    setFragmentSoldeAbsence()
                }
            }else
            {
                if(!viewModel.isFragmentSoldeAbsenceTabletteLoaded){
                    setFragmentSoldeAbsenceTablette()
                }
                if(!viewModel.isFragmentDetailMesAbsences){
                    setFragmentDetailMesAbsences()
                }
            }
        }
        setupContainer()
    }

    private fun registerStartFragmentHistoriqueAbsenceReceiver()
    {
        val intentFilter = IntentFilter("StartFragmentHistoriqueAbsenceReceiver")
        (activity as MainTabletteActivity).registerReceiver(startFragmentHistoriqueAbsenceReceiver,intentFilter)
    }

    private fun setFragmentSoldeAbsence()
    {
        val fragment = SoldeAbsenceFragment()
        changeFragment(fragment,R.id.thirdFragment)
        viewModel.isFragmentSoldeAbsenceLoaded = true
    }

    private fun setFragmentSoldeAbsenceTablette()
    {
        val fragment = SoldeAbsenceTabletteFragment()
        changeFragment(fragment,R.id.firstFragment)
        viewModel.isFragmentSoldeAbsenceTabletteLoaded = true
    }

    private fun setFragmentDetailMesAbsences()
    {
        val fragment = DetailMesAbsencesFragment()
        changeFragment(fragment,R.id.secondFragment)
        viewModel.isFragmentDetailMesAbsences = true
    }

    private fun setHistoriqueAbsenceFragment()
    {
        val fragment = AbsenceHistoryFragment()
        changeFragment(fragment,R.id.fourthFragment)
        viewModel.isFragmentHistoriqueAbsenceLoaded = true
    }

    fun startHistoriqueAbsenceFragment()
    {
        lblTitle?.text = resources.getString(R.string.historique_d_absences)
        btnRetour?.visibility = VISIBLE
        btnDemandeAbsence?.visibility = GONE
        fourthFragment?.visibility = VISIBLE
        if(!viewModel.isFragmentHistoriqueAbsenceLoaded)
        {
            setHistoriqueAbsenceFragment()
        }
    }

    private fun finishHistoriqueAbsenceFragment()
    {
        lblTitle?.text = resources.getString(R.string.mes_absences)
        btnRetour?.visibility = GONE
        btnDemandeAbsence?.visibility = VISIBLE
        fourthFragment?.visibility = GONE
    }

    override fun initComponent()
    {
        component = (activity as MainTabletteActivity).component.mesAbsencesTabletteComponent().setContext(requireContext()).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MesAbsencesTabletteViewModel::class.java)
        //
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnDemandeAbsenceEvent.observe(viewLifecycleOwner, {
           it?.let {
               Timber.tag("fsk").i("press add ")
               // (activity as MainTabletteActivity).changeFragment(AbsenceRequestFragment.newInstance())
           }
        })
        viewModel.pressBtnRetourEvent.observe(viewLifecycleOwner, {
            it?.let {
               finishHistoriqueAbsenceFragment()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = MesAbsencesTabletteFragment()
    }

    private fun changeFragment(newFragment: Fragment, resourceLayout: Int)
    {
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(resourceLayout, newFragment).commit()
    }

    inner class StartFragmentHistoriqueAbsenceReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            startHistoriqueAbsenceFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregiterStartFragmentHistoriqueAbsenceReceiver()
    }

    fun unregiterStartFragmentHistoriqueAbsenceReceiver()
    {
        (activity as MainTabletteActivity).unregisterReceiver(startFragmentHistoriqueAbsenceReceiver)
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame_mes_absences.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame_mes_absences.layoutParams = layoutParams
    }
}