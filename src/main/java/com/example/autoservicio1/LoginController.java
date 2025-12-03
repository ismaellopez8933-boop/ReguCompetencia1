package com.example.autoservicio1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private List<User> users;

    public LoginController() {
        users = new ArrayList<>();
        users.add(new User("admin", "admin", "admin"));
        users.add(new User("analyst", "analyst", "analyst"));
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // Successful login
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Sales Dashboard");

                    MainController mainController = fxmlLoader.getController();
                    // You can pass the user role to the main controller if needed
                    // mainController.setUserRole(user.getRole());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        // Failed login
        errorLabel.setText("Invalid username or password");
    }
}
