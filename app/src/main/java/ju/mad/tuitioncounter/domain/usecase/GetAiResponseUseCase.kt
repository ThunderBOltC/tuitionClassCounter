package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.repository.AiRepository

class GetAiResponseUseCase(private val aiRepository: AiRepository) {

    // Changed to accept single message and apiKey
    suspend operator fun invoke(
        userMessage: String,
        apiKey: String
    ): Result<String> {
        // Convert single message to the list format the repository expects
        val messages = listOf(userMessage to true)
        return aiRepository.getChatResponse(messages, apiKey)
    }
}