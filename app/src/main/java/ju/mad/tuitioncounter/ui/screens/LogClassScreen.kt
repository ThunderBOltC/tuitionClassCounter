package ju.mad.tuitioncounter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LogClassScreen(
    navController: NavController,
    tuitionId: Long,
    viewModel: TuitionViewModel
) {
    val tuitionDetails by viewModel.tuitionDetails.collectAsState()
    val currentTimestamp = remember { System.currentTimeMillis() }

    // Load tuition details
    LaunchedEffect(tuitionId) {
        viewModel.getTuitionDetails(tuitionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = "Log Class",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Add new class for",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = tuitionDetails?.name ?: "Loading...",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Class Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        .format(Date(currentTimestamp)),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(Date(currentTimestamp)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Confirm Button
        Button(
            onClick = {
                viewModel.logClass(tuitionId)
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("✓ Confirm Class", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cancel Button
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}