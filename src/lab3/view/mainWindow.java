package lab3.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lab3.controller.RegistrationSystem;
import java.io.IOException;

public class mainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

            AnchorPane root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
            primaryStage.setScene(new Scene(root, 1000, 500));
            primaryStage.show();

//lab3.controller.RegistrationSystem
    }
}
