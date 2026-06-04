package com.example.practico_4.presentacion.juego.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.practico_4.datos.modelos.EstadoTablero
import com.example.practico_4.datos.modelos.EstadoTablero.Companion.COLUMNAS
import com.example.practico_4.datos.modelos.EstadoTablero.Companion.FILAS
import com.example.practico_4.dominio.motor.MotorJuego

@Composable
fun TableroCanvas(estadoTablero: EstadoTablero, modifier: Modifier = Modifier) {
    val filasSombra = MotorJuego.calcularFilaSombra(estadoTablero)
    val celdasSombra = estadoTablero.piezaActiva
        .tipo.formas()[estadoTablero.piezaActiva.rotacion]
        .map { (df, dc) -> Pair(filasSombra + df, estadoTablero.piezaActiva.columna + dc) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A2E))
    ) {
        val anchoBloque = size.width / COLUMNAS
        val altoBloque = size.height / FILAS
        val separacion = 1f

        // Bloques fijos de la grilla
        estadoTablero.grilla.forEachIndexed { fila, columnas ->
            columnas.forEachIndexed { col, tipo ->
                if (tipo != null) {
                    drawRect(
                        color = tipo.color(),
                        topLeft = Offset(col * anchoBloque + separacion, fila * altoBloque + separacion),
                        size = Size(anchoBloque - separacion * 2, altoBloque - separacion * 2)
                    )
                }
            }
        }

        // Sombra de caída instantánea
        celdasSombra.forEach { (fila, col) ->
            if (fila in 0 until FILAS && col in 0 until COLUMNAS) {
                drawRect(
                    color = estadoTablero.piezaActiva.tipo.color().copy(alpha = 0.3f),
                    topLeft = Offset(col * anchoBloque + separacion, fila * altoBloque + separacion),
                    size = Size(anchoBloque - separacion * 2, altoBloque - separacion * 2)
                )
            }
        }

        // Pieza activa
        estadoTablero.piezaActiva.celdas().forEach { (fila, col) ->
            if (fila in 0 until FILAS && col in 0 until COLUMNAS) {
                drawRect(
                    color = estadoTablero.piezaActiva.tipo.color(),
                    topLeft = Offset(col * anchoBloque + separacion, fila * altoBloque + separacion),
                    size = Size(anchoBloque - separacion * 2, altoBloque - separacion * 2)
                )
            }
        }

        // Líneas de la grilla
        for (col in 0..COLUMNAS) {
            drawLine(
                color = Color(0xFF2D2D44),
                start = Offset(col * anchoBloque, 0f),
                end = Offset(col * anchoBloque, size.height),
                strokeWidth = 0.5f
            )
        }
        for (fila in 0..FILAS) {
            drawLine(
                color = Color(0xFF2D2D44),
                start = Offset(0f, fila * altoBloque),
                end = Offset(size.width, fila * altoBloque),
                strokeWidth = 0.5f
            )
        }
    }
}
