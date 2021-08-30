package fr.strada.smobile.ui.infractions

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.data.repositories.InfractionRepository
import fr.strada.smobile.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class InfractionsViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var infractionsViewModel : InfractionsViewModel
    @Mock
    lateinit var application : Application
    @Mock
    lateinit var infractionRepository : InfractionRepository

    @Before
    fun setup(){
        infractionsViewModel = InfractionsViewModel(application,infractionRepository)
    }

    @Test
    fun testGetInfractionsCategories() = runBlockingTest {
        val list = listOf<InfractionsCategorie>()
        Mockito.`when`(infractionRepository.getInfractionsCategories()).thenReturn(Resource.success(list))
        infractionsViewModel.getInfractionsCategories()
        val flow = infractionsViewModel.infractionsCategories
        delay(2000)
        assertThat(flow.value.data).isEqualTo(list)
    }
}