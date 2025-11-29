package ju.mad.tuitioncounter.ui.adapter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

@Composable
fun MainTuitionListScreen(navController: NavController) {
    val tuitionViewModel: TuitionViewModel = viewModel()
    val tuitionList = tuitionViewModel.tuitionList.value

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Tuition List", modifier = Modifier.padding(16.dp))
        LazyColumn {
            items(tuitionList) { tuition ->
                TuitionListItem(tuition = tuition, navController = navController)
            }
        }

        Button(
            onClick = { navController.navigate("add_tuition_screen") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Tuition")
        }
    }
}

@Composable
fun TuitionListItem(tuition: TuitionModel, navController: NavController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {
            navController.navigate("tuition_detail_screen/${tuition.id}")
        }) {
        Text(text = tuition.name)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("tuition_detail_screen/${tuition.id}") }
        ) {
            Text(text = "Details")
        }
    }
}
