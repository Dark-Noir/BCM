package de.darkestnoir.bcm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WelcomeUI extends Application {

	public void start(Stage WelcomeUIStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("welcomeUI.fxml"));
		Scene welcomeScene = new Scene(root);
		welcomeScene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
		Image icon = new Image("icons/app.png");
		WelcomeUIStage.getIcons().add(icon);
		WelcomeUIStage.setTitle("Brick Collection Manager");
		WelcomeUIStage.setMinWidth(100);
		WelcomeUIStage.setMinHeight(100);
		WelcomeUIStage.setScene(welcomeScene);
		WelcomeUIStage.show();
	}

	public static void start() {
		launch();
	}

}
