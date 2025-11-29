package ju.mad.tuitioncounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ju.mad.tuitioncounter.data.database.TuitionDatabase
import ju.mad.tuitioncounter.data.repository.TuitionRepositoryImpl
import ju.mad.tuitioncounter.domain.usecase.GetTuitionListUseCase
import ju.mad.tuitioncounter.domain.usecase.AddTuitionUseCase
import ju.mad.tuitioncounter.domain.usecase.ResetClassCountUseCase
import ju.mad.tuitioncounter.ui.navigation.AppNavigation
import ju.mad.tuitioncounter.ui.theme.TuitionCounterTheme
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: Initialize Database and Repository
        val tuitionDao = TuitionDatabase.getDatabase(applicationContext).tuitionDao()
        val tuitionRepository = TuitionRepositoryImpl(tuitionDao)

        // Step 2: Initialize Use Cases
        val getTuitionListUseCase = GetTuitionListUseCase(tuitionRepository)
        val addTuitionUseCase = AddTuitionUseCase(tuitionRepository)
        val resetClassCountUseCase = ResetClassCountUseCase(tuitionRepository)


        // Step 3: Initialize ViewModel manually
        val viewModel = TuitionViewModel(getTuitionListUseCase, addTuitionUseCase, tuitionRepository)

        // Step 4: Set up UI
        setContent {
            TuitionCounterTheme {
                AppNavigation(viewModel = viewModel)  // Pass viewModel here
            }
        }
    }
}