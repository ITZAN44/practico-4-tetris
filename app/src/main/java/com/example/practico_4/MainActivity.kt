package com.example.practico_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practico_4.presentacion.juego.PantallaJuego
import com.example.practico_4.presentacion.lobby.PantallaLobby
import com.example.practico_4.presentacion.resultados.PantallaResultados
import com.example.practico_4.ui.theme.Practico4Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ocultarBarrasSistema()
        setContent {
            Practico4Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "lobby") {
                    composable("lobby") {
                        PantallaLobby(
                            onNavegarAJuego = { roomId ->
                                navController.navigate("juego/$roomId")
                            }
                        )
                    }
                    composable(
                        route = "juego/{roomId}",
                        arguments = listOf(navArgument("roomId") { defaultValue = "" })
                    ) { backStackEntry ->
                        val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                        PantallaJuego(
                            roomId = roomId,
                            onTerminarPartida = { ganador, puntaje, lineas, duracion ->
                                navController.navigate("resultados/$ganador/$puntaje/$lineas/$duracion") {
                                    popUpTo("juego/{roomId}") { inclusive = true }
                                }
                            },
                        )
                    }
                    composable(
                        route = "resultados/{ganador}/{puntaje}/{lineas}/{duracion}",
                        arguments = listOf(
                            navArgument("ganador") { defaultValue = "" },
                            navArgument("puntaje") { defaultValue = 0 },
                            navArgument("lineas") { defaultValue = 0 },
                            navArgument("duracion") { defaultValue = 0L }
                        )
                    ) { backStackEntry ->
                        val ganador = backStackEntry.arguments?.getString("ganador") ?: ""
                        val puntaje = backStackEntry.arguments?.getInt("puntaje") ?: 0
                        val lineas = backStackEntry.arguments?.getInt("lineas") ?: 0
                        val duracion = backStackEntry.arguments?.getLong("duracion") ?: 0L
                        
                        PantallaResultados(
                            ganador = ganador,
                            puntaje = puntaje,
                            lineas = lineas,
                            duracion = duracion
                        ) {
                            navController.navigate("lobby") {
                                popUpTo("lobby") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) ocultarBarrasSistema()
    }

    private fun ocultarBarrasSistema() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
