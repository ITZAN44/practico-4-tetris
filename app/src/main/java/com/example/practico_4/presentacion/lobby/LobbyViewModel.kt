package com.example.practico_4.presentacion.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practico_4.datos.red.EventoRed
import com.example.practico_4.datos.repositorios.SocketRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val repository: SocketRepositorio
) : ViewModel() {

    private val _estadoConexion = MutableStateFlow(EstadoConexion.ESPERANDO)
    val estadoConexion: StateFlow<EstadoConexion> = _estadoConexion.asStateFlow()

    private val _codigoSala = MutableStateFlow("")
    val codigoSala: StateFlow<String> = _codigoSala.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _navegarAJuego = MutableStateFlow(false)
    val navegarAJuego: StateFlow<Boolean> = _navegarAJuego.asStateFlow()

    init {
        // Inicializar estado según si ya estamos conectados
        if (repository.estaConectado.value) {
            _estadoConexion.value = EstadoConexion.CONECTADO
        }
        
        repository.conectar()
        
        viewModelScope.launch {
            repository.eventos.collect { evento ->
                when (evento) {
                    is EventoRed.Conectado -> {
                        _estadoConexion.value = EstadoConexion.CONECTADO
                        _mensajeError.value = null
                    }
                    is EventoRed.ErrorConexion -> {
                        _estadoConexion.value = EstadoConexion.ERROR
                        _mensajeError.value = evento.mensaje
                    }
                    is EventoRed.Desconectado -> {
                        _estadoConexion.value = EstadoConexion.ESPERANDO
                    }
                    is EventoRed.SalaCreada -> {
                        _codigoSala.value = evento.roomId
                    }
                    is EventoRed.InicioJuego -> {
                        _navegarAJuego.value = true
                    }
                    is EventoRed.ErrorServidor -> {
                        _mensajeError.value = evento.mensaje
                        _estadoConexion.value = EstadoConexion.ERROR
                    }
                    else -> {}
                }
            }
        }
    }

    fun conectar() {
        _estadoConexion.value = EstadoConexion.ESPERANDO
        _mensajeError.value = null
        _navegarAJuego.value = false // Resetear navegación
        _codigoSala.value = "" // Limpiar código anterior
        repository.conectar()
    }

    fun crearSala() {
        _mensajeError.value = null
        repository.crearSala()
    }

    fun unirseASala(codigo: String) {
        if (codigo.isBlank()) {
            _mensajeError.value = "Ingresa un código"
            return
        }
        _mensajeError.value = null
        repository.unirseASala(codigo)
    }

    override fun onCleared() {
        super.onCleared()
        // No desconectamos aquí porque el socket debe seguir activo en la pantalla de juego
    }
}

enum class EstadoConexion {
    ESPERANDO, CONECTADO, ERROR
}
