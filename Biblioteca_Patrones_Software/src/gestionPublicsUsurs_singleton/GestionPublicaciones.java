package gestionPublicsUsurs_singleton;

import publicaciones_builder.*;
import publicaciones_builder.EstadoPublicacion_State.*;
import usuarios_factoryMethod.Alumno;
import usuarios_factoryMethod.Profesor;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Clase encargada de guardar las publicaciones,
 * programada como Singleton para garantizar
 * que solo tenga una instancia, para ello se establece
 * constructor privado y se crea una nueva instancia propia
 *
 * @author kevin van Liebergen
 * @version 2.0
 */

public final class GestionPublicaciones implements Serializable{

    private static final GestionPublicaciones gestionPubs = new GestionPublicaciones();

    //Hashmap donde se guardan todas las publicaciones creadas
    private static HashMap<String, PublicacionBuilder> publicaciones = new HashMap<>();

    //Se guarda en un HashMap las publicaciones prestadas, array de la clase Prestamo
    private static HashMap<String, Prestamo> publicacionesPrestadas = new HashMap<>();

    private GestionPublicaciones(){
    }


    public static HashMap<String, PublicacionBuilder> getPublicaciones() {
        return publicaciones;
    }

    public static HashMap<String, Prestamo> getPublicacionesPrestadas() {
        return publicacionesPrestadas;
    }

    /**
     * Se anade la publicacion al HashMap, si el ISBN coincide
     * con un ISBN que ya se encuentra en el HashMap no se anade
     *
     * @param publicacion PublicacionBuilder
     */
    public static void anadirPublicacion(PublicacionBuilder publicacion){

        if(publicaciones.containsKey(publicacion.getPublicacion().getIsbn())){
            System.out.println("\nYa existe una publicacion con ese ISBN, no se puede insertar\n");
            JOptionPane.showMessageDialog(null, "Ya existe una publicación con ese ISBN, no se puede insertar", "Información", JOptionPane.INFORMATION_MESSAGE);

        }else {
            publicaciones.put(publicacion.getPublicacion().getIsbn(), publicacion);
            System.out.println("\nPublicación creada correctamente, publicacion creada: \n");
            System.out.println(publicacion.toString());
        }
    }

    /**
     * En este caso no comprobamos si ya existe el isbn
     * debido a que modificamos la publicacion
     *
     * @param publicacionBuilder publicacion actualizada
     */
    public static void anadirPublicacionModificada(PublicacionBuilder publicacionBuilder){
        publicaciones.put(publicacionBuilder.getPublicacion().getIsbn(), publicacionBuilder);
        System.out.println("\nPublicación modificada correctamente, publicacion modificada: \n");
        System.out.println(publicacionBuilder.toString());

    }

    /**
     * Se recorren todas las publicaciones existentes
     * del HashMap
     */
    public static void mostrarPublicaciones(){
        if(publicaciones.isEmpty()){
            System.out.println("La biblioteca no tiene publicaciones\n");
        }else{
            System.out.println("Las publicaciones de la biblioteca son: ");
            System.out.println(publicaciones.values());
        }
    }

    /**
     * Se borra la publicacion solicitada
     * @param isbn referencia a la publicacion con ese isbn
     */
    public static void borrarPublicacion(String isbn){
        if(publicaciones.containsKey(isbn)){
            //Borrando publicacion: {isbn}, {titulo publicacion} ...
            System.out.println("\nBorrando publicacion: "+isbn+", "+publicaciones.get(isbn).getPublicacion().getTitulo()+" ...\n");
            publicaciones.remove(isbn);
        }else{
            System.out.println("\nNo existe ninguna publicacion con el ISBN solicitado\n");
        }
    }

    /**
     * Devuelve la publicacion existente solicitada por el ISBN,
     * anteriormente nos hemos asegurado que existe ese isbn
     * @param isbn String a buscar
     * @return Publicacion
     */
    public static PublicacionBuilder getPublicacionPorISBN(String isbn) {
        return publicaciones.get(isbn);
    }


    /**
     *  Guardado de datos para serializacion,
     *  guardamos en archivo publicaciones.dat
     *  las publicaciones creadas
     */
    public static void guardarPublicacionesSerializacion(){

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("publicaciones.dat");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(publicaciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cargar usuarios Serializacion,
     * se recupera a traves del archivo usuarios.dat creado
     */
    public static void cargarPublicacionesSerializacion(){

        try (FileInputStream fis = new FileInputStream("publicaciones.dat")) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            publicaciones = (HashMap) ois.readObject();
        } catch (IOException ex) {
            System.out.println("No existen publicaciones en la biblioteca. " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error de clase. "+e.getMessage());
        }
    }

    /**
     * Se guarda en el archivo publicacionesPrestadas.dat
     * un array con las publicaciones prestadas
     */
    public static void guardarPublicacionesPrestadasSerializacion(){

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("publicacionesPrestadas.dat");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(publicacionesPrestadas);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    /**
     * Carga publicaciones prestadas guardadas
     * en publicacionesPrestadas.dat
     */
    public static void cargarPublicacionesPrestadasSerializacion(){
        try (FileInputStream fis = new FileInputStream("publicacionesPrestadas.dat")) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            publicacionesPrestadas = (HashMap) ois.readObject();
        } catch (IOException ex) {
            System.out.println("No existen publicaciones prestadas. " );
        } catch (ClassNotFoundException e) {
            System.out.println("Error de clase. "+e.getMessage());
        }

    }

    /**
     * Se tranforma el HashMap en un ArrayList para poder
     * iterarlo mediante el PATRON DE DISENO ITERATOR,
     * se imprime las publicaciones filtradas por autor, titulo,
     * materia o fecha de publicacion
     *
     * Los tres puntos en un metodo es para pasar de 0 a más argumentos,
     * se puede pasar un simple String referenciando al autor o 2 Strings
     * referenciando al autor y titulo
     * 
     * @param numeroOpcion
     * @param args String
     */
    public static void busquedaPublicaciones(int numeroOpcion, String ... args){
        ArrayList<PublicacionBuilder> listaPublicaciones = new ArrayList<>(publicaciones.values());
        String parametroAuxiliar="", AutorABuscar="", TituloABuscar="";
        String busq = args[0];
        boolean encontrado=false;


        Iterator<PublicacionBuilder> it = listaPublicaciones.iterator();

        while(it.hasNext()){
            String CadenaABuscar="";
            PublicacionBuilder publicacion = it.next();

            /*
              Depende de la opcion elegida en el menu seleccionamos aqui
               si buscamos por autor, titulo, ambos o materia
             */
            if(numeroOpcion==1){
                CadenaABuscar = publicacion.getPublicacion().getAutores();
            }else if(numeroOpcion==2){
                CadenaABuscar = publicacion.getPublicacion().getTitulo();
            }else if(numeroOpcion==3){
                AutorABuscar = publicacion.getPublicacion().getAutores();
                TituloABuscar = publicacion.getPublicacion().getTitulo();
            }else if(numeroOpcion==4){
                CadenaABuscar=publicacion.getPublicacion().getMateria();
            }

            /*
              La opcion es 3 significa que se pasa por parametro el autor y titulo a buscar,
              se recoge como parametroAuxiliar el segundo parametro que identifica al titulo
              y se busca por autor y titulo
             */
            if(numeroOpcion==3){
                parametroAuxiliar = args[1];
                if(AutorABuscar.equals(busq) && TituloABuscar.equals(parametroAuxiliar)){
                    System.out.println(publicacion);
                    encontrado=true;
                }
            }else{
                if(CadenaABuscar.equals(busq)){
                    System.out.println(publicacion);
                    encontrado=true;
                }
            }
        }
        if(!encontrado)
            System.out.println("\nPublicacion/es no encontrada/s para los datos introducidos\n");

    }

    /**
     * Para que pueda consultar solamente por medio de la fecha o
     * solo por fecha y materia, admitimos que pueda o no aceptar el String
     * materia, si no se le pasa por parametro se busca unicamente por fecha,
     * en caso contrario busca por fecha y el parametro pasado como materia
     *
     * @param numeroOpcion El numero de opcion del menuBusquedaPublicacion
     * @param fechaPublicacion Tipo de dato Date
     * @param args 0 o mas argumentos String
     */
    public static void busquedaPublicaciones(int numeroOpcion, Date fechaPublicacion, String ... args) {
        ArrayList<PublicacionBuilder> listaPublicaciones = new ArrayList<>(publicaciones.values());
        Iterator<PublicacionBuilder> it = listaPublicaciones.iterator();
        boolean encontrado=false;

        //Si se busca solo por fecha
        if (numeroOpcion == 5) {
            while (it.hasNext()) {
                PublicacionBuilder publicacion = it.next();
                //Se compara la fecha de la publicacion con la fecha introducida manualmente
                if (publicacion.getPublicacion().getFechaPublicacion().equals(fechaPublicacion)) {
                    System.out.println(publicacion);
                    encontrado=true;
                }
            }

            //Si se busca por fecha y materia
        } else if (numeroOpcion == 6) {
            String materia = args[0];
            while (it.hasNext()) {
                PublicacionBuilder publicacion = it.next();
                //Se compara la fecha de la publicacion con la fecha introducida manualmente y la materia
                if (publicacion.getPublicacion().getFechaPublicacion().equals(fechaPublicacion) &&
                        publicacion.getPublicacion().getMateria().equals(materia)) {
                    System.out.println(publicacion);
                    encontrado=true;
                }
            }
        }
        if(!encontrado)
            System.out.println("Publicacion/es no encontrada/s para los datos introducidos");
    }

    /**
     *  Cambia la publicacion de estado Prestado a NoPrestado,
     *  dependiendo de que publicacion y usuario se trate se devuelve
     *  el numero de dias que se va a prestar la publicacion
     *
     * @param nif del usuario
     * @param isbn de la publicacion
     * @return int dias permitidos
     */
    public static int prestarPublicacion(String nif, String isbn){

        int diasPermitidos=0;
        Prestado prestado = new Prestado();

        //Numero de dias permitidos para alquilar el usuario la publicacion
        if(GestionUsuarios.getUsuarios().get(nif) instanceof Profesor){
            if(GestionPublicaciones.getPublicaciones().get(isbn) instanceof LibroBuilder){
                diasPermitidos=10;
            }else if(GestionPublicaciones.getPublicaciones().get(isbn) instanceof RevistaBuilder){
                diasPermitidos=6;
            }else if(GestionPublicaciones.getPublicaciones().get(isbn) instanceof ProyectoBuilder){
                diasPermitidos=12;
            }
        }else if(GestionUsuarios.getUsuarios().get(nif) instanceof Alumno){
            if(GestionPublicaciones.getPublicaciones().get(isbn) instanceof LibroBuilder){
                diasPermitidos=6;
            }else if(GestionPublicaciones.getPublicaciones().get(isbn) instanceof RevistaBuilder){
                diasPermitidos=3;
            }else if(GestionPublicaciones.getPublicaciones().get(isbn) instanceof ProyectoBuilder){
                diasPermitidos=10;
            }
        }

        //Cambiamos el estado de no prestado a prestado
        publicaciones.get(isbn).getPublicacion().setiEstado(prestado);

        Date fechaPrestamo = new Date();

        /*
          Se anade el objeto tipo prestamo al arrayList de las publicaciones prestadas,
          el objeto prestamo mantiene una estructura con las caracteristicas de
          la prestamo como el isbn de la publicacion, el nif del usuario, los dias permitidos
          para llevar el libro y la fecha de prestamo y devolucion
         */

        //Se crea el objeto prestamo
        Prestamo prestamo = new Prestamo(nif, isbn, diasPermitidos,
                publicaciones.get(isbn), fechaPrestamo, null);

        //Se anade al array de publicaciones prestadas
        publicacionesPrestadas.put(isbn, prestamo);

        return diasPermitidos;
    }

    /**
     * Se devuelve la publicacion, se cambia el estado
     * de prestado a no prestado, se asigna la fecha de
     * devolucion al objeto prestamo del array y se
     * averiguan los dias sobrepasados desde los dias permitidos
     *
     * @param isbn de la publicacion
     * @return dias sobrepasados
     */
    public static int devolucionPublicacion(String isbn){
        Prestamo publicacionPrestada;
        NoPrestado noPrestado = new NoPrestado();
        int diasPublicacionPrestada, diasSobrepasados;

        //Cambiamos el estado de prestado a no prestado
        publicaciones.get(isbn).getPublicacion().setiEstado(noPrestado);

        //Asignamos la fecha de devolucion
        Date FechaDevolucion = new Date();
        publicacionesPrestadas.get(isbn).setFechaDevolucion(FechaDevolucion);

        //Recuperamos la publicacion para consultar los dias que ha habido
        //entre fecha de prestamo y fecha de devolucion
        publicacionPrestada = publicacionesPrestadas.get(isbn);

        diasPublicacionPrestada = diferenciaEnDias(publicacionPrestada.getFechaDevolucion(), publicacionPrestada.getFechaPrestamo());

        //Averiguamos si se han sobrepasado los dias y hay que sancionar
        diasSobrepasados = diasPublicacionPrestada - publicacionPrestada.getDiasPermitidos();

        generarFacturaPublicacion(publicacionPrestada);

        publicacionesPrestadas.remove(isbn);

        return diasSobrepasados;
    }

    /**
     * Halla la diferencia en dias entre 2 fechas
     *
     * @param fechaMayor fecha de devolucion
     * @param fechaMenor fecha de prestamo
     * @return dias
     */
    private static int diferenciaEnDias(Date fechaMayor, Date fechaMenor) {
        long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

    /**
     * Se guarda la factura en un fichero txt
     *
     * @param publicacionPrestada datos de publicacion para hacer ticket
     */
    private static void generarFacturaPublicacion(Prestamo publicacionPrestada){

        try {

            String id = publicacionPrestada.getNif() + " - " + publicacionPrestada.getPublicacionBuilder().getPublicacion().getTitulo();
            try (PrintWriter salida = new PrintWriter(new BufferedWriter(new FileWriter(id + ".txt")))) {
                salida.println("\nDatos de la publicacion\n");
                salida.println("ISBN: " + publicacionPrestada.getIsbn());
                salida.println("Titulo: " + publicacionPrestada.getPublicacionBuilder().getPublicacion().getTitulo());
                salida.println("Autor/es: " + publicacionPrestada.getPublicacionBuilder().getPublicacion().getAutores());
                salida.println("Fecha: " + publicacionPrestada.getPublicacionBuilder().getPublicacion().getFechaPublicacion());
                salida.println("\nDatos del usuario\n");
                salida.println("NIF: " + publicacionPrestada.getNif());

                salida.println("Fecha Prestamo de la publicacion: " + publicacionPrestada.getFechaPrestamo());
                salida.println("Fecha de devolucion de la publicacion: " + publicacionPrestada.getFechaDevolucion());

            }
        } catch (IOException ioe) {
            System.out.println("Error IO:" + ioe.toString());
        }
    }

    /**
     * Se anaden observadores (alumno u profesor) a la publicacion para
     * avisar cuando la publicacion este disponible
     *
     * @param nif del usuario a avisar
     * @param isbn de la publicacion prestada
     */
    public static void anadirObservadorAPublicacion(String nif, String isbn){

        //Devuelvo la publicacion, en la publicacion se llama al metodo agregarOberservadores
        PublicacionBuilder publicacionQueObservan = publicaciones.get(isbn);

        publicacionQueObservan.anadirObservador(nif);

    }

    /**
     * Se avisan a los observadores que estaban esperando
     * que la publicacion con el isbn del parametro haya
     * sido devuelta
     *
     * @param isbn de la publicacion que ha sido devuelta
     */
    public static void avisarAObservadoresDePublicacionConcreta(String isbn){
        // Devuelvo la publicacion del HashMap publicaciones
        PublicacionBuilder publicacionQueObservan = publicaciones.get(isbn);

        // Se avisan a todos los observadores
        publicacionQueObservan.avisarObservadores();
    }

    /**
     * Una vez se han avisado a los usuarios se borran los observadores
     * en esa publicacion
     *
     * @param isbn Se hallan los observadores de esa publicacion
     */
    public static void eliminarObservadoresDePublicacion(String isbn){
        //Devuelvo la publicacion
        PublicacionBuilder publicacionQueObservan = publicaciones.get(isbn);

        //Se borran todos los observadores una vez avisados
        publicacionQueObservan.eliminarObservadores();
    }

}
