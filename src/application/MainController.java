package application;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;


public class MainController implements Initializable {
	public static int LOTTO_TICKET_NUMBER_COUNT = 5;

	private static  MainController single;

	public static MainController getInstance(){
		if(single == null){
			single = new MainController();
			return single;
		}else
			return single;
	}

	@FXML
    private FlowPane resultpanel;
	
	@FXML
	private Label usernameLabel;

	@FXML
	private Label moneyLabel;
	
	@FXML
	private Button startbtn;
	
	public Set<Integer> lottRandomNumbers = new HashSet<Integer>();
	public Set<Integer> userPickedNumbers = new HashSet<Integer>();
	
	public MainController() {

	}
	public static String avatar = "";
	public static String username = "";
	

	public void meth(String username) {
		this.username = username;

		this.usernameLabel.setText(username);

//		if(username == "admin") {
//			this.adminBtn.setVisible(true);
//		}else {
//			this.adminBtn.setVisible(false);
//		}
	}

//	public static void start(Stage stage) {
//		this.usernameLabel.setText("test");
//
//		if(this.username == "admin") {
//			return this.adminBtn.setVisible(true);
//		}else {
//			return this.adminBtn.setVisible(false);
//		}
//	}
//
    @FXML
    void numberEventHandler(ActionEvent event) {
    }
    
    @FXML
    void pickedNumberEventHandler(MouseEvent event) {
    		if(userPickedNumbers.size() >= LOTTO_TICKET_NUMBER_COUNT) {
    			return;
    		}
    		Button pickedButton = (Button)event.getSource();
    		pickedButton.setDisable(true);
    		String numberStr = pickedButton.getText(); 
    		Integer number = Integer.parseInt(numberStr);
    		if(!userPickedNumbers.contains(number))
    			userPickedNumbers.add(number);
    		
    		if (userPickedNumbers.size() == LOTTO_TICKET_NUMBER_COUNT) {
    			startbtn.setDisable(false);
    		}
    }
    
    public void clearTickert() {
    	userPickedNumbers.clear();
//		Button pickedButton = (Button)event.getSource();
//		pickedButton.setDisable(true);


	}


	public void bingo_start() throws Exception {
		if(userPickedNumbers.size() ==LOTTO_TICKET_NUMBER_COUNT)
			startApplication();
		
//		pickNumber();
	}
	
	
	public  void doThreadStuff() {
		System.out.println("clicked");
		
		new Thread(()-> {
				try {
				pickNumber();
				System.out.println(userPickedNumbers);
				System.out.println(lottRandomNumbers);
				}catch(Exception ex) {
					System.out.println("Some Thread Error " + ex.getMessage());
				}
		}).start();
		System.out.println("Thread Finished");
	}
	
	public void startApplication() {
		try {
			ArrayList<Integer> al = new ArrayList<Integer>();
			Integer rand;
			resultpanel.getChildren().clear();
			int win = 0;
			while(al.size() < LOTTO_TICKET_NUMBER_COUNT) {
				do {
					rand = new SecureRandom().nextInt(25) + 1;
				} while(al.contains(rand));


				Label label = new Label(rand.toString());
				if (!userPickedNumbers.contains(rand)) {
					label.getStyleClass().add("butoni");
				}
				else {
					label.getStyleClass().add("butoni-success");
					win++;
				}
				al.add(rand);

				resultpanel.getChildren().add(label);
			}
			if (win == 5) {
				JOptionPane.showMessageDialog(null, "თქვენ მოიგეთ! გილოცავთ");
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/bingo_db";
				String user = "root";
				String pass = "";
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url, user, pass);
				List<Integer> listOfNumbers = new ArrayList<>(userPickedNumbers);
				String query = "INSERT INTO ticket( `user_username`, `number_1`, `number_2`, `number_3`, `number_4`, `number_5`, `win`) VALUES ("+this.username+","+listOfNumbers.get(0) +","+listOfNumbers.get(1) +","+listOfNumbers.get(2) +","+listOfNumbers.get(3) +","+listOfNumbers.get(4) +",1)";

				Statement statement = con.createStatement();
				int a  = statement.executeUpdate(query);
				if(a > 0) {
					System.out.print("saved in DB");
				}else {
					JOptionPane.showMessageDialog(null, "ერრორ!");
				}
				if (!con.isClosed()) {
					con.close();
				}
			} else {
				JOptionPane.showMessageDialog(null, "თქვენ წააგეთ!");
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/bingo_db";
				String user = "root";
				String pass = "";
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url, user, pass);
				List<Integer> listOfNumbers = new ArrayList<>(userPickedNumbers);
				String query = "INSERT INTO ticket( user_username, number_1, number_2, number_3, number_4, number_5, win) VALUES ('"+this.username+"',"+listOfNumbers.get(0) +","+listOfNumbers.get(1) +","+listOfNumbers.get(2) +","+listOfNumbers.get(3) +","+listOfNumbers.get(4) +",0)";
				Statement statement = con.createStatement();
				int a  = statement.executeUpdate(query);
				if(a > 0) {
					System.out.print("saved in DB");
				}else {
					JOptionPane.showMessageDialog(null, "ერრორ!");
				}
				if (!con.isClosed()) {
					con.close();
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "შეცდომაა! -" + e.getMessage());
		}
	}
	
	
	public void pickNumber() throws Exception {
//		Random rand = new Random();
//		while (lottRandomNumbers.size() <= 5) {
//			int randd = rand.nextInt(25);
//			lottRandomNumbers.add(randd);
//			System.out.print(randd);
//		}
		
		try {
			if( lottRandomNumbers.size() >= LOTTO_TICKET_NUMBER_COUNT)
				return;
		 
		 	Integer randomInteger = null;
		 	while(randomInteger == null) {
			 	Integer rand = new SecureRandom().nextInt(25);

			 	if(!lottRandomNumbers.contains(rand))
			 		randomInteger = rand;
		 	}
		 	Label label = new Label(randomInteger.toString());
		 	label.getStyleClass().add("butoni");
		 	lottRandomNumbers.add(randomInteger);
			System.out.println(lottRandomNumbers);
			resultpanel.getChildren().add(label);

		 	pickNumber();
		 }catch(Exception ex) {
			 throw ex;
		 }
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		startbtn.setDisable(true);
	}
	
}
