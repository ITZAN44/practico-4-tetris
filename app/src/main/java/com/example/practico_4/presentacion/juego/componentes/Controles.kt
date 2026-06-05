package com.example.practico_4.presentacion.juego.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

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
            BotonControl(icono = Icons.Filled.Refresh, descripcion = "Rotar", onClick = onRotar)
            BotonControl(icono = Icons.Filled.ArrowDownward, descripcion = "Caída instantánea", onClick = onCaerInstantaneo)
        }

        // Fila inferior: izquierda, bajar, derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BotonControl(icono = Icons.Filled.KeyboardArrowLeft, descripcion = "Izquierda", onClick = onMoverIzquierda)
            BotonControl(icono = Icons.Filled.KeyboardArrowDown, descripcion = "Bajar", onClick = onAcelerar)
            BotonControl(icono = Icons.Filled.KeyboardArrowRight, descripcion = "Derecha", onClick = onMoverDerecha)
        }
    }
}

@Composable
private fun BotonControl(icono: ImageVector, descripcion: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .padding(4.dp)
    ) {
        Icon(imageVector = icono, contentDescription = descripcion)
    }
}
