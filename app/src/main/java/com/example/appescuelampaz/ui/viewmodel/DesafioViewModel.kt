package com.example.appescuelampaz.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appescuelampaz.data.DataStoreRepository
import com.example.appescuelampaz.model.DesafioErrores
import com.example.appescuelampaz.model.DesafioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DesafioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)

    private val _uiState = MutableStateFlow(DesafioUiState())
    val uiState: StateFlow<DesafioUiState> = _uiState.asStateFlow()

    init {
        cargarEstadoGuardado()
    }

    private fun cargarEstadoGuardado() {
        viewModelScope.launch {
            repository.nombreFlow.collect { nombre ->
                _uiState.update { it.copy(desafio = it.desafio.copy(nombreEstudiante = nombre)) }
            }
        }
        viewModelScope.launch {
            repository.asignaturaFlow.collect { asignatura ->
                _uiState.update { it.copy(desafio = it.desafio.copy(asignatura = asignatura)) }
            }
        }
        viewModelScope.launch {
            repository.respuestasFlow.collect { aciertos ->
                _uiState.update { it.copy(desafio = it.desafio.copy(respuestasCorrectas = aciertos)) }
            }
        }
        viewModelScope.launch {
            repository.intentosFlow.collect { intentos ->
                _uiState.update { it.copy(desafio = it.desafio.copy(intentosRealizados = intentos)) }
            }
        }
        viewModelScope.launch {
            repository.mensajeFlow.collect { mensaje ->
                _uiState.update { it.copy(mensajeResultado = mensaje) }
            }
        }
        viewModelScope.launch {
            repository.aprobadoFlow.collect { esAprobado ->
                _uiState.update { it.copy(esAprobado = esAprobado) }
            }
        }
    }

    fun onNombreEstudianteChange(nuevoNombre: String) {
        _uiState.update { estado ->
            estado.copy(
                desafio = estado.desafio.copy(nombreEstudiante = nuevoNombre),
                errores = estado.errores.copy(nombreEstudiante = null)
            )
        }
    }

    fun onAsignaturaChange(nuevaAsignatura: String) {
        _uiState.update { estado ->
            estado.copy(desafio = estado.desafio.copy(asignatura = nuevaAsignatura))
        }
    }

    fun onRespuestasTextoChange(texto: String) {
        if (texto.all { it.isDigit() } && texto.length <= 2) {
            _uiState.update {
                it.copy(
                    respuestasTexto = texto,
                    errores = it.errores.copy(respuestasCorrectas = null)
                )
            }
        }
    }

    fun onAutorizacionUtpChange(autorizado: Boolean) {
        _uiState.update { estado ->
            estado.copy(
                desafio = estado.desafio.copy(autorizacionUtp = autorizado),
                errores = estado.errores.copy(autorizacionUtp = null)
            )
        }
    }

    fun resetearDesafio() {
        _uiState.update { estado ->
            estado.copy(
                desafio = estado.desafio.copy(
                    respuestasCorrectas = 0,
                    intentosRealizados = 0
                ),
                mensajeResultado = "",
                esAprobado = false,
                errores = DesafioErrores()
            )
        }

        viewModelScope.launch {
            repository.limpiarDesafio()
        }
    }

    fun procesarIntento(): Boolean {
        val estadoActual = _uiState.value
        val desafioActual = estadoActual.desafio

        val aciertos = estadoActual.respuestasTexto.toIntOrNull()
        val errorRespuestas = when {
            aciertos == null -> "Ingresa la cantidad de respuestas correctas"
            aciertos > desafioActual.totalPreguntas -> "Máximo ${desafioActual.totalPreguntas}"
            else -> null
        }

        val errorNombre = if (desafioActual.nombreEstudiante.isBlank()) {
            "El nombre del estudiante es obligatorio"
        } else null

        val errorIntentos = if (desafioActual.intentosRealizados >= desafioActual.maxIntentosPermitidos) {
            "Límite alcanzado: máximo ${desafioActual.maxIntentosPermitidos} intentos"
        } else null

        val errorUtp = if (!desafioActual.autorizacionUtp) {
            "Debe confirmar la revisión de pauta pedagógica"
        } else null

        val hayErrores = errorNombre != null || errorIntentos != null || errorUtp != null

        if (hayErrores) {
            _uiState.update {
                it.copy(
                    errores = DesafioErrores(
                        nombreEstudiante = errorNombre,
                        intentosExcedidos = errorIntentos,
                        autorizacionUtp = errorUtp
                    )
                )
            }
            return false
        }

        val nuevosIntentos = desafioActual.intentosRealizados + 1
        val aprobado = desafioActual.respuestasCorrectas >= 3
        val resultadoTexto = if (aprobado) "¡Desafío Aprobado!" else "Revisa la retroalimentación e intenta de nuevo"

        _uiState.update { estado ->
            estado.copy(
                desafio = desafioActual.copy(intentosRealizados = nuevosIntentos),
                mensajeResultado = resultadoTexto,
                esAprobado = aprobado,
                errores = DesafioErrores()
            )
        }

        viewModelScope.launch {
            repository.guardarEstadoCompleto(
                nombre = desafioActual.nombreEstudiante,
                asignatura = desafioActual.asignatura,
                respuestas = desafioActual.respuestasCorrectas,
                intentos = nuevosIntentos,
                mensaje = resultadoTexto,
                aprobado = aprobado
            )
        }

        return true
    }
}