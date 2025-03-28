package com.mikedg.thepinballapp.data.model

import com.google.gson.annotations.SerializedName

data class ChangeLog(
    @SerializedName("changelog_id")
    val changelogId: Int,

    @SerializedName("opdb_id_deleted")
    val opdbIdDeleted: String?,

    val action: String,

    @SerializedName("opdb_id_replacement")
    val opdbIdReplacement: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)