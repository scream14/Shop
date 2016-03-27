package shop.gui;


import shop.service.ShopService;
import javax.swing.*;
import java.awt.*;


public class ShopFrame extends JFrame {


    private ShopPanel           shopPanel;
    private CustomersPanel      customersPanel;
    private TransactionsPanel   transPanel;
    private ServicePanel        servicePanel;

    private ShopService service;


    public ShopFrame(ShopService service) {

        this.service = service;

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getJMenu());
        setJMenuBar(menuBar);

        shopPanel = new ShopPanel(service);

        setLayout(new BorderLayout());
        add(shopPanel, BorderLayout.CENTER);

        setLocation(300, 100);
        setMinimumSize(new Dimension(600, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pack();
        setVisible(true);
    }

    private JMenu getJMenu() {
        JMenu general = new JMenu("Menu");
        JMenuItem shopMenuItem = new JMenuItem("Shop");
        JMenuItem transMenuItem = new JMenuItem("Transactions");
        JMenuItem customMenuItem = new JMenuItem("Customers");
        JMenuItem serviceMenuItem = new JMenuItem("Service");

        shopMenuItem.addActionListener(e -> changePanel(shopPanel));

        transMenuItem.addActionListener(e -> {
            transPanel = new TransactionsPanel(service);
            changePanel(transPanel);
        });

        customMenuItem.addActionListener(e ->{
            customersPanel = new CustomersPanel(service.getCustomers(), service.getTransactions());
            changePanel(customersPanel);
        });

        serviceMenuItem.addActionListener(e -> {
            servicePanel = new ServicePanel(service, shopPanel);
            changePanel(servicePanel);
        });

        general.add(shopMenuItem);
        general.add(customMenuItem);
        general.add(transMenuItem);
        general.add(serviceMenuItem);

        return general;
    }

    private void changePanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().doLayout();
        revalidate();
        update(getGraphics());
    }

}
