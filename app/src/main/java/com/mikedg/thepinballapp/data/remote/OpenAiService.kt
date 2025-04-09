package com.mikedg.thepinballapp.data.remote

import ContentItem
import JsonSchema
import JsonSchemaFormat
import Message
import ResponseRequest
import SchemaItems
import SchemaProperty
import TextFormat
import android.graphics.Bitmap.CompressFormat
import android.util.Base64
import coil3.Bitmap
import com.mikedg.thepinballapp.BuildConfig
import com.mikedg.thepinballapp.data.model.openai.OpenAiResponse
import com.mikedg.thepinballapp.util.OPDB_ID_CSV
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.ByteArrayOutputStream

class OpenAiService(
    private val apiToken: String = BuildConfig.OPENAI_API_TOKEN
) {
    interface OpenAiApi {
        @Headers("Authorization: Bearer ${BuildConfig.OPENAI_API_TOKEN}") // TODO use the API token
        @POST("responses")
        suspend fun createResponse(
            @Body requestBody: ResponseRequest
        ): OpenAiResponse
    }

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(OpenAiApi::class.java)

    suspend fun getScore(bitmap: Bitmap): OpenAiResponse {
        val base64Image = ByteArrayOutputStream().use { stream ->
            bitmap.compress(CompressFormat.JPEG, 100, stream)
            Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
        }

        val messages = listOf(
            Message(
                role = "system",
                content = "Taking an image as input, call the function with the required parameters. Use the below machineName,opdbId pairs to find the machine_name and opdb_id. If you find a matching opdb_id, it should use the matching machine_name. :\n$OPDB_ID_CSV"
            ),
            Message(
                role = "user",
                content = listOf(
                    ContentItem(type = "input_text", text = "What pinball machine and score are in this image?"),
                    ContentItem(type = "input_image", image_url = "data:image/jpeg;base64,$base64Image")
                )
            )
        )

        val jsonSchema = JsonSchema(
            type = "object",
            required = listOf("machine_name", "opdb_id", "player_scores"),
            properties = mapOf(
                "opdb_id" to SchemaProperty(
                    type = "string",
                    description = "Unique identifier for the machine in the OPDB"
                ),
                "machine_name" to SchemaProperty(
                    type = "string",
                    description = "Name of the pinball machine in the OPDB"
                ),
                "player_scores" to SchemaProperty(
                    type = "array",
                    description = "List of scores for the players",
                    items = SchemaItems(
                        type = "number",
                        description = "Score of a player"
                    )
                )
            ),
            additionalProperties = false
        )

        val request = ResponseRequest(
            model = "gpt-4o",
            input = messages,
            text = TextFormat(
                format = JsonSchemaFormat(
                    type = "json_schema",
                    name = "store_pinball_data",
                    description = "Stores data read from an image of a pinball machine including machine name, opdb id, and possible player scores",
                    strict = true,
                    schema = jsonSchema
                )
            )
        )


        println("Request JSON: ${moshi.adapter(ResponseRequest::class.java).indent("  ").toJson(request)}")
        return api.createResponse(request)
    }
}

// TODO: move and parse elsewhere?
data class ResponseResult(
    val machine_name: String?,
    val opdb_id: String?,
    val player_scores: List<Long>?
)
