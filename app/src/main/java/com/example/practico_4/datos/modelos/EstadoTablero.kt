package com.example.practico_4.datos.modelos

data class EstadoTablero(
    val grilla: List<List<TipoPieza?>> = List(FILAS) { List(COLUMNAS) { null } },
    val piezaActiva: Pieza,
    val piezaSiguiente: TipoPieza,
    val puntaje: Int = 0,
    val lineasEliminadas: Int = 0,
    val estaTerminado: Boolean = false,
    // El ViewModel lee este campo luego de colocar una pieza y envía el ataque (Desarrollador B)
    val lineasAtaquePendientes: Int = 0
) {
    companion object {
        const val FILAS = 20
        const val COLUMNAS = 10
    }
}
