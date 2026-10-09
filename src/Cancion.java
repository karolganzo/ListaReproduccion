/**
 * Representa una canción de la lista de reproducción.
 * Es inmutable: una vez creada, sus datos no cambian.
 * Solo guarda datos; no sabe nada de nodos ni de enlaces.
 */
public class Cancion {

    private final int id;
    private final String titulo;
    private final String artista;
    private final int duracion; // en segundos, siempre > 0

    public Cancion(int id, String titulo, String artista, int duracion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista no puede estar vacío.");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public int getDuracion() {
        return duracion;
    }

    /** Duración en formato m:ss, por ejemplo 215 -> "3:35". */
    public String duracionFormateada() {
        return String.format("%d:%02d", duracion / 60, duracion % 60);
    }

    @Override
    public String toString() {
        return "[ID " + id + "] " + titulo + " - " + artista
                + " (" + duracionFormateada() + ", " + duracion + " s)";
    }
}
