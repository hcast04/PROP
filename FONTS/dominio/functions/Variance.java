package dominio.functions;

import java.util.*;
import dominio.model.Function;

/**
 * Representa la función Variance de las posibles funciones que puede ejecutar el usuario
 */
public class Variance extends Function{

    // Constructor
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Variance() {
        nParameters = 2;
    }


    // Methods
    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Variance, que retorna la varianza
     * poblacional o muestral de los parámetros indicados
     * @return la varianza en formato string de los valores del vector
     */
    public String getValue(){
        ArrayList<Double> aux = new ArrayList<>();
        String type = parameters.get(1).getString();
        if (parameters.get(2).getArray() == null) {
            aux.add(parameters.get(2).getDouble());
        }
        else {
            aux = parameters.get(2).getArray();
        }
        double mean = 0.0;
        double res = 0.0;
        int size = aux.size();
        for (Double value : aux) {
            mean += value;
        }
        mean = mean / size;

        for (Double value : aux) {
            res += (value - mean) * (value - mean);
        }
        if(type.equals("population")) res = res / size;
        else if (type.equals("sample")) res = res /(size-1);
        else res = 0.0;
        return String.valueOf(res);
    }

}
