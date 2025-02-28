package com.raulrh.tiendatelevisiones.gui.controllers;

import com.raulrh.tiendatelevisiones.base.Controller;
import com.raulrh.tiendatelevisiones.entities.Stock;
import com.raulrh.tiendatelevisiones.entities.Supplier;
import com.raulrh.tiendatelevisiones.entities.Television;
import com.raulrh.tiendatelevisiones.gui.models.StockTableModel;
import com.raulrh.tiendatelevisiones.gui.models.SupplierTableModel;
import com.raulrh.tiendatelevisiones.util.Preferences;
import com.raulrh.tiendatelevisiones.util.Util;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller class responsible for managing the stock-related actions and interactions in the GUI.
 */
public class StockController extends Controller {
    private StockTableModel stockTableModel;

    /**
     * Constructs a StockController instance and initializes it with the main controller.
     *
     * @param mainController The main controller of the application.
     */
    public StockController(MainController mainController) {
        super(mainController);
    }

    /**
     * Sets up the event listeners for buttons in the stock management interface.
     */
    @Override
    public void setupButtons() {
        mainController.view.stockAdd.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Todos los campos son obligatorios");
                return;
            }

            mainController.model.addStock(
                    ((Television) mainController.view.televisionsComboBox1.getSelectedItem()).getId(),
                    ((Supplier) mainController.view.suppliersComboBox.getSelectedItem()).getId(),
                    LocalDate.parse(mainController.view.stockDate.getDate().toString()),
                    (int) mainController.view.stockTotal.getValue()
            );

            refreshTable();
            clearFields();
        });

        mainController.view.stockModify.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, revisa los campos.");
                return;
            }

            int row = mainController.view.stockTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona un inventario.");
                return;
            }

            mainController.model.modifyStock(
                    (ObjectId) mainController.view.stockTable.getValueAt(row, 0),
                    ((Television) mainController.view.televisionsComboBox1.getSelectedItem()).getId(),
                    ((Supplier) mainController.view.suppliersComboBox.getSelectedItem()).getId(),
                    LocalDate.parse(mainController.view.stockDate.getDate().toString()),
                    (int) mainController.view.stockTotal.getValue()
            );

            refreshTable();
            clearFields();
        });

        mainController.view.stockDelete.addActionListener(e -> {
            int row = mainController.view.stockTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona un inventario.");
                return;
            }

            Preferences preferences = Preferences.getInstance();
            if (preferences.isConfirmDelete()) {
                int confirm = Util.showConfirm("¿Estás seguro de que quieres eliminar el inventario?", "Eliminar inventario");
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
            }

            ObjectId id = (ObjectId) mainController.view.stockTable.getValueAt(row, 0);
            mainController.model.deleteStock(id);

            refreshTable();
            clearFields();
        });
    }

    /**
     * Configures the stock table for interaction and adds a listener for row selection.
     */
    @Override
    public void setupTable() {
        mainController.view.stockTable.setCellSelectionEnabled(true);
        mainController.view.stockTable.setDefaultEditor(Object.class, null);
        ListSelectionModel cellSelectionModel = mainController.view.stockTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            if (!cellSelectionModel.isSelectionEmpty()) {
                int row = mainController.view.stockTable.getSelectedRow();
                fillFields(row);
            } else {
                clearFields();
            }
        });
    }

    /**
     * Updates the stock table with the latest data from the model.
     */
    @Override
    public void refreshTable() {
        try {
            stockTableModel = new StockTableModel(mainController.model.getStock());
            mainController.view.stockTable.setModel(stockTableModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears all input fields in the stock management interface.
     */
    @Override
    public void clearFields() {
        mainController.view.televisionsComboBox1.setSelectedIndex(-1);
        mainController.view.suppliersComboBox.setSelectedIndex(-1);
        mainController.view.stockDate.setDate(null);
        mainController.view.stockTotal.setValue(0);
    }

    /**
     * Fills the input fields with data from a selected row in the stock table.
     *
     * @param row The index of the selected row in the stock table.
     */
    @Override
    public void fillFields(int row) {
        Stock stock = stockTableModel.getStock(row);
        Util.setSelectedTelevision(mainController.view.televisionsComboBox1, stock.getTelevisionId());
        Util.setSelectedSupplier(mainController.view.suppliersComboBox, stock.getSupplierId());
        mainController.view.stockTotal.setValue(stock.getQuantity());
        mainController.view.stockDate.setDate(stock.getEntryDate());
    }

    /**
     * Validates that all required fields have been filled out correctly.
     *
     * @return true if all fields are valid, false otherwise.
     */
    @Override
    public boolean validateFields() {
        return mainController.view.televisionsComboBox1.getSelectedIndex() > -1 &&
                mainController.view.suppliersComboBox.getSelectedIndex() > -1 &&
                mainController.view.stockDate.getDate() != null &&
                (int) mainController.view.stockTotal.getValue() > 0;
    }
}