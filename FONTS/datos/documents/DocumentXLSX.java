package datos.documents;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/**
 * Representa un documento XLSX, puede cargarlo desde el ordenador o guardarlo en él
 */
public class DocumentXLSX implements PersistanceDocument {

    /**
     * Representa los nombres de las hojas de datos del documento xlsx
     */
    private ArrayList<String> sheetNames;

    /**
     * Guarda una hoja o un conjunto de hojas en el path especificado con formato xlsx
     * @param path Path donde se guardará el documento
     * @param data Hoja/hojas a guardar
     */
    @Override
    public void saveFile(String path, List<List<ArrayList<String>>> data) {
        Workbook wb = new XSSFWorkbook();
        int SheetSize = data.size();
        for (int i = 0; i < SheetSize; i++) {
            Sheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName(sheetNames.get(i)));
            for (int j = 0; j < data.get(i).size(); j++) {
                Row row = sheet.createRow(j);
                for (int k = 0; k < data.get(i).get(j).size(); k++) {
                    Cell cell = row.createCell(k);
                    cell.setCellValue(data.get(i).get(j).get(k));
                }
            }
        }
        OutputStream fileOut = null;
        try   {
             fileOut = new FileOutputStream(path);
             wb.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileOut != null) fileOut.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            }
    }

    /**
     * Carga una hoja o conjunto de hojas del path especificado con formato xlsx
     * @param path Path desde donde se cargará el documento
     * @return Hoja o hojas cargadas
     * @throws IOException Si no se encuentra el documento
     */
    @Override
    public List<List<List<String>>> loadFile(String path) throws IOException {
        List<List<List<String>>> res = new ArrayList<>();
        FileInputStream fis=new FileInputStream(path);
        Workbook wb= new XSSFWorkbook(fis);
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(0);
            String name = sheet.getSheetName();
            int rowsize = sheet.getLastRowNum();
            if (rowsize < 26) rowsize = 26;
            List<List<String>> aux = new ArrayList<>(Collections.nCopies(rowsize, new ArrayList<>()));
            for (Row row : sheet) {
                int colsize = row.getLastCellNum();
                if (colsize < 26) colsize = 26;
                List<String> aux2 = new ArrayList<>(Collections.nCopies(colsize +  1, ""));
                aux2.set(0,name);
                for (Cell cell: row) {
                    DataFormatter dataFormatter = new DataFormatter();
                    String formattedCellStr;
                    if (cell.getCellType().equals(CellType.FORMULA)) formattedCellStr = "="+dataFormatter.formatCellValue(cell);
                    else formattedCellStr = dataFormatter.formatCellValue(cell);
                    aux2.set(cell.getColumnIndex() + 1, formattedCellStr);

                }
                aux.set(row.getRowNum(), aux2);
            }
            res.add(aux);
        }
        return res;
    }

    /**
     * Define los nombres de las hojas al guardar o cargar un documento con formato xlsx
     * @param names Nombres de las hojas
     */
    @Override
    public void setNames(ArrayList<String> names) {
        this.sheetNames = names;
    }


}
