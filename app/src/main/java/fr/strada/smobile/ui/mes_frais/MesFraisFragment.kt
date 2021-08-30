package fr.strada.smobile.ui.mes_frais

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentMesFraisBinding
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.mes_frais.filtre.FiltreMesFraisFragment
import fr.strada.smobile.utils.KeyBoardUtils
import fr.strada.smobile.utils.Status.*
import fr.strada.smobile.utils.listner.ItemDemandeListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_mes_frais.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MesFraisFragment : BaseFragment(), ItemDemandeListener {

    private lateinit var viewModel: MesFraisViewModel
    private lateinit var sharedViewModel: SharedMesFraisViewModel
    private lateinit var binding: FragmentMesFraisBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private  lateinit var adapter: MesFraisAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mes_frais, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMesFraisRecycleView()
        observeDemandesFrais()
        observeSharedViewModelEvents()
        observeDeleteNoteFraisRespense()
        if(savedInstanceState == null){
            viewModel.getListNoteFrais()
        }
    }

    override fun initComponent() {}

    override fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(MesFraisViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), providerFactory).get(SharedMesFraisViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        btnOpenMenu?.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }
        btn_filtre?.setOnClickListener {
            showFilterFragment()
        }
        btn_add_note_frais?.setOnClickListener {
            val action = MesFraisFragmentDirections.actionMesFraisFragmentToNouvelleDemandeFragment()
            findNavController().navigate(action)
        }
        edit_recherche.setOnEditorActionListener { _, _, _ ->
            val strSearch = viewModel.strSearch.value!!.lowercase()
            if (viewModel.notesFrais.value.status == SUCCESS) {
                val dataFiltred = viewModel.notesFrais.value.data!!.filter {
                    it.titre.lowercase().contains(strSearch)
                }
                adapter.setData(dataFiltred)
                KeyBoardUtils.hideKeyboard(requireActivity())
            }
            false
        }
    }


    private fun startAnimation() {
        val formRightAnimation = Utils.inFromRightAnimation()
        val formBottomAnimation = Utils.inFromBottomAnimation()
        view_top_mes_frais.startAnimation(formRightAnimation)
        recycle_mes_frais.startAnimation(formBottomAnimation)
    }

    private fun setUpMesFraisRecycleView()
    {
        recycle_mes_frais.layoutManager = LinearLayoutManager(context)
        adapter = MesFraisAdapter(requireContext(),this,locale)
        recycle_mes_frais.adapter = adapter

    }

    private fun observeDemandesFrais() {
        lifecycleScope.launch {
           viewModel.notesFrais.collect {
               when(it.status){
                   LOADING -> {

                   }
                   ERROR -> {
                       Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                   }
                   SUCCESS -> {
                       adapter.setData(it.data!!)
                   }
                   NO_CONTENT -> Timber.e("NO_CONTENT")
               }
           }
        }
    }

    private fun observeSharedViewModelEvents()
    {
        sharedViewModel.refreshListNoteFraisEvent.observe(this,{
            viewModel.getListNoteFrais()
        })
    }

    private fun observeDeleteNoteFraisRespense()
    {
        viewModel.deleteNoteFraisRespense.observe(this,{
            when (it.status) {
                SUCCESS -> {
                    viewModel.getListNoteFrais()
                }
                ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onClickDemandeAccepteeListner(position: Int) {
        val isTabletteSize = resources.getBoolean(R.bool.isTablet)
        val noteFrais = adapter.getNoteFrais(position)
        if(!isTabletteSize){
            val action = MesFraisFragmentDirections.actionMesFraisFragmentToDetailDemandeEnvoyeeFragment(noteFrais)
            findNavController().navigate(action)
        }else {
           sharedViewModel.navigateToDemandeAcceptee(noteFrais)
        }
    }

    override fun onClickDemandeNonAccepteeListener(position: Int) {
        val isTabletteSize = resources.getBoolean(R.bool.isTablet)
        val noteFrais = adapter.getNoteFrais(position)
        if(!isTabletteSize)
        {
            val action = MesFraisFragmentDirections.actionMesFraisFragmentToDetailDemandeNonEnvoyeeFragment(noteFrais)
            findNavController().navigate(action)
        }else {
            sharedViewModel.navigateToDemandeEnCour(noteFrais)
        }
    }

    override fun onPressBtnDeleteListener(position: Int) {
        val noteFriasId = adapter.getNoteFraisId(position)
        viewModel.deleteNoteFrais(noteFriasId)
    }

    fun showFilterFragment()
    {
        val filterFragment = FiltreMesFraisFragment()
        filterFragment.setOnSbmitListner { isPriodeFilterActive, dateDebut, dateFin , isDemandeAccepteeActive , isDemandeEnCourActive ->
           var strDateChip:String? = null
            try {
                val sdfUi = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val sdfWebService = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                var strDateDebut : String? = null
                var strDateFin : String? = null
                var enCour :Boolean? = null
                if(isPriodeFilterActive){
                    val dDebut = sdfUi.parse(dateDebut)
                    val dFin  = sdfUi.parse(dateFin)
                    strDateDebut = sdfWebService.format(dDebut!!)
                    strDateFin = sdfWebService.format(dFin!!)
                }
                chip_1.visibility = View.GONE
                chip_2.visibility =View.VISIBLE
                chip_3.visibility =View.VISIBLE
                if(isDemandeAccepteeActive && isDemandeEnCourActive)
                {
                    chip_1.visibility = View.VISIBLE
                    chip_2.visibility =View.GONE
                }
                if(isDemandeEnCourActive){
                    enCour = true
                    chip_2.text = getString(R.string.demande_en_cours)
                }
                if(isDemandeAccepteeActive){
                    enCour = false
                    chip_2.text = getString(R.string.demande_acceptée)
                }
                if (!isPriodeFilterActive)
                {
                    chip_3.visibility =View.GONE

                }

                val dDebut = sdfUi.parse(dateDebut)
                strDateChip = sdfUi.format(dDebut)
                chip_3.text = "De "+strDateChip + "à ... "
                viewModel.getListNoteFrais(strDateDebut,strDateFin,enCour)
            }catch (ex:Exception){

            }
        }
        filterFragment.show(childFragmentManager, FiltreMesFraisFragment.TAG)
    }
}

