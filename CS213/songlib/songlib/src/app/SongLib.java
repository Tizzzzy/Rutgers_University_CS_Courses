/*
 * Author: Xiaoyu Chen, Dong Shu
 * 
 */


package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.SongController;

public class SongLib extends Application {
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/list.fxml"));
		
		// load the fxml
		AnchorPane root = (AnchorPane)loader.load();

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		SongController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene1 = new Scene(root, 600, 800);
		primaryStage.setScene(scene1);
		primaryStage.show(); 
		primaryStage.setResizable(true);
	}


	public static void main(String[] args) {
		launch(args);

	}

}