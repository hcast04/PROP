package dominio.model;

import dominio.parameter.*;

import java.util.*;
import java.lang.String;

/**
 * Representa que en una celda hay una función
 */
public abstract class Function implements CellValueInterface {

    /**
     * Representa la función que ha insertado el usuario
     */
    String formula;

    /**
     * Representa el valor de la función guardado (null cuando no sea válido)
     */
    String value;


    //Atributos
    /**
     * Representa los parámetros necesarios para la función
     */
    protected ArrayList<Parameter> parameters = new ArrayList<>();

    //Methods

    //Getters
    /**
     * Retorna el input que ha hecho el usuario en una celda
     * @return String con el input aplicado en la celda
     */
    @Override
    public String getUserInput() {
        return this.formula;
    }

    /**
     * Si value no esta guardado, lo computa, en caso contrario devuelve el valor guardado
     * @return String con el resultado
     */
    @Override
    public String tryGetValue() {
        if (this.value == null) this.value = getValue();
        return this.value;
    }

    /**
     * Retorna el número de parámetros de una función
     * @return int que indica el número de parámetros de una función
     */
    public abstract int getNParameters();


    /**
     * Retorna el resultado de aplicar la función
     * @return String con el resultado
     */
    public abstract String getValue();

    /**
     * Retorna el tipo de celda en String
     * @return String con el tipo de celda
     */
    @Override
    public String getType() {
        return "function";
    }

    /**
     * Inserta los parámetros a utilizar en la función
     * @param p ArrayList con los parámetros que utiliza la función aplicada
     */
    public void setParameter(ArrayList<Parameter> p) {
        parameters = p;
    }

    //Setters

    /**
     * Inserta la función aplicada en la celda
     * @param f String con la función
     */
    @Override
    public void setFormula(String f) {
        this.value = null;
        this.formula = f;
    }
    /**
     * Inserta el valor del resultado de la función
     * @param s String con el resultado de la función
     */
    public void setValue(String s){ this.value = s;}

    /**
     * Modifica el valor de la función para que sea nulo
     */
    @Override
    public void setNull(){ this.value = null;}

}
