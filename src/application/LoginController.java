package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	   	@FXML
	    private TextField usernameField;

	    @FXML
	    private PasswordField passwordField;
	    
	    public void Login() {
	   	 try {
	   		String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/bingo_db";
		    String user = "root";
		    String pass = "";
			Class.forName(driver);
		    Connection con = DriverManager.getConnection(url, user, pass);
	    	String username = this.usernameField.getText();
	    	String password = this.passwordField.getText();
	    	String query = "select * from users where username = '"+username+"' and password = '"+password+"'";
	    	Statement statement = con.createStatement();
	    	ResultSet results = statement.executeQuery(query);
	    	if(results.next()) {
	    		FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("Main.fxml"));
	            Scene scene = new Scene(fxmlLoader.load());
	            Stage stage = new Stage();
	            stage.setTitle("hello - "+ username);
	            stage.setScene(scene);
	            stage.show();
	            MainController mc = new MainController();
	            mc.meth(username);
	            this.usernameField.getScene().getWindow().hide();
	    	}else {
	            JOptionPane.showMessageDialog(null, "არასწორია იუზერნეიმი ან პაროლი!");
	    	}
		    if (!con.isClosed()) {
		      con.close();
		    }
	   	 } catch (Exception e) {
	   		 System.out.println(e.getMessage());
	   	 }
	    }


		public LoginController() throws ClassNotFoundException, SQLException {
	       
	    	
	    	
	    	
	    }
	    
}
