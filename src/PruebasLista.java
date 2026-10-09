import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Pruebas reproducibles de ListaReproduccion y del Menu (sin dependencias externas).
 *
 * Cada caso imprime: estado inicial, operación, resultado esperado y resultado obtenido.
 * Después de cada modificación se verifica la cantidad de elementos y la
 * consistencia de los enlaces (verificarConsistencia).
 *
 * Ejecutar:  java -cp out PruebasLista
 */
public class PruebasLista {

    private static int total = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        p01ListaVacia();
        p02PrimeraInsercion();
        p03NavegacionUnSoloNodo();
        p04InsercionesInicioYFinal();
        p05RecorridosCincoCanciones();
        p06AvanceDeUltimaAPrimera();
        p07RetrocesoDePrimeraAUltima();
        p08EliminarPrimero();
        p09EliminarUltimo();
        p10EliminarIntermedio();
        p11EliminarActual();
        p12EliminarUnicaCancion();
        p13IdInexistente();
        p14ReproduccionKMayorQueN();
        p15SeleccionarPorId();
        p16EntradasInvalidasEnLista();
        p17EntradasInvalidasEnMenu();
        p18IdsUnicosIncrementales();

        System.out.println();
        System.out.println("==============================================");
        System.out.println("Comprobaciones: " + total + "  Correctas: " + (total - fallidas)
                + "  Fallidas: " + fallidas);
        System.out.println(fallidas == 0 ? "TODAS LAS PRUEBAS PASARON" : "HAY PRUEBAS FALLIDAS");
        System.exit(fallidas == 0 ? 0 : 1);
    }

    // ==================================================================
    // Casos de prueba
    // ==================================================================

    private static void p01ListaVacia() {
        titulo("P01 - Operaciones sobre una lista vacía");
        ListaReproduccion l = new ListaReproduccion();
        String ini = estado(l);
        caso(ini, "getTamanio()", "0", "" + l.getTamanio());
        caso(ini, "getActual()", "null", "" + l.getActual());
        caso(ini, "avanzar()", "null", "" + l.avanzar());
        caso(ini, "retroceder()", "null", "" + l.retroceder());
        caso(ini, "buscar(1)", "null", "" + l.buscar(1));
        caso(ini, "seleccionar(1)", "false", "" + l.seleccionar(1));
        caso(ini, "eliminarPorId(1)", "null", "" + l.eliminarPorId(1));
        caso(ini, "eliminarActual()", "null", "" + l.eliminarActual());
        caso(ini, "reproducir(3)", "0", "" + l.reproducir(3, c -> { }));
        caso(ini, "recorrer adelante / atrás", "[] / []", ids(l, true) + " / " + ids(l, false));
        verificar(l, 0);
    }

    private static void p02PrimeraInsercion() {
        titulo("P02 - Inserción de la primera canción");
        ListaReproduccion l = new ListaReproduccion();
        String ini = estado(l);
        Cancion c = l.agregarAlFinal("Bohemian Rhapsody", "Queen", 354);
        caso(ini, "agregarAlFinal(A)", "ID=1, actual=1", "ID=" + c.getId() + ", actual=" + idDe(l.getActual()));
        caso(estado(l), "inicio y fin", "1 y 1", idDe(l.getPrimera()) + " y " + idDe(l.getUltima()));
        verificar(l, 1);
    }

    private static void p03NavegacionUnSoloNodo() {
        titulo("P03 - Navegación en ambos sentidos con un solo nodo");
        ListaReproduccion l = new ListaReproduccion();
        l.agregarAlInicio("Solo", "Artista", 100);
        String ini = estado(l);
        caso(ini, "avanzar()", "1 (se apunta a sí mismo)", idDe(l.avanzar()) + " (se apunta a sí mismo)");
        caso(estado(l), "retroceder()", "1 (se apunta a sí mismo)", idDe(l.retroceder()) + " (se apunta a sí mismo)");
        caso(estado(l), "avanzar() x3", "1", idDe(avanzarN(l, 3)));
        verificar(l, 1);
    }

    private static void p04InsercionesInicioYFinal() {
        titulo("P04 - Inserciones al inicio y al final (la actual no cambia)");
        ListaReproduccion l = new ListaReproduccion();
        l.agregarAlFinal("B", "Art", 120);        // ID 1 -> actual
        String ini = estado(l);
        l.agregarAlInicio("A", "Art", 120);       // ID 2
        caso(ini, "agregarAlInicio(ID2)", "[2 1] actual=1", ids(l, true) + " actual=" + idDe(l.getActual()));
        ini = estado(l);
        l.agregarAlFinal("C", "Art", 120);        // ID 3
        caso(ini, "agregarAlFinal(ID3)", "[2 1 3] actual=1", ids(l, true) + " actual=" + idDe(l.getActual()));
        ini = estado(l);
        l.agregarAlInicio("Z", "Art", 120);       // ID 4
        caso(ini, "agregarAlInicio(ID4)", "[4 2 1 3] actual=1", ids(l, true) + " actual=" + idDe(l.getActual()));
        caso(estado(l), "inicio y fin", "4 y 3", idDe(l.getPrimera()) + " y " + idDe(l.getUltima()));
        verificar(l, 4);
    }

    private static void p05RecorridosCincoCanciones() {
        titulo("P05 - Recorridos con cinco canciones");
        ListaReproduccion l = listaDe(5);
        String ini = estado(l);
        caso(ini, "recorrerAdelante()", "[1 2 3 4 5]", ids(l, true));
        caso(ini, "recorrerAtras()", "[5 4 3 2 1]", ids(l, false));
        caso(ini, "recorrido no modifica la lista", ini, estado(l));
        verificar(l, 5);
    }

    private static void p06AvanceDeUltimaAPrimera() {
        titulo("P06 - Avance desde la última canción hacia la primera");
        ListaReproduccion l = listaDe(5);
        l.seleccionar(5);
        String ini = estado(l);
        caso(ini, "avanzar()", "1", idDe(l.avanzar()));
        verificar(l, 5);
    }

    private static void p07RetrocesoDePrimeraAUltima() {
        titulo("P07 - Retroceso desde la primera canción hacia la última");
        ListaReproduccion l = listaDe(5);
        String ini = estado(l); // actual = 1
        caso(ini, "retroceder()", "5", idDe(l.retroceder()));
        caso(estado(l), "avanzar() x5 (vuelta completa)", "5", idDe(avanzarN(l, 5)));
        verificar(l, 5);
    }

    private static void p08EliminarPrimero() {
        titulo("P08 - Eliminación del primer nodo");
        ListaReproduccion l = listaDe(5);
        l.seleccionar(3);
        String ini = estado(l);
        Cancion e = l.eliminarPorId(1);
        caso(ini, "eliminarPorId(1)", "eliminada=1 [2 3 4 5] actual=3",
                "eliminada=" + idDe(e) + " " + ids(l, true) + " actual=" + idDe(l.getActual()));
        caso(estado(l), "nuevo inicio / fin.siguiente", "2 / 2",
                idDe(l.getPrimera()) + " / " + idDeSiguienteDelUltimo(l));
        verificar(l, 4);
    }

    private static void p09EliminarUltimo() {
        titulo("P09 - Eliminación del último nodo");
        ListaReproduccion l = listaDe(5);
        l.seleccionar(3);
        String ini = estado(l);
        Cancion e = l.eliminarPorId(5);
        caso(ini, "eliminarPorId(5)", "eliminada=5 [1 2 3 4] actual=3",
                "eliminada=" + idDe(e) + " " + ids(l, true) + " actual=" + idDe(l.getActual()));
        caso(estado(l), "nuevo fin / inicio.anterior", "4 / 4",
                idDe(l.getUltima()) + " / " + idDeAnteriorDelPrimero(l));
        verificar(l, 4);
    }

    private static void p10EliminarIntermedio() {
        titulo("P10 - Eliminación de un nodo intermedio (distinto de la actual)");
        ListaReproduccion l = listaDe(5);
        l.seleccionar(4);
        String ini = estado(l);
        Cancion e = l.eliminarPorId(3);
        caso(ini, "eliminarPorId(3)", "eliminada=3 [1 2 4 5] actual=4",
                "eliminada=" + idDe(e) + " " + ids(l, true) + " actual=" + idDe(l.getActual()));
        caso(estado(l), "recorrerAtras()", "[5 4 2 1]", ids(l, false));
        verificar(l, 4);
    }

    private static void p11EliminarActual() {
        titulo("P11 - Eliminación de la canción actual");
        ListaReproduccion l = listaDe(5);
        l.seleccionar(3);
        String ini = estado(l);
        Cancion e = l.eliminarActual();
        caso(ini, "eliminarActual() (intermedia)", "eliminada=3 [1 2 4 5] actual=4",
                "eliminada=" + idDe(e) + " " + ids(l, true) + " actual=" + idDe(l.getActual()));
        verificar(l, 4);

        l.seleccionar(5);
        ini = estado(l);
        e = l.eliminarActual();
        caso(ini, "eliminarActual() (era la última)", "eliminada=5 [1 2 4] actual=1 fin=4",
                "eliminada=" + idDe(e) + " " + ids(l, true) + " actual=" + idDe(l.getActual())
                        + " fin=" + idDe(l.getUltima()));
        verificar(l, 3);

        ini = estado(l); // actual = 1 = inicio
        e = l.eliminarActual();
        caso(ini, "eliminarActual() (era la primera)", "eliminada=1 [2 4] actual=2 inicio=2",
                "eliminada=" + idDe(e) + " " + ids(l, true) + " actual=" + idDe(l.getActual())
                        + " inicio=" + idDe(l.getPrimera()));
        verificar(l, 2);
    }

    private static void p12EliminarUnicaCancion() {
        titulo("P12 - Eliminación de la única canción");
        ListaReproduccion l = new ListaReproduccion();
        l.agregarAlFinal("Única", "Art", 90);
        String ini = estado(l);
        Cancion e = l.eliminarActual();
        caso(ini, "eliminarActual()", "eliminada=1, inicio/fin/actual=null",
                "eliminada=" + idDe(e) + ", inicio/fin/actual="
                        + (l.getPrimera() == null && l.getUltima() == null && l.getActual() == null ? "null" : "NO null"));
        verificar(l, 0);

        l.agregarAlFinal("Otra", "Art", 90); // ID 2
        ini = estado(l);
        e = l.eliminarPorId(2);
        caso(ini, "eliminarPorId(2) (única)", "eliminada=2 []", "eliminada=" + idDe(e) + " " + ids(l, true));
        verificar(l, 0);
    }

    private static void p13IdInexistente() {
        titulo("P13 - Búsqueda y eliminación de un ID inexistente");
        ListaReproduccion l = listaDe(5);
        l.seleccionar(2);
        String ini = estado(l);
        caso(ini, "buscar(99)", "null", "" + l.buscar(99));
        caso(ini, "eliminarPorId(99)", "null", "" + l.eliminarPorId(99));
        caso(ini, "seleccionar(99)", "false", "" + l.seleccionar(99));
        caso(ini, "estado tras las operaciones", ini, estado(l));
        verificar(l, 5);
    }

    private static void p14ReproduccionKMayorQueN() {
        titulo("P14 - Reproducción simulada con k mayor que la cantidad de canciones");
        ListaReproduccion l = listaDe(3);
        String ini = estado(l);
        StringBuilder reproducidas = new StringBuilder();
        int r = l.reproducir(7, c -> reproducidas.append(c.getId()).append(' '));
        caso(ini, "reproducir(7)", "1 2 3 1 2 3 1 (7)", reproducidas.toString().trim() + " (" + r + ")");
        caso(estado(l), "actual al terminar", "2", idDe(l.getActual()));
        verificar(l, 3);
    }

    private static void p15SeleccionarPorId() {
        titulo("P15 - Seleccionar por ID");
        ListaReproduccion l = listaDe(5);
        String ini = estado(l);
        caso(ini, "seleccionar(4)", "true actual=4", l.seleccionar(4) + " actual=" + idDe(l.getActual()));
        caso(estado(l), "buscar(2) no cambia la actual", "2 actual=4",
                idDe(l.buscar(2)) + " actual=" + idDe(l.getActual()));
        verificar(l, 5);
    }

    private static void p16EntradasInvalidasEnLista() {
        titulo("P16 - Entradas inválidas en la lista (no la modifican)");
        ListaReproduccion l = listaDe(2);
        String ini = estado(l);
        caso(ini, "agregarAlFinal(duración=0)", "excepción", lanzaExcepcion(() -> l.agregarAlFinal("X", "Y", 0)));
        caso(ini, "agregarAlInicio(duración=-5)", "excepción", lanzaExcepcion(() -> l.agregarAlInicio("X", "Y", -5)));
        caso(ini, "agregarAlFinal(título vacío)", "excepción", lanzaExcepcion(() -> l.agregarAlFinal("  ", "Y", 10)));
        caso(ini, "agregarAlFinal(artista null)", "excepción", lanzaExcepcion(() -> l.agregarAlFinal("X", null, 10)));
        caso(ini, "reproducir(0)", "excepción", lanzaExcepcion(() -> l.reproducir(0, c -> { })));
        caso(ini, "reproducir(-2)", "excepción", lanzaExcepcion(() -> l.reproducir(-2, c -> { })));
        caso(ini, "estado tras entradas inválidas", ini, estado(l));
        Cancion c = l.agregarAlFinal("Válida", "Art", 60);
        caso(ini, "siguiente inserción válida", "ID=3 (no se consumieron IDs)", "ID=" + c.getId() + " (no se consumieron IDs)");
        verificar(l, 3);
    }

    private static void p17EntradasInvalidasEnMenu() {
        titulo("P17 - Entradas inválidas en el menú (simulación de teclado)");
        caso("-", "interpretarEntero(\"abc\")", "null", "" + Menu.interpretarEntero("abc"));
        caso("-", "interpretarEntero(\" 12 \")", "12", "" + Menu.interpretarEntero(" 12 "));
        caso("-", "interpretarEntero(\"3.5\")", "null", "" + Menu.interpretarEntero("3.5"));

        // Secuencia: opción con letras, opción fuera de rango, operaciones sobre lista vacía,
        // agregar con título vacío y duración inválida, ID no numérico, k inválido y salir.
        String teclado = String.join("\n",
                "abc",          // opción no numérica
                "99",           // opción fuera de rango
                "7",            // consultar actual con lista vacía
                "6",            // eliminar actual con lista vacía
                "12",           // reproducir con lista vacía
                "2", "", "Song", "Artist", "cero", "-4", "0", "200", // agregar al final con errores
                "3", "x", "55", // buscar: ID no numérico y luego inexistente
                "12", "0", "-1", "3", // reproducir: k inválidos y luego k=3
                "11",
                "13") + "\n";
        String salida = ejecutarMenu(teclado);

        caso("lista vacía", "opción \"abc\"", "mensaje de entero inválido",
                contiene(salida, "Entrada inválida: escribe un número entero.", "mensaje de entero inválido"));
        caso("lista vacía", "opción 99", "mensaje de opción inválida",
                contiene(salida, "Opción inválida", "mensaje de opción inválida"));
        caso("lista vacía", "opciones 7, 6, 12", "aviso de lista vacía",
                contiene(salida, "La lista está vacía", "aviso de lista vacía"));
        caso("lista vacía", "título vacío", "mensaje de texto vacío",
                contiene(salida, "el texto no puede estar vacío", "mensaje de texto vacío"));
        caso("lista vacía", "duración -4 y 0", "mensaje de número > 0",
                contiene(salida, "debe ser mayor que cero", "mensaje de número > 0"));
        caso("[1]", "buscar ID 55", "aviso de ID inexistente",
                contiene(salida, "No existe una canción con ID 55", "aviso de ID inexistente"));
        caso("[1]", "reproducir k=3", "3 reproducciones",
                contarApariciones(salida, "Reproduciendo:") + " reproducciones");
        caso("[1]", "consultar cantidad", "Cantidad de canciones: 1",
                contiene(salida, "Cantidad de canciones: 1", "Cantidad de canciones: 1"));
        caso("-", "salir (opción 13)", "termina sin errores",
                contiene(salida, "Hasta luego.", "termina sin errores"));

        String salidaEof = ejecutarMenu("2\nTítulo"); // la entrada se corta a mitad de una operación
        caso("lista vacía", "fin de entrada inesperado", "termina sin errores",
                contiene(salidaEof, "Hasta luego.", "termina sin errores"));
    }

    private static void p18IdsUnicosIncrementales() {
        titulo("P18 - IDs únicos e incrementales (no se reutilizan tras eliminar)");
        ListaReproduccion l = listaDe(3);
        l.eliminarPorId(3);
        String ini = estado(l);
        Cancion c = l.agregarAlFinal("Nueva", "Art", 100);
        caso(ini, "agregarAlFinal tras eliminar ID 3", "ID=4", "ID=" + c.getId());
        verificar(l, 3);
    }

    // ==================================================================
    // Utilidades
    // ==================================================================

    private static void titulo(String t) {
        System.out.println();
        System.out.println("---- " + t + " ----");
    }

    /** Registra un caso: estado inicial, operación, esperado y obtenido. */
    private static void caso(String estadoInicial, String operacion, String esperado, String obtenido) {
        total++;
        boolean ok = esperado.equals(obtenido);
        if (!ok) fallidas++;
        System.out.println((ok ? "[OK]    " : "[FALLA] ") + "Inicial: " + estadoInicial);
        System.out.println("        Operación: " + operacion);
        System.out.println("        Esperado:  " + esperado);
        System.out.println("        Obtenido:  " + obtenido);
    }

    /** Comprueba la cantidad de elementos y la consistencia de los enlaces. */
    private static void verificar(ListaReproduccion l, int tamanioEsperado) {
        caso(estado(l), "verificar tamaño y enlaces",
                "tamaño=" + tamanioEsperado + ", enlaces consistentes",
                "tamaño=" + l.getTamanio() + ", "
                        + (l.verificarConsistencia() ? "enlaces consistentes" : "ENLACES INCONSISTENTES"));
    }

    /** Lista con n canciones agregadas al final: IDs 1..n, actual = 1. */
    private static ListaReproduccion listaDe(int n) {
        ListaReproduccion l = new ListaReproduccion();
        for (int i = 1; i <= n; i++) {
            l.agregarAlFinal("Canción " + i, "Artista " + i, 60 * i);
        }
        return l;
    }

    private static String ids(ListaReproduccion l, boolean adelante) {
        StringBuilder sb = new StringBuilder();
        if (adelante) {
            l.recorrerAdelante(c -> sb.append(sb.length() == 0 ? "" : " ").append(c.getId()));
        } else {
            l.recorrerAtras(c -> sb.append(sb.length() == 0 ? "" : " ").append(c.getId()));
        }
        return "[" + sb + "]";
    }

    private static String estado(ListaReproduccion l) {
        return ids(l, true) + " actual=" + idDe(l.getActual()) + " n=" + l.getTamanio();
    }

    private static String idDe(Cancion c) {
        return c == null ? "null" : String.valueOf(c.getId());
    }

    private static Cancion avanzarN(ListaReproduccion l, int veces) {
        Cancion c = l.getActual();
        for (int i = 0; i < veces; i++) c = l.avanzar();
        return c;
    }

    /** Usa solo la API pública: seleccionar el último y avanzar, luego restaurar. */
    private static String idDeSiguienteDelUltimo(ListaReproduccion l) {
        Cancion antes = l.getActual();
        l.seleccionar(l.getUltima().getId());
        String r = idDe(l.avanzar());
        l.seleccionar(antes.getId());
        return r;
    }

    private static String idDeAnteriorDelPrimero(ListaReproduccion l) {
        Cancion antes = l.getActual();
        l.seleccionar(l.getPrimera().getId());
        String r = idDe(l.retroceder());
        l.seleccionar(antes.getId());
        return r;
    }

    private static String lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return "sin excepción";
        } catch (IllegalArgumentException e) {
            return "excepción";
        }
    }

    private static String ejecutarMenu(String teclado) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(buffer, true);
        new Menu(new Scanner(teclado), ps).ejecutar();
        return buffer.toString();
    }

    private static String contiene(String texto, String buscado, String siSi) {
        return texto.contains(buscado) ? siSi : "no apareció \"" + buscado + "\"";
    }

    private static int contarApariciones(String texto, String buscado) {
        int cuenta = 0;
        int i = texto.indexOf(buscado);
        while (i >= 0) {
            cuenta++;
            i = texto.indexOf(buscado, i + 1);
        }
        return cuenta;
    }
}
