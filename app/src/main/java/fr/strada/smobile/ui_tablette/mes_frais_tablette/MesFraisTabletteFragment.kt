package fr.strada.smobile.ui_tablette.mes_frais_tablette

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.mes_frais.MesFraisFragment
import fr.strada.smobile.ui.mes_frais.SharedMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.detail_demande_envoyee.DetailDemandeEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee.DetailDemandeNonEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.NouvelleDemandeFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_mes_frais_tablette.*
import javax.inject.Inject

class MesFraisTabletteFragment : BaseFragment() {

    private lateinit var sharedViewModel: SharedMesFraisViewModel
    private lateinit var viewModel :MesFraisTabletteViewModel
    @Inject
    internal lateinit var providerFactory : ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mes_frais_tablette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContainer()
        if(savedInstanceState == null){
            setMesFraisFragment()
        }
        observeParamsViewModel()
    }

    override fun initComponent() {}

    override fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel()
    {
       sharedViewModel = ViewModelProvider(requireActivity(), providerFactory).get(SharedMesFraisViewModel::class.java)
       viewModel = ViewModelProvider(this, providerFactory).get(MesFraisTabletteViewModel::class.java)
    }

    override fun bindViewModel() {}

    override fun setupNavigation() {
        btnAjoutNouvelleDemande.setOnClickListener {
            setNouvelleDemandeFragment()
        }
        btnRetour?.setOnClickListener {
            removeSecondFragment()
        }
    }

    private fun setMesFraisFragment()
    {
        val fragment = MesFraisFragment()
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.firstFragment, fragment).commit()
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame.layoutParams = layoutParams
    }

    private fun observeParamsViewModel(){
        viewModel.isNouvelleDemandeFragmentSetted.observe(viewLifecycleOwner,{
            if(it){
                btnAjoutNouvelleDemande.visibility = GONE
            }else{
                btnAjoutNouvelleDemande.visibility = VISIBLE
            }
        })
        viewModel.isSecondFragmentSetted.observe(viewLifecycleOwner,{
            val orientation = resources.configuration.orientation
            if(it){
                btnRetour?.visibility = VISIBLE
                secondFragment.visibility = VISIBLE
            }else{
                btnRetour?.visibility = GONE
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    secondFragment.visibility = GONE
                }
            }
        })
        sharedViewModel.navigateToDemandeAccepteeEvent.observe(this,{
            setDemandeAccepteeFragment(it)
        })
        sharedViewModel.navigateToDemandeEnCourEvent.observe(this,{
            setDemandeEnCourFragment(it)
        })
        sharedViewModel.finishAddNouvelleDemandeEvent.observe(this,{
            removeSecondFragment()
        })
        sharedViewModel.finishDetailsDemandeEnCourEvent.observe(this,{
            removeSecondFragment()
        })
    }

    private fun setNouvelleDemandeFragment()
    {
        val fragment = NouvelleDemandeFragment()
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.secondFragment, fragment).commit()
        viewModel.isNouvelleDemandeFragmentSetted.value = true
        viewModel.isSecondFragmentSetted.value = true
    }

    private fun setDemandeAccepteeFragment(noteFrais: NoteFrais)
    {
        val fragment = DetailDemandeEnvoyeeFragment()
        val bundle = Bundle()
        bundle.putParcelable("noteFrais",noteFrais)
        fragment.arguments = bundle
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.secondFragment, fragment).commit()
        viewModel.isNouvelleDemandeFragmentSetted.value = false
        viewModel.isSecondFragmentSetted.value = true
    }

    private fun setDemandeEnCourFragment(noteFrais: NoteFrais)
    {
        val fragment = DetailDemandeNonEnvoyeeFragment()
        val bundle = Bundle()
        bundle.putParcelable("noteFrais",noteFrais)
        fragment.arguments = bundle
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.secondFragment, fragment).commit()
        viewModel.isNouvelleDemandeFragmentSetted.value = false
        viewModel.isSecondFragmentSetted.value = true
    }

    fun removeSecondFragment()
    {
        val fragmentManager = childFragmentManager
        for (fragment in fragmentManager.getFragments()) {
            if (fragment is DetailDemandeEnvoyeeFragment || fragment is NouvelleDemandeFragment || fragment is DetailDemandeEnvoyeeFragment || fragment is DetailDemandeNonEnvoyeeFragment) {
                fragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
        viewModel.isNouvelleDemandeFragmentSetted.value = false
        viewModel.isSecondFragmentSetted.value = false
    }

    companion object {
        @JvmStatic
        fun newInstance() = MesFraisTabletteFragment()
    }
}