package com.example.practico_4.presentacion.juego.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Controles(
    onMoverIzquierda: () -> Unit,
    onMoverDerecha: () -> Unit,
    onRotar: () -> Unit,
    onAcelerar: () -> Unit,
    onCaerInstantaneo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Fila superior: rotar y caída instantánea
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BotonControl(etiqueta = "↻", onClick = onRotar)
            BotonControl(etiqueta = "⬇", onClick = onCaerInstantaneo)
        }

        // Fila inferior: izquierda, bajar, derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BotonControl(etiqueta = "←", onClick = onMoverIzquierda)
            BotonControl(etiqueta = "↓", onClick = onAcelerar)
            BotonControl(etiqueta = "→", onClick = onMoverDerecha)
        }
    }
}

@Composable
private fun BotonControl(etiqueta: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .padding(4.dp)
    ) {
        Text(text = etiqueta, fontSize = 20.sp)
    }
}
