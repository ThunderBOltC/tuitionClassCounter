package ju.mad.tuitioncounter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
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

    // Fetch tuitions when screen loads
    LaunchedEffect(Unit) {
        viewModel.getAllTuitions()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
           // Text(text = "Tuition List", modifier = Modifier.padding(bottom = 16.dp))

            LazyColumn {
                items(tuitionList) { tuition ->
                    Button(
                        onClick = {
                            navController.navigate("tuition_detail_screen/${tuition.id}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .height(80.dp)
                    ) {
                        Text(text = tuition.name)
                    }
                }
            }
        }

        // FAB to add new tuition
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