package com.example.autoservicio1;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class TopCustomer {
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty totalSpent;

    public TopCustomer(String name, double totalSpent) {
        this.name = new SimpleStringProperty(name);
        this.totalSpent = new SimpleDoubleProperty(totalSpent);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public double getTotalSpent() {
        return totalSpent.get();
    }

    public SimpleDoubleProperty totalSpentProperty() {
        return totalSpent;
    }
}
