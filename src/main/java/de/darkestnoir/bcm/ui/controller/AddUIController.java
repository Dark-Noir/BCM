package de.darkestnoir.bcm.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.Part;
import org.dajlab.rebrickableapi.v3.vo.PartCategories;
import org.dajlab.rebrickableapi.v3.vo.Themes;

import de.darkestnoir.bcm.BCMApplication;
import de.darkestnoir.bcm.ListString;
import de.darkestnoir.bcm.PartTableElementModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddUIController implements Initializable {
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
	public void themesSearchActive(ActionEvent event) {

	}

	@FXML
	public void themesSearchTyped(KeyEvent event) {
		loadThemes(themesSearch.getText());

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
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (partOrSet.getSelectedToggle() != null) {
					PartOrSet();
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

	private ListCell<ListString> cellFactory(ListView<ListString> view) {
		return new MyListCell(view);
	}

	private class MyListCell extends ListCell<ListString> {
		public MyListCell(ListView<ListString> listView) {
			super();
			updateListView(listView);
			setSkin(createDefaultSkin());
			insetsProperty().addListener(this::insetsChanged);
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

		private void insetsChanged(ObservableValue<? extends Insets> observable, Insets oldValue, Insets newValue) {
			cellset = newValue.getLeft() + newValue.getRight();
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

	private void PartOrSet() {
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

	private void loadParts() {

		addPartColumnImage.setCellValueFactory(new PropertyValueFactory<>("partImage"));
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
		System.out.println(partTablelist.size());
		addPartTable.setItems(partTablelist);
	}

}
