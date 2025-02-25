package com.raulrh.tiendatelevisiones.base.interfaces;

/**
 * Interface defining the basic methods required for a controller in the application.
 * Any class implementing this interface must provide implementations for setting up buttons,
 * setting up tables, refreshing data, clearing fields, filling fields, and validating fields.
 */
public interface IController {

    /**
     * Sets up the button actions in the controller.
     * This method is called to initialize the action listeners for the buttons.
     */
    void setupButtons();

    /**
     * Sets up the table in the controller.
     * This method is called to initialize the table and its settings.
     */
    void setupTable();

    /**
     * Refreshes the table data.
     * This method is called to reload the data into the table, ensuring the latest information is displayed.
     */
    void refreshTable();

    /**
     * Clears the input fields in the controller.
     * This method is used to reset or clear all fields, typically after performing an action.
     */
    void clearFields();

    /**
     * Fills the input fields with data from the specified row.
     * This method populates the fields with the data corresponding to the selected row in the table.
     *
     * @param row The index of the row from which the data will be fetched.
     */
    void fillFields(int row);

    /**
     * Validates the input fields.
     * This method checks if all necessary fields are filled correctly before performing an action.
     *
     * @return true if all fields are valid, false otherwise.
     */
    boolean validateFields();
}