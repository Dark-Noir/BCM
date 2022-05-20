package de.darkestnoir.bcm.ui.controller;

import java.io.IOException;

import de.darkestnoir.bcm.BCMApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainUIController {

	public static void showClosePopup(WindowEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("You're about to quit!");
		alert.setContentText("Do you want to save your changes?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			((Stage) (BCMApplication.getUiStage().getScene().getWindow())).close();
		} else if (event != null) {
			event.consume();
		}
	}

	@FXML
	private AnchorPane sceneAnchorPane;

	@FXML
	public void mainAddClick(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(BCMApplication.class.getClassLoader().getResource("addUI.fxml"));
		Parent parent;
		try {
			parent = fxmlLoader.load();

			AddUIController dialogController = fxmlLoader.<AddUIController>getController();
			dialogController.setLegoColors(BCMApplication.getDatabase().getLegoColors());

			Scene scene = new Scene(parent, 300, 200);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			Image icon = new Image("icons/app.png");
			stage.getIcons().add(icon);
			stage.setTitle("Add...");
			stage.setMinWidth(600);
			stage.setMinHeight(500);

			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void open(ActionEvent event) {
	}

	@FXML
	public void quit(ActionEvent event) {
		MainUIController.showClosePopup(null);
	}

	@FXML
	public void save(ActionEvent event) {
	}

}
