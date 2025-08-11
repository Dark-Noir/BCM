package de.darkestnoir.bcm.ui.controller;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.dortmunddev.models.Color;
import de.dortmunddev.models.Part;
import de.dortmunddev.models.PartCategory;
import de.dortmunddev.models.Theme;

import de.darkestnoir.bcm.BCMApplication;
import de.darkestnoir.bcm.ImageCache;
import de.darkestnoir.bcm.ListString;
import de.darkestnoir.bcm.PartTableElementModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddUIController implements Initializable {
	private class MyListCell extends ListCell<ListString> {
		public MyListCell(ListView<ListString> listView) {
			super();
			updateListView(listView);
			setSkin(createDefaultSkin());
			insetsProperty().addListener(this::insetsChanged);
		}

		private void insetsChanged(ObservableValue<? extends Insets> observable, Insets oldValue, Insets newValue) {
			cellset = newValue.getLeft() + newValue.getRight();
		}

		@Override
		protected void updateItem(ListString item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				setText(item.getListString());
			}

			if (empty) {
				setText("");
			}
		}
	}

	private static final Executor exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), r -> {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	});

	@FXML
	private TextField addCategorySearch;

	@FXML
	private ListView<ListString> addColorList;

	@FXML
	private TextField addColorSearch;

	@FXML
	private RadioButton addPart;

	@FXML
	private ListView<ListString> addPartCategoryList;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnANumber;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnBrand;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnCategory;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnImage;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnName;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnNumber;

	@FXML
	private TextField addPartSearch;

	@FXML
	private TableView<PartTableElementModel> addPartTable;

	@FXML
	private RadioButton addSet;

	double cellset = 0.0;

	@FXML
	private VBox colorVBox;

	private Task<Image> dataLoadTask;

	private final ImageCache imageCache = ImageCache.getInstance();

	private Color[] legoColors;

	private Part[] parts;
	private Part[] filteredParts;

	ObservableList<ListString> list = FXCollections.observableArrayList();

	@FXML
	private HBox mainHBox;

	@FXML
	private VBox partCategoryVBox;

	@FXML
	private VBox partTableVBox;

	@FXML
	private ListView<ListString> themesList;

	@FXML
	private TextField themesSearch;

	@FXML
	private VBox themesVBox;

	@FXML
	private Label detailBrandLabel;

	@FXML
	private Label detailPartNumberLabel;

	@FXML
	private Label detailAltPartNumberLabel;

	@FXML
	private Label detailNameLabel;

	@FXML
	private Label detailCategoryLabel;

	@FXML
	private ImageView mainPartImageView;

	private String selectedCategory = null;
	private String selectedColor = null;

	@FXML
	public void addCategorySearchActive(ActionEvent event) {

	}

	@FXML
	public void addCategorySearchTyped(KeyEvent event) {
		loadPartCategories(addCategorySearch.getText());

	}

	@FXML
	public void addColorSearchActive(ActionEvent event) {

	}

	@FXML
	public void addColorSearchTyped(KeyEvent event) {
		loadColors(addColorSearch.getText());

	}

	@FXML
	public void addPartActive(ActionEvent event) {

	}

	@FXML
	public void addPartSearchActive(ActionEvent event) {

	}

	@FXML
	public void addPartSearchTyped(KeyEvent event) {
		// Implement part search functionality
		String searchText = addPartSearch.getText().toLowerCase();
		applyFiltersWithSearch(searchText);
	}

	@FXML
	public void addSetActive(ActionEvent event) {

	}

	private ListCell<ListString> cellFactory(ListView<ListString> view) {
		return new MyListCell(view);
	}

	private void closeStage(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ToggleGroup partOrSet = new ToggleGroup();
		addPart.setToggleGroup(partOrSet);
		addSet.setToggleGroup(partOrSet);
		mainHBox.getChildren().remove(themesVBox);
		partOrSet.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
				if (partOrSet.getSelectedToggle() != null) {
					partOrSet();
				}
			}
		});

		addPartCategoryList.setCellFactory(this::cellFactory);
		themesList.setCellFactory(this::cellFactory);
		addColorList.setCellFactory(this::cellFactory);

		// Add selection listeners for filtering
		addPartCategoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				selectedCategory = newValue.getListString();
			} else {
				selectedCategory = null;
			}
			applyFilters();
		});

		addColorList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				selectedColor = newValue.getListString();
			} else {
				selectedColor = null;
			}
			applyFilters();
		});

		loadColors("");
		loadPartCategories("");
		loadThemes("");
		loadParts();

		setPreferredWidth(addPartCategoryList, partCategoryVBox);
		setPreferredWidth(themesList, themesVBox);
		setPreferredWidth(addColorList, colorVBox);

	}

	private boolean isImageViewInteractable(ImageView imageView) {
		return imageView.getImage() != null && imageView.getImage().getUrl() != null && imageView.getImage().getUrl().contains("http");
	}

	private void loadColors(String filter) {
		list.clear();
		addColorList.getItems().clear();

		Color[] colors = BCMApplication.getDatabase().getLegoColors();

		for (Color color : colors) {
			if (color.getName().toLowerCase().contains(filter.toLowerCase())) {
				list.add(new ListString(color.getName()));
			}
		}

		addColorList.getItems().addAll(list);
	}

	private void loadPartCategories(String filter) {
		list.clear();
		addPartCategoryList.getItems().clear();

		PartCategory[] partCategories = BCMApplication.getDatabase().getPartCategories();

		for (PartCategory partCategory : partCategories) {
			if (partCategory.getName().toLowerCase().contains(filter.toLowerCase())) {
				list.add(new ListString(partCategory.getName()));
			}
		}

		addPartCategoryList.getItems().addAll(list);

	}

	private void loadParts() {

		addPartColumnImage.setCellValueFactory(new PropertyValueFactory<>("partImage"));

        addPartColumnImage.setCellFactory(tc -> new TableCell<>() {

            private final ImageView imageView = new ImageView();
            private final ImageCache imageCache = ImageCache.getInstance();

            // State per cell
            private String currentUrl;
            private ChangeListener<Number> progressListener;
            private ChangeListener<Boolean> errorListener;

            {
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(48);

                // Maus-Handler einmalig
                setOnMouseEntered(e -> {
                    if (isImageViewInteractable(imageView)) setCursor(Cursor.HAND);
                });
                setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
                setOnMouseClicked(e -> {
                    if (!isImageViewInteractable(imageView)) return;

                    final String url = getItem(); // aktuelle URL der Zelle
                    if (url == null || url.isBlank()) return;

                    BorderPane root = new BorderPane();
                    ImageView alertImageView = new ImageView();
                    alertImageView.setPreserveRatio(true);
                    alertImageView.setFitWidth(500);
                    alertImageView.setFitHeight(500);

                    // großes Bild mit gleichem Loader laden
                    loadIntoImageView(url, alertImageView, /*useCache*/ true);

                    root.setCenter(alertImageView);
                    Scene scene = new Scene(root, 500, 500);
                    Stage stage = new Stage();
                    root.setOnMouseClicked(evt -> stage.close());
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(BCMApplication.getUiStage());
                    stage.show();
                    alertImageView.requestFocus();
                });
            }

            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);

                if (empty || url == null || url.isBlank()) {
                    detachListeners();           // wichtig bei Cell-Reuse
                    currentUrl = null;
                    imageView.setImage(null);
                    setGraphic(null);
                    return;
                }

                // Nur wenn sich die URL ändert, neu laden
                if (!url.equals(currentUrl)) {
                    currentUrl = url;
                    detachListeners();
                    imageView.setImage(null);    // optional: Placeholder-Bild setzen
                    setGraphic(imageView);       // Graphic deterministisch setzen
                    loadIntoImageView(url, imageView, /*useCache*/ true);
                } else {
                    // Sicherstellen, dass die Graphic gesetzt ist (bei Reuse)
                    setGraphic(imageView);
                }
            }

            private void loadIntoImageView(String url, ImageView target, boolean useCache) {
                // 1) Versuche synchron aus dem Cache zu bekommen
                Image img = useCache ? imageCache.get(url) : null;

                if (img == null) {
                    // 2) Fallback: echtes Background-Loading, unabhängig vom Cache
                    img = new Image(url, /*backgroundLoading*/ true);
                }

                // Bild sofort setzen – wenn noch nicht fertig, rendert es, sobald geladen
                target.setImage(img);

                // 3) Für den Tabellen-Cell-Fall an Ladeende hängen + Fehler behandeln
                if (target == imageView) {
                    // Listener pro Cell halten, damit wir sie bei Reuse lösen können
                    progressListener = (obs, oldV, newV) -> {
                        if (Objects.equals(url, currentUrl) && newV.doubleValue() >= 1.0) {
                            // Laden fertig; nichts weiter nötig, ImageView aktualisiert sich selbst
                            detachListeners();
                        }
                    };
                    errorListener = (obs, oldV, newV) -> {
                        if (Boolean.TRUE.equals(newV) && Objects.equals(url, currentUrl)) {
                            target.setImage(new Image("icons/image_not_supported.png"));
                            detachListeners();
                        }
                    };

                    img.progressProperty().addListener(progressListener);
                    img.errorProperty().addListener(errorListener);
                }
            }

            private void detachListeners() {
                Image current = imageView.getImage();
                if (current != null && progressListener != null) {
                    current.progressProperty().removeListener(progressListener);
                }
                if (current != null && errorListener != null) {
                    current.errorProperty().removeListener(errorListener);
                }
                progressListener = null;
                errorListener = null;
            }
        });


        addPartColumnBrand.setCellValueFactory(new PropertyValueFactory<>("partBrand"));
		addPartColumnNumber.setCellValueFactory(new PropertyValueFactory<>("partNumber"));
		addPartColumnANumber.setCellValueFactory(new PropertyValueFactory<>("partANumber"));
		addPartColumnName.setCellValueFactory(new PropertyValueFactory<>("partName"));
		addPartColumnCategory.setCellValueFactory(new PropertyValueFactory<>("partCategory"));

		parts = BCMApplication.getDatabase().getAllParts();
		filteredParts = parts; // Initially show all parts

		updatePartTable();
		
		// Add selection listener to update part details
		addPartTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				updatePartDetails(newValue);
			} else {
				clearPartDetails();
			}
		});
	}

	private void loadThemes(String filter) {
		list.clear();
		themesList.getItems().clear();

		Theme[] themes = BCMApplication.getDatabase().getThemes();

		for (Theme theme : themes) {
			if (theme.getName().toLowerCase().contains(filter.toLowerCase())) {
				list.add(new ListString(theme.getName()));
			}
		}

		themesList.getItems().addAll(list);

	}

	private void partOrSet() {
		if (addPart.isSelected()) {
			mainHBox.getChildren().remove(themesVBox);
			mainHBox.getChildren().add(0, partCategoryVBox);
			mainHBox.getChildren().add(2, colorVBox);
		} else {
			mainHBox.getChildren().add(0, themesVBox);
			mainHBox.getChildren().remove(partCategoryVBox);
			mainHBox.getChildren().remove(colorVBox);
		}
	}

	private void setPreferredWidth(ListView<ListString> listView, Pane pane) {
		ListCell<ListString> cell = new MyListCell(listView);
		double width = 0.0;
		for (ListString item : listView.getItems()) {
			cell.setText(item.getListString() + "         ");
			width = Math.max(width, cell.prefWidth(-1));
		}

		pane.setMaxWidth(width + listView.getInsets().getLeft() + listView.getInsets().getRight());
	}

	@FXML
	public void themesSearchActive(ActionEvent event) {

	}

	@FXML
	public void themesSearchTyped(KeyEvent event) {
		loadThemes(themesSearch.getText());

	}
	
	private void updatePartDetails(PartTableElementModel selectedPart) {
		if (selectedPart != null && selectedPart.getPartNumber() != null && !selectedPart.getPartNumber().isEmpty()) {
			// Find the corresponding Part object
			Part part = findPartByNumber(selectedPart.getPartNumber());
			if (part != null) {
				detailBrandLabel.setText("Lego");
				detailPartNumberLabel.setText(part.getPartNum());
				detailAltPartNumberLabel.setText("N/A"); // Alt part number not available in current model
				detailNameLabel.setText(part.getName());
				
				// Get category name from ID
				String categoryName = getCategoryNameById(part.getPartCatId());
				detailCategoryLabel.setText(categoryName);
				
				// Update the main image
				updateMainImage(part.getPartImgUrl());
			} else {
				clearPartDetails();
			}
		} else {
			clearPartDetails();
		}
	}
	
	private void clearPartDetails() {
		detailBrandLabel.setText("-");
		detailPartNumberLabel.setText("-");
		detailAltPartNumberLabel.setText("-");
		detailNameLabel.setText("-");
		detailCategoryLabel.setText("-");
		
		// Clear the main image
		updateMainImage(null);
	}
	
	private Part findPartByNumber(String partNumber) {
		if (filteredParts != null) {
			for (Part part : filteredParts) {
				if (part.getPartNum().equals(partNumber)) {
					return part;
				}
			}
		}
		return null;
	}
	
	private String getCategoryNameById(int categoryId) {
		PartCategory[] categories = BCMApplication.getDatabase().getPartCategories();
		if (categories != null) {
			for (PartCategory category : categories) {
				if (category.getId() == categoryId) {
					return category.getName();
				}
			}
		}
		return "Unknown";
	}

	private void updateMainImage(String imageUrl) {
		if (imageUrl != null && !imageUrl.trim().isEmpty()) {
			try {
				// Load the image using the existing image cache
				Task<Image> imageTask = new Task<Image>() {
					@Override
					public Image call() {
						return imageCache.get(imageUrl);
					}
				};
				
				imageTask.setOnSucceeded(e -> {
					mainPartImageView.setImage(imageTask.getValue());
				});
				
				imageTask.setOnFailed(e -> {
					// Load default image if loading fails
					loadDefaultImage();
				});
				
				exec.execute(imageTask);
			} catch (Exception e) {
				// Load default image if there's an exception
				loadDefaultImage();
			}
		} else {
			// Load default image if no URL provided
			loadDefaultImage();
		}
	}
	
	private void loadDefaultImage() {
		try {
			Image defaultImage = new Image(getClass().getResourceAsStream("/icons/app.png"));
			mainPartImageView.setImage(defaultImage);
		} catch (Exception e) {
			// If even the default image fails, clear the image
			mainPartImageView.setImage(null);
		}
	}

	private void applyFilters() {
		// Apply filters with current search text
		String searchText = addPartSearch.getText();
		applyFiltersWithSearch(searchText);
	}

	private void updatePartTable() {
		ObservableList<PartTableElementModel> partTablelist = FXCollections.observableArrayList();
		
		if (filteredParts != null) {
			for (Part part : filteredParts) {
				PartTableElementModel partTableElementModel = new PartTableElementModel(
					part.getPartImgUrl(), 
					"Lego", 
					part.getPartNum(), 
					"ANumber", 
					part.getName(),
					String.valueOf(part.getPartCatId())
				);
				partTablelist.add(partTableElementModel);
			}
		}

		System.out.println("Filtered parts: " + (filteredParts != null ? filteredParts.length : 0));
		addPartTable.setItems(partTablelist);
	}

	private void applyFiltersWithSearch(String searchText) {
		if (parts == null) return;

		java.util.List<Part> filteredList = new java.util.ArrayList<>();

		for (Part part : parts) {
			boolean categoryMatch = true;
			boolean colorMatch = true;
			boolean searchMatch = true;

			// Check category filter
			if (selectedCategory != null) {
				String categoryName = getCategoryNameById(part.getPartCatId());
				categoryMatch = selectedCategory.equals(categoryName);
			}

			// Check color filter (for now, we'll skip color filtering as it requires part-color relationships)
			if (selectedColor != null) {
				// For now, we'll skip color filtering as we don't have part-color relationships
				colorMatch = true; // Skip color filtering for now
			}

			// Check search filter
			if (searchText != null && !searchText.trim().isEmpty()) {
				searchMatch = part.getName().toLowerCase().contains(searchText) ||
							part.getPartNum().toLowerCase().contains(searchText);
			}

			if (categoryMatch && colorMatch && searchMatch) {
				filteredList.add(part);
			}
		}

		filteredParts = filteredList.toArray(new Part[0]);
		updatePartTable();
	}

}
