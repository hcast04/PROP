package dominio.model;

import java.util.*;

/**
 * Representa un documento
 */
public class Document {

    //Attributes
    /**
     * Representa el nombre del documento
     */
    private final String name;

    /**
     * Representa las hojas que tiene un documento
     */
    private final ArrayList<Sheet> cjtSheet;

    /**
     * Representa el máximo número de hojas en un documento
     */
    private final int maxSheets = 20;

    //Constructora

    /**
     * Crea un documento con el nombre especificado y con una hoja de cálculo con un número inicial de celdas especificado.
     * @param nameDocument Nombre del documento.
     */
    public Document(String nameDocument) {
        this.name = nameDocument;
        ArrayList<Sheet> aux = new ArrayList<>();
        Sheet sh = new Sheet("Hoja 1");
        aux.add(sh);
        this.cjtSheet = aux;
    }

    public Document(String nameDocument, int a) {
        this.name = nameDocument;
        this.cjtSheet = new ArrayList<>();
    }

    //Public Methods
    /**
     * Verifica si una hoja con nombre name existe o no
     * @param name Nombre de la hoja a revisar su existencia
     * @return true si la hoja con nombre name no existe, falso en caso contrario
     */
    public boolean notexistsSheet(String name) {
        try {
            for (Sheet sh : cjtSheet) {
                if (sh.getName().equals(name)) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            return true;
        }
        return true;
    }

    /**
     * Retorna el conjunto de hojas del documento
     * @return ArrayList con todas las hojas del documento
     */
    public ArrayList<Sheet> getCjtSheet() {
        return this.cjtSheet;
    }


    /**
     * Retorna el nombre del documento
     * @return String que representa el nombre del documento
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retorna el número máximo de hojas del documento
     * @return int con el número máximo de hojas
     */
    public int getMaxSheets() { return this.maxSheets; }

    /**
     * Añade una hoja con el nombre especificado al documento, en caso de que no exista una hoja con el mismo nombre
     * @param name Un string que representa el nombre de la hoja
     * @return True si se ha añadido, falso en caso contrario
     */
    public boolean addSheet(String name)  {
        if (notexistsSheet(name)) {
            Sheet sh = new Sheet(name);
            cjtSheet.add(sh);
            return true;
        }
        else return false;
    }
    /**
     * Añade una hoja con el nombre especificado al documento y el número de filas y columnas especificado. En caso de que no exista una hoja con el mismo nombre
     * @param name Un string que representa el nombre de la hoja
     * @param rows Un int que representa el número de filas de la hoja
     * @param cols Un int que representa el número de columnas de la hoja
     * @return True si se ha añadido, falso en caso contrario
     */
    public boolean addSheet(String name, int rows, int cols) {
        if (notexistsSheet(name)) {
            Sheet sh = new Sheet(name, rows, cols);
            cjtSheet.add(sh);
            return true;
        }
        else return false;
    }

    /**
     * Elimina la hoja con nombre name del Documento, si existe
     * @param name Nombre de la hoja que se quiere eliminar
     */
    public void deleteSheet(String name) {
        if (cjtSheet.size() <= 1) return;
        for (int i = 0; i < cjtSheet.size(); i++) {
            if (cjtSheet.get(i).getName().equals(name)) {
                cjtSheet.remove(i);
                return;
            }
        }
    }

    /**
     * Elimina la hoja con el indice index del Documento, si existe
     * @param index Indice de la hoja que se quiere eliminar
     * @return True si se ha eliminado, falso en caso contrario
     */
    public boolean deleteSheet(int index) {
        if (cjtSheet.size() <= 1) return false;
        if (index >= 0 && index < this.cjtSheet.size()) {
            cjtSheet.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Modifica el nombre de la hoja indicada, en caso de que no exista ya una hoja con el mismo nombre
     * @param index Número de la hoja a la cual se quiere modificar el nombre
     * @param name Nuevo nombre que se le quiere poner a la hoja
     * @return true si se ha cambiado el nombre, falso en caso contrario
     */
    public boolean setName(int index, String name) {
        if (notexistsSheet(name)) {
            cjtSheet.get(index).setName(name);
            return true;
        }
        return false;
    }

}

