package excepciones;

/**
 * Excepcion que se lanzara cuando el formato de un objeto sea incorrecto
 *
 * @author kevin van Liebergen
 * @version 2.0
 */

public class BibliotecaException extends Exception{

    //Acceso puede ser paquete privado, se omite public
    static String FORMATO_NO_VALIDO="Seleccione una fecha con el formato adecuado (dd/MM/yyyy)";
    static String DIA_NO_VALIDO="No se permiten insertar días menores de 1 ni mayores de 31";
    static String MES_NO_VALIDO="No se permiten insertar meses menores de 1 ni mayores de 12";
    static String ANIO_NO_VALIDO="No se permiten insertar anios menores de 1800 ni mayores de 2020";

    BibliotecaException(String txt) {
        super(txt);
    }
}
