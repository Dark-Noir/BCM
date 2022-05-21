package de.darkestnoir.bcm;

import javafx.beans.property.SimpleStringProperty;

public class ListString {
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