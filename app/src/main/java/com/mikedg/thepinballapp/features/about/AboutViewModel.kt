package com.mikedg.thepinballapp.features.about

import androidx.lifecycle.ViewModel
import com.mikedg.thepinballapp.hilt.CalendarNow
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(@CalendarNow calendarNow: Calendar) : ViewModel() {
    val copyrightString = "© ${calendarNow.get(Calendar.YEAR)} Mike DiGiovanni. All rights reserved."
}