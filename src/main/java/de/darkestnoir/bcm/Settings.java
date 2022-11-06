package de.darkestnoir.bcm;

import java.io.Serializable;

public class Settings implements Serializable {

	private static final long serialVersionUID = 998183696897065480L;
	private String apiKey;
	private String databasePath;

	public Settings() {
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @return the databasePath //
	 */
	public String getDatabasePath() {
		return databasePath;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @param databasePath the databasePath to set
	 */
	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	@Override
	public String toString() {
		return "Settings [databasePath=" + databasePath + ", apiKey=" + apiKey + "]";
	}

}
