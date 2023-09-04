package dominio.model;


/**
 * Representa que una celda es de tipo valor
 */
public class CellValue implements CellValueInterface {

    /**
     * Representa el valor de la celda insertado por el usuario
     */
    String value;

    /**
     * Crea el tipo de valor de la celda con el valor indicado
     * @param v Valor de la celda que se quiere insertar
     */
    public CellValue(String v) {
        this.value = v;
    }

    /**
     * Inserta el valor indicado a al valor de la celda
     * @param v Valor de la celda que se quiere insertar
     */
    @Override
    public void setValue(String v) {
        this.value = v;
    }

    @Override
    public void setFormula(String f) {
    }

    /**
     * Retorna el input insertado por el usuario en la celda
     * @return String con el input insertado por el usuario
     */
    @Override
    public String getUserInput() {
        return value;
    }


    @Override
    public String tryGetValue() {
        return value;
    }

    /**
     * Retorna el valor de la celda
     * @return String con el valor que se encuentra en la celda
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Retorna el tipo de celda en String
     * @return String con el tipo de celda
     */
    @Override
    public String getType() {
        return "value";
    }


    @Override
    public void setNull() {
    }

}
