package dominio.model;


import java.util.*;

/**
 * Representa las referencias hechas en la hoja de cálculo
 */
public class References {

    // Attributes

    /**
     * Representa las celdas referenciadas
     */
    private final ArrayList<Cell> keys;

    /**
     * Representa las celdas que referencian a una celda, de manera que cada ArrayList de celdas que referencian a una celda determinada se encuentra dentro de otro ArrayList,
     * y cada posición de este ArrayList externo corresponde a cada posición del ArrayList de celdas referenciadas
     */
    private final ArrayList<ArrayList<Cell>> values;


    // Constructor

    /**
     * Crea una instancia de referencias en una hoja de cálculo
     */
    public References() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }


    // Methods

    // Pre:
    // Post: devuelve la posicion en la que se encuentra el pair p en keys, si está

    /**
     * Devuelve la posicion en la que se encuentra la celda p en keys, en caso de que no se encuentre devuelve -1
     * @param p Celda de la que queremos saber el indice en las keys de las referencias
     * @return int con el indice de la posicion en la que se encuentra la celda p en keys, o -1 si no se encuentra
     */
    private int keyIndex(Cell p) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(p)) return i;
        }
        return -1;
    }

    // Pre:
    // Post: devuelve la posicion en la que se encuentra al cell c en los values de la key en la posicion keyIndex de keys, en caso de que no se encuentre devuelve -1

    /**
     * Devuelve la posicion en la que se encuentra al cell c en los values de la key en la posicion keyIndex de keys, si existe
     * @param c Celda de la que queremos saber el indice en los values de las referencias
     * @param keyIndex Indice de los values donde queremos saber si está el par
     * @return int con el indice de la posicion en la que se encuentra la celda c en keys, o -1 si no se encuentra
     */
    private int valueIndex(Cell c, int keyIndex) {
        for (int i = 0; i < values.get(keyIndex).size(); i++) {
            if (values.get(keyIndex).get(i).equals(c)) return i;
        }
        return -1;
    }

    /**
     * Retorna todas las celdas que són referenciadas
     * @return Un ArrayList con las celdas referenciadas
     */
    public ArrayList<Cell> getKeys() {
        return this.keys;
    }

    /**
     * Retorna todas las celdas que referencian a las celdas que se encuentran en el conjunto de celdas referenciadas
     * @return Un ArrayList con los ArrayLists de las celdas que referencian a cada celda de las referenciadas
     */
    public ArrayList<ArrayList<Cell>> getValues() {
        return this.values;
    }

    /**
     * Retorna las celdas que referencian a una celda determinada
     * @param p Celda de la cual se quieren buscar las celdas que la referencian
     * @return ArrayList con las celdas que referencian a la celda indicada
     */
    public ArrayList<Cell> getReferencesToCell(Cell p) {
        int i = keyIndex(p);
        if (i != -1) return this.values.get(keyIndex(p));
        return new ArrayList<>();
    }

    // Pre:
    // Post: añade una celda cellReferencing a la lista de celdas que referencian a una celda determinada cellReferenced

    /**
     * Añade a las referencias una referencia determinada de una celda a otra
     * @param cellReferenced Celda que es referenciada
     * @param cellReferencing Celda referencia
     */
    public void addReference(Cell cellReferenced, Cell cellReferencing) {

        int i = keyIndex(cellReferenced);
        if (i == -1) {
            keys.add(cellReferenced);
            ArrayList<Cell> aux = new ArrayList<>(List.of(cellReferencing));
            values.add(aux);
        }
        else {
            int j = valueIndex(cellReferencing, i);
            if (j == -1) values.get(i).add(cellReferencing);
        }

    }


    /**
     * Borra de las referencias una referencia determinada de una celda a otra
     * @param cellReferenced Celda que es referenciada
     * @param cellReferencing Celda referencia
     */
    public void eraseReference(Cell cellReferenced, Cell cellReferencing) {

        int i = keyIndex(cellReferenced);
        if (i != -1) {
            int j = valueIndex(cellReferencing, i);
            if (j != -1) {
                values.get(i).remove(j);
            }
            if (values.get(i).size() == 0) {
                keys.remove(i);
                values.remove(i);
            }
        }

    }

    // Pre:
    // Post: borra todas las referencias que hace la celda cellReferencing,en caso que no haga ninguna referencia no hace nada
    /**
     * Borra de las referencias todas las referencias que hace una celda determinada
     * @param cellReferencing Celda que referencia
     */
    public void eraseAllReferencesOfACell(Cell cellReferencing) {

        for (int i = 0; i < keys.size(); i++) {
            int index = valueIndex(cellReferencing, i);
            if (index != -1) {
                //int j = valueIndex(cellReferencing, index);
                values.get(i).remove(index);

                if (values.get(i).size() == 0) {
                    keys.remove(i);
                    values.remove(i);
                    i--;
                }
            }
        }

    }


    // Pre:
    // Post:
    /**
     * Borra de las referencias una celda referenciada y todas las celdas que la referencian
     * @param cellReferenced Celda que es referenciada
     */
    public void eraseCellReferenced(Cell cellReferenced) {

        int i = keyIndex(cellReferenced);
        if (i != -1) {
            keys.remove(i);
            values.remove(i);
        }

    }


    /**
     * Actualiza las referencias cuando la fila indicada es eliminada
     * @param rowPosition Fila borrada de la hoja de cálculo
     */
    public void rowDeleted(int rowPosition) {

        // primero borramos las keys y values de la rowPosition
        for (int i = 0; i < keys.size(); i++) {
            int x = keys.get(i).getRow();
            if (x == rowPosition) {
                keys.remove(i);
                values.remove(i);
                i--;
            }
            else {
                for (int j = 0; j < values.get(i).size(); j++) {
                    int x2 = values.get(i).get(j).getRow();
                    if (x2 == rowPosition) {
                        values.get(i).remove(j);
                        j--;
                        if (values.get(i).size() == 0) {
                            keys.remove(i);
                            values.remove(i);
                        }
                    }
                }
            }
        }

    }


    /**
     * Actualiza las referencias cuando la columna indicada es eliminada
     * @param colPosition Fila borrada de la hoja de cálculo
     */
    public void columnDeleted(int colPosition) {

        // primero borramos las keys y values de la rowPosition
        for (int i = 0; i < keys.size(); i++) {
            int y = keys.get(i).getColumn();
            if (y == colPosition) {
                keys.remove(i);
                values.remove(i);
                i--;
            }
            else {
                for (int j = 0; j < values.get(i).size(); j++) {
                    int y2 = values.get(i).get(j).getColumn();
                    if (y2 == colPosition) {
                        values.get(i).remove(j);
                        j--;
                    }
                }
            }
        }

    }

    /**
     * Retorna las celdas que referencia una celda determinada
     * @param cell Celda de la cual se quieren coger las celdas que són referenciadas por ésta
     * @return ArrayList con las celdas a las que hace referencia la celda indicada
     */
    public ArrayList<Cell> getReferencesFromCell(Cell cell) {

        ArrayList<Cell> r = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            for (int j = 0; j < values.get(i).size(); j++) {
                if (values.get(i).get(j).equals(cell)) {
                    r.add(keys.get(i));
                    break;
                }
            }
        }
        return r;
    }

    /**
     * Comprueba si se crea una referencia cíclica (a si misma) entre una celda y otra a la que referencia o alguna de las referencias de esta segunda celda (recursivamente)
     * @param original Celda que hace la primera referencia
     * @param reference Celda a la que hace referencia
     * @return true si la celda reference hace una referencia a original, false en caso contrario
     */
    public boolean cyclicReference(Cell original, Cell reference) {
        ArrayList<Cell> cells = new ArrayList<>(getReferencesFromCell(reference));
        for (Cell cell : cells) {
            if (cell == original) return true;
            else {
                if (cyclicReference(original, cell)) return true;
            }
        }
        return false;
    }

}
