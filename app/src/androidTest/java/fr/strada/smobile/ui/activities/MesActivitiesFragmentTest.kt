package fr.strada.smobile.ui.activities

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.di.app.DaggerAppComponent
import fr.strada.smobile.di.app.FakeAppModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MesActivitiesFragmentTest{

    lateinit var scenario : FragmentScenario<MesActivitiesFragment>

    @Before
    fun setup() {
        val sMobileApp = ApplicationProvider.getApplicationContext<SmobileApp>()
        val fakeComponent = DaggerAppComponent.builder().appModule(FakeAppModule()).application(sMobileApp).build()
        sMobileApp.setComponent(fakeComponent)
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun test(){

    }

}