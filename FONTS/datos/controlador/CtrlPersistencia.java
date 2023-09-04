package datos.controlador;

import datos.documents.DocumentCSV;
import datos.documents.DocumentXLSX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static datos.auxiliarclasses.DocumentType.formatType;

/**
 * Representa el controlador de datos, capaz de cargar y guardar documentos con formato csv y xlsx
 */

public class CtrlPersistencia {

    /**
     * Representa un documento Csv
     */
    private final DocumentCSV documentCSV;
    /**
     * Representa un documento XLSX
     */
    private final DocumentXLSX documentXLSX;

    public CtrlPersistencia() {
        documentCSV      = new DocumentCSV();
        documentXLSX    = new DocumentXLSX();

    }

    /**
     * Guarda una hoja o conjunto de hojas en el path especificado, si el formato es de csv guardará una hoja, si es xlsx el conjunto de hojas
     * @param path Path donde se guardará el documento
     * @param data Hoja o conjunto de hojas a guardar
     */
    public void saveDocument(String path, List<List<ArrayList<String>>> data) {
        String format = formatType(path);
        if (format.equals("csv")) {
            documentCSV.saveFile(path, data);
        }
        else if (format.equals("xlsx")) {
            documentXLSX.saveFile(path, data);
        }
    }

    /**
     * Carga una hoja o conjunto de hojas del path especificado, si el formato es de csv cargará una hoja, si es xlsx el conjunto de hojas
     * @param path Path desde donde se cargará el documento
     * @param format Formato del documento
     * @return Hoja o conjunto de hojas cargadoç
     * @throws IOException Si no se encuentra el documento
     */
    public List<List<List<String>>> loadDocument(String path, String format) throws IOException {
        if (format.equals("csv")) { //Only saves one sheet
            return documentCSV.loadFile(path);
        }
        else if (format.equals("xlsx")) {
            return documentXLSX.loadFile(path);
        }
        else return null;
    }

    /**
     * Define los nombres de las hojas al guardar o cargar un documento
     * @param names Nombres de las hojas
     * @param format Formato del documento
     */
    public void setnames(String format, ArrayList<String> names) {
        if (format.equals("csv")) documentCSV.setNames(names);
        else documentXLSX.setNames(names); }

}
