package br.com.fisiosolutions.presentation.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fisiosolutions.R
import br.com.fisiosolutions.data.AppState
import br.com.fisiosolutions.data.repository.AuthRepository
import br.com.fisiosolutions.presentation.components.FisioLogo
import br.com.fisiosolutions.presentation.theme.FisioSolutionsTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToUserArea: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository.getInstance(context) }

    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var generalError by remember { mutableStateOf<String?>(null) }

    // Erros específicos de cada campo
    var nameError by remember { mutableStateOf<String?>(null) }
    var birthError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var professionError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Top Logo @fisio_solutions_logo.xml
                FisioLogo()

                Spacer(modifier = Modifier.height(24.dp))

                // Banner de Erro Geral (ex: e-mail já existente)
                if (generalError != null) {
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
                                contentDescription = "Aviso de erro",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = generalError ?: "",
                                color = Color(0xFFC62828),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 1. Nome Completo Input
                RegisterTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        nameError = null
                        generalError = null
                    },
                    label = "Nome Completo",
                    placeholder = "Ex: John Doe Da Silva",
                    semanticLabel = "Preencha seu nome completo neste campo.",
                    isError = nameError != null,
                    supportingText = nameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text
                    ),
                    onClear = {
                        fullName = ""
                        nameError = null
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Data de Nascimento Input
                RegisterTextField(
                    value = birthDate,
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }
                        if (digits.length <= 8) {
                            birthDate = digits
                            birthError = null
                            generalError = null
                        }
                    },
                    label = "Data de Nascimento",
                    placeholder = "Ex: 05/09/1999",
                    semanticLabel = "Preencha sua data de nascimento começando pelo dia, depois mês e finalmente ano, neste campo.",
                    isError = birthError != null,
                    supportingText = birthError,
                    visualTransformation = DateVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onClear = {
                        birthDate = ""
                        birthError = null
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. E-mail Input (US02: Validação de formato)
                RegisterTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                        generalError = null
                    },
                    label = "E-mail",
                    placeholder = "Ex: JohnDoe@gmail.com",
                    semanticLabel = "Preencha o e-mail que deseja cadastrar neste campo.",
                    isError = emailError != null,
                    supportingText = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onClear = {
                        email = ""
                        emailError = null
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Profissão Input
                RegisterTextField(
                    value = profession,
                    onValueChange = {
                        profession = it
                        professionError = null
                        generalError = null
                    },
                    label = "Profissão",
                    placeholder = "Ex: Analista de Desenvolvimento...",
                    semanticLabel = "Preencha sua profissão neste campo.",
                    isError = professionError != null,
                    supportingText = professionError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    onClear = {
                        profession = ""
                        professionError = null
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Senha Input (US02)
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                        confirmPasswordError = null
                        generalError = null
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
                            text = "Digite sua senha",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    isError = passwordError != null,
                    supportingText = if (passwordError != null) {
                        { Text(passwordError ?: "", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .semantics {
                            stateDescription = "Insira a senha desejada"
                        },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = if (passwordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                        unfocusedIndicatorColor = if (passwordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
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

                Spacer(modifier = Modifier.height(16.dp))

                // 6. Confirmar Senha Input (US02: Conferência obrigatória)
                TextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                        generalError = null
                    },
                    label = {
                        Text(
                            text = "Confirmar Senha",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Confirme sua senha",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    isError = confirmPasswordError != null,
                    supportingText = if (confirmPasswordError != null) {
                        { Text(confirmPasswordError ?: "", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .semantics {
                            contentDescription = "Repita nesse campo a mesma senha que cadastrou no campo de senha."
                        },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = if (confirmPasswordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                        unfocusedIndicatorColor = if (confirmPasswordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                painter = painterResource(id = if (confirmPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye),
                                contentDescription = if (confirmPasswordVisible) "Ocultar senha" else "Exibir senha",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Primary Button "Cadastrar" (US02: Persistência e validação)
                Button(
                    onClick = {
                        var hasError = false

                        if (fullName.isBlank()) {
                            nameError = "Nome completo é obrigatório."
                            hasError = true
                        }
                        if (birthDate.length < 8) {
                            birthError = "Data de nascimento inválida (DD/MM/AAAA)."
                            hasError = true
                        }
                        if (email.isBlank()) {
                            emailError = "E-mail é obrigatório."
                            hasError = true
                        } else if (!authRepository.isValidEmail(email)) {
                            emailError = "Formato de e-mail inválido."
                            hasError = true
                        }
                        if (profession.isBlank()) {
                            professionError = "Profissão é obrigatória."
                            hasError = true
                        }
                        if (password.length < 6) {
                            passwordError = "A senha deve ter no mínimo 6 caracteres."
                            hasError = true
                        }
                        if (password != confirmPassword) {
                            confirmPasswordError = "As senhas não coincidem."
                            hasError = true
                        }

                        if (hasError) return@Button

                        isLoading = true
                        generalError = null

                        // Formatação da data de nascimento para DD/MM/AAAA
                        val formattedBirthDate = if (birthDate.length == 8) {
                            "${birthDate.substring(0, 2)}/${birthDate.substring(2, 4)}/${birthDate.substring(4, 8)}"
                        } else {
                            birthDate
                        }

                        coroutineScope.launch {
                            val result = authRepository.register(
                                fullName = fullName,
                                birthDate = formattedBirthDate,
                                email = email,
                                profession = profession,
                                password = password,
                                confirmPassword = confirmPassword
                            )
                            isLoading = false

                            if (result.success && result.user != null) {
                                AppState.currentUser = result.user
                                onNavigateToUserArea()
                            } else {
                                generalError = result.errorMessage
                                    ?: "Erro ao criar conta. Verifique os dados e tente novamente."
                            }
                        }
                    },
                    enabled = !isLoading,
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
                            text = "Cadastrar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Secondary Button "Cancelar"
                Button(
                    onClick = onNavigateToLogin,
                    enabled = !isLoading,
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
                        text = "Cancelar",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Green Bottom Background Graphic
            val secondaryColor = MaterialTheme.colorScheme.secondary

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clearAndSetSemantics { }
                ) {
                    val w = size.width
                    val h = size.height

                    val wavePath = Path().apply {
                        moveTo(0f, h * 0.4f)
                        cubicTo(
                            w * 0.35f, 0f,
                            w * 0.65f, h * 0.8f,
                            w, h * 0.2f
                        )
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(
                        path = wavePath,
                        color = secondaryColor
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    semanticLabel: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClear: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        },
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        isError = isError,
        supportingText = if (supportingText != null) {
            { Text(text = supportingText, color = MaterialTheme.colorScheme.error) }
        } else null,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .semantics {
                stateDescription = semanticLabel
            },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
            unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = onClear,
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
                            contentDescription = "Limpar $label",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    )
}

/**
 * VisualTransformation for formatting digits DDMMYYYY into DD/MM/AAAA.
 */
private class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0, 8) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 || i == 3) out += "/"
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenLightPreview() {
    FisioSolutionsTheme(darkTheme = false) {
        RegisterScreen(
            onNavigateToLogin = {},
            onNavigateToUserArea = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenDarkPreview() {
    FisioSolutionsTheme(darkTheme = true) {
        RegisterScreen(
            onNavigateToLogin = {},
            onNavigateToUserArea = {}
        )
    }
}

