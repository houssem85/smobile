package fr.strada.smobile.ui.apropos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentAProposBinding
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.android.synthetic.main.fragment_a_propos.*
import kotlinx.android.synthetic.main.fragment_a_propos.btnOpenMenu
import kotlinx.android.synthetic.main.fragment_time_clock.*
import javax.inject.Inject


class AProposFragment : BaseFragment() {

    private lateinit var viewModel: AProposViewModel
    private lateinit var binding: FragmentAProposBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_a_propos, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimation()
        setVersionApp()
        if (activity is MainActivitySpi)
        {
            btnOpenMenu?.visibility = View.INVISIBLE
            (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.INVISIBLE
            (activity as MainActivitySpi).image_back_main.visibility = View.VISIBLE

        }
        if (activity is MainTabletteActivity) {
            setupContainer()
        }
    }

    private fun setVersionApp() {
        val versionName = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName
        txtVersionName.text = versionName+""
    }

    companion object {
        @JvmStatic
        fun newInstance() = AProposFragment()
    }

    override fun initComponent() {


    }

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(AProposViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {

    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = fr.strada.smobile.ui_tablette.accueil.Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu =
            fr.strada.smobile.ui_tablette.accueil.Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame_apropos.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame_apropos.layoutParams = layoutParams
    }

    private fun startAnimation() {
        val formBottomAnimation = Utils.inFromBottomAnimation()
        apropos_container.startAnimation(formBottomAnimation)
    }

}