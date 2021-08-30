package fr.strada.smobile.ui_tablette.messagerie.update_message_predifinie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentUpdateMessagePredefinieBinding
import fr.strada.smobile.di_tablette.messagerie.update_message_predifinie.UpdateMessagePredefinieComponent
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_update_message_predefinie.*
import javax.inject.Inject

private const val ARG_OBJECT = "ARG_OBJECT"
private const val ARG_MESSAGE = "ARG_MESSAGE"

class UpdateMessagePredefinieFragment : DialogFragment() {

    private lateinit var component : UpdateMessagePredefinieComponent
    private lateinit var binding : FragmentUpdateMessagePredefinieBinding
    private lateinit var viewModel: UpdateMessagePredefinieViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_message_predefinie, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
        if(savedInstanceState == null)
        {
            initArguments()
        }
    }

    private fun initComponent()
    {
        component = (activity as MainTabletteActivity).component.updateMessagePredefinieComponent().setContext(requireContext()).build()
    }

    private fun injectDependencies()
    {
        component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(UpdateMessagePredefinieViewModel::class.java)
    }

    private fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {
        btnEnregister?.setOnClickListener {
           dialog?.dismiss()
        }
    }

    private fun initArguments()
    {
        if(arguments != null)
        {
            viewModel._object.value = requireArguments().getString(ARG_OBJECT)
            viewModel.messagePredifinie.value = requireArguments().getString(ARG_MESSAGE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateMessagePredefinieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_OBJECT, param1)
                    putString(ARG_MESSAGE, param2)
                }
            }
    }

    override fun onResume()
    {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = Utils.fromDpToPixels(requireActivity(), 550)
        params.height = Utils.fromDpToPixels(requireActivity(), 450)
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        //
        dialog!!.window!!.setBackgroundDrawable(null)
    }
}