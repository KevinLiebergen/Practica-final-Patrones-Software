package publicaciones_builder;



import java.io.Serializable;
import java.util.Date;

/**
 * Esta clase gestiona una estructura de publicaciones prestadas,
 * de tal modo que guarda caracteristicas propias de cada prestamo,
 * como nif, isbn, dias permitidos para coger el libro, la publicacion,
 * y la fecha de prestamo y de devolucion
 *
 * @author kevin van Liebergen
 */

public class Prestamo implements Serializable {


    private String nif;
    private String isbn;
    private int diasPermitidos;
    private PublicacionBuilder publicacionBuilder;
    private Date fechaPrestamo;
    private Date fechaDevolucion;

    public Prestamo(String nif, String isbn, int diasPermitidos, PublicacionBuilder publicacionBuilder, Date fechaPrestamo, Date fechaDevolucion){
        this.nif = nif;
        this.isbn = isbn;
        this.diasPermitidos = diasPermitidos;
        this.publicacionBuilder = publicacionBuilder;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getDiasPermitidos() {
        return diasPermitidos;
    }

    public void setDiasPermitidos(int diasPermitidos) {
        this.diasPermitidos = diasPermitidos;
    }

    public PublicacionBuilder getPublicacionBuilder() {
        return publicacionBuilder;
    }

    public void setPublicacionBuilder(PublicacionBuilder publicacionBuilder) {
        this.publicacionBuilder = publicacionBuilder;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    @Override
    public String toString() {
        return "\nISBN: "+isbn+" NIF del usuario: "+nif+" fecha del prestamo: "+fechaPrestamo+"\n";
    }
}
