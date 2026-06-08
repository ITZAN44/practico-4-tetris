package com.example.practico_4.presentacion.resultados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaResultados(
    ganador: String,
    puntaje: Int,
    lineas: Int,
    duracion: Long,
    onVolverAlLobby: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F23))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = ganador,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Black,
            color = if (ganador.contains("GANASTE")) Color.Green else Color.Red,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        ResultadoItem(etiqueta = "PUNTAJE TOTAL", valor = "$puntaje")
        ResultadoItem(etiqueta = "LÍNEAS ELIMINADAS", valor = "$lineas")
        ResultadoItem(etiqueta = "DURACIÓN", valor = "${duracion}s")

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = onVolverAlLobby,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("VOLVER AL LOBBY", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ResultadoItem(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = Color.Gray, fontSize = 16.sp)
        Text(text = valor, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
