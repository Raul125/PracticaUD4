package com.raulrh.tiendatelevisiones.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.raulrh.tiendatelevisiones.base.LimitedDocument;
import com.raulrh.tiendatelevisiones.entities.Customer;
import com.raulrh.tiendatelevisiones.entities.Supplier;
import com.raulrh.tiendatelevisiones.entities.Television;
import com.raulrh.tiendatelevisiones.gui.controllers.MainController;
import com.raulrh.tiendatelevisiones.util.Preferences;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.Objects;

/**
 * The {@code View} class represents the main graphical user interface (GUI) for the TV Store application.
 * It provides components and layouts for managing televisions, customers, sales, suppliers, and stock.
 */
public class View extends JFrame {

    private static final String TITLE = "Tienda TV";

    // Television-related components
    public JTable televisionsTable;
    public JButton televisionsAdd;
    public JButton televisionsModify;
    public JButton televisionsDelete;
    public JTextField televisionModel;
    public JTextField televisionBrand;
    public JSpinner televisionPrice;
    public DatePicker televisionDate;
    public JComboBox<String> televisionType;
    public JCheckBox televisionSmartTv;

    // Customer-related components
    public JTable customersTable;
    public JButton customersAdd;
    public JButton customersModify;
    public JButton customersDelete;
    public JTextField customerName;
    public JTextField customerSurname;
    public JTextField customerMail;
    public JTextField customerPhone;
    public DatePicker customerDate;
    public JComboBox<String> customerType;

    // Sales-related components
    public JTable salesTable;
    public JButton salesAdd;
    public JButton salesModify;
    public JButton salesDelete;
    public JComboBox<Customer> customersComboBox;
    public JComboBox<Television> televisionsComboBox;
    public DatePicker saleDate;
    public JSpinner saleTotal;
    public JSpinner saleTotalPrice;

    // Supplier-related components
    public JTable suppliersTable;
    public JButton suppliersAdd;
    public JButton suppliersModify;
    public JButton suppliersDelete;
    public JTextField supplierName;
    public JTextField supplierPhone;
    public JTextField supplierAddress;
    public JTextField supplierMail;

    // Stock-related components
    public JTable stockTable;
    public JButton stockAdd;
    public JButton stockModify;
    public JButton stockDelete;
    public JSpinner stockTotal;
    public DatePicker stockDate;
    public JComboBox<Television> televisionsComboBox1;
    public JComboBox<Supplier> suppliersComboBox;

    // Menu actions
    public JMenuItem itemPreferences;
    public JMenuItem itemDisconnect;

    // Admin dialog components
    public JPanel televisionsPanel;
    public JPanel customersPanel;
    public JPanel salesPanel;
    public JPanel suppliersPanel;
    public JPanel stockPanel;

    // Panels
    private JPanel mainPanel;

    // Search
    public JTextField searchTv;
    public JTextField searchClient;
    public JTextField searchSupplier;

    /**
     * Constructs the main window of the application.
     */
    public View() {
        super(TITLE);
        initFrame();
    }

    /**
     * Initializes the main frame and its components.
     */
    public void initFrame() {
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/television.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setContentPane(mainPanel);

        this.pack();
        this.setLocationRelativeTo(null);

        setupMenu();
        setupFields();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Preferences.savePreferences();
                System.exit(0);
            }
        });

        this.setVisible(true);
    }

    /**
     * Configures the menu bar with its actions and components.
     */
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Archivo");
        itemPreferences = new JMenuItem("Preferencias");
        itemDisconnect = new JMenuItem("Conectar");

        MainController.setPanelEnabled(televisionsPanel, false);
        MainController.setPanelEnabled(customersPanel, false);
        MainController.setPanelEnabled(salesPanel, false);
        MainController.setPanelEnabled(suppliersPanel, false);
        MainController.setPanelEnabled(stockPanel, false);

        menu.add(itemPreferences);
        menu.add(itemDisconnect);

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(menuBar);
    }

    /**
     * Initializes various fields with specific properties such as document size or spinner models.
     */
    private void setupFields() {
        televisionModel.setDocument(new LimitedDocument(100));
        televisionBrand.setDocument(new LimitedDocument(50));
        televisionPrice.setModel(new SpinnerNumberModel(0.0f, 0.0f, 100000.0f, 0.1f));

        customerName.setDocument(new LimitedDocument(50));
        customerSurname.setDocument(new LimitedDocument(50));
        customerMail.setDocument(new LimitedDocument(100));
        customerPhone.setDocument(new LimitedDocument(15));

        saleTotal.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
        saleTotalPrice.setModel(new SpinnerNumberModel(0.0f, 0.0f, 100000.0f, 0.1f));

        supplierName.setDocument(new LimitedDocument(50));
        supplierPhone.setDocument(new LimitedDocument(15));
        supplierAddress.setDocument(new LimitedDocument(255));
        supplierMail.setDocument(new LimitedDocument(100));

        stockTotal.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
    }
}