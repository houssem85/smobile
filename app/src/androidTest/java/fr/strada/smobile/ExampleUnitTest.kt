package fr.strada.smobile

import fr.strada.smobile.di.graphical_view.GraphicalViewModule
import fr.strada.smobile.ui.activities.Utils.getDate
import fr.strada.smobile.ui.activities.Utils.getFirstDayInCalenderOfYear
import fr.strada.smobile.ui.activities.Utils.getLastDayInCalenderOfYear
import fr.strada.smobile.ui.activities.Utils.getWeeksOfYear
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit top_cumul_day, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        var day = getFirstDayInCalenderOfYear(2020,Calendar.MONDAY, Locale.FRANCE)
        assertEquals(day, day)

        var day1 = getLastDayInCalenderOfYear(2020,2,Locale.FRANCE)
        assertEquals(day1, day1)

        var day2 = getDate(2020,2,2, Locale.FRANCE)
        assertEquals(day2, day2)

        var day3 = getWeeksOfYear(2020,3, Locale.FRANCE)
        assertEquals(day3, day3)

        var graph = GraphicalViewModule()
        assertEquals(graph, graph)
    }
}
