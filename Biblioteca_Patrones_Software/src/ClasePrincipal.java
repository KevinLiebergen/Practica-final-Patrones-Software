import menus_facade.MenuBiblioteca_facade;


/**
 * @author kevin van Liebergen
 * @version 2.0
 */

public class ClasePrincipal {

    public static void main(String[] args){

        MenuBiblioteca_facade menuBiblio = new MenuBiblioteca_facade();

        menuBiblio.ejecutarOpciones();
    }
}
