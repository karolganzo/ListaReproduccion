# Gestor de listas de reproducción — Lista doblemente enlazada circular

Proyecto de Estructura de Datos. Administra una lista de canciones con navegación
circular en ambos sentidos, implementada con nodos y referencias propios
(sin `LinkedList`, `ArrayList`, arreglos ni otras colecciones).

## Versión de Java

- Desarrollado y probado con **OpenJDK 21**.
- El código solo usa características de Java 8, así que compila con **JDK 8 o superior**.

Comprobar la versión instalada:

```bash
java -version
javac -version
```

## Estructura del proyecto

```
ListaReproduccion/
├── README.md
└── src/
    ├── Cancion.java            # Datos de la canción (id, título, artista, duración > 0)
    ├── Nodo.java               # Nodo con referencias anterior y siguiente
    ├── ListaReproduccion.java  # TAD: lista doblemente enlazada circular
    ├── Menu.java               # Aplicación de consola (punto de entrada)
    └── PruebasLista.java       # Pruebas reproducibles
```

## Compilación

Desde la carpeta `ListaReproduccion`:

```bash
javac -encoding UTF-8 -d out src/*.java
```

En Windows (PowerShell o CMD) el comando es el mismo; si `src/*.java` no funciona, usar `src\*.java`.

## Ejecución

Programa principal (menú):

```bash
java -cp out Menu
```

Pruebas:

```bash
java -cp out PruebasLista
```

Cada prueba muestra el estado inicial, la operación, el resultado esperado y el obtenido.
Al final se imprime el resumen; el programa termina con código 0 si todas pasan.

> Si los acentos se ven mal en la consola de Windows, ejecutar antes `chcp 65001`.

## Complejidad de las operaciones

`n` = cantidad de canciones, `k` = canciones a reproducir.

| Operación | Complejidad | Motivo |
|---|---|---|
| Agregar al inicio o al final | O(1) | Se enlaza entre `fin` e `inicio`; ID por contador, sin buscar duplicados |
| Consultar la canción actual | O(1) | Referencia `actual` |
| Avanzar o retroceder | O(1) | `actual.siguiente` / `actual.anterior` |
| Eliminar la canción actual | O(1) | Ya se tiene la referencia; solo se cambian enlaces |
| Consultar la cantidad | O(1) | Contador `tamanio` |
| Buscar o seleccionar por ID | O(n) | Recorrido hasta encontrarlo o volver a `inicio` |
| Eliminar por ID | O(n) | Búsqueda O(n) + desenlace O(1) |
| Mostrar todas las canciones | O(n) | Cada nodo se visita una vez por sentido |
| Simular `k` reproducciones | O(k) | `k` avances de O(1) |

## Integrantes

- Trejo Ganzo Christopher Karol (trabajo individual, autorizado por el profesor)
