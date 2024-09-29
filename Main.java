package com.codingbee;

import com.codingbee.snn4j.enums.DataFormat;
import com.codingbee.snn4j.exceptions.FileManagingException;
import com.codingbee.snn4j.exceptions.MethodCallingException;
import com.codingbee.snn4j.helping_objects.Dataset;

import com.codingbee.snn4j.exceptions.IncorrectDataException;
import com.codingbee.snn4j.neural_network.MLP;

import static java.lang.Runtime.getRuntime;

public class Main {
    static String networkPath = "src/main/resources";
    static String trainingDataPath = "C:/Users/theco/IdeaProjects/ML/NUMBERS/json_one_training";
    static String testingDataPath = "C:/Users/theco/IdeaProjects/ML/NUMBERS/new_translation";

    public static void main(String[] args) throws Exception {
        MLP network = new MLP(784, 10, new int[]{10, 10}, "digit_recognition_network");
        train(network);
        test(network);
        getRuntime().gc();
    }

    public static void train(MLP network) throws IncorrectDataException, FileManagingException, MethodCallingException {
        System.out.println("Generating values");
        network.generateRandomNeuronsInDir(networkPath);

        System.out.println("Initializing the network");
        network.initNeuronsFromDir(networkPath);


        System.out.println("Loading data");
        Dataset data = new Dataset();
        data.loadData(trainingDataPath, DataFormat.JSON_ONE, 10);
        System.out.println("\n \n \n TRAINING: \n");
        network.train(data, 100, true, true);

        network.saveNetworksValues(networkPath);
    }

    public static void test(MLP network) throws IncorrectDataException, FileManagingException, MethodCallingException {
        network.initNeuronsFromDir(networkPath);

        Dataset data = new Dataset();
        data.loadData(testingDataPath, DataFormat.JSON_ONE, 10);

        System.out.println("Correctness: " + network.getCorrectPercentage(data) + "%");
        System.out.println("Cost: " + network.calculateAverageCost(data.getInputData(), data.getExpectedResults()));
    }
}