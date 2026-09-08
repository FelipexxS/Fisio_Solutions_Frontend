package br.com.fisiosolutions.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.fisiosolutions.R

@Composable
fun FisioLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.fisio_solutions_logo),
        contentDescription = "Logo Fisio Solutions",
        modifier = modifier.height(80.dp)
    )
}
