/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexsudoku;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {
    
    // Nível de dificuldade
    public static int iDiff = 0;
    
    // ID do Utilizador
    public static int iIDUser = 0;
    
    public long currentTime = 0;
    public long timerCurrentTime =0;

    @FXML
    private GridPane gridAvo;           // GridPane principal
    @FXML
    private Label timer;        
    
    DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis( 1000 ),
                event -> {         
                    final long diff = (timerCurrentTime)+(System.currentTimeMillis()-(currentTime+3600000));
                    timer.setText( timeFormat.format( diff ) );
                }
            )
    );
    
    private int[][] iaPuzzle;           // Puzzle a ser usado neste momento
    private int[][] iaPuzzleSolved;           // Puzzle a ser usado neste momento
    private String sLastPressed = "";   // Qual foi a última célula pressionada
    private Label lLastPressed = null;
    
    private Hashtable<Integer,String> htToHex = new Hashtable<>();
    
    private String bordercolor = "-fx-border-color:#76A2CF;";
    
    // Colorir as células com números preenchidos 
    private void pintaInicio() {
        ObservableList<Node> filhos = gridAvo.getChildren();
        
        // Percorrer os gridpane filho
        for (int i = 0; i < filhos.size(); i++) {
            GridPane grid = (GridPane) filhos.get(i);
            ObservableList<Node> netos = grid.getChildren();
            for (int j = 0; j < netos.size(); j++) {
                Label label = (Label) netos.get(j);
                int value = PuzzleSeeds.example1[i][j];
                if (value != -1) {
                    label.setStyle(bordercolor+"-fx-background-color:#B1CAE3;");
                }
                else {
                    label.setStyle(bordercolor+"-fx-background-color:#FFFFFF");
                }
            }
        }
    }
    
    //Quando a célula pré-preenchida é pressionada
    private void handleOnClickedPre (String sId) {
        pintaInicio();
        
        if (!sId.equals(sLastPressed)) {
            ObservableList<Node> filhos = gridAvo.getChildren();
        
            // Percorrer os gridpane filho
            for (int i = 0; i < filhos.size(); i++) {
                GridPane grid = (GridPane) filhos.get(i);
                ObservableList<Node> netos = grid.getChildren();
                for (int j = 0; j < netos.size(); j++) {
                    Label label = (Label) netos.get(j);
                    if (label.getId().substring(0, 1).equals(sId.substring(0,1))) {
                        label.setStyle(bordercolor+"-fx-background-color: #92b4f4");
                    }
                    if (label.getId().substring(1).equals(sId.substring(1))) {
                        label.setStyle(bordercolor+"-fx-background-color: #92b4f4");
                    }
                    if (label.getId().equals(sId)) {
                        lLastPressed = label;
                    }
                }
            }
            sLastPressed = sId;
        }
        else {
            sLastPressed = "";
        }
    }
    
    // Quando uma célula em branco é pressionada, selecionar a sua linha e coluna
    private void handleOnClicked (String sId, double dMousePosX, double dMousePosY) {
        pintaInicio();
        
        // Se já estava selecionado então remover a seleção
        if (!sId.equals(sLastPressed)) {
            ObservableList<Node> filhos = gridAvo.getChildren();
        
            // Percorrer os gridpane filho
            for (int i = 0; i < filhos.size(); i++) {
                GridPane grid = (GridPane) filhos.get(i);
                ObservableList<Node> netos = grid.getChildren();
                for (int j = 0; j < netos.size(); j++) {
                    Label label = (Label) netos.get(j);
                    if (label.getId().substring(0, 1).equals(sId.substring(0,1))) {
                        label.setStyle(bordercolor+"-fx-background-color: #92b4f4");
                    }
                    if (label.getId().substring(1).equals(sId.substring(1))) {
                        label.setStyle(bordercolor+"-fx-background-color: #92b4f4");
                    }
                    if (label.getId().equals(sId)) {
                        lLastPressed = label;
                    }
                }
            }
            sLastPressed = sId;
            
            try {
                FXMLLoader oJanelaNum = new FXMLLoader();
                oJanelaNum.setLocation(getClass().getResource("Escolha.fxml"));
                oJanelaNum.load();
                
                EscolhaController cTeclado = oJanelaNum.getController();
                Parent pTeclado = oJanelaNum.getRoot();
                Scene secondScene = new Scene(pTeclado, 250, 250);

                // Nova janela
                Stage sNums = new Stage();
                sNums.initStyle(StageStyle.UNDECORATED);
                sNums.setTitle("Opções");
                sNums.setScene(secondScene);

                // Colocar a nova janela na posição do rato
                sNums.setX(dMousePosX + 50);
                sNums.setY(dMousePosY + 50);
                sNums.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (! isNowFocused) {
                        pintaInicio();
                        sNums.close();
                    }
                });
                cTeclado.setNumber(lLastPressed, sNums);
                sNums.show();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            sLastPressed = "";
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        currentTime = System.currentTimeMillis();
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
        
        System.out.println("AJUDA: " + iDiff);
        htToHex.put(0,"0"); htToHex.put(1,"1"); htToHex.put(2,"2"); htToHex.put(3,"3"); htToHex.put(4,"4"); htToHex.put(5,"5"); htToHex.put(6,"6"); htToHex.put(7,"7"); htToHex.put(8,"8"); htToHex.put(9,"9"); 
        htToHex.put(10,"A"); htToHex.put(11,"B"); htToHex.put(12,"C"); htToHex.put(13,"D"); htToHex.put(14,"E"); htToHex.put(15,"F");
        iaPuzzle = PuzzleSeeds.example1;
        iaPuzzleSolved = PuzzleSeeds.solution1;
        ObservableList<Node> filhos = gridAvo.getChildren();
        DBHelper.createTables();
        
        
        // Percorrer os gridpane filho
        for (int i = 0; i < filhos.size(); i++) {
            GridPane grid = (GridPane) filhos.get(i);
            ObservableList<Node> netos = grid.getChildren();
            for (int j = 0; j < netos.size(); j++) {
                Label label = (Label) netos.get(j);
                int value = iaPuzzle[i][j];
                if (value != -1) {
                    label.setText(htToHex.get(value));
                    label.setStyle(bordercolor+"-fx-background-color:#B1CAE3");
                    label.setOnMouseClicked(MouseEvent -> handleOnClickedPre(label.getId()));
                }
                else {
                    label.setStyle(bordercolor+"-fx-background-color:#FFFFFF");
                    label.setOnMouseClicked(MouseEvent -> handleOnClicked(label.getId(), MouseEvent.getScreenX(), MouseEvent.getScreenY()));
                }
            }
        }
    }

    public void onMenuClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        
            Parent pMenu = loader.load();
            
            Scene sceneMenu = new Scene(pMenu);
            
            Stage sMenu = new Stage();
            sMenu.setTitle("HexSudoku");
            sMenu.setScene(sceneMenu);
            
            sMenu.setResizable(false);
            
            Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
            sMenu.getIcons().add(iIcon);
            
            // Mostrar o menu
            sMenu.show();
            
            // Fechar a janela atual
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void onPauseClick(ActionEvent event){
        
        timeline.pause();
        timerCurrentTime = 0;
        String  s= timer.getText();
        String[] s2 = s.split(":");
        timerCurrentTime+=Integer.parseInt(s2[0])*3600000;
        timerCurrentTime+=Integer.parseInt(s2[1])*60000;
        timerCurrentTime+=Integer.parseInt(s2[2])*1000;
        
        showInformation();    
    }
    
    public void onSairClick (ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    public void onSolutionClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Solution.fxml"));
            SolutionController sc = loader.<SolutionController>getController();
            sc.iaPuzzle = this.iaPuzzleSolved;
            
            Parent pMenu = loader.load();

            Scene sceneMenu = new Scene(pMenu);

            Stage sMenu = new Stage();
            sMenu.setTitle("HexSudoku");
            sMenu.setScene(sceneMenu);

            sMenu.setResizable(false);

            Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
            sMenu.getIcons().add(iIcon);

            // Mostrar o menu
            sMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void showInformation(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Time Paused");
        alert.setContentText("PAUSED");
        alert.setResizable(false);
        alert.initStyle(StageStyle.UNDECORATED);
         Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                currentTime = System.currentTimeMillis();
                timeline.play();
            }
 
    }   
}