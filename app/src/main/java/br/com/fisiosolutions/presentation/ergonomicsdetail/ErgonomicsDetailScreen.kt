package br.com.fisiosolutions.presentation.ergonomicsdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import br.com.fisiosolutions.R
import br.com.fisiosolutions.presentation.components.BreadcrumbItem
import br.com.fisiosolutions.presentation.components.ExpandableHeader
import br.com.fisiosolutions.presentation.components.FisioBreadcrumb
import br.com.fisiosolutions.presentation.components.FisioHomeButton
import br.com.fisiosolutions.presentation.theme.FisioSolutionsTheme

@Composable
fun ErgonomicsDetailScreen(
    modifier: Modifier = Modifier,
    tipId: String = "tip_2",
    tipNumericId: Int = 0,
    categoryName: String = "Ergonomia",
    tipTitle: String = "5 Dicas de Ergonomia para o seu Home Office",
    imageResId: Int = R.drawable.ergonomics_banner,
    content: String = "Trabalhar de casa trouxe liberdade, mas também alguns desafios para a nossa saúde física. A diferença entre uma postura improvisada no sofá e um setup ajustado é o que define se você terminará o dia com produtividade ou com dores nas costas.\n\nAqui estão 5 dicas essenciais para transformar seu ambiente de trabalho:\n\n1. Ajuste a Altura do Monitor\n    a. Olhar para baixo por horas é o caminho mais rápido para a \"síndrome do pescoço tecnológico\". Como mostra o lado direito da imagem, o ideal é que o topo da tela esteja na altura dos seus olhos.",
    userName: String = "John Doe",
    userProfession: String = "Analista de Desenvolvimento de Software",
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToErgonomicsList: () -> Unit = {},
    onNavigateToMyList: () -> Unit = {},
    onNavigateToEditAccount: () -> Unit = {},
    onSwitchUser: () -> Unit = {},
    onDeleteTip: (Int) -> Unit = {},
    onUpdateTip: (Int, String, String) -> Unit = { _, _, _ -> }
) {
    val scrollState = rememberScrollState()
    var showEditDialog by remember { mutableStateOf(false) }
    var currentTitle by remember(tipTitle) { mutableStateOf(tipTitle) }
    var currentContent by remember(content) { mutableStateOf(content) }
    var editTitle by remember(currentTitle) { mutableStateOf(currentTitle) }
    var editContent by remember(currentContent) { mutableStateOf(currentContent) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Top Navigation Row: Home Button & Interactive Breadcrumb Component
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FisioHomeButton(onClick = onNavigateToHome)

                    Spacer(modifier = Modifier.width(16.dp))

                    val breadcrumbItems = remember(categoryName, currentTitle, onNavigateToErgonomicsList) {
                        listOf(
                            BreadcrumbItem(
                                title = categoryName,
                                onClick = onNavigateToErgonomicsList
                            ),
                            BreadcrumbItem(
                                title = currentTitle,
                                onClick = null
                            )
                        )
                    }

                    FisioBreadcrumb(
                        items = breadcrumbItems,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Tip Title
                Text(
                    text = currentTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 26.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Hero Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = currentTitle,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Formatted Content Text (News article style)
                Text(
                    text = currentContent,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 32.sp,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botões de Ações CRUD (Editar / Excluir)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            editTitle = currentTitle
                            editContent = currentContent
                            showEditDialog = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Editar Dica")
                    }

                    Button(
                        onClick = {
                            val id = tipNumericId.takeIf { it > 0 }
                                ?: tipId.removePrefix("tip_").toIntOrNull()
                                ?: 1
                            onDeleteTip(id)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = "Excluir Dica")
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = {
                    Text(
                        text = "Editar Dica de Ergonomia",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Título da Dica") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editContent,
                            onValueChange = { editContent = it },
                            label = { Text("Orientações e Passos") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 6
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (editTitle.isNotBlank() && editContent.isNotBlank()) {
                                val id = tipNumericId.takeIf { it > 0 }
                                    ?: tipId.removePrefix("tip_").toIntOrNull()
                                    ?: 1
                                onUpdateTip(id, editTitle.trim(), editContent.trim())
                                currentTitle = editTitle.trim()
                                currentContent = editContent.trim()
                                showEditDialog = false
                            }
                        }
                    ) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showEditDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErgonomicsDetailScreenLightPreview() {
    FisioSolutionsTheme(darkTheme = false) {
        ErgonomicsDetailScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErgonomicsDetailScreenDarkPreview() {
    FisioSolutionsTheme(darkTheme = true) {
        ErgonomicsDetailScreen(isDarkTheme = true)
    }
}
