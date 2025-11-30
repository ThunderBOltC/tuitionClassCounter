package ju.mad.tuitioncounter.data.service.model

import com.google.gson.annotations.SerializedName

data class OpenRouterRequest(
    @SerializedName("model")
    val model: String = "openai/gpt-4o-mini",
    @SerializedName("messages")
    val messages: List<Message>
)

data class Message(
    @SerializedName("role")
    val role: String, // "user" or "assistant"
    @SerializedName("content")
    val content: String
)

data class OpenRouterResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("choices")
    val choices: List<Choice>,
    @SerializedName("error")
    val error: ErrorResponse?
)

data class Choice(
    @SerializedName("message")
    val message: Message,
    @SerializedName("finish_reason")
    val finishReason: String?
)

data class ErrorResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: Int?
)