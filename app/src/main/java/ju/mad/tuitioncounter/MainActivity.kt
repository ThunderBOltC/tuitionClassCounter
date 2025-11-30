package ju.mad.tuitioncounter



import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ju.mad.tuitioncounter.data.database.TuitionDatabase
import ju.mad.tuitioncounter.data.repository.TuitionRepositoryImpl
import ju.mad.tuitioncounter.domain.usecase.*
import ju.mad.tuitioncounter.ui.navigation.AppNavigation
import ju.mad.tuitioncounter.ui.theme.TuitionCounterTheme
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, you can add any logic here if needed.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Initialize Database and Repository
        val tuitionDao = TuitionDatabase.getDatabase(applicationContext).tuitionDao()
        val tuitionRepository = TuitionRepositoryImpl(tuitionDao)

        // Initialize Use Cases
        val getTuitionListUseCase = GetTuitionListUseCase(tuitionRepository)
        val getTuitionDetailsUseCase = GetTuitionDetailsUseCase(tuitionRepository)
        val getClassLogsUseCase = GetClassLogsUseCase(tuitionRepository)
        val addTuitionUseCase = AddTuitionUseCase(tuitionRepository)
        val updateTuitionUseCase = UpdateTuitionUseCase(tuitionRepository)
        val deleteTuitionUseCase = DeleteTuitionUseCase(tuitionRepository)
        val logClassUseCase = LogClassUseCase(tuitionRepository)
        val deleteClassLogUseCase = DeleteClassLogUseCase(tuitionRepository)
        val resetClassCountUseCase = ResetClassCountUseCase(tuitionRepository)

        // Initialize ViewModel with all use cases
        val viewModel = TuitionViewModel(
            getTuitionListUseCase,
            getTuitionDetailsUseCase,
            getClassLogsUseCase,
            addTuitionUseCase,
            updateTuitionUseCase,
            deleteTuitionUseCase,
            logClassUseCase,
            deleteClassLogUseCase,
            resetClassCountUseCase
        )

        setContent {
            TuitionCounterTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
