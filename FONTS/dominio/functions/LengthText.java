package dominio.functions;
import dominio.model.Function;

/**
 * Representa la función LengthText de las posibles funciones que puede ejecutar el usuario
 */
public class LengthText extends Function {

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public LengthText() {
        nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Public methods
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en LengthText, que devuelve la longitud del texto dado por parámetro
     * @return longitud del texto en formato string
     */
    public String getValue() {
        try {
            return String.valueOf(parameters.get(1).getString().length());
        }
        catch (NullPointerException e) {
            return "#NAME?";
        }
    }
}