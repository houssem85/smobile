package fr.strada.smobile.ui.spi.ui.tms.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.spi.ui.tms.TmsActivityViewModel
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [PaletteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaletteFragment : Fragment() {
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: TmsActivityViewModel


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
        return inflater.inflate(R.layout.fragment_palette, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaletteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = PaletteFragment()
    }
}