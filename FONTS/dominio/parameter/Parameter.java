package dominio.parameter;

import java.util.ArrayList;

/**
 * Representa los parámetros de una función
 */
public abstract class Parameter{


    /**
     * Añade un double a un parámetro
     * @param a Double que se quiere añadir
     */

    public void addDouble(double a) {
    }

    /**
     * Añade un string a un parámetro
     * @param a String que se quiere añadir
     */
    public void addString(String a) {
    }

    /**
     * Devuelve un string
     * @return String añadido como parámetro
     */
    public String getString() {
        return null;
    }

    /**
     * Devuelve un vector de doubles
     * @return Array añadido como parámetro
     */
    public ArrayList<Double> getArray() {
        return null;
    }

    /**
     * Devuelve un double
     * @return Double añadido como parámetro
     */
    public Double getDouble() {
        return null;
    }

}
