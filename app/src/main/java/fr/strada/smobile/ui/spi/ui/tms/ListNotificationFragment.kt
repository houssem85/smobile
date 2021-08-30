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
import fr.strada.smobile.ui.spi.ui.tms.adapter.NotificationListAdapter
import fr.strada.smobile.ui.spi.ui.tms.model.TourneeItem
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_list_notification.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ListNotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListNotificationFragment : Fragment() {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: TmsActivityViewModel

    lateinit var adapterList: NotificationListAdapter
    var listItem = ArrayList<TourneeItem>()


    private fun injectDepencencices() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(TmsActivityViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDepencencices()
        initViewModel()
        initViewRecycler()
        observeList()
    }

    private fun initViewRecycler() {
        adapterList = NotificationListAdapter(listItem) {
            val action =
                ListNotificationFragmentDirections.actionListNotificationFragment2ToDetailTourneeFragment(
                    it
                )
            findNavController().navigate(action)
        }
        recyclerI_item_tournee_tms.apply {
            adapter = adapterList
            layoutManager = LinearLayoutManager(context)
        }


    }

    private fun observeList() {
        viewModel.getListTournee()
        lifecycleScope.launch {
            viewModel.listeTournee.collect {

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
        fun newInstance() = ListNotificationFragment()
    }
}