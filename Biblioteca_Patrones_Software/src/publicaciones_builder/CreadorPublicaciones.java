package publicaciones_builder;

import excepciones.BibliotecaException;
import gestionPublicsUsurs_singleton.GestionPublicaciones;
import java.util.Date;

/**
 * Clase "director", se encargara de crear publicaciones
 * siguiendo el patron builder.
 *
 * @author kevin van Liebergen
 */

public class CreadorPublicaciones {

    /**
     * Clase abstracta para construir el producto
     */
    private PublicacionBuilder publicacionBuilder;



    /**
     * Establece el constructor concreto
     *
     * @param pb Constructor concreto
     */
    public void setPublicacionBuilder(PublicacionBuilder pb){
        publicacionBuilder = pb;
    }

    /**
     * Devuelve la publicacion compleja creada
     *
     * @return producto Publicacion
     */
    public Publicacion getPublicacion(){
        return publicacionBuilder.getPublicacion();
    }

    /**
     * Se pasa por parametro instancia de publicacion
      * @param publicacion tipo Publicacion
     */
    public void crearPublicacion(Publicacion publicacion){

        publicacionBuilder.crearNuevaPublicacion();

        publicacionBuilder.crearPublicacionBasica(publicacion);

        publicacionBuilder.anadirCaracteristicas();

        GestionPublicaciones.anadirPublicacionModificada(publicacionBuilder);
    }

    /**
     * Crea la publicacion paso a paso
     */
    public void crearPublicacion(){

        publicacionBuilder.crearNuevaPublicacion();

        System.out.println("\n***** Se rellenan los componentes basicos de la publicacion ******\n");
        try {
            publicacionBuilder.crearPublicacionBasica();
        } catch (BibliotecaException e) {
            System.out.println("Error formato fecha");
        }

        System.out.println("\n****** Se rellenan las caracteristicas concretas de la publicacion ******\n");
        publicacionBuilder.anadirCaracteristicas();

        // Se anade al HashMap de publicaciones
        GestionPublicaciones.anadirPublicacion(publicacionBuilder);
    }
    
    public void crearPublicacion(String isbn, String titulo, String autores, Date date, String materia, String espec1, String espec2, String espec3) {
        
        publicacionBuilder.crearNuevaPublicacion();        
        publicacionBuilder.crearPublicacionBasica(isbn, titulo, autores, date, materia);        
        publicacionBuilder.anadirCaracteristicas(espec1, espec2, espec3);
        
        GestionPublicaciones.anadirPublicacion(publicacionBuilder);        
    }

    public void modificarPublicacion(String isbn, String titulo, String autores, Date date, String materia, String espec1, String espec2, String espec3) {
        
        publicacionBuilder.crearNuevaPublicacion();      
        publicacionBuilder.crearPublicacionBasica(isbn, titulo, autores, date, materia);     
        System.out.println("En el metodo modificar publicacion espec 2 vale: "+espec2);
        publicacionBuilder.anadirCaracteristicas(espec1, espec2, espec3);
        
        GestionPublicaciones.anadirPublicacionModificada(publicacionBuilder);        
    }
    

}
