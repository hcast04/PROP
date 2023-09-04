package presentacion;

import dominio.auxiliarclasses.MyException;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Representa cada una de las celdas que componen una hoja de cálculo, y donde el usuario realiza todas las acciones como insertar funciones o
 */

public class CellComponent extends JTextField implements KeyListener, MouseListener {

    /**
     * Representa la coordenada x de la celda
     */
    private final int x;
    /**
     * Representa la coordenada y de la celda
     */
    private final int y;

    /**
     * Representa la fórmula insertada en la celda
     */
    private String formula;
    /**
     * Representa el valor de la celda después de ejecutar la fórmula insertada
     */
    private String value;

    /**
     * Controlador de presentación de la aplicación
     */
    private final CtrlPresentation ctrlPresentation;
    /**
     * Representa la hoja de cálculo en la que se encuentra la celda
     */
    private final SheetComponent sheetComponent;

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            sheetComponent.setControlPressed(false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                ctrlPresentation.executeSelectedCells(sheetComponent.getSelectedCells(), this.getText());
                formula = this.getText();
            } catch (MyException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }


            try {
                ctrlPresentation.updateDocument();
                ctrlPresentation.setFormulaBar(this.getText());
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }

        }

        else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            if (sheetComponent.getNSelectedCells() > 1) {
                try {
                    ctrlPresentation.deleteSelectedBlock(sheetComponent.getSelectedCells());
                } catch (MyException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    ctrlPresentation.updateDocument();
                } catch (MyException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }

        else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            sheetComponent.setControlPressed(true);
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.setText(formula);
        ctrlPresentation.setFormulaBar(formula);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        sheetComponent.setSelecting(true);
        sheetComponent.clearCellsBackground();
        if (e.getButton() == MouseEvent.BUTTON1) {
            sheetComponent.eraseSelectedCells();
            sheetComponent.setSelectedIniCell(x, y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            sheetComponent.setSelecting(false);
            sheetComponent.setSelectedBlock();
            if (sheetComponent.isControlPressed()) {
                try {
                    ctrlPresentation.calcDerivateData(sheetComponent.getSelectedCells(), sheetComponent.getCellInput(sheetComponent.getFirstSelected()));
                } catch (MyException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    ctrlPresentation.updateDocument();
                } catch (MyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if (sheetComponent.isSelecting()) {
            sheetComponent.setSelectedCellsBounds(x, y);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Constructora
     * @param ctrlPresentation Controlador de presentación
     * @param sheetComponent hoja de la celda
     * @param size tamaño hoja
     * @param x coordenada x de la celda
     * @param y coordenada y da la celda
     */
    public CellComponent(CtrlPresentation ctrlPresentation, SheetComponent sheetComponent, int size, int x, int y) {
        super(size);

        this.x = x;
        this.y = y;
        this.ctrlPresentation = ctrlPresentation;
        this.sheetComponent = sheetComponent;

        // Añadimos el keylistener para insertar en el dominio el valor de la celda con la tecla enter
        this.addKeyListener(this);
        this.addMouseListener(this);


    }


    /**
     * Establece el valor de la celda
     * @param value String con el valor que se quiere insertar en la celda
     */
    public void setValue(String value) {
        try {
            double aux = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.DOWN);
            this.value = df.format(aux);
            this.value = this.value.replace(',', '.');
        }
        catch(NumberFormatException e) {
            this.value = value;
        }
        this.setText(this.value);
        this.setCaretPosition(0);
    }

    /**
     * Establece el input insertado en la celda
     * @param formula String con el input que ha insertado el usuario
     */
    public void setInput(String formula) {
        this.formula = formula;
    }

    /**
     * Retorna el input de la celda insertado por el usuario
     * @return String con el input que se ha insertado en la celda
     */
    public String getInput() {
        return formula;
    }
}