package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/* The following class is the driver of the user interface
 * Sets up the stage for JavaFX and launches the UI
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Load the FXML
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("8 Tiles");

        // Specify the resolution of the UI: 400, 525
        primaryStage.setScene(new Scene(root, 400, 525));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Author Code: icon");
        System.out.println("Class: CS 342, Fall 2016");
        System.out.println("Program: #4, 8 Tiles UI");

        // Launch the UI
        launch(args);
    }
}
