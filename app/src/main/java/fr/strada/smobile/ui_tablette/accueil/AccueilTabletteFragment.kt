package fr.strada.smobile.ui_tablette.accueil

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentAccueilTabletteBinding
import fr.strada.smobile.di_tablette.accueil_tablette.AccueilTabletteComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.accueil.Utils.fromDpToPixels
import fr.strada.smobile.ui_tablette.accueil.Utils.getWidthScreen
import fr.strada.smobile.ui_tablette.accueil.statistique.StatistiqueFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.listner.StatistiqueFragmentListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_accueil_tablette.*
import javax.inject.Inject

class AccueilTabletteFragment : BaseFragment(), StatistiqueFragmentListner {

    private lateinit var component: AccueilTabletteComponent
    private lateinit var viewModel: AccueilTabletteViewModel
    private lateinit var binding: FragmentAccueilTabletteBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_accueil_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setStatistiqueFragment()
        }
        setupContainer()
    }

    override fun initComponent() {
        component = (activity as MainTabletteActivity).component.accueilTabletteComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(AccueilTabletteViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

    private fun setStatistiqueFragment() {
        val statistiqueFragment = StatistiqueFragment.newInstance(this)
        changeFragment(statistiqueFragment, R.id.firstFragment)
    }

    private fun changeFragment(newFragment: Fragment, resourceLayout: Int) {
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(resourceLayout, newFragment).commit()
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = getWidthScreen(requireActivity())
        val widthClosedSideMenu = fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame_accueil.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame_accueil.layoutParams = layoutParams
    }

    override fun onClickVoirTousListner() {
        try {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                // (activity as MainTabletteActivity).changeFragment(MesActivitiesTabletteFragment())
                // (activity as MainTabletteActivity).setMenuItemActive(2)
            }
        } catch (ex: Exception) {

        }
    }
}