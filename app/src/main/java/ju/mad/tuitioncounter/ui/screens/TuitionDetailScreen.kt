package ju.mad.tuitioncounter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TuitionDetailScreen(
    navController: NavController,
    tuitionId: Long,
    viewModel: TuitionViewModel
) {
    val context = LocalContext.current
    val tuition = viewModel.tuitionList.value.find { it.id == tuitionId }

    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(tuition?.name ?: "") }
    var location by remember { mutableStateOf(tuition?.location ?: "") }
    var salary by remember { mutableStateOf(tuition?.salary?.toString() ?: "") }
    var targetedClass by remember { mutableStateOf(tuition?.targetedClass?.toString() ?: "") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (tuition == null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text("Tuition not found")
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back")
            }
        }
        return
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Tuition") },
            text = { Text("Are you sure you want to delete this tuition?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTuition(tuition)
                        showDeleteConfirmation = false
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
                Button(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tuition Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isEditing) {
            // Edit Mode
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            TextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            TextField(
                value = salary,
                onValueChange = { salary = it },
                label = { Text("Salary") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            TextField(
                value = targetedClass,
                onValueChange = { targetedClass = it },
                label = { Text("Target Class") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        } else {
            // View Mode
            Text(text = "Name: ${tuition.name}", modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Location: ${tuition.location}", modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Salary: ${tuition.salary}", modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Target Class: ${tuition.targetedClass}", modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Progress: ${tuition.progress}", modifier = Modifier.padding(vertical = 4.dp))
            val formattedDate = remember(tuition.startDateEpochMs) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(tuition.startDateEpochMs))
            }
            Text(text = "Start Date: $formattedDate")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isEditing) {
                // Save Button
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
                // Cancel Button
                Button(
                    onClick = {
                        isEditing = false
                        name = tuition.name
                        location = tuition.location
                        salary = tuition.salary.toString()
                        targetedClass = tuition.targetedClass.toString()
                    }
                ) {
                    Text("Cancel")
                }
            } else {
                // Edit Button
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
                // Delete Button
                Button(
                    onClick = {
                        showDeleteConfirmation = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Button
        Button(
            onClick = {
                viewModel.resetClassCount(tuition.id)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Class Count")
        }
    }
}
