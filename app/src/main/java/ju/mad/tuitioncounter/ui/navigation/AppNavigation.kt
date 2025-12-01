// File: ui/navigation/AppNavigation.kt
package ju.mad.tuitioncounter.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ju.mad.tuitioncounter.ui.screens.*
import ju.mad.tuitioncounter.ui.viewmodels.AiCompanionViewModel
import ju.mad.tuitioncounter.ui.viewmodels.AuthViewModel
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    tuitionViewModel: TuitionViewModel,
    aiCompanionViewModel: AiCompanionViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    // Determine start destination based on auth state
    val startDestination = if (authState.currentUser != null) {
        "tuition_list_screen"
    } else {
        "login_screen"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Screens
        composable("login_screen") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        composable("signup_screen") {
            SignUpScreen(navController = navController, viewModel = authViewModel)
        }

        // Main App Screens (Protected)
        composable("tuition_list_screen") {
            AuthenticatedScreen(authState.currentUser != null, navController) {
                TuitionListScreenWithDrawer(
                    navController = navController,
                    tuitionViewModel = tuitionViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable("add_tuition_screen") {
            AuthenticatedScreen(authState.currentUser != null, navController) {
                AddTuitionScreen(navController = navController, viewModel = tuitionViewModel)
            }
        }

        composable("tuition_detail_screen/{tuitionId}") { backStackEntry ->
            AuthenticatedScreen(authState.currentUser != null, navController) {
                val tuitionId = backStackEntry.arguments?.getString("tuitionId")?.toLongOrNull() ?: 0L
                TuitionDetailScreen(
                    navController = navController,
                    tuitionId = tuitionId,
                    viewModel = tuitionViewModel
                )
            }
        }

        composable("log_class_screen/{tuitionId}") { backStackEntry ->
            AuthenticatedScreen(authState.currentUser != null, navController) {
                val tuitionId = backStackEntry.arguments?.getString("tuitionId")?.toLongOrNull() ?: 0L
                LogClassScreen(
                    navController = navController,
                    tuitionId = tuitionId,
                    viewModel = tuitionViewModel
                )
            }
        }

        composable("ai_companion_screen") {
            AuthenticatedScreen(authState.currentUser != null, navController) {
                AiCompanionScreen(viewModel = aiCompanionViewModel)
            }
        }
    }
}

@Composable
fun AuthenticatedScreen(
    isAuthenticated: Boolean,
    navController: NavController,
    content: @Composable () -> Unit
) {
    if (isAuthenticated) {
        content()
    } else {
        navController.navigate("login_screen") {
            popUpTo(0) { inclusive = true }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuitionListScreenWithDrawer(
    navController: NavController,
    tuitionViewModel: TuitionViewModel,
    authViewModel: AuthViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authState by authViewModel.authState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    navController = navController,
                    authViewModel = authViewModel,
                    currentUser = authState.currentUser,
                    onCloseDrawer = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Tuition Counter") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                TuitionListScreen(navController = navController, viewModel = tuitionViewModel)
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    currentUser: ju.mad.tuitioncounter.domain.model.UserData?,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User Info
        currentUser?.let { user ->
            Text(
                text = user.username ?: "User",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = user.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
        }

        Button(
            onClick = {
                navController.navigate("tuition_list_screen")
                onCloseDrawer()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Tuitions")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("ai_companion_screen")
                onCloseDrawer()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "AI Agent")
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                authViewModel.logout()
                onCloseDrawer()
                navController.navigate("login_screen") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}