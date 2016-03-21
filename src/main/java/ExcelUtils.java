import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class ExcelUtils {
    private static SimpleDateFormat dateFormat;
    private static Sheet ExcelWSheet;

    private static Workbook  ExcelWBook;

    private static Cell cell;

    private static Row row;

    public static Object[][] getTableArray(String FilePath, String SheetName, Integer numRows, Integer numCols) throws Exception {
        File file=new File(FilePath);
        String[][] tabArray = new String[numRows-5][numCols];
        try {
            ExcelWBook = WorkbookFactory.create(file);

            ExcelWSheet= ExcelWBook.getSheetAt(0);
            for(int i  = 5 ; i < numRows ;i++){
                row =  ExcelWSheet.getRow(i);
                for(int j = 0 ; j< numCols;j++){
                    if(j==0){

                        tabArray[i-5][j] = Long.toString(row.getCell(j).getDateCellValue().getTime());

                    }
                    else tabArray[i-5][j] = Double.toString(row.getCell(j).getNumericCellValue());
                }
            }


        } catch (FileNotFoundException e) {

            System.out.println("Could not read the Excel sheet");

            e.printStackTrace();

        } catch (IOException e) {

            System.out.println("Could not read the Excel sheet");

            e.printStackTrace();

        }
        finally{
            //if(file.exists())file.delete();
        }
        return tabArray;

    }


}