package br.com.fisiosolutions.presentation.ergonomicslist

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import br.com.fisiosolutions.presentation.theme.FisioSolutionsTheme

data class ErgonomicsTip(
    val id: String,
    val title: String,
    val imageResId: Int,
    val content: String = "Conteúdo detalhado da dica de ergonomia..."
)

@Composable
fun ErgonomicsListScreen(
    modifier: Modifier = Modifier,
    userName: String = "John Doe",
    userProfession: String = "Analista de Desenvolvimento de Software",
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToExerciseSearch: () -> Unit = {},
    onNavigateToMyList: () -> Unit = {},
    onNavigateToEditAccount: () -> Unit = {},
    onSwitchUser: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(1) } // 0 = Lista de Exercícios, 1 = Dicas de Ergonomia

    val ergonomicsTips = remember {
        listOf(
            ErgonomicsTip(
                id = "tip_1",
                title = "9 Exercícios Simples Para a Melhorar a Postura",
                imageResId = R.drawable.exercise_ilustration_1,
                content = "A boa postura não é apenas uma questão estética, mas fundamental para a saúde da sua coluna. Exercícios simples de alongamento e fortalecimento diários ajudam a prevenir dores, melhorar a respiração e aumentar a disposição.\n\nPrincipais hábitos recomendados:\n1. Manter os ombros relaxados e para trás.\n2. Alinhar as orelhas com a linha dos ombros.\n3. Apoiar ambos os pés firmemente no chão."
            ),
            ErgonomicsTip(
                id = "tip_2",
                title = "5 Dicas de Ergonomia para Melhorar seu Home Office",
                imageResId = R.drawable.ergonomics_banner,
                content = "Trabalhar de casa trouxe liberdade, mas também alguns desafios para a nossa saúde física. A diferença entre uma postura improvisada no sofá e um setup ajustado é o que define se você terminará o dia com produtividade ou com dores nas costas.\n\nAqui estão 5 dicas essenciais para transformar seu ambiente de trabalho:\n\n1. Ajuste a Altura do Monitor\n    a. Olhar para baixo por horas é o caminho mais rápido para a \"síndrome do pescoço tecnológico\". Como mostra o lado direito da imagem, o ideal é que o topo da tela esteja na altura dos seus olhos.\n\n2. Mantenha os Pés Apoiados\n    a. Seus pés devem ficar totalmente apoiados no chão ou em um descanso para pés para evitar pressão excessiva na parte posterior das coxas."
            ),
            ErgonomicsTip(
                id = "tip_3",
                title = "Acessórios para Melhorar a Ergonomia no Escritório",
                imageResId = R.drawable.exercise_ilustration_2,
                content = "Investir nos acessórios certos pode transformar completamente a sua experiência de trabalho no dia a dia.\n\nAcessórios essenciais:\n- Suporte para notebook ajustável\n- Teclado e mouse ergonômicos externos\n- Apoio para os pés e descanso de pulso"
            )
        )
    }

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
                        .clickable {
                            selectedTab = 0
                            onNavigateToExerciseSearch()
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Lista de Exercícios",
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
                        .clickable { selectedTab = 1 }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Dicas de Ergonomia",
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

        Spacer(modifier = Modifier.height(14.dp))

        // Total Count Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total de Dicas: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = ergonomicsTips.size.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Section Divider Line
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ergonomics Tips List
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
                items = ergonomicsTips,
                key = { it.id }
            ) { tip ->
                ErgonomicsTipItem(
                    tip = tip,
                    onItemClick = { onNavigateToDetail(tip.id) }
                )
            }
        }
    }
}

@Composable
private fun ErgonomicsTipItem(
    tip: ErgonomicsTip,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tip Title (Underlined & Bold)
        Text(
            text = tip.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 17.sp,
                lineHeight = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tip Illustration Image Card Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Image(
                painter = painterResource(id = tip.imageResId),
                contentDescription = tip.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErgonomicsListScreenLightPreview() {
    FisioSolutionsTheme(darkTheme = false) {
        ErgonomicsListScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErgonomicsListScreenDarkPreview() {
    FisioSolutionsTheme(darkTheme = true) {
        ErgonomicsListScreen(isDarkTheme = true)
    }
}
