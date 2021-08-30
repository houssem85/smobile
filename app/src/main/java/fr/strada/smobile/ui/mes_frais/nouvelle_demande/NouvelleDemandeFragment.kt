package fr.strada.smobile.ui.mes_frais.nouvelle_demande

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.databinding.FragmentNouvelleDemandeBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.mes_frais.SharedMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.dialog_enregister_demande.DialogEnregisterDemande
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.dialog_envoie_demande.DialogEnvoieDemande
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.DepenseAdapter
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.NouvelleDepenseFragment
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.ItemDepenseListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_nouvelle_demande.*
import javax.inject.Inject

class NouvelleDemandeFragment : BaseFragment() , ItemDepenseListner {

    private lateinit var binding: FragmentNouvelleDemandeBinding
    private lateinit var viewModel: NouvelleDemandeViewModel
    private lateinit var sharedViewModel: SharedMesFraisViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var adapter: DepenseAdapter

    lateinit var dialogLoading : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_nouvelle_demande, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialogLoading()
        setupRecycleDepenses()
        observeDepenses()
        observeSharedEvents()
        observeIsLoading()
        observeCreateNoteFraisRespense()
    }

    private fun setupRecycleDepenses()
    {
        recycleDepenses.layoutManager = LinearLayoutManager(context)
        adapter = DepenseAdapter(requireContext(),this)
        recycleDepenses.adapter = adapter
    }

    private fun observeDepenses(){
        viewModel.depenses.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    private fun observeSharedEvents()
    {
        sharedViewModel.addNouvelleDepenseEvent.observe(requireActivity(),{
            viewModel.addDepense(it)
        })
    }

    private fun observeIsLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner,{
            if(it){
                dialogLoading.show()
            }else{
                dialogLoading.dismiss()
            }
        })
    }

    private fun observeCreateNoteFraisRespense(){
        viewModel.createNoteFraisRespense.observe(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS->{
                    sharedViewModel.refreshListNoteFrais()
                    val isTablete = resources.getBoolean(R.bool.isTablet)
                    if(!isTablete){
                        findNavController().popBackStack()
                    }else{
                       sharedViewModel.finishAddNouvelleDemande()
                    }
                }
                Status.ERROR ->{
                   Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = NouvelleDemandeFragment()
    }

    override fun initComponent()
    {}

    override fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(NouvelleDemandeViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), providerFactory).get(SharedMesFraisViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
        btn_envoyer.setOnClickListener {
            showDialogEnvoieDemande()
        }
        btn_add_depense.setOnClickListener {
            val tabletSize = resources.getBoolean(R.bool.isTablet)
            if(!tabletSize){
                val action  = NouvelleDemandeFragmentDirections.actionNouvelleDemandeFragmentToNouvelleDepenseFragment()
                findNavController().navigate(action)
            }else {
                val nouvelleDepenseFragment = NouvelleDepenseFragment()
                nouvelleDepenseFragment.show(childFragmentManager,NouvelleDepenseFragment.TAG)
            }
        }
        icBack?.setOnClickListener {
            onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,true,{
            onBackPressed()
        })
    }

    private fun showDialogEnvoieDemande(){
        val dialogEnvoieDemande = DialogEnvoieDemande()
        dialogEnvoieDemande.setBtnEnvoyerListner { dialog, email ->
            val EMAIL_REGEX = "[a-zA-Z0-9-.!#$%&'*+–/=?^_`{|}~]{1,64}@[a-z0-9-.]{1,255}"
            val isEmailValid = email.matches(EMAIL_REGEX.toRegex())
            if(isEmailValid || email.isEmpty()){
                dialog.dismiss()
                val title = viewModel.titre.value!!
                val commentaire = viewModel.commentaire.value!!
                val depenses = viewModel.depenses.value!!
                val noteFrais = NoteFrais(true,"","",commentaire,"","","",depenses,0 ,"",title)
                viewModel.createNoteFrais(noteFrais)
            }else{
                Toast.makeText(context, resources.getString(R.string.format_email_incorrecte), Toast.LENGTH_LONG).show()
            }
        }
        dialogEnvoieDemande.show(childFragmentManager,DialogEnvoieDemande.TAG)
    }

    private fun onBackPressed()
    {
        val isFormValid = viewModel.depenses.value!!.isNotEmpty() && viewModel.titre.value!!.isNotEmpty()
        if(isFormValid)
        {
            val dialogEnregisterDemande = DialogEnregisterDemande()
            dialogEnregisterDemande.setOnPressBtnNonListner {
                findNavController().popBackStack()
            }
            dialogEnregisterDemande.setOnPressBtnOuiListner {
                showDialogEnvoieDemande()
            }
            dialogEnregisterDemande.show(childFragmentManager,DialogEnregisterDemande.TAG)
        }else
        {
            findNavController().popBackStack()
        }
    }

    override fun onClickDeleteListner(position: Int)
    {
        viewModel.removeDepense(position)
    }

    override fun onClickDetailDepenseListener(position: Int) {
        // not now
    }

    private fun initDialogLoading()
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader_empty_msg, null)
        dialogBuilder.setView(dialogView)
        dialogLoading = dialogBuilder.create()
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setCancelable(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.addNouvelleDepenseEvent.removeObservers(requireActivity())
    }
}