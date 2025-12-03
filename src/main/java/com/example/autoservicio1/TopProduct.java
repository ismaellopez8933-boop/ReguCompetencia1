package com.example.autoservicio1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TopProduct {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty totalQuantity;

    public TopProduct(String name, int totalQuantity) {
        this.name = new SimpleStringProperty(name);
        this.totalQuantity = new SimpleIntegerProperty(totalQuantity);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getTotalQuantity() {
        return totalQuantity.get();
    }

    public SimpleIntegerProperty totalQuantityProperty() {
        return totalQuantity;
    }
}
