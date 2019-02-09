package menus_facade;

import excepciones.AsegurarOpcionCorrecta;
import excepciones.BibliotecaException;
import gestionPublicsUsurs_singleton.GestionPublicaciones;
import publicaciones_builder.*;

import java.util.Date;
import java.util.Scanner;

/**
 * @author kevin van Liebergen
 */
public class MenuModificarPublicacion implements IMenu {



    @Override
    public void mostrarOpciones(){
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Modificar características propias de la publicación");
        System.out.println("2.- Cambiar de un tipo de publicación a otra (De libro a revista, proyecto a libro...)");
    }


    @Override
    public void ejecutarOpciones() {
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        int seleccion;

        do {
            mostrarOpciones();
            seleccion = asegurar.asegurarRespuestaSeaNumero();

            switch (seleccion){
                case 0:
                    System.out.println("\nVuelvo hacia atrás\n");
                    break;
                case 1:
                    try {
                        modificarPublicacion();
                    } catch (BibliotecaException e) {
                        System.out.println("Error: "+e);
                    }
                    break;
                case 2:
                    modificarTipoDePublicacion();
                    break;
                default:
                    System.out.println("\nOpcion no válida\n");
            }

        }while(seleccion!=0);
    }


    /**
     * Una vez se elija modificar caracteristicas propias de la publicacion
     * se muestran las caracteristicas comunes a libros, revistas y proyectos
     */
    private void mostrarOpcionesBasicas() {
        System.out.println("0.- Volver un menú hacia atrás");
        System.out.println("1.- Modificar titulo");
        System.out.println("2.- Modificar autores");
        System.out.println("3.- Modificar fecha de publicacion");
        System.out.println("4.- Modificar materia");
    }

    /**
     * Dependiendo del tipo de publicacion se muestran caracteristicas
     * concretas de la publicacion a modificar
     * @param publicacion publicacion a modificar
     */
    private void mostrarOpcionesEspecificas(PublicacionBuilder publicacion){
        if(publicacion instanceof LibroBuilder){
            mostrarOpcionesEspecificasParaModificarLibro();
        }else if(publicacion instanceof RevistaBuilder){
            mostrarOpcionesEspecificasParaModificarRevista();
        }else if(publicacion instanceof ProyectoBuilder){
            mostrarOpcionesEspecificasParaModificarProyecto();
        }
    }

    private void mostrarOpcionesEspecificasParaModificarLibro(){
        System.out.println("\nOpciones especificas de libro\n");
        System.out.println("5.- Modificar editorial");
        System.out.println("6.- Modificar localidad");
        System.out.println("7.- Modificar número de edición");
    }

    private void mostrarOpcionesEspecificasParaModificarRevista() {
        System.out.println("\nOpciones especificas de revista\n");
        System.out.println("5.- Modificar periodicidad");
        System.out.println("6.- Modificar número volumen");
        System.out.println("7.- Modificar número de publicación");
    }

    private void mostrarOpcionesEspecificasParaModificarProyecto() {
        System.out.println("\nOpciones especificas de proyecto\n");
        System.out.println("5.- Modificar nombre del tribunal");
        System.out.println("6.- Modificar nombre de la titulación");
        System.out.println("7.- Modificar número de la calificación");
    }


    /**
     * Para  cambiar el tipo de una publicacion a otro tipo, dependiendo
     * de su instancia puede cambiar a los dos otros tipos de publicacion
     *
     * @param publicacion a modificat
     * @return entero tipo a cambiar
     */
    private int mostrarOpcionesParaCambiarDeTipo(PublicacionBuilder publicacion){
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

        if(publicacion instanceof LibroBuilder){
            System.out.println("0.- No cambiar de tipo");
            System.out.println("1.- Cambiar de libro a revista");
            System.out.println("2.- Cambiar de libro a proyecto");
        } if(publicacion instanceof RevistaBuilder){
            System.out.println("0.- No cambiar de tipo");
            System.out.println("1.- Cambiar de revista a libro");
            System.out.println("2.- Cambiar de revista a proyecto");
        } if(publicacion instanceof ProyectoBuilder){
            System.out.println("0.- No cambiar de tipo");
            System.out.println("1.- Cambiar de proyecto a libro");
            System.out.println("2.- Cambiar de proyecto a revista");
        }

        return asegurar.asegurarRespuestaSeaNumero();
    }


    /**
     * Metodo para modificar los campos segun la opcion que se elija
     * mediante el menu anterior y el isbn proporcionado
     */
    private void modificarPublicacion() throws BibliotecaException {
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        Scanner sc = new Scanner(System.in);
        PublicacionBuilder publicacion;
        String isbn;
        int seleccion;

        System.out.println("Inserte el ISBN de la publicación a modificar");
        isbn = asegurar.asegurarISBNExisteEnHashMap();

        publicacion = GestionPublicaciones.getPublicacionPorISBN(isbn);

        mostrarOpcionesBasicas();
        mostrarOpcionesEspecificas(publicacion);

        seleccion = asegurar.asegurarRespuestaSeaNumero();

        switch (seleccion) {
            case 0:
                System.out.println("\nVuelvo hacia atrás\n");
                break;
            case 1:
                System.out.println("\nEscriba el nuevo titulo de la publicación:");
                String titulo = sc.nextLine();

                publicacion.getPublicacion().setTitulo(titulo);
                break;
            case 2:
                System.out.println("\nEscriba Los nuevos autores de la publicación:");
                String autores = sc.nextLine();

                publicacion.getPublicacion().setAutores(autores);
                break;
            case 3:
                Date fechaPublicacion;
                System.out.println("Fecha de la publicacion(dd/MM/yyyy): ");

                fechaPublicacion = asegurar.asegurarFormatoFechaCorrecta();
                publicacion.getPublicacion().setFechaPublicacion(fechaPublicacion);
                break;
            case 4:
                System.out.println("\nEscriba la nueva materia de la publicación:");
                String materia = sc.nextLine();

                publicacion.getPublicacion().setMateria(materia);
                break;
            case 5:

                if(publicacion instanceof LibroBuilder){
                    System.out.println("Nombre de la editorial: ");
                    String editorial = sc.nextLine();
                    ((LibroBuilder) publicacion).setEditorial(editorial);

                }else if(publicacion instanceof RevistaBuilder){
                    System.out.println("Tipo de la periodicidad (trimestral, semestral o anual)");
                    String periodicidad = asegurar.asegurarTipoPeriodicidad();
                    ((RevistaBuilder) publicacion).setPeriodicidad(periodicidad);

                }else if(publicacion instanceof ProyectoBuilder){
                    System.out.println("Nombre del tribunal");
                    String tribunal = sc.nextLine();
                    ((ProyectoBuilder) publicacion).setTribunal(tribunal);
                }

                break;
            case 6:
                if(publicacion instanceof LibroBuilder){
                    System.out.println("Nombre de la localidad: ");
                    String localidad = sc.nextLine();
                    ((LibroBuilder) publicacion).setLocalidad(localidad);

                }else if(publicacion instanceof RevistaBuilder){
                    System.out.println("Numero del volumen");
                    int volumen = asegurar.asegurarRespuestaSeaNumero();
                    ((RevistaBuilder) publicacion).setVolumen(volumen);

                }else if(publicacion instanceof ProyectoBuilder){
                    System.out.println("Nombre de la titulacion");
                    String titulacion = sc.nextLine();
                    ((ProyectoBuilder) publicacion).setTitulacion(titulacion);
                }

                break;
            case 7:
                if(publicacion instanceof LibroBuilder){
                    System.out.println("Número de la edición: ");
                    int edicion = asegurar.asegurarRespuestaSeaNumero();
                    ((LibroBuilder) publicacion).setEdicion(edicion);

                }else if(publicacion instanceof RevistaBuilder){
                    System.out.println("Numero de la publicación");
                    int numero = asegurar.asegurarRespuestaSeaNumero();
                    ((RevistaBuilder) publicacion).setNumero(numero);

                }else if(publicacion instanceof ProyectoBuilder){
                    System.out.println("Numero de la calificación: ");
                    int calificacion = asegurar.asegurarRespuestaSeaNumero();
                    ((ProyectoBuilder) publicacion).setCalificacion(calificacion);
                }

                break;
            default:
                System.out.println("\nOpcion no valida\n");
        }

    }

    /**
     * Cambiar el tipo de publicacion, de libro a revista, libro a proyecto,
     * revista a libro, revista a proyecto, proyecto a libro y proyecto a revista
     */
    private void modificarTipoDePublicacion(){
        CreadorPublicaciones creador = new CreadorPublicaciones();

        Publicacion publicacion = new Publicacion();
        RevistaBuilder rb = new RevistaBuilder(publicacion);
        LibroBuilder lb = new LibroBuilder(publicacion);
        ProyectoBuilder pb = new ProyectoBuilder(publicacion);

        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();
        String isbn;
        int miSeleccion;
        PublicacionBuilder publicacionBuilder;

        System.out.println("Inserte el ISBN de la publicación a modificar");
        isbn = asegurar.asegurarISBNExisteEnHashMap();

        publicacionBuilder= GestionPublicaciones.getPublicacionPorISBN(isbn);


        do{
            miSeleccion = mostrarOpcionesParaCambiarDeTipo(publicacionBuilder);
            switch (miSeleccion){
                case 0:
                    System.out.println("\nVuelvo hacia atrás\n");
                    break;
                case 1:
                    if(publicacionBuilder instanceof LibroBuilder){
                        System.out.println("\nTransformando de libro a revista...\n");
                        //Establece el constructor concreto
                        creador.setPublicacionBuilder(rb);
                    }else if(publicacionBuilder instanceof RevistaBuilder){
                        System.out.println("\nTransformando de revista a libro...\n");
                        creador.setPublicacionBuilder(lb);
                    }else if(publicacionBuilder instanceof ProyectoBuilder){
                        System.out.println("\nTransformando de proyecto a libro...\n");
                        creador.setPublicacionBuilder(lb);
                    }
                    creador.crearPublicacion(publicacionBuilder.getPublicacion());

                    break;
                case 2:
                    if(publicacionBuilder instanceof LibroBuilder){
                        System.out.println("\nTransformando de libro a proyecto...\n");
                        //Establece el constructor concreto
                        creador.setPublicacionBuilder(pb);
                    }else if(publicacionBuilder instanceof RevistaBuilder){
                        System.out.println("\nTransformando de revista a proyecto...\n");
                        creador.setPublicacionBuilder(pb);
                    }else if(publicacionBuilder instanceof ProyectoBuilder){
                        System.out.println("\nTransformando de proyecto a revista...\n");
                        creador.setPublicacionBuilder(rb);
                    }

                    //Construye la publicacion con las caracteristicas basicas ya creadas
                    creador.crearPublicacion(publicacionBuilder.getPublicacion());

                    break;
                default:
                    System.out.println("\nOpción no válida\n");
            }
        }while(miSeleccion!=0);
    }

}
