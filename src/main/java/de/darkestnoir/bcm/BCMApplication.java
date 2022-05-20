package de.darkestnoir.bcm;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BCMApplication extends Application {

	private static Stage uiStage;
	private static boolean settingsLoaded = false;
	private static Database database;
	private static Settings settings;

	public static Database getDatabase() {
		return database;
	}

	public static Settings getSettings() {
		return settings;
	}

	public static Stage getUiStage() {
		return uiStage;
	}

	public static boolean isSettingsLoaded() {
		return settingsLoaded;
	}

	public static void setDatabase(Database database) {
		BCMApplication.database = database;
	}

	public static void setSettings(Settings settings) {
		BCMApplication.settings = settings;
	}

	public static void setSettingsLoaded(boolean settingsLoaded) {
		BCMApplication.settingsLoaded = settingsLoaded;
	}

	public static void setUiStage(Stage uiStage) {
		BCMApplication.uiStage = uiStage;
	}

	public static void start(boolean settingsLoaded, Database database, Settings settings) {
		BCMApplication.setSettingsLoaded(settingsLoaded);
		BCMApplication.setDatabase(database);
		BCMApplication.setSettings(settings);
		launch();
	}

	public static void switchToMainUI() {
		Parent registrationRoot;
		try {
			setSettings(FileUtils.loadSettingsFromFile("BCM.settings"));
			setDatabase(FileUtils.loadDatabaseFromFile(settings.getDatabasePath()));

			FXMLLoader loader = new FXMLLoader(BCMApplication.class.getClassLoader().getResource("mainMenu.fxml"));
			loader.setControllerFactory((Class<?> type) -> {
				try {
					Object controller = type.newInstance();
					return controller;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});

			registrationRoot = loader.load();

			Scene currentScene = new Scene(registrationRoot, 800, 800);

			((Stage) getUiStage().getScene().getWindow()).setScene(currentScene);
			getUiStage().setResizable(true);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage mainUIStage) throws Exception {
		setUiStage(mainUIStage);

		Parent root;

		if (!settingsLoaded) {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("welcomeUI.fxml"));
			mainUIStage.setResizable(false);
			mainUIStage.setMaximized(false);
		} else {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("mainMenu.fxml"));
			mainUIStage.setResizable(true);
		}

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
		Image icon = new Image("icons/app.png");
		mainUIStage.getIcons().add(icon);
		mainUIStage.setTitle("Brick Collection Manager");

		// WelcomeUIStage.setMinWidth(700);
		// WelcomeUIStage.setMinHeight(450);
		// WelcomeUIStage.setMaxWidth(700);
		// WelcomeUIStage.setMaxHeight(450);
		mainUIStage.setScene(scene);
		mainUIStage.show();
	}

}
