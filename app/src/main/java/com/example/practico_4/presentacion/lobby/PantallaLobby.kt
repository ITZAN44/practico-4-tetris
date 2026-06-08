package com.example.practico_4.presentacion.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PantallaLobby(
    onNavegarAJuego: (String) -> Unit,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val estadoConexion by viewModel.estadoConexion.collectAsState()
    val codigoSala by viewModel.codigoSala.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()
    val navegarAJuego by viewModel.navegarAJuego.collectAsState()
    
    var textoCodigo by remember { mutableStateOf("") }

    LaunchedEffect(navegarAJuego) {
        if (navegarAJuego) {
            onNavegarAJuego(codigoSala.ifBlank { textoCodigo })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TETRIS DUEL",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        // Sección Crear Sala
        Button(
            onClick = { viewModel.crearSala() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CREAR SALA")
        }

        if (codigoSala.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Código: $codigoSala",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Esperando al oponente...",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(32.dp))

        // Sección Unirse a Sala
        OutlinedTextField(
            value = textoCodigo,
            onValueChange = { textoCodigo = it.uppercase() },
            label = { Text("Código de Sala") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.unirseASala(textoCodigo) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("UNIRSE A SALA")
        }

        if (mensajeError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensajeError!!,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
            if (estadoConexion == EstadoConexion.ERROR) {
                Button(
                    onClick = { viewModel.conectar() },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("REINTENTAR CONEXIÓN")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        // Estado de conexión
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Estado: ", fontSize = 12.sp)
            Text(
                text = estadoConexion.name,
                fontSize = 12.sp,
                color = when (estadoConexion) {
                    EstadoConexion.CONECTADO -> Color.Green
                    EstadoConexion.ERROR -> Color.Red
                    else -> Color.Gray
                }
            )
        }
    }
}
