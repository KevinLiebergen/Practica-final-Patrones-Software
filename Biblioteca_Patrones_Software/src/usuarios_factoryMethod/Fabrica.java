package usuarios_factoryMethod;

/**
 * Creador: clase para crear distintos usuarios.
 * Se encargara de crear los objetos de usuario
 * dependiendo de lo que eliga el cliente
 *
 * @author kevin van Liebergen
 */

public class Fabrica {

    protected String tipo;

    public Fabrica(String categoria){
        tipo = categoria;
    }

    /**
     * Operador ternario (para realizar if-else inline)
     * para crear un objeto de tipo alumno o profesor
     *
     * @return Objeto de tipo alumno o profesor
     */
    public Usuario creaUsuario(){
        Usuario usuario;

        usuario = (tipo.equalsIgnoreCase("alumno")) ? new Alumno() : new Profesor();
        return usuario;
    }
}
