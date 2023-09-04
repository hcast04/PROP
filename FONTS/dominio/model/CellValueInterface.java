package dominio.model;

/**
 * Interfaz que representa de que tipo es una celda
 */
public interface CellValueInterface {

    /**
     * Retorna el valor de la celda para CellValue, y la fórmula que indica el usuario para Function
     * @return String con el input que ha hecho el usuario en la celda
     */
    String getUserInput();

    /**
     * Retorna el valor de la celda, y en caso de que sea una funciñón y sea nulo, lo calcula y luego lo retorna
     * @return String con el valor de la celda
     */
    String tryGetValue();

    /**
     * Retorna el valor de la celda para CellValue, y el resultado de la función para Function
     * @return String con el valor de la celda
     */
    String getValue();

    /**
     * Inserta un valor en una celda de tipo Value
     * @param v String con el valor que se quiere insertar en la celda
     */
    void setValue(String v);

    /**
     * Inserta una función determinada en una celda de tipo Function
     * @param f String con la función que se quiere insertar en la celda
     */
    void setFormula(String f);

    /**
     * Retorna el tipo de celda: "value" para las celdas con solo un valor y "function" para las celdas con funciones
     */
    String getType();

    /**
     * Actualiza el valor para ponerlo a nulo cuando no es válido en una función
     */
    void setNull();

}
