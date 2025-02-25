package com.raulrh.tiendatelevisiones.util;

import com.raulrh.tiendatelevisiones.entities.Customer;
import com.raulrh.tiendatelevisiones.entities.Supplier;
import com.raulrh.tiendatelevisiones.entities.Television;
import org.bson.types.ObjectId;

import javax.swing.*;

/**
 * The Util class provides utility methods for displaying warning and confirmation dialogs.
 * It contains methods to show a warning dialog and a confirmation dialog with customizable messages and titles.
 */
public class Util {
    /**
     * Displays a warning dialog with the given message.
     *
     * @param message the message to display in the warning dialog
     */
    public static void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Displays a confirmation dialog with the given message and title.
     * It returns the user's choice (either "Si" or "No").
     *
     * @param message the message to display in the confirmation dialog
     * @param title   the title of the confirmation dialog
     * @return the user's choice: 0 for "Si", 1 for "No"
     */
    public static int showConfirm(String message, String title) {
        Object[] options = {"Si", "No"};
        return JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);
    }

    /**
     * Selects a television from the JComboBox that matches the given ID.
     *
     * @param comboBox the JComboBox containing Television objects
     * @param id       the ID of the Television to be selected
     */
    public static void setSelectedTelevision(JComboBox<Television> comboBox, ObjectId id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Television television = comboBox.getItemAt(i);
            if (television.getId().equals(id)) {
                comboBox.setSelectedItem(television);
                break;
            }
        }
    }

    /**
     * Selects a customer from the JComboBox that matches the given ID.
     *
     * @param comboBox the JComboBox containing Customer objects
     * @param id       the ID of the Customer to be selected
     */
    public static void setSelectedCustomer(JComboBox<Customer> comboBox, ObjectId id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Customer customer = comboBox.getItemAt(i);
            if (customer.getId().equals(id)) {
                comboBox.setSelectedItem(customer);
                break;
            }
        }
    }

    /**
     * Selects a supplier from the JComboBox that matches the given ID.
     *
     * @param comboBox the JComboBox containing Supplier objects
     * @param id       the ID of the Supplier to be selected
     */
    public static void setSelectedSupplier(JComboBox<Supplier> comboBox, ObjectId id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Supplier supplier = comboBox.getItemAt(i);
            if (supplier.getId().equals(id)) {
                comboBox.setSelectedItem(supplier);
                break;
            }
        }
    }
}