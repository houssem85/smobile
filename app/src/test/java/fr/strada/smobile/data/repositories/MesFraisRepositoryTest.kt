package fr.strada.smobile.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.mes_frais.NoteFrais
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.MessageFoctory
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MesFraisRepositoryTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mesFraisRepository: MesFraisRepository
    @Mock
    lateinit var api: Api
    @Mock
    lateinit var credentialsManager: CredentialsManager
    @Mock
    lateinit var userPreferences: UserPreferences
    @Mock
    lateinit var internetConnectionChecker: InternetConnectionChecker
    @Mock
    lateinit var messageFoctory : MessageFoctory

    val callback = argumentCaptor<BaseCallback<Credentials , CredentialsManagerException>>()

    @Before
    fun setup(){
        mesFraisRepository = MesFraisRepository(api,credentialsManager,userPreferences,internetConnectionChecker,messageFoctory)
        Mockito.`when`(credentialsManager.getCredentials(callback.capture())).thenAnswer {
            callback.firstValue.onSuccess(Credentials("fake token",null,null,null,null))
        }
        Mockito.`when`(userPreferences.collaborateurId).thenReturn(flow { emit("5ca8c7a7-a9d5-4f48-8e5d-0ea995926892") })
    }

    @Test
    fun testGetListNoteFrais() = runBlockingTest {
       val list = listOf<NoteFrais>()
        Mockito.`when`(internetConnectionChecker.check()).thenReturn(true)
       Mockito.`when`(api.getListNoteFrais(anyString(),anyString(),anyString(),anyString(),anyBoolean())).thenReturn(Response.success(list))
       val res = mesFraisRepository.getListNoteFrais("2021-01-01","2021-01-01",true)
       assertThat(res.data).isEqualTo(list)
    }

}