package de.darkestnoir.bcm;

import java.net.URL;
import java.util.ResourceBundle;

import org.dajlab.rebrickableapi.v3.vo.Color;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AddUIController implements Initializable {
	ObservableList<Colors> list = FXCollections.observableArrayList();

	private Color[] legoColors;

	@FXML
	private TableView<Colors> addColorTable;

	@FXML
	private TableColumn<Colors, String> addColorTableColumn;

	@FXML
	private TextField addColorSearchField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initiateCols();
		loadColors();

	}

	private void initiateCols() {
		addColorTableColumn.setCellValueFactory(new PropertyValueFactory<>("colors"));
	}

	private void loadColors() {
		list.clear();

		Color[] colors = UI.database.getLegoColors();

		for (Color color : colors) {
			list.addAll(new Colors(color.getName()));
		}

		addColorTable.getItems().addAll(list);

	}

	public static class Colors {
		private final SimpleStringProperty colors;

		public Colors(String colors) {
			this.colors = new SimpleStringProperty(colors);
		}

		public String getColors() {
			return colors.get();
		}

	}

	public void setLegoColors(Color[] legoColors) {
		this.legoColors = legoColors;

	}

	@FXML
	public void addColorSort(ActionEvent event) {

	}

	@FXML
	public void addColorSearch(ActionEvent event) {

	}

	private void closeStage(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

}
