package com.example.appescuelampaz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BotonAnimado(
    etiqueta: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    //Animación de color
    val color by animateColorAsState(
        targetValue = if (cargando) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500),
        label = "colorBoton"
    )

    Button(
        onClick = {
            cargando = true
            scope.launch {
                delay(800)      //para simular el procesamiento
                onClick()
                cargando = false
            }
        },
        enabled = !cargando,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Text(etiqueta)
        }
    }
}