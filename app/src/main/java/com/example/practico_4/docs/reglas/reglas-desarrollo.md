# Reglas de Desarrollo del Equipo — Tetris Duel Online

Reglas internas del equipo que aplican a **todo el código generado** durante el proyecto.

---

## 1. Complejidad

- Aplicar **Clean Code** y una buena estructura de carpetas.
- Mantenerlo al nivel de un **proyecto universitario normal**.
- **No sobre-arquitecturizar** ni usar patrones excesivamente complejos.
- Tres líneas similares son mejor que una abstracción prematura.

---

## 2. Idioma de la Nomenclatura

Toda la nomenclatura debe estar en **español**:

- Nombres de variables
- Nombres de funciones
- Nombres de clases
- Nombres de archivos

Sin embargo, se deben respetar al 100% las convenciones estándar de Kotlin y Compose:

| Tipo | Convención | Ejemplo |
|---|---|---|
| Clases / Composables | PascalCase | `MotorJuego`, `PantallaLobby` |
| Funciones / Variables | camelCase | `moverIzquierda()`, `estadoTablero` |
| Constantes | UPPER_SNAKE_CASE | `MAX_COLUMNAS`, `VELOCIDAD_INICIAL` |
| Archivos Kotlin | PascalCase | `GeneradorPiezas.kt` |

---

## 3. Diseño de Interfaz (UI)

- Mantener la interfaz gráfica **simple, limpia y funcional**.
- Nada de diseños extravagantes ni elementos decorativos innecesarios.
- El objetivo es que sea **usable y legible**, no impresionante visualmente.

---

## 4. Cero Alucinaciones

- Aplicar **única y exclusivamente** lo que pide el enunciado (`Practico-4.md`).
- **No inventar** funcionalidades extra.
- **No agregar** pantallas que no se piden.
- **No asumir** nada fuera del documento.
- Si hay duda sobre un requisito, consultar el enunciado antes de implementar.

---

## 5. Estructura de Carpetas del Proyecto

```
app/src/main/java/com/example/practico_4/
├── di/
│   └── AppModule.kt
├── datos/
│   ├── modelos/
│   │   ├── TipoPieza.kt
│   │   ├── Pieza.kt
│   │   ├── EstadoTablero.kt
│   │   └── EstadoPartida.kt
│   ├── repositorios/
│   │   └── SocketRepositorio.kt
│   └── red/
│       └── SocketServicio.kt
├── dominio/
│   └── motor/
│       ├── GeneradorPiezas.kt
│       └── MotorJuego.kt
├── presentacion/
│   ├── lobby/
│   │   ├── LobbyViewModel.kt
│   │   └── PantallaLobby.kt
│   ├── juego/
│   │   ├── JuegoViewModel.kt
│   │   ├── PantallaJuego.kt
│   │   └── componentes/
│   │       ├── TableroCanvas.kt
│   │       ├── VistaPreviaCanvas.kt
│   │       └── Controles.kt
│   └── resultados/
│       └── PantallaResultados.kt
└── MainActivity.kt
```

---

## 6. Comentarios en el Código

- Agregar comentarios **solo cuando el porqué no es obvio**.
- No documentar lo que ya dicen los nombres de las funciones y variables.
- No escribir bloques de comentarios multi-línea ni docstrings extensos.
