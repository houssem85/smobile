package fr.strada.smobile.data.repositories

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.pointeuse.ActivitiePointeuse
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.network.Api
import fr.strada.smobile.data.network.PointeuseDao
import fr.strada.smobile.ui.pointeuse.PointeuseWorker
import fr.strada.smobile.utils.InternetConnectionChecker
import fr.strada.smobile.utils.data_stores.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import retrofit2.Response


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PointeuseRepositoryTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var pointeuseRepository : PointeuseRepository

    @Mock
    lateinit var api : Api
    @Mock
    lateinit var pointeuseDao : PointeuseDao
    @Mock
    lateinit var internetConnectionChecker : InternetConnectionChecker
    @Mock
    lateinit var credentialsManager : CredentialsManager
    @Mock
    lateinit var userPreferences : UserPreferences
    @Mock
    lateinit var context : Application

    @Before
    fun setUp() {
        pointeuseRepository = PointeuseRepository(api , pointeuseDao , internetConnectionChecker,credentialsManager,userPreferences,context)
        `when`(credentialsManager.getCredentials(callback.capture())).thenAnswer {
            callback.firstValue.onSuccess(Credentials("fake token",null,null,null,null))
        }
        `when`(userPreferences.collaborateurId).thenReturn(flow { emit("5ca8c7a7-a9d5-4f48-8e5d-0ea995926892") })
    }

    @Test
    fun testGetListTypeActivitiePointeuseWithNoConnection() = runBlockingTest {
        val travail = TypeActivitePointeuseModel("98629515-7feb-4a82-891c-e18f01189e24","travail")
        val list = listOf(travail)
        `when`(internetConnectionChecker.check()).thenAnswer {
            return@thenAnswer false
        }
        `when`(pointeuseDao.getListTypeActivitePointeuse()).thenAnswer {
            return@thenAnswer list
        }
        val res = pointeuseRepository.getListTypeActivitePointeuse()
        assertThat(res.data).isEqualTo(list)
    }

    val callback = argumentCaptor<BaseCallback<Credentials , CredentialsManagerException>>()

    @Test
    fun testGetListTypeActivitiePointeuseWithConnection()= runBlockingTest {
        val travail = TypeActivitePointeuseModel("98629515-7feb-4a82-891c-e18f01189e24","travail")
        val list = listOf(travail)
        `when`(internetConnectionChecker.check()).thenReturn(true)
        `when`(api.getPointeuseActiviteList(anyString(), anyString())).thenReturn(Response.success(list))
        `when`(pointeuseDao.getListTypeActivitePointeuse()).thenReturn(list)
         val res = pointeuseRepository.getListTypeActivitePointeuse()
         assertThat(res.data).isEqualTo(list)
    }

    @Test
    fun getDerniereActivitiePointeuse()= runBlockingTest {
        val derniereActivitie = ActivitiePointeuse("7c25f34b-14de-4883-a598-fca118980386")
        `when`(internetConnectionChecker.check()).thenReturn(true)
        `when`(api.getDerniereActiviteParCollabId(anyString(), anyString())).thenReturn(Response.success(derniereActivitie))
        `when`(pointeuseDao.getDerniereActivitePointeuse()).thenReturn(listOf(derniereActivitie))
        val res = pointeuseRepository.getDerniereActivitiePointeuse()
        assertThat(res.data).isEqualTo(derniereActivitie)
    }

    @Test
    fun testStartActivitiePointeuse() = runBlockingTest {
        `when`(internetConnectionChecker.check()).thenReturn(true)
        `when`(api.createActivitePointeuse(anyString(), anyString(),anyString(),anyString(),anyString(),anyString(),latitudeDepart =  anyDouble(),longitudeDepart = anyDouble(),anyDouble(),anyDouble())).thenReturn(Response.success("sucess".toResponseBody()))
         val res = pointeuseRepository.startActivitiePointeuse("ed3872e1-77ce-4804-b71a-3a86c21d4af7", anyDouble(), anyDouble())
        assertThat(res.data).isEqualTo(PointeuseWorker.FutureStatus.NOT_ENQUEUED)
    }
}