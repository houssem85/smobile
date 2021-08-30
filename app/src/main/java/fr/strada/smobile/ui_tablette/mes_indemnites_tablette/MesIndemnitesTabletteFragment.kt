package fr.strada.smobile.ui_tablette.mes_indemnites_tablette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.strada.smobile.R
import fr.strada.smobile.ui.indemnites.IndemnitesFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import kotlinx.android.synthetic.main.fragment_mes_indemnites_tablette.*

class MesIndemnitesTabletteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mes_indemnites_tablette, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState == null) {
            val fragment = IndemnitesFragment()
            changeFragment(fragment)
        }
        setupContainer()
    }

    private fun changeFragment(newFragment: Fragment)
    {
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, newFragment).commit()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MesIndemnitesTabletteFragment()
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame.layoutParams = layoutParams
    }


}