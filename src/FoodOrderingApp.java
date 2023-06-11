import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.AbstractAction;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FoodOrderingApp extends JFrame {

    private JPanel contentPane; // Content pane to hold the buttons and menu
    private JPanel previousPanel; // Variable to store the previous panel
    private boolean isLoggedIn = false; // Login status flag
    private DefaultTableModel cartTableModel; // Table model for cart items
    private int currentPanelIndex = 0; // Current panel index

    public FoodOrderingApp() {
        setTitle("Food Ordering App");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Create and add components to the window
        initComponents();

        // Display the window
        setVisible(true);
    }

    private void initComponents() {
        // Set the layout manager for the content pane
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // Create buttons
        JButton loginButton = new JButton("Login");
        JButton viewMenuButton = new JButton("View Menu");
        JButton addToCartButton = new JButton("Add to Cart");
        JButton placeOrderButton = new JButton("Place Order");
        JButton checkoutButton = new JButton("Checkout");
        JButton reviewFoodButton = new JButton("Review Food");
        JButton feedbackButton = new JButton("Feedback");
        JButton paymentButton = new JButton("Payment");

        // Set the layout manager for the button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4));

        // Add buttons to the button panel
        buttonPanel.add(loginButton);
        buttonPanel.add(viewMenuButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(reviewFoodButton);
        buttonPanel.add(feedbackButton);
        buttonPanel.add(paymentButton);

        // Add the button panel to the content pane
        contentPane.add(buttonPanel, BorderLayout.PAGE_START);

        // Add action listeners to the buttons
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display a dialog for entering password and name
                JTextField nameField = new JTextField();
                JPasswordField passwordField = new JPasswordField();
                Object[] message = {
                        "Name:", nameField,
                        "Password:", passwordField
                };
                int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    String password = new String(passwordField.getPassword());
                    // Perform login authentication here
                    if (name.equals("admin") && password.equals("password")) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        isLoggedIn = true; // Set login status to true
                        displayNextPanel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                    }
                }
            }
        });

        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    currentPanelIndex = 0;
                    displayNextPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please login first!");
                }
            }
        });

        addToCartButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isLoggedIn) {
            // Mark the selected items in the cart table
            int rowCount = cartTableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                boolean isSelected = (boolean) cartTableModel.getValueAt(i, 2);
                if (isSelected) {
                    cartTableModel.setValueAt(true, i, 2);
                }
            }
            currentPanelIndex = 1;
            displayNextPanel();
        } else {
            JOptionPane.showMessageDialog(null, "Please login first!");
        }
    }
});

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    currentPanelIndex = 2;
                    displayNextPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please login first!");
                }
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    currentPanelIndex = 3;
                    displayNextPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please login first!");
                }
            }
        });

        reviewFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    currentPanelIndex = 4;
                    displayNextPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please login first!");
                }
            }
        });

        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    currentPanelIndex = 5;
                    displayNextPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please login first!");
                }
            }
        });
        
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isLoggedIn) {
                    JOptionPane.showMessageDialog(null, "Please login first.");
                    return;
                }
                // Display payment options
                String[] options = {"Credit Card", "PayPal", "Cash"};
                String selectedOption = (String) JOptionPane.showInputDialog(
                        null,
                        "Select a payment method:",
                        "Payment",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (selectedOption != null) {
                    // Process the selected payment method
                    JOptionPane.showMessageDialog(null, "Payment method selected: " + selectedOption);
                }
            }
        });
    }

    private void displayNextPanel() {
        JPanel[] panels = createPanels();

        if (previousPanel != null) {
            contentPane.remove(previousPanel); // Remove the previous panel
        }

        JPanel currentPanel = panels[currentPanelIndex];

        if (currentPanelIndex == 2) {
            // Update the place order panel with selected items from the cart
            if (cartTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Please add items to the cart first!");
                currentPanelIndex = 1;
                currentPanel = panels[currentPanelIndex];
            } else {
                JPanel placeOrderPanel = createPlaceOrderPanel();
                currentPanel = placeOrderPanel;
            }
        } else if (currentPanelIndex == 3) {
            // Update the checkout panel with the total bill
            JPanel checkoutPanel = createCheckoutPanel(cartTableModel);
            currentPanel = checkoutPanel;
        }

        contentPane.add(currentPanel, BorderLayout.CENTER); // Add the current panel

        // Store the current panel as the previous panel
        previousPanel = currentPanel;

        contentPane.revalidate(); // Revalidate the frame to reflect the changes
        contentPane.repaint(); // Repaint the frame
        
    }

    private JPanel[] createPanels() {
        // Create the view menu panel
        JPanel viewMenuPanel = new JPanel();
        viewMenuPanel.setPreferredSize(new Dimension(400, 300));
        viewMenuPanel.setLayout(new BorderLayout());

        String[] columnNames = {"Item", "Price"};
        Object[][] data = {
                {"CHEESEBURGER ", "$8"},
                {"CHICKEN NUGGETS ", "$5"},
                {"CHICKEN SANDWICH", "$10"},
                {"SOFT COOKIES ", "$3"},
                {"DONUTS ", "$7"},
                {"BURGER KING", "$20"},
                {"MOUSSE", "$6"}
        };

        JTable menuTable = new JTable(data, columnNames);
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        viewMenuPanel.add(menuScrollPane, BorderLayout.CENTER);

        // Create the cart panel
        JPanel cartPanel = new JPanel();
        cartPanel.setPreferredSize(new Dimension(400, 300));
        cartTableModel = new DefaultTableModel(new Object[][]{
                {"CHEESEBURGER ", "$8",true},
                {"CHICKEN NUGGETS ", "$5",true},
                {"CHICKEN SANDWICH", "$10",true},
                {"SOFT COOKIES ", "$3",true},
                {"DONUTS ", "$7",true},
                {"BURGER KING", "$20",true},
                {"MOUSSE", "$6",true}
        }, new String[]{"Item", "Price", "Select"}) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class; // Set the third column to Boolean type for the select buttons
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only allow editing the select column
            }
        };
        JTable cartTable = new JTable(cartTableModel);
        cartTable.getColumnModel().getColumn(2).setCellRenderer(cartTable.getDefaultRenderer(Boolean.class));
        cartTable.getColumnModel().getColumn(2).setCellEditor(cartTable.getDefaultEditor(Boolean.class));
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartScrollPane);
        JButton doneButton = new JButton("Done");
        cartPanel.add(doneButton);

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanelIndex = 2;
                displayNextPanel();
            }
        });

        // Create the place order panel
        JPanel placeOrderPanel = new JPanel();
        placeOrderPanel.setPreferredSize(new Dimension(400, 300));
        placeOrderPanel.add(new JLabel("Selected Items:"));

        // Create the checkout panel
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BorderLayout());

        // Create a table model for the selected items
        DefaultTableModel selectedItemsTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Item", "Price"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for the table cells
            }
        };
        JTable selectedItemsTable = new JTable(selectedItemsTableModel);

        // Create a scroll pane for the selected items table
        JScrollPane selectedItemsScrollPane = new JScrollPane(selectedItemsTable);
        checkoutPanel.add(selectedItemsScrollPane, BorderLayout.CENTER);

        // Calculate the total bill
        double totalBill = 0.0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            boolean isSelected = (boolean) cartTableModel.getValueAt(i, 2);
            if (isSelected) {
                String item = (String) cartTableModel.getValueAt(i, 0);
                String price = (String) cartTableModel.getValueAt(i, 1);
                selectedItemsTableModel.addRow(new Object[]{item, price});
                double itemPrice = Double.parseDouble(price.substring(1)); // Remove the "$" sign and parse as double
                totalBill += itemPrice;
            }
        }

        JLabel totalBillLabel = new JLabel("Total Bill: $" + totalBill);
        checkoutPanel.add(totalBillLabel, BorderLayout.SOUTH);

        JPanel reviewFoodPanel = new JPanel();
        reviewFoodPanel.setPreferredSize(new Dimension(400, 300));
        reviewFoodPanel.setLayout(new GridLayout(4, 1));
        reviewFoodPanel.add(new JLabel("Review Food:"));

        // Create a ButtonGroup to ensure only one checkbox can be selected
        ButtonGroup reviewFoodGroup = new ButtonGroup();

        // Create the checkboxes for review options
        JRadioButton unsatisfactoryRadioButton = new JRadioButton("Unsatisfactory");
        JRadioButton goodRadioButton = new JRadioButton("Good");
        JRadioButton satisfactoryRadioButton = new JRadioButton("Satisfactory");

        // Add the checkboxes to the review food panel
        reviewFoodPanel.add(unsatisfactoryRadioButton);
        reviewFoodPanel.add(goodRadioButton);
        reviewFoodPanel.add(satisfactoryRadioButton);

        // Add the checkboxes to the ButtonGroup
        reviewFoodGroup.add(unsatisfactoryRadioButton);
        reviewFoodGroup.add(goodRadioButton);
        reviewFoodGroup.add(satisfactoryRadioButton);

        // Create the feedback panel
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setPreferredSize(new Dimension(400, 300));
        feedbackPanel.setLayout(new BorderLayout());
        feedbackPanel.add(new JLabel("Feedback:"), BorderLayout.NORTH);

        // Create a text area for the feedback
        JTextArea feedbackTextArea = new JTextArea();
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea);
        feedbackPanel.add(feedbackScrollPane, BorderLayout.CENTER);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        feedbackPanel.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String feedback = feedbackTextArea.getText();
                JOptionPane.showMessageDialog(null, "Thank you for your feedback!");
            }
        });

        return new JPanel[]{viewMenuPanel, cartPanel, placeOrderPanel, checkoutPanel, reviewFoodPanel, feedbackPanel};
    }

private JPanel createPlaceOrderPanel() {
    JPanel placeOrderPanel = new JPanel();
    placeOrderPanel.setPreferredSize(new Dimension(400, 300));
    placeOrderPanel.setLayout(new BorderLayout());
    placeOrderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Create a panel for customer information
    JPanel customerPanel = new JPanel(new GridLayout(3, 2, 5, 5));
    customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));

    // Create labels for name and address
    JLabel nameLabel = new JLabel("Name:");
    JLabel addressLabel = new JLabel("Address:");
    
    

    // Create text fields for name and address
    JTextField nameField = new JTextField();
    JTextField addressField = new JTextField();
    
    JButton doneButton = new JButton("Done");


    // Add labels and fields to the customer panel
    customerPanel.add(nameLabel);
    customerPanel.add(nameField);
    customerPanel.add(addressLabel);
    customerPanel.add(addressField);
    customerPanel.add(doneButton);
    
    
    
   
    
    // Create a panel for selected items
    JPanel selectedItemsPanel = new JPanel(new BorderLayout());
    selectedItemsPanel.setBorder(BorderFactory.createTitledBorder("Selected Items"));

    // Create a table model for the selected items
    DefaultTableModel selectedItemsTableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Item", "Price", "Delete"} // Add the "Delete" column
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 2; // Enable editing for the delete column only
        }
        
    };
    JTable selectedItemsTable = new JTable(selectedItemsTableModel);

    // Create a scroll pane for the selected items table
    JScrollPane selectedItemsScrollPane = new JScrollPane(selectedItemsTable);
    selectedItemsPanel.add(selectedItemsScrollPane, BorderLayout.CENTER);

    // Add the customer panel and selected items panel to the main panel
    placeOrderPanel.add(customerPanel, BorderLayout.NORTH);
    placeOrderPanel.add(selectedItemsPanel, BorderLayout.CENTER);
    
doneButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the name or address fields are empty
        String name = nameField.getText();
        String address = addressField.getText();
        if (name.isEmpty() || address.isEmpty()) {
            // Display a message dialog asking the user to enter the information
            JOptionPane.showMessageDialog(placeOrderPanel, "Please enter name and address.", "Incomplete Information", JOptionPane.WARNING_MESSAGE);
        } else {
            // Perform the necessary actions when the "Done" button is clicked and the information is valid
            // For example, display a message dialog
            JOptionPane.showMessageDialog(placeOrderPanel, "Information Entered successfully!");

            // You can add your code here to handle the completion of the order
        }
    }
});
    // Add only the selected items to the table and calculate the total bill
    double totalBill = 0.0;
    for (int i = 0; i < cartTableModel.getRowCount(); i++) {
        boolean isSelected = (boolean) cartTableModel.getValueAt(i, 2);
        if (isSelected) {
            String item = (String) cartTableModel.getValueAt(i, 0);
            String price = (String) cartTableModel.getValueAt(i, 1);
            selectedItemsTableModel.addRow(new Object[]{item, price});
            double itemPrice = Double.parseDouble(price.substring(1)); // Remove the "$" sign and parse as double
            totalBill += itemPrice;
        }
    }

    // Create a panel for the total bill
    JPanel totalBillPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    totalBillPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    JLabel totalBillLabel = new JLabel("Total Bill: $" + totalBill);
    totalBillPanel.add(totalBillLabel);

    // Add the total bill panel to the main panel
    placeOrderPanel.add(totalBillPanel, BorderLayout.SOUTH);
    
    // Add a delete button column to the selected items table
    ButtonColumn deleteButtonColumn = new ButtonColumn(selectedItemsTable, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int modelRow = Integer.valueOf(e.getActionCommand());
            String deletedItem = (String) selectedItemsTableModel.getValueAt(modelRow, 0);
            selectedItemsTableModel.removeRow(modelRow);
         
            // Update the total bill after removing an item
            double updatedTotalBill = 0.0;
            for (int row = 0; row < selectedItemsTableModel.getRowCount(); row++) {
                String price = (String) selectedItemsTableModel.getValueAt(row, 1);
                double itemPrice = Double.parseDouble(price.substring(1)); // Remove the "$" sign and parse as double
                updatedTotalBill += itemPrice;
            }
            totalBillLabel.setText("Total Bill: $" + updatedTotalBill);

            // Remove the deleted item from the cartTableModel as well
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                String item = (String) cartTableModel.getValueAt(i, 0);
                if (item.equals(deletedItem)) {
                    cartTableModel.setValueAt(false, i, 2); // Unselect the deleted item in the cartTableModel
                    break;
                }
            }
        }
    }, 2); // Specify the delete button column index as 2

    return placeOrderPanel;
}
private boolean storeCustomerInfo(String Name, String Address) {
    try {
       
        Connection connection = DriverManager.getConnection("jdbc:mysql:foodmunch//localhost:3307/foodmunch");

        String query = "INSERT INTO customerinfo (Name, Address) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, Name);
        statement.setString(2, Address);

        int rowsInserted = statement.executeUpdate();

        statement.close();
        connection.close();

        return rowsInserted > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JTable table;
    private Action action;
    private int mnemonic;
    private Border originalBorder;
    private Border focusBorder;

    private JButton renderButton;
    private JButton editButton;
    private Object editorValue;
    private boolean isButtonColumnEditor;

    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;

        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        originalBorder = editButton.getBorder();
        setFocusBorder(new LineBorder(Color.BLUE));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (table.isEditing() && table.getCellEditor() == ButtonColumn.this) {
                    isButtonColumnEditor = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isButtonColumnEditor && table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }
                isButtonColumnEditor = false;
            }
        });
    }

    public int getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(int mnemonic) {
        this.mnemonic = mnemonic;
        renderButton.setMnemonic(mnemonic);
        editButton.setMnemonic(mnemonic);
    }

    public Border getFocusBorder() {
        return focusBorder;
    }

    public void setFocusBorder(Border focusBorder) {
        this.focusBorder = focusBorder;
        editButton.setBorder(focusBorder);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) {
            editButton.setText("");
            editButton.setIcon(null);
        } else if (value instanceof Icon) {
            editButton.setText("");
            editButton.setIcon((Icon) value);
        } else {
            editButton.setText(value.toString());
            editButton.setIcon(null);
        }
        this.editorValue = value;
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();
        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, String.valueOf(row));
        action.actionPerformed(event);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        if (hasFocus) {
            renderButton.setBorder(focusBorder);
        } else {
            renderButton.setBorder(originalBorder);
        }

        if (value == null) {
            renderButton.setText("");
            renderButton.setIcon(null);
        } else if (value instanceof Icon) {
            renderButton.setText("");
            renderButton.setIcon((Icon) value);
        } else {
            renderButton.setText(value.toString());
            renderButton.setIcon(null);
        }

        return renderButton;
    }
}

private JPanel createCheckoutPanel(DefaultTableModel cartTableModel) {
    JPanel checkoutPanel = new JPanel();
    checkoutPanel.setLayout(new BorderLayout());

    // Create a table model for the selected items
    DefaultTableModel selectedItemsTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Item", "Price"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Disable editing for the table cells
        }
    };
    JTable selectedItemsTable = new JTable(selectedItemsTableModel);

    // Create a scroll pane for the selected items table
    JScrollPane selectedItemsScrollPane = new JScrollPane(selectedItemsTable);
    checkoutPanel.add(selectedItemsScrollPane, BorderLayout.CENTER);

    // Calculate the total bill
    double totalBill = 0.0;
    for (int i = 0; i < cartTableModel.getRowCount(); i++) {
        boolean isSelected = (boolean) cartTableModel.getValueAt(i, 2);
        if (isSelected) {
            String item = (String) cartTableModel.getValueAt(i, 0);
            String price = (String) cartTableModel.getValueAt(i, 1);
            selectedItemsTableModel.addRow(new Object[]{item, price});
            double itemPrice = Double.parseDouble(price.substring(1)); // Remove the "$" sign and parse as double
            totalBill += itemPrice;
        }
    }

    JLabel totalBillLabel = new JLabel("Total Bill: $" + totalBill);
    checkoutPanel.add(totalBillLabel, BorderLayout.EAST);
    JButton confirmOrderButton = new JButton("Confirm Order");
    checkoutPanel.add(confirmOrderButton, BorderLayout.PAGE_END);

    confirmOrderButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to confirm the order?", "Confirm Order", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Order confirmed!");
            } else {
                JOptionPane.showMessageDialog(null, "Order Canceled!");
            }
        }
    });

    // Add a listener to the cartTableModel to remove deleted items from the selectedItemsTableModel
    cartTableModel.addTableModelListener(new TableModelListener() {
        @Override
public void tableChanged(TableModelEvent e) {
    if (e.getType() == TableModelEvent.DELETE) {
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        
        // Remove the corresponding rows from the selectedItemsTableModel
        for (int i = lastRow; i >= firstRow; i--) {
            selectedItemsTableModel.removeRow(i);
        }
    }
}
    });

    return checkoutPanel;
}



class MenuItem {
    private String name;
    private String price;

    public MenuItem(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

interface MenuItemDao {
    List<MenuItem> getAllMenuItems();
    void addMenuItem(MenuItem item);
    void deleteMenuItem(MenuItem item);
}

class MenuItemDAOImpl implements MenuItemDao {
    private List<MenuItem> menuItems;

    public MenuItemDAOImpl() {
        // Initialize the menuItems list or retrieve data from a database
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("CHEESE BURGER", "$8"));
        menuItems.add(new MenuItem("CHICKEN NUGGETS ", "$5"));
        menuItems.add(new MenuItem("CHICKEN SANDWICH", "$10"));
        menuItems.add(new MenuItem("SOFT COOKIES ", "$3"));
        menuItems.add(new MenuItem("DONUTS", "$7"));
        menuItems.add(new MenuItem("BURGER KING", "$20"));
        menuItems.add(new MenuItem("MOUSSE", "$6"));
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItems;
    }

    @Override
    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    @Override
    public void deleteMenuItem(MenuItem item) {
        menuItems.remove(item);
    }
}

public static void main(String[] args) {
        
  
        SwingUtilities.invokeLater(() -> {
            FoodOrderingApp app = new FoodOrderingApp();
        });
    }
}