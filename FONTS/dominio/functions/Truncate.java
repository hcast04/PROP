package dominio.functions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import dominio.model.Function;

/**
 * Representa la función Truncate de las posibles funciones que puede ejecutar el usuario
 */
public class Truncate extends Function {

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */

    public Truncate() {
        nParameters = 2;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }
    // Public methods

    /**
     * Función heredada de Function e implementada en Truncate, que devuelve el valor truncado
     * del parámetro de la entrada
     * @return devuelve el valor truncado "positions" dígitos en formato string
     */
    public String getValue() {
        Double value = parameters.get(1).getDouble();
        Double aux = parameters.get(2).getDouble();
        int positions = aux.intValue();

        String decimalPart = Double.toString(Math.abs(value));
        int integerPlaces = decimalPart.indexOf('.');
        int decimalPlaces = decimalPart.length() - integerPlaces - 1;

        if (positions > decimalPlaces) positions = decimalPlaces;

        BigDecimal finalvalue;
        if (value > 0) {
           finalvalue = new BigDecimal(String.valueOf(value)).setScale(positions, RoundingMode.FLOOR);
        }
        else {
           finalvalue = new BigDecimal(String.valueOf(value)).setScale(positions, RoundingMode.CEILING);
        }

        return String.valueOf(finalvalue);

    }

}