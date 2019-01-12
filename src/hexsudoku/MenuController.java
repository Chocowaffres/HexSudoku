/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexsudoku;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuController implements Initializable {
    
    // ID do Utilizador
    public static int iIDUser = 0;
    
    @FXML
    private ImageView IVPlay;
    @FXML
    private ChoiceBox CBDiff;
    
    private ArrayList<String> alDiff;
    
    public void onSairClick (ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    public void onStatsClick (ActionEvent event) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Stats.fxml"));
            StatsController sc = loader.<StatsController>getController();
            sc.iIDUser = this.iIDUser;
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("HexSudoku");
            stage.setScene(scene);

            // Não é passível de ser redimensionado
            stage.setResizable(false);

            // Colocar o logo como ícone da aplicação
            Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
            stage.getIcons().add(iIcon);

            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alDiff = new ArrayList<>(); alDiff.add("Easy"); alDiff.add("Medium"); alDiff.add("Hard");
        ObservableList<String> olDiffs = FXCollections.<String>observableArrayList(alDiff);
        CBDiff.setItems(olDiffs);
        CBDiff.getSelectionModel().selectFirst();
        
        IVPlay.setOnMouseClicked((MouseEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                FXMLDocumentController fdc = loader.<FXMLDocumentController>getController();
                int iDiff = CBDiff.getSelectionModel().getSelectedIndex();
                fdc.iDiff = iDiff;
                fdc.iIDUser = this.iIDUser;
                
                Parent root = loader.load();
        
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("HexSudoku");
                stage.setScene(scene);

                // Não é passível de ser redimensionado
                stage.setResizable(false);

                // Colocar o logo como ícone da aplicação
                Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
                stage.getIcons().add(iIcon);
                fdc.Stage = stage;
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
  
}
