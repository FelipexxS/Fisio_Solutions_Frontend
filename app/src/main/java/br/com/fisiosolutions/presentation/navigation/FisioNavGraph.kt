package br.com.fisiosolutions.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fisiosolutions.presentation.login.LoginScreen
import br.com.fisiosolutions.presentation.register.RegisterScreen
import br.com.fisiosolutions.presentation.userarea.UserAreaScreen

@Composable
fun FisioNavGraph(
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
            UserAreaScreen()
        }
    }
}
