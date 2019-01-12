/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexsudoku;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StatsController implements Initializable {
    
    // ID do Utilizador
    public static int iIDUser = 0;
    
    @FXML
    private TableView<Game_Stats> tableView;
    @FXML
    private TableColumn<Game_Stats,String> diff;
    @FXML
    private TableColumn<Game_Stats,String> time;
    
    // SQLite connection string
    private static final String URL = "jdbc:sqlite:sudoku.db";
    
    public void onSairClick (ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    public void onMenuClick (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            MenuController mc = loader.<MenuController>getController();
            mc.iIDUser = this.iIDUser;
            
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String sql = "SELECT * FROM stat WHERE iduser = " + this.iIDUser;
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ObservableList<Game_Stats> gameStats
            = FXCollections.observableArrayList();

            while (rs.next()) {
                gameStats.add(new Game_Stats(rs.getString("dificuldade"),rs.getString("tempo")));
            }
            diff.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            time.setCellValueFactory(new PropertyValueFactory<>("tempo"));
            tableView.setItems(gameStats);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (conn != null)
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
  
}
