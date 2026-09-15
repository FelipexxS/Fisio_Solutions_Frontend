package br.com.fisiosolutions.presentation.exerciselist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fisiosolutions.R
import br.com.fisiosolutions.presentation.components.ExpandableHeader
import br.com.fisiosolutions.presentation.components.FisioBackButton
import br.com.fisiosolutions.presentation.components.FisioSelectInput
import br.com.fisiosolutions.presentation.theme.AccentGreen
import br.com.fisiosolutions.presentation.theme.FisioSolutionsTheme

data class Exercise(
    val id: String,
    val title: String,
    val category: String,
    val imageResId: Int,
    var isSaved: Boolean = false,
    var isCompletedToday: Boolean = false,
    val tutorialTitle: String = "ALONGAMENTO DE QUADRIL E LOMBAR",
    val instructions: String = "Para alongar a lombar, deitar de barriga para cima e dobrar os joelhos mantendo os pés no chão, ou deixando uma perna esticada. Com a ajuda das mãos, trazer um joelho em direção ao peito, mantendo essa posição por cerca de 15 segundos. Fazer o mesmo com a outra perna, repetindo o movimento por 2 vezes em cada perna.\n\nLembre-se de fazer os movimentos de forma controlada, respeitando seus limites e evitando dor intensa, e se a dor persistir, consulte um médico."
)

@Composable
fun ExerciseListScreen(
    modifier: Modifier = Modifier,
    userName: String = "John Doe",
    userProfession: String = "Analista de Desenvolvimento de Software",
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToErgonomics: () -> Unit = {},
    onNavigateToMyList: () -> Unit = {},
    onNavigateToEditAccount: () -> Unit = {},
    onSwitchUser: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Lista de Exercícios, 1 = Dicas de Ergonomia
    val categories = remember { listOf("Coluna", "Pescoço", "Braços", "Pernas") }
    var selectedCategory by remember { mutableStateOf("Coluna") }

    val exercises = remember {
        mutableStateListOf(
            Exercise("coluna_1", "Alongamentos de Lombar", "Coluna", R.drawable.exercise_ilustration_1, isSaved = false),
            Exercise("coluna_2", "Fortalecimento", "Coluna", R.drawable.exercise_ilustration_2, isSaved = false),
            Exercise("coluna_3", "Mobilidade da coluna", "Coluna", R.drawable.exercise_ilustration_3, isSaved = true),
            Exercise("coluna_4", "Alongamento Lombar Avançado", "Coluna", R.drawable.exercise_ilustration_4, isSaved = true),
            Exercise("coluna_5", "Descompressão Vertebral", "Coluna", R.drawable.exercise_ilustration_5, isSaved = true),
            Exercise("coluna_6", "Reabilitação Espinhal", "Coluna", R.drawable.exercise_ilustration_1, isSaved = true),
            Exercise("coluna_7", "Postura Lombar", "Coluna", R.drawable.exercise_ilustration_2, isSaved = true),
            Exercise("coluna_8", "Estabilização do Core", "Coluna", R.drawable.exercise_ilustration_3, isSaved = true),
            Exercise("coluna_9", "Alongamento do Psoas", "Coluna", R.drawable.exercise_ilustration_4, isSaved = false),
            Exercise("coluna_10", "Descompressão Sacroilíaca", "Coluna", R.drawable.exercise_ilustration_5, isSaved = false),

            Exercise("pescoco_1", "Alongamento Cervical", "Pescoço", R.drawable.exercise_ilustration_1, isSaved = false),
            Exercise("pescoco_2", "Alívio de Tensão no Pescoço", "Pescoço", R.drawable.exercise_ilustration_2, isSaved = true),

            Exercise("bracos_1", "Alongamento de Bíceps", "Braços", R.drawable.exercise_ilustration_3, isSaved = false),
            Exercise("bracos_2", "Mobilidade de Pulso", "Braços", R.drawable.exercise_ilustration_4, isSaved = true),

            Exercise("pernas_1", "Alongamento de Isquiotibiais", "Pernas", R.drawable.exercise_ilustration_5, isSaved = false),
            Exercise("pernas_2", "Fortalecimento de Quadríceps", "Pernas", R.drawable.exercise_ilustration_1, isSaved = true)
        )
    }

    val filteredExercises = exercises.filter { it.category == selectedCategory }
    val totalCategoryExercises = filteredExercises.size
    val totalSavedCategoryExercises = filteredExercises.count { it.isSaved }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            // Yellow Back Button
            FisioBackButton(onClick = onNavigateBack)

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

        // Body Category Selector
        FisioSelectInput(
            options = categories,
            selectedOption = selectedCategory,
            onOptionSelected = { selectedCategory = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
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
                    onItemClick = { onNavigateToDetail(exercise.id) },
                    onToggleSave = {
                        val index = exercises.indexOfFirst { e -> e.id == exercise.id }
                        if (index != -1) {
                            exercises[index] = exercises[index].copy(isSaved = !exercises[index].isSaved)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ExerciseListItem(
    exercise: Exercise,
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
