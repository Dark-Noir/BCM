package de.darkestnoir.bcm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class addMenu {
	
	public void start(Stage addMenuStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("addMenu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Image icon = new Image("icon.png");
			addMenuStage.getIcons().add(icon);
			addMenuStage.setTitle("Add...");	
			addMenuStage.setMinWidth(100);
			addMenuStage.setMinHeight(100);
			addMenuStage.setScene(scene);
			addMenuStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
