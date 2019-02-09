package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import excepciones.BibliotecaException;
import usuarios_factoryMethod.Fabrica;
import usuarios_factoryMethod.Usuario;

/**
 * Clase que gestiona el menú de usuarios
 *
 * @author kevin van Liebergen
 */

public class MenuCrearUsuario implements IMenu {

    @Override
    public void mostrarOpciones() {
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Crear alumno");
        System.out.println("2.- Crear profesor");
    }

    /**
     * Mediante el patron Factory Method se crean los usuarios,
     * se crea un objeto Fabrica, que devuelve el tipo de usuario
     * a crear para mas tarde rellenar sus datos
     */
    @Override
    public void ejecutarOpciones() {
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        Usuario usuario;
        int seleccion;

        do{
            Fabrica miFabrica=null;
            mostrarOpciones();

            //Para no introducir letras en lugar de numeros
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion){
                case 0:
                    System.out.println("Vuelvo hacia atras");
                    break;
                case 1:
                    //CREO ALUMNO
                    miFabrica = new Fabrica("alumno");
                    break;
                case 2:
                    //CREO PROFESOR
                    miFabrica = new Fabrica("profesor");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

            /*
              Si se ha instanciado la clase Fabrica con alumno o profesor
              procedemos a crear usuario y rellenarlo
             */
            if(miFabrica!=null){
                //Devuelve objeto de tipo profesor o alumno
                usuario = miFabrica.creaUsuario();
                try {
                    usuario.rellenarDatosUsuario();
                } catch (BibliotecaException e) {
                    System.out.println("Error: "+e);
                }
            }

        }while(seleccion!=0);
    }
}
