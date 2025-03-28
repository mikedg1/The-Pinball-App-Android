package com.mikedg.thepinballapp.data.model

import com.google.gson.annotations.SerializedName

data class Machine(
    @SerializedName("opdb_id")
    val opdbId: String,

    @SerializedName("is_machine")
    val isMachine: Boolean,

    val name: String,

    @SerializedName("common_name")
    val commonName: String?,

    val shortname: String,

    @SerializedName("physical_machine")
    val physicalMachine: Int,

    @SerializedName("ipdb_id")
    val ipdbId: Int,

    @SerializedName("manufacture_date")
    val manufactureDate: String,

    val manufacturer: Manufacturer,

    val type: String,

    val display: String,

    @SerializedName("player_count")
    val playerCount: Int,

    val features: List<String>,

    val keywords: List<String>,

    val description: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    val images: List<PinballImage>

)

data class Manufacturer(
    @SerializedName("manufacturer_id")
    val manufacturerId: Int,

    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

data class PinballImage(
    val title: String,
    val primary: Boolean,
    val type: String,
    val urls: ImageUrls,
    val sizes: ImageSizes
)

data class ImageUrls(
    val medium: String,
    val large: String,
    val small: String
)

data class ImageSizes(
    val medium: ImageDimension,
    val large: ImageDimension,
    val small: ImageDimension
)

data class ImageDimension(
    val width: Int,
    val height: Int
)
