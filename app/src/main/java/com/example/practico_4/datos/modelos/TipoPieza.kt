package com.example.practico_4.datos.modelos

import androidx.compose.ui.graphics.Color

enum class TipoPieza {
    I, O, T, S, Z, J, L, BASURA;

    fun formas(): List<List<Pair<Int, Int>>> = when (this) {
        I -> listOf(
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)),
            listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)),
            listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0))
        )
        O -> listOf(
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1))
        )
        T -> listOf(
            listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, 2)),
            listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 0)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 1)),
            listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(2, 1))
        )
        S -> listOf(
            listOf(Pair(0, 1), Pair(0, 2), Pair(1, 0), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 1)),
            listOf(Pair(0, 1), Pair(0, 2), Pair(1, 0), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 1))
        )
        Z -> listOf(
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2)),
            listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(2, 0)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2)),
            listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(2, 0))
        )
        J -> listOf(
            listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(1, 2)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(2, 0)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2)),
            listOf(Pair(0, 1), Pair(1, 1), Pair(2, 0), Pair(2, 1))
        )
        L -> listOf(
            listOf(Pair(0, 2), Pair(1, 0), Pair(1, 1), Pair(1, 2)),
            listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 0)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1))
        )
        BASURA -> listOf(
            listOf(Pair(0, 0)),
            listOf(Pair(0, 0)),
            listOf(Pair(0, 0)),
            listOf(Pair(0, 0))
        )
    }

    fun color(): Color = when (this) {
        I      -> Color.Cyan
        O      -> Color.Yellow
        T      -> Color(0xFF800080)
        S      -> Color.Green
        Z      -> Color.Red
        J      -> Color.Blue
        L      -> Color(0xFFFF7F00)
        BASURA -> Color.Gray
    }
}
