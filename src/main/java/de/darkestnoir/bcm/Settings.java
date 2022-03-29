package de.darkestnoir.bcm;

import java.io.Serializable;

public class Settings implements Serializable {

	private static final long serialVersionUID = 998183696897065480L;
	private String databasePath;
	private String apiKey;

	/**
	 * @return the databasePath //
	 */
	public String getDatabasePath() {
		return databasePath;
	}

	@Override
	public String toString() {
		return "Settings [databasePath=" + databasePath + ", apiKey=" + apiKey + "]";
	}

	/**
	 * @param databasePath the databasePath to set
	 */
	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Settings() {
		// TODO Auto-generated constructor stub
	}

}
