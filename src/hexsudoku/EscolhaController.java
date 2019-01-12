
package hexsudoku;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        String s = bClicked.getText();
        lPos.setText(s);
        
        String sLabel = lPos.getId();
        char cLabelRow = sLabel.charAt(0);
        int row = ((int) cLabelRow)-65; 
        int col = (Integer.parseInt(sLabel.substring(1)))-1;
        
        int pos = row*16+col;
        FXMLDocumentController.updateCurrent(pos, Integer.parseInt(s,16)+1);
        Stage.close();
    }
}
