package fr.strada.smobile.ui_tablette.messagerie.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageConversationModel
import fr.strada.smobile.databinding.FragmentConversationBinding
import fr.strada.smobile.di_tablette.messagerie.conversation.ConversationComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.messagerie.boite_reception.TypeMessage
import fr.strada.smobile.ui_tablette.messagerie.message_predefinie.MessagePredefinieTabletteFragment
import fr.strada.smobile.utils.KeyBoardUtils
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_conversation.*
import javax.inject.Inject

class ConversationFragment : BaseFragment() {

    private lateinit var component : ConversationComponent
    private lateinit var binding : FragmentConversationBinding
    private lateinit var viewModel: ConversationViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var adapter: ConversationAdapter
    @Inject
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initComponent()
    {
        component = (activity as MainTabletteActivity).component.conversationComponent().setContext(requireContext()).build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(ConversationViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
        btnSendMessage.setOnClickListener {
            val list = ArrayList<MessageConversationModel>()
            val strMessage = viewModel.message.value!!
            if (strMessage.trim() != "")
            {
                list.add(MessageConversationModel(strMessage,TypeMessage.ENVOYEE))
                list.addAll(viewModel.messages.value!!)
                viewModel.messages.value = list
                recyclerView.scrollToPosition(0)
                viewModel.message.value = ""
                KeyBoardUtils.hideKeyboard(requireActivity())
            }
        }
        btnMessagesPredifinie.setOnClickListener {
             val fragment = MessagePredefinieTabletteFragment.newInstance()
             fragment.show(childFragmentManager,"MessagePredefinieTabletteFragment")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutManagerInRecycleView()
        attachAdapterToRecycleView()
        observeMessages()
        if(savedInstanceState == null){
            viewModel.getMessagesConversation()
        }
    }

    private fun setLayoutManagerInRecycleView()
    {
        recyclerView.layoutManager = layoutManager
    }

    private fun attachAdapterToRecycleView()
    {
        recyclerView.adapter = adapter
    }

    private fun observeMessages()
    {
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConversationFragment()
    }
}