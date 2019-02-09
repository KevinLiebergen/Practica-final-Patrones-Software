package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import publicaciones_builder.*;

/**
 * Clase que gestiona el menú de publicaciones
 * @author kevin van Liebergen
 */

public class MenuCrearPublicacion implements IMenu {



    @Override
    public void mostrarOpciones() {
        System.out.println("0.- Volver un menu hacia atras");
        System.out.println("1.- Crear libro");
        System.out.println("2.- Crear revista");
        System.out.println("3.- Crear proyecto");
    }

    /**
     * Depende de la opcion elegida se crea libro, revista o proyecto.
     * Se crea mediante el patron Builder, se establece el constructor
     * concreto LibroBuilder, RevistaBuilder y ProyectoBuilder, se asigna
     * al creador y se crea la publicacion
     */
    @Override
    public void ejecutarOpciones() {
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        CreadorPublicaciones creador = new CreadorPublicaciones();
        int seleccion;


        do {
            Publicacion publicacion = new Publicacion();

            mostrarOpciones();

            //Para no introducir letras en lugar de numeros
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion) {

                case 0:
                    System.out.println("Vuelvo hacia atrás");
                    break;
                case 1:
                    LibroBuilder lb = new LibroBuilder(publicacion);
                    //Establece el constructor concreto
                    creador.setPublicacionBuilder(lb);

                    //Construye la publicacion paso a paso
                    creador.crearPublicacion();
                    break;
                case 2:
                    RevistaBuilder rb = new RevistaBuilder(publicacion);
                    //Crear revista
                    creador.setPublicacionBuilder(rb);

                    //Construye la publicacion paso a paso
                    creador.crearPublicacion();
                    break;
                case 3:
                    ProyectoBuilder pb  = new ProyectoBuilder(publicacion);
                    //Crear proyecto
                    creador.setPublicacionBuilder(pb);

                    //Construye la publicacion paso a paso
                    creador.crearPublicacion();
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

        }while(seleccion!=0);

    }



}
