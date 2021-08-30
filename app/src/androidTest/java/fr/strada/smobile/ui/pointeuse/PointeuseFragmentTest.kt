package fr.strada.smobile.ui.pointeuse

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import fr.strada.smobile.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class PointeuseFragmentTest{

    lateinit var scenario: FragmentScenario<PointeuseFragment>

    @Before
    fun setup(){
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun test(){

    }
}