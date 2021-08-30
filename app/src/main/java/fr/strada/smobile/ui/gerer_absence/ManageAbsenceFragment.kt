package fr.strada.smobile.ui.gerer_absence

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import fr.strada.smobile.R
import fr.strada.smobile.ui.base.BaseNFragment
import fr.strada.smobile.ui.gerer_absence.avalider.ListAbsenceAvaliderFragment
import fr.strada.smobile.ui.gerer_absence.enattente.ListeAbsenceEnAttenteFragment
import fr.strada.smobile.ui.gerer_absence.refusee.ListeAbsenceRefuseeFragment
import fr.strada.smobile.ui.gerer_absence.validee.ListeAbsenceValideeFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.utils.FilterBottomSheet
import fr.strada.smobile.utils.NonSwipeableViewPager
import kotlinx.android.synthetic.main.fragment_manage_absence.*

/**
 * A simple [Fragment] subclass.
 */
class   ManageAbsenceFragment : BaseNFragment() {

    lateinit var viewPager: NonSwipeableViewPager
    lateinit var tabFragmentAdapter: TabFragmentAdapter
    lateinit var tabLayout: TabLayout
    private val dialog by lazy { FilterBottomSheet() }
    lateinit var btnCalendarTeam: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_manage_absence, container, false)
        init(view)
        startAnimation(view)
        setupViewPager()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("HOUSSEM_TAG","onViewCreated ManageAbsenceFragment")
        displayCalendarTeam()
        setupNavigation()
    }

    private fun setupNavigation()
    {
        btnOpenMenu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }
        btnFilter.setOnClickListener {
            activity?.supportFragmentManager?.let {
                dialog.show(it, dialog.tag)
            }
        }
    }


    private fun startAnimation(view: View) {
        val calendarEquipe = view.findViewById<AppCompatButton>(R.id.btn_calendar_equipe)
        val toLeft: Animation = AnimationUtils.loadAnimation(activity, R.anim.right_to_left)
        calendarEquipe.startAnimation(toLeft)
    }


    private fun init(view: View) {
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        btnCalendarTeam = view.findViewById(R.id.btn_calendar_equipe)
    }


    private fun setupViewPager() {

        tabFragmentAdapter.addFragment(
            ListAbsenceAvaliderFragment(),
            resources.getString(R.string.a_valider)
        )                                   
        tabFragmentAdapter.addFragment(
            ListeAbsenceEnAttenteFragment(), resources.getString(R.string.en_attente)
        )
        tabFragmentAdapter.addFragment(
            ListeAbsenceValideeFragment(),
            resources.getString(R.string.valid_es)
        )
        tabFragmentAdapter.addFragment(
            ListeAbsenceRefuseeFragment(),
            resources.getString(R.string.refus_es)
        )

        viewPager.adapter = tabFragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    private fun displayCalendarTeam() {
        btnCalendarTeam.setOnClickListener {
            // (activity as MainActivity).addFragment(CalendrierEquipeFragment())
        }
    }
}
