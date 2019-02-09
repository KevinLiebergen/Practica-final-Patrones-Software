package publicaciones_builder;

import java.util.Scanner;
import excepciones.AsegurarOpcionCorrecta;

/**
 * Constructor concreto
 * @author kevin van Liebergen
 */

public class LibroBuilder extends PublicacionBuilder {

    private Publicacion publicacion;
    private String editorial;
    private String localidad;
    private int edicion;


    public LibroBuilder(Publicacion publicacion){
        super(publicacion);
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }


    /**
     * Anade las caracteristicas concretas de la publicacion libro
     */
    @Override
    public void anadirCaracteristicas(){
        Scanner sc = new Scanner(System.in);
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

        System.out.println("Nombre de la editorial: ");
        editorial = sc.nextLine();
        setEditorial(editorial);

        System.out.println("Nombre de la localidad: ");
        localidad = sc.nextLine();
        setLocalidad(localidad);

        System.out.println("Numero de la edicion: ");
        edicion = asegurar.asegurarRespuestaSeaNumero();
        setEdicion(edicion);
    }
    
    @Override
    public void anadirCaracteristicas(String editorial, String localidad, String edicion){
    setEditorial(editorial);
    setLocalidad(localidad);
    setEdicion(Integer.valueOf(edicion));       
    }



    @Override
    public String toString(){

        return "\nISBN: "+publicacion.getIsbn()+
                ", título: "+this.getPublicacion().getTitulo()+
                ", autor/es: "+ this.getPublicacion().getAutores()+
                ",\nfecha publicacion: "+ this.getPublicacion().getFechaPublicacion()+
                ", materia publicacion: "+ this.getPublicacion().getMateria()+
                ",\ntipo: Libro, editorial: "+getEditorial()+
                ", localidad: "+getLocalidad()+
                ", edicion: "+getEdicion()+
                ", prestado: "+getPublicacion().getiEstado().ejecutarAccion()+"\n";
    }

}
