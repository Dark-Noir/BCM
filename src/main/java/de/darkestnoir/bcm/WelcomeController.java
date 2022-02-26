package de.darkestnoir.bcm;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WelcomeController {

	String databaseSaveFolderDefault1 = ((System.getenv("USERPROFILE")) + ("\\Documents\\") + ("BCM\\"));
	String databaseSaveFolderDefault2 = ((System.getenv("USERPROFILE")) + ("\\Documents\\"));
	String databaseSaveLocationDefault = ((System.getenv("USERPROFILE")) + ("\\Documents\\") + ("BCM\\") + ("database.db"));

	@FXML
	Stage WelcomeUIStage;

	@FXML
	public void rebrickableLinkClick() {
		try {
			Desktop.getDesktop().browse(new URL("https://rebrickable.com/login/").toURI());
		} catch (Exception e) {
		}
	}

	@FXML
	public void welcomeOKClick(ActionEvent event) {
		String rebrickableApiKey = rebrickableApiKeyText.getText();
		String databaseSaveLocation = databaseSaveLocationText.getText();
		System.out.println(rebrickableApiKey); // temporary
		System.out.println(databaseSaveLocation); // temporary
	}

	@FXML
	private Button welcomeQuitButton;

	@FXML
	public void welcomeQuitClick(ActionEvent event) {

		Stage WelcomeUIStage = (Stage) welcomeQuitButton.getScene().getWindow();
		WelcomeUIStage.close();

	}

	@FXML
	private Button databaseSaveLocationButton;

	@FXML
	public void databaseSaveLocationClick(ActionEvent event) {
		FileChooser databaseLocationChooser = new FileChooser();
		File databaseFolder = new File(databaseSaveFolderDefault1);
		if (databaseFolder.exists() && databaseFolder.isDirectory()) {
			databaseLocationChooser.setInitialDirectory(new File(databaseSaveFolderDefault1));
		} else {
			databaseLocationChooser.setInitialDirectory(new File(databaseSaveFolderDefault2));
		}
		databaseLocationChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database Files", "*.db"));
		databaseLocationChooser.setInitialFileName("database.db");
		File databaseSaveLocation = databaseLocationChooser.showSaveDialog(WelcomeUIStage);
		if (databaseSaveLocation != null) {
			databaseSaveLocationText.setText(databaseSaveLocation.getAbsolutePath());
		}
	}

	@FXML
	private TextField databaseSaveLocationText;

	@FXML
	private Button welcomeOKButton;

	@FXML
	private TextField rebrickableApiKeyText;

	@FXML
	public void initialize() {

		databaseSaveLocationText.setText(databaseSaveLocationDefault);
		databaseSaveLocationText.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String databaseSaveLocationTextOld, String databaseSaveLocationTextNew) {
				checkOK(databaseSaveLocationTextNew, rebrickableApiKeyText.getText());
			}
		});

		rebrickableApiKeyText.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String rebrickableApiKeyTextOld, String rebrickableApiKeyTextNew) {
				checkOK(databaseSaveLocationText.getText(), rebrickableApiKeyTextNew);
			}
		});

	}

	public void checkOK(String databaseSaveLocationTextNew, String rebrickableApiKeyTextNew) {
		if (databaseSaveLocationTextNew.endsWith(".db") && rebrickableApiKeyTextNew.matches("^[A-Za-z0-9]{32,32}$")) {
			welcomeOKButton.setDisable(false);
		} else {
			welcomeOKButton.setDisable(true);
		}
	}
}
