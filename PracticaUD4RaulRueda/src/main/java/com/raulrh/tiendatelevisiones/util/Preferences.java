package com.raulrh.tiendatelevisiones.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Singleton class for managing application preferences, such as dark mode settings.
 * The preferences are saved and loaded from a JSON file in the user's APPDATA directory.
 */
public class Preferences {
    private static final String FILE_NAME = "preferences.json";
    private static final String FOLDER_NAME = "TiendaTV";
    private static final String APPDATA_PATH = System.getenv("APPDATA");
    private static final String FILE_PATH = APPDATA_PATH + File.separator + FOLDER_NAME + File.separator + FILE_NAME;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Preferences instance;

    private boolean darkMode;
    private boolean confirmDelete = true;

    /**
     * Default constructor. Initializes a new Preferences instance with default values.
     */
    public Preferences() {
    }

    /**
     * Returns the singleton instance of the Preferences class.
     * If no instance exists, it loads preferences from file.
     *
     * @return The singleton instance of Preferences.
     */
    public static Preferences getInstance() {
        if (instance == null) {
            instance = loadPreferences();
        }

        return instance;
    }

    /**
     * Loads preferences from the JSON file. If the file does not exist or cannot be read,
     * it returns a new Preferences instance with default values.
     *
     * @return A Preferences instance loaded from file or a new instance if the file cannot be read.
     */
    private static Preferences loadPreferences() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new Preferences();
        }

        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, Preferences.class);
        } catch (IOException e) {
            return new Preferences();
        }
    }

    /**
     * Saves the current preferences to the JSON file. Creates the folder if it does not exist.
     * Throws a RuntimeException if the folder cannot be created or if there is an error saving the file.
     */
    public static void savePreferences() {
        File folder = new File(APPDATA_PATH + File.separator + FOLDER_NAME);
        if (!folder.exists() && !folder.mkdir()) {
            throw new RuntimeException("Could not create the configuration folder");
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(getInstance(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the dark mode setting.
     *
     * @return {@code true} if dark mode is enabled; {@code false} otherwise.
     */
    public boolean isDarkMode() {
        return darkMode;
    }

    /**
     * Sets the dark mode preference.
     *
     * @param darkMode {@code true} to enable dark mode; {@code false} to disable it.
     */
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    /**
     * Returns the confirmation setting for delete actions.
     *
     * @return {@code true} if delete confirmation is enabled; {@code false} otherwise.
     */
    public boolean isConfirmDelete() {
        return confirmDelete;
    }

    /**
     * Sets the delete confirmation preference.
     *
     * @param confirmDelete {@code true} to enable delete confirmation; {@code false} to disable it.
     */
    public void setConfirmDelete(boolean confirmDelete) {
        this.confirmDelete = confirmDelete;
    }
}