package com.squalorDf.soaptinkoff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * The {@code FileTest} class represents the ability
 * to generate files with numbers
 */
public class FileTest {

    public static void main(String[] args) {
        new FileTest().createFiles();
    }

    /**
     * Number generation method
     */
    public void createFiles() {

        File file;

        for (int f = 1; f <= 20; f++) {

            // create a new file
            file = new File("D:\\ideaProjects\\soap-tinkoff\\src\\main\\resources\\files\\file_" + f + ".txt");

            System.out.println("Start write into " + file.getName());

            try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath())) {

                // length of file in mb
                while (((double) file.length() / (1024 * 1024)) < 1024.) {

                    // numbers array
                    int[] numbers = new int[10000000];
                    Random random = new Random();

                    // fill array
                    for (int i = 0; i < numbers.length; i++) {
                        numbers[i] = random.nextInt(1000);
                    }

                    // lines delimiter
                    int newLine = 0;

                    for (int i = 0; i < numbers.length; i++, newLine++) {

                        if (newLine >= 20) {
                            fileWriter.write("\r\n");
                            newLine = 0;
                        }

                        fileWriter.write(numbers[i] + ", ");
                    }

                    System.out.println("Numbers are written into " + file.getName()
                            + ". Weight: " + ((double) file.length() / (1024 * 1024)) + " mb");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
