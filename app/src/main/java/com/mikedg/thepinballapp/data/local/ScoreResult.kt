package com.mikedg.thepinballapp.data.local

data class ScoreResult(
    // TODO: rename and use serialization tags, if I'm even using serialization for this
    val machine_name: String?,
    val opdb_id: String?,
    val player_scores: List<Long>?
)
