package ju.mad.tuitioncounter.ui.screens

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import java.util.*

@Composable
fun AddTuitionScreen(
    navController: NavController,
    viewModel: TuitionViewModel
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var targetedClass by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }

    val context = LocalContext.current
    val formattedDate = remember(startDate) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(startDate))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter   // 👆 moves form a bit upper
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add New Tuition",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tuition Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = salary,
                onValueChange = { salary = it },
                label = { Text("Salary") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = targetedClass,
                onValueChange = { targetedClass = it },
                label = { Text("Targeted Class") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            // 👉 OutlinedTextField for Start Date (click opens calendar)
            OutlinedTextField(
                value = formattedDate,
                onValueChange = { }, // read-only
                label = { Text("Start Date") },
                singleLine = true,
                enabled = false, // disables manual typing
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val cal = Calendar.getInstance()
                                cal.set(year, month, dayOfMonth, 0, 0, 0)
                                startDate = cal.timeInMillis
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isBlank() || location.isBlank() || salary.isBlank() || targetedClass.isBlank()) {
                        Toast.makeText(context, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
                    } else {
                        val tuition = TuitionModel(
                            name = name,
                            location = location,
                            salary = salary.toDoubleOrNull() ?: 0.0,
                            targetedClass = targetedClass.toIntOrNull() ?: 0,
                            startDateEpochMs = startDate
                        )
                        viewModel.addTuition(tuition)
                        Toast.makeText(context, "Tuition created successfully!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Tuition")
            }
        }
    }
}
