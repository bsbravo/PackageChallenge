package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

import static com.mobiquityinc.domain.Constraints.*;

public class PackageItem {

    private static final String ERROR_MSG_MAX_WEIGHT_ITEM = "Max weight of an item is %.2f. Passed %.2f from item index %d.";
    private static final String ERROR_MSG_MAX_ITEM_COST = "Max cost of an item is %.2f. Passed value %.2f from item index %d.";

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
        checkMaxValue(index, weight, MAX_ITEM_WEIGHT, ERROR_MSG_MAX_WEIGHT_ITEM);
    }

    private void checkMaxItemCost(int index, double cost) throws APIException {
        checkMaxValue(index, cost, MAX_ITEM_COST, ERROR_MSG_MAX_ITEM_COST);
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
