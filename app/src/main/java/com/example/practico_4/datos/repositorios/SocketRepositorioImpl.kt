package com.example.practico_4.datos.repositorios

import com.example.practico_4.datos.red.EventoRed
import com.example.practico_4.datos.red.SocketServicio
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketRepositorioImpl @Inject constructor(
    private val socketServicio: SocketServicio
) : SocketRepositorio {
    
    override val eventos: SharedFlow<EventoRed> = socketServicio.eventos
    override val estaConectado: StateFlow<Boolean> = socketServicio.estaConectado

    override fun conectar() {
        socketServicio.conectar(SocketRepositorio.URL_SERVIDOR)
    }

    override fun desconectar() {
        socketServicio.desconectar()
    }

    override fun crearSala() {
        socketServicio.emitirCrearSala()
    }

    override fun unirseASala(codigo: String) {
        socketServicio.emitirUnirseASala(codigo)
    }

    override fun enviarAtaque(roomId: String, lineas: Int) {
        socketServicio.emitirAtaque(roomId, lineas)
    }

    override fun notificarDerrota(roomId: String) {
        socketServicio.emitirDerrota(roomId)
    }
}
