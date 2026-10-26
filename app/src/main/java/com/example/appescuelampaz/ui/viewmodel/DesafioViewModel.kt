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
import com.example.appescuelampaz.utils.Validaciones

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
                respuestasTexto = "",
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

        val errores = DesafioErrores(
            nombreEstudiante = Validaciones.validarNombre(desafioActual.nombreEstudiante),
            respuestasCorrectas = Validaciones.validarRespuestas(
                estadoActual.respuestasTexto, desafioActual.totalPreguntas
            ),
            intentosExcedidos = Validaciones.validarIntentos(
                desafioActual.intentosRealizados, desafioActual.maxIntentosPermitidos
            ),
            autorizacionUtp = Validaciones.validarAutorizacionUtp(desafioActual.autorizacionUtp)
        )

        val hayErrores = errores.nombreEstudiante != null ||
                errores.respuestasCorrectas != null ||
                errores.intentosExcedidos != null ||
                errores.autorizacionUtp != null

        if (hayErrores) {
            _uiState.update { it.copy(errores = errores) }
            return false
        }

        //Sitodo es válido se calcula el resultado
        val aciertos = estadoActual.respuestasTexto.toInt()
        val nuevosIntentos = desafioActual.intentosRealizados + 1
        val aprobado = Validaciones.estaAprobado(aciertos, desafioActual.totalPreguntas)
        val resultadoTexto = if (aprobado) "¡Desafío Aprobado!" else "Revisa la retroalimentación e intenta de nuevo"

        _uiState.update { estado ->
            estado.copy(
                desafio = desafioActual.copy(
                    respuestasCorrectas = aciertos,
                    intentosRealizados = nuevosIntentos
                ),
                mensajeResultado = resultadoTexto,
                esAprobado = aprobado,
                errores = DesafioErrores()
            )
        }

        //Se guarda en persistencia local (DataStore)
        viewModelScope.launch {
            repository.guardarEstadoCompleto(
                nombre = desafioActual.nombreEstudiante,
                asignatura = desafioActual.asignatura,
                respuestas = aciertos,
                intentos = nuevosIntentos,
                mensaje = resultadoTexto,
                aprobado = aprobado
            )
        }

        return true
    }
}