package fr.strada.smobile.ui_tablette.messagerie.boite_reception

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentBoiteReceptionTabletteBinding
import fr.strada.smobile.di_tablette.messagerie.boite_reception.BoiteReceptionTabletteComponent
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.messagerie.boite_de_reception.BoiteReceptionAdapter
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.messagerie.MessagerieTabletteFragment
import fr.strada.smobile.utils.Toggle
import fr.strada.smobile.utils.listner.ItemBoiteReceptionListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_boite_reception_tablette.*
import javax.inject.Inject

class BoiteReceptionTabletteFragment : BaseFragment() , ItemBoiteReceptionListener {

    private lateinit var component : BoiteReceptionTabletteComponent
    private lateinit var binding : FragmentBoiteReceptionTabletteBinding
    private lateinit var viewModel: BoiteReceptionTabletteViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    internal lateinit var adapter: BoiteReceptionAdapter

    @Inject
    internal lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boite_reception_tablette, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeIsMenuOpned()
        setLayoutManagerInRecycleView()
        attachAdapterInRecycleView()
        observeMessagesInBoiteReception()
        if(savedInstanceState==null){
            viewModel.getAllMessages()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BoiteReceptionTabletteFragment()
    }

    override fun initComponent()
    {
        component = (activity as MainTabletteActivity).component.boiteReceptionTabletteComponent()
                                                                .setContext(requireContext())
                                                                .setOnClickListner(this)
                                                                .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(BoiteReceptionTabletteViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
        edit_filtre_message.setOnClickListener {
           viewModel.isMenuFilterOpned.value = !viewModel.isMenuFilterOpned.value!!
        }
        txt_tous.setOnClickListener{
            viewModel.typeFilter.value = resources.getString(R.string.Tous)
            viewModel.isMenuFilterOpned.value = false
        }
        txt_message_lu.setOnClickListener{
            viewModel.typeFilter.value = resources.getString(R.string.message_lu)
            viewModel.isMenuFilterOpned.value = false
        }
        txt_message_non_lu.setOnClickListener{
            viewModel.typeFilter.value = resources.getString(R.string.Message_non_lu)
            viewModel.isMenuFilterOpned.value = false
        }
    }

    private fun observeIsMenuOpned(){
        viewModel.isMenuFilterOpned.observe(viewLifecycleOwner, {
            Toggle.toggleLayout(it, img_expand_filtre, menu_filtre_expand)
        })
    }

    override fun onClickListner(position: Int)
    {
        (parentFragment as MessagerieTabletteFragment).setSecondFragment()
    }

    override fun OnPressBtnDeleteListener(position: Int) {
        adapter.items.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun setLayoutManagerInRecycleView() {
        recycleView.layoutManager = layoutManager
    }

    private fun attachAdapterInRecycleView() {
        recycleView.adapter = adapter
    }

    private fun observeMessagesInBoiteReception() {

        viewModel.messages.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }
}