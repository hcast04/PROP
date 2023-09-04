package dominio.model;


import dominio.auxiliarclasses.MyException;
import dominio.parameter.*;
import dominio.auxiliarclasses.Pair;


import java.lang.reflect.InvocationTargetException;
import static dominio.auxiliarclasses.ReferenceConverter.*;
import java.util.*;


/**
 * Representa una hoja de cálculo del documento
 */
public class Sheet {

    // Attributes

    /**
     * Representa el nombre de la hoja
     */
    private String name;

    /**
     * Representa el número máximo de filas que puede tener una hoja
     */
    private final int maxRow = 255;

    /**
     * Representa el número máximo de columnas que puede tener una hoja
     */
    private final int maxColumn = 255;

    /**
     * Representa el tamaño por defecto de una hoja
     */
    private final int sheetSize = 26;

    /**
     * Representa el conjunto de celdas que tiene la hoja
     */
    private final ArrayList<ArrayList<Cell>> cjt_cells;

    /**
     * Representa las referencias que hacen las celdas a otras al aplicar funciones con referencias
     */
    private final References references;

    // Constructor

    /**
     * Crea una hoja con el nombre especificado y con el número de celdas por defecto
     * @param name Nombre del documento.
     */
    public Sheet(String name)  {
        this.name = name;
        cjt_cells = new ArrayList<>();
        references = new References();
        iniCells(sheetSize, sheetSize);
    }

    /**
     * Crea una hoja con el nombre y con el número de filas y columnas especificados
     * @param name Nombre del documento.
     */
    public Sheet(String name, int rows, int columns) {
        this.name = name;
        cjt_cells = new ArrayList<>();
        references = new References();
        iniCells(rows,columns);
    }

    /**
     * Inicia la hoja con el número de filas y columnas especificado
     * @param nRow Número de filas que tendrá la hoja.
     * @param nCol Número de columnas que tendrá la hoja.
     */
    private void iniCells(int nRow, int nCol ) {
        for (int i = 0; i < nRow; i++) {
            ArrayList<Cell> aux = new ArrayList<>(nCol);
            for (int j = 0; j < nCol; j++) {
                aux.add(new Cell(nRow - i - 1, j));
            }
            cjt_cells.add(0, aux);
        }
    }



    //Getters

    /**
     * Retorna las referencias hechas en la hoja de cálculo
     * @return Retorna una instancia de la clase References, que representa las referencias hechas en la hoja actual
     */
    public References getReferences() {
        return this.references;
    }

    /**
     * Retorna el nombre de la hoja
     * @return Retorna el nombre actual de la hoja
     */
    public String getName() {return this.name;}

    /**
     * Retorna el conjunto de celdas de la hoja de cálculo
     * @return ArrayList de filas donde cada fila tiene un ArrayLists de celdas que representa las columnas
     */
    public ArrayList<ArrayList<Cell>> getCjt_cells() {return cjt_cells; }


    /**
     * Retorna la celda de fila y columna indicadas
     * @param r Número de fila de la cual se quiere obtener la celda
     * @param c Número de columna de la cual se quiere obtener la celda
     * @return Celda en la posición indicada
     */
    public Cell getCell(int r, int c) {
        return cjt_cells.get(r).get(c);
    }

    /**
     * Retorna el número máximo de filas permitido
     * @return Retorna el número máximo de filas posible
     */
    public int getMaxRow() {
        return this.maxRow;
    }

    /**
     * Retorna el número máximo de columnas permitido
     * @return Retorna el número máximo de columnas posible
     */
    public int getMaxColumn() {
        return this.maxColumn;
    }


    // Public Methods

    /**
     * Inserta un valor en la celda indicada
     * @param r Número de fila de la cual se quiere modificar la celda
     * @param c Número de columna de la cual se quiere modificar la celda
     * @param v Valor que se que se quiere insertar en la celda
     */
    public void setCell(int r, int c, String v) {
        cjt_cells.get(r).get(c).getCellValue().setValue(v);
    }

    /**
     * Inserta un valor en la celda indicada
     * @param r Número de fila de la cual se quiere modificar la celda
     * @param c Número de columna de la cual se quiere modificar la celda
     */
    public void setCell(int r, int c, CellValueInterface cell) {
        cjt_cells.get(r).get(c).setCellValue(cell);
    }


    /**
     * Inserta una nueva referencia al conjunto de referencias
     * @param referenced Celda que es referenciada
     * @param referencing Celda que referencia
     */
    public void setReferences(Cell referenced, Cell referencing) {
        references.addReference(referenced, referencing);
    }

    /**
     * Inserta el nuevo nombre a la hoja de cálculo
     * @param name String con el nuevo nombre que se quiere poner a la hoja de cálculo
     */
    public void setName(String name) {
        this.name = name;
    }



    // Pre: No hemos alcanzado maxRow rows, a esta entre 0 y el número de rows actual
    // Post: Añade una fila en la fila a, actualizamos valor row de las celdas y las referencias

    /**
     * Inserta una nueva fila vacía en la posición indicada, siempre y cuando no se supere el límite máximo de filas permitidas, actualiza los valores y referencias
     * @param a Posición en la que se quiere insertar la nueva fila
     */
    public void addRow(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int size = cjt_cells.get(0).size();
        for (int i = a; i < cjt_cells.size(); i++) {
            for (int j = 0; j < size; j++) {
                cjt_cells.get(i).get(j).setRow(cjt_cells.get(i).get(j).getRow() + 1);
            }
        }
        ArrayList<Cell> aux = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            aux.add(new Cell(a, i));
        }
        cjt_cells.add(a, aux);

        //actualizamos las referencias y los valores
        updateReferences(1, 1, a);
        updateValues();
    }


    //Pre: La fila a existe
    //Post: Fila a eliminada, actualizamos valor row de las celdas y las referencias, actualiza los valores y referencias

    /**
     * Borra la fila indicada en caso de que exista, siempre y cuando no sea la única fila de la hoja de cálculo, actualiza los valores y referencias
     * @param a Número de fila que se quiere borrar
     */
    public void deleteRow(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //actualizamos las referencias
        updateReferences(1, 2, a);
        int size = cjt_cells.get(0).size();
        references.rowDeleted(a);
        for (int i = a + 1; i < cjt_cells.size(); i++) {
            for (int j = 0; j < size; j++) {
                cjt_cells.get(i).get(j).setRow(cjt_cells.get(i).get(j).getRow() - 1);
            }
        }

        cjt_cells.remove(a);
        //actualizamos los valores
        updateValues();


    }


    //Pre: No se ha alcanzado maxColumn columnas, a esta entre 0 y el número de columnas actual, actualiza los valores y referencias
    //Post: Añade una columna en la columna a, actualizamos valor col de las celdas y las referencias

    /**
     * Inserta una nueva columna vacía en la posición indicada, siempre y cuando no se supere el límite máximo de columnas permitidas, actualiza los valores y referencias
     * @param a Posición en la que se quiere insertar la nueva columna
     */
    public void addColumn(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        int size = cjt_cells.size();
        for (int i = a; i < cjt_cells.get(0).size(); i++) {
            for (int j = 0; j < size; j++) {
                cjt_cells.get(j).get(i).setColumn(cjt_cells.get(j).get(i).getColumn() + 1);
            }
        }
        for (int i = 0; i < size; i++) {
            cjt_cells.get(i).add(a, new Cell(i, a));
        }
        //actualizamos las referencias y los valores
        updateReferences(2, 1, a);
        updateValues();
    }


    //Pre: La columna a existe
    //Post: Columna a eliminada, actualizamos valor col de las celdas y las referencias

    /**
     * Borra la columna indicada en caso de que exista, siempre y cuando no sea la única columna de la hoja de cálculo, actualiza los valores y referencias
     * @param a Número de columna que se quiere borrar
     */
    public void deleteColumn(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //actualizamos las referencias
        updateReferences(2, 2, a);
        int size = cjt_cells.size();
        references.columnDeleted(a);
        for (int i = a+1; i < cjt_cells.get(0).size(); i++) {
            for (int j = 0; j < size; j++) {
                cjt_cells.get(j).get(i).setColumn(cjt_cells.get(j).get(i).getColumn() - 1);
            }
        }
        //Recorremos todas las rows y borramos la celda a de cada row
        for (ArrayList<Cell> cjt_cell : cjt_cells) {
            cjt_cell.remove(a);
        }

        //actualizamos los valores
        updateValues();
    }


    //Pre: Las celdas cells existen
    //Post: Elimina el contenido de las celdas cells, actualizamos referencias si es necesario

    /**
     * Elimina el contenido del bloque de celdas especificado siempre y cuando éstas existan dentro de la hoja de cálculo, actualiza los valores y referencias
     * @param cells ArrayList con las celdas de las cuales se quiere borrar su contenido
     */
    public void deleteContBloq(ArrayList<Cell> cells) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Cell aux : cells) {
            int a = aux.getRow();
            int b = aux.getColumn();

            CellValue emptyCell = new CellValue("");
            setCell(a, b, emptyCell);
        }
        //actualizamos los valores
        updateValues();
    }


    //Pre: Las celdas copy y paste existen, ambas arrays tienen el mismo tamaño
    //Post: Copia el contenido de las celdas copy en las celdas paste, actualizamos referencias si es necesario

    /**
     * Copia el contenido de las celdas a copiar y lo inserta en las celdas a pegar en el mismo orden que se han recorrido los dos ArrayLists, siempre y cuando estos ArrayLists tengan el mismo tamaño
     *      y todas las celdas de cada uno de los ArrayLists existan dentro de la hoja de cálculo, actualiza los valores y las referencias
     * @param copy ArrayList con las celdas de las cuales se quiere copiar su contenido
     * @param paste ArrayList con las celdas en las cuales se quiere pegar el contenido de las celdas copiadas
     */
    public void copyContBloq(ArrayList<Cell> copy, ArrayList<Cell> paste) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        for (int i = 0; i < copy.size(); i++) {
            int x = paste.get(i).getRow();
            int y = paste.get(i).getColumn();
            if (copy.get(i).getCellValue().getType().equals("function")) {
                ArrayList<Cell> referenceCopy = references.getReferencesFromCell(copy.get(i));
                for (Cell cell : referenceCopy) references.addReference(cell, paste.get(i));
            }
            setCell(x, y, copy.get(i).getCellValue());
        }
        updateValues();
    }


    //Pre: Las celdas copy y move existen, ambas arrays tienen el mismo tamaño
    //Post: Mueve el contenido de las celdas copy en las celdas move, actualizamos referencias si es necesario

    /**
     * Mueve el contenido de las celdas a mover y lo inserta en las nuevas celdas en el mismo orden que se han recorrido los dos ArrayLists, siempre y cuando estos ArrayLists tengan el mismo tamaño
     *             y todas las celdas de cada uno de los ArrayLists existan dentro de la hoja de cálculo. actualiza los valores y las referencias
     * @param move ArrayList con las celdas de las cuales se quiere mover su contenido
     * @param where ArrayList con las nuevas celdas que contendrán el contenido de las celdas que se quieren mover
     */
    public void moveContBloq(ArrayList<Cell> move, ArrayList<Cell> where) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {


        ArrayList<Cell> aux = new ArrayList<>();

        for (int i = 0; i < move.size(); i++) {
            Cell auxCell = new Cell(move.get(i));
            if (move.get(i).getCellValue().getType().equals("function")) {
                ArrayList<Cell> referenceCopy = references.getReferencesFromCell(move.get(i));
                for (int j = 0; j < referenceCopy.size(); j++) references.addReference(referenceCopy.get(j), where.get(i));
            }
            aux.add(auxCell);
        }

        deleteContBloq(move);


        for (int i = 0; i < move.size(); i++) {
            int x = where.get(i).getRow();
            int y = where.get(i).getColumn();
            setCell(x, y, aux.get(i).getCellValue());
        }
        updateValues();
    }


    //Pre: Las celdas sort existen
    //Post: Ordena el contenido de las celdas sort ascendentemente si criterio = 1, o descendentemente si criterio = 2

    /**
     * Ordena las celdas indicadas, siempre y cuando existan, por su contenido dentro del mismo bloque seleccionado, siguiendo un criterio: 1 para ordenar de manera creciente, 2 para ordenar de manera decreciente
     * Las celdas que eran funciones pasan a ser cellValues y las referencias se eliminan
     * @param sort ArrayList con las celdas de las cuales se quiere ordenar su contenido
     * @param criterio Tipo de criterio a seguir para ordenar las celdas indicadas
     */
    public void sortContBloq(ArrayList<Cell> sort, int criterio) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ArrayList<Cell> aux = new ArrayList<>();

        int firstColumn = sort.get(0).getColumn();
        int lastColumn = sort.get(sort.size() - 1).getColumn();
        int firstRow = sort.get(0).getRow();
        int lastRow = sort.get(sort.size() - 1).getRow();


        for (int i = 0; i < sort.size(); i++) {
            CellValue cellvalue;
            if (sort.get(i).getCellValue().getType().equals("function")) cellvalue = new CellValue(String.valueOf(sort.get(i).getCellValue().tryGetValue()));
            else cellvalue = new CellValue(String.valueOf(sort.get(i).getCellValue().getValue()));
            Cell auxCell = new Cell(sort.get(i));
            auxCell.setCellValue(cellvalue);
            aux.add(auxCell);
        }
        if (criterio == 1) aux.sort(new NaturalOrderComparator(1));
        else if (criterio == 2) aux.sort(new NaturalOrderComparator(2).reversed());

        int n = 0;
        for (int i = firstRow; i < lastRow + 1; i++) {
            for (int j = firstColumn; j < lastColumn + 1; j++) {
                cjt_cells.get(i).get(j).setCellValue(aux.get(n).getCellValue());    // no deberia cambiar row y col de cjt_cells
                ++n;
            }
        }
        updateValues();

    }


    //Pre: Las celdas cells existen
    //Post: Devuelve una arraylist con las celdas donde el String buscar aparezca de las celdas cells
    /**
     * Retorna las celdas que tienen un contenido determinado
     * @return ArrayList con las celdas que contienen el contenido especificado, siempre y cuando las celdas indicadas existen
     * @param cells ArrayList con las celdas de las cuales se quiere buscar un contenido específico
     * @param buscar String con el contenido que se quiere buscar
     */
    public ArrayList<Cell> searchContBloq(ArrayList<Cell> cells, String buscar) {
        ArrayList<Cell> value = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.getCellValue().getType().equals("function")) {
                if (cell.getCellValue().tryGetValue().contains(buscar)) value.add(cell);
            }
            else {
                if (cell.getCellValue().getValue().contains(buscar)) value.add(cell);
            }

        }
        return value;
    }


    //Pre: Las celdas cells existen
    //Post: Reemplaza el contenido remplacee por remplacer de las celdas cells
    /**
     * Busca las celdas que contienen el contenido que se quiere reemplazar y lo reemplaza por el contenido indicado, actualiza los valores y las referencias
     * @param cells ArrayList con las celdas de las cuales se quiere reemplazar un contenido específico
     * @param remplacee String con el contenido que se quiere reemplazar
     * @param remplacer String con el contenido por el cual se quiere reemplazar
     */
    public void replaceContBloq(ArrayList<Cell> cells, String remplacee, String remplacer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ArrayList<Cell> aux = searchContBloq(cells, remplacee);
        for (int i = 0; i < aux.size(); i++) {
            String r;
            if (aux.get(i).getCellValue().getType().equals("function")) {
                r = aux.get(i).getCellValue().tryGetValue();
               // references.eraseAllReferencesOfACell(aux.get(i));
            }
            else r = aux.get(i).getCellValue().getValue();
            aux.get(i).setCellValue(new CellValue(r.replace(remplacee, remplacer)));
        }
        updateValues();
    }

    /**
     * Retorna los parámetros de una función aplicada a una celda o indicadores de errores para saber que ha ocurrido
     * @param input String con la función que se quiere aplicar en una celda
     * @param cell Celda en la cual se quiere aplicar la función
     * @return ArrayList con los parámetros con los cuales se quiere aplicar la función a la celda
     */
    private ArrayList<Parameter> parameterConverter(String input, Cell cell) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<Parameter> aux = new ArrayList<>();
        String function = "nofunction";
        StringBuilder func = new StringBuilder();
        try {
            if (input.charAt(0) == '=') {
                input = stringTrimer(input);
                if (input.contains("#REF!")) {
                    return errorParameterConverter("Ref error");
                } else {
                    ArrayDoubleP arrayDoublep = new ArrayDoubleP();
                    boolean functionWritten = false;
                    StringBuilder parameter = new StringBuilder();
                    if (Character.isLowerCase(input.charAt(1))) {
                        boolean correct = true;
                        for (int i = 1; i < input.length(); i++) {
                            if (input.charAt(i) != '(' && !functionWritten) func.append(input.charAt(i));
                            else if (input.charAt(i) == '(') {
                                function = String.valueOf(func);
                                functionWritten = true;
                                StringP functionParameter = new StringP();
                                functionParameter.addString(function);
                                aux.add(functionParameter);
                                try {
                                    GetFunctionFactory.getInstance(function);
                                } catch (NullPointerException e) {
                                    return errorParameterConverter("incorrectfunction");
                                }

                            } else {
                                if (input.charAt(i) == '[') {
                                    i += 1;
                                    while (i < input.length() && input.charAt(i) != ']') {
                                        parameter.append(input.charAt(i));
                                        i++;
                                    }
                                    StringP stringp = new StringP();
                                    stringp.addString(String.valueOf(parameter));
                                    aux.add(stringp);
                                    parameter = new StringBuilder();
                                } else if (input.charAt(i) == ';' || input.charAt(i) == ')') {
                                    if (parameter.length() == 0) ;      // para evitar error de string
                                    else {
                                        try {
                                            if (arrayDoublep.getArray().size() >= 1) {      // estamos en un arraylist
                                                arrayDoublep.addDouble(Double.parseDouble(String.valueOf(parameter)));
                                                aux.add(arrayDoublep);
                                                arrayDoublep = new ArrayDoubleP();
                                            } else {
                                                DoubleP doublep = new DoubleP();
                                                doublep.addDouble(Double.parseDouble(String.valueOf(parameter)));
                                                aux.add(doublep);
                                            }
                                        } catch (NumberFormatException e) {
                                            return errorParameterConverter(function + "N");
                                        }
                                        parameter = new StringBuilder();
                                    }
                                } else if (input.charAt(i) == ' ') {
                                } else if (input.charAt(i) == ',') {
                                    try {
                                        arrayDoublep.addDouble(Double.parseDouble(String.valueOf(parameter)));
                                        parameter = new StringBuilder();
                                    } catch (NumberFormatException e) {
                                        return errorParameterConverter(function + "N");
                                    }
                                } else if (Character.isUpperCase(input.charAt(i))) {
                                    if (parameter.length() != 0) {
                                        return errorParameterConverter(function + "N");
                                    }
                                    StringBuilder referenciaLetter = new StringBuilder();
                                    int row;
                                    int col;
                                    try {
                                        while (!Character.isDigit(input.charAt(i))) {
                                            referenciaLetter.append(input.charAt(i));
                                            i++;
                                        }
                                        col = letterToNumber(String.valueOf(referenciaLetter));

                                        row = 0;
                                        while (input.charAt(i) != ')' && input.charAt(i) != ';' && input.charAt(i) != ',' && input.charAt(i) != ':') {
                                            row *= 10;
                                            row += Integer.parseInt(String.valueOf(input.charAt(i)));
                                            i++;
                                        }
                                        if (row < 0 || row >= cjt_cells.size() || col < 0 || col >= cjt_cells.get(0).size() || references.cyclicReference(cell, getCell(row, col)) || getCell(row, col) == cell) {
                                            return errorParameterConverter(function + "R");
                                        }
                                    } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                                        return errorParameterConverter(function + "R");
                                    }

                                    double valorReferencia = 0;
                                    String valorReferenciaS = "";
                                    boolean isString = false;
                                    setReferences(getCell(row, col), cell);
                                    try {
                                        valorReferencia = Double.parseDouble(getCell(row, col).getCellValue().getValue()); //get value

                                    } catch (NumberFormatException e) {
                                        valorReferenciaS = getCell(row, col).getCellValue().getValue();
                                        isString = true;
                                    }

                                    if (input.charAt(i) == ',') {
                                        if (isString) {
                                            correct = false;
                                        } else arrayDoublep.addDouble(valorReferencia);

                                    } else if (input.charAt(i) == ';' || input.charAt(i) == ')') {
                                        if (arrayDoublep.getArray().size() >= 1) {      // estamos en un arraylist
                                            if (isString) {
                                                correct = false;
                                            } else {
                                                arrayDoublep.addDouble(valorReferencia);
                                                aux.add(arrayDoublep);
                                                arrayDoublep = new ArrayDoubleP();
                                            }
                                        } else {
                                            if (!isString) {
                                                DoubleP doublep = new DoubleP();
                                                doublep.addDouble(valorReferencia);
                                                aux.add(doublep);
                                            } else {
                                                StringP stringp = new StringP();
                                                stringp.addString(valorReferenciaS);
                                                aux.add(stringp);
                                            }
                                        }
                                    } else if (input.charAt(i) == ':') {

                                        i++;
                                        StringBuilder referenciaLetter2 = new StringBuilder();
                                        int row2;
                                        int col2;
                                        try {
                                            while (!Character.isDigit(input.charAt(i))) {
                                                referenciaLetter2.append(input.charAt(i));
                                                i++;
                                            }
                                            col2 = letterToNumber(String.valueOf(referenciaLetter2));
                                            row2 = 0;
                                            while (input.charAt(i) != ')' && input.charAt(i) != ';' && input.charAt(i) != ',') {
                                                row2 *= 10;
                                                row2 += Integer.parseInt(String.valueOf(input.charAt(i)));
                                                i++;
                                            }
                                            if (row2 < 0 || row2 >= cjt_cells.size() || col2 < 0 || col2 >= cjt_cells.get(0).size() || references.cyclicReference(cell, getCell(row, col)) || getCell(row, col) == cell) {
                                                return errorParameterConverter(function + "R");
                                            }
                                        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                                            return errorParameterConverter(function + "R");
                                        }

                                        try {
                                            if (row == row2 && col == col2) {
                                                DoubleP doublep = new DoubleP();
                                                setReferences(getCell(row, col), cell);
                                                double valorReferencia2 = Double.parseDouble(getCell(row, col).getCellValue().getValue());
                                                doublep.addDouble(valorReferencia2);

                                            } else {
                                                if (row2 > row || col2 > col) {
                                                    if (row == row2) {
                                                        for (int i1 = col; i1 <= col2; i1++) {
                                                            setReferences(getCell(row, i1), cell);
                                                            double valorReferencia2 = Double.parseDouble(getCell(row, i1).getCellValue().getValue()); //get value
                                                            arrayDoublep.addDouble(valorReferencia2);
                                                        }
                                                    } else if (col == col2) {
                                                        for (int i1 = row; i1 <= row2; i1++) {
                                                            setReferences(getCell(i1, col), cell);
                                                            double valorReferencia2 = Double.parseDouble(getCell(i1, col).getCellValue().getValue()); //get value
                                                            arrayDoublep.addDouble(valorReferencia2);

                                                        }
                                                    } else {

                                                        for (int i1 = row; i1 <= row2; i1++) {
                                                            for (int j1 = col; j1 <= col2; j1++) {
                                                                setReferences(getCell(i1, j1), cell);
                                                                double valorReferencia2 = Double.parseDouble(getCell(i1, j1).getCellValue().getValue()); //get value
                                                                arrayDoublep.addDouble(valorReferencia2);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (row == row2) {
                                                        for (int i1 = col2; i1 <= col; i1++) {
                                                            setReferences(getCell(row, i1), cell);
                                                            double valorReferencia2 = Double.parseDouble(getCell(row, i1).getCellValue().getValue()); //get value
                                                            arrayDoublep.addDouble(valorReferencia2);
                                                        }
                                                    } else if (col == col2) {
                                                        for (int i1 = row2; i1 <= row; i1++) {
                                                            setReferences(getCell(i1, col), cell);
                                                            double valorReferencia2 = Double.parseDouble(getCell(i1, col).getCellValue().getValue()); //get value
                                                            arrayDoublep.addDouble(valorReferencia2);

                                                        }
                                                    } else {
                                                        for (int i1 = row2; i1 <= row; i1++) {
                                                            for (int j1 = col2; j1 <= col; j1++) {
                                                                setReferences(getCell(i1, j1), cell);
                                                                double valorReferencia2 = Double.parseDouble(getCell(i1, j1).getCellValue().getValue()); //get value
                                                                arrayDoublep.addDouble(valorReferencia2);
                                                            }
                                                        }
                                                    }
                                                }

                                                if (input.charAt(i) == ',') {
                                                } else if (input.charAt(i) == ';' || input.charAt(i) == ')') {
                                                    aux.add(arrayDoublep);
                                                    arrayDoublep = new ArrayDoubleP();
                                                }
                                            }
                                        } catch (NumberFormatException e) {
                                            correct = false;
                                        }
                                    }
                                } else {
                                    parameter.append(input.charAt(i));
                                }
                            }
                        }
                        if (!correct) {
                            aux = new ArrayList<>();
                            StringP functionParameter = new StringP();
                            functionParameter.addString(function + "R");
                            aux.add(functionParameter);
                        }
                    }
                    else {
                        try {
                            double res = 0.0;
                            StringP stringP = new StringP();
                            boolean isNumber = true;
                            boolean isOperation = false;
                            for (int i = 1; i < input.length(); i++) {
                                if (input.charAt(i) == '+') {
                                    isOperation = true;
                                    String nextVal = nextVal(input, i, cell);
                                    if (nextVal.equals("Error")) {
                                        aux = new ArrayList<>();
                                        stringP.addString("Ref error");
                                        aux.add(stringP);
                                        return aux;
                                    } else if (!isNumeric(nextVal)) {
                                        stringP.addString(nextVal);
                                        isNumber = false;
                                        break;

                                    } else {
                                        res += Double.parseDouble(nextVal);
                                    }
                                } else if (input.charAt(i) == '-') {
                                    isOperation = true;
                                    String nextVal = nextVal(input, i, cell);
                                    if (nextVal.equals("Error")) {
                                        aux = new ArrayList<>();
                                        stringP.addString("Ref error");
                                        aux.add(stringP);
                                        return aux;
                                    } else if (!isNumeric(nextVal)) {
                                        stringP.addString(nextVal);
                                        isNumber = false;
                                        break;
                                    } else {
                                        res -= Double.parseDouble(nextVal);
                                    }

                                } else if (input.charAt(i) == '*') {
                                    isOperation = true;
                                    String nextVal = nextVal(input, i, cell);
                                    if (nextVal.equals("Error")) {
                                        aux = new ArrayList<>();
                                        stringP.addString("Ref error");
                                        aux.add(stringP);
                                        return aux;
                                    } else if (!isNumeric(nextVal)) {
                                        stringP.addString(nextVal);
                                        isNumber = false;
                                        break;
                                    } else {
                                        res *= Double.parseDouble(nextVal);
                                    }
                                } else if (input.charAt(i) == '/') {
                                    isOperation = true;
                                    String nextVal = nextVal(input, i, cell);
                                    if (nextVal.equals("Error")) {
                                        aux = new ArrayList<>();
                                        stringP.addString("Ref error");
                                        aux.add(stringP);
                                        return aux;
                                    } else if (!isNumeric(nextVal)) {
                                        stringP.addString(nextVal);
                                        isNumber = false;
                                        break;

                                    } else {
                                        res /= Double.parseDouble(nextVal);
                                    }
                                } else if (i == 1) {
                                    String nextVal = nextVal(input, 1, cell);
                                    if (nextVal.equals("Error")) {
                                        aux = new ArrayList<>();
                                        stringP.addString("Ref error");
                                        aux.add(stringP);
                                        return aux;
                                    } else if (!isNumeric(nextVal)) {
                                        stringP.addString(nextVal);
                                        isNumber = false;
                                        if (input.contains("+") || input.contains("-") || input.contains("*") || input.contains("/")) {
                                            isOperation = true;
                                        }
                                        break;
                                    } else {
                                        res = Double.parseDouble(nextVal);
                                    }
                                }
                            }
                            StringP functionParameter = new StringP();
                            functionParameter.addString("identity");
                            aux.add(functionParameter);
                            if(isNumber) {
                                String resString = Double.toString(res);
                                stringP.addString(resString);
                            }
                            else if (isOperation) {
                                aux = new ArrayList<>();
                                stringP.addString("Ref error");
                                aux.add(stringP);
                                return aux;
                            }
                            aux.add(stringP);
                            return aux;
                        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                            aux = new ArrayList<>();
                            StringP stringP = new StringP();
                            stringP.addString("Ref error");
                            aux.add(stringP);
                            return aux;
                        }
                    }
                }
            } else {
                StringP stringP = new StringP();
                stringP.addString(function);
                aux.add(stringP);
                stringP = new StringP();
                stringP.addString(input);
                aux.add(stringP);
                return aux;
            }
        }
        catch(StringIndexOutOfBoundsException e){
            return errorParameterConverter("Ref error");
        }


        if (aux.size() == 0) {
                StringP falseFunction = new StringP();
                falseFunction.addString("incorrectfunction");
                aux.add(falseFunction);
        }
        if (input.charAt(input.length() - 1) != ')' || aux.size() == 1) {
            aux = new ArrayList<>();
            StringP functionParameter = new StringP();
            if (!function.equals("nofunction")) {
                functionParameter.addString(function + "N");
                aux.add(functionParameter);
            }
            else {
                functionParameter.addString("incorrectfunction");
                aux.add(functionParameter);
            }
            return aux;
        }
        return aux;


    }

    private String nextVal(String input, int pos, Cell cell) {
        StringBuilder sB = new StringBuilder();
        if(pos != 1) ++pos;
        if(Character.isUpperCase(input.charAt(pos))) {
            int iAux = pos;
            while(pos < input.length() && input.charAt(pos) != '+' && input.charAt(pos) != '-' && input.charAt(pos) != '*' && input.charAt(pos) != '/') ++pos;
            String ref = input.substring(iAux, pos);
            Pair pos1 = getReferencePos(ref);
            if (pos1.getX() < 0 || pos1.getX() >= cjt_cells.size() || pos1.getY() < 0 || pos1.getY() >= cjt_cells.get(0).size() ||  references.cyclicReference(cell, getCell(pos1.getX(), pos1.getY())) || getCell(pos1.getX(), pos1.getY()) == cell) {
                return "Error";
            }
            setReferences(getCell(pos1.getX(), pos1.getY()), cell);
            return getCell(pos1.getX(), pos1.getY()).getCellValue().getValue();
        }
        else {
            while (pos < input.length() && input.charAt(pos) != '+' && input.charAt(pos) != '-' && input.charAt(pos) != '*' && input.charAt(pos) != '/') {
                sB.append(input.charAt(pos));
                ++pos;
            }
        }
        return sB.toString();
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Ejecuta el input en la celda indicada, en caso de haber obtenido algun error del conversor de paramétros muestra en la celda el tipo de error
     * @param input String con la función que se quiere aplicar en una celda
     * @param cell Celda en la cual se quiere aplicar la función
     */
    public void executeInput(String input, Cell cell) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {


        if (input.length() == 0) {
            CellValue emptyCell = new CellValue("");
            cell.setCellValue(emptyCell);
        }
        else if (input.length() == 1 && input.charAt(0) == '=') {
            CellValue errorRef = new CellValue("#ERROR");
            cell.setCellValue(errorRef);
        }
        else {
            ArrayList<Parameter> parameters = parameterConverter(input, cell);
            if (parameters.get(0).getString().equals("nofunction")) {
                CellValue output = new CellValue(parameters.get(1).getString());
                cell.setCellValue(output);
            }
            else if (parameters.get(0).getString().equals("incorrectfunction")) {
                CellValue output = new CellValue("#NAME?");
                cell.setCellValue(output);
            }
            else if (parameters.size() == 1) {
                if (parameters.get(0).getString().equals("Ref error")) {
                    CellValue errorRef = new CellValue("#REF!");
                    cell.setCellValue(errorRef);
                }
                else {
                    String functionN = parameters.get(0).getString();
                    String functionS = functionN.substring(0, functionN.length() - 1);
                    Function function = GetFunctionFactory.getInstance(functionS);
                    function.setFormula(input);
                    if (functionN.charAt(functionN.length() - 1) == 'R') function.setValue("#REF!");
                    else function.setValue("#NAME?");
                    cell.setCellValue(function);
                }
            }
            else {
                Function function = GetFunctionFactory.getInstance(parameters.get(0).getString());
                //mas parametros de los necesarios
                if (parameters.size() == function.getNParameters() + 1 ) {
                    function.setParameter(parameters);
                    function.setFormula(input);
                    cell.setCellValue(function);
                }
                else {
                    function.setFormula(input);
                    function.setValue("#NAME?");
                    cell.setCellValue(function);
                }
            }
        }


    }


    public void calculateDerivateData(ArrayList<Cell> cells, String input) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Cell inicio = cells.get(0);
        Cell end = cells.get(cells.size() - 1);
        String aux;
        if (!(inicio.getRow() > end.getRow() && inicio.getColumn() > inicio.getColumn() || inicio.getRow() < end.getRow() && inicio.getColumn() < inicio.getColumn())) {
            if (inicio.getRow() > end.getRow()) {
                for (int i = 1; i < cells.size(); i++) {
                    executeInput(input, cells.get(i));
                    aux = derivateDataRef(references.getReferencesFromCell(cells.get(i)), input, 1, -1);
                    executeInput(aux, cells.get(i));
                }
            } else if (inicio.getRow() < end.getRow()) {
                for (int i = 1; i < cells.size(); i++) {
                    executeInput(input, cells.get(i));
                    aux = derivateDataRef(references.getReferencesFromCell(cells.get(i)), input, 1, i);
                    executeInput(aux, cells.get(i));
                }
            } else if (inicio.getColumn() > end.getColumn()) {
                for (int i = 1; i < cells.size(); i++) {
                    executeInput(input, cells.get(i));
                    aux = derivateDataRef(references.getReferencesFromCell(cells.get(i)), input, 2, -1);
                    executeInput(aux, cells.get(i));
                }
            } else {
                for (int i = 1; i < cells.size(); i++) {
                    executeInput(input, cells.get(i));
                    aux = derivateDataRef(references.getReferencesFromCell(cells.get(i)), input, 2, 1);
                    executeInput(aux, cells.get(i));
                }

            }
            updateValues();
        }

    }


        //identifier == 1 row, else col
         private String derivateDataRef(ArrayList<Cell> references,String input, int identifier, int num) {
             int actual = 0;
             int auxact;
             StringBuilder newFormula = new StringBuilder();
             for (int k = 0; k < references.size(); k++) {
                 String refOld;
                 String ref;
                 int row;
                 String col;
                 col = number2Letter(references.get(k).getColumn());
                 row = references.get(k).getRow();
                 if (identifier == 1) {
                    refOld = col + row;
                    ref = col + (row + num);
                 }
                 else {
                 refOld = col + row;
                 ref = (col + num) + row;
                 }
                 auxact = actual;
                 actual = input.indexOf(refOld, actual) + refOld.length();
                 String auxFunc = input.substring(auxact, actual);
                 String replauxFunc = auxFunc.replaceAll("\\b" + refOld + "\\b", ref);
                 newFormula.append(replauxFunc);
             }
             String auxFunc = input.substring(actual);
             newFormula.append(auxFunc);
             return String.valueOf(newFormula);
    }


    /**
     * Recorta el string eliminando posibles espacios
     * @param function String que se quiere recortar
     * @return El string recortado
     */
    private static String stringTrimer(String function) {
        StringBuilder result = new StringBuilder();
        boolean opened = false;
        for (int i = 0; i < function.length(); i++) {
            if (function.charAt(i) == '[') opened = true;
            if (function.charAt(i) == ']') opened = false;
            if (function.charAt(i) == ' ') {
                if (opened) result.append(function.charAt(i));
            } else result.append(function.charAt(i));
        }
        return String.valueOf(result);
    }

    /**
     * Actualiza los valores cuando se modifica una celda, para cada celda que referenciaba a updated, se actualizan sus valores
     * @param updated Celda que se ha actualizado
     */
    public void updateRecursiveValues(Cell updated) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<Cell> refs = new ArrayList<>(references.getReferencesToCell(updated));

        for (int i = 0; i < refs.size(); i++) {
            references.eraseAllReferencesOfACell(refs.get(i));
            refs.get(i).getCellValue().setNull();
            executeInput(refs.get(i).getCellValue().getUserInput(), refs.get(i));
            updateRecursiveValues(refs.get(i));
        }

    }

    /**
     * Actualiza los valores de todas las celdas de la hoja. Para eso cogemos las celdas que son referenciadas y llamamos a updateRecursiveValues en las celdas padre
     */
    public void updateValues() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<Cell> keys =  new ArrayList<>(references.getKeys());
        for (int i = 0; i < keys.size(); i++) {
            //si es referencia padre
            if (references.getReferencesFromCell(keys.get(i)).size() == 0) {
                updateRecursiveValues(keys.get(i));
            }
        }

    }
    /**
     * Actualiza las formulas de las celdas que tienen referencias cuando añadimos o eliminamos una fila o columna.
     * @param identifier Indica si la operación es de añadir/eliminar una fila o una columna, 1 para fila, cualquier otro número columna
     * @param identifier2 Indica si la operación es de añadir/eliminar, 1 para añadir, cualquier otro número para eliminar
     */
    //identifier 1 indica si es row o column, identifier2 indica si se ha añadido o eliminado, a indica donde
    public void updateReferences(int identifier, int identifier2, int a) {
        for (int i = 0; i < cjt_cells.size(); i++) {
            for (int j = 0; j < cjt_cells.get(i).size(); j++) {
                if (cjt_cells.get(i).get(j).getCellValue().getType().equals("function") && !cjt_cells.get(i).get(j).getCellValue().getUserInput().contains("[")) {
                    ArrayList<Cell> auxR = references.getReferencesFromCell(cjt_cells.get(i).get(j));
                    int actual = 0;
                    int auxact;
                    StringBuilder newFormula = new StringBuilder();
                    int rc;
                    for (int k = 0; k < auxR.size(); k++) {
                        String refOld;
                        String ref;
                        int row;
                        String col;
                        if (identifier == 1) rc = auxR.get(k).getRow();
                        else rc = auxR.get(k).getColumn();
                        if (rc >= a) {
                            if (identifier2 != 1 && rc == a) {
                                col = number2Letter(auxR.get(k).getColumn());
                                row = auxR.get(k).getRow();
                                refOld = col + row;
                                ref = "#REF!";
                            } else {
                                col = number2Letter(auxR.get(k).getColumn());
                                row = auxR.get(k).getRow();
                                if (identifier == 1) {
                                    if (identifier2 == 1) {
                                        if (row != 0) refOld = col + (row - 1);
                                        else refOld = col + row;
                                        ref = col + row;
                                    } else {
                                        refOld = col + row;
                                        if (row > 0) ref = col + (row - 1);
                                        else ref = col + row;
                                    }
                                } else {
                                    String oldCol;
                                    if (identifier2 == 1) {
                                        col = number2Letter(rc);
                                        if (rc == 0) oldCol = col;
                                        else oldCol = number2Letter(rc - 1);
                                    } else {
                                        oldCol = number2Letter(rc);
                                        if (rc == 0) col = oldCol;
                                        else col = number2Letter(rc - 1);
                                    }
                                    row = auxR.get(k).getRow();
                                    refOld = oldCol + row;
                                    ref = col + row;
                                }
                            }
                            auxact = actual;
                            actual = cjt_cells.get(i).get(j).getCellValue().getUserInput().indexOf(refOld, actual) + refOld.length();
                            String auxFunc = cjt_cells.get(i).get(j).getCellValue().getUserInput().substring(auxact, actual);
                            String replauxFunc = auxFunc.replaceAll("\\b" + refOld + "\\b", ref);
                            newFormula.append(replauxFunc);
                            if (ref.equals("#REF!")) break;
                        }

                    }
                    String auxFunc = cjt_cells.get(i).get(j).getCellValue().getUserInput().substring(actual);
                    newFormula.append(auxFunc);
                    if (String.valueOf(newFormula).contains("=#REF!")) {
                        cjt_cells.get(i).get(j).getCellValue().setFormula("#REF!");
                        cjt_cells.get(i).get(j).getCellValue().setValue("#REF!");
                    }
                    else cjt_cells.get(i).get(j).getCellValue().setFormula(String.valueOf(newFormula));
                }

            }
        }
    }


    /**
     * Convierte un array de pairs a un array de celdas cuyas posiciones son las mismas que habia en el array de pairs siempre y cuando existan en la hoja
     * @param pairs Array con las posiciones de las celdas
     * @return Array con las celdas en las posiciones que teníamos en el array de pairs
     * @throws MyException Si una celda del array pairs no existe en la hoja, lanza una excepción
     */
    public ArrayList<Cell> pairToCell(ArrayList<Pair> pairs) throws MyException {
        ArrayList<Cell> cells = new ArrayList<>();
        for (Pair pair : pairs) {
            int r = pair.getX();
            int c = pair.getY();
            if (r < 0 || r > getCjt_cells().size() || c < 0 || c > getCjt_cells().get(0).size()) throw new MyException("Cell does not exist");
            Cell aux = getCell(r, c);
            cells.add(aux);
        }
        return cells;
    }

    /**
     * Retorna un arraylist de parametros con el primer parametro un string indicando un error para tratarlo en execute input
     * @param error String con el error que hay que tratar
     * @return Array con el error que hay que tratar
     */
    private ArrayList<Parameter> errorParameterConverter(String error) {
        ArrayList<Parameter> parameterError = new ArrayList<>();
        StringP falseFunction = new StringP();
        falseFunction.addString(error);
        parameterError.add(falseFunction);
        return parameterError;
    }


}

