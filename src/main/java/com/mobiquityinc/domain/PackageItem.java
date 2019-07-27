package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

import static com.mobiquityinc.domain.Constraints.MAX_ITEM_COST;
import static com.mobiquityinc.domain.Constraints.MAX_ITEM_WEIGHT;

public class PackageItem {

    private int index;
    private double weight;
    private double cost;

    public PackageItem(int index, double weight, double cost) throws APIException {
        checkMaxItemWeight(index, weight);
        checkMaxItemCost(index, cost);

        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    private void checkMaxItemWeight(int index, double weight) throws APIException {
        checkMaxValue(index, weight, MAX_ITEM_WEIGHT, "Max weight of an item is %f. Passed %f from item index %d.");
    }

    private void checkMaxItemCost(int index, double cost) throws APIException {
        checkMaxValue(index, cost, MAX_ITEM_COST, "Max cost of an item is %f. Passed %f from item index %d.");
    }

    private void checkMaxValue(int index, double actual, double max, String errorMessage) throws APIException {
        if (actual > max) {
            throw new APIException(String.format(errorMessage, max, actual, index));
        }
    }

    public int getIndex() {
        return index;
    }

    public double getWeight() {
        return weight;
    }

    public double getCost() {
        return cost;
    }

}
