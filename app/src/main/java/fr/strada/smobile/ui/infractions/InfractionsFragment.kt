package fr.strada.smobile.ui.infractions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.ui.gerer_absence.TabFragmentAdapter
import fr.strada.smobile.ui.infractions.filtre.FiltreFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_infractions.*
import kotlinx.android.synthetic.main.fragment_infractions.btnOpenMenu
import kotlinx.android.synthetic.main.fragment_infractions.chip_1
import kotlinx.android.synthetic.main.fragment_infractions.chip_2
import kotlinx.android.synthetic.main.fragment_infractions.chip_3
import kotlinx.android.synthetic.main.fragment_mes_frais.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class InfractionsFragment : Fragment(R.layout.fragment_infractions) {

    lateinit var viewModel : InfractionsViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        setupNavigation()
        observeInfractionsCategories()
        if(savedInstanceState == null)
        {
            viewModel.getInfractionsCategories()
            viewModel.getInfractions()
        }
    }

    private fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(requireActivity(), providerFactory).get(InfractionsViewModel::class.java)
    }

    private fun setupNavigation()
    {
        btnOpenMenu?.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }
        btnFilter?.setOnClickListener {
            showFilterFragment()
        }
    }

    private fun observeInfractionsCategories()
    {
        lifecycleScope.launch {
            viewModel.infractionsCategories.collect {
                when(it.status){
                    Status.SUCCESS ->{
                        setUpViewPagerWithFragment(it.data!!)
                    }
                    Status.ERROR ->{
                        Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setUpViewPagerWithFragment(infractionsCategorie : List<InfractionsCategorie>)
    {
        val tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        val tousFragment = InfractionCategorieFragment.newInstance(InfractionCategorieFragment.TOUS_ID)
        tabFragmentAdapter.addFragment(tousFragment,getString(R.string.Tous))
        infractionsCategorie.forEach {
            val fragment = InfractionCategorieFragment.newInstance(it.infractionCategorieId)
            tabFragmentAdapter.addFragment(fragment,it.infractionCategorieLibelle)
        }
        viewPager.offscreenPageLimit = infractionsCategorie.size
        viewPager.adapter = tabFragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    fun showFilterFragment()
    {
        val filterFragment = FiltreFragment()
        filterFragment.setOnSbmitListner { isPriodeFilterActive, dateDebut, dateFin , isCategorieClassFilterActive , infractionClass ->
            try {
              val sdfUi = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
              val sdfWebService = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
              var strDateDebut : String? = null
              var strDateFin : String? = null
              var infraClass : Int? = null
              if(isPriodeFilterActive){
                  val dDebut = sdfUi.parse(dateDebut)
                  val dFin  = sdfUi.parse(dateFin)
                  strDateDebut = sdfWebService.format(dDebut!!)
                  strDateFin = sdfWebService.format(dFin!!)
              }
              if(isCategorieClassFilterActive){
                  infraClass =  infractionClass
              }
                chip_1.visibility = View.GONE
                chip_2.visibility =View.VISIBLE
                chip_3.visibility =View.VISIBLE
                if (infraClass == 0) {
                    chip_2.text = "class 4"
                    chip_3.text = "class 5"
                }
                else
                chip_2.text = "class $infraClass"
                viewModel.getInfractions(strDateDebut,strDateFin,infraClass)
            }catch (ex:Exception){ }
        }
        filterFragment.show(childFragmentManager, FiltreFragment.TAG)
    }
}