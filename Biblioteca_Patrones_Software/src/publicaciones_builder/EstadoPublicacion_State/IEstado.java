package publicaciones_builder.EstadoPublicacion_State;


/**
 * Interface para gestionar los estados
 * de la publicacion (Prestado - NoPrestado)
 *
 * @author kevin van Liebergen
 */

public interface IEstado {

    /**
     * Modifica el estado del objeto publicacion
     *
     * @return String
     */
     String ejecutarAccion();
}
