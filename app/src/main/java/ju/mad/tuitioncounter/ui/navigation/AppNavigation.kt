package ju.mad.tuitioncounter.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// import androidx.hilt.navigation.compose.hiltViewModel // REMOVED: No longer needed
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ju.mad.tuitioncounter.ui.screens.*
import ju.mad.tuitioncounter.ui.viewmodels.AiCompanionViewModel
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// FIX 1: Accept both ViewModels as parameters
fun AppNavigation(
    tuitionViewModel: TuitionViewModel,
    aiCompanionViewModel: AiCompanionViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    navController = navController,
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
            NavHost(
                navController = navController,
                startDestination = "tuition_list_screen",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("tuition_list_screen") {
                    TuitionListScreen(navController = navController, viewModel = tuitionViewModel)
                }
                composable("add_tuition_screen") {
                    AddTuitionScreen(navController = navController, viewModel = tuitionViewModel)
                }
                composable("tuition_detail_screen/{tuitionId}") { backStackEntry ->
                    val tuitionId = backStackEntry.arguments?.getString("tuitionId")?.toLongOrNull() ?: 0L
                    TuitionDetailScreen(
                        navController = navController,
                        tuitionId = tuitionId,
                        viewModel = tuitionViewModel
                    )
                }
                composable("log_class_screen/{tuitionId}") { backStackEntry ->
                    val tuitionId = backStackEntry.arguments?.getString("tuitionId")?.toLongOrNull() ?: 0L
                    LogClassScreen(
                        navController = navController,
                        tuitionId = tuitionId,
                        viewModel = tuitionViewModel
                    )
                }
                composable("ai_companion_screen") {
                    // FIX 2: Remove hiltViewModel() and pass the ViewModel from the function parameter
                    AiCompanionScreen(
                        viewModel = aiCompanionViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout (Coming Soon)")
        }
    }
}
