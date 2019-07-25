package com.mobiquityinc.io;

import com.mobiquityinc.domain.PackageItem;
import com.mobiquityinc.util.Tuple;
import com.mobiquityinc.exception.APIException;

import java.util.List;

public interface Reader {

    boolean hasNextLine();

    Tuple<List<PackageItem>, Double> readLine() throws APIException;
}
