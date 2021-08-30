package fr.strada.smobile.ui.infractions

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_infraction_categorie.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfractionCategorieFragment : Fragment(R.layout.fragment_infraction_categorie) {

    private var isFragmentVisible = false

    private var categorieId: String = ""

    lateinit var viewModel: InfractionsViewModel

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var adapter: InfractionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        injectDependencies()
        initViewModel()
        setUpRecyclerView()
        observeAllInfractions()
    }

    private fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            providerFactory
        ).get(InfractionsViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        adapter = InfractionsAdapter(requireContext(), this::onClick)
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter
    }

    private fun initArguments() {
        arguments?.let {
            categorieId = it.getString(CATEGORIE_ID)!!
        }
    }

    private fun onClick(position: Int) {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        val infraction = adapter.getItem(position)
        if (!tabletSize) {
            val action =
                InfractionsFragmentDirections.actionInfractionsFragmentToDetailInfractionFragment(
                    infraction
                )
            findNavController().navigate(action)
        } else {
            viewModel.setPressItemInfractionEvent(infraction)
        }
    }

    private fun observeAllInfractions() {
        lifecycleScope.launch {
            viewModel.infractions.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        var listFiltred =
                            it.data!!.filter { it.infractionCategorieId == categorieId || categorieId == TOUS_ID }
                        adapter.setData(listFiltred)
                        if (isFragmentVisible) {
                            showFirstInfractionDetailsInTabletteLandscapeMode()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        isFragmentVisible = menuVisible
        if (menuVisible) {
            showFirstInfractionDetailsInTabletteLandscapeMode()
        }
    }

    private fun showFirstInfractionDetailsInTabletteLandscapeMode() {
        try {
            val isTablette = resources.getBoolean(R.bool.isTablet)
            val orientation = resources.configuration.orientation
            val isLandScape = orientation == Configuration.ORIENTATION_LANDSCAPE
            if (isTablette && isLandScape) {
                viewModel.infractions.value.data?.let { list ->
                    var listFiltred =
                        list.filter { it.infractionCategorieId == categorieId || categorieId == TOUS_ID }
                    val isListNotEmpty = listFiltred.isNotEmpty()
                    if (isListNotEmpty) {
                        val firstInfraction = listFiltred.first()
                        viewModel.setPressItemInfractionEvent(firstInfraction)
                    } else {
                        viewModel.setPressItemInfractionEvent(null)
                    }
                }
            }
        } catch (ex: Exception) {
        }
    }

    companion object {
        const val CATEGORIE_ID = "CATEGORIE_ID"
        const val TOUS_ID = "TOUS"

        @JvmStatic
        fun newInstance(categorieId: String) =
            InfractionCategorieFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORIE_ID, categorieId)
                }
            }
    }
}