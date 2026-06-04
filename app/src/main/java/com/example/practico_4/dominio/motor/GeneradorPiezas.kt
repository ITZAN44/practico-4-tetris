package com.example.practico_4.dominio.motor

import com.example.practico_4.datos.modelos.EstadoTablero
import com.example.practico_4.datos.modelos.Pieza
import com.example.practico_4.datos.modelos.TipoPieza

object GeneradorPiezas {

    private val tiposJugables = TipoPieza.entries.filter { it != TipoPieza.BASURA }

    fun generarAleatoria(): TipoPieza = tiposJugables.random()

    fun crearPiezaInicial(tipo: TipoPieza): Pieza = Pieza(
        tipo = tipo,
        fila = 0,
        columna = EstadoTablero.COLUMNAS / 2 - 2,
        rotacion = 0
    )
}
