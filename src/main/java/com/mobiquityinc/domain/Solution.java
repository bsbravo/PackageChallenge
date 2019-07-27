package com.mobiquityinc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain object the store the items of a (possible) solution.
 */
public class Solution {

    private List<PackageItem> items = new ArrayList<>();
    private double totalWeight;
    private double totalCost;

    public Solution() {
    }

    private Solution(List<PackageItem> items, double totalWeight, double totalCost) {
        this.items = items;
        this.totalWeight = totalWeight;
        this.totalCost = totalCost;
    }

    public Solution add(PackageItem item) {
        List<PackageItem> newList = new ArrayList<>(items);
        newList.add(item);
        return new Solution(newList, totalWeight + item.getWeight(), totalCost + item.getCost());
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
        return items.stream().map(PackageItem::getIndex).map(i->i+"").collect(Collectors.joining(","));
    }
}
