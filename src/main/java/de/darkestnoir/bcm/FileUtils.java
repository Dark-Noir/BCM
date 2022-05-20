package de.darkestnoir.bcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
	private static String settingsPath = System.getenv("APPDATA") + File.separator + "BCM" + File.separator;

	public static boolean hasWriteAccess(String FilePath) {
		return Files.isWritable(new File(FilePath).toPath());

	}

	public static Database loadDatabaseFromFile(String databasePath) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(databasePath);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		Database database = (Database) objectInputStream.readObject();
		objectInputStream.close();
		System.out.println(database);
		return database;
	}

	public static Settings loadSettingsFromFile(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(settingsPath + fileName);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		Settings settings = (Settings) objectInputStream.readObject();
		objectInputStream.close();
		System.out.println(settings);
		return settings;
	}

	public static void saveDatabaseToFile(Database database, String databasePath) throws IOException {

		// Files.createDirectories(Paths.get(databasePath));

		FileOutputStream fileOutputStream = new FileOutputStream(databasePath);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(database);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	public static void saveSettingsToFile(Settings settings, String fileName) throws IOException {

		Files.createDirectories(Paths.get(settingsPath));

		FileOutputStream fileOutputStream = new FileOutputStream(settingsPath + fileName);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(settings);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	private FileUtils() {
	}
}
