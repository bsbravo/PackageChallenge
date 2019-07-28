package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

import java.util.List;

import static com.mobiquityinc.domain.Constraints.*;

/**
 * Solution using Dynamic Programming.
 */
public class DPMaxPackageCostFinder implements MaxPackageCostFinder {

    private static final String ERROR_MSG_MAX_PACKAGE_WEIGHT = "Max weight that a package can take is %d but found that input package weight limit is %d";
    private static final String ERROR_MSG_MAX_INPUT_ITEMS = "There might be up to %d items choose from but found %d items";

    private static void checkMaxPackageWeightLimit(int packageWeightLimit) throws APIException {
        checkMaxValue(packageWeightLimit, MAX_PACKAGE_WEIGHT, ERROR_MSG_MAX_PACKAGE_WEIGHT);
    }

    private static void checkMaxNumberOfItems(int numberOfItems) throws APIException {
        checkMaxValue(numberOfItems, MAX_INPUT_ITEMS, ERROR_MSG_MAX_INPUT_ITEMS);
    }

    /**
     * This is the core method to solve the proposed challenge.
     * <p>
     * It was used Dynamic Programming to optimize the time complexity and avoid calculating all different combinations
     * of items. We first resolve the problem for smaller subsets and then use those calculated solutions
     * to find the solution for the next steps until we find the final solution.
     * <p>
     * It was assumed that the weight of an item is a decimal number with two decimal places.
     * <p>
     * The basic concept of the solution using Dynamic Programming is:
     * 1) Create and Initialize the matrix named "solutions" with N lines and W columns where N is the number of items
     * and W is the limit weight of the package.
     * 2) For each item i
     * 2.1) For each weight wi from 1 to W
     * 2.1.1) Calculate the ideal solutions[i][wi]:
     *      Compare the cost of item i plus cost of solutions[i-1][W-wi] with the cost of previous ideal
     * Solution.
     *       If it is greater we have a new ideal solution containing the item i.
     *       If it is less we carry the previous ideal solution to solutions[i][wi].
     *       It it is equal we check witch one has the smaller weight.
     * 2.1.2) At the end of step 2.1.1 we'll have in solutions[i][wi] the solution for items from 1 to i and max weight
     * of wi. This is the partial solution for one subset of the problem.
     * 3) When it is reached the last line and last column we'll have the final best solution considering all items and
     * max weight that the package supports.
     * <p>
     * Time complexity of the algorithm is O(N*M) where N is the number of items and M is the limit weight of the package.
     * Space complexity is also O(N*M) since we use a matrix to store the solutions calculated in each step.
     * <p>
     * One of the challenges I faced was to handle the decimal number for items weight and still get the
     * benefits from Dynamic Programming. The strategy chosen was to transform the weight of items to integers by
     * multiplying them by 100, this increases the space used since we have to allocate more space for the used matrix
     * but it is still a reasonable solution.
     *
     * @param items              the list of all items that can be added to the package.
     * @param packageWeightLimit weight limit that is supported by the package.
     * @return Solution with all items that their costs sum up to the max cost value possible.
     * In case there are two or more different lists of items that sum up to the max cost value
     * than the list of items with the minimum total weight is chosen.
     * @throws APIException in case of any constraint violation.
     */
    @Override
    public Solution findSolution(List<PackageItem> items, int packageWeightLimit) throws APIException {
        checkMaxNumberOfItems(items.size());
        checkMaxPackageWeightLimit(packageWeightLimit);

        int maxPackageWeightLimit = packageWeightLimit * 100;
        Solution[][] solutions = new Solution[items.size() + 1][maxPackageWeightLimit + 1];

        for (int i = 0; i <= items.size(); i++) {
            for (int w = 0; w <= maxPackageWeightLimit; w++) {
                solutions[i][w] = new Solution();
            }
        }

        for (int i = 1; i <= items.size(); i++) {
            PackageItem item = items.get(i - 1);
            for (int w = 100; w <= maxPackageWeightLimit; w++) {
                Solution previousSolution = solutions[i - 1][w];
                int wi = (int) (item.getWeight() * 100);

                if (wi > w) {
                    solutions[i][w] = previousSolution;
                    continue;
                }

                double previousSolutionWeight = previousSolution.getTotalWeight();
                double previousSolutionCost = previousSolution.getTotalCost();
                Solution newPossibleSolution = solutions[i - 1][w - wi].add(item);
                double newPossibleSolutionCost = item.getCost() + solutions[i - 1][w - wi].getTotalCost();
                double newPossibleSolutionWeight = item.getWeight() + solutions[i - 1][w - wi].getTotalWeight();

                if (newPossibleSolutionCost > previousSolutionCost) {
                    solutions[i][w] = newPossibleSolution;
                } else if (newPossibleSolutionCost == previousSolutionCost &&
                        newPossibleSolutionWeight < previousSolutionWeight) {
                    solutions[i][w] = newPossibleSolution;
                } else {
                    solutions[i][w] = previousSolution;
                }
            }
        }

        return solutions[items.size()][maxPackageWeightLimit];
    }
}
