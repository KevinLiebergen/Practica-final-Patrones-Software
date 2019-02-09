package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import gestionPublicsUsurs_singleton.GestionPublicaciones;

import java.util.Scanner;

/**
 * Clase que gestiona el menu de borrar publicacion,
 * con 2 opciones
 * @author kevin van Liebergen
 */
public class MenuBorrarPublicacion implements IMenu {


    /**
     * Opciones generales del menu BorrarPublicacion
     */
    @Override
    public void mostrarOpciones() {
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Borrar publicación");
    }

    /**
     * En caso de elegir borrar se debe ingresar el isbn
     * de la publicacion a borrar, dentro del metodo borrarPublicacion
     * se comprueba si existe o no dicho isbn
     */
    @Override
    public void ejecutarOpciones() {
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        Scanner sc = new Scanner(System.in);
        int seleccion;
        String isbn;

        do{
            mostrarOpciones();
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion){
                case 0:
                    System.out.println("Vuelvo hacia atras");
                    break;
                case 1:
                    System.out.println("Inserte el ISBN de la publicación");
                    isbn= sc.nextLine();
                    GestionPublicaciones.borrarPublicacion(isbn);
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }while(seleccion!=0);
    }
}
