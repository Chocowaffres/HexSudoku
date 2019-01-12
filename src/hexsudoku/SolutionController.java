
package hexsudoku;

import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SolutionController implements Initializable {
    
    // GridPane principal
    @FXML
    private GridPane gridAvo;           
    
    // Puzzle a ser mostrado
    public static int[] iaPuzzle;           
    
    private Hashtable<Integer,String> htToHex = new Hashtable<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        htToHex.put(0,"0"); htToHex.put(1,"1"); htToHex.put(2,"2"); htToHex.put(3,"3"); htToHex.put(4,"4"); htToHex.put(5,"5"); htToHex.put(6,"6"); htToHex.put(7,"7"); htToHex.put(8,"8"); htToHex.put(9,"9"); 
        htToHex.put(10,"A"); htToHex.put(11,"B"); htToHex.put(12,"C"); htToHex.put(13,"D"); htToHex.put(14,"E"); htToHex.put(15,"F");
        ObservableList<Node> filhos = gridAvo.getChildren();
        
        // Percorrer os gridpane filho
        int k = 0;
        for (int i = 0; i < filhos.size(); i++) {
            GridPane grid = (GridPane) filhos.get(i);
            ObservableList<Node> netos = grid.getChildren();
            for (int j = 0; j < netos.size(); j++) {
                int perBox = ((k / 4) % 4) * 16 + ((k % 64) / 16) * 4 + (k / 64) * 64 + (k %4);
                Label label = (Label) netos.get(j);
                int value = iaPuzzle[perBox] - 1;
                label.setText(htToHex.get(value));
                k++;
            }
        }
    }
}
