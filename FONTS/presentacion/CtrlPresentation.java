package presentacion;

import dominio.auxiliarclasses.MyException;
import dominio.auxiliarclasses.Pair;
import dominio.controlador.CtrlDominio;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Representa el controlador de presentación que comunica las interacciones del usuario con la capa de dominio
 */

public class CtrlPresentation {

    /**
     * Controlador de dominio
     */
    private final CtrlDominio ctrlDominio;
    /**
     * Vista principal de la aplicación
     */
    private final ExcelFrame mainView;

    /**
     * Crea un controlador de presentación
     */
    public CtrlPresentation() throws MyException {

        this.ctrlDominio = new CtrlDominio();
        this.mainView = new ExcelFrame(this);

    }

    /**
     * Retorna el número de hojas de cálculo del documento
     * @return Entero con el número de hojas en el documento
     */
    public int getNSheetsDomain() {
        return ctrlDominio.getNSheets();
    }

    /**
     * Retorna el número de filas en la hoja indicada
     * @param nSheet Índice de la hoja de la cual queremos el número de filas
     * @return Entero con el número de filas de la hoja indicada
     */
    public int getNRows(int nSheet) throws MyException {
        ctrlDominio.selectSheet(nSheet);
        return ctrlDominio.getNRows();
    }

    /**
     * Retorna el número de columnas en la hoja indicada
     * @param nSheet Índice de la hoja de la cual queremos el número de columnas
     * @return Entero con el número de columnas de la hoja indicada
     */
    public int getNColumns(int nSheet) throws MyException {
        ctrlDominio.selectSheet(nSheet);
        return ctrlDominio.getNColumns();
    }

    /**
     * Retorna el nombre de la hoja de cálculo indicada
     * @param nSheet Índice de la hoja de la cual queremos el número de columnas
     * @return String con el nombre de la hoja de cálculo
     */
    public String getSheetName(int nSheet) throws MyException {
        ctrlDominio.selectSheet(nSheet);
        return ctrlDominio.getSheetName();
    }

    /**
     * Añade una nueva hoja de cálculo al documento actual
     * @param nameSheet String con el nombre de la nueva hoja
     * @return True en caso de que se haya añadido correctamente la nueva hoja, false en caso contrario
     */
    public boolean addSheet(String nameSheet) throws MyException {
        return ctrlDominio.addSheet(nameSheet);
    }

    /**
     * Borra la hoja indicada del documento
     * @param index Entero con el índice de la hoja que se quiere borrar
     * @return True en caso de que se haya borrado correctamente, false en caso contrario
     */
    public boolean deleteSheet(int index) {
        return ctrlDominio.deleteSheet(index);
    }

    /**
     * Edita el nombre de la hoja indicada
     * @param index Índice de la hoja a la cual se le quiere editar el nombre
     * @param name Nuevo nombre de la hoja
     * @return True en caso de que se haya renombrado correctamente, false en caso contrario
     * @throws MyException Si la sheet indicada no existe, lanza una excepción
     */
    public boolean renameSheet(int index, String name) throws MyException {
        return ctrlDominio.renameSheet(index, name);
    }

    /**
     * Selecciona la hoja con el índice indicado
     * @param index Índice de la hoja que se quiere seleccionar
     * @throws MyException Si la hoja no existe en el documento
     */
    public void selectSheet(int index) throws MyException {
        ctrlDominio.selectSheet(index);
    }

    /**
     * Actualiza el documento con los nuevos valores
     */
    public void updateDocument() throws MyException {
        mainView.updateDocument();
    }

    /**
     * Retorna el valor de la celda en la posición indicada
     * @param r Fila en la que se encuentra la celda
     * @param c Columna en la que se encuentra la celda
     * @return String con el valor de la celda
     */
    public String getCellValue(int r, int c) throws MyException {
        return ctrlDominio.getValue(r, c);
    }

    /**
     * Retorna el input de la celda en la posición indicada
     * @param r Fila en la que se encuentra la celda
     * @param c Columna en la que se encuentra la celda
     * @return String con el input de la celda
     */
    public String getCellInput(int r, int c) throws MyException {
        return ctrlDominio.getInput(r, c);
    }

    /**
     * Añade una nueva fila a la hoja de cálculo
     * @param r Posición en la que se quiere insertar la nueva fila
     */
    public void addRow(int r) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException{
        ctrlDominio.addRow(r);
    }

    /**
     * Añade una nueva columna a la hoja de cálculo
     * @param c Posición en la que se quiere insertar la nueva columna

     */
    public void addColumn(int c) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.addColumn(c);
    }

    /**
     * Borra una columna en la posición indicada
     * @param c Índice de la columna que se quiere borrar
     */
    public void deleteColumn(int c) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.deleteColumn(c);
    }

    /**
     * Borra una fila en la posición indicada
     * @param r Índice de la fila que se quiere borrar
     */
    public void deleteRow(int r) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException{
        ctrlDominio.deleteRow(r);
    }

    /**
     * Ejecuta el input insertado en un conjunto de celdas seleccionadas
     * @param cells Celdas a las cuales se les aplica el input insertado
     * @param input String con el input que se inserta en las celdas
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void executeSelectedCells(ArrayList<Pair> cells, String input) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.modifyCells(cells, input);
    }

    /**
     * Ordena un bloque de celdas con un criterio específico
     * @param cells Conjunto de celdas que se quieren ordenar
     * @param criterio Criterio con el que se ordena las celdas (1 para ordenar de manera creciente, 2 para decreciente)
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void sortSelectedBlock(ArrayList<Pair> cells, int criterio) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.sortContBloq(cells, criterio);
    }

    /**
     * Copia el bloque de celdas seleccionado en el nuevo bloque de celdas indicado
     * @param cells Bloque de celdas que se quiere copiar
     * @param newCells Bloque de celdas donde se copia
     * @throws MyException Si una celda del array copy o where no existe en la hoja, lanza una excepción
     */
    public void copySelectedBlock(ArrayList<Pair> cells, ArrayList<Pair> newCells) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.copyContBloq(cells, newCells);
    }

    /**
     * Mueve el bloque de celdas seleccionado en el nuevo bloque de celdas indicado
     * @param cells Bloque de celdas que se quiere mover
     * @param newCells Bloque de celdas donde se mueve
     * @throws MyException Si una celda del array move o where no existe en la hoja, lanza una excepción
     */
    public void moveSelectedBlock(ArrayList<Pair> cells, ArrayList<Pair> newCells) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.moveContBloq(cells, newCells);
    }

    /**
     * Borra el bloque de celdas seleccionado
     * @param cells Conjunto de celdas que se quiere borrar
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void deleteSelectedBlock(ArrayList<Pair> cells) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.deleteContBloq(cells);
    }

    /**
     * Busca dentro de las celdas seleccionadas un cierto valor y selecciona las que lo contienen
     * @param cells Conjunto de celdas en las cuales se quiere buscar
     * @param search String con el valor que se quiere buscar
     * @return ArrayList con las posiciones de las celdas que contienen el valor a buscar dentro de las celdas seleccionadas
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public ArrayList<Pair> searchContSelectedBlock(ArrayList<Pair> cells, String search) throws MyException {
        return ctrlDominio.searchContBloq(cells, search);
    }

    /**
     * Reemplaza un cierto valor por otro dentro de las celdas seleccionadas
     * @param cells Celdas en las cuales se quiere buscar el contenido indicado y reemplazarlo
     * @param replacee String con el valor a reemplazar
     * @param replacer String con el nuevo valor con el que se reemplaza
     * @throws MyException Si una celda del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void replaceContSelectedBlock(ArrayList<Pair> cells, String replacee, String replacer) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.replaceContBloq(cells, replacee, replacer);
    }

    /**
     * Inserta el input de una celda en la barra de fórmula
     * @param input String con el input de la celda seleccionada
     */
    public void setFormulaBar(String input) {
        mainView.setInputFormulaBar(input);
    }

    /**
     * Carga un documento en la aplicación
     * @param path Dirección donde se encuentra el documento
     * @throws MyException Si la sheet añadida no se puede seleccionar
     * @throws IOException Si no se encuentra el documento
     */
    public void loadDocument(String path) throws MyException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.loadData(path);
    }

    /**
     * Guarda el documento actual
     * @param path Dirección donde se guardará el documento
     */
    public void saveDocument(String path) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.saveData(path);
    }

    /**
     * Realiza el cálculo de datos derivados
     * @param cellsaux Celdas a las cuales se les aplica el cálculo
     * @param input String con el input de la primera celda que se aplicará al resto de celdas seleccionadas
     * @throws MyException Si una cell del array cellsaux no existe en la hoja, lanza una excepción
     */
    public void calcDerivateData(ArrayList<Pair> cellsaux, String input) throws MyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ctrlDominio.calculateDerivateData(cellsaux, input);
    }

}