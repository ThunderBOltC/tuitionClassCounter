// File: MainActivity.kt
package ju.mad.tuitioncounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import ju.mad.tuitioncounter.data.auth.AuthRepositoryImpl
import ju.mad.tuitioncounter.data.database.TuitionDatabase
import ju.mad.tuitioncounter.data.repository.AiRepositoryImpl
import ju.mad.tuitioncounter.data.repository.TuitionRepositoryImpl
import ju.mad.tuitioncounter.data.service.RetrofitInstance
import ju.mad.tuitioncounter.domain.usecase.*
import ju.mad.tuitioncounter.ui.navigation.AppNavigation
import ju.mad.tuitioncounter.ui.theme.TuitionCounterTheme
import ju.mad.tuitioncounter.ui.viewmodels.AiCompanionViewModel
import ju.mad.tuitioncounter.ui.viewmodels.AuthViewModel
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission granted callback
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

        // Initialize Database and Repositories
        val tuitionDao = TuitionDatabase.getDatabase(applicationContext).tuitionDao()
        val tuitionRepository = TuitionRepositoryImpl(tuitionDao)

        val aiService = RetrofitInstance.api
        val aiRepository = AiRepositoryImpl(aiService)

        // Initialize Auth Repository
        val authRepository = AuthRepositoryImpl(FirebaseAuth.getInstance())

        // Initialize Tuition Use Cases
        val getTuitionListUseCase = GetTuitionListUseCase(tuitionRepository)
        val getTuitionDetailsUseCase = GetTuitionDetailsUseCase(tuitionRepository)
        val getClassLogsUseCase = GetClassLogsUseCase(tuitionRepository)
        val addTuitionUseCase = AddTuitionUseCase(tuitionRepository)
        val updateTuitionUseCase = UpdateTuitionUseCase(tuitionRepository)
        val deleteTuitionUseCase = DeleteTuitionUseCase(tuitionRepository)
        val logClassUseCase = LogClassUseCase(tuitionRepository)
        val deleteClassLogUseCase = DeleteClassLogUseCase(tuitionRepository)
        val resetClassCountUseCase = ResetClassCountUseCase(tuitionRepository)
        val getAiResponseUseCase = GetAiResponseUseCase(aiRepository)

        // Initialize Auth Use Cases
        val signUpUseCase = SignUpUseCase(authRepository)
        val loginUseCase = LoginUseCase(authRepository)
        val logoutUseCase = LogoutUseCase(authRepository)
        val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
        val sendPasswordResetUseCase = SendPasswordResetUseCase(authRepository)

        // Initialize ViewModels
        val tuitionViewModel = TuitionViewModel(
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

        val aiCompanionViewModel = AiCompanionViewModel(getAiResponseUseCase)

        val authViewModel = AuthViewModel(
            signUpUseCase,
            loginUseCase,
            logoutUseCase,
            getCurrentUserUseCase,
            sendPasswordResetUseCase
        )

        setContent {
            TuitionCounterTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    tuitionViewModel = tuitionViewModel,
                    aiCompanionViewModel = aiCompanionViewModel
                )
            }
        }
    }
}