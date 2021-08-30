package fr.strada.smobile.ui_tablette.messagerie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentMessagerieTabletteBinding
import fr.strada.smobile.di_tablette.messagerie.MessagerieTabletteComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.messagerie.nouveau_message.TypeMessage
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.messagerie.boite_reception.BoiteReceptionTabletteFragment
import fr.strada.smobile.ui_tablette.messagerie.conversation.ConversationFragment
import fr.strada.smobile.ui_tablette.messagerie.nouveau_message.NouveauMessageTabletteFragment
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_messagerie_tablette.*
import javax.inject.Inject

class MessagerieTabletteFragment : BaseFragment() {

    private lateinit var component : MessagerieTabletteComponent
    private lateinit var binding : FragmentMessagerieTabletteBinding
    private lateinit var viewModel: MessagerieTabletteViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messagerie_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initComponent()
    {
        component = (activity as MainTabletteActivity).component.messagerieTabletteComponent().setContext(requireContext()).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MessagerieTabletteViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
       btnRetour?.setOnClickListener {
          removeSecondFragment()
       }
       btnNouveauMessage?.setOnClickListener {
           val fragment = NouveauMessageTabletteFragment.newInstance(TypeMessage.NOUVEAU)
           fragment.show(childFragmentManager,"NouveauMessageTabletteFragment")
       }
        appCompatButtonNouveauMessage?.setOnClickListener {
            val fragment = NouveauMessageTabletteFragment.newInstance(TypeMessage.NOUVEAU)
            fragment.show(childFragmentManager,"NouveauMessageTabletteFragment")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MessagerieTabletteFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null)
        {
            setFirstFragment()
        }
        setupContainer()
    }

    private fun setFirstFragment()
    {
        val fragment = BoiteReceptionTabletteFragment.newInstance()
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.firstFragment, fragment).commit()
    }

    fun setSecondFragment()
    {
        val fragment = ConversationFragment.newInstance()
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.secondFragment, fragment).commit()
        viewModel.isConversationActive.value = true
    }

    private fun removeSecondFragment()
    {
        val fragmentManager = childFragmentManager
        for (fragment in fragmentManager.fragments) {
            if (fragment is ConversationFragment) {
                fragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
        viewModel.isConversationActive.value = false
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame.layoutParams = layoutParams
    }

}