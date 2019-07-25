package com.mobiquityinc.packer;

import com.mobiquityinc.domain.Constraints;
import com.mobiquityinc.domain.PackageItem;
import com.mobiquityinc.domain.Result;
import com.mobiquityinc.exception.APIException;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackerTest {

    @Test
    void givenInputSampleMatchGivenResults() throws Exception {
        String expectedSolution = new String(Files.readAllBytes(Paths.get("src/test/resources/samples/output/example_output.txt")));

        String solution = Packer.pack("src/test/resources/samples/input/example_input.txt");

        assertEquals(expectedSolution, solution);
    }

    @Test
    void maxPackageWeightGreaterThanLimitThrowsApiException() {
        assertThrows(APIException.class, () ->
            Packer.result(new ArrayList<>(), Constraints.MAX_PACKAGE_WEIGHT + 0.1)
        );
    }

    @Test
    void morePackageItemsThanLimitThrowsApiException() throws APIException {
        List<PackageItem> items = new ArrayList<>();
        for (int i = 0; i < Constraints.MAX_INPUT_ITEMS + 1; i++) {
            items.add(new PackageItem(i, 0, 0));
        }

        assertThrows(APIException.class, () ->
            Packer.result(items, Constraints.MAX_PACKAGE_WEIGHT)
        );
    }

    @Test
    void anyItemWithCostGreaterThanLimitThrowsApiException() {
        assertThrows(APIException.class, () -> {
            PackageItem item = new PackageItem(1, 0, Constraints.MAX_ITEM_COST + 1);
            List<PackageItem> input = Collections.singletonList(item);

            Packer.result(input, 50);

        });
    }

    @Test
    void anyItemWithWeightGreaterThanLimitThrowsApiException() {
        assertThrows(APIException.class, () -> {
            PackageItem item = new PackageItem(1, Constraints.MAX_ITEM_WEIGHT + 1, 0);
            List<PackageItem> input = Collections.singletonList(item);

            Packer.result(input, 50);

        });
    }

    @Test
    void zeroItemsReturnsNothing() throws APIException {
        List<PackageItem> emptyItemsList = Collections.emptyList();

        Result result = Packer.result(emptyItemsList, Constraints.MAX_PACKAGE_WEIGHT);

        assertEquals("-", result.toString());
    }

    @Test
    void oneItemWithWeightLessThanMaxReturnsTheItem() throws APIException {
        PackageItem item = new PackageItem(1, 1, 1);
        List<PackageItem> input = Collections.singletonList(item);

        Result result = Packer.result(input, Constraints.MAX_PACKAGE_WEIGHT);

        assertEquals("1", result.toString());
    }

    @Test
    void oneItemWithWeightGreaterThanMaxReturnsNothing() throws APIException {
        PackageItem item = new PackageItem(1, 100, 100);
        List<PackageItem> input = Collections.singletonList(item);

        Result result = Packer.result(input, 99);

        assertEquals("-", result.toString());
    }

    @Test
    void givenSample2MatchGivenResults() throws Exception {
        String expectedSolution = new String(Files.readAllBytes(Paths.get("src/test/resources/samples/output/custom_test_case_output.txt")));

        String solution = Packer.pack("src/test/resources/samples/input/custom_test_case_input.txt");

        assertEquals(expectedSolution, solution);
    }


}