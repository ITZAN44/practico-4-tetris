package com.example.practico_4.presentacion.juego

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay

@Composable
fun PantallaJuego(
    roomId: String,
    onTerminarPartida: (String, Int, Int, Long) -> Unit,
    viewModel: JuegoViewModel = hiltViewModel()
) {
    val estado by viewModel.estadoTablero.collectAsState()
    val estadoOponente by viewModel.estadoOponente.collectAsState()

    LaunchedEffect(roomId) {
        viewModel.configurarPartida(roomId)
    }

    LaunchedEffect(Unit) {
        viewModel.eventoFinPartida.collect { resultado ->
            onTerminarPartida(
                resultado.ganador,
                resultado.puntaje,
                resultado.lineas,
                resultado.duracionSegundos
            )
        }
    }

    // Requerimiento 14: muestra "37" dorado al eliminar cualquier línea
    var mostrar37 by remember { mutableStateOf(false) }
    LaunchedEffect(estado.lineasEliminadas) {
        if (estado.lineasEliminadas > 0) {
            mostrar37 = true
            delay(1500)
            mostrar37 = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F23))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                InfoJuego(etiqueta = "Puntaje", valor = "${estado.puntaje}")
                Spacer(modifier = Modifier.height(4.dp))
                InfoJuego(etiqueta = "Líneas", valor = "${estado.lineasEliminadas}")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Siguiente", color = Color.Gray, fontSize = 11.sp)
                VistaPreviaCanvas(tipo = estado.piezaSiguiente)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Oponente", color = Color.Gray, fontSize = 11.sp)
                Text(
                    text = estadoOponente,
                    color = if (estadoOponente == "Desconectado") Color.Red else Color.Green,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TableroCanvas(
                estadoTablero = estado,
                modifier = Modifier.fillMaxSize()
            )
            if (mostrar37) {
                Text(
                    text = "37",
                    color = Color(0xFFFFD700),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

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
