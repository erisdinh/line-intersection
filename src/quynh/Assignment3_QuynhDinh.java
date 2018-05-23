package quynh;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Assignment3_QuynhDinh extends Application {

    ///////////////////////////////////////////////////////////////////////////
    // entry-point of JavaFX application
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Line Intersection");
        
        // set app's icon
        stage.getIcons().add(new Image(Assignment3_QuynhDinh.class.getResourceAsStream("point.png")));
        
        stage.show();
    }

    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        launch(args);
    }
}
