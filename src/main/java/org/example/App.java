package org.example;


import opennlp.tools.doccat.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;


import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, world!");

        /*InputStreamFactory dataIn = new InputStreamFactory() {
            @Override
            public InputStream createInputStream() throws IOException {
                return new FileInputStream("train_nlp.txt");
            }
        };
        TrainingParameters trparam = new TrainingParameters();
        trparam.put("Iterations", 500);
        //trparam.put("Algorithm", "NAIVEBAYES");
        DoccatFactory docfactory = new DoccatFactory();
        ObjectStream<String> lineStream =
                new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        model = DocumentCategorizerME.train("ru", sampleStream, trparam, docfactory);
        String modelFile = "modelfile";
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
            model.serialize(modelOut);
        }*/
        String modelFile = "modelfile";
        DoccatModel model = new DoccatModel(new FileInputStream(modelFile));
        System.out.println(model.toString());
        String string = "Учитель-логопед";
        String[] input = string.split(" ");
        //input[0] = "первый";
        //input[0] = "механика";
        //input[1] = "механика";
        //input[1] = "по";
        //input[2] = "ИТ";
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
        double[] out = myCategorizer.categorize(input);
        String[] groups = new String[100];
        for (int i = 0; i < out.length; i++) {
            groups[i] = myCategorizer.getCategory(i);
        }
        String category = myCategorizer.getBestCategory(out);
        int indexOfCat = myCategorizer.getIndex(category);
        System.out.println(category);
        //myCategorizer.getAllResults()
    }

}
