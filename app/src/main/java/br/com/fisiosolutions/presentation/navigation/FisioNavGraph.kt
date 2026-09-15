package br.com.fisiosolutions.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fisiosolutions.presentation.editaccount.EditAccountScreen
import br.com.fisiosolutions.presentation.ergonomicsdetail.ErgonomicsDetailScreen
import br.com.fisiosolutions.presentation.ergonomicslist.ErgonomicsListScreen
import br.com.fisiosolutions.presentation.exercisedetail.ExerciseDetailScreen
import br.com.fisiosolutions.presentation.exerciselist.ExerciseListScreen
import br.com.fisiosolutions.presentation.login.LoginScreen
import br.com.fisiosolutions.presentation.myexercises.MyExercisesScreen
import br.com.fisiosolutions.presentation.register.RegisterScreen
import br.com.fisiosolutions.presentation.userarea.UserAreaScreen

@Composable
fun FisioNavGraph(
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "login"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToUserArea = { navController.navigate("userArea") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToUserArea = {
                    navController.navigate("userArea") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("userArea") {
            UserAreaScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onNavigateToExerciseSearch = { navController.navigate("exerciseList") },
                onNavigateToTutorial = { exerciseId: String -> navController.navigate("exerciseDetail/$exerciseId") },
                onNavigateToErgonomicsList = { navController.navigate("ergonomics") },
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("exerciseList") {
            ExerciseListScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { exerciseId -> navController.navigate("exerciseDetail/$exerciseId") },
                onNavigateToErgonomics = { navController.navigate("ergonomics") },
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "exerciseDetail/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: "coluna_1"
            ExerciseDetailScreen(
                exerciseId = exerciseId,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("ergonomics") {
            ErgonomicsListScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { tipId -> navController.navigate("ergonomicsDetail/$tipId") },
                onNavigateToExerciseSearch = { navController.navigate("exerciseList") },
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "ergonomicsDetail/{tipId}",
            arguments = listOf(navArgument("tipId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tipId = backStackEntry.arguments?.getString("tipId") ?: "tip_1"
            ErgonomicsDetailScreen(
                tipId = tipId,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("myList") {
            MyExercisesScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { exerciseId -> navController.navigate("exerciseDetail/$exerciseId") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("editAccount") {
            EditAccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}
