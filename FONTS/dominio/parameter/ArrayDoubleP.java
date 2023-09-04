package dominio.parameter;

import java.util.ArrayList;

/**
 * Representa un parámetro de tipo array de Doubles
 */
public class ArrayDoubleP extends Parameter {

    /**
     * Representa un parámetro de tipo Array de Double
     */
    private final ArrayList<Double> a = new ArrayList<>();

    /**
     * Añade el double indicado al parámetro
     * @param a Double que se quiere añadir al ArrayList
     */
    @Override
    public void addDouble(double a) {
        this.a.add(a);
    }

    /**
     * Retrona el parámetro
     * @return Array del parámetro
     */
    @Override
    public ArrayList<Double> getArray() {
        return a;
    }


}
