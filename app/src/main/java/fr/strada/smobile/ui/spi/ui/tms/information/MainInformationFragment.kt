package fr.strada.smobile.ui.spi.ui.tms.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.spi.ui.tms.TmsActivityViewModel
import fr.strada.smobile.ui.spi.ui.tms.adapter.ButtonListAdapter
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_main_information.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MainInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainInformationFragment : Fragment() {
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: TmsActivityViewModel
    lateinit var adapterButton : ButtonListAdapter

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
        return inflater.inflate(R.layout.fragment_main_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDepencencices()
        initViewModel()
        initRecyclerView()
    }
    companion object {

        @JvmStatic
        fun newInstance() = MainInformationFragment()
    }

fun initRecyclerView(){
    adapterButton = ButtonListAdapter {

    }
    recycler_button.apply {
        adapter = adapterButton
        layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL,false)

    }
}
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 5}
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> ExpiditeurFragment()
                1 -> DestinaireFragment()
                2 -> PaletteFragment()
                3 -> MarchandiseFragment()
                4 -> TarifFragment()
                else -> ExpiditeurFragment()
            }
        }
    }

