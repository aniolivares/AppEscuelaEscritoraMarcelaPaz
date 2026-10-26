package com.example.appescuelampaz.model

data class Desafio(
    val nombreEstudiante: String = "",
    val asignatura: String = "Lenguaje y Comunicación",
    val respuestasCorrectas: Int = 0,
    val totalPreguntas: Int = 15,
    val intentosRealizados: Int = 0,
    val maxIntentosPermitidos: Int = 3,
    val autorizacionUtp: Boolean = false
)

data class DesafioErrores(
    val nombreEstudiante: String? = null,
    val respuestasCorrectas: String? = null,
    val intentosExcedidos: String? = null,
    val autorizacionUtp: String? = null
)

data class DesafioUiState(
    val desafio: Desafio = Desafio(),
    val respuestasTexto: String = "",
    val errores: DesafioErrores = DesafioErrores(),
    val mensajeResultado: String = "",
    val esAprobado: Boolean = false
)