package usuarios_factoryMethod;

/**
 * @author kevin van Liebergen
 */

public class Profesor extends Usuario {
    private String tipo;

    public Profesor(){
        tipo = "profesor";
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "\nNIF: "+getNif()+
                ", nombre completo: "+getNombreCompleto()+
                ",\ncategoria: "+tipo+
                ", fecha de inscripcion: "+getFechaInscripcion()+"\n";
    }
}
