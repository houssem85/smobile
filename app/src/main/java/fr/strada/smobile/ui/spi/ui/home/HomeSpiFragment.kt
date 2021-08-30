package fr.strada.smobile.ui.spi.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui.spi.MainActivitySpiViewModel
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.OPEN_ACCUEIL
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.android.synthetic.main.home_spi_fragment.*
import timber.log.Timber
import javax.inject.Inject

class HomeSpiFragment : Fragment() {

    companion object {
        fun newInstance() = HomeSpiFragment()
    }

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var viewModel: MainActivitySpiViewModel
    val list = listOf(
        AppItem(
            R.drawable.ic_group_9515,
            "Tracking",
            "Messages, notifications, Alertes"
        ),
        AppItem(
            R.drawable.ic_path_5751,
            "Time",
            "Messages, notifications, Alertes"
        ),
        AppItem(
            R.drawable.ic_groupe_141,
            "TMS",
            "Messages, notifications, Alertes"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_spi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()

        (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.VISIBLE
        (activity as MainActivitySpi).image_back_main.visibility = View.GONE

        recycler_app.apply {
            adapter = AppItemAdapter(list) {
                when (it) {
                    "Time" -> startMainActivityMobileOrTablette()
                    "TMS" -> Timber.e("TMS")
                    "Tracking" -> findNavController().navigate(R.id.action_global_trackingFragment)
                    "TMS" -> {
                        findNavController().navigate(R.id.action_navigation_home_spi_to_listNotificationFragment2)
                    }
                    "Tracking" -> Timber.e("Tracking")
                }

            }
            layoutManager = LinearLayoutManager(requireContext())
        }

    }


    data class AppItem(
        @DrawableRes val image: Int,
        val name: String,
        val txt: String
    )

    private fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(), providerFactory).get(
            MainActivitySpiViewModel::class.java
        )
    }

    private fun startMainActivityMobileOrTablette() {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        val intent = if (tabletSize) {
            Intent(requireContext(), MainTabletteActivity::class.java)
        } else {
            Intent(requireContext(), MainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = OPEN_ACCUEIL
        startActivity(intent)
    }


}