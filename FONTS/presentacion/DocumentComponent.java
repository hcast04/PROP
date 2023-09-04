package presentacion;

import dominio.auxiliarclasses.MyException;
import dominio.auxiliarclasses.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Representa el documento que contiene todas las hojas de cálculo, y desde el cual se puede acceder a cada una de estas hojas
 */


public class DocumentComponent implements MouseListener, ActionListener {

    /**
     * Controlador de presentación
     */
    private final CtrlPresentation ctrlPresentation;
    /**
     * Representa el documento con sus hojas de cálculo
     */
    private final JTabbedPane document;
    /**
     * Instancia de la ventana principal
     */
    private final ExcelFrame mainWindow;

    /**
     * Menú Pop-up con varias opciones para el documento
     */
    private JPopupMenu sheetOptions;
    /**
     * Item del menú Pop-up para borrar una hoja de cálculo determinada
     */
    private JMenuItem deleteSheet;
    /**
     * Item del menú Pop-up para editar el nombre de una hoja de cálculo determinada
     */
    private JMenuItem renameSheet;

    /**
     * Item del menú Pop-up que crea una nueva hoja de cálculo dentro del documento actual
     */
    private JMenuItem createSheet;

    /**
     * Conjunto de hojas de cálculo del documento
     */
    private final ArrayList<SheetComponent> sheets;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            sheetOptions.show(document, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Hacemos que la instancia de sheet en el controlador de dominio sea la misma que la sheet que seleccionamos al clicar en su pestaña
            // de manera que las modificaciones las hacemos en esa sheet
            mainWindow.setSheetFocused(document.getSelectedIndex());
            getSheetFocused().eraseSelectedCells();
            getSheetFocused().clearCellsBackground();
            mainWindow.setInputFormulaBar("");

            try {
                ctrlPresentation.selectSheet(document.getSelectedIndex());
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteSheet) {
            if (!ctrlPresentation.deleteSheet(document.getSelectedIndex())) {
                JOptionPane.showMessageDialog(mainWindow.getMainWindow(), "Como mínimo ha de haber una hoja de cálculo", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (document.getSelectedIndex() == 0) {
                    mainWindow.setSheetFocused(0);
                }
                else {
                    mainWindow.setSheetFocused(document.getSelectedIndex()-1);
                }
                try {
                    ctrlPresentation.updateDocument();
                } catch (MyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        else if (e.getSource() == renameSheet) {
            String name = JOptionPane.showInputDialog("Insertar nuevo nombre de la hoja: ");
            if (name != null) {
                try {
                    if (!ctrlPresentation.renameSheet(document.getSelectedIndex(), name)) {
                        JOptionPane.showMessageDialog(mainWindow.getMainWindow(), "Ya existe una celda con el mismo nombre", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ctrlPresentation.updateDocument();
                    }
                } catch (MyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        else if (e.getSource() == createSheet) {
            try {
                int state = addSheet();
                if (state == 1) {
                    JOptionPane.showMessageDialog(mainWindow.getMainWindow(), "Ya existe una hoja con este nombre o se ha alcanzado el número máximo de hojas", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (state == 0){
                    mainWindow.setSheetFocused(mainWindow.getSheetFocused() + 1);
                    ctrlPresentation.updateDocument();
                }
            } catch (MyException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Constructora
     * @param ctrlPresentation Controlador de presentación
     * @param mainWindow Frame de la ventana principal
     */
    public DocumentComponent(CtrlPresentation ctrlPresentation, ExcelFrame mainWindow) throws MyException {

        this.ctrlPresentation = ctrlPresentation;
        this.mainWindow = mainWindow;
        sheets = new ArrayList<>();
        // Creamos el tabbedPane
        document = new JTabbedPane();
        document.setTabPlacement(JTabbedPane.BOTTOM);

        document.addMouseListener(this);

        for (int i = 0; i < ctrlPresentation.getNSheetsDomain(); i++) {

            SheetComponent sheet = new SheetComponent(ctrlPresentation, ctrlPresentation.getNRows(i), ctrlPresentation.getNColumns(i), ctrlPresentation.getSheetName(i));
            document.add(sheet.getName(), sheet.getSheet());
            sheets.add(sheet);

        }

        setPopupMenu();

    }

    /**
     * Crea y configura el menú Pop-up
     */
    private void setPopupMenu() {

        sheetOptions = new JPopupMenu("Options");
        deleteSheet = new JMenuItem("Delete sheet");
        renameSheet = new JMenuItem("Rename sheet");
        createSheet = new JMenuItem("Create sheet");

        sheetOptions.add(deleteSheet);
        sheetOptions.add(renameSheet);
        sheetOptions.add(createSheet);

        deleteSheet.addActionListener(this);
        renameSheet.addActionListener(this);
        createSheet.addActionListener(this);

    }

    /**
     * Retorna la instancia del documento
     * @return JTabbedPane del documento actual
     */
    public JTabbedPane getDocument() {
        return document;
    }

    /**
     * Añade una hoja a la celda
     * @return 0 en caso de que haya añadido la hoja correctamente, 1 en caso de que no se haya añadido correctamente, 2 en caso de que se haya cancelado la operación
     */
    public int addSheet() throws MyException {
        String nameSheet = JOptionPane.showInputDialog("Name of the new sheet: ");
        if (nameSheet != null) {
            ctrlPresentation.setFormulaBar("");
            if (ctrlPresentation.addSheet(nameSheet)) return 0;
            else return 1;
        }
        return 2;

    }

    /**
     * Establece a qué hoja se están aplicando las operaciones realizadas
     * @param index Entero con el índice de la hoja de cálculo dentro del documento actual
     */
    public void setFocus(int index) throws MyException {
        document.setSelectedIndex(index);
        ctrlPresentation.selectSheet(index);
        mainWindow.setSheetFocused(index);
    }

    /**
     * Retorna el conjunto de posiciones de las celdas seleccionadas de la hoja enfocada
     * @return ArrayList de Pairs con las posiciones de las celdas seleccionadas de la hoja enfocada
     */
    public ArrayList<Pair> selectedCellsFromSheet() {
        return sheets.get(document.getSelectedIndex()).getSelectedCells();
    }

    /**
     * Retorna la instancia de la hoja enfocada
     * @return Instancia de la hoja de cálculo enfocada
     */
    public SheetComponent getSheetFocused() {
        return sheets.get(document.getSelectedIndex());
    }

}