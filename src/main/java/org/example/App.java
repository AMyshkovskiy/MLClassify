package org.example;


import opennlp.tools.doccat.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws IOException {

        String modelFile = "modelfile";
        String specFile = "train_nlp.txt";
        String excelFileName = "E:\\Работа\\Рабочие Excel\\Укрупненная группа 08062020.xlsx";
        //trainAndSerialize(specFile, modelFile);
        DoccatModel model = new DoccatModel(new FileInputStream(modelFile));
        /*String string = "Первый помощник механика";
        String[] input = string.split(" ");
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
        double[] out = myCategorizer.categorize(input);
        String category = myCategorizer.getBestCategory(out);
        System.out.println(category);*/
        workWithExcel(model, excelFileName);
    }

    public static DoccatModel trainingModel(final String specFile) throws IOException {
        InputStreamFactory dataIn = new InputStreamFactory() {
            @Override
            public InputStream createInputStream() throws IOException {
                return new FileInputStream(specFile);
            }
        };
        DoccatModel model = null;
        TrainingParameters trparam = new TrainingParameters();
        trparam.put("Iterations", 500);
        DoccatFactory docfactory = new DoccatFactory();
        ObjectStream<String> lineStream =
                new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
        model = DocumentCategorizerME.train("ru", sampleStream, trparam, docfactory);
        return model;
    }

    public static void serializeModel(DoccatModel model, String modelFile) throws IOException {
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
            model.serialize(modelOut);
        }
    }

    public static void trainAndSerialize(String specFile, String modelFile) throws IOException {
        DoccatModel model = trainingModel(specFile);
        serializeModel(model, modelFile);
    }

    public static void workWithExcel(DoccatModel model, String excelFileName) throws IOException {
        File file = new File(excelFileName);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            XSSFCell cell = (XSSFCell) row.getCell(0);
            String position = cell.getStringCellValue();
            String[] input = position.split(" ");
            DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
            double[] out = myCategorizer.categorize(input);
            String category = myCategorizer.getBestCategory(out);
            XSSFCell cell4 = (XSSFCell) row.createCell(3);
            cell4.setCellValue(category);

        }
        FileOutputStream out = new FileOutputStream(excelFileName);
        workbook.write(out);
        out.close();
    }

}
