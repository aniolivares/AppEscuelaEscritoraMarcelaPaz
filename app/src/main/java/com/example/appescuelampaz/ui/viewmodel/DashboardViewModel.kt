package com.example.appescuelampaz.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appescuelampaz.data.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val nombreEstudiante: String = "",
    val asignatura: String = "Lenguaje y Comunicación",
    val respuestasCorrectas: Int = 0,
    val esAprobado: Boolean = false
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            repository.nombreFlow.collect { nombre ->
                _uiState.update { it.copy(nombreEstudiante = nombre) }
            }
        }
        viewModelScope.launch {
            repository.asignaturaFlow.collect { asig ->
                _uiState.update { it.copy(asignatura = asig) }
            }
        }
        viewModelScope.launch {
            repository.respuestasFlow.collect { resp ->
                _uiState.update { it.copy(respuestasCorrectas = resp) }
            }
        }
        viewModelScope.launch {
            repository.aprobadoFlow.collect { apr ->
                _uiState.update { it.copy(esAprobado = apr) }
            }
        }
    }
}