package com.raulrh.tiendatelevisiones.gui.dialogs;

import com.raulrh.tiendatelevisiones.entities.Customer;
import com.raulrh.tiendatelevisiones.entities.Sale;
import com.raulrh.tiendatelevisiones.gui.Model;
import com.raulrh.tiendatelevisiones.gui.models.SaleTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SaleDialog extends JDialog {
    public SaleDialog(JFrame parent, Customer customer, Model model) {
        super(parent, "Ventas de " + customer.toString(), true);

        setLayout(new GridLayout(1, 1));

        List<Sale> customerSales = model.getSales().stream()
                .filter(sale -> sale.getCustomerId().equals(customer.getId())).toList();

        JTable ventasTable = new JTable(new SaleTableModel(customerSales));
        JScrollPane ventasScroll = new JScrollPane(ventasTable);
        JPanel ventasPanel = new JPanel(new BorderLayout());
        ventasPanel.setBorder(BorderFactory.createTitledBorder("Ventas"));
        ventasPanel.add(ventasScroll, BorderLayout.CENTER);

        add(ventasPanel);

        setSize(600, 300);
        setLocationRelativeTo(parent);
    }
}
