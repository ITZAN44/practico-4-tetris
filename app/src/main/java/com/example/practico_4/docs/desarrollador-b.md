# Desarrollador B — Tareas (50% final)

**Prerequisito:** El Desarrollador A entregó el motor del juego funcionando en modo local.
Verificar los criterios de entrega al final de `desarrollador-a.md` antes de empezar.

**Objetivo:** Conectar el juego a la red, construir el lobby, los resultados y completar la experiencia multijugador.

---

## Prerrequisitos

Leer y tener siempre a mano:

- `docs/reglas-globales.md`
- `docs/reglas-desarrollo.md`
- `docs/desarrollador-a.md` — para entender qué ya existe y no duplicar

---

## Tarea 1 — Módulo de Red (`datos/red/` y `datos/repositorios/`)

### `SocketServicio.kt`

Clase que encapsula el cliente Socket.IO:

- [ ] Conectar al servidor (URL configurable, sin hardcodear `localhost`)
- [ ] Desconectar limpiamente
- [ ] Emitir eventos al servidor:
  - `create_room`
  - `join_room` con payload `{ roomId }`
  - `send_attack` con payload `{ roomId, garbageLines }`
  - `game_over` con payload `{ roomId }`
- [ ] Escuchar eventos del servidor y exponerlos como `Flow` o callbacks:
  - `room_created` → devuelve `roomId`
  - `game_start`
  - `receive_attack` → devuelve `garbageLines: Int`
  - `victory`
  - `opponent_disconnected`
  - `error_message` → devuelve mensaje

### `SocketRepositorio.kt`

- [ ] Interfaz `SocketRepositorio` con los métodos necesarios
- [ ] Implementación `SocketRepositorioImpl` que usa `SocketServicio`
- [ ] Registrar en `AppModule.kt` (DI con Hilt)

---

## Tarea 2 — Pantalla Lobby (`presentacion/lobby/`)

### `LobbyViewModel.kt`

- [ ] `crearSala()` — emite `create_room`, espera `room_created` y guarda el `roomId`
- [ ] `unirseASala(codigo)` — emite `join_room`, maneja errores (`error_message`)
- [ ] `estadoConexion: StateFlow<EstadoConexion>` — ESPERANDO / CONECTADO / ERROR
- [ ] `codigoSala: StateFlow<String>` — código visible en pantalla al crear
- [ ] Escucha `game_start` y navega a `PantallaJuego`

### `PantallaLobby.kt`

- [ ] Botón **"Crear Sala"** → llama a `crearSala()` → muestra el código generado
- [ ] Campo de texto + Botón **"Unirse"** → llama a `unirseASala(codigo)`
- [ ] Indicador de estado de conexión de ambos jugadores
- [ ] Mensaje de error si la sala no existe o está llena
- [ ] Transición automática a `PantallaJuego` al recibir `game_start`

---

## Tarea 3 — Integración Socket ↔ Motor de Juego (`presentacion/juego/JuegoViewModel.kt`)

Modificar el `JuegoViewModel` que dejó el Desarrollador A:

- [ ] Inyectar `SocketRepositorio` en el ViewModel
- [ ] Al eliminar líneas → calcular ataque (`MotorJuego.calcularLineasAtaque()`) → emitir `send_attack`
- [ ] Escuchar `receive_attack` → llamar `MotorJuego.aplicarLineasBasura()` con el estado actual
- [ ] Al detectar derrota (`estaTerminado == true`) → emitir `game_over`
- [ ] Escuchar `victory` → marcar al jugador local como ganador
- [ ] Escuchar `opponent_disconnected` → terminar la partida

---

## Tarea 4 — Estado del Oponente en Pantalla de Juego

Completar la sección reservada en `PantallaJuego.kt`:

- [ ] Mostrar indicador de **conexión del oponente** (conectado / desconectado)
- [ ] Mostrar **estado del oponente** (en juego / perdió)

> Nota: el servidor de referencia no transmite el tablero del oponente, solo los ataques y eventos de derrota/desconexión. La sección de estado debe mostrar lo que el servidor efectivamente envía.

---

## Tarea 5 — Manejo de Fin de Partida

- [ ] Al recibir `victory`: guardar resultado como GANADOR y navegar a `PantallaResultados`
- [ ] Al detectar derrota local: guardar resultado como PERDEDOR y navegar a `PantallaResultados`
- [ ] Al recibir `opponent_disconnected`: fin de partida por desconexión, navegar a `PantallaResultados`
- [ ] Registrar tiempo de inicio al recibir `game_start` para calcular la duración

---

## Tarea 6 — Pantalla de Resultados (`presentacion/resultados/`)

### `PantallaResultados.kt`

Composable que recibe los datos del resultado vía argumentos de navegación o ViewModel:

- [ ] Mostrar **Ganador** (nombre del jugador o "Tú" / "Oponente")
- [ ] Mostrar **Puntaje obtenido**
- [ ] Mostrar **Cantidad de líneas eliminadas**
- [ ] Mostrar **Duración de la partida**
- [ ] Botón para volver al Lobby

---

## Tarea 7 — Requerimiento Especial (Número 37)

Durante una partida normal deberá existir algún elemento visual, mecánica o referencia relacionada con el **número 37**. La implementación queda a criterio del equipo.

Ejemplos posibles (elegir uno):

- Efecto visual especial cuando el puntaje alcanza 37 o un múltiplo de 37
- Indicador especial al eliminar la línea número 37
- Combo especial que se activa en algún momento relacionado con el 37

- [ ] Definir con el equipo qué se va a implementar
- [ ] Implementarlo en la pantalla de juego

---

## Tarea 8 — Navegación General

Configurar la navegación entre pantallas en `MainActivity.kt` o en un archivo dedicado:

- [ ] `NavHost` con 3 rutas:
  - `"lobby"` → `PantallaLobby`
  - `"juego"` → `PantallaJuego`
  - `"resultados"` → `PantallaResultados`
- [ ] Pasar los datos del resultado a `PantallaResultados` (puntaje, líneas, duración, ganador)

---

## Criterio de Verificación Final

El proyecto está completo cuando:

1. Dos dispositivos/emuladores pueden crear y unirse a una sala con código
2. La partida inicia automáticamente cuando se conecta el segundo jugador
3. Los movimientos se reflejan inmediatamente en pantalla (sin esperar la red)
4. Al eliminar 2+ líneas, el oponente recibe las líneas basura correspondientes
5. Cuando un jugador pierde, el oponente recibe la notificación de victoria
6. La pantalla de resultados muestra ganador, puntaje, líneas y duración
7. La desconexión de un jugador termina la partida correctamente
8. El requerimiento del número 37 está visible en una partida normal
