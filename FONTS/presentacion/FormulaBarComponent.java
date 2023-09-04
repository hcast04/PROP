package presentacion;

import javax.swing.*;
import java.awt.*;

/**
 * Representa la barra donde se muestra qué ha escrito el usuario en la celda seleccionada
 */


public class FormulaBarComponent {

    /**
     * Representa la barra que contiene los componentes de la barra de fórmula
     */
    private final JToolBar toolBar;
    /**
     * Campo de texto que muestra el input de la celda seleccionada
     */
    private final JTextField formulaBar;

    /**
     * Constructora
     */
    public FormulaBarComponent() {

        toolBar = new JToolBar();
        JLabel formulaLabel = new JLabel(" Fórmula: ", SwingConstants.CENTER);
        formulaBar = new JTextField();
        formulaBar.setEditable(false);
        JPanel formulaBarPanel = new JPanel();

        GridBagConstraints c = new GridBagConstraints();

        formulaBarPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        formulaBarPanel.setLayout(new GridBagLayout());     // Usamos GridBagLayout porque es mas moldeable con el GridBagConstraints

        // Insertamos label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        formulaBarPanel.add(formulaLabel, c);

        // Insertamos TextField
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        formulaBarPanel.add(formulaBar, c);

        // Insertamos formulaBarPanel en la toolBar
        toolBar.add(formulaBarPanel);
        toolBar.setFloatable(false);

    }

    /**
     * Retorna la instancia de la barra de fórmula
     * @return  JToolBar con los componentes de la barra de fórmula
     */
    public JToolBar getFormulaBarComponent() {
        return toolBar;
    }

    /**
     * Inserta el texto en el campo de texto, que será el input de la celda seleccionada
     * @param input String con el input de la celda seleccionada
     */
    public void setInput(String input) {
        formulaBar.setText(input);
    }

}