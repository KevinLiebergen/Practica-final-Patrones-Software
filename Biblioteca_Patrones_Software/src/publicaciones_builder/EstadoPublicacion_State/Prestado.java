package publicaciones_builder.EstadoPublicacion_State;


import java.io.Serializable;

/**
 * Estado Prestado
 *
 * @author kevin van Liebergen
 */

public class Prestado implements  IEstado, Serializable {

    private String es_prestado = "Publicacion prestada";

    @Override
    public String ejecutarAccion() {
        return es_prestado;
    }
}
