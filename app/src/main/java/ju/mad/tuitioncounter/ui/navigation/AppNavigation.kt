package ju.mad.tuitioncounter.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ju.mad.tuitioncounter.ui.screens.AddTuitionScreen
import ju.mad.tuitioncounter.ui.screens.TuitionDetailScreen
import ju.mad.tuitioncounter.ui.screens.TuitionListScreen
import ju.mad.tuitioncounter.ui.viewmodels.TuitionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: TuitionViewModel) {
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
                    TuitionListScreen(navController = navController, viewModel = viewModel)
                }
                composable("add_tuition_screen") {
                    AddTuitionScreen(navController = navController, viewModel = viewModel)
                }
                composable("tuition_detail_screen/{tuitionId}") { backStackEntry ->
                    val tuitionId = backStackEntry.arguments?.getString("tuitionId")?.toLongOrNull() ?: 0L
                    TuitionDetailScreen(
                        navController = navController,
                        tuitionId = tuitionId,
                        viewModel = viewModel
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

        // Tuitions Button
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

        // AI Agent Button (Disabled)
        Button(
            onClick = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "AI Agent (Coming Soon)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button (Disabled)
        Button(
            onClick = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout (Coming Soon)")
        }
    }
}