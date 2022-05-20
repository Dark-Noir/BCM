package de.darkestnoir.bcm.ui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dajlab.rebrickableapi.v3.service.IRebrickableService;
import org.dajlab.rebrickableapi.v3.service.RebrickableServiceImpl;
import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.RebrickableException;

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

	private String databaseSaveFolderDefault1 = ((System.getenv("USERPROFILE")) + ("\\Documents\\") + ("BCM\\"));
	private String databaseSaveFolderDefault2 = ((System.getenv("USERPROFILE")) + ("\\Documents\\"));
	private String databaseSaveLocationDefault = ((System.getenv("USERPROFILE")) + ("\\Documents\\") + ("BCM\\") + ("database.db"));

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
		} else {
			IRebrickableService service = new RebrickableServiceImpl("46a80b7668b5acf68df5ac4b7bff9662"); // Test API key

			LocalDateTime lastApiSyncTime = BCMApplication.getDatabase().getApiDate();
			LocalDateTime currentLocalDate = LocalDateTime.now();

			if (lastApiSyncTime == null || currentLocalDate.isAfter(lastApiSyncTime.plusDays(1))) {
				try {
					Color[] color = service.getColors().getAllColors();

					List<Color> colorList = new ArrayList(Arrays.asList(color));

					List<Color> elementsToRemove = new ArrayList<>();
					for (Color currentColor : colorList) {
						if (currentColor.getName().equals("[Unknown]")) {
							elementsToRemove.add(currentColor);
						}
					}
					colorList.removeAll(elementsToRemove);
					Collections.sort(colorList);

					BCMApplication.getDatabase().setLegoColors(colorList.toArray(new Color[colorList.size()]));

					System.out.println("Loading colors done");
				} catch (RebrickableException e) {
					e.printStackTrace();
					System.out.println("Loading colors failed");
				}

//			try {
//				Part[] part = service.getParts().getAllParts();
//				UI.database.setLegoParts(part);
//
//				System.out.println("Loading parts done");
//			} catch (RebrickableException e) {
//				e.printStackTrace();
//				System.out.println("Loading parts failed");
//			}

				BCMApplication.getDatabase().setApiDate(currentLocalDate);

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
				System.out.println(dtf.format(currentLocalDate));

				try {
					FileUtils.saveDatabaseToFile(BCMApplication.getDatabase(), BCMApplication.getSettings().getDatabasePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
