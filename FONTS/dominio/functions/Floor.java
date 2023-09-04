package dominio.functions;

import dominio.model.Function;
import java.lang.Math;

/**
 * Representa la función Floor de las posibles funciones que puede ejecutar el usuario
 */
public class Floor extends Function {

    //Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Floor() {
        nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Public methods
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Floor, que devuelve el número sin decimales
     * redondeando a la baja en formato string
     * @return el parámetro de entrada redondeada a la baja en formato String
     */

    public String getValue() {
        try {
            double parameter = parameters.get(1).getDouble();
            return String.valueOf(Math.floor(parameter));
        }
        catch (NullPointerException e) {
            return "#NAME?";
        }
    }

}