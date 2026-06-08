package com.example.practico_4.presentacion.juego

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practico_4.datos.modelos.EstadoTablero
import com.example.practico_4.datos.red.EventoRed
import com.example.practico_4.datos.repositorios.SocketRepositorio
import com.example.practico_4.dominio.motor.GeneradorPiezas
import com.example.practico_4.dominio.motor.MotorJuego
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuegoViewModel @Inject constructor(
    private val socketRepositorio: SocketRepositorio
) : ViewModel() {

    private val _estadoTablero = MutableStateFlow(crearEstadoInicial())
    val estadoTablero: StateFlow<EstadoTablero> = _estadoTablero.asStateFlow()

    private val _estadoOponente = MutableStateFlow("En juego")
    val estadoOponente: StateFlow<String> = _estadoOponente.asStateFlow()

    private val _eventoFinPartida = MutableSharedFlow<ResultadoPartida>()
    val eventoFinPartida: SharedFlow<ResultadoPartida> = _eventoFinPartida.asSharedFlow()

    private var trabajoGravedad: Job? = null
    private val intervaloGravedadMs = 800L
    private var roomId: String = ""
    private var tiempoInicio: Long = 0
    private var resultadoFinalizado = false

    fun configurarPartida(id: String) {
        this.roomId = id
        this.tiempoInicio = System.currentTimeMillis()
        this.resultadoFinalizado = false
        escucharEventosRed()
        iniciarJuego()
    }

    private fun escucharEventosRed() {
        viewModelScope.launch {
            socketRepositorio.eventos.collect { evento ->
                when (evento) {
                    is EventoRed.AtaqueRecibido -> {
                        actualizar { MotorJuego.aplicarLineasBasura(it, evento.lineasBasura) }
                    }
                    is EventoRed.Victoria -> {
                        if (!resultadoFinalizado) {
                            finalizarPartida("¡GANASTE!")
                        }
                    }
                    is EventoRed.OponenteDesconectado -> {
                        _estadoOponente.value = "Desconectado"
                        if (!resultadoFinalizado) {
                            finalizarPartida("¡GANASTE!\n(Oponente abandonó)")
                        }
                    }
                    is EventoRed.Desconectado -> {
                        if (!resultadoFinalizado) {
                            // Si se pierde conexión, no es necesariamente una derrota humillante
                            // Mostramos un mensaje neutral pero indicando que la partida terminó
                            finalizarPartida("CONEXIÓN PERDIDA")
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun iniciarJuego() {
        _estadoTablero.value = crearEstadoInicial()
        iniciarBucleGravedad()
    }

    fun moverIzquierda() = actualizar { MotorJuego.moverIzquierda(it) }
    fun moverDerecha() = actualizar { MotorJuego.moverDerecha(it) }
    fun rotar() = actualizar { MotorJuego.rotar(it) }
    
    fun acelerar() = actualizar { 
        val nuevo = MotorJuego.bajar(it)
        gestionarPostMovimiento(nuevo)
    }

    fun caerInstantaneo() = actualizar { 
        val nuevo = MotorJuego.caerInstantaneo(it)
        gestionarPostMovimiento(nuevo)
    }

    private fun gestionarPostMovimiento(nuevoEstado: EstadoTablero): EstadoTablero {
        if (nuevoEstado.lineasAtaquePendientes > 0) {
            socketRepositorio.enviarAtaque(roomId, nuevoEstado.lineasAtaquePendientes)
        }
        verificarDerrota(nuevoEstado)
        return nuevoEstado.copy(lineasAtaquePendientes = 0)
    }

    private fun iniciarBucleGravedad() {
        trabajoGravedad?.cancel()
        trabajoGravedad = viewModelScope.launch {
            while (isActive) {
                delay(intervaloGravedadMs)
                if (!resultadoFinalizado) {
                    _estadoTablero.update { estadoActual ->
                        if (!estadoActual.estaTerminado) {
                            val nuevo = MotorJuego.bajar(estadoActual)
                            gestionarPostMovimiento(nuevo)
                        } else {
                            estadoActual
                        }
                    }
                }
            }
        }
    }

    private fun actualizar(accion: (EstadoTablero) -> EstadoTablero) {
        if (!resultadoFinalizado) {
            _estadoTablero.update { actual ->
                if (!actual.estaTerminado) {
                    accion(actual)
                } else {
                    actual
                }
            }
        }
    }

    private fun verificarDerrota(estado: EstadoTablero) {
        if (estado.estaTerminado && !resultadoFinalizado) {
            resultadoFinalizado = true 
            socketRepositorio.notificarDerrota(roomId)
            finalizarPartida("PERDISTE")
        }
    }

    private fun finalizarPartida(resultado: String) {
        resultadoFinalizado = true
        trabajoGravedad?.cancel()
        val final = _estadoTablero.value
        viewModelScope.launch {
            _eventoFinPartida.emit(
                ResultadoPartida(
                    ganador = resultado,
                    puntaje = final.puntaje,
                    lineas = final.lineasEliminadas,
                    duracionSegundos = (System.currentTimeMillis() - tiempoInicio) / 1000
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        trabajoGravedad?.cancel()
    }

    private fun crearEstadoInicial(): EstadoTablero {
        val primerTipo = GeneradorPiezas.generarAleatoria()
        return EstadoTablero(
            piezaActiva = GeneradorPiezas.crearPiezaInicial(primerTipo),
            piezaSiguiente = GeneradorPiezas.generarAleatoria()
        )
    }
}

data class ResultadoPartida(
    val ganador: String,
    val puntaje: Int,
    val lineas: Int,
    val duracionSegundos: Long
)
