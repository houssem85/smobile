package fr.strada.smobile.ui_tablette.pointeuse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.ui.pointeuse.jour_activitie.JourActivitieFragment
import fr.strada.smobile.ui.pointeuse.PointeuseFragment
import fr.strada.smobile.ui.pointeuse_graph.PointeuseGraphActivity
import fr.strada.smobile.utils.IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED
import kotlinx.android.synthetic.main.fragment_pointeuse_tablette.*

class PointeuseTabletteFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pointeuse_tablette, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null){
            val isMenuTypeActivitiePointeuseOpned = arguments?.getBoolean(IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED) ?: false
            val pointeuseFragment = PointeuseFragment.factory(isMenuTypeActivitiePointeuseOpned)
            childFragmentManager.beginTransaction().replace(R.id.left_fragment,pointeuseFragment).commit()
        }
        right_fragment?.let {
            val jourActivitieFragment = JourActivitieFragment()
            childFragmentManager.beginTransaction().replace(R.id.right_fragment,jourActivitieFragment).commit()
        }
        btnOpenViewGraphic.setOnClickListener {
            val intent = Intent(activity,PointeuseGraphActivity::class.java)
            startActivity(intent)
        }
        setupContainer()
    }

    fun setJourActivitieFragment(jourActivite: JourActivite)
    {
        val fragment  = JourActivitieFragment.factory(jourActivite)
        childFragmentManager.beginTransaction().replace(R.id.right_fragment,fragment).commit()
    }

    companion object {
        fun factory(isMenuActivitiePointeuseOpned: Boolean = false): PointeuseTabletteFragment {
            val fragment = PointeuseTabletteFragment()
            val args = Bundle()
            args.putBoolean(IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED, isMenuActivitiePointeuseOpned)
            fragment.arguments = args
            return fragment
        }
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = fr.strada.smobile.ui_tablette.accueil.Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu =
            fr.strada.smobile.ui_tablette.accueil.Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = container.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        container.layoutParams = layoutParams
    }
}