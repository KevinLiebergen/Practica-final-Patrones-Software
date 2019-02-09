package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import gestionPublicsUsurs_singleton.GestionUsuarios;

import java.util.Scanner;

/**
 * Clase que gestiona el menu de borrar usuario,
 * con 2 opciones
 *
 * @author kevin van Liebergen
 */

public class MenuBorrarUsuario implements  IMenu{


    @Override
    public void mostrarOpciones() {
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Borrar usuario");
    }

    @Override
    public void ejecutarOpciones() {
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        Scanner sc = new Scanner(System.in);
        int seleccion;
        String nif;

        do{
            mostrarOpciones();
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion){
                case 0:
                    System.out.println("Vuelvo hacia atras");
                    break;
                case 1:
                    System.out.println("Inserte el NIF del usuario");
                    nif= sc.nextLine();
                    GestionUsuarios.borrarUsuario(nif);
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }while(seleccion!=0);

    }
}
