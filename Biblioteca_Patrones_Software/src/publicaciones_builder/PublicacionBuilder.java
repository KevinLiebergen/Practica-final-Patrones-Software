package publicaciones_builder;

import excepciones.AsegurarOpcionCorrecta;
import excepciones.BibliotecaException;
import gestionPublicsUsurs_singleton.GestionUsuarios;
import publicaciones_builder.EstadoPublicacion_State.NoPrestado;
import usuarios_factoryMethod.avisarPublicacionDevuelta_observer.Sujeto;

import java.io.Serializable;
import java.util.Date;
import java.util.Scanner;

/**
 * clase abstracta que especifica los métodos para crear
 * las partes del producto concreto
 *
 * @author kevin van Liebergen
 */

public abstract class PublicacionBuilder implements Serializable {

    /**
     * Producto complejo a crear: publicacion
     */
    private Publicacion publicacion;

    /**
     * Para patron observer
     */
    private Sujeto sujeto;


    public PublicacionBuilder(Publicacion publicacion) {

        this.publicacion = publicacion;
        sujeto = new Sujeto();
    }

    /**
     * Devuelve el producto complejo que se crea
     * @return producto publicacion
     */
    public Publicacion getPublicacion(){ return publicacion;}

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    /**
     * Crea un nuevo producto del tipo complejo publicacion
     */
    public void crearNuevaPublicacion(){
        publicacion = new Publicacion();
    }

    /**
     * Se rellenan caracteristicas basicas de la publicacion con
     * la publicacion pasada por parametro, sirve para cambiar de tipo
     * la publicacion, se crea una nueva y se setean las caracteristicas
     * Patron prototype
     */
    public void crearPublicacionBasica(Publicacion publicacion){

        publicacion.setTitulo(publicacion.getTitulo());
        publicacion.setIsbn(publicacion.getIsbn());
        publicacion.setAutores(publicacion.getAutores());
        publicacion.setFechaPublicacion(publicacion.getFechaPublicacion());

        setPublicacion(publicacion);

    }

    /**
     * Se rellenan caracteristicas basicas de la publicacion,
     * titulo, isbn,autores, materia y fecha de publicacion
     */
    public void crearPublicacionBasica() throws BibliotecaException {
        Scanner sc = new Scanner(System.in);
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        NoPrestado noPrestado = new NoPrestado();

        String titulo, isbn, autores, materia;
        Date fechaPublicacion;

        Publicacion publicacion = new Publicacion();

        System.out.println("Titulo de la publicacion: ");
        titulo = sc.nextLine();
        publicacion.setTitulo(titulo);

        System.out.println("ISBN de la publicacion: ");
        isbn = sc.nextLine();
        publicacion.setIsbn(isbn);

        System.out.println("Autor/es de la publicacion: ");
        autores = sc.nextLine();
        publicacion.setAutores(autores);

        System.out.println("Fecha de la publicacion(dd/MM/yyyy): ");

        fechaPublicacion = asegurar.asegurarFormatoFechaCorrecta();
        publicacion.setFechaPublicacion(fechaPublicacion);

        System.out.println("Materia de la publicacion: ");
        materia = sc.nextLine();
        publicacion.setMateria(materia);

        //Al crear la publicación estará en no prestado
        publicacion.setiEstado(noPrestado);


        setPublicacion(publicacion);
    }
    
    public void crearPublicacionBasica(String isbn, String titulo, String autores, Date date, String materia){
        NoPrestado noPrestado = new NoPrestado();

        publicacion.setIsbn(isbn);
        publicacion.setAutores(autores);
        publicacion.setTitulo(titulo);
        publicacion.setAutores(autores);
        publicacion.setFechaPublicacion(date);
        publicacion.setMateria(materia);
        
        publicacion.setiEstado(noPrestado);
        
        setPublicacion(publicacion);
    }

    /**
     * Metodos abstractos generales que se rellenan
     * en sus clases hijas
     */
    public abstract void anadirCaracteristicas();
    
    public abstract void anadirCaracteristicas(String espec1, String espec2, String espec3);

    public abstract String toString();


    /**
     * Para anadir observadores a esta publicacion,
     * de modo que cuando la publicacion se devuelva
     * se avise a los usuarios que esten esperando
     * @param nif
     */
    public void anadirObservador(String nif){
        sujeto.registrarObservador(GestionUsuarios.getUsuarios().get(nif));
        System.out.println("\nUsuario añadido a la lista para avisar\n");
    }

    /**
     * Se notifican a todos los observadores
     * que la publicacion esta disponible
     */
    public void avisarObservadores(){
        sujeto.notificar();
    }

    /**
     * Se eliminan los observadores cuando se les haya notificado
     */
    public void eliminarObservadores(){
        sujeto.retirarObservadores();
    }
}
