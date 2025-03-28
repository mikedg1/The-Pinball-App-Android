package com.mikedg.thepinballapp.features.home

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Search : Route()

    @Serializable
    object ChangeLog : Route()

    @Serializable
    data class MachineInfo(val id: String) : Route()

    @Serializable
    object About : Route()
}