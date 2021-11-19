package kim;

import org.apache.commons.cli.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;
import java.util.Random;

/**
 * runs an classifier prediction on the leptograpsus variegatus species dataset.
 * the modelfile that is provided, was tested and trained in weka for optimisation.
 * the model is a meta costsensitiveclassifier with the SimpleLogisic algorithm.
 * the program will also show the perfomance of the algorithm in the output.
 */

public class WekaRunner {
    String [] arguments;
    private static final String HELP = "help";
    private static final String FILE = "file-name";
    private final String modelFile = "testdata/SimpleLogistic.model";
    private Options options;

    public static void main(String[] args) {
        WekaRunner runner = new WekaRunner();
        runner.start(args);
    }

    private void start(String args[]) {
        this.arguments = args;
        buildOptions();
        String filename = arugmentParser();

        String file = "testdata/data1.arff";
        try {

            Instances instances = loadArff(file);
            printInstances(instances);
            CostSensitiveClassifier fromFile = loadClassifier();
            Instances unknownInstances = loadArff(filename);
            System.out.println("\nunclassified unknownInstances = \n" + unknownInstances);
            classifyNewInstance(fromFile, unknownInstances);

            Classifier c1 = loadClassifier();
            Evaluation eval = new Evaluation(instances);
            eval.crossValidateModel(c1, instances, 10, new Random(1));



            CostSensitiveClassifier smp = loadClassifier();
            smp.buildClassifier(instances);

            /* Print the algorithm summary */
            System.out.println("** Decision Tress Evaluation with Datasets **");
            System.out.println(eval.toSummaryString());
            System.out.print(" the expression for the input data as per algorithm is ");
            System.out.println(smp);
            System.out.println(eval.toMatrixString());
            System.out.println(eval.toClassDetailsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildOptions() {
        this.options = new Options();

        Option help = new Option("h", "help", false, "Help function" );
        Option fileName = new Option("f", "file-name", true, "datafile with unknown classified instances" );

        options.addOption(help);
        options.addOption(fileName);
    }




    public String arugmentParser(){
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();

        try {
            CommandLine command = parser.parse(this.options, this.arguments);
            //always prints help option so you can see how to provide the arguments
            helpFormatter.printHelp("Leptograpsus variegatus species predictor", this.options, true);

            if (command.hasOption(HELP)){
                helpFormatter.printHelp("Leptograpsus variegatus species predictor", this.options, true);
                return null;
            } else if (command.hasOption(FILE)){
                return command.getOptionValue(FILE);
            }
        } catch (ParseException e) {
            // Printing the help when something goes wrong
            helpFormatter.printHelp("Leptograpsus variegatus species predictor", this.options, true);
        }
        return null;

    }

    private void classifyNewInstance(CostSensitiveClassifier smp, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = smp.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
    }

    private CostSensitiveClassifier loadClassifier() throws Exception {
        return (CostSensitiveClassifier) weka.core.SerializationHelper.read(modelFile);
    }


    private void printInstances(Instances instances) {
        int numAttributes = instances.numAttributes();

        for (int i = 0; i < numAttributes; i++) {
            System.out.println("attribute " + i + " = " + instances.attribute(i));
        }

        System.out.println("class index = " + instances.classIndex());

    }

    private Instances loadArff(String datafile) throws IOException {
        try {
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e) {
            throw new IOException("could not read from file");
        }
    }
}
