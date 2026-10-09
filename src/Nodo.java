/**
 * Nodo de la lista doblemente enlazada circular.
 * Guarda una canción y dos referencias: al nodo anterior y al siguiente.
 *
 * Los campos son de acceso de paquete (sin modificador) para que solo
 * ListaReproduccion los manipule; el menú nunca toca nodos.
 */
class Nodo {

    Cancion cancion;
    Nodo anterior;
    Nodo siguiente;

    Nodo(Cancion cancion) {
        this.cancion = cancion;
        // Un nodo recién creado aún no está enlazado.
        this.anterior = null;
        this.siguiente = null;
    }
}
