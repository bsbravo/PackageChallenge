package com.mobiquityinc.io;

import com.mobiquityinc.domain.Solution;

import java.util.List;

public class OutputWriter implements Writer {

    @Override
    public String formatSolutions(List<Solution> solutions) {
        String newLine = "";

        StringBuilder solution = new StringBuilder();

        for (Solution result : solutions) {
            solution.append(newLine).append(result.toString());
            newLine = "\n";
        }

        return solution.toString();
    }
}
