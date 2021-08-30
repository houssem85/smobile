package fr.strada.smobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentHomeBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.pointeuse.millisToTime
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    lateinit var viewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    //--------------------------------- custom Navigation ------------------------------------//

    override fun initComponent()
    {}

    override fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(HomeViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
        btnOpenMenu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }
        btnVoirTousActivities.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mesActivitiesFragment)
        }
    }

    //-----------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActivitiesHebdomadaire()
        fetchDataFromServer()
    }

    private fun observeActivitiesHebdomadaire(){
        viewModel.activitiesHebdomadaire.observe(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS-> {
                    Timber.e(it.data!!.nuitCumul.totalMilliseconds.toString())
                    valueTempsNuitHebdomadaire.text = millisToTime(it.data.nuitCumul.totalMilliseconds)
                    valueTempsServiceHebdomadaire.text = millisToTime(it.data.serviceCumul.totalMilliseconds)
                }
            }
        })
    }


    private fun fetchDataFromServer()
    {
        if(savedInstanceState == null)
        {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val firstDayOfWeek = Utils.getFirstDayInCurrentWeek(Calendar.MONDAY, locale)
            val lastDayOfWeek = Utils.getLastDayInCurrentWeek(Calendar.MONDAY, locale)
            viewModel.getActivitiesHebdomadaire(year,month + 1,firstDayOfWeek,lastDayOfWeek)
        }
    }
}



