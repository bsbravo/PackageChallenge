package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

import java.util.List;

public interface MaxPackageCostFinder {

    Solution findSolution(List<PackageItem> items, int packageWeightLimit) throws APIException;
}
