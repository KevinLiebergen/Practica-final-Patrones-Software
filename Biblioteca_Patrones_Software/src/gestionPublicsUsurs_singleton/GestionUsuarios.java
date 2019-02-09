package gestionPublicsUsurs_singleton;

import usuarios_factoryMethod.Usuario;

import java.io.*;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * Clase encargada de guardar los usuarios,
 * programada como Singleton para garantizar
 * que solo tenga una instancia, para ello se establece
 * constructor privado y se crea una nueva instancia propia
 *
 * @author kevin van Liebergen
 * @version 2.0
 */

public final class GestionUsuarios implements Serializable {

    private static final GestionUsuarios gestionUsrs = new GestionUsuarios();
    private static HashMap<String, Usuario> usuarios = new HashMap<>();


    private GestionUsuarios(){
    }

    public static HashMap<String, Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Se anade el usuario al HashMap, si el nif coincide
     * con un nif que ya se encuentra en el HashMap no se anade
     *
     * @param usuario Usuario
     */
    public static void anadirUsuario(Usuario usuario){
        if(usuarios.containsKey(usuario.getNif())){
            System.out.println("\nYa existe un usuario con ese DNI, no se puede insertar\n");
        }else{
            usuarios.put(usuario.getNif(), usuario);
            System.out.println("\nUsuario creado correctamente, usuario creado: \n");
            System.out.println(usuario.toString());
        }
    }

    /**
     * Se recorren todos los usuarios existentes
     * del HashMap
     */
    public static void mostrarUsuarios(){
        if(usuarios.isEmpty()){
            System.out.println("\nNo existen usuarios en la aplicacion\n");
        }else{
            System.out.println("\nLos usuarios de la biblioteca son: ");
            System.out.println(usuarios.values());
        }
    }

    /**
     * Se borra el usuario solicitado, si existe en el HashMap
     * @param nif del usuario
     */
    public static void borrarUsuario(String nif){
        if(usuarios.containsKey(nif)){
            //Borrando usuario: {nif}, {nombre completo} ...
            System.out.println("\nBorrando usuario: "+nif+", "+usuarios.get(nif).getNombreCompleto()+" ...\n");
            usuarios.remove(nif);
        }else{
            System.out.println("\nNo existe ningun usuario con el NIF solicitado\n");
            JOptionPane.showMessageDialog(null, "No existe ningun usuario con el NIF solicitado", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     *  Guardado de datos para serializacion,
     *  guardamos en archivo usuarios.dat los usuarios creados
     */
    public static void guardarUsuariosSerializacion(){

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("usuarios.dat");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Cargar usuarios Serializacion,
     * se recupera a traves del archivo usuarios.dat creado
     */
    public static void cargarUsuariosSerializacion(){

        try (FileInputStream fis = new FileInputStream("usuarios.dat")) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            usuarios = (HashMap) ois.readObject();
        } catch (IOException ex) {
            System.out.println("Error de IO, no existen usuarios en la biblioteca. " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error de clase. "+e.getMessage());
        }
    }

}
