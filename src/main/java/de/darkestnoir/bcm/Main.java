package de.darkestnoir.bcm;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			Settings settings = FileUtils.loadSettingsFromFile("BCM.settings");
			Database database = FileUtils.loadDatabaseFromFile(settings.getDatabasePath());
			WelcomeUI.start(true);
		} catch (IOException e) {
			WelcomeUI.start(false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

//		WelcomeUI.start();

//		IRebrickableService service = new RebrickableServiceImpl("46a80b7668b5acf68df5ac4b7bff9662"); // Test API key
//		try {
//			Color[] color = service.getColors().getAllColors();
//
//			for (int i = 0; i < color.length; i++) {
//				System.out.println(color[i].getName());
//			}
//		} catch (RebrickableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		IRebrickableService service = new RebrickableServiceImpl("46a80b7668b5acf68df5ac4b7bff9662"); // Test API key
//		try {
//			Part[] part = service.getParts().getAllParts();
//
//			for (int i = 0; i < part.length; i++) {
//				System.out.println(part[i].getName());
//			}
//		} catch (RebrickableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
