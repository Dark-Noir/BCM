package de.darkestnoir.bcm;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;


public class MainUI extends Application {
	@Override
	public void start(Stage MainUIStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Image icon = new Image("assets/icons/app.png");
			MainUIStage.getIcons().add(icon);
			MainUIStage.setTitle("Brick Collection Manager");	
			MainUIStage.setMinWidth(100);
			MainUIStage.setMinHeight(100);
			MainUIStage.setScene(scene);
			MainUIStage.show();
			
			MainUIStage.setOnCloseRequest(event -> {
				event.consume();
				quit(MainUIStage);
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void quit(Stage MainUIStage) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quit");
		alert.setHeaderText("You're abour to quit!");
		alert.setContentText("Do you want to save your changes?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			MainUIStage.close();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
