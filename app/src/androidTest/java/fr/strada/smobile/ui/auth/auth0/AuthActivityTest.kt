package fr.strada.smobile.ui.auth.auth0

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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
class AuthActivityTest{

    lateinit var scenario: ActivityScenario<AuthActivity>

    @Before
    fun setup()
    {
        val application = ApplicationProvider.getApplicationContext<SmobileApp>()
        val fakeComponent = DaggerAppComponent.builder().appModule(FakeAppModule()).application(application).build()
        application.setComponent(fakeComponent)
        scenario = launch(AuthActivity::class.java)
    }

    @Test
    fun testLogin(){
        Espresso.onView(withId(R.id.input_nom_email)).perform(replaceText("test@test.com"))
        Espresso.onView(withId(R.id.inpute_password)).perform(replaceText("test"))
        Espresso.onView(withId(R.id.btn_auth)).perform(click())

        Espresso.onView(withId(R.id.container)).check(matches(isDisplayed()))
    }
}