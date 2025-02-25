package com.raulrh.tiendatelevisiones.gui.controllers;

import com.raulrh.tiendatelevisiones.base.Controller;
import com.raulrh.tiendatelevisiones.entities.Customer;
import com.raulrh.tiendatelevisiones.entities.Sale;
import com.raulrh.tiendatelevisiones.entities.Television;
import com.raulrh.tiendatelevisiones.gui.models.SaleTableModel;
import com.raulrh.tiendatelevisiones.util.Preferences;
import com.raulrh.tiendatelevisiones.util.Util;

import javax.swing.*;
import java.time.LocalDate;

/**
 * The SalesController class handles the user interactions with the sales interface.
 * It includes methods for adding, modifying, and deleting sales, as well as refreshing the sales table and validating input fields.
 */
public class SalesController extends Controller {
    private SaleTableModel saleTableModel;

    /**
     * Constructs a SalesController with a reference to the MainController.
     *
     * @param mainController The main controller that this controller belongs to.
     */
    public SalesController(MainController mainController) {
        super(mainController);
    }

    /**
     * Sets up the buttons' action listeners for the sales interface.
     * - Adds a sale when the 'Add' button is pressed.
     * - Modifies a sale when the 'Modify' button is pressed.
     * - Deletes a sale when the 'Delete' button is pressed.
     */
    @Override
    public void setupButtons() {
        mainController.view.salesAdd.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Todos los campos son obligatorios");
                return;
            }

            mainController.model.addSale(
                    ((Customer) mainController.view.customersComboBox.getSelectedItem()).getId(),
                    ((Television) mainController.view.televisionsComboBox.getSelectedItem()).getId(),
                    LocalDate.parse(mainController.view.saleDate.getDate().toString()),
                    (int) mainController.view.saleTotal.getValue(),
                    (double) mainController.view.saleTotalPrice.getValue()
            );

            refreshTable();
            clearFields();
        });

        mainController.view.salesModify.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, revisa los campos.");
                return;
            }

            int row = mainController.view.salesTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona una venta.");
                return;
            }

            Customer customer = (Customer) mainController.view.customersComboBox.getSelectedItem();
            Television television = (Television) mainController.view.televisionsComboBox.getSelectedItem();
            Sale sale = saleTableModel.getSale(row);
            mainController.model.modifySale(
                    sale.getId(),
                    customer.getId(),
                    television.getId(),
                    LocalDate.parse(mainController.view.saleDate.getDate().toString()),
                    (int) mainController.view.saleTotal.getValue(),
                    (double) mainController.view.saleTotalPrice.getValue()
            );

            refreshTable();
            clearFields();
        });

        mainController.view.salesDelete.addActionListener(e -> {
            int row = mainController.view.salesTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona una venta.");
                return;
            }

            Preferences preferences = Preferences.getInstance();
            if (preferences.isConfirmDelete()) {
                int confirm = Util.showConfirm("¿Estás seguro de que quieres eliminar la venta?", "Eliminar venta");
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
            }

            Sale sale = saleTableModel.getSale(row);
            mainController.model.deleteSale(sale.getId());

            refreshTable();
            clearFields();
        });
    }

    /**
     * Sets up the sales table with a selection model and listener.
     */
    @Override
    public void setupTable() {
        mainController.view.salesTable.setCellSelectionEnabled(true);
        mainController.view.salesTable.setDefaultEditor(Object.class, null);
        ListSelectionModel cellSelectionModel = mainController.view.salesTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            if (!cellSelectionModel.isSelectionEmpty()) {
                int row = mainController.view.salesTable.getSelectedRow();
                fillFields(row);
            } else {
                clearFields();
            }
        });
    }

    /**
     * Refreshes the sales table with updated data.
     */
    @Override
    public void refreshTable() {
        try {
            saleTableModel = new SaleTableModel(mainController.model.getSales());
            mainController.view.salesTable.setModel(saleTableModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears all input fields in the sales interface.
     */
    @Override
    public void clearFields() {
        mainController.view.customersComboBox.setSelectedIndex(-1);
        mainController.view.televisionsComboBox.setSelectedIndex(-1);
        mainController.view.saleDate.setDate(null);
        mainController.view.saleTotal.setValue(0);
        mainController.view.saleTotalPrice.setValue(0);
    }

    /**
     * Fills the input fields in the sales interface with data from the selected row in the table.
     *
     * @param row The row index in the table to retrieve data from.
     */
    @Override
    public void fillFields(int row) {
        Sale sale = saleTableModel.getSale(row);
        Util.setSelectedCustomer(mainController.view.customersComboBox, sale.getCustomerId());
        Util.setSelectedTelevision(mainController.view.televisionsComboBox, sale.getTelevisionId());
        mainController.view.saleDate.setDate(sale.getSaleDate());
        mainController.view.saleTotal.setValue(sale.getQuantity());
        mainController.view.saleTotalPrice.setValue(sale.getTotal());
    }

    /**
     * Validates the input fields in the sales interface.
     *
     * @return true if all required fields are filled correctly; false otherwise.
     */
    @Override
    public boolean validateFields() {
        return mainController.view.customersComboBox.getSelectedIndex() > -1 &&
                mainController.view.televisionsComboBox.getSelectedIndex() > -1 &&
                mainController.view.saleDate.getDate() != null &&
                (int) mainController.view.saleTotal.getValue() > 0 &&
                (double) mainController.view.saleTotalPrice.getValue() > 0;
    }
}