package fr.strada.smobile.ui.mes_frais.detail_depense_envoyee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentDetailDepenseEnvoyeeBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_detail_depense_envoyee.*
import javax.inject.Inject

private const val ARG_DAY = "ARG_DAY"
private const val ARG_MONTH = "ARG_MONTH"
private const val ARG_YEAR = "ARG_YEAR"
private const val ARG_TYPE = "ARG_TYPE"
private const val ARG_MONTANT = "ARG_MONTANT"

class DetailDepenseEnvoyeeFragment : BaseFragment() {

    private lateinit var binding:FragmentDetailDepenseEnvoyeeBinding
    private lateinit var viewModel:DetailDepenseEnvoyeeViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_depense_envoyee, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initComponent() {
    }

    override fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(DetailDepenseEnvoyeeViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        icBackDetailDepenseEnvoyer?.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun initArguments() {

    }

    companion object {
        @JvmStatic
        fun newInstance(day: Int, month: Int ,year:Int,type:String,montant:Double):DetailDepenseEnvoyeeFragment{
            val fragment = DetailDepenseEnvoyeeFragment()
            val args = Bundle()
            args.putInt(ARG_DAY,day)
            args.putInt(ARG_MONTH,month)
            args.putInt(ARG_YEAR,year)
            args.putString(ARG_TYPE,type)
            args.putDouble(ARG_MONTANT,montant)
            fragment.arguments = args
            return fragment
        }
    }
}