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

    // Requerimiento 14: Referencia al número 37
    // El tablero brilla o cambia el color del texto cuando hay una relación con el 37
    val esEspecial37 = (estado.puntaje > 0 && estado.puntaje % 37 == 0) || 
                       (estado.lineasEliminadas > 0 && estado.lineasEliminadas % 37 == 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (esEspecial37) Color(0xFF1A1A3D) else Color(0xFF0F0F23))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                InfoJuego(
                    etiqueta = "Puntaje", 
                    valor = "${estado.puntaje}",
                    colorValor = if (esEspecial37) Color(0xFFFFD700) else Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                InfoJuego(etiqueta = "Líneas", valor = "${estado.lineasEliminadas}")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (esEspecial37) {
                    Text("¡MODO 37!", color = Color(0xFFFFD700), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("Siguiente", color = Color.Gray, fontSize = 11.sp)
                }
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

        TableroCanvas(
            estadoTablero = estado,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

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
private fun InfoJuego(etiqueta: String, valor: String, colorValor: Color = Color.White) {
    Column {
        Text(etiqueta, color = Color.Gray, fontSize = 11.sp)
        Text(valor, color = colorValor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
