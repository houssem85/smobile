package fr.strada.smobile.ui.pointeuse

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.data.repositories.PointeuseRepository
import fr.strada.smobile.utils.Resource
import fr.strada.smobile.utils.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PointeuseViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var pointeuseViewModel : PointeuseViewModel
    @Mock
    lateinit var application: Application
    @Mock
    lateinit var pointeuseRepository: PointeuseRepository

    @Before
    fun setup()  {
        pointeuseViewModel = PointeuseViewModel(application,pointeuseRepository)
    }

    @Test
    fun testGetJourActivities() = runBlockingTest{
        val list = listOf<JourActivite>()
        Mockito.`when`(pointeuseRepository.getLastSixDaysActivites(anyString())).thenReturn(Resource.success(list))
        pointeuseViewModel.getJourActivities("2020-10-10")
        val value = pointeuseViewModel.jourActivities.getOrAwaitValueTest(indexForReturnedValue = 2)
        assertThat(value.data).isEqualTo(list)
    }
}