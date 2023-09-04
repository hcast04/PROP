package dominio.functions;


import dominio.model.Function;

/**
 * Representa la función ElementExtraction de las posibles funciones que puede ejecutar el usuario
 */
public class ElementExtraction extends Function {

    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public ElementExtraction() {
        nParameters = 2;
    }

    // Methods

    /**
     * Retorna el elemento extraído (indicado en extraction) de la fecha date
     * @param date fecha a la cual extraer el parámetro
     * @param extraction dia mes o año que queremos extraer
     * @return dia mes o año de la fecha  en formato string
     */
    private String getDate(String date, String extraction) {

        if (date != null) {
            int index1 = -1, index2 = -1;
            for (int i = 0; i < date.length(); i++) {
                if (date.charAt(i) == '/' || date.charAt(i) == '-') {
                    if (index1 == -1) index1 = i;
                    else index2 = i;
                }
            }

            if (extraction.equals("day")) {
                return date.substring(0, index1);
            }
            else if (extraction.equals("month")) {
                return date.substring(index1 + 1, index2);
            }
            else {
                return date.substring(index2 + 1);
            }
        }
        else return "#NAME?";

    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    public int getNParameters() { return nParameters; }


    /**
     * Función heredada de Function e implementada en ElementExtraction, que calcula el elemento extraído de la fecha
     * @return el elemento extraído de la fecha en formato string
     */
    public String getValue() {
        try {
            String extraction = parameters.get(1).getString();
            String date = parameters.get(2).getString();
            return getDate(date, extraction);
        }
        catch (NullPointerException e) {
            return "#NAME?";
        }

    }

}