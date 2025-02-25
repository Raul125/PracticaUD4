package com.raulrh.tiendatelevisiones.base;

import com.raulrh.tiendatelevisiones.base.interfaces.IController;
import com.raulrh.tiendatelevisiones.gui.controllers.MainController;

/**
 * Base controller class that implements the IController interface.
 * This class provides a foundation for handling button setup, table setup,
 * table refreshing, and form field management in derived controller classes.
 * It is expected that subclasses will implement the abstract methods.
 */
public class Controller implements IController {
    public final MainController mainController;

    /**
     * Constructs a Controller with the given MainController.
     * It initializes the mainController and sets up buttons and table.
     *
     * @param mainController The main controller that controls the overall application flow.
     */
    public Controller(MainController mainController) {
        this.mainController = mainController;

        setupButtons();
        setupTable();
    }

    /**
     * Sets up the buttons for the controller. This method is intended to be overridden
     * by subclasses to define button behavior.
     *
     * @throws UnsupportedOperationException if not overridden in a subclass.
     */
    @Override
    public void setupButtons() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Sets up the table for the controller. This method is intended to be overridden
     * by subclasses to define table behavior.
     *
     * @throws UnsupportedOperationException if not overridden in a subclass.
     */
    @Override
    public void setupTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Refreshes the table data. This method is intended to be overridden
     * by subclasses to implement the specific logic for refreshing the table.
     *
     * @throws UnsupportedOperationException if not overridden in a subclass.
     */
    @Override
    public void refreshTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Clears the form fields. This method is intended to be overridden
     * by subclasses to define how the fields should be cleared.
     *
     * @throws UnsupportedOperationException if not overridden in a subclass.
     */
    @Override
    public void clearFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Fills the form fields with data from the specified row.
     * This method is intended to be overridden by subclasses to define
     * how to populate the fields based on the row.
     *
     * @param row The row number from the table that contains the data to populate.
     * @throws UnsupportedOperationException if not overridden in a subclass.
     */
    @Override
    public void fillFields(int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Validates the form fields to ensure that the data entered is correct.
     * This method is intended to be overridden by subclasses to implement
     * the field validation logic.
     *
     * @return True if the fields are valid, otherwise false.
     * @throws UnsupportedOperationException if not overridden in a subclass.
     */
    @Override
    public boolean validateFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}