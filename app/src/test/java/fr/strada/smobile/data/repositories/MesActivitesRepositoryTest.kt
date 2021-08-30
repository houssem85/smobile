package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.activites.day.ActivitesJournaliere
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MesActivitesRepositoryTest {

    lateinit var mesActivitesRepository: MesActivitesRepository
    @Mock
    lateinit var api: Api
    @Mock
    lateinit var credentialsManager: CredentialsManager
    @Mock
    lateinit var userPreferences : UserPreferences
    @Mock
    lateinit var internetConnectionChecker: InternetConnectionChecker
    @Mock
    lateinit var context : Application

    val callback = argumentCaptor<BaseCallback<Credentials , CredentialsManagerException>>()

    @Before
    fun setUp() {
        mesActivitesRepository = MesActivitesRepositoryImpl(api,credentialsManager,userPreferences,internetConnectionChecker,context)
        Mockito.`when`(credentialsManager.getCredentials(callback.capture())).thenAnswer {
            callback.firstValue.onSuccess(Credentials("fake token",null,null,null,null))
        }
        Mockito.`when`(userPreferences.collaborateurId).thenReturn(flow { emit("1d5da10e-b39a-4a28-8816-ce431906b27a")})
    }

    @Test
    fun testGetActivitesJournaliere() = runBlockingTest {
        val list = ActivitesJournaliere(listOf(), listOf())
        Mockito.`when`(internetConnectionChecker.check()).thenReturn(true)
        Mockito.`when`(api.getActivitesJournaliere(anyString(),anyString(),anyString())).thenReturn(Response.success(list))
        val res = mesActivitesRepository.getActivitesJournaliere(anyString())
        assertThat(res.data).isEqualTo(list)
    }
}