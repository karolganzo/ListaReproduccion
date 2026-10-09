import java.io.PrintStream;
import java.util.Scanner;

/**
 * Aplicación de consola. Solo se encarga de leer datos, validarlos y mostrar
 * resultados; toda la lógica de nodos y enlaces vive en ListaReproduccion.
 *
 * Recibe el Scanner y el PrintStream por constructor para poder probarse
 * con entradas simuladas (ver PruebasLista).
 */
public class Menu {

    private final ListaReproduccion lista = new ListaReproduccion();
    private final Scanner entrada;
    private final PrintStream salida;

    public Menu(Scanner entrada, PrintStream salida) {
        this.entrada = entrada;
        this.salida = salida;
    }

    public static void main(String[] args) {
        new Menu(new Scanner(System.in), System.out).ejecutar();
    }

    public void ejecutar() {
        salida.println("=== Gestor de listas de reproducción (lista doblemente enlazada circular) ===");
        boolean continuar = true;
        while (continuar) {
            mostrarOpciones();
            Integer opcion = leerEntero("Elige una opción: ");
            if (opcion == null) { // fin de la entrada (EOF)
                break;
            }
            continuar = atender(opcion);
        }
        salida.println("Hasta luego.");
    }

    private void mostrarOpciones() {
        salida.println();
        salida.println(" 1. Agregar canción al inicio");
        salida.println(" 2. Agregar canción al final");
        salida.println(" 3. Buscar canción por ID");
        salida.println(" 4. Seleccionar canción por ID (hacerla actual)");
        salida.println(" 5. Eliminar canción por ID");
        salida.println(" 6. Eliminar la canción actual");
        salida.println(" 7. Consultar la canción actual");
        salida.println(" 8. Avanzar a la siguiente canción");
        salida.println(" 9. Retroceder a la canción anterior");
        salida.println("10. Mostrar la lista en ambos sentidos");
        salida.println("11. Consultar la cantidad de canciones");
        salida.println("12. Simular reproducción de k canciones");
        salida.println("13. Salir");
    }

    /** Ejecuta una opción. Devuelve false cuando hay que salir. */
    private boolean atender(int opcion) {
        switch (opcion) {
            case 1:  agregar(true); break;
            case 2:  agregar(false); break;
            case 3:  buscar(); break;
            case 4:  seleccionar(); break;
            case 5:  eliminarPorId(); break;
            case 6:  eliminarActual(); break;
            case 7:  consultarActual(); break;
            case 8:  avanzar(); break;
            case 9:  retroceder(); break;
            case 10: mostrarLista(); break;
            case 11: salida.println("Cantidad de canciones: " + lista.getTamanio()); break;
            case 12: reproducir(); break;
            case 13: return false;
            default: salida.println("Opción inválida: elige un número del 1 al 13.");
        }
        return true;
    }

    // ------------------------------------------------------------------
    // Opciones
    // ------------------------------------------------------------------

    private void agregar(boolean alInicio) {
        String titulo = leerTextoNoVacio("Título: ");
        if (titulo == null) return;
        String artista = leerTextoNoVacio("Artista: ");
        if (artista == null) return;
        Integer duracion = leerEnteroPositivo("Duración en segundos (> 0): ");
        if (duracion == null) return;

        Cancion c = alInicio
                ? lista.agregarAlInicio(titulo, artista, duracion)
                : lista.agregarAlFinal(titulo, artista, duracion);
        salida.println("Agregada " + (alInicio ? "al inicio" : "al final") + ": " + c);
    }

    private void buscar() {
        if (avisarSiVacia()) return;
        Integer id = leerEntero("ID a buscar: ");
        if (id == null) return;
        Cancion c = lista.buscar(id);
        if (c == null) {
            salida.println("No existe una canción con ID " + id + ". La lista no cambió.");
        } else {
            salida.println("Encontrada:");
            salida.println("  ID:       " + c.getId());
            salida.println("  Título:   " + c.getTitulo());
            salida.println("  Artista:  " + c.getArtista());
            salida.println("  Duración: " + c.duracionFormateada() + " (" + c.getDuracion() + " s)");
        }
    }

    private void seleccionar() {
        if (avisarSiVacia()) return;
        Integer id = leerEntero("ID a seleccionar: ");
        if (id == null) return;
        if (lista.seleccionar(id)) {
            salida.println("Canción actual: " + lista.getActual());
        } else {
            salida.println("No existe una canción con ID " + id + ". La canción actual no cambió.");
        }
    }

    private void eliminarPorId() {
        if (avisarSiVacia()) return;
        Integer id = leerEntero("ID a eliminar: ");
        if (id == null) return;
        Cancion eliminada = lista.eliminarPorId(id);
        if (eliminada == null) {
            salida.println("No existe una canción con ID " + id + ". La lista no cambió.");
        } else {
            salida.println("Eliminada: " + eliminada);
            mostrarActualTrasEliminar();
        }
    }

    private void eliminarActual() {
        if (avisarSiVacia()) return;
        Cancion eliminada = lista.eliminarActual();
        salida.println("Eliminada la canción actual: " + eliminada);
        mostrarActualTrasEliminar();
    }

    private void mostrarActualTrasEliminar() {
        if (lista.estaVacia()) {
            salida.println("La lista quedó vacía.");
        } else {
            salida.println("Canción actual: " + lista.getActual());
        }
    }

    private void consultarActual() {
        if (avisarSiVacia()) return;
        salida.println("Canción actual: " + lista.getActual());
    }

    private void avanzar() {
        if (avisarSiVacia()) return;
        salida.println("Siguiente -> " + lista.avanzar());
    }

    private void retroceder() {
        if (avisarSiVacia()) return;
        salida.println("Anterior <- " + lista.retroceder());
    }

    private void mostrarLista() {
        if (avisarSiVacia()) return;
        Cancion actual = lista.getActual();
        salida.println("Hacia adelante (inicio -> fin):");
        lista.recorrerAdelante(c -> salida.println((c == actual ? "  * " : "    ") + c));
        salida.println("Hacia atrás (fin -> inicio):");
        lista.recorrerAtras(c -> salida.println((c == actual ? "  * " : "    ") + c));
        salida.println("(* = canción actual)  Total: " + lista.getTamanio());
    }

    private void reproducir() {
        if (avisarSiVacia()) return;
        Integer k = leerEnteroPositivo("¿Cuántas canciones reproducir? (k > 0): ");
        if (k == null) return;
        lista.reproducir(k, c -> salida.println("Reproduciendo: " + c));
        salida.println("Siguiente en reproducirse (actual): " + lista.getActual());
    }

    private boolean avisarSiVacia() {
        if (lista.estaVacia()) {
            salida.println("La lista está vacía. Agrega una canción primero (opciones 1 o 2).");
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Lectura y validación de entradas
    // ------------------------------------------------------------------

    /** Lee una línea; devuelve null si se terminó la entrada. */
    private String leerLinea(String mensaje) {
        salida.print(mensaje);
        if (!entrada.hasNextLine()) {
            salida.println();
            return null;
        }
        return entrada.nextLine();
    }

    /** Repite hasta recibir un entero. Devuelve null solo si se terminó la entrada. */
    private Integer leerEntero(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) return null;
            Integer valor = interpretarEntero(linea);
            if (valor != null) return valor;
            salida.println("Entrada inválida: escribe un número entero.");
        }
    }

    private Integer leerEnteroPositivo(String mensaje) {
        while (true) {
            Integer valor = leerEntero(mensaje);
            if (valor == null) return null;
            if (valor > 0) return valor;
            salida.println("Entrada inválida: el número debe ser mayor que cero.");
        }
    }

    private String leerTextoNoVacio(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) return null;
            if (!linea.trim().isEmpty()) return linea.trim();
            salida.println("Entrada inválida: el texto no puede estar vacío.");
        }
    }

    /** Convierte texto a entero; devuelve null si no es un entero válido. */
    static Integer interpretarEntero(String texto) {
        if (texto == null) return null;
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
