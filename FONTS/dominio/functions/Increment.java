package dominio.functions;

import java.math.BigDecimal;
import dominio.model.Function;

/**
 * Representa la función Increment de las posibles funciones que puede ejecutar el usuario
 */
public class Increment extends Function{

    //Atributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public Increment() {
        nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Methods
    public int getNParameters() { return nParameters; }

    /**
     * Función heredada de Function e implementada en Increment, que devuelve el parámetro de entrada incrementado
     * @return parámetro de entrada incrementado en uno en formato string
     */
    public String getValue(){
        String result = "#NAME?";
        try {
            double aux = parameters.get(1).getDouble();
            if(aux >= 0) {
                return String.valueOf(++aux);
            }
            else {
                BigDecimal a = BigDecimal.valueOf(aux).add(BigDecimal.valueOf(1));
                return String.valueOf(a);
            }

        }
        catch (NullPointerException e) {
            return result;
        }
    }
}
