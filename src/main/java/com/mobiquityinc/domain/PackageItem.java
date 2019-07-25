package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

import static com.mobiquityinc.domain.Constraints.*;

public class PackageItem {

    private int index;
    private double weight;
    private double cost;

    public PackageItem(int index, double weight, double cost) throws APIException {
        if (weight > MAX_ITEM_WEIGHT) {
            throw new APIException(String.format("Max weight of an item is %f. Passed %f from item index %d.", MAX_ITEM_WEIGHT, weight, index));
        }
        if (cost > MAX_ITEM_COST) {
            throw new APIException(String.format("Max cost of an item is %f. Passed %f from item index %d.", MAX_ITEM_COST, cost, index));
        }
        this.index = index;
        this.weight = weight;
        this.cost = cost;
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

    @Override
    public String toString() {
        return "PackageItem{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}
