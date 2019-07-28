package com.mobiquityinc.packer;

import com.mobiquityinc.domain.*;
import com.mobiquityinc.exception.APIException;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackerTest {

    private MaxPackageCostFinder maxPackageCostFinder = new DPMaxPackageCostFinder();

    @Test
    void inputExampleMatchGivenResults() throws Exception {
        String expectedSolution = new String(Files.readAllBytes(Paths.get("src/test/resources/samples/output/example_output.txt")));

        String solution = Packer.pack("src/test/resources/samples/input/example_input.txt");

        assertEquals(expectedSolution, solution);
    }

    @Test
    void packageWeightGreaterThaMaxThrowsApiException() {
        assertThrows(APIException.class, () ->
                maxPackageCostFinder.findSolution(new ArrayList<>(), Constraints.MAX_PACKAGE_WEIGHT + 1)
        );
    }

    @Test
    void morePackageItemsThanMaxThrowsApiException() {
        assertThrows(APIException.class, () -> {
            List<PackageItem> items = new ArrayList<>();
            for (int i = 0; i < Constraints.MAX_INPUT_ITEMS + 1; i++) {
                items.add(new PackageItem(i, 0, 0));
            }
            maxPackageCostFinder.findSolution(items, Constraints.MAX_PACKAGE_WEIGHT);
        });
    }

    @Test
    void anyItemWithCostGreaterThanMaxThrowsApiException() {
        assertThrows(APIException.class, () -> {
            PackageItem item = new PackageItem(1, 1, Constraints.MAX_ITEM_COST + 1);
            List<PackageItem> input = Collections.singletonList(item);

            maxPackageCostFinder.findSolution(input, Constraints.MAX_PACKAGE_WEIGHT);

        });
    }

    @Test
    void anyItemWithWeightGreaterThanMaxThrowsApiException() {
        assertThrows(APIException.class, () -> {
            PackageItem item = new PackageItem(1, Constraints.MAX_ITEM_WEIGHT + 1, 1);
            List<PackageItem> input = Collections.singletonList(item);

            maxPackageCostFinder.findSolution(input, Constraints.MAX_PACKAGE_WEIGHT);

        });
    }

    @Test
    void filesDoesNotExistThrowsException() {
        assertThrows(APIException.class, () -> Packer.pack("src/test/resources/samples/input/invalid_file.txt"));
    }

    @Test
    void zeroItemsReturnsNothing() throws APIException {
        List<PackageItem> emptyItemsList = Collections.emptyList();

        Solution solution = maxPackageCostFinder.findSolution(emptyItemsList, Constraints.MAX_PACKAGE_WEIGHT);

        assertEquals("-", solution.toString());
    }

    @Test
    void oneItemWithWeightLessThanPackageLimitReturnsTheItem() throws APIException {
        PackageItem item = new PackageItem(1, 1, 1);
        List<PackageItem> input = Collections.singletonList(item);

        Solution result = maxPackageCostFinder.findSolution(input, Constraints.MAX_PACKAGE_WEIGHT);

        assertEquals("1", result.toString());
    }

    @Test
    void oneItemWithWeightGreaterThanPackageLimitReturnsNothing() throws APIException {
        PackageItem item = new PackageItem(1, 100, Constraints.MAX_ITEM_COST);
        List<PackageItem> input = Collections.singletonList(item);

        Solution solution = maxPackageCostFinder.findSolution(input, 90);

        assertEquals("-", solution.toString());
    }

    @Test
    void customTestCasesMatchGivenResults() throws Exception {
        String expectedSolution = new String(Files.readAllBytes(Paths.get("src/test/resources/samples/output/custom_test_case_output.txt")));

        String solution = Packer.pack("src/test/resources/samples/input/custom_test_case_input.txt");

        assertEquals(expectedSolution, solution);
    }


}