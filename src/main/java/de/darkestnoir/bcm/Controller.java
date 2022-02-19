package de.darkestnoir.bcm;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	@FXML
	private AnchorPane scenePane;
	
	Stage primaryStage;
	
	public void quit(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("You're abour to quit!");
		alert.setContentText("Do you want to save your changes?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			primaryStage = (Stage) scenePane.getScene().getWindow();
			primaryStage.close();
		}
	}
	@FXML
	public void open(ActionEvent event){
		FileChooser chooser = new FileChooser();
		File f = chooser.showOpenDialog(primaryStage);
	}
	
}
