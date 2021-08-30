package fr.strada.smobile.ui.infractions.detail_infraction

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.data.models.infractions.Value
import fr.strada.smobile.di.app.DaggerAppComponent
import fr.strada.smobile.di.app.FakeAppModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailInfractionFragmentTest{

    lateinit var scenario : FragmentScenario<DetailInfractionFragment>

    @Before
    fun setup()
    {
        val value = Value(3600000L)
        val infraction = Infraction("infractionId","infractionCategorieId","infractionCategorieCode","infractionCategorieLibelle","infractionLibelle","2020-10-10",value,0)
        val application  = ApplicationProvider.getApplicationContext<SmobileApp>()
        val fakeCompoment  = DaggerAppComponent.builder().appModule(FakeAppModule()).application(application).build()
        application.setComponent(fakeCompoment)
        val bundle = Bundle()
        bundle.putParcelable("infraction",infraction)
        scenario = launchFragmentInContainer(bundle,themeResId = R.style.AppTheme)
    }

    @Test
    fun test(){
       
    }
}