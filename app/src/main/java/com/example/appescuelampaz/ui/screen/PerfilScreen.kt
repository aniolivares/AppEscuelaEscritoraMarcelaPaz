package com.example.appescuelampaz.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appescuelampaz.ui.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    onNavegarADesafio: () -> Unit = {},
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val estado by perfilViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var opcionDocenteSeleccionada by remember { mutableStateOf(estado.esDocente) }

    LaunchedEffect(estado.esDocente) {
        if (estado.esDocente) {
            opcionDocenteSeleccionada = true
        }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        perfilViewModel.onFotoSelected(uri)
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val path = android.provider.MediaStore.Images.Media.insertImage(
                context.contentResolver, it, "FotoPerfil_${System.currentTimeMillis()}", null
            )
            path?.let { uriStr ->
                perfilViewModel.onFotoSelected(Uri.parse(uriStr))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Perfil de Usuario",
            style = MaterialTheme.typography.headlineSmall
        )

        // Foto de Perfil
        if (estado.fotoUri != null) {
            AsyncImage(
                model = estado.fotoUri,
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color(0xFF71BFB5), CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto por defecto",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape),
                tint = Color(0xFF71BFB5)
            )
        }

        // Botones Galería y Cámara
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { galeriaLauncher.launch("image/*") }) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                Text(" Galería", modifier = Modifier.padding(start = 4.dp))
            }
            OutlinedButton(onClick = { camaraLauncher.launch(null) }) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Text(" Cámara", modifier = Modifier.padding(start = 4.dp))
            }
        }

        // Campo Nombre Completo
        OutlinedTextField(
            value = estado.nombreEstudiante,
            onValueChange = { perfilViewModel.onNombreChange(it) },
            label = { Text("Nombre Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF71BFB5),
                unfocusedBorderColor = Color(0xFF8CCDC3),
                focusedLabelColor = Color(0xFF71BFB5),
                focusedLeadingIconColor = Color(0xFF71BFB5)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Selección de Rol
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F5F3))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Selecciona el Rol de Acceso:",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            opcionDocenteSeleccionada = false
                            perfilViewModel.cambiarAEstudiante()
                        }
                ) {
                    RadioButton(
                        selected = !opcionDocenteSeleccionada,
                        onClick = {
                            opcionDocenteSeleccionada = false
                            perfilViewModel.cambiarAEstudiante()
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF71BFB5))
                    )
                    Text(text = "Estudiante", modifier = Modifier.padding(start = 8.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            opcionDocenteSeleccionada = true
                        }
                ) {
                    RadioButton(
                        selected = opcionDocenteSeleccionada,
                        onClick = {
                            opcionDocenteSeleccionada = true
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF71BFB5))
                    )
                    Text(text = "Docente / Jefatura UTP", modifier = Modifier.padding(start = 8.dp))
                }

                if (opcionDocenteSeleccionada) {
                    if (!estado.esDocente) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = estado.pinInput,
                                onValueChange = { perfilViewModel.onPinInputChange(it) },
                                label = { Text("Clave de Autorización UTP") },
                                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                                isError = estado.errorPin != null,
                                supportingText = {
                                    estado.errorPin?.let {
                                        Text(text = it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF71BFB5),
                                    unfocusedBorderColor = Color(0xFF8CCDC3),
                                    focusedLabelColor = Color(0xFF71BFB5),
                                    focusedLeadingIconColor = Color(0xFF71BFB5)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = { perfilViewModel.validarPinUtp() },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null)
                                Text(" Validar Clave", modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text(
                                text = "Acceso Docente / UTP Habilitado",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onNavegarADesafio,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ir a Registro de Desafío")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}