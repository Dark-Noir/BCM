package de.darkestnoir.bcm.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.dajlab.rebrickableapi.v3.vo.Color;

import de.darkestnoir.bcm.BCMApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddUIController implements Initializable {
	public static class ColorString {
		private final SimpleStringProperty colorString;

		public ColorString(String colorString) {
			this.colorString = new SimpleStringProperty(colorString);
		}

		public String getColorString() {
			return colorString.get();
		}

		@Override
		public String toString() {
			return colorString.get();
		}

	}

	ObservableList<ColorString> list = FXCollections.observableArrayList();

	private Color[] legoColors;

	@FXML
	private RadioButton addPart;

//	@FXML
//	private void initialize() {
//		ToggleGroup partOrSet = new ToggleGroup();
//		addPart.setToggleGroup(partOrSet);
//		addSet.setToggleGroup(partOrSet);
//	}

	@FXML
	private RadioButton addSet;

	@FXML
	private TextField addPartSearch;

	@FXML
	private TextField addCategorySearch;

	@FXML
	private ListView<?> addCategoryList;

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
	private ListView<ColorString> addColorList;

	@FXML
	public void addCategorySearchActive(ActionEvent event) {

	}

	@FXML
	public void addCategorySearchTyped(KeyEvent event) {

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
		initiateCols();
		loadColors("");

	}

	private void initiateCols() {
//		addColorTableColumn.setCellValueFactory(new PropertyValueFactory<>("colors"));
	}

	private void loadColors(String filter) {
		list.clear();
		addColorList.getItems().clear();

		Color[] colors = BCMApplication.getDatabase().getLegoColors();

		for (Color color : colors) {
			if (color.getName().toLowerCase().contains(filter.toLowerCase())) {
				list.add(new ColorString(color.getName()));
			}
		}

		addColorList.getItems().addAll(list);

	}

	public void setLegoColors(Color[] legoColors) {
		this.legoColors = legoColors;

	}

}
