package fr.strada.smobile.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import fr.strada.smobile.data.models.pointeuse.TypeActivitePointeuseModel
import fr.strada.smobile.data.network.PointeuseDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase : AppDatabase
    private lateinit var pointeuseDao : PointeuseDao

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase::class.java).build()
        pointeuseDao = appDatabase.pointeuseDao()
    }

    @After
    fun teardowwn()
    {
        appDatabase.close()
    }

    @Test
    fun insertTypeActivitiesPointeuse() = runBlockingTest {
        val deplacement = TypeActivitePointeuseModel("4e93f07a-d28e-4c64-8809-4b9a3b4cac59","1234","Déplacement","/assets/img/78.png","#353035ff","a3d7a61b-7597-4304-ade4-456df9466951")
        pointeuseDao.insertListTypeActivitePointeuse(listOf(deplacement))
        val listTypeActivitePointeuse = pointeuseDao.getListTypeActivitePointeuse()
        assertThat(listTypeActivitePointeuse).contains(deplacement)
    }

}