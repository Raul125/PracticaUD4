package com.raulrh.tiendatelevisiones.gui.controllers;

import com.raulrh.tiendatelevisiones.base.Controller;
import com.raulrh.tiendatelevisiones.entities.Customer;
import com.raulrh.tiendatelevisiones.gui.models.CustomerTableModel;
import com.raulrh.tiendatelevisiones.util.Preferences;
import com.raulrh.tiendatelevisiones.util.Util;
import org.bson.types.ObjectId;

import javax.swing.*;

/**
 * Controller class for managing customer-related actions such as adding, modifying, deleting,
 * and interacting with the customer table. It handles the business logic for customer operations
 * and provides methods for validating fields and updating the UI components accordingly.
 */
public class CustomerController extends Controller {
    private CustomerTableModel customerTableModel;

    /**
     * Constructs a CustomerController with the given MainController.
     *
     * @param mainController The main controller that controls the overall application flow.
     */
    public CustomerController(MainController mainController) {
        super(mainController);
    }

    /**
     * Sets up action listeners for buttons related to customer management:
     * adding, modifying, and deleting customers. Each action triggers the corresponding
     * logic for customer handling and validation.
     */
    @Override
    public void setupButtons() {
        mainController.view.customersAdd.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, rellene todos los campos.");
                return;
            }

            String mail = mainController.view.customerMail.getText();
            if (mainController.model.checkCustomerExists(mail, new ObjectId())) {
                Util.showWarningDialog("El cliente ya existe.");
                return;
            }

            mainController.model.addCustomer(
                    mainController.view.customerName.getText(),
                    mainController.view.customerSurname.getText(),
                    mail,
                    mainController.view.customerPhone.getText(),
                    mainController.view.customerDate.getDate(),
                    (short) mainController.view.customerType.getSelectedIndex());

            refreshTable();
            clearFields();
        });

        mainController.view.customersModify.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, revisa los campos.");
                return;
            }

            int row = mainController.view.customersTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona un cliente.");
                return;
            }

            ObjectId id = (ObjectId) mainController.view.customersTable.getValueAt(row, 0);
            String mail = mainController.view.customerMail.getText();
            if (mainController.model.checkCustomerExists(mail, id)) {
                Util.showWarningDialog("El cliente ya existe.");
                return;
            }

            mainController.model.modifyCustomer(
                    id,
                    mainController.view.customerName.getText(),
                    mainController.view.customerSurname.getText(),
                    mail,
                    mainController.view.customerPhone.getText(),
                    mainController.view.customerDate.getDate(),
                    mainController.view.customerType.getSelectedIndex());

            refreshTable();
            clearFields();
        });

        mainController.view.customersDelete.addActionListener(e -> {
            int row = mainController.view.customersTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona un cliente.");
                return;
            }

            Preferences preferences = Preferences.getInstance();
            if (preferences.isConfirmDelete()) {
                int confirm = Util.showConfirm("¿Estás seguro de que quieres eliminar el cliente?", "Eliminar cliente");
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
            }

            ObjectId id = (ObjectId) mainController.view.customersTable.getValueAt(row, 0);
            mainController.model.deleteCustomer(id);

            refreshTable();
            clearFields();
            mainController.salesController.refreshTable();
        });
    }

    /**
     * Sets up the customer table by enabling cell selection and disabling editing.
     * Also sets up a selection listener to fill fields with the selected customer data.
     */
    @Override
    public void setupTable() {
        mainController.view.customersTable.setCellSelectionEnabled(true);
        mainController.view.customersTable.setDefaultEditor(Object.class, null);
        ListSelectionModel cellSelectionModel = mainController.view.customersTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            if (!cellSelectionModel.isSelectionEmpty()) {
                int row = mainController.view.customersTable.getSelectedRow();
                fillFields(row);
            } else {
                clearFields();
            }
        });
    }

    /**
     * Refreshes the customer table and updates the combo box with customer data.
     * Catches and handles any SQL exceptions that may occur.
     */
    @Override
    public void refreshTable() {
        try {
            customerTableModel = new CustomerTableModel(mainController.model.getCustomers());
            mainController.view.customersTable.setModel(customerTableModel);
            refreshComboBox();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes the combo box with the updated customer list.
     * The combo box will display customer name and surname.
     */
    public void refreshComboBox() {
        mainController.view.customersComboBox.removeAllItems();
        for (Customer customer : customerTableModel.getCustomers()) {
            mainController.view.customersComboBox.addItem(customer);
        }
    }

    /**
     * Clears all input fields on the customer form.
     */
    @Override
    public void clearFields() {
        mainController.view.customerName.setText("");
        mainController.view.customerSurname.setText("");
        mainController.view.customerMail.setText("");
        mainController.view.customerPhone.setText("");
        mainController.view.customerDate.setDate(null);
        mainController.view.customerType.setSelectedIndex(-1);
    }

    /**
     * Fills the customer form with data from the selected customer row.
     *
     * @param row The row number from the customer table to populate the fields.
     */
    @Override
    public void fillFields(int row) {
        Customer customer = customerTableModel.getCustomer(row);
        mainController.view.customerName.setText(customer.getFirstName());
        mainController.view.customerSurname.setText(customer.getLastName());
        mainController.view.customerMail.setText(customer.getEmail());
        mainController.view.customerPhone.setText(customer.getPhone());
        mainController.view.customerDate.setDate(customer.getRegistrationDate());
        mainController.view.customerType.setSelectedIndex(customer.getType());
    }

    /**
     * Validates the fields on the customer form to ensure that they are not empty
     * and that the email matches the expected pattern.
     *
     * @return True if all fields are valid, otherwise false.
     */
    @Override
    public boolean validateFields() {
        return !mainController.view.customerName.getText().isEmpty() &&
                !mainController.view.customerSurname.getText().isEmpty() &&
                mainController.view.customerMail.getText().matches(MainController.EMAIL_PATTERN) &&
                !mainController.view.customerPhone.getText().isEmpty() &&
                mainController.view.customerDate.getDate() != null &&
                mainController.view.customerType.getSelectedIndex() != -1;
    }
}