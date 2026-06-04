# Backend de Referencia

## Objetivo

El backend tiene la responsabilidad de coordinar la comunicación entre dos jugadores durante una partida de Tetris Duel.

La lógica del juego (movimiento de piezas, colisiones, eliminación de líneas, puntaje y renderizado) se ejecuta completamente en la aplicación Android.

El servidor únicamente:

* Crea salas.
* Permite unirse a salas.
* Inicia partidas.
* Transmite ataques.
* Notifica victorias.
* Gestiona desconexiones.

---
## Ojo
no olvidarse de abrir el firewall para que el celular pueda ver el socket. Desde el emulador conectarse a la IP de la máquina, no a localhost.

# Tecnologías Utilizadas

* Node.js
* Express
* Socket.IO
* CORS

---

# Instalación

Crear un proyecto Node.js:

```bash
npm init -y
```

Instalar dependencias:

```bash
npm install express socket.io cors
```

---

# Estructura

```text
backend/
│
├── package.json
└── server.js
```

---

# Implementación

## server.js

```javascript
const express = require("express");
const http = require("http");
const { Server } = require("socket.io");
const cors = require("cors");

const app = express();

app.use(cors());

const server = http.createServer(app);

const io = new Server(server, {
    cors: {
        origin: "*"
    }
});

const rooms = {};

io.on("connection", (socket) => {

    console.log("Connected:", socket.id);

    socket.on("create_room", () => {

        const roomId = Math.random()
            .toString(36)
            .substring(2, 8)
            .toUpperCase();

        rooms[roomId] = {
            players: [socket.id]
        };

        socket.join(roomId);

        socket.emit("room_created", {
            roomId
        });
    });

    socket.on("join_room", ({ roomId }) => {

        const room = rooms[roomId];

        if (!room) {
            socket.emit("error_message", {
                message: "Room not found"
            });
            return;
        }

        if (room.players.length >= 2) {
            socket.emit("error_message", {
                message: "Room full"
            });
            return;
        }

        room.players.push(socket.id);

        socket.join(roomId);

        io.to(roomId).emit("game_start");
    });

    socket.on("send_attack", ({ roomId, garbageLines }) => {

        socket.to(roomId).emit("receive_attack", {
            garbageLines
        });
    });

    socket.on("game_over", ({ roomId }) => {

        socket.to(roomId).emit("victory");
    });

    socket.on("disconnect", () => {

        for (const roomId in rooms) {

            const room = rooms[roomId];

            if (room.players.includes(socket.id)) {

                socket.to(roomId).emit("opponent_disconnected");

                delete rooms[roomId];
            }
        }
    });
});

app.get("/", (_, res) => {
    res.send("Tetris Duel Server Running");
});

server.listen(3000, () => {
    console.log("Server running on port 3000");
});
```

---

# Explicación del Código

## Creación del servidor

```javascript
const app = express();
const server = http.createServer(app);
```

Express proporciona el servidor HTTP básico y Socket.IO utiliza dicho servidor para gestionar las conexiones en tiempo real.

---

## Configuración de Socket.IO

```javascript
const io = new Server(server, {
    cors: {
        origin: "*"
    }
});
```

Permite conexiones desde la aplicación Android sin restricciones de origen.

---

## Almacenamiento de salas

```javascript
const rooms = {};
```

Mantiene las salas activas en memoria.

Ejemplo:

```javascript
{
    "ABCD12": {
        players: [
            "socket1",
            "socket2"
        ]
    }
}
```

---

## Evento: create_room

```javascript
socket.on("create_room", () => {})
```

Permite a un jugador crear una nueva sala.

Acciones realizadas:

1. Generar código único.
2. Registrar la sala.
3. Asociar al jugador a la sala.
4. Enviar el código generado al cliente.

Evento emitido:

```javascript
room_created
```

Respuesta:

```json
{
  "roomId": "ABCD12"
}
```

---

## Evento: join_room

```javascript
socket.on("join_room", ({ roomId }) => {})
```

Permite que un segundo jugador se conecte a una sala existente.

Validaciones:

* La sala debe existir.
* La sala no puede estar llena.

Si la unión es exitosa:

```javascript
io.to(roomId).emit("game_start");
```

Se notifica a ambos jugadores que la partida puede comenzar.

---

## Evento: send_attack

```javascript
socket.on("send_attack", ({ roomId, garbageLines }) => {})
```

Se ejecuta cuando un jugador elimina varias líneas y genera un ataque.

Ejemplo enviado por Android:

```json
{
  "roomId": "ABCD12",
  "garbageLines": 2
}
```

El servidor reenvía el ataque al rival:

```javascript
receive_attack
```

Respuesta:

```json
{
  "garbageLines": 2
}
```

La aplicación Android deberá agregar dos líneas basura al tablero del oponente.

---

## Evento: game_over

```javascript
socket.on("game_over", ({ roomId }) => {})
```

Cuando un jugador pierde, el servidor notifica inmediatamente la victoria al rival.

Evento enviado:

```javascript
victory
```

---

## Evento: disconnect

```javascript
socket.on("disconnect", () => {})
```

Se ejecuta cuando un jugador cierra la aplicación o pierde la conexión.

Acciones:

1. Buscar la sala correspondiente.
2. Notificar al oponente.
3. Eliminar la sala.

Evento emitido:

```javascript
opponent_disconnected
```

---

# Eventos del Sistema

## Cliente → Servidor

### Crear sala

```javascript
create_room
```

### Unirse a sala

```javascript
join_room
```

Payload:

```json
{
  "roomId": "ABCD12"
}
```

### Enviar ataque

```javascript
send_attack
```

Payload:

```json
{
  "roomId": "ABCD12",
  "garbageLines": 2
}
```

### Notificar derrota

```javascript
game_over
```

Payload:

```json
{
  "roomId": "ABCD12"
}
```

---

## Servidor → Cliente

### Sala creada

```javascript
room_created
```

### Inicio de partida

```javascript
game_start
```

### Recibir ataque

```javascript
receive_attack
```

### Victoria

```javascript
victory
```

### Oponente desconectado

```javascript
opponent_disconnected
```

### Error

```javascript
error_message
```

---

# Ejecución

Iniciar el servidor:

```bash
node server.js
```

Salida esperada:

```text
Server running on port 3000
```

---

# URL de Desarrollo

HTTP:

```text
http://localhost:3000
```

Socket.IO:

```text
ws://localhost:3000
```

---

# Consideraciones

Este backend es intencionalmente simple para fines académicos.

La lógica del juego permanece completamente en Android, reduciendo la carga del servidor y facilitando el desarrollo del proyecto dentro del tiempo establecido.
