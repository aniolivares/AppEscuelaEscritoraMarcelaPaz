package com.example.appescuelampaz.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appescuelampaz.ui.viewmodel.DesafioViewModel

@Composable
fun DesafioScreen() {
    val desafioViewModel: DesafioViewModel = viewModel()
    val estado by desafioViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val asignaturasDisponibles = listOf(
        "Lenguaje y Comunicación",
        "Matemática",
        "Ciencias Naturales",
        "Historia, Geografía y Ciencias Sociales"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Evaluación y Desafío",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF143B35)
        )

        // 1. Campo Nombre del Estudiante en Menta
        OutlinedTextField(
            value = estado.desafio.nombreEstudiante,
            onValueChange = { desafioViewModel.onNombreEstudianteChange(it) },
            label = { Text("Nombre del Estudiante") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF71BFB5),
                unfocusedBorderColor = Color(0xFF8CCDC3),
                focusedLabelColor = Color(0xFF71BFB5),
                focusedLeadingIconColor = Color(0xFF71BFB5)
            ),
            isError = estado.errores.nombreEstudiante != null,
            supportingText = {
                estado.errores.nombreEstudiante?.let { error ->
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 2. Selección de Asignaturas en Cajitas Menta
        Text(
            text = "Selecciona Asignatura:",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF143B35)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            asignaturasDisponibles.forEach { asignatura ->
                val estaSeleccionada = estado.desafio.asignatura == asignatura

                Surface(
                    onClick = { desafioViewModel.onAsignaturaChange(asignatura) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (estaSeleccionada) Color(0xFF71BFB5) else Color.White,
                    border = BorderStroke(
                        width = if (estaSeleccionada) 2.dp else 1.dp,
                        color = if (estaSeleccionada) Color(0xFF71BFB5) else Color(0xFF8CCDC3)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estaSeleccionada,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color(0xFF71BFB5)
                            )
                        )
                        Text(
                            text = asignatura,
                            color = if (estaSeleccionada) Color.White else Color(0xFF1C423C),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // 3. Cantidad de Aciertos / Respuestas Correctas
        OutlinedTextField(
            value = if (estado.desafio.respuestasCorrectas == 0) "" else estado.desafio.respuestasCorrectas.toString(),
            onValueChange = { valor ->
                if (valor.isEmpty()) {
                    desafioViewModel.onRespuestasCorrectasChange(0)
                } else {
                    val aciertos = valor.toIntOrNull()
                    if (aciertos != null) {
                        // Cambia el 5 por el número máximo de preguntas si no son 5
                        desafioViewModel.onRespuestasCorrectasChange(aciertos.coerceIn(0, 15))
                    }
                }
            },
            label = { Text("Respuestas Correctas (0 a 15)") },
            leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF71BFB5),
                unfocusedBorderColor = Color(0xFF8CCDC3),
                focusedLabelColor = Color(0xFF71BFB5),
                focusedLeadingIconColor = Color(0xFF71BFB5)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // 4. Casilla de Autorización UTP Encerrada en Cajita Menta
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (estado.desafio.autorizacionUtp) Color(0xFFE9F5F3) else Color.White,
            border = BorderStroke(
                width = 1.dp,
                color = if (estado.desafio.autorizacionUtp) Color(0xFF71BFB5) else Color(0xFF8CCDC3)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        desafioViewModel.onAutorizacionUtpChange(!estado.desafio.autorizacionUtp)
                    }
                    .padding(12.dp)
            ) {
                Checkbox(
                    checked = estado.desafio.autorizacionUtp,
                    onCheckedChange = { desafioViewModel.onAutorizacionUtpChange(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF71BFB5)
                    )
                )
                Text(
                    text = "Confirmo la revisión de pauta pedagógica y autorización UTP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1C423C),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        estado.errores.autorizacionUtp?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // 5. Botones de Acción: Evaluar y Reiniciar
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { desafioViewModel.procesarIntento() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF71BFB5)),
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Evaluar")
                }
            }

            OutlinedButton(
                onClick = { desafioViewModel.resetearDesafio() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reiniciar",
                        tint = Color(0xFF71BFB5)
                    )
                    Text(" Reiniciar", color = Color(0xFF71BFB5), modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        // Alerta de Intentos Excedidos
        estado.errores.intentosExcedidos?.let { errorIntentos ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = errorIntentos,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // 6. Tarjeta con Animación para el Resultado
        AnimatedVisibility(
            visible = estado.mensajeResultado.isNotEmpty(),
            enter = fadeIn() + expandVertically()
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (estado.esAprobado) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = estado.mensajeResultado,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (estado.esAprobado) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                    Text(
                        text = "Intentos realizados: ${estado.desafio.intentosRealizados} / ${estado.desafio.maxIntentosPermitidos}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}