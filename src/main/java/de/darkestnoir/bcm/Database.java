package de.darkestnoir.bcm;

import java.io.Serializable;
import java.time.LocalDateTime;

import de.dortmunddev.models.Color;
import de.dortmunddev.models.Part;
import de.dortmunddev.models.PartCategory;
import de.dortmunddev.models.Theme;

public class Database implements Serializable {

	private static final long serialVersionUID = -8120203326101679986L;

	private LocalDateTime apiDate;

	private Color[] legoColors;

	private Part[] legoParts;

	private PartCategory[] partCategories;

	private Theme[] themes;

	/**
	 * @return the legoParts
	 */
	public Part[] getAllParts() {
		return legoParts;
	}

	/**
	 * @return the apiDate
	 */
	public LocalDateTime getApiDate() {
		return apiDate;
	}

	/**
	 * @return the legoColors
	 */
	public Color[] getLegoColors() {
		return legoColors;
	}

	/**
	 * @return the legoColors
	 */
	public PartCategory[] getPartCategories() {
		return partCategories;
	}

	/**
	 * @return the themes
	 */
	public Theme[] getThemes() {
		return themes;
	}

	/**
	 * @param apiDate the apiDate to set
	 */
	public void setApiDate(LocalDateTime apiDate) {
		this.apiDate = apiDate;
	}

	/**
	 * @param legoColors the legoColors to set
	 */
	public void setLegoColors(Color[] legoColors) {
		this.legoColors = legoColors;
	}

	/**
	 * @param legoParts the legoParts to set
	 */
	public void setLegoParts(Part[] legoParts) {
		this.legoParts = legoParts;
	}

	public void setPartCategories(PartCategory[] partCategories) {
		this.partCategories = partCategories;
	}

	public void setThemes(Theme[] themes) {
		this.themes = themes;
	}

}
