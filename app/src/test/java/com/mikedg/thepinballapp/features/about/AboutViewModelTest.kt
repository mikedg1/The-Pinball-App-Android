package com.mikedg.thepinballapp.features.about

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class AboutViewModelTest {

    @Test
    fun `getCopyrightString uses Calendar date`() {
        val year = 2050
        // Arrange
        val mockCalendar = Calendar.getInstance().apply { set(Calendar.YEAR, year) }
        val viewModel = AboutViewModel(mockCalendar)

        // Act
        val result = viewModel.copyrightString

        // Assert that the year is shoved into this string
        assertEquals("© ${year} Mike DiGiovanni. All rights reserved.", result)
    }
}