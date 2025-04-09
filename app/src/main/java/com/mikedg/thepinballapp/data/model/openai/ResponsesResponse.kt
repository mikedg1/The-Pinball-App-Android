package com.mikedg.thepinballapp.data.model.openai

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
    val content: List<ContentItem>
)

data class ContentItem(
    val type: String,
    val text: String,
    val annotations: List<Any> = emptyList()
)

data class Reasoning(
    val effort: String? = null,
    val generate_summary: String? = null
)

data class TextResponse(
    val format: TextFormat
)

data class TextFormat(
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

