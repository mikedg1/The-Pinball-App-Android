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