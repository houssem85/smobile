package fr.strada.smobile.ui_tablette.messagerie.nouveau_message

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentNouveauMessageBinding
import fr.strada.smobile.di.messagerie.nouveau_message.NouveauMessageComponent
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.messagerie.nouveau_message.NouveauMessageViewModel
import fr.strada.smobile.ui.messagerie.nouveau_message.TypeMessage
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.listner.ItemDestinataireListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_nouveau_message.*
import javax.inject.Inject

private const val ARG_TYPE_MESSAGE = "TYPE_MESSAGE"

class NouveauMessageTabletteFragment : DialogFragment() , ItemDestinataireListner{

    private var typeMessage: TypeMessage? = null

    private lateinit var component: NouveauMessageComponent
    private lateinit var binding: FragmentNouveauMessageBinding
    private lateinit var viewModel: NouveauMessageViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var dialog: Dialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
    }

    private fun initArgs()
    {
        arguments?.let {
            typeMessage = it.getSerializable(ARG_TYPE_MESSAGE) as TypeMessage
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nouveau_message, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun initComponent() {
        component = (activity as MainTabletteActivity).component.nouveauMessageComponent()
            .setContext(requireContext())
            .setItemDestinataireListner(this).build()
    }

    private fun injectDependencies() {
        component.inject(this)
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(NouveauMessageViewModel::class.java)
        ////
        viewModel.resetViewModel()
    }

    private fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {

        viewModel.pressSelectDestinataireEvent.observe(viewLifecycleOwner, {
            it?.let {
                dialog.show()
            }
        })
        viewModel.pressBtnEnvoyerEvent.observe(viewLifecycleOwner, {
            it?.let {
                getDialog()?.dismiss()
            }
        })

    }

    private fun startAnimation() {
        val formBottomAnimation = Utils.inFromBottomAnimation()
        content_nouveau_message.startAnimation(formBottomAnimation)
    }

    override fun onResume()
    {
        super.onResume()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }

    companion object {
        @JvmStatic
        fun newInstance(typeMessage: TypeMessage) =
            NouveauMessageTabletteFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TYPE_MESSAGE, typeMessage)
                }
            }
    }

    override fun onItemClickListner(position: Int, destinataire: String) {
        viewModel.destinataire.value = destinataire
        dialog.dismiss()
    }
}