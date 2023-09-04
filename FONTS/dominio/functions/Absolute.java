package dominio.functions;
import dominio.model.Function;

/**
 * Representa la función Absolute de las posibles funciones que puede ejecutar el usuario
 */
public class Absolute extends Function {



    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Absolute() {
         nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Methods
    public int getNParameters() { return nParameters; }


    /**
     * Función heredada de Function e implementada en Absolute, que retorna el valor
     * absoluto del número pasado por parámetro
     * @return valor absoluto del parámetro en formato String
     */
    public String getValue() {

        String result = "#NAME?";
        try {
            double parameter = parameters.get(1).getDouble();
            if (parameter < 0) return String.valueOf(parameter * -1);
            else return String.valueOf(parameter);
        }
        catch (NullPointerException e) {
            return result;
        }
    }

}