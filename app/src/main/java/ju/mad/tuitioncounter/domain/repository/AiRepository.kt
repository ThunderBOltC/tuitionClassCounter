package ju.mad.tuitioncounter.domain.repository

interface AiRepository {
    suspend fun getChatResponse(
        messages: List<Pair<String, Boolean>>, // content to isUser
        apiKey: String
    ): Result<String>
}