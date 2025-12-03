package com.example.autoservicio1;

import com.example.autoservicio1.Sale;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.util.List;

public class PdfExporter {

    public static void exportToPdf(List<Sale> sales) throws IOException {
        String dest = "sales_report.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3}))
                .useAllAvailableWidth();

        // Add headers
        table.addHeaderCell("Sale ID");
        table.addHeaderCell("Customer Name");
        table.addHeaderCell("Product Name");
        table.addHeaderCell("Category");
        table.addHeaderCell("Quantity");
        table.addHeaderCell("Unit Price");
        table.addHeaderCell("Sale Amount");
        table.addHeaderCell("Profit");
        table.addHeaderCell("Sale Date");
        table.addHeaderCell("Region");
        table.addHeaderCell("Payment Method");

        // Add data
        for (Sale sale : sales) {
            table.addCell(String.valueOf(sale.getSaleId()));
            table.addCell(sale.getCustomerName());
            table.addCell(sale.getProductName());
            table.addCell(sale.getCategory());
            table.addCell(String.valueOf(sale.getQuantity()));
            table.addCell(String.valueOf(sale.getUnitPrice()));
            table.addCell(String.valueOf(sale.getSaleAmount()));
            table.addCell(String.valueOf(sale.getProfit()));
            table.addCell(sale.getSaleDate().toString());
            table.addCell(sale.getRegion());
            table.addCell(sale.getPaymentMethod());
        }

        document.add(table);
        document.close();
    }
}
