package fr.strada.smobile.ui.profil

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.di.app.DaggerAppComponent
import fr.strada.smobile.di.app.FakeAppModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class ProfilFragmentTest{

    private lateinit var scenario : FragmentScenario<ProfilFragment>

    @Before
    fun setup(){
        val application = ApplicationProvider.getApplicationContext<SmobileApp>()
        val component = DaggerAppComponent.builder().appModule(FakeAppModule()).application(application).build()
        application.setComponent(component)
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun testUi(){
        onView(withId(R.id.lblMatricule)).check(matches(withText("cn")))
        onView(withId(R.id.lblNom)).check(matches(withText("daoud")))
        onView(withId(R.id.lblPrenom)).check(matches(withText("houssem")))
    }
}