package com.example.practico_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.practico_4.presentacion.juego.PantallaJuego
import com.example.practico_4.ui.theme.Practico4Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practico4Theme {
                // Desarrollador B: reemplazar PantallaJuego con NavHost (Lobby → Juego → Resultados)
                PantallaJuego()
            }
        }
    }
}
