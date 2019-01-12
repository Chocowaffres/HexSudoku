package hexsudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Login_ihc extends Application {
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("HexSudoku");
        Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
        stage.getIcons().add(iIcon);
        
        DBHelper.createTables();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
