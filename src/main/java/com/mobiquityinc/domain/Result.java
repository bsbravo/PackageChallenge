package com.mobiquityinc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Result {

    private List<PackageItem> items = new ArrayList<>();
    private double totalWeight;
    private double totalCost;

    public Result() {
    }

    private Result(List<PackageItem> items, double totalWeight, double totalCost) {
        this.items = items;
        this.totalWeight = totalWeight;
        this.totalCost = totalCost;
    }

    public Result add(PackageItem item) {
        List<PackageItem> newList = new ArrayList<>(items);
        newList.add(item);
        return new Result(newList, totalWeight + item.getWeight(), totalCost + item.getCost());
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        if (items.size() == 0) {
            return "-";
        }
        return items.stream().map(packageItem -> String.valueOf(packageItem.getIndex())).collect(Collectors.joining(","));
    }
}
