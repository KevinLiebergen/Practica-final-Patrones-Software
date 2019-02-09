package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import excepciones.BibliotecaException;
import gestionPublicsUsurs_singleton.GestionPublicaciones;
import gestionPublicsUsurs_singleton.GestionUsuarios;

/**
 * Clase que gestiona el menu principal de la biblioteca
 *
 * @author kevin van Liebergen
 */

public class MenuBiblioteca_facade implements IMenu {

    private AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

    private MenuCrearPublicacion menuCrearPublicacion;
    private MenuBorrarPublicacion menuBorrarPublicacion;
    private MenuModificarPublicacion menuModificarPublicacion;
    private MenuBusquedaPublicaciones menuBusquedaPublicaciones;
    private MenuCrearUsuario menuCrearUsuario;
    private MenuBorrarUsuario menuBorrarUsuario;
    private MenuPrestamoDevolucionPublicacion menuPrestamoDevolucionPublicacion;

    /**
     * Constructor
     */
    public MenuBiblioteca_facade(){
        menuCrearPublicacion = new MenuCrearPublicacion();
        menuBorrarPublicacion = new MenuBorrarPublicacion();
        menuModificarPublicacion = new MenuModificarPublicacion();
        menuBusquedaPublicaciones = new MenuBusquedaPublicaciones();
        menuCrearUsuario = new MenuCrearUsuario();
        menuBorrarUsuario = new MenuBorrarUsuario();
        menuPrestamoDevolucionPublicacion = new MenuPrestamoDevolucionPublicacion();
    }

    /**
     * Opciones generales del menu principal
     */
    public void mostrarOpciones(){
        System.out.println("\n0.- Salir de la aplicación");
        System.out.println("\n      Gestión de publicaciones\n");
        System.out.println("1.- Crear publicación");
        System.out.println("2.- Borrar publicación");
        System.out.println("3.- Mostrar publicaciones");
        System.out.println("4.- Modificar publicación");
        System.out.println("5.- Busqueda publicaciones");
        System.out.println("\n      Gestión de usuarios\n");
        System.out.println("6.- Crear usuario");
        System.out.println("7.- Borrar usuario");
        System.out.println("8.- Mostrar usuarios");
        System.out.println("\n      Gestión Prestamo / devolucion de publicaciones\n");
        System.out.println("9.- Préstamo / Devolución de publicaciones / publicaciones prestadas");
    }

    /**
     * Se selecciona un menu y se dirige a ejecutarOpciones de ese menu
     */
    public void ejecutarOpciones() {

        /*
          Cargamos los usuarios y publicaciones guardados
          de la sesion anterior
         */
        GestionUsuarios.cargarUsuariosSerializacion();
        GestionPublicaciones.cargarPublicacionesSerializacion();
        GestionPublicaciones.cargarPublicacionesPrestadasSerializacion();

        int seleccion;
        System.out.println("\nBuenos días, ¿que acción desea realizar?\n");

        do {

            mostrarOpciones();

            //Para no introducir letras en lugar de numeros
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            IMenu menu=null;

            switch (seleccion) {

                case 0:
                    System.out.println("Gracias, ¡hasta la próxima!");
                    //Se guarda en los ficheros .dat para serializacion
                    //los datos creados y/o modificados
                    GestionUsuarios.guardarUsuariosSerializacion();
                    GestionPublicaciones.guardarPublicacionesSerializacion();
                    GestionPublicaciones.guardarPublicacionesPrestadasSerializacion();
                    break;
                case 1:
                    menu = menuCrearPublicacion;
                    break;
                case 2:
                    menu = menuBorrarPublicacion;
                    break;
                case 3:
                    GestionPublicaciones.mostrarPublicaciones();
                    break;
                case 4:
                    menu = menuModificarPublicacion;
                    break;
                case 5:
                    menu = menuBusquedaPublicaciones;
                    break;
                case 6:
                    menu = menuCrearUsuario;
                    break;
                case 7:
                    menu = menuBorrarUsuario;
                    break;
                case 8:
                    GestionUsuarios.mostrarUsuarios();
                    break;
                case 9:
                    menu = menuPrestamoDevolucionPublicacion;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

            if(menu!=null){
                try {
                    menu.ejecutarOpciones();
                } catch (BibliotecaException e) {
                    System.out.println("Error: "+e);
                }
            }

        }while(seleccion!=0);

    }

}
