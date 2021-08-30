package fr.strada.smobile.ui_tablette.gerer_les_absences_tablette

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentGererLesAbsencesTabletteBinding
import fr.strada.smobile.di_tablette.gerer_les_absences_tablette.GererLesAbsencesTabletteComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.calendrier_equipe.CalendrierEquipeFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.detail_demande_absence.DetailDemandeAbsenceFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.GererAbsenceFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_gerer_les_absences_tablette.*
import timber.log.Timber
import javax.inject.Inject


class GererLesAbsencesTabletteFragment : BaseFragment() {

    private lateinit var component: GererLesAbsencesTabletteComponent
    private lateinit var viewModel: GererLesAbsencesTabletteViewModel
    private lateinit var binding: FragmentGererLesAbsencesTabletteBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var dialogFilter: Dialog

    private val startFragmentCalendrierEquipeReceiver = StartFragmentCalendrierEquipeReceiver()
    private val startFragmentDetailDemandeAbsenceReceiver = StartFragmentDetailDemandeAbsenceReceiver()
    private val finishFragmentDetailDemandeAbsenceReceiver = FinishFragmentDetailDemandeAbsenceReceiver()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gerer_les_absences_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerStartFragmentCalendrierEquipeReceiver()
        registerFinishFragmentDetailDemandeAbsenceReceiver()
        registerStartFragmentDetailDemandeAbsenceReceiver()
        val orientation= resources.configuration.orientation
        if (savedInstanceState == null)
        {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                setGererAbsenceFragment()
                Timber.tag("fsk").i("set gerer absence only")
            } else {
                setGererAbsenceFragment()
                setCalendrierEquipeFragment()
                Timber.tag("fsk").i("set gerer absence and calendrier equipe only")
            }
        }else
        {
            if(orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                if (!viewModel.isSecondFragmentLoaded)
                {
                    setCalendrierEquipeFragment()
                }
            }
            // portrait rien a faire
            if (viewModel.isThirdFragmentVisible)
            {
                startFragmentDetailDemandeAbsence()
            }

        }
        setupContainer()
    }

    fun registerStartFragmentCalendrierEquipeReceiver()
    {
        val intentFilter = IntentFilter("StartFragmentCalendrierEquipeReceiver")
        (activity as MainTabletteActivity).registerReceiver(startFragmentCalendrierEquipeReceiver,intentFilter)
    }

    fun registerStartFragmentDetailDemandeAbsenceReceiver()
    {
        val intentFilter = IntentFilter("StartFragmentDetailDemandeAbsenceReceiver")
        (activity as MainTabletteActivity).registerReceiver(startFragmentDetailDemandeAbsenceReceiver,intentFilter)
    }

    fun registerFinishFragmentDetailDemandeAbsenceReceiver()
    {
        val intentFilter = IntentFilter("FinishFragmentDetailDemandeAbsenceReceiver")
        (activity as MainTabletteActivity).registerReceiver(finishFragmentDetailDemandeAbsenceReceiver,intentFilter)
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.gererLesAbsencesComponent().setContext(requireContext()).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(GererLesAbsencesTabletteViewModel::class.java)
        // reset viewModel
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnRetourEvent.observe(viewLifecycleOwner, {
            it?.let {
                if(thirdFragment.visibility == VISIBLE)
                {
                    finishFragmentDetailDemandeAbsence()
                }else
                {
                    finishFragmentCalendrierEquipe()
                }
            }
        })
        viewModel.pressBtnFilterEvent.observe(viewLifecycleOwner, {
            it?.let {
                dialogFilter.show()
            }
         })
    }

    private fun setGererAbsenceFragment()
    {
        changeFragment(GererAbsenceFragment(), R.id.firstFragment)
    }

    private fun setCalendrierEquipeFragment()
    {
        changeFragment(CalendrierEquipeFragment(),  R.id.secondFragment)
        viewModel.isSecondFragmentLoaded = true
    }

    private fun setDetailDemandeAbsenceFragment()
    {
        changeFragment(DetailDemandeAbsenceFragment(), R.id.thirdFragment)
    }

    private fun changeFragment(newFragment: Fragment, resourceLayout: Int)
    {
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(resourceLayout, newFragment).commit()
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame_gerer_les_absences.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame_gerer_les_absences.layoutParams = layoutParams
    }

    fun startFragmentCalendrierEquipe()
    {
        btnRetour?.visibility = VISIBLE
        lblTitle?.text = resources.getString(R.string.calendrier_d_quipe)
        secondFragment?.visibility = VISIBLE
        icFilter?.visibility = GONE
        setCalendrierEquipeFragment()
    }

    fun startFragmentDetailDemandeAbsence()
    {
        btnRetour?.visibility = VISIBLE
        lblTitle?.text = resources.getString(R.string.d_tails_demande)
        thirdFragment?.visibility = VISIBLE
        icFilter?.visibility = GONE
        setDetailDemandeAbsenceFragment()
        viewModel.isThirdFragmentVisible = true
    }

    fun finishFragmentCalendrierEquipe()
    {
        btnRetour?.visibility = GONE
        lblTitle?.text = resources.getString(R.string.gerer_les_absences)
        secondFragment?.visibility = GONE
        icFilter?.visibility = VISIBLE
    }

    fun finishFragmentDetailDemandeAbsence()
    {
        btnRetour?.visibility = GONE
        lblTitle?.text = resources.getString(R.string.gerer_les_absences)
        thirdFragment?.visibility = GONE
        icFilter?.visibility = VISIBLE
        viewModel.isThirdFragmentVisible = false
    }

    inner class StartFragmentCalendrierEquipeReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            startFragmentCalendrierEquipe()
        }
    }

    inner class StartFragmentDetailDemandeAbsenceReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            startFragmentDetailDemandeAbsence()
        }
    }


    inner class FinishFragmentDetailDemandeAbsenceReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            finishFragmentDetailDemandeAbsence()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregiterStartFragmentCalendrierEquipeReceiver()
        unregiterStartFragmentDetailDemandeAbsenceReceiver()
        unregiterFinishFragmentDetailDemandeAbsenceReceiver()
    }

    fun unregiterStartFragmentCalendrierEquipeReceiver()
    {
        (activity as MainTabletteActivity).unregisterReceiver(startFragmentCalendrierEquipeReceiver)
    }

    fun unregiterStartFragmentDetailDemandeAbsenceReceiver()
    {
        (activity as MainTabletteActivity).unregisterReceiver(startFragmentDetailDemandeAbsenceReceiver)
    }

    fun unregiterFinishFragmentDetailDemandeAbsenceReceiver()
    {
        (activity as MainTabletteActivity).unregisterReceiver(finishFragmentDetailDemandeAbsenceReceiver)
    }
}