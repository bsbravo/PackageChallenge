package com.mobiquityinc.packer;

import com.mobiquityinc.domain.PackageItem;
import com.mobiquityinc.domain.Result;
import com.mobiquityinc.util.Tuple;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.io.FileReader;
import com.mobiquityinc.io.OutputWriter;

import java.util.ArrayList;
import java.util.List;

import static com.mobiquityinc.domain.Constraints.MAX_INPUT_ITEMS;
import static com.mobiquityinc.domain.Constraints.MAX_PACKAGE_WEIGHT;
import static java.util.Comparator.comparing;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {

        FileReader inputReader = new FileReader(filePath);
        List<Result> results = new ArrayList<>();

        while (inputReader.hasNextLine()) {
            results.add(result(inputReader.readLine()));
        }

        return new OutputWriter().printSolution(results);
    }

    private static Result result(Tuple<List<PackageItem>, Double> tuple) throws APIException {
        return result(tuple.getValue1(), tuple.getValue2());
    }

    public static Result result(List<PackageItem> items, double maxWeight) throws APIException {

        if(items.size() > MAX_INPUT_ITEMS) {
            throw new APIException(String.format("There might be up to %d items you need to choose from but found %d items", MAX_INPUT_ITEMS, items.size()));
        }

        if(maxWeight > MAX_PACKAGE_WEIGHT) {
            throw new APIException(String.format("Max weight that a package can take is %f but found that input package can take %f", MAX_PACKAGE_WEIGHT, maxWeight));
        }

        List<Result> possibleSolutions = new ArrayList<>();
        possibleSolutions.add(new Result());

        items.forEach(actual -> {
            List<Result> newSolutions = new ArrayList<>();
            possibleSolutions.forEach(r -> {
                if (r.getTotalWeight() + actual.getWeight() <= maxWeight) {
                    Result newResult = r.add(actual);
                    newSolutions.add(newResult);
                }
            });
            possibleSolutions.addAll(newSolutions);
        });

        return possibleSolutions.parallelStream().max(
                comparing(Result::getTotalCost).thenComparing(comparing(Result::getTotalWeight).reversed())
        ).get();
    }

}
