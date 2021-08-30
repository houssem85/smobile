package fr.strada.smobile.ui_tablette.messagerie.message_predefinie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentMessagePredefinieTabletteBinding
import fr.strada.smobile.di_tablette.messagerie.message_predifinie.MessagePredefinieTabletteComponent
import fr.strada.smobile.ui.messagerie.message_predefini.MessagePredefiniAdapter
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.messagerie.nouveau_message_predefinie.NouveauMessagePredifinieTabletteFragment
import fr.strada.smobile.ui_tablette.messagerie.update_message_predifinie.UpdateMessagePredefinieFragment
import fr.strada.smobile.utils.listner.ItemMessagePredefiniListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_message_predefinie_tablette.*
import javax.inject.Inject

class MessagePredefinieTabletteFragment : DialogFragment() , ItemMessagePredefiniListener {

    private lateinit var component: MessagePredefinieTabletteComponent
    private lateinit var viewModel: MessagePredefinieTabletteViewModel
    private lateinit var binding: FragmentMessagePredefinieTabletteBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: MessagePredefiniAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_predefinie_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        //
        dialog!!.window!!.setBackgroundDrawable(null)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MessagePredefinieTabletteFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeMessages()
        if(savedInstanceState == null){
            viewModel.getMessages()
        }
    }

    private fun initComponent() {
        component = (activity as MainTabletteActivity).component. messagePredefinieTabletteComponent()
            .setContext(requireContext())
            .setOnClickListner(this)
            .build()
    }

    private  fun injectDependencies() {
        component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MessagePredefinieTabletteViewModel::class.java)
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {
        container?.setOnClickListener {
            dialog?.dismiss()
        }
        btnAddMessagePredefinie?.setOnClickListener {
            val fragement = NouveauMessagePredifinieTabletteFragment.newInstance()
            fragement.show(requireActivity().supportFragmentManager,"NouveauMessagePredifinieTabletteFragment")
            dialog?.dismiss()
        }
    }

    private fun setLayoutManagerInRecycleView() {
        recyclerView.layoutManager = layoutManager
    }

    private fun attachAdapterInRecycleView() {
        recyclerView.adapter = adapter
    }

    private fun observeMessages() {
        viewModel.messagesPredefini.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }


    override fun onClickListner(position: Int)
    {
        val message= viewModel.messagesPredefini.value!![position]
        val fragement = UpdateMessagePredefinieFragment.newInstance(message.objet!!,message.messageContent!!.replace(".",""))
        fragement.show(requireActivity().supportFragmentManager,"NouveauMessagePredifinieTabletteFragment")
        dialog?.dismiss()
    }

    override fun OnPressBtnDeleteListener(position: Int) {
        adapter.items.removeAt(position)
        adapter.notifyDataSetChanged()
    }
}