package dominio.functions;

import java.util.*;
import dominio.model.Function;

/**
 * Representa la función StandardDeviation de las posibles funciones que puede ejecutar el usuario
 */
public class StandardDeviation extends Function {

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public StandardDeviation() {
        nParameters = 2;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en StandardDeviation, que retorna la
     * desviación estándar poblacional o muestral de los parámetros indicados
     * @return la desviación estándar en formato String
     */
    public String getValue() {

        String type = parameters.get(1).getString();
        ArrayList<Double> values = new ArrayList<>();

        if (parameters.get(2).getArray() == null) {
            values.add(parameters.get(2).getDouble());
        }
        else {
            values = parameters.get(2).getArray();
        }



        double sum = 0.0, standardDeviation = 0.0;
        for(double num : values) {
            sum += num;
        }
        double mean = sum/values.size();

        for(double num: values) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        if (Objects.equals(type, "population")) standardDeviation = Math.sqrt(standardDeviation/values.size());
        else if (Objects.equals(type, "sample")) standardDeviation = Math.sqrt(standardDeviation/(values.size()-1));
        else return "#NAME?";
        return Double.toString(standardDeviation);
    }


}