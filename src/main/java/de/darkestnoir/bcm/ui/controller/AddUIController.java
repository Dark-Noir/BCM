package de.darkestnoir.bcm.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.PartCategories;
import org.dajlab.rebrickableapi.v3.vo.Themes;

import de.darkestnoir.bcm.BCMApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddUIController implements Initializable {
	public static class ListString {
		private final SimpleStringProperty listString;

		public ListString(String listString) {
			this.listString = new SimpleStringProperty(listString);
		}

		public String getListString() {
			return listString.get();
		}

		@Override
		public String toString() {
			return listString.get();
		}

	}

	ObservableList<ListString> list = FXCollections.observableArrayList();

	private Color[] legoColors;

	@FXML
	private RadioButton addPart;

	@FXML
	private RadioButton addSet;

	@FXML
	private TextField addPartSearch;

	@FXML
	private SplitPane splitPane;

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
	private TableView<?> addPartTable;

	@FXML
	private TableColumn<?, ?> addPartColumnImage;

	@FXML
	private TableColumn<?, ?> addPartColumnBrand;

	@FXML
	private TableColumn<?, ?> addPartColumnNumber;

	@FXML
	private TableColumn<?, ?> addPartColumnDesc;

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
		splitPane.getItems().remove(themesVBox);
		partOrSet.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (partOrSet.getSelectedToggle() != null) {
					PartOrSet();
				}
			}
		});
		loadColors("");
		loadPartCategories("");
		loadThemes("");

	}

	private void PartOrSet() {
		if (addPart.isSelected()) {
			splitPane.getItems().remove(themesVBox);
			splitPane.getItems().add(0, partCategoryVBox);
			splitPane.getItems().add(2, colorVBox);
		} else {
			splitPane.getItems().add(0, themesVBox);
			splitPane.getItems().remove(partCategoryVBox);
			splitPane.getItems().remove(colorVBox);
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

}
