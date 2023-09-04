package datos.auxiliarclasses;

/**
 * Representa una clase auxiliar que sirve para retornar el formato de un documento
 */
public class DocumentType {

    /**
     * Devuelve el formato de un documento leyendo el path donde se guarda o de donde se carga
     * @param path Path donde se encuentra el documento o donde se quiere guardar
     * @return Formato del documento
     */
    public static String formatType(String path) {
        int dotIndex = path.lastIndexOf('.');
        return (dotIndex == -1) ? "" : path.substring(dotIndex + 1);
    }
}
