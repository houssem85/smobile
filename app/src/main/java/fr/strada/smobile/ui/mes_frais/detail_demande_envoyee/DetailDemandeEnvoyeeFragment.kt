package fr.strada.smobile.ui.mes_frais.detail_demande_envoyee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentDetailDemandeEnvoyeeBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_detail_demande_envoyee.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DetailDemandeEnvoyeeFragment : BaseFragment(){

    private lateinit var viewModel: DetailDemandeEnvoyeeViewModel
    private lateinit var binding: FragmentDetailDemandeEnvoyeeBinding
    private lateinit var depenseDisabledAdapter: DepenseDisabledAdapter
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_demande_envoyee, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentNoteFais()
        setupRecyclerDepenses()
        observeNoteFais()
    }

    fun getArgumentNoteFais() {
        arguments?.let {
            val safeArgs = DetailDemandeEnvoyeeFragmentArgs.fromBundle(it)
            val noteFrais = safeArgs.noteFrais
            viewModel.detailNoteFrais.value = noteFrais
        }
    }

    override fun initComponent() {}

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(DetailDemandeEnvoyeeViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
        icBackDetailNotefrais?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerDepenses()
    {
        depenseDisabledAdapter = DepenseDisabledAdapter(requireContext()){

        }
        recycleDepenses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = depenseDisabledAdapter
        }
    }

    private fun observeNoteFais()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.detailNoteFrais.collect {
                depenseDisabledAdapter.setData(it!!.listDepenses)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(numero:Int): DetailDemandeEnvoyeeFragment {
            val fragment = DetailDemandeEnvoyeeFragment()
            val args = Bundle()
            args.putInt("numero", numero)
            fragment.arguments = args
            return fragment
        }
    }
}