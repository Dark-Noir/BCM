package de.darkestnoir.bcm;

import javafx.beans.property.SimpleStringProperty;

public class PartTableElementModel {
	private final SimpleStringProperty partImage;
	private final SimpleStringProperty partBrand;
	private final SimpleStringProperty partNumber;
	private final SimpleStringProperty partANumber;
	private final SimpleStringProperty partName;
	private final SimpleStringProperty partCategory;

	public PartTableElementModel(String partImage, String partBrand, String partNumber, String partANumber, String partName, String partCategory) {
		this.partImage = new SimpleStringProperty(partImage);
		this.partBrand = new SimpleStringProperty(partBrand);
		this.partNumber = new SimpleStringProperty(partNumber);
		this.partANumber = new SimpleStringProperty(partANumber);
		this.partName = new SimpleStringProperty(partName);
		this.partCategory = new SimpleStringProperty(partCategory);
	}

	public String getPartImage() {
		return partImage.get();
	}

	public String getPartBrand() {
		return partBrand.get();
	}

	public String getPartNumber() {
		return partNumber.get();
	}

	public String getPartANumber() {
		return partANumber.get();
	}

	public String getPartName() {
		return partName.get();
	}

	public String getPartCategory() {
		return partCategory.get();
	}

}