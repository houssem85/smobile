package fr.strada.smobile.ui.spi.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui.spi.MainActivitySpiViewModel
import fr.strada.smobile.ui.spi.ui.home.HomeSpiFragment
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.android.synthetic.main.menu_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MenuFragment : Fragment() {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    companion object {
        fun newInstance() = MenuFragment()
    }
    val list = listOf(
        HomeSpiFragment.AppItem(
            R.drawable.ic_path_5751,
            "Time",
            "Messages, notifications, Alertes"
        ),
        HomeSpiFragment.AppItem(
            R.drawable.ic_group_9515,
            "Tracking",
            "Messages, notifications, Alertes"
        ),
        HomeSpiFragment.AppItem(
            R.drawable.ic_groupe_141,
            "Tournées",
            "Messages, notifications, Alertes"
        ),
        HomeSpiFragment.AppItem(
            R.drawable.ic_path_17076,
            "Reporting",
            "Messages, notifications, Alertes"
        ),
    )

    private lateinit var viewModel: MainActivitySpiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.VISIBLE
        (activity as MainActivitySpi).image_back_main.visibility = View.VISIBLE

        recycler_app_menu.apply {
            adapter = MenuItemAdapter(list){
                when(it){
                    "Time" -> startActivity(Intent(requireContext() , MainActivity::class.java ))
                    "Tournées" -> {
                        findNavController().navigate(R.id.action_navigation_menu_to_listTournerFragment)
                    }
                    "Tracking" -> Timber.e("Tracking")
                }
            }
            layoutManager = LinearLayoutManager(requireContext())
        }


    }

    private fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(requireActivity(),providerFactory).get(MainActivitySpiViewModel::class.java)
    }


}