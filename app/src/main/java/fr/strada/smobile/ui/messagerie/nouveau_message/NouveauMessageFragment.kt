package fr.strada.smobile.ui.messagerie.nouveau_message

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentNouveauMessageBinding
import fr.strada.smobile.di.messagerie.nouveau_message.NouveauMessageComponent
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.utils.KeyBoardUtils
import fr.strada.smobile.utils.listner.ItemDestinataireListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_nouveau_message.*
import javax.inject.Inject

private const val ARG_TYPE_MESSAGE = "TYPE_MESSAGE"

class NouveauMessageFragment : BaseFragment() , ItemDestinataireListner {

    private var typeMessage: TypeMessage? = null

    private lateinit var component:NouveauMessageComponent
    private lateinit var binding:FragmentNouveauMessageBinding
    private lateinit var viewModel: NouveauMessageViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
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

    override fun initComponent() {
        component = (activity as MainActivity).component.nouveauMessageComponent()
                                                        .setContext(requireContext())
                                                        .setItemDestinataireListner(this).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(NouveauMessageViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
         viewModel.pressBtnBackEvent.observe(viewLifecycleOwner, {
             KeyBoardUtils.hideKeyboard(requireActivity())
             (activity as MainActivity).onBackPressed()
         })
         viewModel.pressSelectDestinataireEvent.observe(viewLifecycleOwner, {
            dialog.show()
         })
        viewModel.pressBtnEnvoyerEvent.observe(viewLifecycleOwner, {
            (activity as MainActivity).onBackPressed()
        })

    }

    private fun startAnimation() {
        val formBottomAnimation = Utils.inFromBottomAnimation()
        content_nouveau_message.startAnimation(formBottomAnimation)
    }

    companion object {
        @JvmStatic
        fun newInstance(typeMessage: TypeMessage) =
            NouveauMessageFragment().apply {
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