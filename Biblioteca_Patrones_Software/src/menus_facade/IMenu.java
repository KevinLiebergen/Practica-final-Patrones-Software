package menus_facade;

import excepciones.BibliotecaException;

/**
 * Interfaz que deberán implementar las clases que gestionen menús
 *
 * @author kevin van Liebergen
 */

public interface IMenu {

    /**
     * Muestra las opciones de menú,
     * public es redundante en interfaces
     */
    void mostrarOpciones();

    /**
     * Ejecuta el comportamiento de las opciones de menú
     *
     * @throws BibliotecaException para fechas
     */
    void ejecutarOpciones() throws BibliotecaException;

}
