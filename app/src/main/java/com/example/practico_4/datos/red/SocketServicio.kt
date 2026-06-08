package com.example.practico_4.datos.red

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketServicio @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var socket: Socket? = null

    private val _eventos = MutableSharedFlow<EventoRed>(extraBufferCapacity = 10)
    val eventos = _eventos.asSharedFlow()

    private val _estaConectado = MutableStateFlow(false)
    val estaConectado = _estaConectado.asStateFlow()

    fun conectar(url: String) {
        if (!estaConectadoAInternet()) {
            _eventos.tryEmit(EventoRed.ErrorConexion("Sin acceso a la red (Verifica Wi-Fi)"))
            return
        }

        if (socket?.connected() == true) {
            _estaConectado.value = true
            _eventos.tryEmit(EventoRed.Conectado)
            return
        }
        
        try {
            socket = IO.socket(url)
            configurarEventos()
            socket?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
            _eventos.tryEmit(EventoRed.ErrorConexion("Error al inicializar socket"))
        }
    }

    private fun estaConectadoAInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun desconectar() {
        socket?.disconnect()
        socket?.off()
        _estaConectado.value = false
    }

    private fun configurarEventos() {
        socket?.on(Socket.EVENT_CONNECT) {
            _estaConectado.value = true
            _eventos.tryEmit(EventoRed.Conectado)
        }

        socket?.on(Socket.EVENT_DISCONNECT) {
            _estaConectado.value = false
            _eventos.tryEmit(EventoRed.Desconectado)
        }

        socket?.on(Socket.EVENT_CONNECT_ERROR) {
            _estaConectado.value = false
            _eventos.tryEmit(EventoRed.ErrorConexion("Error de conexión al servidor"))
        }

        socket?.on("room_created") { args ->
            val data = args[0] as JSONObject
            _eventos.tryEmit(EventoRed.SalaCreada(data.getString("roomId")))
        }

        socket?.on("game_start") {
            _eventos.tryEmit(EventoRed.InicioJuego)
        }

        socket?.on("receive_attack") { args ->
            val data = args[0] as JSONObject
            _eventos.tryEmit(EventoRed.AtaqueRecibido(data.getInt("garbageLines")))
        }

        socket?.on("victory") {
            _eventos.tryEmit(EventoRed.Victoria)
        }

        socket?.on("opponent_disconnected") {
            _eventos.tryEmit(EventoRed.OponenteDesconectado)
        }

        socket?.on("error_message") { args ->
            val data = args[0] as JSONObject
            _eventos.tryEmit(EventoRed.ErrorServidor(data.getString("message")))
        }
    }

    fun emitirCrearSala() {
        socket?.emit("create_room")
    }

    fun emitirUnirseASala(roomId: String) {
        val data = JSONObject().put("roomId", roomId)
        socket?.emit("join_room", data)
    }

    fun emitirAtaque(roomId: String, lineasBasura: Int) {
        val data = JSONObject()
            .put("roomId", roomId)
            .put("garbageLines", lineasBasura)
        socket?.emit("send_attack", data)
    }

    fun emitirDerrota(roomId: String) {
        val data = JSONObject().put("roomId", roomId)
        socket?.emit("game_over", data)
    }
}

sealed class EventoRed {
    object Conectado : EventoRed()
    object Desconectado : EventoRed()
    data class ErrorConexion(val mensaje: String) : EventoRed()
    data class SalaCreada(val roomId: String) : EventoRed()
    object InicioJuego : EventoRed()
    data class AtaqueRecibido(val lineasBasura: Int) : EventoRed()
    object Victoria : EventoRed()
    object OponenteDesconectado : EventoRed()
    data class ErrorServidor(val mensaje: String) : EventoRed()
}
