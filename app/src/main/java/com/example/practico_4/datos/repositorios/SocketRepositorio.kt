package com.example.practico_4.datos.repositorios

import com.example.practico_4.datos.red.EventoRed
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SocketRepositorio {
    val eventos: SharedFlow<EventoRed>
    val estaConectado: StateFlow<Boolean>
    fun conectar()
    fun desconectar()
    fun crearSala()
    fun unirseASala(codigo: String)
    fun enviarAtaque(roomId: String, lineas: Int)
    fun notificarDerrota(roomId: String)

    companion object {
        // CAMBIA AQUÍ LA IP PARA TODO EL PROYECTO
        const val URL_SERVIDOR = "http://IP_SERVIDOR:3000"
    }
}
