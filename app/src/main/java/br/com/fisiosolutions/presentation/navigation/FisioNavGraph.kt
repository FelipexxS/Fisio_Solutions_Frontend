package br.com.fisiosolutions.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fisiosolutions.R
import br.com.fisiosolutions.data.AppState
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
    val context = androidx.compose.ui.platform.LocalContext.current
    val sessionManager = androidx.compose.runtime.remember {
        br.com.fisiosolutions.data.local.SessionManager.getInstance(context)
    }

    val exerciseRepository = androidx.compose.runtime.remember {
        br.com.fisiosolutions.data.repository.ExerciseRepository.getInstance(context)
    }

    val ergonomicsRepository = androidx.compose.runtime.remember {
        br.com.fisiosolutions.data.repository.ErgonomicsRepository.getInstance(context)
    }

    // Inicializa o usuário da sessão ativa caso já esteja autenticado e sincroniza listas salvas e dicas de ergonomia
    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (AppState.currentUser == null && sessionManager.isLoggedIn()) {
            AppState.currentUser = sessionManager.getCurrentUser()
        }
        exerciseRepository.syncSavedExercises()
        ergonomicsRepository.syncTips()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToUserArea = {
                    navController.navigate("userArea") {
                        popUpTo("login") { inclusive = true }
                    }
                },
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
            val user = AppState.currentUser ?: sessionManager.getCurrentUser()
            UserAreaScreen(
                userName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                userProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onNavigateToExerciseSearch = { navController.navigate("exerciseList") },
                onNavigateToTutorial = { exerciseId: String -> navController.navigate("exerciseDetail/$exerciseId") },
                onNavigateToErgonomicsList = { navController.navigate("ergonomics") },
                onSwitchUser = {
                    sessionManager.clearSession()
                    AppState.currentUser = null
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("exerciseList") {
            val user = AppState.currentUser ?: sessionManager.getCurrentUser()
            ExerciseListScreen(
                userName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                userProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToHome = {
                    navController.navigate("userArea") {
                        popUpTo("userArea") { inclusive = true }
                    }
                },
                onNavigateToDetail = { exerciseId -> navController.navigate("exerciseDetail/$exerciseId") },
                onNavigateToErgonomics = {
                    navController.navigate("ergonomics") {
                        popUpTo("userArea") { inclusive = false }
                        launchSingleTop = true
                    }
                },
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
            val exercise = AppState.getExerciseById(exerciseId)

            val categoryName = exercise?.category ?: AppState.currentSelectedCategory
            val exerciseTitle = exercise?.title ?: "Alongamento da coluna"
            val tutorialTitle = exercise?.tutorialTitle ?: "ALONGAMENTO DE QUADRIL E LOMBAR"
            val imageResId = exercise?.imageResId ?: R.drawable.exercise_ilustration_1
            val instructions = exercise?.instructions ?: "Para alongar a lombar, deitar de barriga para cima e dobrar os joelhos mantendo os pés no chão, ou deixando uma perna esticada. Com a ajuda das mãos, trazer um joelho em direção ao peito, mantendo essa posição por cerca de 15 segundos. Fazer o mesmo com a outra perna, repetindo o movimento por 2 vezes em cada perna.\n\nLembre-se de fazer os movimentos de forma controlada, respeitando seus limites e evitando dor intensa, e se a dor persistir, consulte um médico."

            // Sync global currentSelectedCategory to the exercise's category
            if (exercise != null) {
                AppState.currentSelectedCategory = exercise.category
            }

            val user = AppState.currentUser ?: sessionManager.getCurrentUser()

            ExerciseDetailScreen(
                exerciseId = exerciseId,
                exerciseTitle = exerciseTitle,
                categoryName = categoryName,
                tutorialTitle = tutorialTitle,
                imageResId = imageResId,
                instructions = instructions,
                userName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                userProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToHome = {
                    navController.navigate("userArea") {
                        popUpTo("userArea") { inclusive = true }
                    }
                },
                onNavigateToExerciseList = {
                    navController.navigate("exerciseList") {
                        popUpTo("userArea")
                    }
                },
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
            val user = AppState.currentUser ?: sessionManager.getCurrentUser()
            ErgonomicsListScreen(
                userName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                userProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToHome = {
                    navController.navigate("userArea") {
                        popUpTo("userArea") { inclusive = true }
                    }
                },
                onNavigateToDetail = { tipId -> navController.navigate("ergonomicsDetail/$tipId") },
                onNavigateToExerciseSearch = {
                    navController.navigate("exerciseList") {
                        popUpTo("userArea") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    sessionManager.clearSession()
                    AppState.currentUser = null
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCreateTip = { title, content ->
                    ergonomicsRepository.createTip(title, content)
                }
            )
        }
        composable(
            route = "ergonomicsDetail/{tipId}",
            arguments = listOf(navArgument("tipId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tipId = backStackEntry.arguments?.getString("tipId") ?: "tip_1"
            val tip = AppState.getErgonomicsTipById(tipId)
            val tipTitle = tip?.title ?: "Dica de Ergonomia"
            val imageResId = tip?.imageResId ?: R.drawable.ergonomics_banner
            val content = tip?.content ?: "Conteúdo detalhado da dica de ergonomia..."
            val user = AppState.currentUser ?: sessionManager.getCurrentUser()

            ErgonomicsDetailScreen(
                tipTitle = tipTitle,
                imageResId = imageResId,
                content = content,
                userName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                userProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToHome = {
                    navController.navigate("userArea") {
                        popUpTo("userArea") { inclusive = true }
                    }
                },
                onNavigateToErgonomicsList = {
                    navController.navigate("ergonomics") {
                        popUpTo("userArea")
                    }
                },
                onNavigateToMyList = { navController.navigate("myList") },
                onNavigateToEditAccount = { navController.navigate("editAccount") },
                onSwitchUser = {
                    sessionManager.clearSession()
                    AppState.currentUser = null
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("myList") {
            val user = AppState.currentUser ?: sessionManager.getCurrentUser()
            MyExercisesScreen(
                userName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                userProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
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
            val user = AppState.currentUser ?: sessionManager.getCurrentUser()
            EditAccountScreen(
                initialFullName = user?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
                initialBirthDate = user?.birthDate ?: "",
                initialEmail = user?.email?.ifBlank { "usuario@email.com" } ?: "usuario@email.com",
                initialProfession = user?.profession?.ifBlank { "Paciente" } ?: "Paciente",
                isGoogleUser = user?.isGoogleUser == true,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { updatedName, updatedBirth, updatedEmail, updatedProfession ->
                    val currentUser = AppState.currentUser ?: sessionManager.getCurrentUser()
                    if (currentUser != null) {
                        val updatedUser = currentUser.copy(
                            fullName = updatedName,
                            birthDate = if (currentUser.isGoogleUser) currentUser.birthDate else updatedBirth,
                            email = if (currentUser.isGoogleUser) currentUser.email else updatedEmail,
                            profession = updatedProfession
                        )
                        AppState.currentUser = updatedUser
                        sessionManager.updateUserProfile(updatedUser)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}
