package ju.mad.tuitioncounter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ju.mad.tuitioncounter.BuildConfig
// This now uses your project's ChatMessage model
import ju.mad.tuitioncounter.domain.model.ChatMessage
import ju.mad.tuitioncounter.ui.viewmodels.AiCompanionViewModel
import kotlinx.coroutines.launch

@Composable
fun AiCompanionScreen(
    viewModel: AiCompanionViewModel
) {
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Automatically scroll to the bottom when a new message appears
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            coroutineScope.launch {
                // Scroll to the last item in the list
                listState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    Scaffold(
        bottomBar = {
            ChatInputBar(
                message = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    if (messageText.text.isNotBlank()) {
                        viewModel.sendMessage(messageText.text, apiKey = BuildConfig.OPENROUTER_API_KEY)
                        messageText = TextFieldValue("") // Clear the input field
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(chatMessages) { message ->
                    ChatBubble(message = message)
                }

                // Show a typing indicator when the AI is "thinking"
                if (isLoading) {
                    item {
                        // Create a temporary ChatMessage for the typing indicator
                        ChatBubble(message = ChatMessage(content = "● ● ●", isUser = false), isTyping = true)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, isTyping: Boolean = false) {
    // FIX: Check the 'isUser' boolean property from your ChatMessage class
    val isUserMessage = message.isUser
    val bubbleColor = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val alignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isUserMessage) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(bubbleColor)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.content,
                color = if (isTyping) textColor.copy(alpha = 0.6f) else textColor
            )
        }
    }
}

@Composable
fun ChatInputBar(
    message: TextFieldValue,
    onMessageChange: (TextFieldValue) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                placeholder = { Text("Type a message...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSendClick,
                enabled = message.text.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, "Send")
            }
        }
    }
}
