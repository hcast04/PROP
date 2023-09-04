package dominio.functions;

import java.util.*;
import java.lang.Math;
import dominio.model.Function;

/**
 * Representa la función PearsonCorrelation de las posibles funciones que puede ejecutar el usuario
 */
public class PearsonCorrelation extends Function {

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public PearsonCorrelation() {
        nParameters = 2;
    }



    /**
     * Retorna la media de los elementos de vectorX
     * @param vectorX vector con los elementos a calcular su media
     * @return media de los elementos de vectorX en formato double
     */
    private double meanX(ArrayList<Double> vectorX) {
        double sum = 0;
        for (int i = 0; i < vectorX.size(); i++) {
            sum += vectorX.get(i);
        }
        return sum / vectorX.size();
    }

    /**
     * Retorna la media de los elementos de vectorY
     * @param vectorY vector con los elementos a calcular su media
     * @return media de los elementos de vectorY en formato double
     */
    private double meanY(ArrayList<Double> vectorY) {
        double sum = 0;
        for (int i = 0; i < vectorY.size(); i++) {
            sum += vectorY.get(i);
        }
        return sum / vectorY.size();
    }


    // Public methods

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en PearsonCorrelation, que retorna el coeficiente
     * de correlación de Pearson de las dos variables pasadas por parámetro
     * @return coeficiente de Pearson de las dos variables  en formato string
     */
    public String getValue() {

        ArrayList<Double> vectorX = new ArrayList<>();

        if (parameters.get(1).getArray() == null) {
            vectorX.add(parameters.get(1).getDouble());
        }
        else {
            vectorX = parameters.get(1).getArray();
        }

        ArrayList<Double> vectorY = new ArrayList<>();

        if (parameters.get(2).getArray() == null) {
            vectorY.add(parameters.get(2).getDouble());
        }
        else {
            vectorY = parameters.get(2).getArray();
        }

        double sumXdotY = 0, sumXsquare = 0, sumYsquare = 0;
        if (vectorX.size() == vectorY.size()) {
            for (int i = 0; i < vectorX.size(); i++) {
                double x = vectorX.get(i);
                double y = vectorY.get(i);
                sumXdotY += (x - meanX(vectorX)) * (y - meanY(vectorY));
                sumXsquare += (x - meanX(vectorX)) * (x - meanX(vectorX));
                sumYsquare += (y - meanY(vectorY)) * (y - meanY(vectorY));
                if (sumXsquare == 0 || sumYsquare == 0) return "#NAME?";
            }
            return String.valueOf(sumXdotY / Math.sqrt(sumXsquare * sumYsquare));
        }
        else return "#NAME?";

    }


}