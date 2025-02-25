package com.raulrh.tiendatelevisiones.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.raulrh.tiendatelevisiones.util.Preferences;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code PreferencesDialog} class represents a dialog for managing application preferences,
 * including settings for dark mode and confirmation before deletion.
 * It allows users to change the preferences and save them for future sessions.
 */
public class PreferencesDialog extends JDialog {
    private final Frame owner;

    // UI components
    public JButton saveButton;
    public JCheckBox confirmDeleteCheck;
    public JCheckBox darkMode;
    private JPanel panel;

    /**
     * Constructs a {@code PreferencesDialog} instance, initializing the dialog and its components.
     *
     * @param owner The owner frame of this dialog.
     */
    public PreferencesDialog(Frame owner) {
        super(owner, "Preferencias", true);
        this.owner = owner;
        initDialog();
    }

    /**
     * Initializes the dialog components and sets up the action listeners.
     */
    private void initDialog() {
        this.setContentPane(panel);
        this.panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(owner);

        Preferences preferences = Preferences.getInstance();
        boolean oldDarkMode = preferences.isDarkMode();

        // Action listener for saving the preferences
        saveButton.addActionListener(e -> {
            preferences.setDarkMode(darkMode.isSelected());
            preferences.setConfirmDelete(confirmDeleteCheck.isSelected());

            // Apply the dark mode theme if it was changed
            if (oldDarkMode != preferences.isDarkMode()) {
                if (oldDarkMode) {
                    FlatIntelliJLaf.setup();
                } else {
                    FlatDarculaLaf.setup();
                }

                FlatLaf.updateUI();
            }

            this.dispose();
        });

        darkMode.setSelected(preferences.isDarkMode());
        confirmDeleteCheck.setSelected(preferences.isConfirmDelete());

        this.setVisible(true);
    }
}