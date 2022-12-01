package de.darkestnoir.bcm.ui.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.dajlab.rebrickableapi.v3.service.IRebrickableService;
import org.dajlab.rebrickableapi.v3.service.RebrickableServiceImpl;
import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.Part;
import org.dajlab.rebrickableapi.v3.vo.PartCategories;
import org.dajlab.rebrickableapi.v3.vo.RebrickableException;
import org.dajlab.rebrickableapi.v3.vo.Themes;

import de.darkestnoir.bcm.BCMApplication;
import de.darkestnoir.bcm.FileUtils;
import de.darkestnoir.bcm.ImageCache;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainUIController {

	@FXML
	private AnchorPane sceneAnchorPane;

	String dbPath;

	@FXML
	public void initialize() {
		IRebrickableService service = new RebrickableServiceImpl(BCMApplication.getSettings().getApiKey());

		LocalDateTime lastApiSyncTime = BCMApplication.getDatabase().getApiDate();
		LocalDateTime currentLocalDate = LocalDateTime.now();

		if (lastApiSyncTime == null || currentLocalDate.isAfter(lastApiSyncTime.plusDays(10))) {
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

			try {
				PartCategories[] partCategories = service.getPartCategories().getAllPartCategories();

				List<PartCategories> partCategoriesList = new ArrayList(Arrays.asList(partCategories));

				Collections.sort(partCategoriesList);

				BCMApplication.getDatabase().setPartCategories(partCategoriesList.toArray(new PartCategories[partCategoriesList.size()]));

				System.out.println("Loading part categories done");
			} catch (RebrickableException e) {
				e.printStackTrace();
				System.out.println("Loading part categories failed");
			}

			try {
				Themes[] themes = service.getThemes().getAllThemes();

				List<Themes> themesList = new ArrayList(Arrays.asList(themes));

				Collections.sort(themesList);

				BCMApplication.getDatabase().setThemes(themesList.toArray(new Themes[themesList.size()]));

				System.out.println("Loading themes done");
			} catch (RebrickableException e) {
				e.printStackTrace();
				System.out.println("Loading themes failed");
			}

			try {
				Part[] part = service.getParts().getAllParts();
				BCMApplication.getDatabase().setLegoParts(part);

				System.out.println("Loading parts done");
			} catch (RebrickableException e) {
				e.printStackTrace();
				System.out.println("Loading parts failed");
			}

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

	@FXML
	public void mainAddClick(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(BCMApplication.class.getClassLoader().getResource("addUI.fxml"));
		Parent parent;
		try {
			parent = fxmlLoader.load();

			Scene scene = new Scene(parent, 1024, 736);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			Image icon = new Image("icons/app.png");
			stage.getIcons().add(icon);
			stage.setTitle("Add...");
			stage.setMinWidth(1024);
			stage.setMinHeight(736);

			try {
				dbPath = BCMApplication.getSettings().getDatabasePath();
				dbPath = dbPath.substring(0, dbPath.lastIndexOf("\\") + 1) + "imageCache.db";

				HashMap<String, String> imageBase64Cache = (HashMap<String, String>) FileUtils.loadImageCacheFromFile(dbPath);
				ImageCache.getInstance().getBase64ImageCache().putAll(imageBase64Cache);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

			stage.show();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					try {
						FileUtils.saveImageCacheToFile(ImageCache.getInstance().getBase64ImageCache(), dbPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void open(ActionEvent event) {
	}

	@FXML
	public void quit(ActionEvent event) {
		BCMApplication.showClosePopup(null);
	}

	@FXML
	public void save(ActionEvent event) {
	}

}
