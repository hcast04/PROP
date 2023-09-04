package dominio.functions;

import dominio.model.Function;

/**
 * Representa la función ReplaceText de las posibles funciones que puede ejecutar el usuario
 */
public class ReplaceText extends Function {

    //Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public ReplaceText() {
        nParameters = 3;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }

    // Public methods

    /**
     * Función heredada de Function e implementada en ReplaceText, que reemplaza la parte replacee de texto por replacer
     * @return el texto con la parte indicada reemplazada en formato string
     */
    public String getValue() {
        String text = parameters.get(1).getString();
        if (text == null) text = String.valueOf(parameters.get(1).getDouble());
        String replacee = parameters.get(2).getString();
        if (replacee == null) replacee = String.valueOf(parameters.get(2).getDouble());
        String replacer = parameters.get(3).getString();
        if (replacer == null) replacer = String.valueOf(parameters.get(3).getDouble());
        return text.replaceAll(replacee,replacer);


    }


}
