package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import gestionPublicsUsurs_singleton.GestionPublicaciones;

import java.util.Date;
import java.util.Scanner;

/**
 * Clase que gestiona el menu de busqueda de publicaciones,
 * permite buscar por autor, titulo, autor y titulo, materia,
 * fecha publicacion y materia y fecha de publicacion
 *
 * @author kevin van Liebergen
 */

public class MenuBusquedaPublicaciones implements IMenu {

    private AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

    @Override
    public void mostrarOpciones() {
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Realizar búsqueda por nombre de autor");
        System.out.println("2.- Realizar búsqueda por titulo");
        System.out.println("3.- Realizar búsqueda por nombre de autor y titulo");
        System.out.println("4.- Realizar búsqueda por materia");
        System.out.println("5.- Realizar búsqueda por fecha de publicación");
        System.out.println("6.- Realizar búsqueda por materia y fecha de publicación");
    }

    /**
     * Se pasa por parametro la opcion elegida del menu
     */
    @Override
    public void ejecutarOpciones(){
        Scanner sc = new Scanner(System.in);
        String autor, titulo, materia;
        Date fecha;
        int seleccion;

        do{
            mostrarOpciones();
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion){
                case 0:
                    System.out.println("\nVuelvo hacia atrás\n");
                    break;
                case 1:
                    //NOMBRE
                    System.out.println("Nombre del autor a buscar: ");
                    autor = sc.nextLine();
                    GestionPublicaciones.busquedaPublicaciones(1, autor);
                    break;
                case 2:
                    //TITULO
                    System.out.println("Nombre del titulo a buscar: ");
                    titulo = sc.nextLine();
                    GestionPublicaciones.busquedaPublicaciones(2, titulo);
                    break;
                case 3:
                    //AUTOR - TITULO
                    System.out.println("Nombre del autor: ");
                    autor = sc.nextLine();

                    System.out.println("Nombre del título: ");
                    titulo = sc.nextLine();

                    GestionPublicaciones.busquedaPublicaciones(3, autor, titulo);
                    break;
                case 4:
                    //MATERIA
                    System.out.println("Nombre de la materia a buscar: ");
                    materia = sc.nextLine();
                    GestionPublicaciones.busquedaPublicaciones(4, materia);
                    break;
                case 5:
                    //FECHA PUBLICACION
                    System.out.println("Fecha de publicacion a buscar (dd/MM/yyyy): ");
                    fecha = asegurar.asegurarFormatoFechaCorrecta();
                    GestionPublicaciones.busquedaPublicaciones(5, fecha);
                    break;
                case 6:
                    // FECHA - MATERIA
                    System.out.println("Nombre de la materia a buscar: ");
                    materia = sc.nextLine();

                    System.out.println("Fecha de publicacion: ");
                    fecha = asegurar.asegurarFormatoFechaCorrecta();
                    GestionPublicaciones.busquedaPublicaciones(6, fecha, materia );
                    break;
                default:
                    System.out.println("\nOpción no válida\n");
            }

        } while(seleccion!=0);
    }
}
