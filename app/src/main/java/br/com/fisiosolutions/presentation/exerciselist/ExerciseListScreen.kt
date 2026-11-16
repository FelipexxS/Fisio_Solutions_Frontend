package br.com.fisiosolutions.presentation.exerciselist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fisiosolutions.data.AppState
import br.com.fisiosolutions.data.repository.ExerciseRepository
import br.com.fisiosolutions.domain.model.Exercise
import br.com.fisiosolutions.presentation.components.ExpandableHeader
import br.com.fisiosolutions.presentation.components.FisioHomeButton
import br.com.fisiosolutions.presentation.components.FisioSelectInput
import br.com.fisiosolutions.presentation.theme.AccentGreen
import br.com.fisiosolutions.presentation.theme.FisioSolutionsTheme
import kotlinx.coroutines.launch

@Composable
fun ExerciseListScreen(
    modifier: Modifier = Modifier,
    userName: String = AppState.currentUser?.fullName?.ifBlank { "Usuário Cadastrado" } ?: "Usuário Cadastrado",
    userProfession: String = AppState.currentUser?.profession?.ifBlank { "Paciente" } ?: "Paciente",
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToErgonomics: () -> Unit = {},
    onNavigateToMyList: () -> Unit = {},
    onNavigateToEditAccount: () -> Unit = {},
    onSwitchUser: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val exerciseRepository = remember {
        ExerciseRepository.getInstance(context)
    }

    var selectedTab by remember { mutableStateOf(0) } // 0 = Lista de Exercícios, 1 = Dicas de Ergonomia
    val categories = AppState.categories
    val selectedCategory = AppState.currentSelectedCategory

    // Estados da Busca Semântica por IA ("Descreva sua dor")
    var showPainDialog by remember { mutableStateOf(false) }
    var complaintInput by remember { mutableStateOf("") }
    var dialogErrorMessage by remember { mutableStateOf<String?>(null) }
    var isSearchingSemantic by remember { mutableStateOf(false) }
    var activeComplaintQuery by remember { mutableStateOf<String?>(null) }
    var recommendedNumericIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var recommendationReasons by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }

    val filteredExercises = if (activeComplaintQuery != null && recommendedNumericIds.isNotEmpty()) {
        recommendedNumericIds.mapNotNull { numId ->
            AppState.allExercises.find { it.numericId == numId }
        }
    } else {
        AppState.allExercises.filter { it.category == selectedCategory }
    }

    val totalCategoryExercises = filteredExercises.size
    val totalSavedCategoryExercises = filteredExercises.count { it.isSaved }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Expandable Header Component
            ExpandableHeader(
                userName = userName,
                userProfession = userProfession,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onNavigateToMyList = onNavigateToMyList,
                onNavigateToEditAccount = onNavigateToEditAccount,
                onSwitchUser = onSwitchUser
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Top Controls Header: Back Button & Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FisioHomeButton(onClick = onNavigateToHome)

                Spacer(modifier = Modifier.width(16.dp))

                // Accessible Tab Controls Row
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tab 1: Lista de Exercícios
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedTab = 0 }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Exercícios",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (selectedTab == 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(3.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }

                    // Tab 2: Dicas de Ergonomia
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedTab = 1
                                onNavigateToErgonomics()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Ergonomia",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (selectedTab == 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(3.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body Category Selector + "Descreva sua dor" Semantic Search Button
            val selectorDisplayValue = if (activeComplaintQuery != null) {
                "Recomendação baseada em IA"
            } else {
                selectedCategory
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FisioSelectInput(
                    options = categories,
                    selectedOption = selectorDisplayValue,
                    onOptionSelected = {
                        AppState.currentSelectedCategory = it
                        // Ao trocar a região manualmente, limpa o filtro de busca semântica
                        activeComplaintQuery = null
                        recommendedNumericIds = emptyList()
                        recommendationReasons = emptyMap()
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Button(
                    onClick = {
                        dialogErrorMessage = null
                        showPainDialog = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .heightIn(min = 42.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(10.dp))
                        .semantics {
                            contentDescription = "Descreva sua dor. Abre janela para busca inteligente de exercícios por sintomas."
                        }
                ) {
                    Text(
                        text = "Descreva sua dor",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Indicador de busca semântica ativa com opção de limpar filtro
            if (activeComplaintQuery != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recomendados para: \"$activeComplaintQuery\"",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Limpar busca",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(role = Role.Button) {
                                activeComplaintQuery = null
                                recommendedNumericIds = emptyList()
                                recommendationReasons = emptyMap()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .semantics {
                                contentDescription = "Limpar busca por sintomas e voltar para a categoria $selectedCategory"
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Totals Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Total de Exercícios: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$totalCategoryExercises",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Exercícios Salvos: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$totalSavedCategoryExercises",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Section Divider Line
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exercise List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 28.dp
                )
            ) {
                items(
                    items = filteredExercises,
                    key = { it.id }
                ) { exercise ->
                    ExerciseListItem(
                        exercise = exercise,
                        recommendationReason = recommendationReasons[exercise.numericId],
                        onItemClick = { onNavigateToDetail(exercise.id) },
                        onToggleSave = {
                            if (exercise.isSaved) {
                                exerciseRepository.removeExerciseFromUserList(exercise.id)
                            } else {
                                exerciseRepository.addExerciseToUserList(exercise.id)
                            }
                        }
                    )
                }
            }
        }

        // Pop-up (AlertDialog) para Pesquisa Semântica por IA ("Descreva sua dor")
        if (showPainDialog) {
            AlertDialog(
                onDismissRequest = {
                    if (!isSearchingSemantic) showPainDialog = false
                },
                title = {
                    Text(
                        text = "Descreva sua dor",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Conte o que você está sentindo (ex: formigamento no punho ao digitar, dor na lombar sentado) para recomendarmos os melhores exercícios.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = complaintInput,
                            onValueChange = {
                                complaintInput = it
                                dialogErrorMessage = null
                            },
                            label = {
                                Text(
                                    text = "Sua queixa ou desconforto",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Ex: Dor no pescoço ao usar o computador",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            minLines = 2,
                            maxLines = 4,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .semantics {
                                    contentDescription = "Campo de texto para descrever sua dor ou sintoma"
                                    stateDescription = "Descreva o que está sentindo para buscar exercícios recomendados"
                                },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            trailingIcon = {
                                if (complaintInput.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            complaintInput = ""
                                            dialogErrorMessage = null
                                        },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Limpar descrição da dor",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        if (dialogErrorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dialogErrorMessage ?: "",
                                color = Color(0xFFD32F2F),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val trimmed = complaintInput.trim()
                            if (trimmed.isBlank()) {
                                dialogErrorMessage = "Por favor, descreva o que está sentindo."
                                return@Button
                            }
                            isSearchingSemantic = true
                            dialogErrorMessage = null
                            coroutineScope.launch {
                                val recommendations = exerciseRepository.recommendExercisesByComplaint(trimmed)
                                isSearchingSemantic = false
                                if (recommendations.isNotEmpty()) {
                                    recommendedNumericIds = recommendations.map { it.numericId }
                                    recommendationReasons = recommendations
                                        .filter { it.motivoRecomendacao.isNotBlank() }
                                        .associate { it.numericId to it.motivoRecomendacao }
                                    activeComplaintQuery = trimmed
                                    showPainDialog = false
                                } else {
                                    dialogErrorMessage = "Não encontramos exercícios para essa descrição. Tente detalhar a região ou sintoma."
                                }
                            }
                        },
                        enabled = !isSearchingSemantic,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (isSearchingSemantic) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Buscar",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showPainDialog = false },
                        enabled = !isSearchingSemantic
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun ExerciseListItem(
    exercise: Exercise,
    recommendationReason: String? = null,
    onItemClick: () -> Unit,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Exercise Title (Underlined & Bold)
        Text(
            text = exercise.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        if (!recommendationReason.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recommendationReason,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Exercise Illustration Image in Rounded Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFC8E6C9).copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = exercise.imageResId),
                contentDescription = exercise.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Status: "Adicionar a Lista" Button OR "Salvo ✓" Indicator
        if (exercise.isSaved) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable(onClick = onToggleSave)
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = "Salvo",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Exercício Salvo",
                    tint = AccentGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Button(
                onClick = onToggleSave,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.shadow(2.dp, shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "Adicionar a Lista",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseListScreenLightPreview() {
    FisioSolutionsTheme(darkTheme = false) {
        ExerciseListScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseListScreenDarkPreview() {
    FisioSolutionsTheme(darkTheme = true) {
        ExerciseListScreen(isDarkTheme = true)
    }
}
