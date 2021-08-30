package fr.strada.smobile.ui_tablette.messagerie.nouveau_message_predefinie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentNouveauMessagePredifinieTabletteBinding
import fr.strada.smobile.di_tablette.messagerie.nouveau_message_predifinie.NouveauMessagePredifinieTabletteComponent
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_nouveau_message_predifinie_tablette.*
import javax.inject.Inject

class NouveauMessagePredifinieTabletteFragment : DialogFragment() {

    private lateinit var component : NouveauMessagePredifinieTabletteComponent
    private lateinit var binding : FragmentNouveauMessagePredifinieTabletteBinding
    private lateinit var viewModel: NouveauMessagePredifinieTabletteViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nouveau_message_predifinie_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun initComponent()
    {
        component = (activity as MainTabletteActivity).component.nouveauMessagePredifinieTabletteComponent().setContext(requireContext()).build()
    }

    private fun injectDependencies() {
        component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(NouveauMessagePredifinieTabletteViewModel::class.java)
    }

    private fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {
        btnAjouter?.setOnClickListener {
           dialog?.dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NouveauMessagePredifinieTabletteFragment()
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = Utils.fromDpToPixels(requireActivity(), 550)
        params.height = Utils.fromDpToPixels(requireActivity(), 450)
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        //
        dialog!!.window!!.setBackgroundDrawable(null)
    }
}