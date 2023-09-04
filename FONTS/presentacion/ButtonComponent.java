package presentacion;

import dominio.auxiliarclasses.MyException;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Representa cada uno de los índices de las filas y las columnas, con los cuales se puede interactuar para seleccionar la fila, columna, toda la hoja de cálculo entera u otras acciones como
 * insertar o eliminar las filas y las columnas
 */


public class ButtonComponent extends JButton implements MouseListener, ActionListener, KeyListener {

    /**
     * Representa la coordenada x del botón
     */
    private final int x;
    /**
     * Representa la coordenada y del botón
     */
    private final int y;

    /**
     * Controlador de presentación
     */
    private final CtrlPresentation ctrlPresentation;

    /**
     * Representa el menú Pop-up del botón en caso de que sea un índice de columnas
     */
    private final JPopupMenu pmColumnButtons;
    /**
     * Representa el menú Pop-up del botón en caso de que sea un índice de filas
     */
    private final JPopupMenu pmRowButtons;
    /**
     * Item del menú de columnas que añade una columna a la izquierda
     */
    private final JMenuItem addColumnLeft;
    /**
     * Item del menú de columnas que añade una columna a la derecha
     */
    private final JMenuItem addColumnRight;
    /**
     * Item del menú de filas que añade una fila encima
     */
    private final JMenuItem addRowUp;
    /**
     * Item del menú de filas qeu añade una fila debajo
     */
    private final JMenuItem addRowDown;
    /**
     * Item del menú de filas que borra la fila
     */
    private final JMenuItem deleteRow;
    /**
     * Item del menú de columnas que borra la columna
     */
    private final JMenuItem deleteColumn;
    /**
     * Representa la hoja de cálculo en la que se encuentra el botón
     */
    private final SheetComponent sheetComponent;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Cuando hacemos rightclick a un boton
        if (e.getButton() == MouseEvent.BUTTON3 && isColumnButton()) {
            pmColumnButtons.show(this, e.getX(), e.getY());
        }

        else if (e.getButton() == MouseEvent.BUTTON3 && isRowButton()) {
            pmRowButtons.show(this, e.getX(), e.getY());
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addColumnRight) {
            try {
                ctrlPresentation.addColumn(x+1);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ctrlPresentation.updateDocument();
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == addColumnLeft) {
            try {
                // Para añadir a la izquierda, hay que pasar la misma posicion porque el dominio tira todas las casillas desde la indicada hacia la derecha
                ctrlPresentation.addColumn(x);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

            try {
                ctrlPresentation.updateDocument();
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == addRowUp) {
            try {
                ctrlPresentation.addRow(y);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ctrlPresentation.updateDocument();
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == addRowDown) {
            try {
                ctrlPresentation.addRow(y+1);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ctrlPresentation.updateDocument();
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == deleteColumn) {
            try {
                ctrlPresentation.deleteColumn(x);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ctrlPresentation.updateDocument();
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == deleteRow) {
            try {
                ctrlPresentation.deleteRow(y);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ctrlPresentation.updateDocument();
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }

        else if (e.getSource() == this) {
            if (isRowButton()) {
                sheetComponent.setSelectedRow(y);
            }
            else if (isColumnButton()) {
                sheetComponent.setSelectedColumn(x);
            }
            else if (isCornerButton()) {
                sheetComponent.setSelectedEntireSheet();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Constructora
     * @param ctrlPresentation Controlador de presentación
     * @param sheetComponent Hoja donde se encuentra el botón
     * @param text Texto que contiene el botón
     * @param x coordenada x del botón
     * @param y coordenada y del botón
     */
    public ButtonComponent(CtrlPresentation ctrlPresentation, SheetComponent sheetComponent, String text, int x, int y) {

        super();

        this.ctrlPresentation = ctrlPresentation;
        this.sheetComponent = sheetComponent;

        this.setText(text);

        this.x = x;
        this.y = y;

        // Popup menu de los botones de las columnas
        pmColumnButtons = new JPopupMenu("Options");
        addColumnLeft = new JMenuItem("Add column left");
        addColumnRight = new JMenuItem("Add column right");
        deleteColumn = new JMenuItem("Delete column");
        pmColumnButtons.add(addColumnLeft);
        pmColumnButtons.add(addColumnRight);
        pmColumnButtons.add(deleteColumn);

        // Popup menu de los botones de las filas
        pmRowButtons = new JPopupMenu("Options");
        addRowUp = new JMenuItem("Add row up");
        addRowDown = new JMenuItem("Add row down");
        deleteRow = new JMenuItem("Delete row");
        pmRowButtons.add(addRowUp);
        pmRowButtons.add(addRowDown);
        pmRowButtons.add(deleteRow);

        this.addMouseListener(this);
        this.addActionListener(this);
        this.addKeyListener(this);
        addColumnRight.addActionListener(this);
        addColumnLeft.addActionListener(this);
        addRowUp.addActionListener(this);
        addRowDown.addActionListener(this);
        deleteColumn.addActionListener(this);
        deleteRow.addActionListener(this);

    }

    private boolean isRowButton() {
        return (x == -1 && y != -1);
    }

    private boolean isColumnButton() {
        return (x != -1 && y == -1);
    }

    private boolean isCornerButton() {
        return (x == -1 && y == -1);
    }


}