package torilab.assessment.notes.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtilsTest {

    @Test
    fun `toFormattedDate with default pattern returns correct format`() {
        val timestamp = 1700000000000L
        val expected = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
            .format(Date(timestamp))
        assertEquals(expected, timestamp.toFormattedDate())
    }

    @Test
    fun `toFormattedDate with custom pattern returns correct format`() {
        val timestamp = 1700000000000L
        val pattern = "yyyy-MM-dd"
        val expected = SimpleDateFormat(pattern, Locale.getDefault())
            .format(Date(timestamp))
        assertEquals(expected, timestamp.toFormattedDate(pattern))
    }

    @Test
    fun `toFormattedDate with zero timestamp returns epoch date`() {
        val timestamp = 0L
        val result = timestamp.toFormattedDate("yyyy-MM-dd")
        assertTrue(result.isNotBlank())
    }

    @Test
    fun `toFormattedDate with time-only pattern returns time string`() {
        val timestamp = 1700000000000L
        val pattern = "HH:mm:ss"
        val expected = SimpleDateFormat(pattern, Locale.getDefault())
            .format(Date(timestamp))
        assertEquals(expected, timestamp.toFormattedDate(pattern))
    }
}
