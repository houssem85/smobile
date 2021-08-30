package fr.strada.smobile.ui_tablette.mes_activities_tablette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.strada.smobile.R
import fr.strada.smobile.ui.activities.MesActivitiesFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import kotlinx.android.synthetic.main.fragment_accueil_tablette.*
import kotlinx.android.synthetic.main.fragment_mes_activities_tablette.*

class MesActivitiesTabletteFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mes_activities_tablette, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState == null) {
            val fragment = MesActivitiesFragment()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MesActivitiesTabletteFragment.
         */
        @JvmStatic
        fun newInstance() = MesActivitiesTabletteFragment()
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