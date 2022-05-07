package de.darkestnoir.bcm;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.dajlab.rebrickableapi.v3.vo.Color;
import org.dajlab.rebrickableapi.v3.vo.Part;

public class Database implements Serializable {

	private static final long serialVersionUID = -8120203326101679986L;

	private Color[] legoColors;

	/**
	 * @return the legoColors
	 */
	public Color[] getLegoColors() {
		return legoColors;
	}

	/**
	 * @param legoColors the legoColors to set
	 */
	public void setLegoColors(Color[] legoColors) {
		this.legoColors = legoColors;
	}

	private Part[] legoParts;

	/**
	 * @return the legoParts
	 */
	public Part[] getAllParts() {
		return legoParts;
	}

	/**
	 * @param legoParts the legoParts to set
	 */
	public void setLegoParts(Part[] legoParts) {
		this.legoParts = legoParts;
	}

	private LocalDateTime apiDate;

	/**
	 * @return the apiDate
	 */
	public LocalDateTime getApiDate() {
		return apiDate;
	}

	/**
	 * @param apiDate the apiDate to set
	 */
	public void setApiDate(LocalDateTime apiDate) {
		this.apiDate = apiDate;
	}

}
