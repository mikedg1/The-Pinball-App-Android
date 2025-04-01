package com.mikedg.thepinballapp.data.model

import com.squareup.moshi.Json

data class ChangeLog(
    @Json(name = "changelog_id")
    val changelogId: Int?,

    @Json(name = "opdb_id_deleted")
    val opdbIdDeleted: String?,

    val action: String?,

    @Json(name = "opdb_id_replacement")
    val opdbIdReplacement: String?,

    @Json(name = "created_at")
    val createdAt: String?,

    @Json(name = "updated_at")
    val updatedAt: String?
)
