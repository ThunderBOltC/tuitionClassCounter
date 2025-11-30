package ju.mad.tuitioncounter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ju.mad.tuitioncounter.domain.model.ChatMessage
import ju.mad.tuitioncounter.domain.usecase.GetAiResponseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiCompanionViewModel(
    private val getAiResponseUseCase: GetAiResponseUseCase
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Add a welcoming message when the screen is first opened
        _chatMessages.value = listOf(
            ChatMessage(content = "Hello! How can I help you manage your tuitions today?", isUser = false)
        )
    }

    fun sendMessage(userMessage: String, apiKey: String) {
        // Create and add the user's message immediately
        val newUserMessage = ChatMessage(content = userMessage, isUser = true)
        _chatMessages.value += newUserMessage

        // Set loading state to true to show the typing indicator
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Make the API call - now properly handling Result type
                val result = getAiResponseUseCase(userMessage, apiKey)

                result.onSuccess { aiResponse ->
                    // Once the response is received, create the AI's message
                    val newAiMessage = ChatMessage(content = aiResponse, isUser = false)
                    // Add the new AI message to the list
                    _chatMessages.value += newAiMessage
                }.onFailure { error ->
                    // If there's an error, add an error message to the chat
                    val errorMessage = ChatMessage(
                        content = "Sorry, I couldn't get a response: ${error.message ?: "Unknown error"}",
                        isUser = false
                    )
                    _chatMessages.value += errorMessage
                    error.printStackTrace() // Log the error for debugging
                }
            } catch (e: Exception) {
                // Catch any unexpected exceptions
                val errorMessage = ChatMessage(
                    content = "Sorry, I couldn't get a response. Please check your connection or API key.",
                    isUser = false
                )
                _chatMessages.value += errorMessage
                e.printStackTrace() // Log the error for debugging
            } finally {
                // Whether successful or not, stop the loading indicator
                _isLoading.value = false
            }
        }
    }
}