/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexsudoku;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EscolhaController implements Initializable {
    
    @FXML
    private Label lPos;
    
    @FXML
    private Stage Stage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    public void setNumber (Label Pos, Stage stage) {
        this.lPos = Pos;
        this.Stage = stage;
    }
    
    public void onNumberClick (ActionEvent event) {
        Button bClicked = (Button) event.getSource();
        lPos.setText(bClicked.getText());
        
        Stage.close();
    }
}
