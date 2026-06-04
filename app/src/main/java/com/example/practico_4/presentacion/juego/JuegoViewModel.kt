package com.example.practico_4.presentacion.juego

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practico_4.datos.modelos.EstadoTablero
import com.example.practico_4.dominio.motor.GeneradorPiezas
import com.example.practico_4.dominio.motor.MotorJuego
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuegoViewModel @Inject constructor() : ViewModel() {

    private val _estadoTablero = MutableStateFlow(crearEstadoInicial())
    val estadoTablero: StateFlow<EstadoTablero> = _estadoTablero.asStateFlow()

    private var trabajoGravedad: Job? = null
    private val intervaloGravedadMs = 800L

    fun iniciarJuego() {
        _estadoTablero.value = crearEstadoInicial()
        iniciarBucleGravedad()
    }

    fun moverIzquierda() = actualizar { MotorJuego.moverIzquierda(it) }
    fun moverDerecha() = actualizar { MotorJuego.moverDerecha(it) }
    fun rotar() = actualizar { MotorJuego.rotar(it) }
    fun acelerar() = actualizar { MotorJuego.bajar(it) }

    fun caerInstantaneo() = actualizar { estado ->
        val nuevo = MotorJuego.caerInstantaneo(estado)
        procesarAtaquePendiente(nuevo)
        nuevo.copy(lineasAtaquePendientes = 0)
    }

    fun aplicarLineasBasura(cantidad: Int) = actualizar { MotorJuego.aplicarLineasBasura(it, cantidad) }

    private fun iniciarBucleGravedad() {
        trabajoGravedad?.cancel()
        trabajoGravedad = viewModelScope.launch {
            while (isActive) {
                delay(intervaloGravedadMs)
                val estadoActual = _estadoTablero.value
                if (!estadoActual.estaTerminado) {
                    val nuevo = MotorJuego.bajar(estadoActual)
                    procesarAtaquePendiente(nuevo)
                    _estadoTablero.value = nuevo.copy(lineasAtaquePendientes = 0)
                }
            }
        }
    }

    private fun actualizar(accion: (EstadoTablero) -> EstadoTablero) {
        val actual = _estadoTablero.value
        if (!actual.estaTerminado) {
            _estadoTablero.value = accion(actual)
        }
    }

    // Desarrollador B: reemplazar este stub para enviar el ataque via socket
    private fun procesarAtaquePendiente(estado: EstadoTablero) {
        if (estado.lineasAtaquePendientes > 0) {
            // socketRepositorio.enviarAtaque(roomId, estado.lineasAtaquePendientes)
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
