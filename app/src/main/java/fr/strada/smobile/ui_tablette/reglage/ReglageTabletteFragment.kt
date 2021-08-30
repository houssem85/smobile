package fr.strada.smobile.ui_tablette.reglage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.strada.smobile.R
import fr.strada.smobile.ui.reglage.ReglageFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import kotlinx.android.synthetic.main.fragment_reglage_tablette.*

class ReglageTabletteFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reglage_tablette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null)
        {
            setReglageFragment()
        }
        setupContainer()
    }

    private fun setReglageFragment()
    {
        val fragment = ReglageFragment.newInstance()
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReglageTabletteFragment()
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = parent.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        parent.layoutParams = layoutParams
    }
}