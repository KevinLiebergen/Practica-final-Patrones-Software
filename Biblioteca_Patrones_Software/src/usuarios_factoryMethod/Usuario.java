package usuarios_factoryMethod;

import excepciones.AsegurarOpcionCorrecta;
import excepciones.BibliotecaException;
import gestionPublicsUsurs_singleton.GestionUsuarios;
import usuarios_factoryMethod.avisarPublicacionDevuelta_observer.IObservador;

import java.io.Serializable;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Producto abstracto usuario
 *
 * @author kevin van Liebergen
 */
public abstract class Usuario implements Serializable, IObservador {

    /**
     * Caracteristicas basicas del usuario
     */
    private String nif;
    private String nombreCompleto;
    private Date fechaInscripcion;

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    /**
     * Se pregunta por pantalla y se rellena los datos del usuario a crear
     *
     * @throws excepciones.BibliotecaException
     */
    public void rellenarDatosUsuario() throws BibliotecaException {
        Scanner sc = new Scanner(System.in);
        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

        System.out.println("NIF del usuario");
        nif = sc.nextLine();
        setNif(nif);

        System.out.println("Nombre completo del usuario: ");
        nombreCompleto = sc.nextLine();
        setNombreCompleto(nombreCompleto);

        System.out.println("Fecha de inscripcion(dd/MM/yyyy): ");
        fechaInscripcion = asegurar.asegurarFormatoFechaCorrecta();
        setFechaInscripcion(fechaInscripcion);

        GestionUsuarios.anadirUsuario(this);
    }

    public void rellenarDatosUsuario(String nifGrafico, String nombreGrafico, Date fechaGrafico) {
        setNif(nifGrafico);
        setNombreCompleto(nombreGrafico);
        setFechaInscripcion(fechaGrafico);

        GestionUsuarios.anadirUsuario(this);

    }

    @Override
    public void avisar() {
        
        String mensaje = "\nLa publicación solicitada ya esta disponible para el usuario " + this.getNombreCompleto()
                + " con NIF: " + nif + "\n";

        System.out.println(mensaje);
        
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);                        
    }

    public abstract String toString();
}
