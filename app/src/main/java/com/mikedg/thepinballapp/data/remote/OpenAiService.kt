package com.mikedg.thepinballapp.data.remote

import android.graphics.Bitmap.CompressFormat
import coil3.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import com.mikedg.thepinballapp.BuildConfig
import com.mikedg.thepinballapp.data.remote.OpdbApiService.OpdbApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

class OpenAiService(
    private val apiToken: String = BuildConfig.OPENAI_API_TOKEN
) {
    interface OpenAiApi {
        @Headers("Authorization: Bearer ${BuildConfig.OPENAI_API_TOKEN}")
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
                content = "Taking an image as input, call the function with the required parameters. Use the below machineName,opdbId pairs to find the machine_name and opdb_id. If you find a matching opdb_id, it should use the matching machine_name. :\n$pairs"
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

data class ResponseRequest(
    val model: String,
    val input: List<Message>,
    val text: TextFormat
)

data class Message(
    val role: String,
    val content: Any // Can be String or List<ContentItem>
)

data class ContentItem(
    val type: String,
    val text: String? = null,
    val image_url: String? = null
)

data class TextFormat(
    val format: JsonSchemaFormat
)

data class JsonSchemaFormat(
    val type: String,
    val name: String,
    val description: String,
    val strict: Boolean,
    val schema: JsonSchema
)

data class JsonSchema(
    val type: String,
    val required: List<String>,
    val properties: Map<String, SchemaProperty>,
    val additionalProperties: Boolean
)

data class SchemaProperty(
    val type: String,
    val description: String,
    val items: SchemaItems? = null
)

data class SchemaItems(
    val type: String,
    val description: String
)

// Response model
data class ResponseResult(
    val machine_name: String?,
    val opdb_id: String?,
    val player_scores: List<Long>?
)

data class OpenAiResponse(
    val id: String,
    val `object`: String,
    val created_at: Long,
    val status: String,
    val error: String? = null,
    val incomplete_details: Any? = null,
    val instructions: String? = null,
    val max_output_tokens: Int? = null,
    val model: String,
    val output: List<OutputItem>,
    val parallel_tool_calls: Boolean,
    val previous_response_id: String? = null,
    val reasoning: Reasoning,
    val store: Boolean,
    val temperature: Int,
    val text: TextResponse,
    val tool_choice: String,
    val tools: List<Any>,
    val top_p: Int,
    val truncation: String,
    val usage: Usage,
    val user: String? = null,
    val metadata: Map<String, Any>,
//    val output_text: String? = null
)

data class OutputItem(
    val type: String,
    val id: String,
    val status: String,
    val role: String,
    val content: List<ContentItem2>
)

data class ContentItem2(
    val type: String,
    val text: String,
    val annotations: List<Any> = emptyList()
)

data class Reasoning(
    val effort: String? = null,
    val generate_summary: String? = null
)

data class TextResponse(
    val format: TextFormat2
)

data class TextFormat2(
    val type: String,
    val description: String,
    val name: String,
    val schema: Any,
    val strict: Boolean
)

data class Usage(
    val input_tokens: Int,
    val input_tokens_details: InputTokensDetails,
    val output_tokens: Int,
    val output_tokens_details: OutputTokensDetails,
    val total_tokens: Int
)

data class InputTokensDetails(
    val cached_tokens: Int
)

data class OutputTokensDetails(
    val reasoning_tokens: Int
)

const val pairs = """Challenger,G50L9-MDxXD
Moon Shot,GR7V3-MQPyL
Galaxy Play,GRDlQ-MJ9yJ
Free Fall,G4xlK-MDEKL
The 30's,G4Xdq-MLBlD
Sisters,G5WPp-MLyZJ
Space Mission,GRnX6-MQk8L
Groovy,GRpKY-MDlYQ
Big Ben,G5QBX-MQd1L
City Slicker,GrEVb-MLOxJ
Big Day,G48JX-MJw3J
Flash Gordon,G5728-MDbjD
Pinball Champ '82,GrPdq-MQYkJ
Surfer,GrZwo-MJNlQ
High Roller Casino,G4JZ4-ML0pL
Explorer,G5VPX-MQjXJ
The 4 Horsemen,GRKdD-MLnxD
Blondie,GRVdN-MLqwD
Sky·Line,GRQ6d-MDRlL
Dealer's Choice,GRwW9-MJoeQ
Apollo 13,G411e-MJrEL
Criterium 75,G4Plz-MDz6D
Moon Flight,GreN3-MJ7wJ

"""