package kim;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;
import java.util.Random;

/**
 This program to calculated the expected values of a dataset while using machine learning.
 it can build a weka alogrithm model of use a previous build model to calculate the accuracy in which the model can
 predict the classifier value.
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
            Instances instances = loadArff(datafile);
            printInstances(instances);

            System.out.println("[LOG]\tdeserialize model:\t" + modelFile);
            RandomForest fromFile = loadClassifier();

            Instances unknownInstances = loadArff(unknownFile);

            System.out.println("\nunclassified unknownInstances = \n" + unknownInstances);
            classifyNewInstance(fromFile, unknownInstances);


            Classifier c1 = new RandomForest();
            Evaluation eval = new Evaluation(instances);
            eval.crossValidateModel(c1, instances, 10, new Random(1));


            RandomForest randomforest = new RandomForest();
            randomforest.buildClassifier(instances);

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
        Instances labeled = new Instances(unknownInstances);
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = tree.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
    }


    private RandomForest loadClassifier() throws Exception {
        return (RandomForest) weka.core.SerializationHelper.read(modelFile);
    }



    private void printInstances(Instances instances) {
        int numAttributes = instances.numAttributes();

        for (int i = 0; i < numAttributes; i++) {
            System.out.println("attribute " + i + " = " + instances.attribute(i));
        }
        System.out.println("class index = " + instances.classIndex());


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
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 27);
            System.out.println();
            return data;
        } catch (Exception e) {
            throw new IOException("could not read from file");
        }
    }
}
