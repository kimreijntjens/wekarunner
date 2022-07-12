package kim;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.IOException;



/**
 * This program is used to predict the species colour of the Leptograpsus variegatus, a rock crab that has an
 * orange or blue coloured species. The program uses a SimpleLogistic model to predict the colour based on the
 * body measurements. This program can be fed through the commandline with an arff file containing the body measurements,
 * yet unknown colour of the species.
 */


public class WekaRunner {
    /**
     * main class that either runs with the file given on the commandline or the default file
     * @param args: the commandline arguments: jar file and optional the data file
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        WekaRunner runWeka = new WekaRunner();
        Cli argsCli = new Cli(args);
        // if present uses the filename given from the commandline
        String inputFile = argsCli.fileName;
        // If not, uses the default file stated below
        if (inputFile == null){
            inputFile = "testdata/data1_short.arff";

        }

        // run program and classify the unknown instances
        runWeka.start(inputFile);
    }

    /**
     * uses or the inputFile from commandline or the default file.
     * loads the unknown instances that are to be predicted whether they are the orange or the blue species
     * @param inputFile: String with the name of the file containing the unknown instances.
     */
    private void start(String inputFile) {
        try {
            AbstractClassifier fromFile = loadClassifier();
            Instances unknownInstances = loadFile(inputFile);
            System.out.println(unknownInstances.getClass());
            // print the lines that are in the file
            // lines should end with a ?
            // this is the value that will be predicted by the model
            System.out.println("\nunclassified unknownInstances = \n" + unknownInstances);
            classifyNewInstance(fromFile, unknownInstances);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * labels the instances with the correct species colour predicted with the model
     * @param classifier: object that holds the classifier for the unknown instances.
     * @param unknownInstances: object that holds the instances that are to be classified.
     */
    private void classifyNewInstance(AbstractClassifier classifier, Instances unknownInstances) throws Exception {
        Instances labeled = new Instances(unknownInstances);
        // classifies every line with the correct species predicted with the model
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = classifier.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
    }

    /**
     * holds the object that contains the weka model, used for predicting the species colour of the unknown instances.
     * this is in this case a Simple Logistic model tested beforehand in weka.
     */
    private AbstractClassifier loadClassifier() throws Exception {
        final String wekaFile = "testdata/SimpleLogistic.model";
        return (AbstractClassifier) weka.core.SerializationHelper.read(wekaFile);
    }

    /**
     * loads the instances to be classified and sets the index of the to be predicted value on the
     * last attribute. this is the default value when working in weka.
     * @param datafile: string containing the unknown instances.
     */
    private Instances loadFile(String datafile) throws IOException {
        try {
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            // set unknown value on last attribute
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
            // when something goes wrong in loading, throw an error
        } catch (Exception e) {
            throw new IOException("error in reading the file");
        }
    }
}


