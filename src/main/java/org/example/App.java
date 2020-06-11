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

        String modelFile = "modelfile";
        String specFile = "train_nlp.txt";
        trainAndSerialize(specFile, modelFile);
        DoccatModel model = new DoccatModel(new FileInputStream(modelFile));
        System.out.println(model.toString());
        String string = "Учитель логопед";
        String[] input = string.split(" ");
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
        double[] out = myCategorizer.categorize(input);
        String category = myCategorizer.getBestCategory(out);
        System.out.println(category);
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

}
