import java.util.function.Consumer;

/**
 * TAD Lista de reproducción implementado como lista doblemente enlazada circular.
 *
 * Invariantes:
 *  - Lista vacía: inicio == fin == actual == null y tamanio == 0.
 *  - Un solo nodo: su anterior y su siguiente apuntan a sí mismo.
 *  - Lista no vacía: fin.siguiente == inicio y inicio.anterior == fin.
 *  - Para todo nodo x: x.siguiente.anterior == x y x.anterior.siguiente == x.
 *  - actual siempre es un nodo que pertenece a la lista (o null si está vacía).
 *
 * Esta clase no imprime nada en consola: la presentación es responsabilidad del Menu.
 */
public class ListaReproduccion {

    private Nodo inicio;
    private Nodo fin;
    private Nodo actual;
    private int tamanio;

    /** Contador incremental: garantiza IDs únicos sin buscar duplicados (inserción O(1)). */
    private int siguienteId = 1;

    // ------------------------------------------------------------------
    // Consultas O(1)
    // ------------------------------------------------------------------

    public boolean estaVacia() {
        return tamanio == 0;
    }

    /** O(1): se lleva un contador, no se recorre la lista. */
    public int getTamanio() {
        return tamanio;
    }

    /** O(1). Devuelve null si la lista está vacía. */
    public Cancion getActual() {
        return actual == null ? null : actual.cancion;
    }

    /** O(1). Devuelve null si la lista está vacía. */
    public Cancion getPrimera() {
        return inicio == null ? null : inicio.cancion;
    }

    /** O(1). Devuelve null si la lista está vacía. */
    public Cancion getUltima() {
        return fin == null ? null : fin.cancion;
    }

    // ------------------------------------------------------------------
    // Inserción O(1)
    // ------------------------------------------------------------------

    /** Agrega una canción al inicio. O(1). Devuelve la canción creada (con su ID). */
    public Cancion agregarAlInicio(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (estaVacia()) {
            insertarEnListaVacia(nuevo);
        } else {
            enlazarEntreFinEInicio(nuevo);
            inicio = nuevo; // el nuevo nodo pasa a ser el primero
        }
        return nuevo.cancion;
    }

    /** Agrega una canción al final. O(1). Devuelve la canción creada (con su ID). */
    public Cancion agregarAlFinal(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (estaVacia()) {
            insertarEnListaVacia(nuevo);
        } else {
            enlazarEntreFinEInicio(nuevo);
            fin = nuevo; // el nuevo nodo pasa a ser el último
        }
        return nuevo.cancion;
    }

    /**
     * Crea la canción (valida los datos) y su nodo.
     * El ID solo se consume si los datos son válidos.
     */
    private Nodo crearNodo(String titulo, String artista, int duracion) {
        Cancion cancion = new Cancion(siguienteId, titulo, artista, duracion); // puede lanzar excepción
        siguienteId++;
        return new Nodo(cancion);
    }

    /** Primer nodo: se enlaza consigo mismo y se vuelve inicio, fin y actual. */
    private void insertarEnListaVacia(Nodo nuevo) {
        nuevo.siguiente = nuevo;
        nuevo.anterior = nuevo;
        inicio = nuevo;
        fin = nuevo;
        actual = nuevo; // regla: la primera canción insertada es la actual
        tamanio = 1;
    }

    /**
     * En una lista circular, insertar al inicio y al final es colocar el nodo
     * en el mismo lugar: entre fin e inicio. Solo cambia cuál referencia
     * (inicio o fin) se mueve después. No se modifica 'actual'.
     */
    private void enlazarEntreFinEInicio(Nodo nuevo) {
        nuevo.siguiente = inicio;
        nuevo.anterior = fin;
        fin.siguiente = nuevo;
        inicio.anterior = nuevo;
        tamanio++;
    }

    // ------------------------------------------------------------------
    // Navegación O(1)
    // ------------------------------------------------------------------

    /** Avanza a la siguiente canción. O(1). Devuelve la nueva actual o null si está vacía. */
    public Cancion avanzar() {
        if (estaVacia()) {
            return null;
        }
        actual = actual.siguiente; // desde el último llega al primero
        return actual.cancion;
    }

    /** Retrocede a la canción anterior. O(1). Devuelve la nueva actual o null si está vacía. */
    public Cancion retroceder() {
        if (estaVacia()) {
            return null;
        }
        actual = actual.anterior; // desde el primero llega al último
        return actual.cancion;
    }

    // ------------------------------------------------------------------
    // Búsqueda O(n)
    // ------------------------------------------------------------------

    /**
     * Localiza el nodo con el ID dado. O(n) en el peor caso.
     * Condición de término circular: se detiene al regresar a 'inicio',
     * nunca esperando encontrar null.
     */
    private Nodo buscarNodo(int id) {
        if (estaVacia()) {
            return null;
        }
        Nodo aux = inicio;
        do {
            if (aux.cancion.getId() == id) {
                return aux;
            }
            aux = aux.siguiente;
        } while (aux != inicio);
        return null;
    }

    /** Busca una canción por ID. O(n). Devuelve null si no existe. No modifica la lista. */
    public Cancion buscar(int id) {
        Nodo nodo = buscarNodo(id);
        return nodo == null ? null : nodo.cancion;
    }

    /** Convierte en actual la canción con ese ID. O(n). Devuelve false si no existe. */
    public boolean seleccionar(int id) {
        Nodo nodo = buscarNodo(id);
        if (nodo == null) {
            return false;
        }
        actual = nodo;
        return true;
    }

    // ------------------------------------------------------------------
    // Eliminación
    // ------------------------------------------------------------------

    /**
     * Elimina por ID. O(n) = búsqueda O(n) + desenlace O(1).
     * Devuelve la canción eliminada, o null si el ID no existe (la lista no cambia).
     */
    public Cancion eliminarPorId(int id) {
        Nodo nodo = buscarNodo(id);
        if (nodo == null) {
            return null;
        }
        desenlazar(nodo);
        return nodo.cancion;
    }

    /**
     * Elimina la canción actual. O(1): ya se tiene la referencia, no hay búsqueda.
     * La nueva actual es la que le seguía. Devuelve la eliminada o null si está vacía.
     */
    public Cancion eliminarActual() {
        if (estaVacia()) {
            return null;
        }
        Nodo eliminado = actual;
        desenlazar(eliminado);
        return eliminado.cancion;
    }

    /**
     * Quita un nodo de la estructura en O(1) a partir de su referencia.
     * Primero se guardan los vecinos para no perder nodos al cambiar enlaces.
     */
    private void desenlazar(Nodo nodo) {
        if (tamanio == 1) {
            // Única canción: la lista queda vacía.
            inicio = null;
            fin = null;
            actual = null;
        } else {
            Nodo ant = nodo.anterior;
            Nodo sig = nodo.siguiente;

            // Puentear el nodo: sus vecinos se apuntan entre sí.
            ant.siguiente = sig;
            sig.anterior = ant;

            // Actualizar referencias de la lista que apuntaban al nodo eliminado.
            if (nodo == inicio) {
                inicio = sig;
            }
            if (nodo == fin) {
                fin = ant;
            }
            if (nodo == actual) {
                actual = sig; // regla: la nueva actual es la que le seguía
            }
        }
        // El nodo eliminado ya no es alcanzable desde la lista;
        // se limpian sus enlaces y el recolector de basura podrá liberarlo.
        nodo.anterior = null;
        nodo.siguiente = null;
        tamanio--;
    }

    // ------------------------------------------------------------------
    // Recorridos O(n) y reproducción O(k)
    // ------------------------------------------------------------------

    /** Visita cada canción una sola vez desde el inicio hacia el final. O(n). */
    public void recorrerAdelante(Consumer<Cancion> visitar) {
        if (estaVacia()) {
            return;
        }
        Nodo aux = inicio;
        do {
            visitar.accept(aux.cancion);
            aux = aux.siguiente;
        } while (aux != inicio);
    }

    /** Visita cada canción una sola vez desde el final hacia el inicio. O(n). */
    public void recorrerAtras(Consumer<Cancion> visitar) {
        if (estaVacia()) {
            return;
        }
        Nodo aux = fin;
        do {
            visitar.accept(aux.cancion);
            aux = aux.anterior;
        } while (aux != fin);
    }

    /**
     * Reproducción simulada: k veces "mostrar actual y avanzar". O(k).
     * Al terminar, actual queda en la canción que correspondería reproducir después.
     * Devuelve cuántas canciones se reprodujeron (0 si la lista está vacía).
     */
    public int reproducir(int k, Consumer<Cancion> reproducirCancion) {
        if (k <= 0) {
            throw new IllegalArgumentException("k debe ser un entero positivo.");
        }
        if (estaVacia()) {
            return 0;
        }
        for (int i = 0; i < k; i++) {
            reproducirCancion.accept(actual.cancion);
            actual = actual.siguiente;
        }
        return k;
    }

    // ------------------------------------------------------------------
    // Verificación de invariantes (usada por las pruebas) O(n)
    // ------------------------------------------------------------------

    /**
     * Comprueba que la estructura cumple todas sus invariantes:
     * estado vacío correcto, circularidad, enlaces consistentes en ambos sentidos,
     * contador igual al número real de nodos y 'actual' perteneciente a la lista.
     */
    public boolean verificarConsistencia() {
        if (tamanio == 0) {
            return inicio == null && fin == null && actual == null;
        }
        if (inicio == null || fin == null || actual == null) {
            return false;
        }
        if (fin.siguiente != inicio || inicio.anterior != fin) {
            return false;
        }

        // Recorrido hacia adelante: enlaces simétricos, conteo y pertenencia de actual.
        Nodo aux = inicio;
        int cuenta = 0;
        boolean actualEncontrado = false;
        do {
            if (aux.siguiente == null || aux.anterior == null) {
                return false;
            }
            if (aux.siguiente.anterior != aux || aux.anterior.siguiente != aux) {
                return false;
            }
            if (aux == actual) {
                actualEncontrado = true;
            }
            cuenta++;
            if (cuenta > tamanio) {
                return false; // hay más nodos de los contados o el ciclo no cierra en inicio
            }
            aux = aux.siguiente;
        } while (aux != inicio);
        if (cuenta != tamanio || !actualEncontrado) {
            return false;
        }

        // Recorrido hacia atrás: también debe visitar exactamente 'tamanio' nodos.
        aux = fin;
        cuenta = 0;
        do {
            cuenta++;
            if (cuenta > tamanio) {
                return false;
            }
            aux = aux.anterior;
        } while (aux != fin);
        return cuenta == tamanio;
    }
}
