package fr.strada.smobile.data.repositories

import android.app.Application
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest{

    lateinit var userRepository : UserRepositoryImpl
    @Mock
    lateinit var api :Api
    @Mock
    lateinit var apiUAM : Api
    @Mock
    lateinit var apiAuth0 : AuthenticationAPIClient
    @Mock
    lateinit var userPreferences : UserPreferences
    @Mock
    lateinit var credentialsManager: CredentialsManager
    @Mock
    lateinit var internetConnectionChecker: InternetConnectionChecker
    @Mock
    lateinit var context : Application

    val callback = argumentCaptor<BaseCallback<Credentials , CredentialsManagerException>>()

    @Before
    fun setup(){
        userRepository = UserRepositoryImpl(api,apiUAM,apiAuth0,userPreferences,credentialsManager,internetConnectionChecker,context)
        Mockito.`when`(credentialsManager.getCredentials(callback.capture())).thenAnswer {
            callback.firstValue.onSuccess(Credentials("fake token",null,null,null,null))
        }
    }

    @Test
    fun testIsUserLoggedIn() = runBlockingTest{
        val isUserLoggedIn = userRepository.isUserLoggedIn()
        assertThat(isUserLoggedIn).isTrue()
    }
}