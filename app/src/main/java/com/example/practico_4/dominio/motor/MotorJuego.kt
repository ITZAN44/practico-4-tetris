package com.example.practico_4.dominio.motor

import com.example.practico_4.datos.modelos.EstadoTablero
import com.example.practico_4.datos.modelos.EstadoTablero.Companion.COLUMNAS
import com.example.practico_4.datos.modelos.EstadoTablero.Companion.FILAS
import com.example.practico_4.datos.modelos.Pieza
import com.example.practico_4.datos.modelos.TipoPieza

object MotorJuego {

    fun moverIzquierda(estado: EstadoTablero): EstadoTablero {
        val nueva = estado.piezaActiva.copy(columna = estado.piezaActiva.columna - 1)
        return if (hayColision(estado.grilla, nueva)) estado else estado.copy(piezaActiva = nueva)
    }

    fun moverDerecha(estado: EstadoTablero): EstadoTablero {
        val nueva = estado.piezaActiva.copy(columna = estado.piezaActiva.columna + 1)
        return if (hayColision(estado.grilla, nueva)) estado else estado.copy(piezaActiva = nueva)
    }

    fun rotar(estado: EstadoTablero): EstadoTablero {
        val nuevaRotacion = (estado.piezaActiva.rotacion + 1) % 4
        val nueva = estado.piezaActiva.copy(rotacion = nuevaRotacion)
        return if (hayColision(estado.grilla, nueva)) estado else estado.copy(piezaActiva = nueva)
    }

    fun bajar(estado: EstadoTablero): EstadoTablero {
        val nueva = estado.piezaActiva.copy(fila = estado.piezaActiva.fila + 1)
        return if (hayColision(estado.grilla, nueva)) colocarPieza(estado) else estado.copy(piezaActiva = nueva)
    }

    fun caerInstantaneo(estado: EstadoTablero): EstadoTablero {
        var pieza = estado.piezaActiva
        while (!hayColision(estado.grilla, pieza.copy(fila = pieza.fila + 1))) {
            pieza = pieza.copy(fila = pieza.fila + 1)
        }
        return colocarPieza(estado.copy(piezaActiva = pieza))
    }

    fun calcularFilaSombra(estado: EstadoTablero): Int {
        var pieza = estado.piezaActiva
        while (!hayColision(estado.grilla, pieza.copy(fila = pieza.fila + 1))) {
            pieza = pieza.copy(fila = pieza.fila + 1)
        }
        return pieza.fila
    }

    fun aplicarLineasBasura(estado: EstadoTablero, cantidad: Int): EstadoTablero {
        if (cantidad <= 0) return estado
        val nuevaGrilla = estado.grilla.drop(cantidad).toMutableList()
        repeat(cantidad) {
            val columnaVacia = (0 until COLUMNAS).random()
            nuevaGrilla.add(List(COLUMNAS) { col -> if (col == columnaVacia) null else TipoPieza.BASURA })
        }
        return estado.copy(grilla = nuevaGrilla)
    }

    fun calcularLineasAtaque(lineasEliminadas: Int): Int = when (lineasEliminadas) {
        1    -> 0
        2    -> 1
        3    -> 2
        4    -> 4
        else -> 0
    }

    private fun colocarPieza(estado: EstadoTablero): EstadoTablero {
        val nuevaGrilla = estado.grilla.map { it.toMutableList() }.toMutableList()
        estado.piezaActiva.celdas().forEach { (fila, col) ->
            if (fila in 0 until FILAS && col in 0 until COLUMNAS) {
                nuevaGrilla[fila][col] = estado.piezaActiva.tipo
            }
        }

        val (grillaLimpia, lineasEliminadas) = eliminarLineasCompletas(nuevaGrilla)
        val puntajeGanado = calcularPuntaje(lineasEliminadas)
        val lineasAtaque = calcularLineasAtaque(lineasEliminadas)

        val siguientePieza = estado.piezaSiguiente
        val nuevaPiezaActiva = GeneradorPiezas.crearPiezaInicial(siguientePieza)
        val nuevaPiezaSiguiente = GeneradorPiezas.generarAleatoria()

        val terminado = hayColision(grillaLimpia, nuevaPiezaActiva)

        return estado.copy(
            grilla = grillaLimpia,
            piezaActiva = nuevaPiezaActiva,
            piezaSiguiente = nuevaPiezaSiguiente,
            puntaje = estado.puntaje + puntajeGanado,
            lineasEliminadas = estado.lineasEliminadas + lineasEliminadas,
            estaTerminado = terminado,
            lineasAtaquePendientes = lineasAtaque
        )
    }

    private fun eliminarLineasCompletas(
        grilla: MutableList<MutableList<TipoPieza?>>
    ): Pair<List<List<TipoPieza?>>, Int> {
        val filasIncompletas = grilla.filter { fila -> fila.any { it == null } }
        val eliminadas = FILAS - filasIncompletas.size
        val filasVacias = List(eliminadas) { MutableList<TipoPieza?>(COLUMNAS) { null } }
        return Pair(filasVacias + filasIncompletas, eliminadas)
    }

    private fun hayColision(grilla: List<List<TipoPieza?>>, pieza: Pieza): Boolean =
        pieza.celdas().any { (fila, col) ->
            col < 0 || col >= COLUMNAS || fila >= FILAS || (fila >= 0 && grilla[fila][col] != null)
        }

    private fun calcularPuntaje(lineasEliminadas: Int): Int = when (lineasEliminadas) {
        1    -> 100
        2    -> 300
        3    -> 500
        4    -> 800
        else -> 0
    }
}
