# Desarrollador A — Tareas (50% inicial)

**Objetivo:** Construir la base sólida del motor del juego sobre la que el Desarrollador B pueda conectar el multijugador.

**Estado: ✅ COMPLETO — entregado al Desarrollador B**

---

## Prerrequisitos

- `docs/reglas/reglas-globales.md`
- `docs/reglas/reglas-desarrollo.md`

---

## Tarea 1 — Setup del Proyecto ✅

- [x] Agregar dependencias en `build.gradle.kts` (app):
  - Hilt: `hilt-android` + `hilt-android-compiler` (KSP) — versión `2.59.2`
  - hilt-navigation-compose: `1.2.0`
  - Coroutines: `kotlinx-coroutines-android` — versión `1.9.0`
  - lifecycle-viewmodel-compose: `2.10.0`
- [x] Configurar Hilt en `MainActivity.kt` (`@AndroidEntryPoint`)
- [x] Crear `AppAplicacion.kt` con `@HiltAndroidApp`
- [x] Crear `AppModule.kt` en `di/` con stub listo para que B agregue `SocketRepositorio`
- [x] Agregar permiso `INTERNET` en `AndroidManifest.xml`
- [x] Proyecto compila y corre sin errores

> **Notas de versiones resueltas:**
> - KSP: `2.2.10-2.0.2` (Google cambió el esquema de versioning a partir de Kotlin 2.2.x)
> - Hilt: `2.59.2` (versión mínima compatible con AGP 9.x — versiones anteriores usan `BaseExtension` que fue removida)
> - `gradle.properties`: se agregó `android.disallowKotlinSourceSets=false` (compatibilidad KSP + AGP 9 built-in Kotlin)

---

## Tarea 2 — Modelos de Datos (`datos/modelos/`) ✅

### `TipoPieza.kt` ✅

- [x] Enum con los 7 tipos: `I`, `O`, `T`, `S`, `Z`, `J`, `L` + `BASURA` (para líneas de ataque)
- [x] Cada tipo tiene su forma definida como lista de coordenadas `(deltaFila, deltaColumna)` con 4 rotaciones
- [x] Cada tipo tiene su color asociado vía `fun color(): Color`

### `Pieza.kt` ✅

- [x] Data class con `tipo`, `fila`, `columna`, `rotacion`
- [x] Método `celdas()` que resuelve las posiciones absolutas en el tablero

### `EstadoTablero.kt` ✅

- [x] Data class con `grilla: List<List<TipoPieza?>>` (10×20), `piezaActiva`, `piezaSiguiente`, `puntaje`, `lineasEliminadas`, `estaTerminado`, `lineasAtaquePendientes`
- [x] Constantes `FILAS = 20` y `COLUMNAS = 10` en el companion object

### `EstadoPartida.kt` ✅

- [x] Enum con estados: `ESPERANDO`, `EN_JUEGO`, `FIN`

---

## Tarea 3 — Motor del Juego (`dominio/motor/`) ✅

### `GeneradorPiezas.kt` ✅

- [x] Genera aleatoriamente uno de los 7 tipos jugables (excluye `BASURA`)
- [x] Crea la pieza inicial centrada en la columna superior (`columna = COLUMNAS/2 - 2`)

### `MotorJuego.kt` ✅

#### Movimiento y colisiones

- [x] `moverIzquierda(estado)` — con validación de colisión
- [x] `moverDerecha(estado)` — con validación de colisión
- [x] `rotar(estado)` — rotación 90° horario con validación de colisión
- [x] `bajar(estado)` — baja una fila; si colisiona, llama a `colocarPieza`
- [x] `caerInstantaneo(estado)` — baja hasta la última posición libre y coloca
- [x] `calcularFilaSombra(estado)` — devuelve la fila de la sombra para renderizado
- [x] Detección de colisiones: paredes, suelo y bloques existentes

#### Colocación y líneas

- [x] `colocarPieza(estado)` — fija la pieza, limpia líneas completas, genera la siguiente
- [x] `eliminarLineasCompletas` — elimina filas llenas y baja las superiores
- [x] `calcularLineasAtaque(lineasEliminadas)` — tabla 1→0, 2→1, 3→2, 4→4
- [x] `calcularPuntaje` — 1 línea: 100pts, 2: 300pts, 3: 500pts, 4: 800pts

#### Fin de partida y ataques

- [x] Derrota detectada en `colocarPieza` cuando la nueva pieza colisiona al aparecer → `estaTerminado = true`
- [x] `aplicarLineasBasura(estado, cantidad)` — agrega líneas basura desde abajo (celda vacía aleatoria por línea)
- [x] `lineasAtaquePendientes` en `EstadoTablero` para que el ViewModel las envíe vía socket (Desarrollador B)

---

## Tarea 4 — ViewModel del Juego (`presentacion/juego/`) ✅

### `JuegoViewModel.kt` ✅

- [x] `_estadoTablero: MutableStateFlow<EstadoTablero>` — estado interno
- [x] `estadoTablero: StateFlow<EstadoTablero>` — expuesto a la UI
- [x] `iniciarJuego()` — inicializa el tablero y arranca el loop de gravedad
- [x] Loop de gravedad con Coroutines (`viewModelScope`) — intervalo 800ms
- [x] `moverIzquierda()`, `moverDerecha()`, `rotar()`, `acelerar()`, `caerInstantaneo()`
- [x] `aplicarLineasBasura(cantidad)` — stub listo para que B la llame al recibir `receive_attack`
- [x] `procesarAtaquePendiente()` — stub con comentario `// Desarrollador B` para envío vía socket
- [x] Derrota detectada: el loop de gravedad se detiene cuando `estaTerminado == true`

---

## Tarea 5 — Pantalla de Juego (`presentacion/juego/`) ✅

### `TableroCanvas.kt` ✅

- [x] Renderiza la grilla vacía con `drawLine`
- [x] Renderiza bloques fijos con `drawRect` (color por `TipoPieza`)
- [x] Renderiza la pieza activa
- [x] Renderiza la sombra de caída (transparencia 30% del color de la pieza)

### `VistaPreviaCanvas.kt` ✅

- [x] Renderiza la pieza siguiente centrada en un Canvas 4×4 bloques

### `Controles.kt` ✅

- [x] Botón ← (mover izquierda)
- [x] Botón → (mover derecha)
- [x] Botón ↻ (rotar)
- [x] Botón ↓ (acelerar caída)
- [x] Botón ⬇ (caída instantánea)

### `PantallaJuego.kt` ✅

- [x] Encabezado con puntaje, líneas eliminadas, vista previa y sección de oponente (placeholder para B)
- [x] Tablero ocupa todo el espacio disponible (`weight(1f)`)
- [x] Mensaje "GAME OVER" visible al terminar
- [x] Controles al pie de la pantalla

---

## Criterios de Entrega — Todos Verificados ✅

| # | Criterio | Estado |
|---|---|---|
| 1 | Tablero renderizado con Canvas (10×20, colores por tipo) | ✅ |
| 2 | 7 piezas aparecen aleatoriamente y se mueven/rotan | ✅ |
| 3 | Gravedad funciona (piezas bajan solas) | ✅ |
| 4 | Caída instantánea y sombra visibles | ✅ |
| 5 | Líneas completas se eliminan y puntaje se actualiza | ✅ |
| 6 | Derrota detectada (`estaTerminado = true`) | ✅ |
| 7 | `aplicarLineasBasura()` implementada y lista | ✅ |
| 8 | `JuegoViewModel` expone `estadoTablero: StateFlow` | ✅ |
