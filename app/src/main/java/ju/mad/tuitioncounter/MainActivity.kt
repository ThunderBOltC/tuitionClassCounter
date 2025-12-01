package ju.mad.tuitioncounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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

    private var keepSplash = true  // Controls splash dismissal

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Notifications disabled. Some reminders won't work.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Install splash screen
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplash }

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

        // Simulate loading (e.g., auth check) – replace with real async load if needed
        Thread.sleep(2000)  // Demo delay – remove in production
        keepSplash = false

        // Initialize Database and Repositories (unchanged)
        val tuitionDao = TuitionDatabase.getDatabase(applicationContext).tuitionDao()
        val tuitionRepository = TuitionRepositoryImpl(tuitionDao)

        val aiService = RetrofitInstance.api
        val aiRepository = AiRepositoryImpl(aiService)

        val authRepository = AuthRepositoryImpl(FirebaseAuth.getInstance())

        // Use Cases (unchanged)
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

        val signUpUseCase = SignUpUseCase(authRepository)
        val loginUseCase = LoginUseCase(authRepository)
        val logoutUseCase = LogoutUseCase(authRepository)
        val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
        val sendPasswordResetUseCase = SendPasswordResetUseCase(authRepository)

        // ViewModels (unchanged)
        val tuitionViewModel = TuitionViewModel(
            getTuitionListUseCase, getTuitionDetailsUseCase, getClassLogsUseCase,
            addTuitionUseCase, updateTuitionUseCase, deleteTuitionUseCase,
            logClassUseCase, deleteClassLogUseCase, resetClassCountUseCase
        )

        val aiCompanionViewModel = AiCompanionViewModel(getAiResponseUseCase)

        val authViewModel = AuthViewModel(
            signUpUseCase, loginUseCase, logoutUseCase,
            getCurrentUserUseCase, sendPasswordResetUseCase
        )

        setContent {
            TuitionCounterTheme(dynamicColor = true) {  // Enable dynamic theming
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        authViewModel = authViewModel,
                        tuitionViewModel = tuitionViewModel,
                        aiCompanionViewModel = aiCompanionViewModel
                    )
                }
            }
        }
    }
}