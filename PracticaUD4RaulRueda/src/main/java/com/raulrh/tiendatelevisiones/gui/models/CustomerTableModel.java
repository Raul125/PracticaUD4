package com.raulrh.tiendatelevisiones.gui.models;

import com.raulrh.tiendatelevisiones.base.enums.CustomerType;
import com.raulrh.tiendatelevisiones.entities.Customer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CustomerTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre", "Apellido", "Email", "Teléfono", "Fecha de Registro", "Tipo"};
    private final List<Customer> customers;

    public CustomerTableModel(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Customer getCustomer(int rowIndex) {
        return customers.get(rowIndex);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> customer.getId();
            case 1 -> customer.getFirstName();
            case 2 -> customer.getLastName();
            case 3 -> customer.getEmail();
            case 4 -> customer.getPhone();
            case 5 -> customer.getRegistrationDate();
            case 6 -> CustomerType.fromCode(customer.getType());
            default -> null;
        };
    }
}