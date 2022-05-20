package de.darkestnoir.bcm;

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

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UIController {

	String databaseSaveFolderDefault1 = ((System.getenv("USERPROFILE")) + ("\\Documents\\") + ("BCM\\"));
	String databaseSaveFolderDefault2 = ((System.getenv("USERPROFILE")) + ("\\Documents\\"));
	String databaseSaveLocationDefault = ((System.getenv("USERPROFILE")) + ("\\Documents\\") + ("BCM\\") + ("database.db"));

	@FXML
	Stage uiStage;

	@FXML
	public void rebrickableLinkClick() {
		try {
			Desktop.getDesktop().browse(new URL("https://rebrickable.com/login/").toURI());
		} catch (Exception e) {
		}
	}

	@FXML
	private Label databaseInvalid;

	@FXML
	public void welcomeOKClick(ActionEvent event) {
		String rebrickableApiKey = rebrickableApiKeyText.getText();
		String databaseSaveLocation = databaseSaveLocationText.getText();
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
			databaseInvalid.setVisible(true);
		}
	}

	@FXML
	private Button welcomeQuitButton;

	@FXML
	public void welcomeQuitClick(ActionEvent event) {

		Stage uiStage = (Stage) welcomeQuitButton.getScene().getWindow();
		uiStage.close();

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
		File databaseSaveLocation = databaseLocationChooser.showSaveDialog(uiStage);
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

		if (databaseSaveLocationText != null) {
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
		} else {
			IRebrickableService service = new RebrickableServiceImpl("46a80b7668b5acf68df5ac4b7bff9662"); // Test API key

			LocalDateTime lastApiSyncTime = BCMApplication.database.getApiDate();
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

					BCMApplication.database.setLegoColors(colorList.toArray(new Color[colorList.size()]));

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

				BCMApplication.database.setApiDate(currentLocalDate);

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
				System.out.println(dtf.format(currentLocalDate));

				try {
					FileUtils.saveDatabaseToFile(BCMApplication.database, BCMApplication.settings.getDatabasePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	private Label apiKeyInvalid;

	public void checkOK(String databaseSaveLocationTextNew, String rebrickableApiKeyTextNew) {
		if (databaseSaveLocationTextNew.endsWith(".db") && rebrickableApiKeyTextNew.matches("^[A-Za-z0-9]{32,32}$")) {
			apiKeyInvalid.setVisible(false);
			welcomeOKButton.setDisable(false);
		} else if (!rebrickableApiKeyTextNew.matches("^[A-Za-z0-9]{32,32}$")) {
			apiKeyInvalid.setVisible(true);
			welcomeOKButton.setDisable(true);
		} else {
			apiKeyInvalid.setVisible(false);
			welcomeOKButton.setDisable(true);
		}
	}

	@FXML
	public void quit(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("You're abour to quit!");
		alert.setContentText("Do you want to save your changes?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			uiStage = (Stage) scenePane.getScene().getWindow();
			uiStage.close();
		}
	}

	@FXML
	private AnchorPane scenePane;

	@FXML
	public void open(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		File f = chooser.showOpenDialog(uiStage);
	}

	@FXML
	public void mainAddClick(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(BCMApplication.class.getClassLoader().getResource("addMenu.fxml"));
		Parent parent;
		try {
			parent = fxmlLoader.load();

			AddUIController dialogController = fxmlLoader.<AddUIController>getController();
			dialogController.setLegoColors(BCMApplication.database.getLegoColors());

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
}
