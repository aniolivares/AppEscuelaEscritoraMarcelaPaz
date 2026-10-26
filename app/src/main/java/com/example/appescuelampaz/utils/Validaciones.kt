package com.example.appescuelampaz.utils

object Validaciones {

    fun validarNombre(nombre: String): String? = when {
        nombre.isBlank() -> "El nombre del estudiante es obligatorio"
        nombre.trim().length < 3 -> "El nombre debe tener al menos 3 letras"
        !nombre.all { it.isLetter() || it.isWhitespace() } -> "El nombre solo puede contener letras"
        else -> null
    }

    fun validarRespuestas(texto: String, totalPreguntas: Int): String? {
        val aciertos = texto.toIntOrNull()
        return when {
            aciertos == null -> "Ingresa la cantidad de respuestas correctas"
            aciertos > totalPreguntas -> "No puede superar $totalPreguntas preguntas"
            else -> null
        }
    }

    fun validarIntentos(realizados: Int, maximo: Int): String? =
        if (realizados >= maximo) "Límite alcanzado: máximo $maximo intentos" else null

    fun validarAutorizacionUtp(autorizado: Boolean): String? =
        if (!autorizado) "Debe confirmar la revisión de pauta pedagógica" else null

    fun estaAprobado(aciertos: Int, totalPreguntas: Int): Boolean =
        aciertos * 100 / totalPreguntas >= 60
}