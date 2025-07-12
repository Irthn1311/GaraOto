package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the LoginView.fxml first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Đăng nhập Gara Ô tô"); // Set title for login window
        primaryStage.setResizable(false); // Login window usually not resizable
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
    