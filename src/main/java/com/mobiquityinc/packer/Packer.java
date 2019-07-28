/*
 * Bruno Bravo
 * LinkedIn: https://www.linkedin.com/in/bsbravo
 * GitHub: https://github.com/bsbravo
 * bruno.soares.bravo@gmail.com
 */

package com.mobiquityinc.packer;

import com.mobiquityinc.domain.DPMaxPackageCostFinder;
import com.mobiquityinc.domain.MaxPackageCostFinder;
import com.mobiquityinc.domain.PackageItem;
import com.mobiquityinc.domain.Solution;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.io.FileReader;
import com.mobiquityinc.io.Reader;
import com.mobiquityinc.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Method {@link #pack(String)} is the entry point method of the challenge and was given from the basic source code.
 * <p>
 * Method {@link DPMaxPackageCostFinder#findSolution(List, int)} is the core method to find best solution given a list
 * of items and the package weight limit. There are comments on that method explaining the approach taken as well the
 * analyse of the time/space complexity of the used algorithm. I believe the rest of the code is pretty much
 * self-explanatory.
 * <p>
 * There is 100% of test coverage by unit tests and all green :)
 * I first started building the unit tests before implementing the solution.
 * The given example in file "Packaging-Challenge.pdf" was one my first test cases.
 * I also built tests to check the enforcement of all the constraints.
 * I've created folder 'test/resources/samples' to add the input files and expected output results for unit tests that
 * compute the solutions.
 * <p>
 * I've followed the Single Responsibility Principle where each class/method is responsible for only one functionality.
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
        MaxPackageCostFinder maxPackageCostFinder = new DPMaxPackageCostFinder();

        List<Solution> solutions = new ArrayList<>();

        while (fileReader.hasNextEntry()) {
            Tuple<List<PackageItem>, Integer> tuple = fileReader.readNextEntry();
            List<PackageItem> items = tuple.getValue1();
            int packageWeightLimit = tuple.getValue2();

            solutions.add(maxPackageCostFinder.findSolution(items, packageWeightLimit));
        }

        return solutions.stream().map(Solution::toString).collect(Collectors.joining("\n"));
    }

}
