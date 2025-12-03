package com.example.autoservicio1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    public static void loadCsvInThread(File file, Runnable onFinished) {
        Thread thread = new Thread(() -> {
            try {
                List<Sale> sales = readSalesFromCsv(file);
                Database.insertSales(sales);
                if (onFinished != null) {
                    onFinished.run();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static List<Sale> readSalesFromCsv(File file) throws IOException {
        List<Sale> sales = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Sale sale = new Sale(
                        Integer.parseInt(values[0]),
                        Integer.parseInt(values[1]),
                        values[2],
                        Integer.parseInt(values[3]),
                        values[4],
                        values[5],
                        Integer.parseInt(values[6]),
                        Double.parseDouble(values[7]),
                        Double.parseDouble(values[8]),
                        Double.parseDouble(values[9]),
                        LocalDate.parse(values[10], formatter),
                        values[11],
                        values[12]
                );
                sales.add(sale);
            }
        }
        return sales.subList(0, Math.min(sales.size(), 1000));
    }
}
