package com.raulrh.tiendatelevisiones;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.raulrh.tiendatelevisiones.gui.Model;
import com.raulrh.tiendatelevisiones.gui.View;
import com.raulrh.tiendatelevisiones.gui.controllers.MainController;
import com.raulrh.tiendatelevisiones.util.Preferences;

public class Main {
    public static void main(String[] args) {
        Preferences preferences = Preferences.getInstance();
        if (preferences.isDarkMode()) {
            FlatDarculaLaf.setup();
        } else {
            FlatIntelliJLaf.setup();
        }

        Model model = new Model();
        View view = new View();
        MainController mainController = new MainController(model, view);
    }
}
