package dominio.parameter;



/**
 * Representa un parámetro de tipo Double
 */
public class DoubleP extends Parameter {

    /**
     * Representa el double del parámetro
     */
    private Double a;

    /**
     * Añade el double indicado como parámetro
     * @param a Double que se quiere añadir
     */
    @Override
    public void addDouble(double a) {
        this.a = a;
    }


    /**
     * Retrona el parámetro
     * @return Double del parámetro
     */
    @Override
    public Double getDouble() {
        return a;
    }

}
