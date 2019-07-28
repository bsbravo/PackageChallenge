package com.mobiquityinc.io;

import com.mobiquityinc.domain.PackageItem;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.util.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader implements Reader {

    private Scanner scanner;

    public FileReader(String filePath) throws APIException {
        File file = new File(filePath);
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            throw new APIException(String.format("Cannot open the specified file. [%s]", e.getMessage()), e);
        }
    }

    @Override
    public boolean hasNextEntry() {
        return scanner.hasNextLine();
    }

    @Override
    public Tuple<List<PackageItem>, Integer> readNextEntry() throws APIException {
        String[] line = scanner.nextLine().split(":");
        if (line.length < 2) {
            return new Tuple<>(Collections.emptyList(), 0);
        }
        int packageWeightLimit = Integer.parseInt(line[0].trim());
        List<PackageItem> items = new ArrayList<>();

        Pattern p = Pattern.compile("\\((.*?)\\)");
        Matcher m = p.matcher(line[1].trim());
        while (m.find()) {
            String[] item = m.group(1).split(",");
            PackageItem packageItem = new PackageItem(Integer.parseInt(item[0].trim()),
                    Double.parseDouble(item[1].trim()),
                    Double.parseDouble(item[2].trim().replaceAll("€", "")));
            items.add(packageItem);
        }
        return new Tuple<>(items, packageWeightLimit);
    }

}
