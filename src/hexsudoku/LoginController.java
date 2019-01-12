package hexsudoku;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    
    private static final String URL = "jdbc:sqlite:sudoku.db";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBHelper.createTables();
    }

    public void signin(ActionEvent event){

        String user = username.getText();
        String pass = password.getText();
        String sql = "Select * from user where nome='"+user+"' and password ='"+ pass+"'";
        
        if(user.length()>=4 && pass.length()>=4){
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if(rs.next()){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    MenuController fdc = loader.<MenuController>getController();
                    fdc.iIDUser = rs.getInt("id");
                    Parent root = loader.load();
                    
                    Scene scene = new Scene(root);
                    Stage sNums = new Stage();
                    sNums.setTitle("HexSudoku");
                    sNums.setScene(scene);
                    sNums.setResizable(false);

                    Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
                    sNums.getIcons().add(iIcon);
                    
                    sNums.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    
                }
                else{
                    showWarning("Username and password do not match!");
                }
                conn.close();
            }
            catch(Exception e){}     
        }
        else{
            showWarning("Please fill both fields.\nThey must be at least 4 character long");
        }
    }

    public void signup(ActionEvent event){

        String user = username.getText();
        String pass = password.getText();
        String sql = "Select * from user where nome='"+user+"'";
        String sql2 = "INSERT INTO user(nome,password) VALUES(?,?)";
   
        if(user.length()>=4 && pass.length()>=4){
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if(rs.next()){
                    showWarning("Username already in use!");
                }
                else{   
                    PreparedStatement pstmt = conn.prepareStatement(sql2);
                    pstmt.setString(1, user);
                    pstmt.setString(2, pass);
                    pstmt.executeUpdate();

                    sql = "Select * from user where nome='"+user+"'";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    MenuController fdc = loader.<MenuController>getController();
                    fdc.iIDUser = rs.getInt("id");
                    Parent root = loader.load();
                    
                    Scene scene = new Scene(root);
                    Stage sNums = new Stage();
                    sNums.setTitle("HexSudoku");
                    sNums.setScene(scene);
                    sNums.setResizable(false);

                    Image iIcon = new Image(getClass().getResourceAsStream("/hexsudoku/images/logo.jpg"));
                    sNums.getIcons().add(iIcon);
                    
                    sNums.show();
                   ((Node) (event.getSource())).getScene().getWindow().hide();
                }
                conn.close();
            }catch(Exception e){}
        }
        else{
            showWarning("Please fill both fields.\nThey must be at least 4 character long");
        }
    }

    public void showWarning(String s){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("WARNING");
        alert.setContentText(s);
        alert.showAndWait();
    }
    
}
