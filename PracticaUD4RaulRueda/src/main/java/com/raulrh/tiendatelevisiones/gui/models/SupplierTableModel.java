package com.raulrh.tiendatelevisiones.gui.models;

import com.raulrh.tiendatelevisiones.entities.Supplier;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SupplierTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre", "Teléfono", "Dirección", "Correo Electrónico"};
    private final List<Supplier> suppliers;

    public SupplierTableModel(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public int getRowCount() {
        return suppliers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Supplier getSupplier(int rowIndex) {
        return suppliers.get(rowIndex);
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supplier supplier = suppliers.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> supplier.getId();
            case 1 -> supplier.getName();
            case 2 -> supplier.getPhone();
            case 3 -> supplier.getAddress();
            case 4 -> supplier.getEmail();
            default -> null;
        };
    }
}