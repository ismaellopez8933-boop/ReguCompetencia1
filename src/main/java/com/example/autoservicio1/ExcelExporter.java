package com.example.autoservicio1;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static void exportToExcel(List<Sale> sales) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sales");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Sale ID", "Customer Name", "Product Name", "Category", "Quantity", "Unit Price", "Sale Amount", "Profit", "Sale Date", "Region", "Payment Method"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Create data rows
        int rowNum = 1;
        for (Sale sale : sales) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sale.getSaleId());
            row.createCell(1).setCellValue(sale.getCustomerName());
            row.createCell(2).setCellValue(sale.getProductName());
            row.createCell(3).setCellValue(sale.getCategory());
            row.createCell(4).setCellValue(sale.getQuantity());
            row.createCell(5).setCellValue(sale.getUnitPrice());
            row.createCell(6).setCellValue(sale.getSaleAmount());
            row.createCell(7).setCellValue(sale.getProfit());
            row.createCell(8).setCellValue(sale.getSaleDate().toString());
            row.createCell(9).setCellValue(sale.getRegion());
            row.createCell(10).setCellValue(sale.getPaymentMethod());
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("sales_report.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
