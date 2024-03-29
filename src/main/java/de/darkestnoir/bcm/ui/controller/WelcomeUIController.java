package de.darkestnoir.bcm.ui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.darkestnoir.bcm.BCMApplication;
import de.darkestnoir.bcm.Database;
import de.darkestnoir.bcm.FileUtils;
import de.darkestnoir.bcm.Settings;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WelcomeUIController {

	private String databaseSaveFolderDefault2 = ((System.getenv("USERPROFILE")) + ("\\Documents\\"));
	private String databaseSaveFolderDefault1 = databaseSaveFolderDefault2 + ("BCM\\");
	private String databaseSaveLocationDefault = databaseSaveFolderDefault1 + ("database.db");

	@FXML
	private Stage uiStage;

	@FXML
	private Label databaseInvalidLabel;

	@FXML
	private Button welcomeQuitButton;

	@FXML
	private Button databaseSaveLocationButton;

	@FXML
	private TextField databaseSaveLocationTextField;

	@FXML
	private Button welcomeOKButton;

	@FXML
	private TextField rebrickableApiKeyTextField;

	@FXML
	private Label apiKeyInvalidLabel;

	@FXML
	private AnchorPane sceneAnchorPane;

	/**
	 * @param databaseSaveLocationText
	 * @param rebrickableApiKeyText
	 */
	public void checkInputValidity(String databaseSaveLocationText, String rebrickableApiKeyText) {
		boolean okButtonEnabled = false;
		boolean apiKeyValid = false;

		if (rebrickableApiKeyText.matches("^[A-Za-z0-9]{32,32}$")) {
			apiKeyValid = true;

			if (databaseSaveLocationText.endsWith(".db")) {
				okButtonEnabled = true;
			}
		}

		apiKeyInvalidLabel.setVisible(!apiKeyValid);
		welcomeOKButton.setDisable(!okButtonEnabled);
	}

	@FXML
	public void initialize() {
		if (databaseSaveLocationTextField != null) {
			databaseSaveLocationTextField.setText(databaseSaveLocationDefault);
			databaseSaveLocationTextField.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String databaseSaveLocationTextOld, String databaseSaveLocationTextNew) {
					checkInputValidity(databaseSaveLocationTextNew, rebrickableApiKeyTextField.getText());
				}
			});

			rebrickableApiKeyTextField.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String rebrickableApiKeyTextOld, String rebrickableApiKeyTextNew) {
					checkInputValidity(databaseSaveLocationTextField.getText(), rebrickableApiKeyTextNew);
				}
			});
		}
	}

	@FXML
	public void openDBFileChooser(ActionEvent event) {
		FileChooser databaseLocationChooser = new FileChooser();
		File databaseFolder = new File(databaseSaveFolderDefault1);
		if (databaseFolder.exists() && databaseFolder.isDirectory()) {
			databaseLocationChooser.setInitialDirectory(new File(databaseSaveFolderDefault1));
		} else {
			databaseLocationChooser.setInitialDirectory(new File(databaseSaveFolderDefault2));
		}
		databaseLocationChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database Files", "*.db"));
		databaseLocationChooser.setInitialFileName("database.db");
		File databaseSaveLocation = databaseLocationChooser.showSaveDialog(uiStage);
		if (databaseSaveLocation != null) {
			databaseSaveLocationTextField.setText(databaseSaveLocation.getAbsolutePath());
		}
	}

	@FXML
	public void rebrickableLinkClick() {
		try {
			Desktop.getDesktop().browse(new URL("https://rebrickable.com/login/").toURI());
		} catch (Exception e) {
		}
	}

	@FXML
	public void welcomeOKClick(ActionEvent event) {
		String rebrickableApiKey = rebrickableApiKeyTextField.getText();
		String databaseSaveLocation = databaseSaveLocationTextField.getText();
		System.out.println(rebrickableApiKey); // temporary
		System.out.println(databaseSaveLocation); // temporary

		String databaseFilePath = databaseSaveLocation.substring(0, databaseSaveLocation.lastIndexOf("\\"));
		Settings settings = new Settings();
		settings.setDatabasePath(databaseSaveLocation);
		settings.setApiKey(rebrickableApiKey);

		Database database = new Database();

		try {
			Files.createDirectories(Paths.get(databaseFilePath));
			try {
				FileUtils.saveSettingsToFile(settings, "BCM.settings");
				FileUtils.saveDatabaseToFile(database, databaseSaveLocation);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						BCMApplication.switchToMainUI();
					}
				});

			} catch (IOException e1) {
				e1.printStackTrace();
			} // Todo API key check
				// Todo Check if database with same name exists
		} catch (IOException e1) {
			databaseInvalidLabel.setVisible(true);
		}
	}

	@FXML
	public void welcomeQuitClick(ActionEvent event) {
		BCMApplication.showClosePopup(null);
	}
}
