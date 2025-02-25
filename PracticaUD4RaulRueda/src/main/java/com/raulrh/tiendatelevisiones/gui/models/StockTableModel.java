package com.raulrh.tiendatelevisiones.gui.models;

import com.raulrh.tiendatelevisiones.entities.Stock;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StockTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Televisión", "Proveedor", "Cantidad", "Fecha de Entrada"};
    private final List<Stock> stockList;

    public StockTableModel(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public int getRowCount() {
        return stockList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Stock getStock(int rowIndex) {
        return stockList.get(rowIndex);
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Stock stock = stockList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> stock.getId();
            case 1 -> stock.getTelevisionId();
            case 2 -> stock.getSupplierId();
            case 3 -> stock.getQuantity();
            case 4 -> stock.getEntryDate();
            default -> null;
        };
    }
}