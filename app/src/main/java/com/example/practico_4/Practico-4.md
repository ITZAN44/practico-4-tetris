# Practico 4: Tetris Duel Online

**Fecha de presentación:** 15/06/2026

## Descripción General
Desarrollar una aplicación Android utilizando Jetpack Compose donde dos jugadores compiten en partidas de Tetris en tiempo real mediante comunicación por sockets. Cada jugador posee su propio tablero. Cuando un jugador elimina líneas, genera ataques que afectan al tablero del oponente agregando líneas basura en la parte inferior. El ganador será el último jugador que permanezca activo sin perder la partida.

## Requerimientos

### 1. Creación y unión de partidas
La aplicación deberá permitir:
* Crear una sala de juego.
* Generar un código único para la sala.
* Unirse a una sala existente mediante código.
* Mostrar el estado de conexión de ambos jugadores.
* Iniciar la partida automáticamente cuando existan dos jugadores conectados.

### 2. Tablero de juego
La aplicación deberá mostrar:
* Tablero principal del jugador.
* Vista previa de la siguiente pieza.
* Puntaje actual.
* Cantidad de líneas eliminadas.
* Estado del oponente.
* Indicador de conexión.

El tablero tendrá dimensiones de:
* 10 columnas.
* 20 filas.

### 3. Control de piezas
El jugador deberá poder:
* Mover piezas hacia la izquierda.
* Mover piezas hacia la derecha.
* Rotar piezas.
* Acelerar la caída.
* Realizar caída instantánea.

### 4. Generación de piezas
El sistema deberá generar aleatoriamente las siguientes piezas:
* I
* O
* T
* S
* Z
* J
* L

### 5. Eliminación de líneas
Cuando una fila se encuentre completamente llena:
* La fila deberá eliminarse.
* Las filas superiores deberán descender.
* El puntaje deberá actualizarse.
* Se deberá calcular el ataque correspondiente.

### 6. Sistema de ataque
Las líneas eliminadas deberán afectar al oponente según la siguiente tabla:

| Líneas eliminadas | Líneas basura enviadas |
| :---: | :---: |
| 1 | 0 |
| 2 | 1 |
| 3 | 2 |
| 4 | 4 |

Las líneas basura:
* Se agregarán desde la parte inferior.
* Tendrán una celda vacía aleatoria.
* Empujarán el contenido actual hacia arriba.

### 7. Comunicación en tiempo real
La aplicación deberá utilizar Socket.IO para:
* Crear salas.
* Unirse a salas.
* Notificar inicio de partida.
* Enviar ataques.
* Informar derrota.
* Informar desconexiones.

La lógica local del tablero deberá continuar funcionando aunque exista latencia en la red.

### 8. Fin de partida
La partida finalizará cuando:
* Un jugador alcance la parte superior del tablero.
* Un jugador abandone la partida.
* Un jugador pierda la conexión.

### 9. Pantalla de resultados
Al finalizar una partida deberá mostrarse:
* Ganador.
* Puntaje obtenido.
* Cantidad de líneas eliminadas.
* Duración de la partida.

### 11. Interfaz gráfica
La interfaz deberá desarrollarse utilizando Jetpack Compose.
El tablero deberá renderizarse utilizando Canvas.
No se permite utilizar imágenes para representar los bloques.
Los bloques deberán dibujarse mediante primitivas gráficas (drawLine, drawRect, etc).

### 12. Arquitectura
La aplicación deberá implementar:
* MVVM.
* Repository Pattern.
* Dependency injection.
* Coroutines.
* StateFlow.

### 13. Rendimiento
La aplicación deberá mantener una experiencia fluida durante toda la partida.
Los movimientos del jugador deberán reflejarse inmediatamente en pantalla.

### 14. Requerimiento especial
Durante una partida normal deberá existir algún elemento visual, mecánica o referencia relacionada con el número 37.
La implementación queda a criterio del equipo.