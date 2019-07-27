package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

import java.util.List;

import static com.mobiquityinc.domain.Constraints.MAX_INPUT_ITEMS;
import static com.mobiquityinc.domain.Constraints.MAX_PACKAGE_WEIGHT;

/**
 * Solution of the challenge using Dynamic Programming.
 */
public class DPMaxCostPackageFinder implements MaxCostPackageFinder {

    private static void checkPackageWeightLimit(int packageWeightLimit) throws APIException {
        checkMaxValue(packageWeightLimit, MAX_PACKAGE_WEIGHT, "Max weight that a package can take is %f but found that input package can take %f");
    }

    private static void checkMaxNumberOfItems(int numberOfItems) throws APIException {
        checkMaxValue(numberOfItems, MAX_INPUT_ITEMS, "There might be up to %.0f items you need to choose from but found %.0f items");
    }

    private static void checkMaxValue(double actual, double max, String errorMessage) throws APIException {
        if (actual > max) {
            throw new APIException(String.format(errorMessage, actual, max));
        }
    }

    /**
     *
     * This is the core method to solve the proposed challenge.
     *
     * It was used Dynamic Programming to optimize the cost of runtime and avoid calculating all
     * different combinations of items.
     * It was assumed that the weight of an item is a decimal number with two decimal places.
     *
     * The basic concept of the solution using Dynamic Programming is:
     * 1) Create and Initialize a solution matrix with N lines and W columns where N is the number of items
     * and W is the limit weight of the package.
     * 2) For each item i
     * 3) For each weight wi from 1 to W
     * 4) calculate the ideal solution[i][wi]:
     * 4.1) We test if cost of item i plus solution[i-1][W-wi] is greater than the previous ideal Solution. If it is
     * then we have a new ideal solution containing the item i. If not then we update solution[i][wi] with the last
     * costs are equals we check witch one has the smaller weight.
     * 5) When it is reached the last line and last column we have the best solution considering all items and
     * the package weight limit.
     *
     * Time complexity of the algorithm is O(N*M) where N is the number of items and M is the limit weight of the package.
     * Space complexity is also O(N*M) since we use a matrix to store the solutions calculated in previous steps.
     *
     * One of the challenges I faced was to handle the decimal number for item weight and still get the
     * benefits from Dynamic Programming. The strategy chosen was to transform the weight to integers by multiplying them by 100
     * this increases the space used since we have to allocate more space for
     * the matrix used by Dynamic Programming but it is still a reasonable solution.
     *
     * @param items the list of all items that can be added to the package.
     * @param packageWeightLimit weight limit that is supported by the package.
     * @return Solution with all items that their weight sum sup to the max cost value possible.
     * In case there two or more different lists of items that sum up to the max cost value
     * than the list of items with the minimum total weight is chosen.
     *
     * @throws APIException
     */
    @Override
    public Solution findSolution(List<PackageItem> items, int packageWeightLimit) throws APIException {
        checkMaxNumberOfItems(items.size());
        checkPackageWeightLimit(packageWeightLimit);

        packageWeightLimit = packageWeightLimit * 100;

        Solution solution[][] = new Solution[items.size() + 1][packageWeightLimit + 1];
        for (int i = 0; i < items.size() + 1; i++) {
            for (int w = 0; w < packageWeightLimit + 1; w++) {
                solution[i][w] = new Solution();
            }
        }

        for (int i = 1; i < items.size() + 1; i++) {
            for (int w = 100; w < packageWeightLimit + 1; w++) {
                PackageItem item = items.get(i - 1);
                Solution previousSolution = solution[i - 1][w];
                double previousSolutionWeight = previousSolution.getTotalWeight();
                double previousSolutionCost = previousSolution.getTotalCost();
                int wi = (int) (item.getWeight() * 100);

                if (wi <= w) {
                    Solution newPossibleSolution = solution[i - 1][w - wi].add(item);
                    double newPossibleSolutionCost = item.getCost() + solution[i - 1][w - wi].getTotalCost();
                    double newPossibleSolutionWeight = item.getWeight() + solution[i - 1][w - wi].getTotalWeight();

                    if (newPossibleSolutionCost > previousSolutionCost) {
                        solution[i][w] = newPossibleSolution;
                    } else if (newPossibleSolutionCost == previousSolutionCost &&
                            newPossibleSolutionWeight < previousSolutionWeight) {
                        solution[i][w] = newPossibleSolution;
                    } else {
                        solution[i][w] = previousSolution;
                    }
                } else {
                    solution[i][w] = previousSolution;
                }
            }
        }

        return solution[items.size()][packageWeightLimit];
    }
}
