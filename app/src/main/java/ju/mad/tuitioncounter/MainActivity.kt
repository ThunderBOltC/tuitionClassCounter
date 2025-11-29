package ju.mad.tuitioncounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ju.mad.tuitioncounter.data.database.TuitionDatabase
import ju.mad.tuitioncounter.data.repository.TuitionRepositoryImpl
import ju.mad.tuitioncounter.domain.usecase.*
import ju.mad.tuitioncounter.ui.navigation.AppNavigation
import ju.mad.tuitioncounter.ui.theme.TuitionCounterTheme
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Database and Repository
        val tuitionDao = TuitionDatabase.getDatabase(applicationContext).tuitionDao()
        val tuitionRepository = TuitionRepositoryImpl(tuitionDao)

        // Initialize Use Cases
        val getTuitionListUseCase = GetTuitionListUseCase(tuitionRepository)
        val addTuitionUseCase = AddTuitionUseCase(tuitionRepository)
        val updateTuitionUseCase = UpdateTuitionUseCase(tuitionRepository)
        val deleteTuitionUseCase = DeleteTuitionUseCase(tuitionRepository)
        val resetClassCountUseCase = ResetClassCountUseCase(tuitionRepository)

        // Initialize ViewModel
        val viewModel = TuitionViewModel(
            getTuitionListUseCase,
            addTuitionUseCase,
            updateTuitionUseCase,
            deleteTuitionUseCase,
            resetClassCountUseCase
        )

        setContent {
            TuitionCounterTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}