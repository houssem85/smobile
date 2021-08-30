package fr.strada.smobile

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import fr.strada.smobile.ui.activities.MesActivitiesFragment
import fr.strada.smobile.ui.activities.journalier.MyDailyActivitiesFragment
import fr.strada.smobile.ui.home.HomeFragment
import fr.strada.smobile.ui.indemnites.IndemnitesFragment
import fr.strada.smobile.ui.indemnites.journalier.IndemniteJournalierFragment
import fr.strada.smobile.ui.infractions.InfractionsFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.mes_frais.MesFraisFragment
import fr.strada.smobile.ui.mes_frais.detail_demande_envoyee.DetailDemandeEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.detail_demande_non_envoyee.DetailDemandeNonEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.detail_depense.DetailDepenseFragment
import fr.strada.smobile.ui.mes_frais.detail_depense_envoyee.DetailDepenseEnvoyeeFragment
import fr.strada.smobile.ui.mes_frais.nouvelle_demande.NouvelleDemandeFragment
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.NouvelleDepenseFragment
import fr.strada.smobile.ui.pointeuse.PointeuseFragment
import fr.strada.smobile.ui.profil.ProfilFragment
import fr.strada.smobile.ui.reglage.ReglageFragment
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicReference


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {

    @Rule
    @JvmField
    // Given I'm in the authentification activity
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun intentsInit() { // initialize Espresso Intents capturing
        Intents.init()
    }

    @After
    fun intentsTeardown() { // release Espresso Intents capturing
        Intents.release()
    }

    @Test
    fun homeFragment() {
        val fragment = HomeFragment()
        changeFragment(fragment)
    }

    /*   @Test
       fun absenceHistoryFragment() {
           val fragment = AbsenceHistoryFragment()
           changeFragment(fragment)
       }
   */
    @Test
    fun mesActivitiesFragment() {
        val fragment = MesActivitiesFragment()
        changeFragment(fragment)
        Thread.sleep(3000)
        onView(withId(R.id.btnHebdomadaire)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.btnTerminer)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnMensuel)).perform(click())
        Thread.sleep(2000)
    }

    @Test
    fun myDailyActivitiesFragment() {
        val fragment = MyDailyActivitiesFragment()
        changeFragment(fragment)
    }

    /* @Test
     fun indemnitesFragmentFragment() {
         val fragment = IndemnitesFragment()
         changeFragment(fragment)
         Thread.sleep(2000);
         onView(withId(R.id.btnMensuel)).perform(click())
         Thread.sleep(2000);
         onView(withId(R.id.btnHebdomadaire)).perform(click())
         Thread.sleep(2000);
         onView(withId(R.id.btnTerminer)).perform(click())
         Thread.sleep(2000);
     }

     @Test
     fun indemniteJournalierFragment() {
         val fragment = IndemniteJournalierFragment()
         changeFragment(fragment)
     }

     @Test
     fun infractionsFragmentFragment() {
         val fragment = InfractionsFragment()
         changeFragment(fragment)
     }
 */
/*    @Test
    fun tousInfractionsFragment() {
        val fragment = TousInfractionsFragment()
        changeFragment(fragment)
    }

    @Test
    fun tempsDeReposInfractionsFragment() {
        val fragment = TempsDeReposInfractionsFragment()
        changeFragment(fragment)
    }

    @Test
    fun tempsDeConduiteInfractionsFragment() {
        val fragment = TempsDeConduiteInfractionsFragment()
        changeFragment(fragment)
    }

    @Test
    fun tempsDePauseInfractionsFragment() {
        val fragment = TempsDePauseInfractionsFragment()
        changeFragment(fragment)
    }*/

    /*  @Test
      fun manageAbsenceFragment() {
          val fragment = ManageAbsenceFragment()
          val fragment1 = ListeAbsenceEnAttenteFragment()
          val fragment2 = ListAbsenceAvaliderFragment()
          val fragment3 = ListeAbsenceRefuseeFragment()
          val fragment4 = ListeAbsenceValideeFragment()
          changeFragment(fragment)
          Thread.sleep(2000);
          onView(withId(R.id.btn_calendar_equipe)).perform(click())
          changeFragment(fragment1)
          changeFragment(fragment2)
          changeFragment(fragment3)
          changeFragment(fragment4)
      }

      @Test
      fun soldeAbsenceFragment() {
          val fragment = SoldeAbsenceFragment()
          changeFragment(fragment)
      }*/

    @Test
    fun profilFragment() {
        val fragment = ProfilFragment()
        changeFragment(fragment)
        Thread.sleep(2000)
        onView(withId(R.id.appCompatImageView2)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.menu_frais)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.txt_reglage)).perform(click())
        Thread.sleep(1000)
    }
    @Test
    fun settingFragment() {
        val fragment = ReglageFragment()
        changeFragment(fragment)
        Thread.sleep(2000)
        onView(withId(R.id.switch_synchro)).perform(click())
        Thread.sleep(2000)
        if (viewExists(R.id.btnActiver)) {
            onView(withId(R.id.btnActiver)).perform(click())
        }
        else{
            onView(withId(R.id.switch_synchro)).perform(click())
            Thread.sleep(2000)
            onView(withId(R.id.btnActiver)).perform(click())
        }
        Thread.sleep(2000)


    }
/*
    @Test
    fun messagerieFragment() {
        val fragment = MessagerieFragment()
        changeFragment(fragment)
        Thread.sleep(2000);
        onView(withId(R.id.edit_filtre_message)).perform(click())
        Thread.sleep(2000);
        onView(withId(R.id.btn_add_message)).perform(click())
    }

    @Test
    fun messagerieDetailFragment() {
        val fragment = MessagePredefiniFragment()
        val fragment1 = BoiteReceptionFragment()
        changeFragment(fragment)
        Thread.sleep(2000)
        changeFragment(fragment1)
        Thread.sleep(2000)
    }*/

    @Test
    fun fraisFragment() {
        val fragment = MesFraisFragment()
        val fragment1 = NouvelleDepenseFragment()
        val fragment2 = NouvelleDemandeFragment()
        val fragment3 = DetailDepenseEnvoyeeFragment()
        val fragment4 = DetailDepenseFragment()
        val fragment5 = DetailDemandeNonEnvoyeeFragment()
        val fragment6 = DetailDemandeEnvoyeeFragment()
        changeFragment(fragment)
        Thread.sleep(2000)
        onView(withId(R.id.btn_filtre)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.switchPeriode)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.imgCalendarDateDebut)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnTerminer)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.btnAnnuler)).perform(click())
        Thread.sleep(1000)
        changeFragment(fragment1)
        Thread.sleep(2000)
        changeFragment(fragment2)
        Thread.sleep(2000)
        changeFragment(fragment3)
        Thread.sleep(2000)
        changeFragment(fragment4)
        Thread.sleep(2000)
        changeFragment(fragment5)
        Thread.sleep(2000)
        changeFragment(fragment6)
    }

    @Test
    fun timeClockFragment() {
        val fragment = PointeuseFragment()
        changeFragment(fragment)
        Thread.sleep(2000)
        onView(withId(R.id.btnOpenViewGraphic)).perform(click())

    }


    private fun changeFragment(newFragment: Fragment)
    {
        val fragmentManager = mActivityTestRule.activity.supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, newFragment)
            .commitAllowingStateLoss()
    }

    fun clickIn(x: Int, y: Int): ViewAction {
        return GeneralClickAction(
            Tap.SINGLE,
            { view ->
                val screenPos = IntArray(2)
                view?.getLocationOnScreen(screenPos)

                val screenX = (screenPos[0] + x).toFloat()
                val screenY = (screenPos[1] + y).toFloat()

                floatArrayOf(screenX, screenY)
            },
            Press.FINGER,
            InputDevice.SOURCE_MOUSE,
            MotionEvent.BUTTON_PRIMARY)
    }

    fun viewExists(id: Int): Boolean {
        return try {
            onView(withId(id)).exists()
        } catch (e: RuntimeException) {
            false
        }
    }

    fun ViewInteraction.exists(): Boolean {
        val viewExists = AtomicReference<Boolean>()

        this.perform(object : ViewAction {
            override fun perform(uiController: UiController?, view: View?) {
                viewExists.set(view != null)
            }

            override fun getConstraints(): Matcher<View>? {
                return Matchers.allOf(
                    ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE),
                    ViewMatchers.isAssignableFrom(View::class.java))
            }

            override fun getDescription(): String {
                return "check if view exists"
            }
        })
        return viewExists.get()
    }
}