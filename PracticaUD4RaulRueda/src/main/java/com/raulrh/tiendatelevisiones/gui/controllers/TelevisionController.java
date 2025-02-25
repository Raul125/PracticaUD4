package com.raulrh.tiendatelevisiones.gui.controllers;

import com.raulrh.tiendatelevisiones.base.Controller;
import com.raulrh.tiendatelevisiones.entities.Television;
import com.raulrh.tiendatelevisiones.gui.models.TelevisionTableModel;
import com.raulrh.tiendatelevisiones.util.Preferences;
import com.raulrh.tiendatelevisiones.util.Util;
import org.bson.types.ObjectId;

import javax.swing.*;

/**
 * Controller class responsible for managing televisions within the application.
 * This class handles user interactions, updates the model, and refreshes the view accordingly.
 */
public class TelevisionController extends Controller {
    private TelevisionTableModel televisionTableModel;

    /**
     * Constructor that initializes the TelevisionController with a reference to the main controller.
     *
     * @param mainController the main controller of the application
     */
    public TelevisionController(MainController mainController) {
        super(mainController);
    }

    /**
     * Sets up action listeners for the buttons related to television management.
     */
    @Override
    public void setupButtons() {
        mainController.view.televisionsAdd.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, rellene todos los campos.");
                return;
            }

            String model = mainController.view.televisionModel.getText();
            String brand = mainController.view.televisionBrand.getText();
            if (mainController.model.checkTelevisionExists(model, brand, new ObjectId())) {
                Util.showWarningDialog("La televisión ya existe.");
                return;
            }

            mainController.model.addTelevision(
                    model,
                    brand,
                    (double) mainController.view.televisionPrice.getValue(),
                    mainController.view.televisionDate.getDate(),
                    (short) mainController.view.televisionType.getSelectedIndex(),
                    mainController.view.televisionSmartTv.isSelected());

            refreshTable();
            clearFields();
        });

        mainController.view.televisionsModify.addActionListener(e -> {
            if (!validateFields()) {
                Util.showWarningDialog("Por favor, revisa los campos.");
                return;
            }

            int row = mainController.view.televisionsTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona una televisión.");
                return;
            }

            ObjectId id = (ObjectId) mainController.view.televisionsTable.getValueAt(row, 0);
            String model = mainController.view.televisionModel.getText();
            String brand = mainController.view.televisionBrand.getText();
            if (mainController.model.checkTelevisionExists(model, brand, id)) {
                Util.showWarningDialog("La televisión ya existe.");
                return;
            }

            mainController.model.modifyTelevision(
                    id,
                    model,
                    brand,
                    (double) mainController.view.televisionPrice.getValue(),
                    mainController.view.televisionDate.getDate(),
                    mainController.view.televisionType.getSelectedIndex(),
                    mainController.view.televisionSmartTv.isSelected());

            refreshTable();
            clearFields();
        });

        mainController.view.televisionsDelete.addActionListener(e -> {
            int row = mainController.view.televisionsTable.getSelectedRow();
            if (row == -1) {
                Util.showWarningDialog("Selecciona una televisión.");
                return;
            }

            Preferences preferences = Preferences.getInstance();
            if (preferences.isConfirmDelete()) {
                int confirm = Util.showConfirm("¿Estás seguro de que quieres eliminar la televisión?", "Eliminar televisión");
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
            }

            ObjectId id = (ObjectId) mainController.view.televisionsTable.getValueAt(row, 0);
            mainController.model.deleteTelevision(id);

            refreshTable();
            clearFields();

            mainController.stockController.refreshTable();
            mainController.salesController.refreshTable();
        });
    }

    /**
     * Configures the television table for selection and sets up a listener for user interaction.
     */
    @Override
    public void setupTable() {
        mainController.view.televisionsTable.setCellSelectionEnabled(true);
        mainController.view.televisionsTable.setDefaultEditor(Object.class, null);
        ListSelectionModel cellSelectionModel = mainController.view.televisionsTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            if (!cellSelectionModel.isSelectionEmpty()) {
                int row = mainController.view.televisionsTable.getSelectedRow();
                fillFields(row);
            } else {
                clearFields();
            }
        });
    }

    /**
     * Refreshes the television table view by updating its data model.
     * This method retrieves data from the model and updates the associated components.
     */
    @Override
    public void refreshTable() {
        try {
            televisionTableModel = new TelevisionTableModel(mainController.model.getTelevisions());
            mainController.view.televisionsTable.setModel(televisionTableModel);
            refreshComboBox();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes the content of the television-related combo boxes.
     */
    public void refreshComboBox() {
        mainController.view.televisionsComboBox.removeAllItems();
        mainController.view.televisionsComboBox1.removeAllItems();
        for (Television television : televisionTableModel.getTelevisions()) {
            mainController.view.televisionsComboBox.addItem(television);
            mainController.view.televisionsComboBox1.addItem(television);
        }
    }

    /**
     * Fills the fields in the television form with data from the selected row.
     *
     * @param row the selected row in the television table
     */
    @Override
    public void fillFields(int row) {
        Television television = televisionTableModel.getTelevision(row);
        mainController.view.televisionModel.setText(television.getModel());
        mainController.view.televisionBrand.setText(television.getBrand());
        mainController.view.televisionPrice.setValue(television.getPrice());
        mainController.view.televisionDate.setDate(television.getReleaseDate());
        mainController.view.televisionType.setSelectedIndex(television.getType());
        mainController.view.televisionSmartTv.setSelected(television.getIsSmart());
    }

    /**
     * Clears all fields in the television form.
     */
    @Override
    public void clearFields() {
        mainController.view.televisionModel.setText("");
        mainController.view.televisionBrand.setText("");
        mainController.view.televisionPrice.setValue(0);
        mainController.view.televisionDate.setDate(null);
        mainController.view.televisionType.setSelectedIndex(-1);
        mainController.view.televisionSmartTv.setSelected(false);
    }

    /**
     * Validates that all necessary fields in the television form are filled and valid.
     *
     * @return true if all fields are valid; false otherwise
     */
    @Override
    public boolean validateFields() {
        return mainController.view.televisionModel.getText() != null && !mainController.view.televisionModel.getText().isEmpty()
                && mainController.view.televisionBrand.getText() != null && !mainController.view.televisionBrand.getText().isEmpty()
                && mainController.view.televisionPrice.getValue() != null && (double) mainController.view.televisionPrice.getValue() > 0
                && mainController.view.televisionDate.getDate() != null
                && mainController.view.televisionType.getSelectedIndex() != -1;
    }
}