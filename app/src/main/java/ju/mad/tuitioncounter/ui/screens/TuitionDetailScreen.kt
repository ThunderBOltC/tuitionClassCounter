package ju.mad.tuitioncounter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

@Composable
fun TuitionDetailScreen(
    navController: NavController,
    tuitionId: Long,
    viewModel: TuitionViewModel
) {
    val tuition = viewModel.tuitionList.value.find { it.id == tuitionId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Tuition Details", modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "Name: ${tuition?.name ?: "Unknown"}")
        Text(text = "Location: ${tuition?.location ?: "Unknown"}")
        Text(text = "Salary: ${tuition?.salary ?: 0.0}")
        Text(text = "Target Class: ${tuition?.targetedClass ?: 0}")
        Text(text = "Start Date: ${tuition?.startDateEpochMs ?: 0L}")
    }
}