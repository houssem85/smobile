package fr.strada.smobile.ui.spi.ui.tms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.spi.ui.tms.adapter.DetailTourneeAdapter
import fr.strada.smobile.ui.spi.ui.tms.model.EnlevementLivraison
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_detail_tournee.*
import kotlinx.android.synthetic.main.fragment_list_tourner.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailTourneeFragment : Fragment() {

    private lateinit var  adapterList: DetailTourneeAdapter

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: TmsActivityViewModel
    var listItem = ArrayList<EnlevementLivraison>()


    private fun injectDepencencices() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(TmsActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_tournee, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDepencencices()
        initViewModel()
        initViewRecycler()
        observeList()
    }

    private fun initViewRecycler() {
        adapterList = DetailTourneeAdapter(listItem) {
            findNavController().navigate(R.id.action_detailTourneeFragment_to_mainInformationFragment)
        }
        recyclerview_enlevmentlivreaison.apply {
            adapter = adapterList
            layoutManager = LinearLayoutManager(context)
        }


    }
    private fun getArgument() : String  {
        var id = ""
        arguments?.let {
            val safeArgs = DetailTourneeFragmentArgs.fromBundle(it)
            id = safeArgs.idTournee
        }
        return id
    }
    private fun observeList() {
        viewModel.getdetailTournee(getArgument())
        lifecycleScope.launch {
            viewModel.detailTournee.collect {

                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.SUCCESS -> {
                        listItem.clear()
                        listItem.addAll(it.data!!)
                        adapterList.notifyDataSetChanged()

                    }
                    Status.NO_CONTENT -> Timber.tag("tag").e("NO_CONTENT")
                }

            }
        }

    }

    companion object {
        fun newInstance() = DetailTourneeFragment()

    }
}