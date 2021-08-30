package fr.strada.smobile.ui.home

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.di.app.DaggerAppComponent
import fr.strada.smobile.di.app.FakeAppModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest{

    lateinit var seanario : FragmentScenario<HomeFragment>

    @Before
    fun setup(){
        val application = ApplicationProvider.getApplicationContext<SmobileApp>()
        val fakeComponent = DaggerAppComponent.builder().appModule(FakeAppModule()).application(application).build()
        application.setComponent(fakeComponent)
        seanario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun test(){
        onView(withId(R.id.valueTempsServiceJournalie)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    "00:00"
                )
            )
        )
    }
}