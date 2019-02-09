package publicaciones_builder;

import publicaciones_builder.EstadoPublicacion_State.IEstado;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa el producto complejo a crear: una publicacion
 *
 * @author kevin van Liebergen
 */


public class Publicacion implements Serializable {

    /**
     * Caracteristicas basicas de la publicacion
     * antes de especializarse
     */
    private String isbn;
    private String titulo;
    private String autores;
    private Date fechaPublicacion;
    private String materia;

    //Estado prestado - noPrestado patron State
    private IEstado iEstado;


    /**
     * Constructor por defecto
     */
    public Publicacion() {

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public IEstado getiEstado() {
        return iEstado;
    }

    public void setiEstado(IEstado iEstado){
        this.iEstado = iEstado;
    }

    /**
     * Devuelve una cadena con todas las caracteristicas de la publicacion
     * @return Cadena que describe la publicacion
     */
    @Override
    public String toString() {
        return "Publicacion{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autores='" + autores + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", materia='" + materia + '\'' +
                ", prestado='" +iEstado.ejecutarAccion()+ '\''+
                '}';
    }
}
