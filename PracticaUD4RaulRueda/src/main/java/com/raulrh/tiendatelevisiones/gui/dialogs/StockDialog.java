package com.raulrh.tiendatelevisiones.gui.dialogs;

import com.raulrh.tiendatelevisiones.entities.Stock;
import com.raulrh.tiendatelevisiones.entities.Supplier;
import com.raulrh.tiendatelevisiones.gui.Model;
import com.raulrh.tiendatelevisiones.gui.models.StockTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StockDialog extends JDialog {
    public StockDialog(JFrame parent, Supplier supplier, Model model) {
        super(parent, "Stock de " + supplier.toString(), true);

        setLayout(new GridLayout(1, 1));

        List<Stock> supplierStock = model.getStock().stream()
                .filter(stock -> stock.getSupplierId().equals(supplier.getId())).toList();

        JTable stockTable = new JTable(new StockTableModel(supplierStock));
        JScrollPane stockScroll = new JScrollPane(stockTable);
        JPanel stockPanel = new JPanel(new BorderLayout());
        stockPanel.setBorder(BorderFactory.createTitledBorder("Stock"));
        stockPanel.add(stockScroll, BorderLayout.CENTER);

        add(stockPanel);

        setSize(600, 300);
        setLocationRelativeTo(parent);
    }
}