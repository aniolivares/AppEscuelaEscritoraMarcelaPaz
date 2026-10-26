package com.example.appescuelampaz.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appescuelampaz.data.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.appescuelampaz.utils.Validaciones

data class PerfilUiState(
    val nombreEstudiante: String = "",
    val errorNombre: String? = null,
    val fotoUri: Uri? = null,
    val esDocente: Boolean = false,
    val pinInput: String = "",
    val errorPin: String? = null
)

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    //Clave UTP
    private val CLAVE_UTP_CORRECTA = "vivaxile"

    init {
        cargarPerfil()
    }

    private fun cargarPerfil() {
        viewModelScope.launch {
            repository.nombreFlow.collect { nombre ->
                _uiState.update { it.copy(nombreEstudiante = nombre) }
            }
        }
        viewModelScope.launch {
            repository.fotoUriFlow.collect { uriStr ->
                if (uriStr.isNotEmpty()) {
                    _uiState.update { it.copy(fotoUri = Uri.parse(uriStr)) }
                }
            }
        }
        viewModelScope.launch {
            repository.esDocenteFlow.collect { esDocente ->
                _uiState.update { it.copy(esDocente = esDocente) }
            }
        }
    }

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombreEstudiante = nuevoNombre, errorNombre = null) }
        viewModelScope.launch {
            repository.guardarNombreEstudiante(nuevoNombre)
        }
    }

    fun onFotoSelected(uri: Uri?) {
        if (uri != null) {
            _uiState.update { it.copy(fotoUri = uri) }
            viewModelScope.launch {
                repository.guardarFotoUri(uri.toString())
            }
        }
    }

    fun validarParaContinuar(): Boolean {
        val error = Validaciones.validarNombre(_uiState.value.nombreEstudiante)
        _uiState.update { it.copy(errorNombre = error) }
        return error == null
    }

    fun onPinInputChange(nuevoPin: String) {
        _uiState.update { it.copy(pinInput = nuevoPin, errorPin = null) }
    }

    fun validarPinUtp() {
        val pinLimpio = _uiState.value.pinInput.trim()
        if (pinLimpio.equals(CLAVE_UTP_CORRECTA, ignoreCase = true)) {
            _uiState.update { it.copy(esDocente = true, pinInput = "", errorPin = null) }
            viewModelScope.launch {
                repository.guardarRolDocente(true)
            }
        } else {
            _uiState.update { it.copy(errorPin = "Clave UTP incorrecta") }
        }
    }

    fun cambiarAEstudiante() {
        _uiState.update { it.copy(esDocente = false, pinInput = "", errorPin = null) }
        viewModelScope.launch {
            repository.guardarRolDocente(false)
        }
    }
}