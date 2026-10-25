package com.example.appescuelampaz.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appescuelampaz.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen() {
    val dashboardViewModel: DashboardViewModel = viewModel()
    val estado by dashboardViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Informe Pedagógico UTP",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF143B35)
        )

        // Tarjeta Encabezado Informativo
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE9F5F3),
            border = BorderStroke(1.dp, Color(0xFF8CCDC3)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = Color(0xFF71BFB5),
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "Métricas de Rendimiento General",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF143B35)
                )
            }
        }

        // Estadísticas Básicas
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD1EFEA)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF71BFB5)
                    )
                    Text(
                        text = "Estudiante",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF1C423C),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = if (estado.nombreEstudiante.isNotEmpty()) estado.nombreEstudiante else "Sin registro",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF143B35)
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD1EFEA)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Color(0xFF71BFB5)
                    )
                    Text(
                        text = "Asignatura",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF1C423C),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = estado.asignatura,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF143B35)
                    )
                }
            }
        }

        // Barra de Progreso
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFF8CCDC3)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Aciertos Obtenidos",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF143B35)
                    )
                    Text(
                        text = "${estado.respuestasCorrectas} / 5",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF71BFB5)
                    )
                }

                LinearProgressIndicator(
                    progress = { estado.respuestasCorrectas / 5f },
                    color = Color(0xFF71BFB5),
                    trackColor = Color(0xFFE9F5F3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = if (estado.esAprobado) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        tint = if (estado.esAprobado) Color(0xFF2E7D32) else Color(0xFFC62828),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = if (estado.esAprobado) "Estado: Aprobado (Logro alcanzado)" else "Estado: Pendiente / Requiere Refuerzo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (estado.esAprobado) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }
        }
    }
}