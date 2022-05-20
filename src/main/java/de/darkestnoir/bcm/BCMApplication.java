package de.darkestnoir.bcm;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BCMApplication extends Application {

	private static Stage UIStage;
	private static boolean settingsLoaded = false;
	public static Database database;
	public static Settings settings;

	@Override
	public void start(Stage MainUIStage) throws Exception {
		UIStage = MainUIStage;

		Parent root;

		if (!settingsLoaded) {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("welcomeUI.fxml"));
			MainUIStage.setResizable(false);
			MainUIStage.setMaximized(false);
		} else {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("mainMenu.fxml"));
			MainUIStage.setResizable(true);
		}

		Scene Scene = new Scene(root);
		Scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
		Image icon = new Image("icons/app.png");
		MainUIStage.getIcons().add(icon);
		MainUIStage.setTitle("Brick Collection Manager");

		// WelcomeUIStage.setMinWidth(700);
		// WelcomeUIStage.setMinHeight(450);
		// WelcomeUIStage.setMaxWidth(700);
		// WelcomeUIStage.setMaxHeight(450);
		MainUIStage.setScene(Scene);
		MainUIStage.show();
	}

	public static void close() {
		if (UIStage != null) {
			UIStage.close();
			System.out.println("close");
		} else {
			System.out.println("error");
		}
	}

	public static void start(boolean settingsLoaded, Database database, Settings settings) {
		BCMApplication.settingsLoaded = settingsLoaded;
		BCMApplication.database = database;
		BCMApplication.settings = settings;
		launch();
	}

	public static void switchToMainUI() {
		Parent registrationRoot;
		try {
			settings = FileUtils.loadSettingsFromFile("BCM.settings");
			database = FileUtils.loadDatabaseFromFile(settings.getDatabasePath());

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

			((Stage) UIStage.getScene().getWindow()).setScene(currentScene);
			UIStage.setResizable(true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
