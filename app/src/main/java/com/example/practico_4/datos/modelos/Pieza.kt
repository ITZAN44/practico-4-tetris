package com.example.practico_4.datos.modelos

data class Pieza(
    val tipo: TipoPieza,
    val fila: Int,
    val columna: Int,
    val rotacion: Int = 0
) {
    fun celdas(): List<Pair<Int, Int>> =
        tipo.formas()[rotacion].map { (df, dc) -> Pair(fila + df, columna + dc) }
}
