package uk.ac.aber.dcs.frontend;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JfxApp extends Application {


    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("JfxApp.fxml"));
        primaryStage.setTitle("Cipher Encryption");
        primaryStage.setScene(new Scene(root, 568,451));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
