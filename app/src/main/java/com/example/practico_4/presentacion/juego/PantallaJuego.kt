package com.example.practico_4.presentacion.juego

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practico_4.presentacion.juego.componentes.Controles
import com.example.practico_4.presentacion.juego.componentes.TableroCanvas
import com.example.practico_4.presentacion.juego.componentes.VistaPreviaCanvas

@Composable
fun PantallaJuego(
    viewModel: JuegoViewModel = hiltViewModel()
) {
    val estado by viewModel.estadoTablero.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.iniciarJuego()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F23))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado: puntaje, líneas, siguiente pieza y oponente en una sola fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna izquierda: puntaje y líneas
            Column {
                InfoJuego(etiqueta = "Puntaje", valor = "${estado.puntaje}")
                Spacer(modifier = Modifier.height(4.dp))
                InfoJuego(etiqueta = "Líneas", valor = "${estado.lineasEliminadas}")
            }

            // Centro: siguiente pieza
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Siguiente", color = Color.Gray, fontSize = 11.sp)
                VistaPreviaCanvas(tipo = estado.piezaSiguiente)
            }

            // Columna derecha: estado del oponente (Desarrollador B)
            Column(horizontalAlignment = Alignment.End) {
                Text("Oponente", color = Color.Gray, fontSize = 11.sp)
                Text("—", color = Color.White, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Mensaje de Game Over (solo visible cuando termina)
        if (estado.estaTerminado) {
            Text(
                text = "GAME OVER",
                color = Color.Red,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Tablero principal: ocupa todo el espacio restante
        TableroCanvas(
            estadoTablero = estado,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Controles
        Controles(
            onMoverIzquierda = viewModel::moverIzquierda,
            onMoverDerecha = viewModel::moverDerecha,
            onRotar = viewModel::rotar,
            onAcelerar = viewModel::acelerar,
            onCaerInstantaneo = viewModel::caerInstantaneo
        )
    }
}

@Composable
private fun InfoJuego(etiqueta: String, valor: String) {
    Column {
        Text(etiqueta, color = Color.Gray, fontSize = 11.sp)
        Text(valor, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
