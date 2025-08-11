package de.darkestnoir.bcm.ui.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.dortmunddev.RebrickableApiClient;
import de.dortmunddev.LegoService;
import de.dortmunddev.ApiException;
import de.dortmunddev.models.Color;
import de.dortmunddev.models.Part;
import de.dortmunddev.models.PartCategory;
import de.dortmunddev.models.Theme;

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
		try (RebrickableApiClient client = new RebrickableApiClient(BCMApplication.getSettings().getApiKey())) {
			LegoService legoService = client.lego();

			LocalDateTime lastApiSyncTime = BCMApplication.getDatabase().getApiDate();
			LocalDateTime currentLocalDate = LocalDateTime.now();

			if (lastApiSyncTime == null || currentLocalDate.isAfter(lastApiSyncTime.plusDays(10))) {
				try {
					// Get all colors (we'll need to handle pagination)
					List<Color> allColors = new ArrayList<>();
					int page = 1;
					boolean hasMore = true;
					
					while (hasMore) {
						var response = legoService.getColors(page);
						if (response.getData() != null && response.getData().getResults() != null) {
							allColors.addAll(Arrays.asList(response.getData().getResults()));
							hasMore = response.getData().hasNext();
							page++;
						} else {
							hasMore = false;
						}
					}

					List<Color> elementsToRemove = new ArrayList<>();
					for (Color currentColor : allColors) {
						if (currentColor.getName().equals("[Unknown]")) {
							elementsToRemove.add(currentColor);
						}
					}
					allColors.removeAll(elementsToRemove);
					Collections.sort(allColors, (c1, c2) -> c1.getName().compareTo(c2.getName()));

					BCMApplication.getDatabase().setLegoColors(allColors.toArray(new Color[0]));

					System.out.println("Loading colors done");
				} catch (ApiException e) {
					e.printStackTrace();
					System.out.println("Loading colors failed");
				}

				try {
					// Get all part categories
					List<PartCategory> allPartCategories = new ArrayList<>();
					int page = 1;
					boolean hasMore = true;
					
					while (hasMore) {
						var response = legoService.getPartCategories(page);
						if (response.getData() != null && response.getData().getResults() != null) {
							allPartCategories.addAll(Arrays.asList(response.getData().getResults()));
							hasMore = response.getData().hasNext();
							page++;
						} else {
							hasMore = false;
						}
					}

					Collections.sort(allPartCategories, (c1, c2) -> c1.getName().compareTo(c2.getName()));

					BCMApplication.getDatabase().setPartCategories(allPartCategories.toArray(new PartCategory[0]));

					System.out.println("Loading part categories done");
				} catch (ApiException e) {
					e.printStackTrace();
					System.out.println("Loading part categories failed");
				}

				try {
					// Get all themes
					List<Theme> allThemes = new ArrayList<>();
					int page = 1;
					boolean hasMore = true;
					
					while (hasMore) {
						var response = legoService.getThemes(page);
						if (response.getData() != null && response.getData().getResults() != null) {
							allThemes.addAll(Arrays.asList(response.getData().getResults()));
							hasMore = response.getData().hasNext();
							page++;
						} else {
							hasMore = false;
						}
					}

					Collections.sort(allThemes, (t1, t2) -> t1.getName().compareTo(t2.getName()));

					BCMApplication.getDatabase().setThemes(allThemes.toArray(new Theme[0]));

					System.out.println("Loading themes done");
				} catch (ApiException e) {
					e.printStackTrace();
					System.out.println("Loading themes failed");
				}

				try {
					// Get all parts (this might take a while due to large dataset)
					List<Part> allParts = new ArrayList<>();
					int page = 1;
					boolean hasMore = true;
					
					while (hasMore) {
						var response = legoService.getParts(page);
						if (response.getData() != null && response.getData().getResults() != null) {
							allParts.addAll(Arrays.asList(response.getData().getResults()));
							hasMore = response.getData().hasNext();
							page++;
						} else {
							hasMore = false;
						}
					}
					
					BCMApplication.getDatabase().setLegoParts(allParts.toArray(new Part[0]));

					System.out.println("Loading parts done");
				} catch (ApiException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to close API client");
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
