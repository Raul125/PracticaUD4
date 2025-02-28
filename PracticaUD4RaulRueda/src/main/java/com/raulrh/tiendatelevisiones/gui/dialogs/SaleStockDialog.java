package com.raulrh.tiendatelevisiones.gui.dialogs;

import com.raulrh.tiendatelevisiones.entities.Sale;
import com.raulrh.tiendatelevisiones.entities.Stock;
import com.raulrh.tiendatelevisiones.entities.Television;
import com.raulrh.tiendatelevisiones.gui.Model;
import com.raulrh.tiendatelevisiones.gui.models.SaleTableModel;
import com.raulrh.tiendatelevisiones.gui.models.StockTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SaleStockDialog extends JDialog {
    public SaleStockDialog(JFrame parent, Television television, Model model) {
        super(parent, "Ventas e Inventario de " + television.toString(), true);

        setLayout(new GridLayout(2, 1));

        List<Sale> televisionSales = model.getSales().stream()
                .filter(sale -> sale.getTelevisionId().equals(television.getId())).toList();

        JTable ventasTable = new JTable(new SaleTableModel(televisionSales));
        JScrollPane ventasScroll = new JScrollPane(ventasTable);
        JPanel ventasPanel = new JPanel(new BorderLayout());
        ventasPanel.setBorder(BorderFactory.createTitledBorder("Ventas"));
        ventasPanel.add(ventasScroll, BorderLayout.CENTER);

        List<Stock> televisionStock = model.getStock().stream()
                .filter(stock -> stock.getTelevisionId().equals(television.getId())).toList();
        JTable inventarioTable = new JTable(new StockTableModel(televisionStock));
        JScrollPane inventarioScroll = new JScrollPane(inventarioTable);
        JPanel inventarioPanel = new JPanel(new BorderLayout());
        inventarioPanel.setBorder(BorderFactory.createTitledBorder("Inventario"));
        inventarioPanel.add(inventarioScroll, BorderLayout.CENTER);

        add(ventasPanel);
        add(inventarioPanel);

        setSize(600, 300);
        setLocationRelativeTo(parent);
    }
}