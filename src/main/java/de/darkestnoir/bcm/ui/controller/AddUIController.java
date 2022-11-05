package de.darkestnoir.bcm.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.Part;
import org.dajlab.rebrickableapi.v3.vo.PartCategories;
import org.dajlab.rebrickableapi.v3.vo.Themes;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
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
import javafx.scene.text.TextAlignment;
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

	double cellset = 0.0;

	ObservableList<ListString> list = FXCollections.observableArrayList();

	private Color[] legoColors;

	@FXML
	private RadioButton addPart;

	@FXML
	private RadioButton addSet;

	@FXML
	private TextField addPartSearch;

	@FXML
	private HBox mainHBox;

	@FXML
	private VBox partCategoryVBox;

	@FXML
	private VBox themesVBox;

	@FXML
	private VBox partTableVBox;

	@FXML
	private VBox colorVBox;

	@FXML
	private TextField addCategorySearch;

	@FXML
	private ListView<ListString> addPartCategoryList;

	@FXML
	private TextField themesSearch;

	@FXML
	private ListView<ListString> themesList;

	@FXML
	private TableView<PartTableElementModel> addPartTable;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnImage;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnBrand;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnNumber;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnANumber;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnName;

	@FXML
	private TableColumn<PartTableElementModel, String> addPartColumnCategory;

	@FXML
	private TextField addColorSearch;

	@FXML
	private ListView<ListString> addColorList;

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

		loadColors("");
		loadPartCategories("");
		loadThemes("");
		loadParts();

		setPreferredWidth(addPartCategoryList, partCategoryVBox);
		setPreferredWidth(themesList, themesVBox);
		setPreferredWidth(addColorList, colorVBox);

	}

	private boolean isImageViewInteractable(ImageView imageView) {
		return imageView.getImage() != null && imageView.getImage().getUrl().contains("http");
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

		PartCategories[] partCategories = BCMApplication.getDatabase().getPartCategories();

		for (PartCategories partCategory : partCategories) {
			if (partCategory.getName().toLowerCase().contains(filter.toLowerCase())) {
				list.add(new ListString(partCategory.getName()));
			}
		}

		addPartCategoryList.getItems().addAll(list);

	}

	private void loadParts() {

		addPartColumnImage.setCellValueFactory(new PropertyValueFactory<>("partImage"));

		addPartColumnImage.setCellFactory(tc -> {
			return new TableCell<PartTableElementModel, String>() {
				private final ImageView imageView = new ImageView();
				private final ImageCache imageCache = ImageCache.getInstance();
				private Task<Image> dataLoadTask;

				private int index;

				private void handleImageLoading(String url, ImageView imageView, Labeled node) {
					if (url != null && !url.trim().isEmpty()) {
						try {
							loadImage(url, imageView, node);
						} catch (Exception e) {
							loadImage("icons/image_not_supported.png", imageView, node);
						}
					} else {
						loadImage("icons/image_not_supported.png", imageView, node);
					}
				}

				private void loadImage(String url, ImageView imageView, Labeled node) {
					if (dataLoadTask != null) {
						dataLoadTask.cancel();
					}

					dataLoadTask = new Task<Image>() {
						@Override
						public Image call() {
							return imageCache.get(url);
						}
					};

					dataLoadTask.setOnSucceeded(e -> {
						imageView.setImage(dataLoadTask.getValue());
						if (node != null) {
							node.setGraphic(imageView);
						}
					});

					exec.execute(dataLoadTask);
				}

				@Override
				protected void updateItem(String url, boolean empty) {
					super.updateItem(url, empty);

					imageView.setPreserveRatio(true);
					imageView.setFitWidth(48);

					setOnMouseEntered(event -> {
						if (isImageViewInteractable(imageView)) {
							setCursor(Cursor.HAND);
						}
					});

					setOnMouseExited(event -> setCursor(Cursor.DEFAULT));

					setOnMouseClicked(event -> {
						if (isImageViewInteractable(imageView)) {
							index = getIndex();

							BorderPane root = new BorderPane();

							final ImageView alertImageView = new ImageView();

							alertImageView.setPreserveRatio(true);
							alertImageView.setFitWidth(500);
							alertImageView.setFitHeight(500);
							handleImageLoading(url, alertImageView, null);

							BorderPane root2 = new BorderPane();

							Label imageLabel = new Label("test");
							imageLabel.setTextAlignment(TextAlignment.CENTER);

							Button leftButton = new Button("<");
							leftButton.setOnMouseClicked(event2 -> {
								int currentIndex = index;
								currentIndex -= 1;

								if (currentIndex == -1) {
									currentIndex = BCMApplication.getDatabase().getAllParts().length - 1;
								}

								while (true) {
									String newPartUrl = BCMApplication.getDatabase().getAllParts()[currentIndex].getPartImgUrl();
									if (newPartUrl != null) {
										break;
									}
									currentIndex -= 1;
									if (currentIndex == -1) {
										currentIndex = BCMApplication.getDatabase().getAllParts().length - 1;
									}
								}

								index = currentIndex;

								Part a = BCMApplication.getDatabase().getAllParts()[currentIndex];
								setItem(a.getPartImgUrl());

								handleImageLoading(getItem(), alertImageView, null);

								addPartTable.getSelectionModel().select(index);
								addPartTable.scrollTo(index);
								alertImageView.requestFocus();
							});

							Button rightButton = new Button(">");
							rightButton.setOnMouseClicked(event2 -> {
								int currentIndex = index;
								currentIndex += 1;

								if (currentIndex == BCMApplication.getDatabase().getAllParts().length) {
									currentIndex = 0;
								}

								while (true) {
									String newPartUrl = BCMApplication.getDatabase().getAllParts()[currentIndex].getPartImgUrl();
									if (newPartUrl != null) {
										break;
									}
									currentIndex += 1;
									if (currentIndex == BCMApplication.getDatabase().getAllParts().length) {
										currentIndex = 0;
									}
								}

								index = currentIndex;

								Part a = BCMApplication.getDatabase().getAllParts()[currentIndex];
								setItem(a.getPartImgUrl());

								handleImageLoading(getItem(), alertImageView, null);

								addPartTable.getSelectionModel().select(index);
								addPartTable.scrollTo(index);
								alertImageView.requestFocus();
							});

							root.setCenter(alertImageView);
							root.setBottom(root2);
							root2.setCenter(imageLabel);
							root2.setLeft(leftButton);
							root2.setRight(rightButton);
							root2.setPrefHeight(25);
							root.layout();

							Scene errorScene = new Scene(root, 500, 500 + root2.getHeight());
							Stage errorStage = new Stage();

							root.setOnMouseClicked(evt -> errorStage.close());

							errorStage.setScene(errorScene);
							errorStage.initStyle(StageStyle.UNDECORATED);
							errorStage.initModality(Modality.APPLICATION_MODAL);
							errorStage.initOwner(BCMApplication.getUiStage());
							errorStage.show();
							alertImageView.requestFocus();
						}
					});

					handleImageLoading(url, imageView, this);
				}
			};
		});

		addPartColumnBrand.setCellValueFactory(new PropertyValueFactory<>("partBrand"));
		addPartColumnNumber.setCellValueFactory(new PropertyValueFactory<>("partNumber"));
		addPartColumnANumber.setCellValueFactory(new PropertyValueFactory<>("partANumber"));
		addPartColumnName.setCellValueFactory(new PropertyValueFactory<>("partName"));
		addPartColumnCategory.setCellValueFactory(new PropertyValueFactory<>("partCategory"));

		Part[] parts = BCMApplication.getDatabase().getAllParts();

		ObservableList<PartTableElementModel> partTablelist = FXCollections.observableArrayList();
		for (Part part : parts) {

			PartTableElementModel partTableElementModel = new PartTableElementModel(part.getPartImgUrl(), "Lego", part.getPartNum(), "ANumber", part.getName(),
					part.getPartCatId());

			partTablelist.add(partTableElementModel);
		}

		for (int i = 0; i < 100; i++) {
			PartTableElementModel partTableElementModel = new PartTableElementModel("", "Lego", "", "ANumber", "", "");
			partTablelist.add(partTableElementModel);
		}

		System.out.println(partTablelist.size());
		addPartTable.setItems(partTablelist);
	}

	private void loadThemes(String filter) {
		list.clear();
		themesList.getItems().clear();

		Themes[] themes = BCMApplication.getDatabase().getThemes();

		for (Themes theme : themes) {
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

}
