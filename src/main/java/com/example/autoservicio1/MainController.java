package com.example.autoservicio1;

import com.example.autoservicio1.Database;
import com.example.autoservicio1.Sale;
import com.example.autoservicio1.CsvLoader;
import com.example.autoservicio1.PdfExporter;
import com.example.autoservicio1.ExcelExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private TableView<TopCustomer> topCustomersTable;
    @FXML
    private TableView<TopProduct> topProductsTable;
    @FXML
    private PieChart paymentMethodChart;
    @FXML
    private LineChart<String, Number> salesOverTimeChart;
    @FXML
    private BarChart<String, Number> salesByCategoryChart;
    @FXML
    private BarChart<String, Number> profitByCategoryChart;
    @FXML
    private BarChart<String, Number> quantityByRegionChart;

    private final ObservableList<Sale> salesData = FXCollections.observableArrayList();
    private final ObservableList<TopCustomer> topCustomersData = FXCollections.observableArrayList();
    private final ObservableList<TopProduct> topProductsData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTables();
        loadAllData();
    }

    private void loadAllData() {
        loadSalesFromDatabase();
        loadChartData();
        loadTopCustomers();
        loadTopProducts();
    }

    private void setupTables() {
        // Sales table
        TableColumn<Sale, Integer> saleIdCol = new TableColumn<>("Sale ID");
        saleIdCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        TableColumn<Sale, String> customerNameCol = new TableColumn<>("Customer Name");
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        TableColumn<Sale, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        TableColumn<Sale, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Sale, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Sale, Double> unitPriceCol = new TableColumn<>("Unit Price");
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        TableColumn<Sale, Double> saleAmountCol = new TableColumn<>("Sale Amount");
        saleAmountCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        TableColumn<Sale, Double> profitCol = new TableColumn<>("Profit");
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profit"));
        TableColumn<Sale, LocalDate> saleDateCol = new TableColumn<>("Sale Date");
        saleDateCol.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        TableColumn<Sale, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        TableColumn<Sale, String> paymentMethodCol = new TableColumn<>("Payment Method");
        paymentMethodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        salesTable.getColumns().addAll(saleIdCol, customerNameCol, productNameCol, categoryCol, quantityCol, unitPriceCol, saleAmountCol, profitCol, saleDateCol, regionCol, paymentMethodCol);
        salesTable.setItems(salesData);

        // Top Customers table
        TableColumn<TopCustomer, String> customerNameTopCol = new TableColumn<>("Customer Name");
        customerNameTopCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<TopCustomer, Double> totalSpentCol = new TableColumn<>("Total Spent");
        totalSpentCol.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));
        topCustomersTable.getColumns().addAll(customerNameTopCol, totalSpentCol);
        topCustomersTable.setItems(topCustomersData);

        // Top Products table
        TableColumn<TopProduct, String> productNameTopCol = new TableColumn<>("Product Name");
        productNameTopCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<TopProduct, Integer> totalQuantityCol = new TableColumn<>("Total Quantity");
        totalQuantityCol.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        topProductsTable.getColumns().addAll(productNameTopCol, totalQuantityCol);
        topProductsTable.setItems(topProductsData);
    }

    private void loadSalesFromDatabase() {
        salesData.clear();
        String sql = "SELECT * FROM sales";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                salesData.add(new Sale(
                        rs.getInt("sale_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("sale_amount"),
                        rs.getDouble("profit"),
                        rs.getDate("sale_date").toLocalDate(),
                        rs.getString("region"),
                        rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadChartData() {
        // Clear previous data
        paymentMethodChart.getData().clear();
        salesOverTimeChart.getData().clear();
        salesByCategoryChart.getData().clear();
        profitByCategoryChart.getData().clear();
        quantityByRegionChart.getData().clear();

        // Load new data
        loadPaymentMethodChartData();
        loadSalesOverTimeChartData();
        loadSalesByCategoryChartData();
        loadProfitByCategoryChartData();
        loadQuantityByRegionChartData();
    }

    private void loadPaymentMethodChartData() {
        Map<String, Double> salesByPaymentMethod = Database.getSalesByPaymentMethod();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : salesByPaymentMethod.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        paymentMethodChart.setData(pieChartData);
    }

    private void loadSalesOverTimeChartData() {
        Map<String, Double> salesOverTime = Database.getSalesOverTime();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas Mensuales");
        for (Map.Entry<String, Double> entry : salesOverTime.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        salesOverTimeChart.getData().add(series);
    }

    private void loadSalesByCategoryChartData() {
        Map<String, Double> salesByCategory = Database.getTotalSalesByCategory();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas por Categoría");
        for (Map.Entry<String, Double> entry : salesByCategory.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        salesByCategoryChart.getData().add(series);
    }

    private void loadProfitByCategoryChartData() {
        Map<String, Double> profitByCategory = Database.getAvgProfitByCategory();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Beneficio por Categoría");
        for (Map.Entry<String, Double> entry : profitByCategory.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        profitByCategoryChart.getData().add(series);
    }

    private void loadQuantityByRegionChartData() {
        Map<String, Integer> quantityByRegion = Database.getQuantityByRegion();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cantidad por Región");
        for (Map.Entry<String, Integer> entry : quantityByRegion.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        quantityByRegionChart.getData().add(series);
    }

    private void loadTopCustomers() {
        topCustomersData.clear();
        List<Map<String, Object>> topCustomers = Database.getTop10Customers();
        for (Map<String, Object> customer : topCustomers) {
            topCustomersData.add(new TopCustomer((String) customer.get("name"), (Double) customer.get("totalSpent")));
        }
    }

    private void loadTopProducts() {
        topProductsData.clear();
        List<Map<String, Object>> topProducts = Database.getTop10Products();
        for (Map<String, Object> product : topProducts) {
            topProductsData.add(new TopProduct((String) product.get("name"), (Integer) product.get("totalQuantity")));
        }
    }

    @FXML
    private void handleLoadCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(salesTable.getScene().getWindow());
        if (file != null) {
            CsvLoader.loadCsvInThread(file, () -> {
                // Reload data after CSV import is complete
                javafx.application.Platform.runLater(this::loadAllData);
            });
        }
    }

    @FXML
    private void handleExportPdf() {
        try {
            PdfExporter.exportToPdf(salesTable.getItems());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportExcel() {
        try {
            ExcelExporter.exportToExcel(salesTable.getItems());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
