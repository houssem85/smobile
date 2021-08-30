package fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee

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
import fr.strada.smobile.data.models.mes_frais.UpdateDepense
import fr.strada.smobile.databinding.FragmentDetailDemandeNonEnvoyeeBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.mes_frais.SharedMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.detail_depense.DetailDepenseFragment
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.dialog_enregister_demande.DialogEnregisterDemande
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.dialog_envoie_demande.DialogEnvoieDemande
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.DepenseAdapter
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.NouvelleDepenseFragment
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.ItemDepenseListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_detail_demande_non_envoyee.*
import kotlinx.android.synthetic.main.fragment_nouvelle_demande.*
import javax.inject.Inject


class DetailDemandeNonEnvoyeeFragment : BaseFragment(), ItemDepenseListner {

    private lateinit var viewModel: DetailDemandeNonEnvoyeeViewModel
    private lateinit var binding: FragmentDetailDemandeNonEnvoyeeBinding
    private lateinit var sharedViewModel: SharedMesFraisViewModel

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    lateinit var depenseAdapter: DepenseAdapter

    lateinit var dialogLoading: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail_demande_non_envoyee,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleDepenses()
        initDialogLoading()
        observeSharedEvents()
        observeIsLoading()
        observeUpdateNoteFraisRespense()
        if (savedInstanceState == null) {
            getArgumentNoteFais()
        }
        observeNoteFais()
    }

    fun getArgumentNoteFais() {
        arguments?.let {
            val safeArgs = DetailDemandeNonEnvoyeeFragmentArgs.fromBundle(it)
            val noteFrais = safeArgs.noteFrais
            viewModel.detailNoteFrais.value = noteFrais
        }
    }

    private fun observeNoteFais()
    {
        viewModel.detailNoteFrais.observe(viewLifecycleOwner, {
            it?.let {
                val depenses = it.listDepenses
                depenseAdapter.setData(depenses)
            }
        })
    }


    override fun initComponent() {}

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            providerFactory
        ).get(DetailDemandeNonEnvoyeeViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            providerFactory
        ).get(SharedMesFraisViewModel::class.java)

    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    private fun observeIsLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                dialogLoading.show()
            } else {
                dialogLoading.dismiss()
            }
        })
    }


    override fun setupNavigation() {
        btn_envoyer_detail.setOnClickListener {
            showDialogEnvoieDemande()
        }
        btn_add_depense_detail.setOnClickListener {
            val isTablete  = resources.getBoolean(R.bool.isTablet)
            if(!isTablete){
                val action = DetailDemandeNonEnvoyeeFragmentDirections.actionDetailDemandeNonEnvoyeeFragmentToNouvelleDepenseFragment()
                findNavController().navigate(action)
            }else{
                val nouvelleDepenseFragment = NouvelleDepenseFragment()
                nouvelleDepenseFragment.show(childFragmentManager, NouvelleDepenseFragment.TAG)
            }
        }
        icBackDetail?.setOnClickListener {
            onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, {
            onBackPressed()
        })
    }

    private fun setupRecycleDepenses() {
        depenseAdapter = DepenseAdapter(requireContext(), this@DetailDemandeNonEnvoyeeFragment)
        recycleDepensesDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = depenseAdapter
        }
    }

    private fun observeSharedEvents() {
        sharedViewModel.addNouvelleDepenseEvent.observe(requireActivity(), {
            viewModel.addDepense(it)
        })
        sharedViewModel.updateDepenseEvent.observe(requireActivity(), {
            viewModel.updateDepense(it)
        })
    }

    private fun showDialogEnvoieDemande() {
        val dialogEnvoieDemande = DialogEnvoieDemande()
        dialogEnvoieDemande.setBtnEnvoyerListner { dialog, email ->
            val EMAIL_REGEX = "[a-zA-Z0-9-.!#$%&'*+–/=?^_`{|}~]{1,64}@[a-z0-9-.]{1,255}"
            val isEmailValid = email.matches(EMAIL_REGEX.toRegex())
            val isEmailEmpty  = email.isEmpty()
            if (isEmailValid || isEmailEmpty) {
                dialog.dismiss()
                val noteFrais = viewModel.detailNoteFrais.value!!
                viewModel.updateNoteFrais(noteFrais)
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.format_email_incorrecte),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        dialogEnvoieDemande.show(childFragmentManager, DialogEnvoieDemande.TAG)
    }

    private fun onBackPressed() {
        val isFormValid = viewModel.detailNoteFrais.value!!.listDepenses.isNotEmpty()
        if (isFormValid) {
            val dialogEnregisterDemande = DialogEnregisterDemande()
            dialogEnregisterDemande.setOnPressBtnNonListner {
                findNavController().popBackStack()
            }
            dialogEnregisterDemande.setOnPressBtnOuiListner {
                showDialogEnvoieDemande()
            }
            dialogEnregisterDemande.show(childFragmentManager, DialogEnregisterDemande.TAG)
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onClickDeleteListner(position: Int) {
        viewModel.removeDepense(position)
    }

    override fun onClickDetailDepenseListener(position: Int) {
        val isTablete = resources.getBoolean(R.bool.isTablet)
        val depense = depenseAdapter.getDepense(position)
        val updateDepense = UpdateDepense(position, depense)
        if(!isTablete){
            val action = DetailDemandeNonEnvoyeeFragmentDirections.actionDetailDemandeNonEnvoyeeFragmentToDetailDepenseFragment(updateDepense)
            findNavController().navigate(action)
        }else{
            val bundle = Bundle()
            bundle.putParcelable("updateDepense",updateDepense)
            val fragment = DetailDepenseFragment()
            fragment.arguments = bundle
            fragment.show(childFragmentManager,DetailDepenseFragment.TAG)
        }
    }
    private fun initDialogLoading() {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader_empty_msg, null)
        dialogBuilder.setView(dialogView)
        dialogLoading = dialogBuilder.create()
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setCancelable(false)
    }
    private fun observeUpdateNoteFraisRespense()
    {
        viewModel.updateNoteFraisRespense.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    sharedViewModel.refreshListNoteFrais()
                    val isTablete  = resources.getBoolean(R.bool.isTablet)
                    if(isTablete)
                    {
                        sharedViewModel.finishDetailsDemandeEnCour()
                    }else{
                        findNavController().popBackStack()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.addNouvelleDepenseEvent.removeObservers(requireActivity())
        sharedViewModel.updateDepenseEvent.removeObservers(requireActivity())
    }
}

