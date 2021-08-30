package fr.strada.smobile.data.repositories

import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.infractions.InfractionsCategorie
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class InfractionRepositoryTest{

    lateinit var infractionRepository:InfractionRepository

    @Mock
    lateinit var api : Api
    @Mock
    lateinit var internetConnectionChecker : InternetConnectionChecker
    @Mock
    lateinit var  credentialsManager : CredentialsManager
    @Mock
    lateinit var userPreferences : UserPreferences
    @Mock
    lateinit var messageFoctory : MessageFoctory

    val callback = argumentCaptor<BaseCallback<Credentials , CredentialsManagerException>>()

    @Before
    fun setup(){
        infractionRepository = InfractionRepository(api,internetConnectionChecker,credentialsManager,userPreferences,messageFoctory)
        Mockito.`when`(credentialsManager.getCredentials(callback.capture())).thenAnswer {
            callback.firstValue.onSuccess(Credentials("fake token",null,null,null,null))
        }
    }

    @Test
    fun testGetInfractionsCategories() = runBlockingTest {
        val list = listOf(InfractionsCategorie("thrrth","infraction 1","infraction"))
        Mockito.`when`(internetConnectionChecker.check()).thenReturn(true)
        Mockito.`when`(api.getInfractionsCategories(ArgumentMatchers.anyString())).thenReturn(Response.success(list))
        val res = infractionRepository.getInfractionsCategories()
        assertThat(res.data).isEqualTo(list)
    }
}