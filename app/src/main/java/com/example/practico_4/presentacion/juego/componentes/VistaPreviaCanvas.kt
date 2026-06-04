package com.example.practico_4.presentacion.juego.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practico_4.datos.modelos.TipoPieza

@Composable
fun VistaPreviaCanvas(tipo: TipoPieza, modifier: Modifier = Modifier) {
    val celdas = tipo.formas()[0]
    val maxFila = (celdas.maxOfOrNull { it.first } ?: 0) + 1
    val maxCol = (celdas.maxOfOrNull { it.second } ?: 0) + 1
    val tamanioCelda = 18.dp

    Canvas(
        modifier = modifier
            .size(tamanioCelda * 4, tamanioCelda * 4)
            .background(Color(0xFF1A1A2E))
    ) {
        val bloque = size.width / 4f
        val offsetFila = (4 - maxFila) / 2f
        val offsetCol = (4 - maxCol) / 2f

        celdas.forEach { (fila, col) ->
            drawRect(
                color = tipo.color(),
                topLeft = Offset(
                    (col + offsetCol) * bloque + 1f,
                    (fila + offsetFila) * bloque + 1f
                ),
                size = Size(bloque - 2f, bloque - 2f)
            )
        }
    }
}
