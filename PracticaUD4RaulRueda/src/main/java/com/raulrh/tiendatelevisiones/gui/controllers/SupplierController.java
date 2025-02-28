package com.raulrh.tiendatelevisiones.gui.controllers;

import com.raulrh.tiendatelevisiones.base.Controller;
import com.raulrh.tiendatelevisiones.entities.Supplier;
import com.raulrh.tiendatelevisiones.entities.Television;
import com.raulrh.tiendatelevisiones.gui.dialogs.StockDialog;
import com.raulrh.tiendatelevisiones.gui.models.SupplierTableModel;
import com.raulrh.tiendatelevisiones.gui.models.TelevisionTableModel;
import com.raulrh.tiendatelevisiones.util.Preferences;
import com.raulrh.tiendatelevisiones.util.Util;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;

/**
 * Controller responsible for managing supplier-related operations in the application.
 * It handles the addition, modification, deletion, and display of suppliers in the user interface.
 */
public class SupplierController extends Controller {
    private SupplierTableModel supplierTableModel;

    /**
     * Constructor to initialize the SupplierController.
     *
     * @param mainController the main controller that provides access to shared resources and components.
     */
    public SupplierController(MainController mainController) {
        super(mainController);
    }

    /**
     * Configures the action listeners for buttons related to supplier management.
     * This includes adding, modifying, and deleting suppliers.
     */
    @Override
    public void setupButtons() {
        mainController.view.suppliersAdd.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Todos los campos son obligatorios");
                return;
            }

            String mail = mainController.view.supplierMail.getText();
            if (mainController.model.checkSupplierExists(mail, new ObjectId())) {
                Util.showWarningDialog("El proveedor ya existe.");
                return;
            }

            mainController.model.addSupplier(
                    mainController.view.supplierName.getText(),
                    mainController.view.supplierPhone.getText(),
                    mainController.view.supplierAddress.getText(),
                    mail
            );

            refreshTable();
            clearFields();
        });

        mainController.view.suppliersModify.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, revisa los campos.");
                return;
            }

            int row = mainController.view.suppliersTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona un proveedor.");
                return;
            }

            ObjectId id = (ObjectId) mainController.view.suppliersTable.getValueAt(row, 0);
            String mail = mainController.view.supplierMail.getText();
            if (mainController.model.checkSupplierExists(mail, id)) {
                Util.showWarningDialog("El proveedor ya existe.");
                return;
            }

            mainController.model.modifySupplier(
                    id,
                    mainController.view.supplierName.getText(),
                    mainController.view.supplierPhone.getText(),
                    mainController.view.supplierAddress.getText(),
                    mail
            );

            refreshTable();
            clearFields();
        });

        mainController.view.suppliersDelete.addActionListener(e -> {
            int row = mainController.view.suppliersTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona un proveedor.");
                return;
            }

            Preferences preferences = Preferences.getInstance();
            if (preferences.isConfirmDelete()) {
                int confirm = Util.showConfirm("¿Estás seguro de que quieres eliminar el proveedor?", "Eliminar proveedor");
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
            }

            ObjectId id = (ObjectId) mainController.view.suppliersTable.getValueAt(row, 0);
            mainController.model.deleteSupplier(id);

            refreshTable();
            clearFields();

            mainController.stockController.refreshTable();
        });

        mainController.view.searchSupplier.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTelevisions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTelevisions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTelevisions();
            }

            private void filterTelevisions() {
                String searchText = mainController.view.searchSupplier.getText().trim().toLowerCase();
                List<Supplier> allSuppliers = mainController.model.getSuppliers();
                List<Supplier> filteredSuppliers;

                if (searchText.isEmpty()) {
                    filteredSuppliers = allSuppliers;
                } else {
                    filteredSuppliers = allSuppliers.stream()
                            .filter(supplier -> supplier.getName().toLowerCase().contains(searchText)).toList();
                }

                supplierTableModel = new SupplierTableModel(filteredSuppliers);
                mainController.view.suppliersTable.setModel(supplierTableModel);
            }
        });
    }

    /**
     * Configures the table used to display supplier information.
     * This includes enabling cell selection and handling row selection to populate form fields.
     */
    @Override
    public void setupTable() {
        mainController.view.suppliersTable.setCellSelectionEnabled(true);
        mainController.view.suppliersTable.setDefaultEditor(Object.class, null);
        ListSelectionModel cellSelectionModel = mainController.view.suppliersTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            if (!cellSelectionModel.isSelectionEmpty()) {
                int row = mainController.view.suppliersTable.getSelectedRow();
                fillFields(row);
            } else {
                clearFields();
            }
        });

        mainController.view.suppliersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!mainController.isConnected) {
                    return;
                }

                if (evt.getClickCount() == 2) {
                    JTable table = (JTable) evt.getSource();
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Supplier supplier = supplierTableModel.getSupplier(row);
                        StockDialog stockDialog = new StockDialog(mainController.view, supplier, mainController.model);
                        stockDialog.setVisible(true);
                    }
                }
            }
        });
    }

    /**
     * Refreshes the supplier table by fetching updated data from the model
     * and updating the table model and associated combo boxes.
     */
    @Override
    public void refreshTable() {
        try {
            supplierTableModel = new SupplierTableModel(mainController.model.getSuppliers());
            mainController.view.suppliersTable.setModel(supplierTableModel);
            refreshComboBox();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes the combo box that lists suppliers based on the current table data.
     */
    public void refreshComboBox() {
        mainController.view.suppliersComboBox.removeAllItems();
        for (Supplier supplier : supplierTableModel.getSuppliers()) {
            mainController.view.suppliersComboBox.addItem(supplier);
        }
    }

    /**
     * Clears the form fields related to supplier data.
     */
    @Override
    public void clearFields() {
        mainController.view.supplierName.setText("");
        mainController.view.supplierPhone.setText("");
        mainController.view.supplierAddress.setText("");
        mainController.view.supplierMail.setText("");
    }

    /**
     * Populates the form fields with supplier data from the selected table row.
     *
     * @param row the selected row index in the suppliers table.
     */
    @Override
    public void fillFields(int row) {
        Supplier supplier = supplierTableModel.getSupplier(row);
        mainController.view.supplierName.setText(supplier.getName());
        mainController.view.supplierPhone.setText(supplier.getPhone());
        mainController.view.supplierAddress.setText(supplier.getAddress());
        mainController.view.supplierMail.setText(supplier.getEmail());
    }

    /**
     * Validates the form fields to ensure all required data is provided and in the correct format.
     *
     * @return true if all fields are valid; false otherwise.
     */
    @Override
    public boolean validateFields() {
        return mainController.view.supplierName.getText() != null && !mainController.view.supplierName.getText().isEmpty() &&
                mainController.view.supplierPhone.getText() != null && !mainController.view.supplierPhone.getText().isEmpty() &&
                mainController.view.supplierAddress.getText() != null && !mainController.view.supplierAddress.getText().isEmpty() &&
                mainController.view.supplierMail.getText() != null && mainController.view.supplierMail.getText().matches(MainController.EMAIL_PATTERN);
    }
}