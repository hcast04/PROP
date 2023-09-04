package dominio.parameter;



/**
 * Representa un parámetro de tipo String
 */
public class StringP extends Parameter{


    /**
     * Representa el string del parámetro
     */
    private String a;


    /**
     * Añade el string indicado como parámetro
     * @param a String que se quiere añadir
     */
    @Override
    public void addString(String a) {
        this.a = a;
    }

    /**
     * Retrona el parámetro
     * @return String del parámetro
     */
    @Override
    public String getString() {
        return a;
    }



}
