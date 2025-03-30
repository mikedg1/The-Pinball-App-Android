package com.mikedg.thepinballapp.data.model

import com.squareup.moshi.Json

data class Machine(
    @Json(name = "opdb_id")
    val opdbId: String?,

    @Json(name = "is_machine")
    val isMachine: Boolean?,

    val name: String?,

    @Json(name = "common_name")
    val commonName: String?,

    val shortname: String?,

    @Json(name = "physical_machine")
    val physicalMachine: Int?,

    @Json(name = "ipdb_id")
    val ipdbId: Int?,

    @Json(name = "manufacture_date")
    val manufactureDate: String?,

    val manufacturer: Manufacturer,

    val type: String?,

    val display: String?,

    @Json(name = "player_count")
    val playerCount: Int?,

    val features: List<String>?,

    val keywords: List<String>?,

    val description: String?,

    @Json(name = "created_at")
    val createdAt: String?,

    @Json(name = "updated_at")
    val updatedAt: String?,

    val images: List<PinballImage>?
)

data class Manufacturer(
    @Json(name = "manufacturer_id")
    val manufacturerId: Int?,

    val name: String?,

    @Json(name = "full_name")
    val fullName: String?,

    @Json(name = "created_at")
    val createdAt: String?,

    @Json(name = "updated_at")
    val updatedAt: String?
)

data class PinballImage(
    val title: String?,
    val primary: Boolean?,
    val type: String?,
    val urls: ImageUrls?,
    val sizes: ImageSizes?
)

data class ImageUrls(
    val medium: String?,
    val large: String?,
    val small: String?
)

data class ImageSizes(
    val medium: ImageDimension?,
    val large: ImageDimension?,
    val small: ImageDimension?
)

data class ImageDimension(
    val width: Int?,
    val height: Int?
)
