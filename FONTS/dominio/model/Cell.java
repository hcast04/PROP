package dominio.model;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Representa una celda de la hoja de cálculo
 */
public class Cell {

    //Attributes
    /**
     * Representa la fila en la que se encuentra la celda
     */
    private int row;

    /**
     * Representa la columna en la que se encuentra la celda
     */
    private int column;

    /**
     * Representa el tipo de celda que es (una función o simplemente un valor)
     */
    private CellValueInterface cellValue;

    //Constructora

    /**
     * Crea una celda en la posición indicada
     * @param r Fila de la cual pertenece la celda
     * @param c Columna de la cual pertenece la celda
     */
    public Cell(int r, int c) {
        this.row = r;
        this.column = c;
        this.cellValue = new CellValue("");
    }

    /**
     * Crea una celda igual a la celda indicada
     * @param cell Celda con la cual se quiere crear la nueva celda
     */
    public Cell(Cell cell) {
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.cellValue = cell.getCellValue();
    }


    /**
     * Inserta el tipo de celda indicado en la celda
     * @param cellV Tipo de celda que se quiere establecer en la celda
     */
    public void setCellValue(CellValueInterface cellV) {
        this.cellValue = cellV;
    }

    //Getters

    /**
     * Retorna el tipo de celda
     * @return Tipo de celda
     */
    public CellValueInterface getCellValue() {
        return this.cellValue;
    }

    /**
     * Retorna la fila en la que se encuentra la celda
     * @return Fila de la celda
     */
    public int getRow() {
        return row;
    }

    /**
     * Retorna la columna en la que se encuentra la celda
     * @return Columna de la celda
     */
    public int getColumn() {
        return column;
    }

    //Public Methods

    // Pre:
    // Post: cambia el contenido content de la celda por cont

    /**
     * Inserta en la celda la nueva fila en la que se encuentra
     * @param r Fila en la que se quiere insertar la celda
     */
    public void setRow(int r) {
        this.row = r;
    }

    /**
     * Inserta en la celda la nueva columna en la que se encuentra
     * @param c Columna en la que se quiere insertar la celda
     */
    public void setColumn(int c) {
        this.column = c;
    }

}


/**
 * Representa un comparador
 */
class NaturalOrderComparator implements Comparator<Cell> {

    /**
     * Representa el criterio de comparación: 1 ascendente, 2 descendente
     */
    private final int comparator;

    /**
     * Crea un comparador con parametro comparador
     * @param comparator representa el criterio de ordenación
     */
    public NaturalOrderComparator(Integer comparator) {
        this.comparator = comparator;
    }

    /**
     * Compara dos strings, el que tenga mas digitos gana, en caso de ser iguales el que tenga el valor mas alto gana
     * @return 0 si son iguales, 1 si a es mayor que b, -1 si a es menor que b
     */
    int compareRight(String a, String b) {
        int bias = 0, ia = 0, ib = 0;

        // The longest run of digits wins. That aside, the greatest
        // value wins, but we can't know that it will until we've scanned
        // both numbers to know that they have the same magnitude, so we
        // remember it in BIAS.
        for (; ; ia++, ib++) {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);

            if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
                return bias;
            }
            if (!Character.isDigit(ca)) {
                return -1;
            }
            if (!Character.isDigit(cb)) {
                return +1;
            }
            if (ca == 0 && cb == 0) {
                return bias;
            }

            if (bias == 0) {
                if (ca < cb) {
                    bias = -1;
                } else if (ca > cb) {
                    bias = 1;
                }
            }
        }
    }
    /**
     * Compara el valor de 2 celdas, ademas define la precision a 3 digitos, las celdas vacias al final
     * @return 0 si son los valores son iguales, 1 si a es mayor que b, -1 si a es menor que b
     */
    public int compare(Cell o1, Cell o2) {
        String a;
        if (o1.getCellValue().getType().equals("function") || o1.getCellValue().getValue().contains(".")) {

            try {
                double aux = Double.parseDouble(o1.getCellValue().getValue());
                DecimalFormat df = new DecimalFormat("#.###");
                a = df.format(aux);
            }
            catch(NumberFormatException e) {
                a = o1.getCellValue().getValue();
            }
        }
        else a = o1.getCellValue().getValue();

        String b;
        if (o2.getCellValue().getType().equals("function") || o2.getCellValue().getValue().contains(".")) {
            try {
                double aux = Double.parseDouble(o2.getCellValue().getValue());
                DecimalFormat df = new DecimalFormat("#.###");
                b = df.format(aux);
            }
            catch(NumberFormatException e) {
                b = o2.getCellValue().getValue();
            }

        }
        else b = o2.getCellValue().getValue();
        //nulls last

        if (this.comparator == 1) {
            if (a.isEmpty() && b.isEmpty()) return 0;
            else if (a.isEmpty()) return Integer.MAX_VALUE;
            else if (b.isEmpty()) return Integer.MIN_VALUE;
        }



        int ia = 0, ib = 0;   //char at i
        int nza = 0, nzb = 0; //numero zeroes
        char ca, cb;

        while (true) {
            // Only count the number of zeroes leading the last number compared
            nza = nzb = 0;

            ca = charAt(a, ia);
            cb = charAt(b, ib);

            // skip over leading spaces or zeros
            while (Character.isSpaceChar(ca) || ca == '0') {
                if (ca == '0') {
                    nza++;
                } else {
                    // Only count consecutive zeroes
                    nza = 0;
                }

                ca = charAt(a, ++ia);
            }

            while (Character.isSpaceChar(cb) || cb == '0') {
                if (cb == '0') {
                    nzb++;
                } else {
                    // Only count consecutive zeroes
                    nzb = 0;
                }

                cb = charAt(b, ++ib);
            }

            // Process run of digits
            if (Character.isDigit(ca) && Character.isDigit(cb)) {
                int bias = compareRight(a.substring(ia), b.substring(ib));
                if (bias != 0) {
                    return bias;
                }
            }

            if (ca == 0 && cb == 0) {
                // The strings compare the same. Perhaps the caller
                // will want to call strcmp to break the tie.
                return compareEqual(a, b, nza, nzb);
            }
            if (ca < cb) {
                return -1;
            }
            if (ca > cb) {
                return +1;
            }

            ++ia;
            ++ib;
        }
    }


    static char charAt(String s, int i) {
        return i >= s.length() ? 0 : s.charAt(i);
    }

    static int compareEqual(String a, String b, int nza, int nzb) {
        if (nza - nzb != 0)
            return nza - nzb;

        if (a.length() == b.length())
            return a.compareTo(b);

        return a.length() - b.length();
    }
}

