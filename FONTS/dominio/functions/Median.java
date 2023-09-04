package dominio.functions;

import java.util.*;
import dominio.model.Function;

/**
 * Representa la función Median de las posibles funciones que puede ejecutar el usuario
 */
public class Median extends Function{

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Median() {
        nParameters = 1;
    }

    // Public methods

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Median, que devuelve la mediana de los parámetros indicados
     * @return la mediana del vector en formato string
     */
    public String getValue() {
        ArrayList<Double> vector = new ArrayList<>();

        if (parameters.get(1).getArray() == null) {
            if ((parameters.get(1).getDouble() == null)) return "#NAME?";
            else vector.add(parameters.get(1).getDouble());
        }
        else {
            vector = parameters.get(1).getArray();
        }
        Collections.sort(vector);
        double aux;
        int size = vector.size();
        if (size % 2 == 1) aux = vector.get(((size + 1) / 2) - 1);
        else aux = (vector.get(size / 2 - 1) + vector.get(size / 2)) / 2;

        return String.valueOf(aux);
    }
}
