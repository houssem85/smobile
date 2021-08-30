package fr.strada.smobile.ui.messagerie.boite_de_reception

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentBoiteReceptionBinding
import fr.strada.smobile.di.messagerie.boite_reception.BoiteReceptionComponent
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.utils.listner.ItemBoiteReceptionListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_boite_reception.*
import javax.inject.Inject


class BoiteReceptionFragment : BaseFragment() , ItemBoiteReceptionListener {

    private lateinit var component: BoiteReceptionComponent
    private lateinit var viewModel: BoiteReceptionViewModel
    private lateinit var binding: FragmentBoiteReceptionBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    internal lateinit var adapter: BoiteReceptionAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boite_reception, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeMessagesInBoiteReception()
        viewModel.getAllMessages()
    }


    override fun initComponent() {
        component = (activity as MainActivity).component.boiteReceptionComponent()
            .setContext(requireContext())
            .setOnClickListner(this)
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(BoiteReceptionViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
    }

    private fun startAnimation() {
        val formBottomAnimation = Utils.inFromBottomAnimation()
        recycleViewBoiteReception.startAnimation(formBottomAnimation)
    }

    private fun setLayoutManagerInRecycleView() {
        recycleViewBoiteReception.layoutManager = layoutManager
    }

    private fun attachAdapterInRecycleView() {
        recycleViewBoiteReception.adapter = adapter
    }

    private fun observeMessagesInBoiteReception() {

        viewModel.messages.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    override fun onClickListner(position: Int) {
    }


    override fun OnPressBtnDeleteListener(position: Int) {
        adapter.items.removeAt(position)
        adapter.notifyDataSetChanged()
    }


}