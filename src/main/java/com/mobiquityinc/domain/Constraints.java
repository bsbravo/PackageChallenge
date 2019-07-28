package com.mobiquityinc.domain;

import com.mobiquityinc.exception.APIException;

public class Constraints {

    public static final int MAX_PACKAGE_WEIGHT = 100;
    public static final double MAX_ITEM_WEIGHT = 100;
    public static final double MAX_ITEM_COST = 100;
    public static final int MAX_INPUT_ITEMS = 15;

    public static void checkMaxValue(int actual, int max, String errorMessage) throws APIException {
        if (actual > max) {
            throw new APIException(String.format(errorMessage, max, actual));
        }
    }

    public static void checkMaxValue(int index, double actual, double max, String errorMessage) throws APIException {
        if (actual > max) {
            throw new APIException(String.format(errorMessage, max, actual, index));
        }
    }
}
