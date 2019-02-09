package publicaciones_builder;

import excepciones.AsegurarOpcionCorrecta;

/**
 * Constructor concreto
 *
 * @author kevin van Liebergen
 * @version 2.0
 */


public class RevistaBuilder extends PublicacionBuilder{

    private Publicacion publicacion;
    private String periodicidad;
    private int volumen;
    private int numero;

    public RevistaBuilder(Publicacion publicacion){
        super(publicacion);
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public int getVolumen() {
        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Anade las caracteristicas concretas de la publicacion proyecto
     */
    public void anadirCaracteristicas(){

        AsegurarOpcionCorrecta asegurar = new AsegurarOpcionCorrecta();

        System.out.println("Tipo de la periodicidad (trimestral, semestral o anual): ");
        periodicidad = asegurar.asegurarTipoPeriodicidad();
        setPeriodicidad(periodicidad);

        System.out.println("Numero del volumen: ");
        volumen = asegurar.asegurarRespuestaSeaNumero();
        setVolumen(volumen);

        System.out.println("Numero de la publicacion: ");
        numero = asegurar.asegurarRespuestaSeaNumero();
        setNumero(numero);

    }
    
    @Override
    public void anadirCaracteristicas(String periodicidad, String volumen, String numero){
        setPeriodicidad(periodicidad);       
        setVolumen(Integer.valueOf(volumen));
        setNumero(Integer.valueOf(numero));
    }

    public String toString(){
        return "\nISBN: "+this.getPublicacion().getIsbn()+
                ", título: "+this.getPublicacion().getTitulo()+
                ", autor/es: "+ this.getPublicacion().getAutores()+
                ",\nfecha publicacion: "+this.getPublicacion().getFechaPublicacion()+
                ", materia publicacion: "+ this.getPublicacion().getMateria()+
                ",\ntipo: Revista, periodicidad: "+getPeriodicidad()+
                ", volumen: "+getVolumen()+
                ", nº publicacion: "+getNumero()+
                ", prestado: "+getPublicacion().getiEstado().ejecutarAccion()+"\n";
    }

}
