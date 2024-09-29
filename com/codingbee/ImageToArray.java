package com.codingbee;

import com.codingbee.snn4j.objects_for_parsing.JsonOne;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageToArray {
    static ObjectMapper mapper = new ObjectMapper();
    static String originalPath = "C:/Users/theco/IdeaProjects/ML/NUMBERS/numbers/trainingSet/trainingSet/";
    static String newPath = "C:/Users/theco/IdeaProjects/ML/NUMBERS/json_one_testing/example";
    static int examplesPerDigit = 50;

    public static double[] readImageAsArray(String filePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(filePath));

        int width = image.getWidth();
        int height = image.getHeight();

        double[] pixelArray = new double[width * height];

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                double gray = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0;
                if(gray> 0.5){
                    pixelArray[index++] = 1;
                }else {
                    pixelArray[index++] = 0;
                }
            }
        }

        return pixelArray;
    }

    public static void processImagesInDirectory(String directoryPath, int number) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if (files == null || files.length == 0) {
            System.out.println("No JPG files found in the directory: " + directoryPath);
            return;
        }
        int i = 0;
        for (File file : files) {
            i++;
            if (i < examplesPerDigit) continue;
            if (i > 2 * examplesPerDigit) break;
            try {
                double[] pixelArray = readImageAsArray(file.getAbsolutePath());
                JsonOne trainingExample = new JsonOne(number, pixelArray);
                String name = file.getName().replace("img_", "").replace(".jpg", "");
                name = newPath + name + ".json";
                mapper.writeValue(new File(name), trainingExample);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file: " + file.getName());
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String directoryPath = originalPath + i;
            processImagesInDirectory(directoryPath, i);
        }
    }
}
