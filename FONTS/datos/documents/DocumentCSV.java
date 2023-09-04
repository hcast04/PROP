package datos.documents;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un documento con formato csv, puede cargarlo desde el ordenador o guardarlo en él
 */
public class DocumentCSV implements PersistanceDocument {


    /**
     * Guarda una hoja en el path especificado con formato csv
     * @param path Path donde se guardará el documento
     * @param data Hoja a guardar
     */
    @Override
    public void saveFile(String path, List<List<ArrayList<String>>> data) {
        try {
            FileWriter outputFile = new FileWriter(path);
            convertToCsv(outputFile, data.get(0)); //solo una sheet
            outputFile.flush();
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga una hoja del path especificado con formato csv
     * @param path Path desde donde se cargará el documento
     * @return Hoja cargada
     * @throws IOException Si no se encuentra el documento
     */
    @Override
    public List<List<List<String>>> loadFile(String path) throws IOException {
        List<List<String>> aux = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        convertFromCsv(br, aux);
        List<List<List<String>>> res = new ArrayList<>();
        res.add(aux);
        return res;
    }

    /**
     * Convierte una hoja de datos a formato csv para poder guardarla
     * @param a Archivo donde guardará los datos de la hoja
     * @param data Hoja de datos a guardar
     * @throws IOException Si no se encuentra el documento
     */
    private static void convertToCsv(FileWriter a, List<ArrayList<String>> data) throws IOException {
        for (ArrayList<String> arrayList : data) {
            for (String word2Add : arrayList) {
                if (word2Add.contains(",") && word2Add.contains("\"")) {
                    a.append('"');
                    for (int k = 0; k < word2Add.length(); k++) {
                        if (word2Add.charAt(k) != '"') a.append(word2Add.charAt(k));
                        else {
                            a.append('"');
                            a.append(word2Add.charAt(k));
                        }
                    }
                    a.append('"');
                } else if (word2Add.contains(",")) {
                    a.append('"');
                    a.append(word2Add);
                    a.append('"');
                } else {
                    a.append(word2Add);
                }
                a.append(",");
            }
            a.append("\n");
        }
    }


    /**
     * Convierte un archivo csv a una hoja de datos para poder cargarla
     * @param br Archivo desde donde se cargará la hoja
     * @param res Resultado de la carga
     * @throws IOException Si no se encuentra el documento
     */
    private static void convertFromCsv(BufferedReader br, List<List<String>> res) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            List<String> aux = convertLineCsv(line, ',', '"');
            res.add(aux);
        }
    }


    /**
     * Convierte una linea de un archivo csv a una linea de datos
     * @param line Linea a convertir
     * @param separators Separadores entre los datos (por defecto es ',' al ser un csv)
     * @param quote Si hay una quote en los datos tiene que indicarse al usar strings en la hoja de datos
     * @return Linea de datos
     */
    private static List<String> convertLineCsv(String line, char separators, char quote) {
        List<String> result = new ArrayList<>();
        if (line.isEmpty()) return result; //cuando acaba

        //aqui guardaremos cada string
        StringBuilder currVal = new StringBuilder();
        //Si hay quotes mirar si tenemos comas dentro / otras quotes
        boolean inQuotes = false;
        //
        boolean start = false;
        //en caso de haber doble quotes (probar
        boolean doubleQuotes = false;
        char[] chars = line.toCharArray();
        for (char ch : chars) {
            //si estamos en quotes
            if (inQuotes) {
                start = true;
                //si salimos de quotes
                if (ch == quote) {
                    inQuotes = false;
                    doubleQuotes = false;
                } else {
                    if (ch == '\"') {
                        //tenemos doble quotes, añadimos
                        if (!doubleQuotes) {
                            currVal.append(ch);
                            doubleQuotes = true;
                        }
                        //añadimos valor dentro de doble quotes (incluso comas)
                    } else {
                        currVal.append(ch);
                    }
                }

                //si no estamos en quotes
            } else {
                //empezamos quotes
                if (ch == quote) {
                    inQuotes = true;
                    //si ya teniamos quote, añadimos la quote
                    if (chars[0] != '"') {
                        if (start) {
                            currVal.append('"');
                        }
                    }
                    //si hay coma añadir palabra dado que no estamos en quotes
                } else if (ch == separators) {
                    result.add(currVal.toString());
                    currVal = new StringBuilder();
                    start = false;
                    //si hay linea acabar
                } else if (ch == '\n') {
                    break;
                } else {
                    currVal.append(ch);
                }
            }
        }

        result.add(currVal.toString());
        return result;
    }


    /**
     * Define el nombre de las hoja al guardar un documento
     * @param names Nombres de las hojas
     */
    @Override
    public void setNames(ArrayList<String> names) {

    }
}