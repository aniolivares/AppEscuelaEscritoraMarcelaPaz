package com.example.appescuelampaz.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appescuelampaz.data.DataStoreRepository
import com.example.appescuelampaz.ui.screen.DashboardScreen
import com.example.appescuelampaz.ui.screen.DesafioScreen
import com.example.appescuelampaz.ui.screen.PerfilScreen

sealed class Pantalla(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Perfil : Pantalla("perfil", "Perfil", Icons.Default.Person)
    object Desafio : Pantalla("desafio", "Desafío", Icons.AutoMirrored.Filled.Assignment)
    object Dashboard : Pantalla("dashboard", "Informe UTP", Icons.Default.Analytics)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val repository = remember { DataStoreRepository(context) }
    val esDocente by repository.esDocenteFlow.collectAsState(initial = false)

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    // Se muestran 2 o 3 pestañas según la validación del rol
    val pantallas = if (esDocente) {
        listOf(Pantalla.Perfil, Pantalla.Desafio, Pantalla.Dashboard)
    } else {
        listOf(Pantalla.Perfil, Pantalla.Desafio)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escuela Marcela Paz - MPAZ PRO 3.º") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD1EFEA),
                    titleContentColor = Color(0xFF143B35)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFE9F5F3)
            ) {
                pantallas.forEach { pantalla ->
                    NavigationBarItem(
                        icon = { Icon(pantalla.icono, contentDescription = pantalla.titulo) },
                        label = { Text(pantalla.titulo) },
                        selected = rutaActual == pantalla.ruta,
                        onClick = {
                            if (rutaActual != pantalla.ruta) {
                                if (pantalla == Pantalla.Perfil) {
                                    navController.popBackStack(Pantalla.Perfil.ruta, inclusive = false)
                                } else {
                                    navController.navigate(pantalla.ruta) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Pantalla.Perfil.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Pantalla.Perfil.ruta) {
                PerfilScreen(
                    onNavegarADesafio = {
                        navController.navigate(Pantalla.Desafio.ruta) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Pantalla.Desafio.ruta) {
                DesafioScreen()
            }
            composable(Pantalla.Dashboard.ruta) {
                DashboardScreen()
            }
        }
    }
}