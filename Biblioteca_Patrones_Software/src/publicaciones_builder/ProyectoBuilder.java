package publicaciones_builder;

import excepciones.AsegurarOpcionCorrecta;
import java.util.Scanner;

/**
 * Constructor concreto
 *
 * @author kevin van Liebergen
 */

public class ProyectoBuilder extends PublicacionBuilder {

    private Publicacion publicacion;
    private String tribunal;
    private String titulacion;
    private int calificacion;

    public ProyectoBuilder(Publicacion publicacion){
        super(publicacion);
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public String getTribunal() {
        return tribunal;
    }

    public void setTribunal(String tribunal) {
        this.tribunal = tribunal;
    }

    public String getTitulacion() {
        return titulacion;
    }

    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    /**
     * Anade las caracteristicas concretas de la publicacion proyecto
     */
    public void anadirCaracteristicas(){
        Scanner sc = new Scanner(System.in);
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

        System.out.println("Nombre del tribunal: ");
        tribunal = sc.nextLine();
        setTribunal(tribunal);

        System.out.println("Nombre de la titulacion: ");
        titulacion = sc.nextLine();
        setTitulacion(titulacion);

        System.out.println("Numero de la calificacion: ");
        calificacion = asegurar.asegurarRespuestaSeaNumero();
        setCalificacion(calificacion);
    }
    
    @Override
    public void anadirCaracteristicas(String tribunal, String titulacion, String calificacion){
        setTribunal(tribunal);
        setTitulacion(titulacion);
        setCalificacion(Integer.valueOf(calificacion));
    }



    public String toString(){
        return "\nISBN: "+this.getPublicacion().getIsbn()+
                ", título: "+this.getPublicacion().getTitulo()+
                ", autor/es: "+this.getPublicacion().getAutores()+
                ",\nfecha publicacion: "+this.getPublicacion().getFechaPublicacion()+
                ", materia publicacion: "+ this.getPublicacion().getMateria()+
                ",\ntipo: Proyecto, tribunal: "+getTribunal()+
                ", titulacion: "+getTitulacion()+
                ", calificacion: "+getCalificacion()+
                ", prestado: "+getPublicacion().getiEstado().ejecutarAccion()+"\n";

    }

}
