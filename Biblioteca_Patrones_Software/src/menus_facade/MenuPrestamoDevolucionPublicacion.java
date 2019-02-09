package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import gestionPublicsUsurs_singleton.GestionPublicaciones;
import gestionPublicsUsurs_singleton.GestionUsuarios;
import usuarios_factoryMethod.Alumno;
import usuarios_factoryMethod.Profesor;


/**
 * @author kevin van Liebergen
 */

public class MenuPrestamoDevolucionPublicacion implements IMenu {

    private AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

    @Override
    public void mostrarOpciones() {
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Préstamo de publicación");
        System.out.println("2.- Devolución de publicación");
        System.out.println("3.- Mostrar publicaciones prestadas");
    }

    @Override
    public void ejecutarOpciones(){
        int seleccion;

        do{
            mostrarOpciones();
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion){
                case 0:
                    System.out.println("\nVuelvo hacia atras\n");
                    break;
                case 1:
                    menuPrestarPublicacion();
                    break;
                case 2:
                    menuDevolverPublicacion();
                    break;
                case 3:
                    menuMostrarPublicacionesPrestadas();
                    break;
                default:
                    System.out.println("\nOpción no válida\n");
            }
        }while (seleccion!=0);
    }

    /**
     * Se pide NIF e ISBN, si la publicacion esta prestada se avisa
     * cuando se devuelva, si no se presta x dias dependiendo de si
     * el usuario es alumno o profesor y del tipo de publicacion que sea
     */
    private void menuPrestarPublicacion(){
        String nif,isbn, tipoUsuario;
        int dias;

        System.out.println("Inserte el NIF del usuario: ");
        nif = asegurar.asegurarNIFExisteEnHashMap();


        System.out.println("Inserte el ISBN de la publicacion a prestar: ");
        isbn = asegurar.asegurarISBNExisteEnHashMap();

        if(asegurar.siEstaPrestadoAvisar(isbn)){
            //SE ANADEN LOS OBSERVADORES A AVISAR CON LA DETERMINADA PUBLICACION
            GestionPublicaciones.anadirObservadorAPublicacion(nif, isbn);

        }else{
            dias = GestionPublicaciones.prestarPublicacion(nif,isbn);

            if(GestionUsuarios.getUsuarios().get(nif) instanceof Profesor){
                tipoUsuario = ((Profesor) GestionUsuarios.getUsuarios().get(nif)).getTipo();
            }else{
                tipoUsuario = ((Alumno) GestionUsuarios.getUsuarios().get(nif)).getTipo();
            }

            //print que indica cuantos dias se presta y a que tipo de usuario (alumno o profesor)
            System.out.println("\nLos días permitidos para prestar la publicación al usuario: "+tipoUsuario+
                    ", con NIF "+GestionUsuarios.getUsuarios().get(nif).getNif()+ " y nombre "+
                    GestionUsuarios.getUsuarios().get(nif).getNombreCompleto()+" con la publicacion " +
                    GestionPublicaciones.getPublicaciones().get(isbn).getPublicacion().getTitulo()+" son" +
                    " de "+dias+" dias.\n");
        }
    }

    /**
     * Se pide ISBN, se comprueba que la publicacion existe y ha sido prestada,
     * Si ha sido entregada fuera de plazo se castiga con dias de sancion, los
     * mismos que los dias que ha entregado la publicacion fuera de plazo.
     *
     * Se avisan a los usuarios que esperan que quieren esa publicacion que la
     * publicacion ha sido devuelta, despues de avisar se borran los observadores
     * de la publicacion
     */
    private void menuDevolverPublicacion(){
        String isbn;
        int diasSancion;

        System.out.println("Inserte el ISBN de la publicacion a devolver: ");
        isbn = asegurar.asegurarISBNExisteEnHashMapYSiPrestado();

        diasSancion = GestionPublicaciones.devolucionPublicacion(isbn);

        if(diasSancion<=0){
            System.out.println("\nDevolución de la publicación correcta y entregada dentro del plazo\n");
        }else{
            System.out.println("\nDevolución fuera de plazo indicado, dias de sanción: "+diasSancion);
        }

        //Avisamos a los usuarios que lo necesiten que el libro esta devuelto
        GestionPublicaciones.avisarAObservadoresDePublicacionConcreta(isbn);

        //Eliminamos los observadores de la lista a avisar
        GestionPublicaciones.eliminarObservadoresDePublicacion(isbn);

    }

    /**
     * Se muestra una lista con todas las publicaciones que han sido prestadas
     */
    private void menuMostrarPublicacionesPrestadas(){
        if(GestionPublicaciones.getPublicacionesPrestadas().isEmpty()){
            System.out.println("\nActualmente no hay publicaciones prestadas\n");
        }else{
            System.out.println(GestionPublicaciones.getPublicacionesPrestadas());
        }
    }
}
