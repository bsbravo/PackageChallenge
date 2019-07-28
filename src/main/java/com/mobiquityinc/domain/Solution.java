package com.mobiquityinc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain object that stores the items of a solution.
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
        List<PackageItem> newListItems = new ArrayList<>(items);
        newListItems.add(item);
        return new Solution(newListItems, totalWeight + item.getWeight(), totalCost + item.getCost());
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
