# Reglas Globales — Tetris Duel Online

Restricciones técnicas extraídas del enunciado que aplican a **ambos desarrolladores** en todo momento.

---

## Interfaz Gráfica (Req. 11)

- La interfaz deberá desarrollarse utilizando **Jetpack Compose**.
- El tablero deberá renderizarse utilizando **Canvas**.
- **No se permite utilizar imágenes** para representar los bloques.
- Los bloques deberán dibujarse mediante **primitivas gráficas** (`drawLine`, `drawRect`, etc.).

---

## Arquitectura (Req. 12)

La aplicación deberá implementar:

- **MVVM**
- **Repository Pattern**
- **Dependency Injection**
- **Coroutines**
- **StateFlow**

---

## Tablero (Req. 2)

Dimensiones fijas:

- **10 columnas**
- **20 filas**

---

## Piezas (Req. 4)

El sistema deberá generar aleatoriamente las siguientes piezas:

- I, O, T, S, Z, J, L

---

## Sistema de Ataque (Req. 6)

Tabla de líneas basura enviadas al oponente:

| Líneas eliminadas | Líneas basura enviadas |
|:-:|:-:|
| 1 | 0 |
| 2 | 1 |
| 3 | 2 |
| 4 | 4 |

Las líneas basura:

- Se agregarán desde la parte inferior.
- Tendrán una celda vacía aleatoria.
- Empujarán el contenido actual hacia arriba.

---

## Comunicación en Tiempo Real (Req. 7)

- La aplicación deberá utilizar **Socket.IO**.
- La lógica local del tablero deberá continuar funcionando aunque exista **latencia en la red**.

### Eventos Cliente → Servidor

| Evento | Descripción |
|---|---|
| `create_room` | Crear una sala |
| `join_room` | Unirse a sala existente |
| `send_attack` | Enviar líneas de ataque |
| `game_over` | Notificar derrota propia |

### Eventos Servidor → Cliente

| Evento | Descripción |
|---|---|
| `room_created` | Confirmación de sala creada con código |
| `game_start` | Inicio de partida (ambos jugadores conectados) |
| `receive_attack` | Recibir líneas basura del oponente |
| `victory` | El oponente perdió, somos ganadores |
| `opponent_disconnected` | El oponente se desconectó |
| `error_message` | Error del servidor (sala no encontrada, llena, etc.) |

---

## Fin de Partida (Req. 8)

La partida finalizará cuando:

- Un jugador alcance la parte superior del tablero.
- Un jugador abandone la partida.
- Un jugador pierda la conexión.

---

## Requerimiento Especial (Req. 14)

Durante una partida normal deberá existir algún elemento visual, mecánica o referencia relacionada con el **número 37**. La implementación queda a criterio del equipo.

---

## Rendimiento (Req. 13)

- La aplicación deberá mantener una experiencia fluida durante toda la partida.
- Los movimientos del jugador deberán reflejarse **inmediatamente** en pantalla.

---

## Nota de Red (socket.md)

- Desde el **emulador**: conectarse a la **IP de la máquina**, no a `localhost`.
- Abrir el **firewall** para que el celular físico pueda ver el socket.
- El servidor corre en el **puerto 3000**.
