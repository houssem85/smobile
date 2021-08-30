package fr.strada.smobile

import androidx.fragment.app.Fragment
import androidx.test.espresso.intent.Intents
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import fr.strada.smobile.ui_tablette.accueil.AccueilTabletteFragment
import fr.strada.smobile.ui_tablette.accueil.statistique.StatistiqueFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.GererLesAbsencesTabletteFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.detail_demande_absence.DetailDemandeAbsenceFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_a_valider.AbsenceAValiderFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_en_attente.AbsenceEnAttenteFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_refusee.AbsenceRefuseeFragment
import fr.strada.smobile.ui_tablette.gerer_les_absences_tablette.gerer_absence.absence_validee.AbsenceValideeFragment
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.mes_absences_tablette.MesAbsencesTabletteFragment
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.DetailMesAbsencesFragment
import fr.strada.smobile.ui_tablette.mes_absences_tablette.detail_mes_absences.demande_absence_en_attente.DemandeAbsenceEnAttenteFragment
import fr.strada.smobile.ui_tablette.mes_absences_tablette.solde_absence_tablette.SoldeAbsenceTabletteFragment
import fr.strada.smobile.ui_tablette.pointeuse.PointeuseTabletteFragment
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TabletteActivityTest {

    @Rule
    @JvmField
    // Given I'm in the authentification activity
    var mActivityTestRule = ActivityTestRule(MainTabletteActivity::class.java)

    @Before
    fun intentsInit() { // initialize Espresso Intents capturing
        Intents.init()
    }

    @After
    fun intentsTeardown() { // release Espresso Intents capturing
        Intents.release()
    }

    @Test
    fun tabletteFragments() {
        val fragment = AccueilTabletteFragment()
        val fragment1 = StatistiqueFragment()
        val fragment2 = GererLesAbsencesTabletteFragment()
        val fragment3 = DetailDemandeAbsenceFragment()
     //   val fragment4 = GraphPointeuseTabletteFragment()
        val fragment5 = MesAbsencesTabletteFragment()
        val fragment6 = DetailMesAbsencesFragment()
        val fragment7 = DemandeAbsenceEnAttenteFragment()
        val fragment8 = SoldeAbsenceTabletteFragment()
        val fragment9 = PointeuseTabletteFragment()
        val fragment10 = AbsenceAValiderFragment()
        val fragment11 = AbsenceEnAttenteFragment()
        val fragment12 = AbsenceRefuseeFragment()
        val fragment13 = AbsenceValideeFragment()
        changeFragment(fragment)
        Thread.sleep(2000)
        changeFragment(fragment1)
        Thread.sleep(2000)
        changeFragment(fragment2)
        Thread.sleep(2000)
        changeFragment(fragment3)
        Thread.sleep(2000)
        Thread.sleep(2000)
        changeFragment(fragment5)
        Thread.sleep(2000)
        changeFragment(fragment6)
        Thread.sleep(2000)
        changeFragment(fragment7)
        Thread.sleep(2000)
        changeFragment(fragment8)
        Thread.sleep(2000)
        changeFragment(fragment9)
        Thread.sleep(2000)
        changeFragment(fragment10)
        Thread.sleep(2000)
        changeFragment(fragment11)
        Thread.sleep(2000)
        changeFragment(fragment12)
        Thread.sleep(2000)
        changeFragment(fragment13)
    }



    private fun changeFragment(newFragment: Fragment)
    {
        val fragmentManager = mActivityTestRule.activity.supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
    }
}