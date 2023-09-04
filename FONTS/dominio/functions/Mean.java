package dominio.functions;
import dominio.model.Function;
import java.util.*;

/**
 * Representa la función Mean de las posibles funciones que puede ejecutar el usuario
 */
public class Mean extends Function{

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Mean() {
        nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Public methods
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Mean, que devuelve la media de los parámetros indicados
     * @return la media de los elementos pasados por parámetro en formato string
     */
    public String getValue() {
        ArrayList<Double> aux = new ArrayList<>();

        if (parameters.get(1).getArray() == null) {
            if ((parameters.get(1).getDouble() == null)) return "#NAME?";
            else aux.add(parameters.get(1).getDouble());
        }
        else {
            aux = parameters.get(1).getArray();
        }
        Double resu = 0.0;
        for (Double value : aux) {
            resu += value;
        }
        resu = resu/aux.size();
        if (resu % 1 == 0) return String.valueOf(resu.intValue());
        return String.valueOf(resu);
    }

}