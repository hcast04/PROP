package dominio.functions;

import dominio.model.Function;

/**
 * Representa la función Identity de las posibles funciones que puede ejecutar el usuario
 */
public class Identity extends Function {

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Identity() {
        nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Methods
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Identity, que devuelve el mismo parámetro de entrada
     * @return el mismo parámetro de la entrada  en formato string
     */
    public String getValue() {
        return parameters.get(1).getString();
    }


}