package usuarios_factoryMethod;


/**
 * @author kevin van Liebergen
 */

public class Alumno extends Usuario{
    private String tipo;

    public Alumno(){
        tipo = "alumno";
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
