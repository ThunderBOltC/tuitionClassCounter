package ju.mad.tuitioncounter.data.repository

import ju.mad.tuitioncounter.data.service.AiService
import ju.mad.tuitioncounter.data.service.model.Message
import ju.mad.tuitioncounter.data.service.model.OpenRouterRequest
import ju.mad.tuitioncounter.domain.repository.AiRepository

class AiRepositoryImpl(private val aiService: AiService) : AiRepository {

    override suspend fun getChatResponse(
        messages: List<Pair<String, Boolean>>,
        apiKey: String
    ): Result<String> {
        return try {
            val formattedMessages = messages.map { (content, isUser) ->
                Message(
                    role = if (isUser) "user" else "assistant",
                    content = content
                )
            }

            val request = OpenRouterRequest(messages = formattedMessages)

            // Headers are now in the interceptor, just call the API
            val response = aiService.getChatCompletion(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.error != null) {
                    Result.failure(Exception("API Error: ${body.error.message}"))
                } else {
                    val aiMessage = body?.choices?.firstOrNull()?.message?.content
                    if (aiMessage != null) {
                        Result.success(aiMessage)
                    } else {
                        Result.failure(Exception("No response from AI"))
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.message}", e))
        }
    }
}