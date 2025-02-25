package com.raulrh.tiendatelevisiones.gui.models;

import com.raulrh.tiendatelevisiones.base.enums.TelevisionType;
import com.raulrh.tiendatelevisiones.entities.Television;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TelevisionTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Modelo", "Marca", "Precio", "Fecha de lanzamiento", "Tipo", "Smart TV"};
    private final List<Television> televisions;

    public TelevisionTableModel(List<Television> televisions) {
        this.televisions = televisions;
    }

    @Override
    public int getRowCount() {
        return televisions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Television getTelevision(int rowIndex) {
        return televisions.get(rowIndex);
    }

    public List<Television> getTelevisions() {
        return televisions;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Television tv = televisions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> tv.getId();
            case 1 -> tv.getModel();
            case 2 -> tv.getBrand();
            case 3 -> tv.getPrice();
            case 4 -> tv.getReleaseDate();
            case 5 -> TelevisionType.fromCode(tv.getType());
            case 6 -> tv.getIsSmart() ? "Sí" : "No";
            default -> null;
        };
    }
}
