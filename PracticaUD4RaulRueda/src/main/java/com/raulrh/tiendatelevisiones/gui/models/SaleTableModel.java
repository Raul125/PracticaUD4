package com.raulrh.tiendatelevisiones.gui.models;

import com.raulrh.tiendatelevisiones.entities.Sale;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SaleTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Cliente", "Televisión", "Fecha de Venta", "Cantidad", "Total"};
    private final List<Sale> sales;

    public SaleTableModel(List<Sale> sales) {
        this.sales = sales;
    }

    @Override
    public int getRowCount() {
        return sales.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Sale getSale(int rowIndex) {
        return sales.get(rowIndex);
    }

    public List<Sale> getSales() {
        return sales;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Sale sale = sales.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> sale.getId();
            case 1 -> sale.getCustomerId();
            case 2 -> sale.getTelevisionId();
            case 3 -> sale.getSaleDate();
            case 4 -> sale.getQuantity();
            case 5 -> sale.getTotal();
            default -> null;
        };
    }
}