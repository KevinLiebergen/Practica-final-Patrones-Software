package usuarios_factoryMethod.avisarPublicacionDevuelta_observer;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Esta clase Sujeto gestiona una Array con los
 * observadores que esperan que un libro se devuelva
 *
 * @author kevin van Liebergen
 */
public class Sujeto implements Serializable {

    private ArrayList<IObservador> observadores;

    public Sujeto(){
        observadores = new ArrayList<>();
    }

    /**
     *
     * @param o usuario (alumno o profesor)
     */
    public void registrarObservador(IObservador o){
        observadores.add(o);
    }

    /**
     * Una vez se notifican a los usuarios que esta
     * disponible el libro se formatea la lista
     */
    public void retirarObservadores(){
        observadores.clear();
    }

    /**
     * Se notifica a los observadores, llamada a este metodo
     * cuando una publicacion ha sido devuelta y lo necesitan
     * demas usuarios
     */
    public void notificar(){

        for(IObservador o:observadores){
            o.avisar();
        }
    }
}
