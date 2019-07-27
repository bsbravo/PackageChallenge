package com.mobiquityinc.packer;

import com.mobiquityinc.domain.*;
import com.mobiquityinc.exception.APIException;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackerTest {

    private MaxCostPackageFinder maxCostPackageFinder = new DPMaxCostPackageFinder();

    @Test
    void filesDoesNotExistThrowsException() {
        try {
            Packer.pack("src/test/resources/samples/input/invalid_file.txt");
            fail();
        } catch (APIException e) {
        }
    }

    @Test
    void givenInputSampleMatchGivenResults() throws Exception {
        String expectedSolution = new String(Files.readAllBytes(Paths.get("src/test/resources/samples/output/example_output.txt")));

        String solution = Packer.pack("src/test/resources/samples/input/example_input.txt");

        assertEquals(expectedSolution, solution);
    }

    @Test
    void maxPackageWeightGreaterThanLimitThrowsApiException() {
        assertThrows(APIException.class, () ->
                maxCostPackageFinder.findSolution(new ArrayList<>(), Constraints.MAX_PACKAGE_WEIGHT + 1)
        );
    }

    @Test
    void morePackageItemsThanLimitThrowsApiException() throws APIException {
        List<PackageItem> items = new ArrayList<>();
        for (int i = 0; i < Constraints.MAX_INPUT_ITEMS + 1; i++) {
            items.add(new PackageItem(i, 0, 0));
        }

        assertThrows(APIException.class, () ->
                maxCostPackageFinder.findSolution(items, Constraints.MAX_PACKAGE_WEIGHT)
        );
    }

    @Test
    void anyItemWithCostGreaterThanLimitThrowsApiException() {
        assertThrows(APIException.class, () -> {
            PackageItem item = new PackageItem(1, 0, Constraints.MAX_ITEM_COST + 1);
            List<PackageItem> input = Collections.singletonList(item);

            maxCostPackageFinder.findSolution(input, 50);

        });
    }

    @Test
    void anyItemWithWeightGreaterThanLimitThrowsApiException() {
        assertThrows(APIException.class, () -> {
            PackageItem item = new PackageItem(1, Constraints.MAX_ITEM_WEIGHT + 1, 0);
            List<PackageItem> input = Collections.singletonList(item);

            maxCostPackageFinder.findSolution(input, 50);

        });
    }

    @Test
    void zeroItemsReturnsNothing() throws APIException {
        List<PackageItem> emptyItemsList = Collections.emptyList();

        Solution solution = maxCostPackageFinder.findSolution(emptyItemsList, Constraints.MAX_PACKAGE_WEIGHT);

        assertEquals("-", solution.toString());
    }

    @Test
    void oneItemWithWeightLessThanMaxReturnsTheItem() throws APIException {
        PackageItem item = new PackageItem(1, 1, 1);
        List<PackageItem> input = Collections.singletonList(item);

        Solution result = maxCostPackageFinder.findSolution(input, Constraints.MAX_PACKAGE_WEIGHT);

        assertEquals("1", result.toString());
    }

    @Test
    void oneItemWithWeightGreaterThanMaxReturnsNothing() throws APIException {
        PackageItem item = new PackageItem(1, 100, 100);
        List<PackageItem> input = Collections.singletonList(item);

        Solution solution = maxCostPackageFinder.findSolution(input, 99);

        assertEquals("-", solution.toString());
    }

    @Test
    void givenSample2MatchGivenResults() throws Exception {
        String expectedSolution = new String(Files.readAllBytes(Paths.get("src/test/resources/samples/output/custom_test_case_output.txt")));

        String solution = Packer.pack("src/test/resources/samples/input/custom_test_case_input.txt");

        assertEquals(expectedSolution, solution);
    }


}