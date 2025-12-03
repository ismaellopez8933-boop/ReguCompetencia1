package com.example.autoservicio1;

import com.example.autoservicio1.Sale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_autoservicio";
    private static final String USER = "ismael";
    private static final String PASSWORD = "Ismaelo22#";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void insertSales(List<Sale> sales) {
        String sql = "INSERT INTO ventas (sale_id, customer_id, customer_name, product_id, product_name, category, quantity, unit_price, sale_amount, profit, sale_date, region, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Sale sale : sales) {
                pstmt.setString(1, sale.getSaleId());
                pstmt.setString(2, sale.getCustomerId());
                pstmt.setString(3, sale.getCustomerName());
                pstmt.setString(4, sale.getProductId());
                pstmt.setString(5, sale.getProductName());
                pstmt.setString(6, sale.getCategory());
                pstmt.setInt(7, sale.getQuantity());
                pstmt.setDouble(8, sale.getUnitPrice());
                pstmt.setDouble(9, sale.getSaleAmount());
                pstmt.setDouble(10, sale.getProfit());
                pstmt.setDate(11, java.sql.Date.valueOf(sale.getSaleDate()));
                pstmt.setString(12, sale.getRegion());
                pstmt.setString(13, sale.getPaymentMethod());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Double> getSalesByPaymentMethod() {
        Map<String, Double> salesByPaymentMethod = new HashMap<>();
        String sql = "SELECT payment_method, SUM(sale_amount) as total_sales FROM ventas GROUP BY payment_method";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salesByPaymentMethod.put(rs.getString("payment_method"), rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesByPaymentMethod;
    }

    public static Map<String, Double> getSalesOverTime() {
        Map<String, Double> salesOverTime = new TreeMap<>();
        String sql = "SELECT DATE_FORMAT(sale_date, '%Y-%m') as month, SUM(sale_amount) as total_sales FROM ventas GROUP BY month ORDER BY month";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salesOverTime.put(rs.getString("month"), rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesOverTime;
    }

    public static Map<String, Double> getTotalSalesByCategory() {
        Map<String, Double> salesByCategory = new LinkedHashMap<>();
        String sql = "SELECT category, SUM(sale_amount) as total_sales FROM ventas GROUP BY category ORDER BY total_sales DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                salesByCategory.put(rs.getString("category"), rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesByCategory;
    }

    public static Map<String, Double> getAvgProfitByCategory() {
        Map<String, Double> profitByCategory = new LinkedHashMap<>();
        String sql = "SELECT category, AVG(profit) as avg_profit FROM ventas GROUP BY category ORDER BY avg_profit DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                profitByCategory.put(rs.getString("category"), rs.getDouble("avg_profit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profitByCategory;
    }

    public static Map<String, Integer> getQuantityByRegion() {
        Map<String, Integer> quantityByRegion = new LinkedHashMap<>();
        String sql = "SELECT region, SUM(quantity) as total_quantity FROM ventas GROUP BY region ORDER BY total_quantity DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                quantityByRegion.put(rs.getString("region"), rs.getInt("total_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantityByRegion;
    }

    public static List<Map<String, Object>> getTop10Customers() {
        List<Map<String, Object>> topCustomers = new ArrayList<>();
        String sql = "SELECT customer_name, SUM(sale_amount) as total_spent FROM ventas GROUP BY customer_name ORDER BY total_spent DESC LIMIT 10";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> customer = new HashMap<>();
                customer.put("name", rs.getString("customer_name"));
                customer.put("totalSpent", rs.getDouble("total_spent"));
                topCustomers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topCustomers;
    }

    public static List<Map<String, Object>> getTop10Products() {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        String sql = "SELECT product_name, SUM(quantity) as total_quantity FROM ventas GROUP BY product_name ORDER BY total_quantity DESC LIMIT 10";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("name", rs.getString("product_name"));
                product.put("totalQuantity", rs.getInt("total_quantity"));
                topProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topProducts;
    }
}
