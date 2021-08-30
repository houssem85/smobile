package fr.strada.smobile.ui.spi.ui.doc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.android.synthetic.main.doc_fragment.*
import timber.log.Timber
import javax.inject.Inject

class DocFragment : Fragment() {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var viewModel: DocViewModel

    private lateinit var documentAdapter: DocumentsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.doc_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        observeDocuments()
        (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.VISIBLE
        (activity as MainActivitySpi).image_back_main.visibility = View.VISIBLE
        viewModel.getPdf()

        fab_doc_add.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_doc_to_ajoutDocFragment)
        }


    }

    private fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(), providerFactory).get(
            DocViewModel::class.java
        )
    }

    private fun observeDocuments() {
        documentAdapter = DocumentsAdapter(requireContext())
        recycler_doc.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.documents.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.tag("success").i("success")
                    documentAdapter.setData(it.data!!)

                }
                Status.LOADING -> {
                    Timber.tag("loading").i("loading")


                }
                Status.ERROR -> {
                    Timber.tag("fail").i("fail")


                }
            }
        })

    }


}