package ju.mad.tuitioncounter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

@Composable
fun TuitionListScreen(
    navController: NavController,
    viewModel: TuitionViewModel
) {
    val tuitionList = viewModel.tuitionList.value

    LaunchedEffect(Unit) {
        viewModel.getAllTuitions()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Tuition List",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(tuitionList) { tuition ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            navController.navigate("tuition_detail_screen/${tuition.id}")
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = tuition.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Progress: ${tuition.progress}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("add_tuition_screen") },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}