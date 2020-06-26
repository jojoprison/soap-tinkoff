package com.squalorDf.soaptinkoff.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.squalodf.ws.numbers.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * The Repository of SOAP project containing JPA
 * methods and custom {@code findNumber} method.
 */
@Repository
public interface NumberRepository extends JpaRepository<Number, Integer> {

    Logger logger = LoggerFactory.getLogger(NumberRepository.class);

    /**
     * Search input number in 20 existing files.
     * Since {@code FindNumberRequest.getNumber()} returns only an integer value
     * (xjc generated classes), it will return 0 if a string value is received.
     * There is no point in further error handling,
     * but that code is left as a working example.
     *
     * @param number number to be found
     * @return result of search
     */
    default Result findNumber(Integer number) {

        Result result = new Result();

        logger.info("Searching number is " + number);

        // not need to update DB without number
        if (number == null) {

            String error = "The number must not be null";

            logger.error(error);

            result.setCode("02.Result.Error");
            result.setError(error);

            return result;
        }

        // choose 'resources' directory in this project, may choose anything else
        URL filesDirectoryPath = getClass().getClassLoader()
                .getResource("files");
        File filesDirectory;

        // get files directory object
        try {
            filesDirectory = new File(Objects.requireNonNull(filesDirectoryPath).getFile());
        } catch (NullPointerException ex) {

            String error = "Directory of files " + filesDirectoryPath.getPath() + "is not exist";

            logger.error(error);

            result.setCode("02.Result.Error");
            result.setError(error);

            updateDB(number, result);

            return result;
        }

        // get files in files directory
        File[] listOfFiles = filesDirectory.listFiles();

        if (listOfFiles == null) {

            String error = "Directory of files is not exist";

            logger.error(error);

            result.setCode("02.Result.Error");
            result.setError("Directory of files is not exist");

            updateDB(number, result);

            return result;
        }

        // using set to exclude repetition
        Set<String> fileNames = new HashSet<>();

        // start to parsing files
        for (File file : listOfFiles) {
            if (file.isFile()) {

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                    while (br.ready()) {

                        String nextLine = br.readLine();
                        // use comma delimiter with carriage return
                        StringTokenizer st = new StringTokenizer(nextLine, ", " + System.lineSeparator());

                        while (st.hasMoreTokens()) {
                            if (Integer.parseInt(st.nextToken()) == number) {
                                fileNames.add(file.getName());
                            }
                        }
                    }
                } catch (IOException ex) {

                    String error = "Read file error: " + file.getAbsolutePath();

                    logger.error(error);

                    result.setCode("02.Result.Error");
                    result.setError(error);
                    addFileNames(result, fileNames);

                    updateDB(number, result);

                    return result;
                } catch (NumberFormatException ex) {

                    String error = "Not a number was found in file: " + file.getAbsolutePath();

                    logger.error(error);

                    result.setCode("02.Result.Error");
                    result.setError(error);
                    addFileNames(result, fileNames);

                    updateDB(number, result);

                    return result;
                }
            }
        }

        if (result.getCode() == null && fileNames.isEmpty()) {
            result.setCode("01.Result.NotFound");
        } else {
            result.setCode("00.Result.OK");
            result.getFileNames().addAll(fileNames);
        }

        updateDB(number, result);

        logger.info("Successful completion of the search of number " + number);

        return result;
    }

    /**
     * @param result    result of searching number
     * @param fileNames set of file names containing the searched number
     */
    default void addFileNames(Result result, Set<String> fileNames) {
        // using a list we need to get a link to it
        result.getFileNames().addAll(fileNames);
    }

    /**
     * Update DataBase method
     *
     * @param number searched number
     * @param result result of searching number
     */
    default void updateDB(int number, Result result) {

        logger.info("Start to update DB with number " + number);

        // more performance usage
        StringBuilder fileNames = new StringBuilder();

        // split file names
        for (String fileName : result.getFileNames()) {
            fileNames.append(fileName).append(", ");
        }

        // delete last comma
        if (fileNames.length() != 0) {
            fileNames.delete(fileNames.length() - 2, fileNames.length());
        }

        // update DB
        save(new Number(result.getCode(), number, fileNames.toString(), result.getError()));

        logger.info("Successful completion of updating DB with number " + number);
    }
}
