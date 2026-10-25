package com.example.appescuelampaz.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferencias_escuela")

class DataStoreRepository(private val context: Context) {

    companion object {
        val KEY_NOMBRE_ESTUDIANTE = stringPreferencesKey("nombre_estudiante")
        val KEY_FOTO_URI = stringPreferencesKey("foto_uri_estudiante")
        val KEY_ASIGNATURA = stringPreferencesKey("asignatura_seleccionada")
        val KEY_RESPUESTAS = intPreferencesKey("respuestas_correctas")
        val KEY_INTENTOS = intPreferencesKey("intentos_realizados")
        val KEY_MENSAJE = stringPreferencesKey("mensaje_resultado")
        val KEY_APROBADO = booleanPreferencesKey("es_aprobado")
        val KEY_ES_DOCENTE = booleanPreferencesKey("es_docente") // Rol UTP/Docente
    }

    suspend fun guardarNombreEstudiante(nombre: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE_ESTUDIANTE] = nombre
        }
    }

    suspend fun guardarFotoUri(uriString: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_FOTO_URI] = uriString
        }
    }

    suspend fun guardarRolDocente(esDocente: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ES_DOCENTE] = esDocente
        }
    }

    suspend fun guardarEstadoCompleto(
        nombre: String,
        asignatura: String,
        respuestas: Int,
        intentos: Int,
        mensaje: String,
        aprobado: Boolean
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE_ESTUDIANTE] = nombre
            prefs[KEY_ASIGNATURA] = asignatura
            prefs[KEY_RESPUESTAS] = respuestas
            prefs[KEY_INTENTOS] = intentos
            prefs[KEY_MENSAJE] = mensaje
            prefs[KEY_APROBADO] = aprobado
        }
    }

    suspend fun limpiarDesafio() {
        context.dataStore.edit { prefs ->
            prefs[KEY_RESPUESTAS] = 0
            prefs[KEY_INTENTOS] = 0
            prefs[KEY_MENSAJE] = ""
            prefs[KEY_APROBADO] = false
        }
    }

    val nombreFlow: Flow<String> = context.dataStore.data.map { it[KEY_NOMBRE_ESTUDIANTE] ?: "" }
    val fotoUriFlow: Flow<String> = context.dataStore.data.map { it[KEY_FOTO_URI] ?: "" }
    val esDocenteFlow: Flow<Boolean> = context.dataStore.data.map { it[KEY_ES_DOCENTE] ?: false }
    val asignaturaFlow: Flow<String> = context.dataStore.data.map { it[KEY_ASIGNATURA] ?: "Lenguaje y Comunicación" }
    val respuestasFlow: Flow<Int> = context.dataStore.data.map { it[KEY_RESPUESTAS] ?: 0 }
    val intentosFlow: Flow<Int> = context.dataStore.data.map { it[KEY_INTENTOS] ?: 0 }
    val mensajeFlow: Flow<String> = context.dataStore.data.map { it[KEY_MENSAJE] ?: "" }
    val aprobadoFlow: Flow<Boolean> = context.dataStore.data.map { it[KEY_APROBADO] ?: false }
}