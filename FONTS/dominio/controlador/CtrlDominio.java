package dominio.controlador;

import datos.controlador.CtrlPersistencia;
import dominio.auxiliarclasses.MyException;
import dominio.auxiliarclasses.Pair;
import dominio.model.*;
import org.apache.commons.io.FilenameUtils;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import static datos.auxiliarclasses.DocumentType.formatType;
import static dominio.auxiliarclasses.ReferenceConverter.cellToPair;


/**
 * Representa el controlador de dominio que ejecuta las funcionalidades principales del dominio
 */

public class CtrlDominio {
    private Sheet sheet;
    private Document d;
    private final CtrlPersistencia dataController;


    //Constructor

    /**
     * Crea un controlador de dominio
     */
    public CtrlDominio() {
        this.d = new Document("Documento 1");
        this.sheet = d.getCjtSheet().get(0);
        this.dataController = new CtrlPersistencia();
    }

    /**
     * Crea un controlador de dominio que es una copia de cd
     * @param cd Controlador de dominio
     */
    public CtrlDominio(CtrlDominio cd) {
        this.d = cd.getD();
        this.sheet = cd.getSheetAct();
        this.dataController = cd.getDataController();
    }

    //Getters

    //get actual sheet
    public Sheet getSheetAct() {
        return this.sheet;
    }

    //get Sheet number a
    public Sheet getSheet(int a) {
        return this.d.getCjtSheet().get(a);
    }

    public Document getD() {
        return this.d;
    }

    public String getSheetName() {
        return sheet.getName();
    }

    public int getMaxRow() {
        return sheet.getMaxRow();
    }

    public int getMaxCol() {
        return sheet.getMaxColumn();
    }

    public int getNRows() {
        return this.sheet.getCjt_cells().size();
    }

    public int getNColumns() {
        return this.sheet.getCjt_cells().get(0).size();
    }

    public CtrlPersistencia getDataController() { return this.dataController; }

    public int getNSheets() { return this.d.getCjtSheet().size(); }

    public int getMaxSheets() { return this.d.getMaxSheets(); }


    public String getValue(int r, int c) throws MyException {
        return selectCell(r, c).getCellValue().tryGetValue();
    }

    public String getInput(int r, int c) throws MyException {
        return selectCell(r, c).getCellValue().getUserInput();
    }

    public String getInput(Cell c) {
        return c.getCellValue().getUserInput();
    }



    /**
     * Carga un documento con formato csv o xlsx
     * @param path String con el camino del documento
     * @throws IOException Si no se encuentra el documento
     * @throws MyException Si la sheet añadida no se puede seleccionar
     */
    //Controlador dades
    public void loadData(String path) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException, IOException {
        String format = formatType(path);
        if (format.equals("csv")) {
            List<List<String>> data = dataController.loadDocument(path, format).get(0);
            int rows = data.size() + 1;
            int cols = data.get(0).size();
            if (rows < 26) rows = 27;
            if (cols < 26) cols = 26;
            File f = new File(path);
            String fileName = FilenameUtils.removeExtension(f.getName());
            if (!addSheet(fileName, rows,cols)) {
                for (int i = 0; i < getMaxSheets() - getNSheets(); i++) {
                    String numero = String.valueOf(i);
                    if (addSheet(fileName + numero, rows, cols)) break;
                }
            }
            for (int i = 1; i < data.size(); i++) {
                for (int j = 0; j < data.get(0).size(); j++) {
                    try {
                        sheet.executeInput(data.get(i - 1).get(j), sheet.getCell(i, j));
                    } catch (InputMismatchException | NumberFormatException e) {
                        break;
                    }
                }
            }
            sheet.updateValues();
        }
        else if (format.equals("xlsx")) {
            d = new Document("Document xlsx", 0);
            List<List<List<String>>> data = dataController.loadDocument(path, format);
            for (List<List<String>> datum : data) { // num sheets
                int row = datum.size() + 1;
                if (row < 26) row = 27;
                int col = datum.get(0).size() - 1;
                if (col < 26) col = 26;
                if (!addSheet(datum.get(0).get(0), row, col)) return;
                Sheet sh = getSheet(this.d.getCjtSheet().size() - 1);
                selectSheet(sh);
                for (int j = 1; j < datum.size(); j++) {
                    for (int k = 1; k < datum.get(0).size(); k++) {
                        if (datum.get(j - 1).size() != 0) {
                            try {
                                sheet.executeInput(datum.get(j - 1).get(k), sheet.getCell(j, k - 1));
                            } catch (InputMismatchException | NumberFormatException e) {
                                break;
                            }
                        }
                    }
                }
                sheet.updateValues();
            }
        }
    }

    /**
     * Guarda una sheet o un conjunto de sheets en el path indicado
     * @param path String que representa el camino donde guardará el documento y su formato
     */

    public void saveData(String path) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String format = formatType(path);
        ArrayList<String> names = new ArrayList<>();
        boolean xlsx = false;
        List<List<ArrayList<String>>> data = new ArrayList<>();
        int nsheets;
        if(format.equals("csv")) nsheets = 1;
        else if (format.equals("xlsx")){
            nsheets = getNSheets();
            xlsx = true;
        }
        else return;
        sheet.addRow(0);
        for (int i = 0; i < nsheets; i++) {
            if (xlsx) names.add(getSheet(i).getName());
            ArrayList<ArrayList<String>> aux = new ArrayList<>();
            for (int j = 1; j < getNRows(); j++) {
                ArrayList<String> line = new ArrayList<>();
                for (int k = 0; k < getNColumns(); k++) {
                    line.add(getInput(sheet.getCell(j, k))); //LINEA
                }
                aux.add(line);
            }
            data.add(aux);
        }
        sheet.deleteRow(0);
        dataController.setnames(format, names);
        dataController.saveDocument(path, data);
    }



    //Methods
    /**
     * Retorna la celda de fila y columna indicadas si está dentro de la hoja
     * @param r Número de fila de la cual se quiere obtener la celda
     * @param c Número de columna de la cual se quiere obtener la celda
     * @return Celda en la posición indicada
     * @throws MyException Si la celda no existe
     */
    public Cell selectCell(int r, int c) throws MyException {
        if (r < 0 || r > getNRows() || c < 0 || c > getNColumns()) throw new MyException("Cell does not exist");
        return sheet.getCell(r, c);
    }

    /**
     * Selecciona la hoja sh donde estamos trabajando en el excel si existe
     * @param sh Hoja a seleccionar
     * @throws MyException Si la hoja no existe en el documento
     */
    public void selectSheet(Sheet sh) throws MyException {
        if (d.notexistsSheet(sh.getName())) throw new MyException("Sheet does not exist in the document");
        this.sheet = sh;
    }
    /**
     * Selecciona la hoja con indice index donde estamos trabajando en el excel si existe
     * @param index Indice de la hoja a seleccionar
     * @throws MyException Si la celda no existe en el documento
     */
    public void selectSheet(int index) throws MyException {
        if (d.notexistsSheet(getSheet(index).getName())) throw new MyException("Sheet does not exist in the document");
        this.sheet = getSheet(index);
    }


    /**
     * Modifica el contenido de las celdas cellsaux con el input input y actualiza valores
     * @param cellsaux Arraylist de pares con las posiciones de las celdas
     * @param input String con el input
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void modifyCells(ArrayList<Pair> cellsaux, String input) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException {
        ArrayList<Cell> cells = sheet.pairToCell(cellsaux);
        for (Cell cell : cells) {
            ArrayList<Cell> oldRefs = sheet.getReferences().getReferencesFromCell(cell);
            for (Cell oldRef : oldRefs) {
                sheet.getReferences().eraseReference(oldRef, cell);
            }
            sheet.executeInput(input, cell);
            sheet.updateRecursiveValues(cell);
        }
    }


    /**
     * Inserta una nueva fila vacía en la posición indicada, siempre y cuando no se supere el límite máximo de filas permitidas, actualiza los valores y referencias
     * @param a Posición en la que se quiere insertar la nueva fila
     */
    public void addRow(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (getNRows() < getMaxRow()) sheet.addRow(a);
    }

    /**
     * Inserta una nueva columna vacía en la posición indicada, siempre y cuando no se supere el límite máximo de columnas permitidas, actualiza los valores y referencias
     * @param a Posición en la que se quiere insertar la nueva columna
     */
    public void addColumn(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (getNColumns() < getMaxCol()) sheet.addColumn(a);
    }

    /**
     * Borra la fila indicada en caso de que exista, siempre y cuando no sea la única fila de la hoja de cálculo, actualiza los valores y referencias
     * @param a Número de fila que se quiere borrar
     */
    public void deleteRow(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException{
        if (a < 0 || a > getNRows() - 1 || sheet.getCjt_cells().size() == 1) return;
        sheet.deleteRow(a);
    }

    /**
     * Borra la columna indicada en caso de que exista, siempre y cuando no sea la única columna de la hoja de cálculo, actualiza los valores y referencias
     * @param a Número de columna que se quiere borrar
     */
    public void deleteColumn(int a) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException{
        if (a < 0 || a > getNColumns() - 1 || sheet.getCjt_cells().get(0).size() == 1) return;
        sheet.deleteColumn(a);
    }


    /**
     * Elimina el contenido del bloque de celdas especificado siempre y cuando éstas existan dentro de la hoja de cálculo, actualiza los valores y referencias
     * @param cellsaux ArrayList con los pares con las posiciones de las celdas de las cuales se quiere borrar su contenido
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void deleteContBloq(ArrayList<Pair> cellsaux) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException {
        ArrayList<Cell> cells = sheet.pairToCell(cellsaux);
        sheet.deleteContBloq(cells);
    }

    /**
     * Mueve el contenido de las celdas a mover y lo inserta en las nuevas celdas en el mismo orden que se han recorrido los dos ArrayLists, siempre y cuando estos ArrayLists tengan el mismo tamaño
     *             Si alguna celda de where no existe en la hoja, se añaden columnas y filas hasta que exista actualiza los valores y las referencias
     * @param move ArrayList con las celdas de las cuales se quiere mover su contenido
     * @param where ArrayList con las nuevas celdas que contendrán el contenido de las celdas que se quieren mover
     * @throws MyException Si una celda del array move o where no existe en la hoja, lanza una excepción
     */
    public void moveContBloq(ArrayList<Pair> move, ArrayList<Pair> where) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException {
        ArrayList<Cell> move2 = sheet.pairToCell(move);
        if (where.get(where.size() - 1).getX() >= getMaxRow() || where.get(where.size() - 1).getY() >= getMaxCol() || where.get(0).getX() >= getMaxRow() || where.get(0).getY() >= getMaxCol()) throw new MyException("La celda esta fuera del tamaño maximo");
        fixOuterCells(where.get(where.size() - 1));
        fixOuterCells(where.get(0));
        ArrayList<Cell> where2 = sheet.pairToCell(where);
        sheet.moveContBloq(move2, where2);
    }

    /**
     * Copia el contenido de las celdas a copiar y lo inserta en las celdas a pegar en el mismo orden que se han recorrido los dos ArrayLists, siempre y cuando estos ArrayLists tengan el mismo tamaño
     *      Si alguna celda de where no existe en la hoja, se añaden columnas y filas hasta que exista
     * @param copy ArrayList con las celdas de las cuales se quiere copiar su contenido
     * @param where ArrayList con las celdas en las cuales se quiere pegar el contenido de las celdas copiadas
     * @throws MyException Si una celda del array copy o where no existe en la hoja, lanza una excepción
     */
    public void copyContBloq(ArrayList<Pair> copy, ArrayList<Pair> where) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException {
        ArrayList<Cell> copy2 = sheet.pairToCell(copy);
        if (where.get(where.size() - 1).getX() >= getMaxRow() || where.get(where.size() - 1).getY() >= getMaxCol() || where.get(0).getX() >= getMaxRow() || where.get(0).getY()  >= getMaxCol()) throw new MyException("La celda esta fuera del tamaño maximo");
        fixOuterCells(where.get(where.size() - 1));
        fixOuterCells(where.get(0));
        ArrayList<Cell> where2 = sheet.pairToCell(where);
        sheet.copyContBloq(copy2, where2);
    }

    /**
     * Ordena las celdas indicadas, siempre y cuando existan, por su contenido dentro del mismo bloque seleccionado, siguiendo un criterio: 1 para ordenar de manera creciente, 2 para ordenar de manera decreciente
     * Las celdas que eran funciones pasan a ser cellValues y las referencias se eliminan
     * @param cellsaux ArrayList con las celdas de las cuales se quiere ordenar su contenido
     * @param criterio Tipo de criterio a seguir para ordenar las celdas indicadas
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void sortContBloq(ArrayList<Pair> cellsaux, int criterio) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException {
        ArrayList<Cell> cells = sheet.pairToCell(cellsaux);
        sheet.sortContBloq(cells, criterio);
    }

    /**
     * Busca las celdas que contienen el contenido que se quiere reemplazar y lo reemplaza por el contenido indicado, actualiza los valores y las referencias
     * @param cellsaux ArrayList con las celdas de las cuales se quiere reemplazar un contenido específico
     * @param replacee String con el contenido que se quiere reemplazar
     * @param replacer String con el contenido por el cual se quiere reemplazar
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void replaceContBloq(ArrayList<Pair> cellsaux, String replacee, String replacer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MyException {
        ArrayList<Cell> cells = sheet.pairToCell(cellsaux);
        sheet.replaceContBloq(cells, replacee, replacer);
    }

    /**
     * Retorna las celdas que tienen un contenido determinado
     * @return ArrayList con las celdas que contienen el contenido especificado, siempre y cuando las celdas indicadas existen
     * @param cellsaux ArrayList con las celdas de las cuales se quiere buscar un contenido específico
     * @param search String con el contenido que se quiere buscar
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public ArrayList<Pair> searchContBloq(ArrayList<Pair> cellsaux, String search) throws MyException {
        ArrayList<Cell> cells = sheet.pairToCell(cellsaux);
        ArrayList<Cell> found = sheet.searchContBloq(cells, search);
        return cellToPair(found);
    }
    /**
     * Añade una hoja con el nombre especificado al documento, en caso de que no exista una hoja con el mismo nombre
     * @param name Un string que representa el nombre de la hoja
     * @return True si se ha añadido, falso en caso contrario
     */
    public boolean addSheet(String name) throws MyException {
        if (d.getCjtSheet().size() < d.getMaxSheets()) {
            if (!d.addSheet(name)) return false;
            else {
                selectSheet(d.getCjtSheet().size() - 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Añade una hoja con el nombre especificado al documento y el número de filas y columnas especificado. En caso de que no exista una hoja con el mismo nombre
     * @param name Un string que representa el nombre de la hoja
     * @param rows Un int que representa el número de filas de la hoja
     * @param columns Un int que representa el número de columnas de la hoja
     * @return True si se ha añadido, falso en caso contrario
     */
    public boolean addSheet(String name, int rows, int columns) throws MyException {
        if (d.getCjtSheet().size() < d.getMaxSheets()) {
            if (!d.addSheet(name, rows, columns)) return false;
            else {
                selectSheet(d.getCjtSheet().size() - 1);
                return true;
            }
        }
        return false;
    }


    /**
     * Elimina la hoja con el indice index del Documento, si existe
     * @param sheet Indice de la hoja que se quiere eliminar
     * @return True si se ha eliminado, falso en caso contrario
     */
    public boolean deleteSheet(int sheet) {
        return d.deleteSheet(sheet);
    }


    /**
     * Modifica el nombre de la hoja indicada, en caso de que no exista ya una hoja con el mismo nombre
     * @param index Número de la hoja a la cual se quiere modificar el nombre
     * @param newname Nuevo nombre que se le quiere poner a la hoja
     * @return true si se ha cambiado el nombre, falso en caso contrario
     * @throws MyException Si la sheet indicada no existe, lanza una excepción
     */
    public boolean renameSheet(int index, String newname) throws MyException {
        if (index < 0 || index > d.getCjtSheet().size() - 1) throw new MyException("Sheet no existe");
        return d.setName(index, newname);
    }


    /**
     * Extiende la función de una celda con referencias a otras celdas para las demás celdas seleccionadas, usando las referencias apropiadas
     * @param cellsaux ArrayList con las celdas, la primera es la celda de la cual se quiere extender la función, las demás son las celdas a las que se quiere añadir la función
     * @param input String con la formula de la primera celda que se quiere añadir a las celdas seleccionadas
     * @throws MyException Si una cell del array cellsaux no existe en la hoja, lanza una excepción
     */

    public void calculateDerivateData(ArrayList<Pair> cellsaux, String input) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<Cell> cells = sheet.pairToCell(cellsaux);
         sheet.calculateDerivateData(cells, input);
    }

   /**
     * Añade filas y columnas hasta que en la hoja exista la celda con las coordenadas que hay en pair
     * @param pair Coordenadas de la ultima celda que tiene que existir
     */
   private void fixOuterCells(Pair pair) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        while (pair.getX() >= getNRows()) {
            addRow(getNRows());
        }
        while (pair.getY() >= getNColumns()) {
            addColumn(getNColumns());
        }
    }






}
