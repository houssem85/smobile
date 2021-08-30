package fr.strada.smobile.ui.messagerie.message_predefini

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.databinding.FragmentMessagePredefiniBinding
import fr.strada.smobile.di.messagerie.message_predefini.MessagePredefiniComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.messagerie.detail_message_predefini.DetailMessagePredefiniActivity
import fr.strada.smobile.utils.listner.ItemMessagePredefiniListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_message_predefini.*
import javax.inject.Inject


class MessagePredefiniFragment : BaseFragment(), ItemMessagePredefiniListener {
    private lateinit var component: MessagePredefiniComponent
    private lateinit var viewModel: MessagePredefiniViewModel
    private lateinit var binding: FragmentMessagePredefiniBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: MessagePredefiniAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_predefini, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeMessages()
        viewModel.getMessages()
    }

    override fun initComponent() {
        component = (activity as MainActivity).component.messagePredefiniComponent()
            .setContext(requireContext())
            .setOnClickListner(this)
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(MessagePredefiniViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

    private fun setLayoutManagerInRecycleView() {
        recycleViewMessagePredefini.layoutManager = layoutManager
    }

    private fun attachAdapterInRecycleView() {
        recycleViewMessagePredefini.adapter = adapter
    }

    private fun observeMessages() {
        viewModel.messagesPredefini.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }


    override fun onClickListner(position: Int) {
        startDetailInfraction(adapter.items[position])
    }

    override fun OnPressBtnDeleteListener(position: Int) {
       adapter.items.removeAt(position)
       adapter.notifyDataSetChanged()
    }

    private fun startDetailInfraction(messageModel: MessageModel) {
        val intent = Intent(activity, DetailMessagePredefiniActivity::class.java)
        intent.putExtra("messagePredefini", messageModel.messageContent.toString())
        intent.putExtra("objet", messageModel.objet.toString())
        startActivity(intent)
    }

}

