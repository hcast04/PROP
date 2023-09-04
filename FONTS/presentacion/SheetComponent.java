package presentacion;

import dominio.auxiliarclasses.MyException;
import dominio.auxiliarclasses.Pair;
import dominio.auxiliarclasses.ReferenceConverter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Representa cada una de las hojas de cálculo disponibles en un documento, que es el principal componente con el que interactúa el usuario
 */

public class SheetComponent  {

    /**
     * Representa el tamaño de celda de todas las celdas y botones
     */
    private Dimension cellSize;
    /**
     * Representa el nombre de la hoja de cálculo
     */
    private final String name;
    /**
     * Representa el número de filas que tiene la hoja
     */
    private final int nRows;
    /**
     * Representa el número de columnas que tiene la hoja
     */
    private final int nColumns;

    /**
     * Representa la hoja de cálculo, conteniendo todos los paneles con todos los componentes
     */
    private final JScrollPane sheetScrollPane;

    /**
     * Panel de la hoja de cálculo
     */
    private final JPanel sheetPanel;
    /**
     * Panel de las celdas de la hoja
     */
    private final JPanel textGrid;
    /**
     * Panel de los botones que indexan las filas
     */
    private final JPanel rowButtons;
    /**
     * Panel de los botones que indexan las columnas
     */
    private final JPanel colButtons;
    /**
     * Layout de la hoja de cálulo
     */
    private GridLayout gl;

    /**
     * Representa el conjunto de celdas de la hoja
     */
    private final ArrayList<ArrayList<CellComponent>> cellComponents;
    /**
     * Representa el conjunto de posiciones de las celdas seleccionadas
     */
    private final ArrayList<Pair> selectedCells;
    /**
     * Representa la coordenada x más grande de las celdas seleccionadas
     */
    private int maxSelectedX;
    /**
     * Representa la coordenada y más grande de las celdas seleccionadas
     */
    private int maxSelectedY;

    /**
     * Representa la coordenada x más pequeña de las celdas seleccionadas
     */
    private int minSelectedX;
    /**
     * Representa la coordenada y más pequeña de las celdas seleccionadas
     */
    private int minSelectedY;

    /**
     * Representa la posición de la primera celda seleccionada
     */
    private Pair firstSelected;

    /**
     * Representa si se está seleccionando celdas o no
     */
    private boolean selecting;
    /**
     * Representa si se está presionando la tecla CONTROL
     */
    private boolean controlPressed;

    /**
     * Constructora
     * @param ctrlPresentation Controlador de Presentación
     * @param rows Número de filas inicial de la hoja de cálculo
     * @param cols Número de columnas inicial de la hoja de cálculo
     * @param name Nombre de la hoja de cálculo
     */
    public SheetComponent(CtrlPresentation ctrlPresentation, int rows, int cols, String name) throws MyException {

        this.name = name;
        this.nRows = rows;
        this.nColumns = cols;


        selectedCells = new ArrayList<>();
        cellComponents = new ArrayList<>();
        maxSelectedX = maxSelectedY = minSelectedX = minSelectedY = 0;

        textGrid = new JPanel();
        rowButtons = new JPanel();
        rowButtons.setLayout(new GridLayout(nRows, 1));
        colButtons = new JPanel();
        colButtons.setLayout(new GridLayout(1, nColumns + 1));      // Añadimos uno para el boton de la esquina
        sheetPanel = new JPanel(new BorderLayout());


        // Añadimos las celdas
        gl = new GridLayout(nRows, nColumns);   // Definimos el tamaño del textGrid
        textGrid.setLayout(gl);
        for (int i = 0; i < nRows; i++) {
            ArrayList<CellComponent> aux = new ArrayList<>();
            for (int j = 0; j < nColumns; j++) {
                CellComponent c = new CellComponent(ctrlPresentation, this, 5, i, j);
                c.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                aux.add(c);
                c.setMaximumSize(new Dimension(100, 16));
                cellSize = c.getPreferredSize();
                c.setValue(ctrlPresentation.getCellValue(i, j));
                c.setInput(ctrlPresentation.getCellInput(i, j));
                textGrid.add(c);
            }
            cellComponents.add(aux);
        }

        // Añadimos los botones de las filas
        for (int i = 0; i < nRows; i++) {
            ButtonComponent b = new ButtonComponent(ctrlPresentation, this, String.valueOf(i), -1, i);
            b.setPreferredSize(cellSize);
            rowButtons.add(b);
        }

        // Añadimos los botones de las columnas
        for (int i = 0; i < (nColumns + 1); i++) {
            ButtonComponent b;
            if (i == 0) {
                b = new ButtonComponent(ctrlPresentation, this,"", -1, -1);
            }
            else {
                String title = ReferenceConverter.number2Letter(i-1);
                b = new ButtonComponent(ctrlPresentation, this, title, i-1, -1);
            }
            b.setPreferredSize(cellSize);
            colButtons.add(b);
        }

        // Añadimos rowButtons, colButtons y textGrid en sus posiciones dentro del BorderLayout
        sheetPanel.add(rowButtons, BorderLayout.WEST);
        sheetPanel.add(colButtons, BorderLayout.NORTH);
        sheetPanel.add(textGrid, BorderLayout.CENTER);

        sheetScrollPane = new JScrollPane(sheetPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    }

    /**
     * Retorna la hoja de cálculo actual
     * @return Un JScrollPane que representa la hoja de cálculo
     */
    public JScrollPane getSheet() {
        return sheetScrollPane;
    }

    /**
     * Retorna el nombre de la hoja de cálculo
     * @return Un String con el nombre de la hoja de cálculo
     */
    public String getName() {
        return name;
    }

    /**
     * Añade una celda al ArrayList de celdas seleccionadas
     * @param x Coordenada x de la celda
     * @param y Coordenada y de la celda
     */
    public void addSelectedCell(int x, int y) {
        selectedCells.add(new Pair(x, y));
        cellComponents.get(x).get(y).setBorder(BorderFactory.createLineBorder(Color.decode("#92BCCA")));
        cellComponents.get(x).get(y).setBackground(Color.decode("#DAEEF5"));
    }

    /**
     * Vacía el ArrayList de las celdas seleccionadas
     */
    public void eraseSelectedCells() {
        selectedCells.clear();
    }

    /**
     * Retorna las posiciones de las celdas seleccionadas
     * @return Un ArrayList de Pairs que contiene las posiciones de las celdas seleccionadas
     */
    public ArrayList<Pair> getSelectedCells() {
        return selectedCells;
    }

    /**
     * Retorna el número de celdas seleccionadas
     * @return Un int con el tamaño del ArrayList que contiene las posiciones de las celdas seleccionadas
     */
    public int getNSelectedCells() {
        return selectedCells.size();
    }

    /**
     * Indica si se está seleccionando celdas
     * @param bool Es true en caso de que se estén seleccionando celdas, false en caso contrario
     */
    public void setSelecting(boolean bool) {
        selecting = bool;
    }

    /**
     * Retorna true si se está seleccionando celdas, false en caso contrario
     * @return El valor del booleano que indica si se está seleccionando celdas o no
     */
    public boolean isSelecting() {
        return selecting;
    }


    /**
     * Indica si se está presionando la tecla CONTROL
     * @param bool Es true en caso de que se esté presionando la tecla CONTROL
     */
    public void setControlPressed(boolean bool) {
        controlPressed = bool;
    }

    /**
     * Retorna true si se está presionando la tecla CONTROL, false en caso contrario
     * @return El valor del boolean que indica si se está presionando la tecla CONTROL o no
     */
    public boolean isControlPressed() {
        return controlPressed;
    }

    /**
     * Selecciona un bloque de celdas dado una posición mínima (la de la esquina superior izquierda)
     * y una posición máxima (la de la esquina inferior derecha), la cuales están presentes como atributos en la clase
     */
    public void setSelectedBlock() {

        for (int i = minSelectedX; i <= maxSelectedX; i++) {
            for (int j = minSelectedY; j <= maxSelectedY; j++) {
                addSelectedCell(i, j);
            }
        }

    }

    /**
     * Inserta la coordenada x mínima de las celdas seleccionadas
     * @param x Coordenada x que se está evaluando
     */
    public void setXMin(int x) {
        if (x < minSelectedX) minSelectedX = x;
    }

    /**
     * Inserta la coordenada y mínima de las celdas seleccionadas
     * @param y Coordenada y que se está evaluando
     */
    public void setYMin(int y) {
        if (y < minSelectedY) minSelectedY = y;
    }

    /**
     * Inserta la coordenada x máxima de las celdas seleccionadas
     * @param x Coordenada x que se está evaluando
     */
    public void setXMax(int x) {
        if (x > maxSelectedX) maxSelectedX = x;
    }

    /**
     * Inserta la coordenada y máxima de las celdas seleccionadas
     * @param y Coordenada y que se está evaluando
     */
    public void setYMax(int y) {
        if (y > maxSelectedY) maxSelectedY = y;
    }

    /**
     * Evalúa las posiciones dadas para determinar las posiciones máximas y mínimas de las celdas seleccionadas
     * @param x Coordenada x de la celda a evaluar
     * @param y Coordenada y de la celda a evaluar
     */
    public void setSelectedCellsBounds(int x, int y) {
        setXMax(x);
        setXMin(x);
        setYMax(y);
        setYMin(y);
    }

    /**
     * Inserta la primera celda seleccionada e inicializa las posiciones máximas y mínimas con sus coordenadas
     * @param x Coordenada x de la primera celda
     * @param y Coordenada y de la primera celda
     */
    public void setSelectedIniCell(int x, int y) {
        firstSelected = new Pair(x, y);
        maxSelectedX = x;
        maxSelectedY = y;
        minSelectedX = x;
        minSelectedY = y;
    }

    /**
     * Elimina el color de fondo de las celdas seleccionadas
     */
    public void clearCellsBackground() {
        for (int i = 0; i < cellComponents.size(); i++) {
            for (int j = 0; j < cellComponents.get(i).size(); j++) {
                cellComponents.get(i).get(j).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cellComponents.get(i).get(j).setBackground(Color.WHITE);
            }
        }
    }

    /**
     * Retorna la celda en la posición más pequeña de las celdas seleccionadas
     * @return Un Pair con la posición de la celda en la posición más pequeña de las celdas seleccionadas
     */
    public Pair getMinSelectedCell() {
        return new Pair(minSelectedX, minSelectedY);
    }

    /**
     * Inserta las celdas seleccionadas en el ArrayList de celdas seleccionadas
     * @param cells ArrayList con las posiciones de las celdas seleccionadas
     */
    public void setSelectedCells(ArrayList<Pair> cells) {
        selectedCells.clear();
        clearCellsBackground();
        for (int i = 0; i < cells.size(); i++) {
            addSelectedCell(cells.get(i).getX(), cells.get(i).getY());
        }
    }

    /**
     * Inserta las celdas de la fila indicada en el ArrayList de celdas seleccionadas
     * @param row Número de la fila de la cual se quiere seleccionar las celdas
     */
    public void setSelectedRow(int row) {
        selectedCells.clear();
        clearCellsBackground();
        for (int i = 0; i < nColumns; i++) {
            addSelectedCell(row, i);
        }
    }

    /**
     * Inserta las celdas de la columna indicada en el ArrayList de celdas seleccionadas
     * @param column Número de la columna de la cual se quiere seleccionar las celdas
     */
    public void setSelectedColumn(int column) {
        selectedCells.clear();
        clearCellsBackground();
        for (int i = 0; i < nRows; i++) {
            addSelectedCell(i, column);
        }
    }

    /**
     * Inserta todas las celdas presentes en la hoja de cálculo en el ArrayList de celdas seleccionadas
     */
    public void setSelectedEntireSheet() {
        selectedCells.clear();
        clearCellsBackground();
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nColumns; j++) {
                addSelectedCell(i, j);
            }
        }
    }

    /**
     * Retorna el input de la celda indicada
     * @param cell Pair con la posición de la celda de la cual se quiere conseguir el input insertado
     * @return Un String con el input de la celda indicada
     */
    public String getCellInput(Pair cell) {
        return cellComponents.get(cell.getX()).get(cell.getY()).getInput();
    }

    /**
     * Retorna la posición de la primera celda seleccionada
     * @return Un Pair con la posición de la primera celda seleccionada
     */
    public Pair getFirstSelected() {
        return firstSelected;
    }

}