package dominio.functions;

import java.text.SimpleDateFormat;
import dominio.model.Function;
import java.util.*;

/**
 * Representa la función DayOfWeek de las posibles funciones que puede ejecutar el usuario
 */
public class DayOfWeek extends Function{
    // Attributes
    /**
     * Representa el int con el número de parámetros que necesita la función
     */
    private final int nParameters;

    /**
     * Constructora
     */
    public DayOfWeek() {
        nParameters = 1;
    }

    /**
     * Retorna el número de parámetros que necesita la función
     * @return int con el número de parámetros que necesita la función
     */
    // Public methods
    public int getNParameters() { return nParameters; }


    /**
     * Función heredada de Function e implementada en DayOfWeek, que retorna el dia de la semana de la fecha indicada
     * @return dia de la semana  en formato string
     */
    public String getValue() {
        try {
            if (parameters.get(1).getString() == null) return "NULL";
            String date = parameters.get(1).getString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date aux = sdf.parse(date);
            sdf.applyPattern("EEEE");
            return sdf.format(aux);
        }
        catch(Exception e) {
            return "#NAME?";
        }

    }

}