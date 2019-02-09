package excepciones;

import gestionPublicsUsurs_singleton.GestionPublicaciones;
import gestionPublicsUsurs_singleton.GestionUsuarios;
import publicaciones_builder.EstadoPublicacion_State.NoPrestado;
import publicaciones_builder.EstadoPublicacion_State.Prestado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


/**
 * Clase para asegurar que el input escrito es del tipo correcto
 *
 * @author kevin van Liebergen
 * @version 2.0
 */

public class AsegurarOpcionCorrecta {

    private Scanner sc = new Scanner(System.in);


    /**
     * Nos aseguramos de que solo se inserten numeros a la hora
     * de elegir en los menús
     *
     * @return entero
     */
    public int asegurarRespuestaSeaNumero(){

        int seleccion;

        while(true) {
            try {
                seleccion = sc.nextInt();
                break;
            } catch (Exception e) {
                sc.next();
                System.out.println("Seleccione un número");
            }
        }
        return seleccion;
    }
    
    /**
     * 
     * @param cadena
     * @return 
     */
    public boolean isNumeric(String cadena){
        boolean resultado;
        
        try{
            Integer.parseInt(cadena);
            resultado = true;
        }catch(NumberFormatException excepcion){
            resultado = false;
        }
        
        return resultado;
    }

    /**
     * A la hora de crear una revista es necesario
     * que escriba correctamente el tipo de periodicidad
     *
     * @return (trimestral | semestral | anual)
     */
    public String asegurarTipoPeriodicidad(){

        String tipo;
        while(true) {
            tipo = sc.nextLine();

            if ( (tipo.equalsIgnoreCase("trimestral"))|
                 (tipo.equalsIgnoreCase("semestral"))|
                 (tipo.equalsIgnoreCase("anual")) ){
                break;
            }else{
                System.out.println("\nEscriba trimestral, semestral o anual: ");
            }
        }
        return tipo;
    }

    /**
     * Es necesario especificar correctamente el formato de la fecha
     * y limitar el numero de dias, meses y anios.
     *
     * @return Date
     */
    public Date asegurarFormatoFechaCorrecta() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        String fecha;
        String[] fechaDescompuesta;

        while(true) try {
            fecha = sc.next();
            fechaDescompuesta = fecha.split("/");

            if (fechaDescompuesta.length != 3) {
                throw new BibliotecaException(BibliotecaException.FORMATO_NO_VALIDO);
            }
            if (Integer.valueOf(fechaDescompuesta[0]) < 1 | Integer.valueOf(fechaDescompuesta[0]) > 31) {
                throw new BibliotecaException(BibliotecaException.DIA_NO_VALIDO);
            }
            if (Integer.valueOf(fechaDescompuesta[1]) < 1 | Integer.valueOf(fechaDescompuesta[1]) > 12) {
                throw new BibliotecaException(BibliotecaException.MES_NO_VALIDO);
            }
            if (Integer.valueOf(fechaDescompuesta[2]) < 1800 | Integer.valueOf(fechaDescompuesta[2]) > 2020) {
                throw new BibliotecaException(BibliotecaException.ANIO_NO_VALIDO);
            }

            date = formatoFecha.parse(fecha);
            break;
        } catch (BibliotecaException e) {
            System.out.println("Error: "+e);
        } catch (ParseException e) {
            System.out.println("Ademas seleccione los días, meses y anios correctos");
        } catch (NumberFormatException e) {
            System.out.println("Es necesario ingresar numeros");
        }
        return date;
    }

    /**
     * A la hora de registrar un nuevo usuario
     * es necesario conocer si el NIF ya existe
     * @return NIF correcto sin estar repetido
     */
    public String asegurarNIFExisteEnHashMap(){
        String nif;
        while(true) {
            nif = sc.next();

            if(GestionUsuarios.getUsuarios().containsKey(nif)){
                break;
            }else{
                System.out.println("NIF no está registrado, introduce un NIF existente: ");
            }
        }
        return nif;
    }


    /**
     * Antes de registrar, modificar o alquilar una publicacion es necesario
     * asegurar de que el numero de isbn exista en el HashMap publicaciones
     *
     * @return String
     */
    public String asegurarISBNExisteEnHashMap(){
        String isbn;
        while(true) {
            isbn = sc.next();

            if (existeEnHashMapPublicaciones(isbn)) {
                break;
            }else{
                System.out.println("\nISBN no existe en la biblioteca, introduce un ISBN existente: \n");
            }
        }
        return isbn;
    }


    /**
     * Abstrae si existe el ISBN para cuando se llama mas veces
     * desde distintos lugares
     * @param isbn de la publicacion
     * @return booleano
     */
    public boolean existeEnHashMapPublicaciones(String isbn){
        return GestionPublicaciones.getPublicaciones().containsKey(isbn);
    }

    /**
     * A la hora de devolver una publicacion es necesario assegurarse
     * de que existe una publicacion con ese isbn y ha sido prestada
     * @return isbn que existe en HashMap y a la vez este prestado
     */
    public String asegurarISBNExisteEnHashMapYSiPrestado(){
        String isbn;
        while(true) {
            isbn = sc.next();

            if (existeEnHashMapPublicaciones(isbn)) {
                if(GestionPublicaciones.getPublicaciones().get(isbn).getPublicacion().getiEstado() instanceof Prestado){
                    break;
                }else{
                    System.out.println("\nLa publicación no está prestada, no se devuelve\n");
                }
            }else{
                System.out.println("\nISBN no existe en la biblioteca, introduce un ISBN existente: \n");
            }
        }
        return isbn;
    }

    /**
     * Para el patron observer, se avisa cuando una publicacion que se queria
     * alquilar esta prestada, el metodo compara el isbn introducido si es de
     * estado Prestado o NoPrestado
     *
     * @param isbn de publicacion
     * @return si ISBN esta prestado
     */
    public boolean siEstaPrestadoAvisar(String isbn){
        if(GestionPublicaciones.getPublicaciones().get(isbn).getPublicacion().getiEstado() instanceof NoPrestado){
            return false;
        }else{
            System.out.println("\nLa publicación está prestada, le avisaremos cuando se devuelva\n");
            return true;
        }

    }

}
