package com.raulrh.tiendatelevisiones.gui.controllers;

import com.raulrh.tiendatelevisiones.base.enums.CustomerType;
import com.raulrh.tiendatelevisiones.base.enums.TelevisionType;
import com.raulrh.tiendatelevisiones.gui.Model;
import com.raulrh.tiendatelevisiones.gui.PreferencesDialog;
import com.raulrh.tiendatelevisiones.gui.View;

import javax.swing.*;
import java.awt.*;

/**
 * The MainController class is the main controller that handles the logic for managing the application's views, models,
 * and controllers. It also handles database connections and UI settings.
 */
public class MainController {
    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public final Model model;
    public final View view;

    public final TelevisionController televisionController;
    public final CustomerController customerController;
    public final SalesController salesController;
    public final SupplierController supplierController;
    public final StockController stockController;

    public boolean isConnected = false;

    /**
     * Constructs a MainController object and initializes its components, including setting up options and buttons.
     *
     * @param model The model for the application.
     * @param view  The view for the application.
     */
    public MainController(Model model, View view) {
        this.model = model;
        this.view = view;

        televisionController = new TelevisionController(this);
        customerController = new CustomerController(this);
        salesController = new SalesController(this);
        supplierController = new SupplierController(this);
        stockController = new StockController(this);

        setupButtons();

        // Add television types to combo box
        for (TelevisionType type : TelevisionType.values()) {
            this.view.televisionType.addItem(type.toString());
        }

        // Add customer types to combo box
        for (CustomerType type : CustomerType.values()) {
            this.view.customerType.addItem(type.toString());
        }
    }

    /**
     * Enables or disables all components within a given panel.
     *
     * @param panel     The panel whose components to enable/disable.
     * @param isEnabled true to enable, false to disable.
     */
    public static void setPanelEnabled(JPanel panel, boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();
        for (Component component : components) {
            component.setEnabled(isEnabled);
        }
    }

    /**
     * Refreshes all controllers' tables.
     */
    private void refreshAll() {
        televisionController.refreshTable();
        customerController.refreshTable();
        salesController.refreshTable();
        supplierController.refreshTable();
        stockController.refreshTable();
    }

    /**
     * Sets up the action listeners for various buttons in the view.
     */
    private void setupButtons() {
        view.itemPreferences.addActionListener(e -> {
            new PreferencesDialog(view);
        });

        view.itemDisconnect.addActionListener(e -> {
            try {
                if (!isConnected) {
                    model.connect();
                    refreshAll();
                    setPanelEnabled(view.televisionsPanel, true);
                    setPanelEnabled(view.customersPanel, true);
                    setPanelEnabled(view.salesPanel, true);
                    setPanelEnabled(view.suppliersPanel, true);
                    setPanelEnabled(view.stockPanel, true);
                    view.itemDisconnect.setText("Desconectar");
                    isConnected = true;
                } else {
                    setPanelEnabled(view.televisionsPanel, false);
                    setPanelEnabled(view.customersPanel, false);
                    setPanelEnabled(view.salesPanel, false);
                    setPanelEnabled(view.suppliersPanel, false);
                    setPanelEnabled(view.stockPanel, false);
                    view.itemDisconnect.setText("Conectar");
                    isConnected = false;
                    model.disconnect();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}