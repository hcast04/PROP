package dominio.functions;
import dominio.model.Function;

import java.util.*;

/**
 * Representa la función Covariance de las posibles funciones que puede ejecutar el usuario
 */
public class Covariance extends Function{

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Covariance() {
        nParameters = 2;
    }

    // Methods


    /**
     * Retorna la media de los elementos de v
     * @param v Array con los elementos a los cuales se le aplica la media
     * @param n tamaño de v
     * @return la media de los valores de v en formato double
     */
    private double mean(ArrayList<Double> v, double n) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += v.get(i);
        }
        return sum /n;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Public methods
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Covariance, que retorna
     * la covarianza de las dos variables pasadas por parámetro
     * @return covarianza de las dos variables en formato string
     */
    public String getValue() {
        ArrayList<Double> vectorX = new ArrayList<>();
        String result = "NULL";

        try {
            if (parameters.get(1).getArray() == null) {
                vectorX.add(parameters.get(1).getDouble());
                if (vectorX.get(0) == null) return result;
            } else {
                vectorX = parameters.get(1).getArray();
            }
        }
        catch (IndexOutOfBoundsException e) {
            return "#NAME?";
        }

        ArrayList<Double> vectorY = new ArrayList<>();

        try {
            if (parameters.get(2).getArray() == null) {
                vectorY.add(parameters.get(1).getDouble());
                if (vectorY.get(0) == null) return result;
            } else {
                vectorY = parameters.get(2).getArray();
            }
        }
        catch (IndexOutOfBoundsException e) {
            return "#NAME?";
        }

        double res = 0;
        int sizeX = vectorX.size();
        int sizeY = vectorY.size();
        if(sizeX == sizeY) {
            double meanX = mean(vectorX, sizeX);
            double meanY = mean(vectorY, sizeY);
            for (int i = 0; i < sizeX; i++) {
                res += ((vectorX.get(i) - meanX) * (vectorY.get(i) - meanY));
            }
            res = res / (sizeX);
            return String.valueOf(res);
        }
        else return "#NAME?";
    }
}
