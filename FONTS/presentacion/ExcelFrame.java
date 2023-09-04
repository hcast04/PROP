package presentacion;

import dominio.auxiliarclasses.MyException;
import dominio.auxiliarclasses.Pair;
import dominio.auxiliarclasses.ReferenceConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Representa la ventana principal que contiene todos los componentes de la aplicación, siendo lo primero que se ejecuta al inicializar el programa
 */

public class ExcelFrame implements ActionListener {

    /**
     * Representa la ventana principal en la que se encuentra todos los componentes
     */
    private JFrame mainWindow;
    /**
     * Controlador de presentación
     */
    private final CtrlPresentation ctrlPresentation;
    /**
     * Representa el documento en el que se está ejecutando las operaciones
     */
    private DocumentComponent documentComponent;
    /**
     * Representa la barra de texto en la que se muestra el input de la celda seleccionada
     */
    private FormulaBarComponent formulaBarComponent;

    /**
     * Representa el menú de la aplicación
     */
    private JMenuBar menuBar;
    /**
     * Componente del menú que gestiona la creación, guardado y carga de documentos
     */
    private JMenu fileMenu;
    /**
     * Componente del menú que gestiona operaciones de bloque en la hoja de cálculo enfocada
     */
    private JMenu editMenu;

    /**
     * Item del menú que crea nuevo documento
     */
    private JMenuItem newDocument;
    /**
     * Item del menú que carga nuevo documento
     */
    private JMenuItem loadDocument;
    /**
     * Item del menú que guarda el documento actual
     */
    private JMenuItem saveDocument;

    /**
     * Item del menú que ordena con un criterio seleccionado por el usuario (creciente o decreciente) el bloque de celdas seleccionado
     */
    private JMenuItem sortBlock;
    /**
     * Item del menú que copia el bloque de celdas seleccionado en la posición indicada
     */
    private JMenuItem copyBlock;
    /**
     * Item del menú que mueve el bloque de celdas seleccionado a la posición indicada
     */
    private JMenuItem moveBlock;
    /**
     * Item del menú que reemplaza cierto elemento indicado por el usuario por otro también indicado por el usuario dentro del bloque de celdas seleccionado
     */
    private JMenuItem replaceBlock;
    /**
     * Item del menú que busca cierto elemento indicado por el usuario dentro del bloque de celdas seleccionado por el usuario
     */
    private JMenuItem searchBlock;

    /**
     * Booleano que indica si los cambios en el documento actual han sido guardados o no
     */
    private boolean save;
    /**
     * Entero que indica qué hoja de cálculo está enfocada para realizar operaciones
     */
    private int sheetFocused;
    /**
     * Fichero actual
     */
    File actualFile;

    // Creadora de la ventana principal

    /**
     * Constructora
     * @param ctrlPresentation Controlador de presentación
     */
    public ExcelFrame(CtrlPresentation ctrlPresentation) throws MyException {

        this.ctrlPresentation = ctrlPresentation;
        initializeComponents();
        setMenu();
        makeVisible();

    }


    /**
     * Inicializa todos los componentes de la ventana
     */
    public void initializeComponents() throws MyException {

        // Configuramos la ventana princial
        mainWindow = new JFrame();
        mainWindow.setTitle("Excel");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1080, 720);
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setResizable(true);

        // Documento no guardado
        save = true;
        sheetFocused = 0;

        // Inicializamos el documento
        documentComponent = new DocumentComponent(ctrlPresentation, this);
        documentComponent.setFocus(0);

        // Inicializamos el FormulaBarComponent
        formulaBarComponent = new FormulaBarComponent();
        mainWindow.add(formulaBarComponent.getFormulaBarComponent(), BorderLayout.NORTH);

        // Añadimos el documento a la window
        mainWindow.add(documentComponent.getDocument(), BorderLayout.CENTER);

    }


    /**
     * Crea y configura la barra de menú de la aplicación
     */
    public void setMenu() {

        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");

        newDocument = new JMenuItem("New document");
        loadDocument = new JMenuItem("Load document");
        saveDocument = new JMenuItem("Save document");

        fileMenu.add(newDocument);
        fileMenu.add(loadDocument);
        fileMenu.add(saveDocument);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        sortBlock = new JMenuItem("Sort block");
        copyBlock = new JMenuItem("Copy block");
        moveBlock = new JMenuItem("Move block");
        replaceBlock = new JMenuItem("Replace content in block");
        searchBlock = new JMenuItem("Search content in block");

        editMenu.add(sortBlock);
        editMenu.add(copyBlock);
        editMenu.add(moveBlock);
        editMenu.add(searchBlock);
        editMenu.add(replaceBlock);



        // Action listeners
        newDocument.addActionListener(this);
        loadDocument.addActionListener(this);
        saveDocument.addActionListener(this);
        sortBlock.addActionListener(this);
        copyBlock.addActionListener(this);
        moveBlock.addActionListener(this);
        searchBlock.addActionListener(this);
        replaceBlock.addActionListener(this);

        this.mainWindow.setJMenuBar(menuBar);

    }

    /**
     * Inserta el input de la celda seleccionada en la barra de fórmula
     * @param input String con el input de la celda seleccionada
     */
    public void setInputFormulaBar(String input) {
        formulaBarComponent.setInput(input);
    }

    /**
     * Retorna la instancia de la ventana principal
     * @return JFrame que representa la ventana principal
     */
    public JFrame getMainWindow() {
        return mainWindow;
    }

    /**
     * Actualiza el documento borrando los datos mostrados actualmente y reinsertando los datos actualizados de la capa de dominio
     */
    public void updateDocument() throws MyException {
        mainWindow.remove(documentComponent.getDocument());     // Cuando hacemos el update primero hay que borrar el documento que hay actualmente y luego substituirlo
                                                                // por la nueva version
        documentComponent = new DocumentComponent(ctrlPresentation, this);
        mainWindow.add(documentComponent.getDocument(), BorderLayout.CENTER);
        documentComponent.setFocus(sheetFocused);
        save = false;
        makeVisible();
    }

    /**
     * Establece la hoja de cálculo indicada como la hoja de cálculo que se está enfocando a la hora de hacer operaciones
     * @param index Integer con el índice de la hoja de cálculo dentro del documento en la que se están aplicando las operaciones
     */
    public void setSheetFocused(int index) {
        sheetFocused = index;
    }

    /**
     * Retorna la hoja que está enfocada actualmente
     * @return Entero con el índice de la hoja enfocada actualmente
     */
    public int getSheetFocused() {
        return sheetFocused;
    }

    /**
     * Permite que todos los componentes sean visibles, al igual que la ventana principal
     */
    public void makeVisible() {
        mainWindow.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newDocument) {
            // generar nuevo documento y setear la vista
            if (!this.save){
                int answer = JOptionPane.showOptionDialog(null, "Do you wish to save the changes?", "Excel", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE, null, null, 0);
                if (answer == 0) {
                    JFileChooser fileChooser = new JFileChooser();
                    int response = fileChooser.showSaveDialog(null);    // seleccionar file para guardar
                    if (response == JFileChooser.APPROVE_OPTION) {
                        actualFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                    this.save = true;

                }
                this.mainWindow.setVisible(false);
                this.mainWindow.dispose();
                CtrlPresentation ctrlPresentation = null;
                try {
                    ctrlPresentation = new CtrlPresentation();
                } catch (MyException ex) {
                    ex.printStackTrace();
                }


            }

            else {
                this.mainWindow.setVisible(false);
                this.mainWindow.dispose();
                CtrlPresentation ctrlPresentation = null;
                try {
                    ctrlPresentation = new CtrlPresentation();
                } catch (MyException ex) {
                    ex.printStackTrace();
                }

            }
        }
        else if (e.getSource() == loadDocument) {           //evaluar con booleano tambien cuando quiero cargar otro documento

            if (!this.save){
                int answer = JOptionPane.showOptionDialog(null, "Do you wish to save changes?", "Excel", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE, null, null, 0);
                if (answer == 0) {
                    JFileChooser fileChooser = new JFileChooser();
                    int response = fileChooser.showSaveDialog(null);    // seleccionar file para guardar
                    if (response == JFileChooser.APPROVE_OPTION) {
                        save = true;
                        actualFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);    // seleccionar file para abrir
            if (response == JFileChooser.APPROVE_OPTION) {
                actualFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    ctrlPresentation.loadDocument(actualFile.getAbsolutePath());
                } catch (MyException | IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                try {
                    ctrlPresentation.updateDocument();
                } catch (MyException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getSource() == saveDocument) {
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showSaveDialog(null);    // seleccionar file para guardar
            if (response == JFileChooser.APPROVE_OPTION) {
                actualFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    ctrlPresentation.saveDocument(actualFile.getAbsolutePath());
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            this.save = true;
        }

        else if (e.getSource() == sortBlock) {
            String[] criterios = {"Creciente", "Decreciente"};
            int criterio = JOptionPane.showOptionDialog(null, "Elige un criterio: ", "", 0, JOptionPane.QUESTION_MESSAGE, null, criterios, "Creciente");
            try {
                ctrlPresentation.sortSelectedBlock(documentComponent.selectedCellsFromSheet(), criterio+1);
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
        else if (e.getSource() == copyBlock) {
            String pos = JOptionPane.showInputDialog("Insertar celda con menor índice de fila y columna a partir de la cual quieres empezar a copiar contenido");
            if (pos != null) {
                Pair p = ReferenceConverter.getReferencePos(pos);

                Pair pMin = documentComponent.getSheetFocused().getMinSelectedCell();

                int offsetX = p.getX() - pMin.getX();
                int offsetY = p.getY() - pMin.getY();
                ArrayList<Pair> newPositions = new ArrayList<>();
                for (int i = 0; i < documentComponent.selectedCellsFromSheet().size(); i++) {
                    newPositions.add(new Pair(documentComponent.selectedCellsFromSheet().get(i).getX() + offsetX, documentComponent.selectedCellsFromSheet().get(i).getY() + offsetY));
                }

                try {
                    ctrlPresentation.copySelectedBlock(documentComponent.selectedCellsFromSheet(), newPositions);
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

        else if (e.getSource() == moveBlock) {
            String pos = JOptionPane.showInputDialog("Insertar celda con menor índice de fila y columna a partir de la cual quieres empezar a mover contenido");
            if (pos != null) {
                Pair p = ReferenceConverter.getReferencePos(pos);

                Pair pMin = documentComponent.getSheetFocused().getMinSelectedCell();

                int offsetX = p.getX() - pMin.getX();
                int offsetY = p.getY() - pMin.getY();
                ArrayList<Pair> newPositions = new ArrayList<>();
                for (int i = 0; i < documentComponent.selectedCellsFromSheet().size(); i++) {
                    newPositions.add(new Pair(documentComponent.selectedCellsFromSheet().get(i).getX() + offsetX, documentComponent.selectedCellsFromSheet().get(i).getY() + offsetY));
                }

                try {
                    ctrlPresentation.moveSelectedBlock(documentComponent.selectedCellsFromSheet(), newPositions);
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

        else if (e.getSource() == searchBlock) {
            String search = JOptionPane.showInputDialog("Introduce el contenido que quieres buscar: ");
            if (search != null) {
                ArrayList<Pair> foundCells;
                try {
                    foundCells = ctrlPresentation.searchContSelectedBlock(documentComponent.selectedCellsFromSheet(), search);
                } catch (MyException ex) {
                    throw new RuntimeException(ex);
                }
                documentComponent.getSheetFocused().setSelectedCells(foundCells);
            }

        }

        else if (e.getSource() == replaceBlock) {
            String replacee = JOptionPane.showInputDialog("Introduce el contenido que quieres reemplazar: ");
            if (replacee != null) {
                String replacer = JOptionPane.showInputDialog("Introduce el contenido por el cual lo quieres reemplazar: ");
                if (replacer != null) {
                    try {
                        ctrlPresentation.replaceContSelectedBlock(documentComponent.selectedCellsFromSheet(), replacee, replacer);
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

    }

}