package de.darkestnoir.bcm;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WelcomeUI extends Application {

	private static Stage UIStage;
	private static boolean settingsLoaded = false;

	@Override
	public void start(Stage WelcomeUIStage) throws Exception {
		UIStage = WelcomeUIStage;

		Parent root;

		if (!settingsLoaded) {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("welcomeUI.fxml"));
			WelcomeUIStage.setResizable(false);
			WelcomeUIStage.setMaximized(false);
		} else {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("mainMenu.fxml"));
			WelcomeUIStage.setResizable(true);
		}

		Scene welcomeScene = new Scene(root);
		welcomeScene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
		Image icon = new Image("icons/app.png");
		WelcomeUIStage.getIcons().add(icon);
		WelcomeUIStage.setTitle("Brick Collection Manager");

		// WelcomeUIStage.setMinWidth(700);
		// WelcomeUIStage.setMinHeight(450);
		// WelcomeUIStage.setMaxWidth(700);
		// WelcomeUIStage.setMaxHeight(450);
		WelcomeUIStage.setScene(welcomeScene);
		WelcomeUIStage.show();
	}

	public static void close() {
		if (UIStage != null) {
			UIStage.close();
			System.out.println("close");
		} else {
			System.out.println("error");
		}
	}

	public static void start(boolean settingsLoaded) {
		WelcomeUI.settingsLoaded = settingsLoaded;
		launch();
	}

	public static void switchToMainUI() {
		Parent registrationRoot;
		try {
			FXMLLoader loader = new FXMLLoader(WelcomeUI.class.getClassLoader().getResource("mainMenu.fxml"));
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
		}
	}

}
