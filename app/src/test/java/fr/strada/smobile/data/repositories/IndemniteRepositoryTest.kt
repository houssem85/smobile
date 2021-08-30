package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.indemnite.journalier.IndemniteJournalier
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class IndemniteRepositoryTest {

    lateinit var indemniteRepository : IndemniteRepository
    @Mock
    lateinit var api : Api
    @Mock
    lateinit var credentialsManager : CredentialsManager
    @Mock
    lateinit var userPreferences : UserPreferences
    @Mock
    lateinit var internetConnectionChecker : InternetConnectionChecker
    @Mock
    lateinit var context : Application

    val callback = argumentCaptor<BaseCallback<Credentials , CredentialsManagerException>>()

    @Before
    fun setUp() {
        indemniteRepository = IndemniteRepository(api,credentialsManager,userPreferences,internetConnectionChecker,context)
        Mockito.`when`(credentialsManager.getCredentials(callback.capture())).thenAnswer {
            callback.firstValue.onSuccess(Credentials("fake token",null,null,null,null))
        }
        Mockito.`when`(userPreferences.collaborateurId).thenReturn(flow { emit("1d5da10e-b39a-4a28-8816-ce431906b27a")})
    }

    @Test
    fun testGetIndemnitejournalier() = runBlockingTest {
        val indemnite = IndemniteJournalier(12, listOf(),"2020-20-20")
        Mockito.`when`(api.getIndemniteJournalier(ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString()))
            .thenReturn(Response.success(indemnite))
        Mockito.`when`(internetConnectionChecker.check()).thenReturn(true)
        val res = indemniteRepository.getIndemnitejournalier("rryjryjrj")
        assertThat(res.data).isEqualTo(indemnite)
    }
}