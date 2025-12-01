package ju.mad.tuitioncounter.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.ui.navigation.AppNavigation
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuitionDetailScreen(
    navController: NavController,
    tuitionId: Long,
    viewModel: TuitionViewModel
) {
    val tuitionDetails by viewModel.tuitionDetails.collectAsState()
    val classLogs by viewModel.classLogs.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var targetedClass by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var selectedClassLogId by remember { mutableStateOf<Long?>(null) }
    var isPressing by remember { mutableStateOf(false) }
    var showTargetCompletedDialog by remember { mutableStateOf(false) }

    // Load data when screen opens
    LaunchedEffect(tuitionId) {
        viewModel.getTuitionDetails(tuitionId)
        viewModel.getClassLogs(tuitionId)
    }

    // Update local state when tuition details change
    LaunchedEffect(tuitionDetails) {
        tuitionDetails?.let { tuition ->
            name = tuition.name
            location = tuition.location
            salary = tuition.salary.toString()
            targetedClass = tuition.targetedClass.toString()
            if (tuition.classCount >= tuition.targetedClass && tuition.targetedClass > 0) {
                showTargetCompletedDialog = true
            }
        }
    }

    // Delete Confirmation Dialog for the main tuition
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Tuition") },
            text = { Text("Are you sure you want to delete this tuition? All class logs will also be deleted.") },
            confirmButton = {
                Button(
                    onClick = {
                        val toDelete = tuitionDetails
                        showDeleteConfirmation = false
                        // First request deletion in ViewModel
                        toDelete?.let { viewModel.deleteTuition(it) }
                        // Then navigate away (single pop only here)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (tuitionDetails == null) {
        // Show a loading/empty state until we have data (or navigated away)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val tuition = tuitionDetails!!

    if (showTargetCompletedDialog) {
        AlertDialog(
            onDismissRequest = { showTargetCompletedDialog = false },
            title = { Text("Target Completed!") },
            text = { Text("You have completed the targeted number of classes for this tuition. Do you want to reset the class count?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetClassCount(tuitionId)
                        showTargetCompletedDialog = false
                    }
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTargetCompletedDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    selectedClassLogId = null
                    isPressing = false
                })
            },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Header
        item {
            Text(
                text = "Tuition Details",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Details Section
        item {
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = salary,
                        onValueChange = { salary = it },
                        label = { Text("Salary") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = targetedClass,
                        onValueChange = { targetedClass = it },
                        label = { Text("Target Class") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow("Name", tuition.name)
                        DetailRow("Location", tuition.location)
                        DetailRow("Salary", "৳${tuition.salary}")
                        DetailRow("Target Classes", tuition.targetedClass.toString())
                        DetailRow("Progress", tuition.progress)
                        DetailRow(
                            "Start Date",
                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                .format(Date(tuition.startDateEpochMs))
                        )
                    }
                }
            }
        }

        // Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (isEditing) {
                    Button(
                        onClick = {
                            val updatedTuition = TuitionModel(
                                id = tuition.id,
                                name = name,
                                location = location,
                                salary = salary.toDoubleOrNull() ?: 0.0,
                                targetedClass = targetedClass.toIntOrNull() ?: 0,
                                classCount = tuition.classCount,
                                startDateEpochMs = tuition.startDateEpochMs
                            )
                            viewModel.updateTuition(updatedTuition)
                            isEditing = false
                        }
                    ) {
                        Text("Save")
                    }
                    OutlinedButton(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                } else {
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                    Button(
                        onClick = { showDeleteConfirmation = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                }
            }
        }

        // Reset Class Count Button
        item {
            OutlinedButton(
                onClick = { viewModel.resetClassCount(tuitionId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Class Count")
            }
        }

        // Add Class Button
        item {
            Button(
                onClick = {
                    if (tuition.classCount >= tuition.targetedClass && tuition.targetedClass > 0) {
                        showTargetCompletedDialog = true
                    } else {
                        navController.navigate("log_class_screen/$tuitionId")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("➕ Add Class")
            }
        }

        // Class Logs Section Header
        item {
            Text(
                text = "Class History (${classLogs.size} classes)",
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Class Logs List
        if (classLogs.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No classes have been logged yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(classLogs, key = { it.id }) { classLog ->
                val isSelected = classLog.id == selectedClassLogId

                val scale by animateFloatAsState(
                    targetValue = if (isSelected && isPressing) 1.03f else 1f,
                    label = "scale"
                )
                val elevation by animateDpAsState(
                    targetValue = if (isSelected && isPressing) 8.dp else 2.dp,
                    label = "elevation"
                )

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale)
                        .pointerInput(classLog.id) {
                            detectTapGestures(
                                onLongPress = {
                                    isPressing = true
                                    selectedClassLogId = classLog.id
                                },
                                onPress = {
                                    isPressing = true
                                    awaitRelease()
                                    isPressing = false
                                },
                                onTap = {
                                    selectedClassLogId = null
                                }
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            val classNumber = classLogs.size - classLogs.indexOf(classLog)
                            Text(
                                text = "Class #$classNumber",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                                    .format(Date(classLog.entryTimestampMs)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (isSelected) {
                            IconButton(onClick = {
                                // Pass both classId and tuitionId to the viewModel
                                viewModel.deleteClassLog(classLog.id, tuitionId)
                                selectedClassLogId = null
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Class Log",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
