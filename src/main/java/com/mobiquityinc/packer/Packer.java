package com.mobiquityinc.packer;

import com.mobiquityinc.domain.DPMaxCostPackageFinder;
import com.mobiquityinc.domain.MaxCostPackageFinder;
import com.mobiquityinc.domain.PackageItem;
import com.mobiquityinc.domain.Solution;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.io.FileReader;
import com.mobiquityinc.io.OutputWriter;
import com.mobiquityinc.io.Reader;
import com.mobiquityinc.util.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Method {@link #pack(String)} is the entry point method of the challenge
 * and was given from the basic source code.
 * <p>
 * Method {@link DPMaxCostPackageFinder#findSolution(List, int)} is the
 * core method to find best solution given a list of items and the package
 * weight limit.
 * <p>
 * There is 100% of test coverage by unit tests and all green :)
 * I first started building the unit tests before implementing the solution.
 * The given example in file "Packaging-Challenge.pdf" was one my firsts unit tests.
 * I also built tests to check the enforcement of all the constraints.
 * I've created folder test/resources/samples to add the input files and expected output results
 * for unit tests that compute the solutions.
 * <p>
 * I've created different classes to read the input, find the ideal solution and print the formatted result
 * following the Single responsibility principle. The same principle is applied to methods inside the classes.
 * <p>
 * <p>
 * That's all :) I’m eager to receive your feedback and I hope I can join Mobiquity team soon.
 *
 * @author Bruno Bravo
 */
public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {

        Reader fileReader = new FileReader(filePath);
        MaxCostPackageFinder finder = new DPMaxCostPackageFinder();

        List<Solution> solutions = new ArrayList<>();

        while (fileReader.hasNextEntry()) {
            Tuple<List<PackageItem>, Integer> tuple = fileReader.readNextEntry();
            List<PackageItem> items = tuple.getValue1();
            int packageWeightLimit = tuple.getValue2();

            solutions.add(finder.findSolution(items, packageWeightLimit));
        }

        return new OutputWriter().formatSolutions(solutions);
    }

}
