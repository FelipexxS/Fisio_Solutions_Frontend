package br.com.fisiosolutions.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fisiosolutions.presentation.editaccount.EditAccountScreen
import br.com.fisiosolutions.presentation.login.LoginScreen
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
                onSwitchUser = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("myList") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Minha Lista (Exercícios Salvos)",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        composable("editAccount") {
            EditAccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}
