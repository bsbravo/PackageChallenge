package com.mobiquityinc.io;

import com.mobiquityinc.domain.Result;

import java.util.List;

public class OutputWriter implements Writer {

    @Override
    public String printSolution(List<Result> results) {
        String newLine = "";

        StringBuilder solution = new StringBuilder();

        for (Result result : results) {
            solution.append(newLine).append(result.toString());
            newLine = "\n";
        }

        return solution.toString();
    }
}
