package de.darkestnoir.bcm;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.Part;
import org.dajlab.rebrickableapi.v3.vo.PartCategories;
import org.dajlab.rebrickableapi.v3.vo.Themes;

public class Database implements Serializable {

	private static final long serialVersionUID = -8120203326101679986L;

	private Color[] legoColors;

	private PartCategories[] partCategories;

	private Themes[] themes;

	private Part[] legoParts;

	private LocalDateTime apiDate;

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
	public PartCategories[] getPartCategories() {
		return partCategories;
	}

	/**
	 * @return the themes
	 */
	public Themes[] getThemes() {
		return themes;
	}

	/**
	 * @return the legoParts
	 */
	public Part[] getAllParts() {
		return legoParts;
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

	public void setPartCategories(PartCategories[] partCategories) {
		this.partCategories = partCategories;
	}

	public void setThemes(Themes[] themes) {
		this.themes = themes;
	}

	/**
	 * @param legoParts the legoParts to set
	 */
	public void setLegoParts(Part[] legoParts) {
		this.legoParts = legoParts;
	}

}
