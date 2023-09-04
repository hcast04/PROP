package datos.documents;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Interfaz que representa los documentos que el controlador de datos soporta
 */
public interface PersistanceDocument {

    /**
     * Guarda una hoja o conjunto de hojas en el path especificado
     * @param path Path donde se guardará el documento
     * @param data Hoja o conjunto de hojas a guardar
     */
    void saveFile(String path, List<List<ArrayList<String>>> data);

    /**
     * Carga una hoja o conjunto de hojas del path especificado
     * @param path Path desde donde se cargará el documento
     * @return Hoja o hojas cargadas
     */
    List<List<List<String>>> loadFile(String path) throws IOException;

    void setNames(ArrayList<String> names);

}
