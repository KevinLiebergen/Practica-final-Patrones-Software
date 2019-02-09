package publicaciones_builder.EstadoPublicacion_State;


import java.io.Serializable;

/**
 * Estado NoPrestado
 *
 * @author kevin van Liebergen
 */

public class NoPrestado implements IEstado, Serializable {

    private String es_prestado = "Publicacion no prestada";

    @Override
    public String ejecutarAccion() {
        return es_prestado;
    }
}