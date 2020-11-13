package kim;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.net.search.fixed.FromFile;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;
import java.util.Random;

/**
 * If you saved a model to a file in WEKA, you can use it reading the generated java object.
 * Here is an example with Random Forest classifier (previously saved to a file in WEKA):
 * import java.io.ObjectInputStream;
 * import weka.core.Instance;
 * import weka.core.Instances;
 * import weka.core.Attribute;
 * import weka.core.FastVector;
 * import weka.classifiers.trees.RandomForest;
 * RandomForest rf = (RandomForest) (new ObjectInputStream(PATH_TO_MODEL_FILE)).readObject();
 * <p>
 * or
 * RandomTree treeClassifier = (RandomTree) SerializationHelper.read(new FileInputStream("model.weka")));
 */


public class WekaRunner {
    // trained model
    private final String modelFile = "C:testdata/Randomforest.model";

    public static void main(String[] args) {
        WekaRunner runner = new WekaRunner();
        runner.start();

    }

    private void start() {
        String datafile = "testdata/diabetic_data_clean.arff";
        String unknownFile = "testdata/diabetic_data_clean_short.arff";

        try {
            //Classifier cls = new RandomForest();
            //RandomForest tree = new RandomForest();
            Instances instances = loadArff(datafile);
            printInstances(instances);

            //saveClassifier(randomforest);

            System.out.println("[LOG]\tdeserialize model:\t" + modelFile);
            RandomForest fromFile = loadClassifier();

            Instances unknownInstances = loadArff(unknownFile);

            System.out.println("\nunclassified unknownInstances = \n" + unknownInstances);
            classifyNewInstance(fromFile, unknownInstances);



            Classifier c1 = new RandomForest();
            Evaluation eval = new Evaluation(instances);
            eval.crossValidateModel(c1, instances, 10, new Random(1));
            //System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));


            RandomForest randomforest = new RandomForest();
            randomforest.buildClassifier(instances);
            /*
             * train the alogorithm with the training data and evaluate the
             * algorithm with testing data
             */
//            Evaluation eval = new Evaluation(instances);
//            eval.evaluateModel(randomforest, instances);


            /* Print the algorithm summary */
            System.out.println("** Decision Tress Evaluation with Datasets **");
            System.out.println(eval.toSummaryString());
            System.out.print(" the expression for the input data as per alogorithm is ");
            System.out.println(randomforest);
            System.out.println(eval.toMatrixString());
            System.out.println(eval.toClassDetailsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void classifyNewInstance(RandomForest tree, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = tree.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
    }

//    private RandomForest loadClassifier() throws Exception {
//        // deserialize model
//        RandomForest tree = (RandomForest) (new ObjectInputStream(modelFile)).readObject();
//        System.out.println("tree" + tree);
//        return (RandomForest) weka.core.SerializationHelper.read(modelFile);
//    }

//    private void saveClassifier(RandomForest randomforest) throws Exception {
//        //post 3.5.5
//        // serialize model
//        weka.core.SerializationHelper.write(modelFile, randomforest);
//    }

//    protected RandomForest loadClassifier() {
//        RandomForest cls = null;
//        try {
//            cls = (RandomForest) weka.core.SerializationHelper.read(modelFile);
//            System.out.println("gelukt");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return cls;
//    }

    private RandomForest loadClassifier() throws Exception {
        // deserialize model
        return (RandomForest) weka.core.SerializationHelper.read(modelFile);
    }

//    private void saveClassifier(RandomForest RandomForest) throws Exception {
//        //post 3.5.5
//        // serialize model
//        //weka.core.SerializationHelper.write(modelFile, RandomForest);
//
//         //serialize model pre 3.5.5
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFile));
//        oos.writeObject(RandomForest);
//        oos.flush();
//        oos.close();
//    }



//    private RandomForest buildClassifier(Instances instances) throws Exception {
//        //String[] options = new String[2];
//        //options[0] = "-P";            // unpruned tree
//        //options[1] = "100";
//        RandomForest tree = new RandomForest();
//        //tree.setOptions(options);     // set the options
//        tree.buildClassifier(instances);   // build classifier
//
//        return tree;
//    }

    private void printInstances(Instances instances) {
        int numAttributes = instances.numAttributes();

        for (int i = 0; i < numAttributes; i++) {
            System.out.println("attribute " + i + " = " + instances.attribute(i));
        }
        //instances.setClassIndex(instances.numAttributes() - 27);
        System.out.println("class index = " + instances.classIndex());
//        Enumeration<Instance> instanceEnumeration = instances.enumerateInstances();
//        while (instanceEnumeration.hasMoreElements()) {
//            Instance instance = instanceEnumeration.nextElement();
//            System.out.println("instance " + instance. + " = " + instance);
//        }

        //or
        int numInstances = instances.numInstances();
        for (int i = 0; i < numInstances; i++) {
            if (i == 5) break;
            Instance instance = instances.instance(i);
            System.out.println("instance = " + instance);
        }
    }

    private Instances loadArff(String datafile) throws IOException {
        try {
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            // setting class attribute if the data format does not provide this information
            // For example, the XRFF format saves the class attribute information as well
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 27);
            System.out.println();
            return data;
        } catch (Exception e) {
            throw new IOException("could not read from file");
        }
    }
}
