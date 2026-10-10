package br.com.fisiosolutions.presentation.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fisiosolutions.R
import br.com.fisiosolutions.data.AppState
import br.com.fisiosolutions.data.repository.AuthRepository
import br.com.fisiosolutions.presentation.components.FisioLogo
import br.com.fisiosolutions.presentation.theme.FisioSolutionsTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToUserArea: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository.getInstance(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Dialog "Esqueceu sua senha?"
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var isSendingForgotLink by remember { mutableStateOf(false) }
    var forgotFeedbackMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Main Top Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Logo "FiSio SOLUTIONS"
                FisioLogo()

                Spacer(modifier = Modifier.height(24.dp))

                // E-mail Input
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            text = "E-mail",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Insira seu e-mail neste campo.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    trailingIcon = {
                        if (email.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        email = ""
                                        errorMessage = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar e-mail",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password Input
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            text = "Senha",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Insira sua senha neste campo.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye),
                                contentDescription = if (passwordVisible) "Ocultar senha" else "Exibir senha",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // "Esqueceu sua senha?" Link (US03: Disparo de link temporário)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Esqueceu sua senha?",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable {
                            forgotEmail = email
                            forgotFeedbackMessage = null
                            showForgotPasswordDialog = true
                        }
                    )
                }

                // Notificação clara de erro sem alterar o contexto da tela (US03)
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFEBEE))
                            .border(1.dp, Color(0xFFEF5350), RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Erro de autenticação",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = Color(0xFFC62828),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Primary Button "Entrar" (US03: Autenticação e validação com JWT)
                Button(
                    onClick = {
                        if (email.isBlank()) {
                            errorMessage = "Por favor, insira seu e-mail."
                            return@Button
                        }
                        if (!authRepository.isValidEmail(email)) {
                            errorMessage = "Formato de e-mail inválido."
                            return@Button
                        }
                        if (password.isBlank()) {
                            errorMessage = "Por favor, insira sua senha."
                            return@Button
                        }

                        isLoading = true
                        errorMessage = null

                        coroutineScope.launch {
                            val result = authRepository.login(email, password)
                            isLoading = false
                            if (result.success && result.user != null) {
                                AppState.currentUser = result.user
                                onNavigateToUserArea()
                            } else {
                                errorMessage = result.errorMessage
                                    ?: "Credenciais inválidas. Verifique seu e-mail e senha."
                            }
                        }
                    },
                    enabled = !isLoading && !isGoogleLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            text = "Entrar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Google Login Button (US03)
                OutlinedButton(
                    onClick = {
                        isGoogleLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            val result = authRepository.loginWithGoogle(
                                email = if (email.isNotBlank() && authRepository.isValidEmail(email)) email else "usuario.google@gmail.com",
                                fullName = "Usuário Google"
                            )
                            isGoogleLoading = false
                            if (result.success && result.user != null) {
                                AppState.currentUser = result.user
                                onNavigateToUserArea()
                            } else {
                                errorMessage = result.errorMessage ?: "Erro ao autenticar com a conta Google."
                            }
                        }
                    },
                    enabled = !isLoading && !isGoogleLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    if (isGoogleLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Entrar com o Google",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Green Bottom Section with Concave Curve
            val backgroundColor = MaterialTheme.colorScheme.background
            val secondaryColor = MaterialTheme.colorScheme.secondary

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(secondaryColor)
            ) {
                // Curved transition at top
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.TopCenter)
                ) {
                    val w = size.width
                    val h = size.height

                    val wavePath = Path().apply {
                        moveTo(0f, 0f)
                        cubicTo(
                            w * 0.35f, h * 0.9f,
                            w * 0.65f, h * 0.9f,
                            w, 0f
                        )
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(
                        path = wavePath,
                        color = secondaryColor
                    )

                    // Draw inverse curve background on top side
                    val topWhiteCurve = Path().apply {
                        moveTo(0f, 0f)
                        cubicTo(
                            w * 0.35f, h * 0.9f,
                            w * 0.65f, h * 0.9f,
                            w, 0f
                        )
                        lineTo(w, 0f)
                        lineTo(0f, 0f)
                        close()
                    }
                    drawPath(
                        path = topWhiteCurve,
                        color = backgroundColor
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Headline
                    Text(
                        text = "Seu corpo em movimento, sua vida sem limites.",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Exercise Illustrations Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.exercise_ilustration_1),
                            contentDescription = "Ilustração de exercício de alongamento: Uma mulher faz uma pose de pilates.",
                            modifier = modifier.height(132.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.exercise_ilustration_2),
                            contentDescription = "Ilustração de exercício de alongamento: Uma segunda mulher faz uma segunda pose de pilates.",
                            modifier = modifier.height(132.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // First time access prompt
                    Text(
                        text = "É seu primeiro acesso?",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // "Cadastre-se Grátis" Button
                    Button(
                        onClick = onNavigateToRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Text(
                            text = "Cadastre-se Grátis",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // US03: Dialog "Esqueceu sua senha?" para disparo de link temporário de redefinição
        if (showForgotPasswordDialog) {
            AlertDialog(
                onDismissRequest = {
                    if (!isSendingForgotLink) showForgotPasswordDialog = false
                },
                title = {
                    Text(
                        text = "Recuperar Senha",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Informe seu e-mail cadastrado. Enviaremos um link temporário para redefinição de sua senha.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = forgotEmail,
                            onValueChange = {
                                forgotEmail = it
                                forgotFeedbackMessage = null
                            },
                            label = { Text("E-mail cadastrado") },
                            placeholder = { Text("Ex: seuemail@gmail.com") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                        if (forgotFeedbackMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = forgotFeedbackMessage ?: "",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (forgotEmail.isBlank() || !authRepository.isValidEmail(forgotEmail)) {
                                forgotFeedbackMessage = "Por favor, digite um e-mail válido."
                                return@Button
                            }
                            isSendingForgotLink = true
                            forgotFeedbackMessage = null
                            coroutineScope.launch {
                                val res = authRepository.requestPasswordReset(forgotEmail)
                                isSendingForgotLink = false
                                forgotFeedbackMessage = res.message
                            }
                        },
                        enabled = !isSendingForgotLink,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (isSendingForgotLink) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Enviar link")
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showForgotPasswordDialog = false },
                        enabled = !isSendingForgotLink
                    ) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLightPreview() {
    FisioSolutionsTheme(darkTheme = false) {
        LoginScreen(onNavigateToUserArea = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenDarkPreview() {
    FisioSolutionsTheme(darkTheme = true) {
        LoginScreen(onNavigateToUserArea = {})
    }
}

